package org.duckdns.hjow.commons.stream;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.duckdns.hjow.commons.core.Disposeable;

/** 여러 쓰레드로 동시 작업 후, 쓰레드 모두 종료 시까지 대기 */
public class SwingSimultaneousWork implements Disposeable  {
	protected Vector<SwingSingleAction> works = new Vector<SwingSingleAction>();
	protected transient Vector<Throwable> throwable = new Vector<Throwable>();
    protected transient volatile boolean fStarted = false;
    protected transient volatile boolean fEnded   = false;
    protected transient volatile boolean threadSwitch = false;
    
    /** 동시 처리할 작업 지정해 객체 생성 */
    public SwingSimultaneousWork(List<SingleAction> processes) {
    	int key = 0;
    	for(SingleAction r : processes) {
    		SwingSingleAction s = new SwingSingleAction() {
				@Override
				protected Serializable doInBackground() throws Exception {
					try { getAction().run(getKey()); } catch(Throwable t) { throw new RuntimeException(t.getMessage(), t); }
					return null;
				}
    		};
    		s.setAction(r);
    		s.setKey(key);
    		works.add(s);
    		key++;
    	}
    }
    
    /** 동시 처리할 작업 지정해 객체 생성, 반복 횟수도 같이 지정 */
    public SwingSimultaneousWork(int loop, List<SingleAction> processes) {
    	int key = 0;
    	for(SingleAction r : processes) {
    		SwingSingleAction s = new SwingSingleAction() {
				@Override
				protected Serializable doInBackground() throws Exception {
					try { getAction().run(getKey()); } catch(Throwable t) { throw new RuntimeException(t.getMessage(), t); }
					return null;
				}
    		};
    		s.setAction(r);
    		s.setKey(key);
    		works.add(s);
    		key++;
    	}
    }
    
    /** 동시 처리할 작업 지정해 객체 생성 */
    public SwingSimultaneousWork(SingleAction ... processes) {
    	int key = 0;
    	for(SingleAction r : processes) {
    		SwingSingleAction s = new SwingSingleAction() {
				@Override
				protected Serializable doInBackground() throws Exception {
					try { getAction().run(getKey()); } catch(Throwable t) { throw new RuntimeException(t.getMessage(), t); }
					return null;
				}
    		};
    		s.setAction(r);
    		s.setKey(key);
    		works.add(s);
    		key++;
    	}
    }
    
    /** 동시 처리할 작업 지정해 객체 생성, 반복 횟수도 같이 지정 */
    public SwingSimultaneousWork(int loop, SingleAction ... processes) {
    	int key = 0;
    	for(SingleAction r : processes) {
    		SwingSingleAction s = new SwingSingleAction() {
				@Override
				protected Serializable doInBackground() throws Exception {
					try { getAction().run(getKey()); } catch(Throwable t) { throw new RuntimeException(t.getMessage(), t); }
					return null;
				}
    		};
    		s.setAction(r);
    		s.setKey(key);
    		works.add(s);
    		key++;
    	}
    }
    
    /** 작업 완료 시까지 대기 */
    protected void waiting() {
    	while(threadSwitch) {
    		try { Thread.sleep(50L); } catch(InterruptedException ex) { threadSwitch = false; }
    	}
    }
    
    /** 등록된 작업 모두 시작, 이후 작업이 모두 완료될 때까지 대기 (작업 모두 완료 시까지 리턴되지 않음) */
    public synchronized void start() {
    	if(fStarted) throw new RuntimeException("These works are already started.");
    	fStarted = true;
    	threadSwitch = true;
    	
    	// 작업 모두 시작
    	for(SwingSingleAction w : works) {
    		w.execute();
    	}
    	
    	// 다른 작업들 모두 완료 시까지 대기
    	waiting();
    	
    	// 작업 목록 비우기
    	works.clear();
    	
    	// 작업 중 예외 발생 건이 있으면, 하나를 골라 다시 발생시키기
    	if(! throwable.isEmpty()) {
    		Throwable excOne = throwable.get(0);
    		if(excOne instanceof RuntimeException) { throw (RuntimeException) excOne; }
    		throw new RuntimeException(excOne.getMessage(), excOne);
    	}
    }
    
    /** 작업 중단 */
    @Override
    public void dispose() {
    	threadSwitch = false;
    	for(SwingSingleAction w : works) {
    		try { w.cancel(true); } catch(Throwable tx) {}
    	}
    	works.clear();
    }
    
    /** 작업 1건 완료 시 이 메소드가 호출됨 (자식 객체로부터) */
    protected void onWorkEnd(SwingSingleAction work) {
    	// 모든 작업 다 완료됐는지 확인
    	for(SwingSingleAction w : works) {
    		if(! w.isDone()) return; // 하나라도 완료 안됐으면 중단
    	}
    	
    	// 모두 완료 - 제일 마지막에 호출됨
    	//     작업 완료 대기 중단시키기
    	threadSwitch = false;
    	
    	if(fEnded) return; // 완료 처리는 단 한번만 호출되도록 보장
    	fEnded = true;
    	
		works.clear(); // 작업 목록 비우기
    }
}