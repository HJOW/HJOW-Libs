package org.duckdns.hjow.commons.stream;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.duckdns.hjow.commons.core.Disposeable;

/** 여러 쓰레드로 동시 작업 후, 쓰레드 모두 종료 시까지 대기 */
public class SimultaneousWork implements Disposeable {
    protected Vector<SingleWork> works = new Vector<SingleWork>();
    protected transient Vector<Throwable> throwable = new Vector<Throwable>();
    protected transient volatile boolean fStarted = false;
    protected transient volatile boolean fEnded   = false;
    protected transient volatile boolean threadSwitch = false;
    
    /** 동시 처리할 작업 지정해 객체 생성 */
    public SimultaneousWork(List<SingleAction> processes) {
    	int key = 0;
    	for(SingleAction r : processes) {
    		works.add(new SingleWork(this, r, key));
    		key++;
    	}
    }
    
    /** 동시 처리할 작업 지정해 객체 생성, 반복 횟수도 같이 지정 */
    public SimultaneousWork(int loop, List<SingleAction> processes) {
    	int key = 0;
    	for(SingleAction r : processes) {
    		works.add(new SingleWork(this, r, loop, key));
    		key++;
    	}
    }
    
    /** 동시 처리할 작업 지정해 객체 생성 */
    public SimultaneousWork(SingleAction ... processes) {
    	int key = 0;
    	for(SingleAction r : processes) {
    		works.add(new SingleWork(this, r, key));
    		key++;
    	}
    }
    
    /** 동시 처리할 작업 지정해 객체 생성, 반복 횟수도 같이 지정 */
    public SimultaneousWork(int loop, SingleAction ... processes) {
    	int key = 0;
    	for(SingleAction r : processes) {
    		works.add(new SingleWork(this, r, loop, key));
    		key++;
    	}
    }
    
    /** 등록된 작업 모두 시작, 이후 작업이 모두 완료될 때까지 대기 (작업 모두 완료 시까지 리턴되지 않음) */
    public synchronized void start() {
    	if(fStarted) throw new RuntimeException("These works are already started.");
    	fStarted = true;
    	threadSwitch = true;
    	
    	// 작업 모두 시작
    	Vector<SingleWork> works = new Vector<SingleWork>();
    	works.addAll(this.works);
    	for(SingleWork w : works) {
    		w.start();
    	}
    	works = null;
    	
    	// 다른 작업들 모두 완료 시까지 대기
    	waiting();
    	
    	// 작업 목록 비우기
    	this.works.clear();
    	
    	// 작업 중 예외 발생 건이 있으면, 하나를 골라 다시 발생시키기
    	if(! throwable.isEmpty()) {
    		Throwable excOne = throwable.get(0);
    		if(excOne instanceof RuntimeException) { throw (RuntimeException) excOne; }
    		throw new RuntimeException(excOne.getMessage(), excOne);
    	}
    }
    
    /** 작업 완료 시까지 대기 */
    protected void waiting() {
    	while(threadSwitch) {
    		try { Thread.sleep(50L); } catch(InterruptedException ex) { threadSwitch = false; }
    	}
    }
    
    /** 작업 중단 */
    @Override
    public void dispose() {
    	threadSwitch = false;
    	Vector<SingleWork> works = new Vector<SingleWork>();
    	works.addAll(this.works);
    	for(SingleWork w : works) {
    		w.dispose();
    	}
    	works.clear();
    	this.works.clear();
    }
    
    /** 작업 1건 완료 시 이 메소드가 호출됨 (자식 객체로부터) */
    protected void onWorkEnd(SingleWork work) {
    	// 모든 작업 다 완료됐는지 확인
    	Vector<SingleWork> works = new Vector<SingleWork>();
    	works.addAll(this.works);
    	for(SingleWork w : works) {
    		if(! w.isEnded()) return; // 하나라도 완료 안됐으면 중단
    	}
    	
    	// 모두 완료 - 제일 마지막에 호출됨
    	//     작업 완료 대기 중단시키기
    	threadSwitch = false;
    	
    	if(fEnded) return; // 완료 처리는 단 한번만 호출되도록 보장
    	fEnded = true;
    	
    	//     예외 발생 여부 체크
		for(SingleWork w : works) {
			Throwable t = w.getException();
			if(t != null) throwable.add(t); // 예외 발생 시 일단 리스트에 담기
			
			w.dispose(); // 순환 참조 제거
		}
		works.clear(); // 작업 목록 비우기
    }
}

/** 작업 쓰레드 하나 */
class SingleWork implements Disposeable, Serializable {
	private static final long serialVersionUID = 5631487702581254137L;
	protected volatile int key = 0;
	
	protected transient volatile boolean threadSwitch = false;
	protected transient volatile boolean flagEnd = false;
	protected transient volatile int     cycleNow = 0;
	protected transient volatile Throwable exception = null;
	protected transient Thread thread;
	
	public SingleWork(SimultaneousWork superInstances, SingleAction r, int key) {
		this(superInstances, r, 1, key);
	}
	
	public SingleWork(final SimultaneousWork superInstances, final SingleAction r, final int cycleMax, final int key) {
		this.key = key;
		this.thread = new Thread(new Runnable() {
			@Override
			public void run() {
				SimultaneousWork superInst = superInstances;
				threadSwitch = true;
				flagEnd = false;
				for(cycleNow=0; cycleNow<cycleMax; cycleNow++) {
					if(! threadSwitch) { exception = new RuntimeException("Job interrupted."); break; }
				    try { r.run(cycleNow); } catch(Throwable t) { exception = t; break; }
				}
				flagEnd = true;
				if(superInst == null) return;
				else superInst.onWorkEnd(getSelf());
				superInst = null;
			}
		});
	}
	
	public void start() {
		this.flagEnd = false;
		thread.start();
	}
	
	public boolean isEnded() {
		return flagEnd;
	}
	
	public Throwable getException() {
		return exception;
	}
	
	public int getKey() { return key; }
	public void setKey(int k) { this.key = k; }
	protected SingleWork getSelf() { return this; }

	@Override
	public void dispose() {
		threadSwitch = false;
		if(thread != null) { try { thread.interrupt(); } catch(Throwable ignores) {} }
		thread = null;
	}
}