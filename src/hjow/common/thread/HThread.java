/*
 
 Copyright 2019 HJOW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 
 */
package hjow.common.thread;

import java.util.Random;

import hjow.common.core.Core;
import hjow.common.core.Releasable;
import hjow.common.exception.ThreadRunningYetException;

public class HThread implements Releasable, Runnable {
    private static final long serialVersionUID = 3038317792699851805L;
    
    protected transient long randomNo = new Random().nextLong();
    
    protected transient RiskyRunnable onThread, onFinished;
    protected transient boolean switches = false;
    protected transient boolean aliveness = false;
    protected transient boolean pauseSwitch = false;
    protected transient long gap = 100;
    protected transient Thread thread;
    
    protected transient int  countSuccess   = 0;
    protected transient int  countFail      = 0;
    protected transient long startTimeMills = 0;
    protected transient long timeMillVal    = 0;
    protected transient long timeMillGap    = 0;
    protected transient long timeMillMin    = 0;
    protected transient long timeMillMax    = 0;
    protected transient Throwable lastException = null;
    
    protected String name;
    
    public HThread() {
        
    }
    
    public HThread(RiskyRunnable onThread, RiskyRunnable onFinished) {
        this.onThread = onThread;
        this.onFinished = onFinished;
    }
    
    @Override
    public void releaseResource() {
        stop();
        thread = null;
    }
    
    /** 쓰레드를 중단시킵니다. 즉시 모든 작업이 중단되지는 않고, 현재 실행중인 작업들이 끝나고 중단됩니다. */
    public void stop() {
        switches = false;
    }
    
    /** 쓰레드 작동을 개시합니다. */
    public void start() {
        if(isAlive()) throw new ThreadRunningYetException("This thread is running yet.");
        switches = true;
        aliveness = true;
        thread = new Thread(this);
        thread.start();
        
        startTimeMills = System.currentTimeMillis();
        timeMillVal    = startTimeMills;
        timeMillGap    = timeMillVal - startTimeMills;
        timeMillMax    = timeMillGap;
        timeMillMin    = timeMillGap;
    }
    
    /** 쓰레드 중단 예약이 되어 있는지를 반환합니다. */
    public boolean isSwitchOn() {
        return switches;
    }
    
    /** 쓰레드 객체 활성화 여부를 반환합니다. */
    public boolean isAlive() {
        return aliveness;
    }

    @Override
    public void run() {
        while(switches) {
            if(! pauseSwitch) {
                try {
                    startTimeMills = System.currentTimeMillis();
                    if(onThread != null) {
                        onThread.run();
                    }
                    
                    timeMillVal = System.currentTimeMillis();
                    timeMillGap = timeMillVal - startTimeMills;
                    if(timeMillMax < timeMillGap) timeMillMax = timeMillGap;
                    if(timeMillMin > timeMillGap) timeMillMin = timeMillGap;
                    if(timeMillMin < 1) timeMillMin = 1;
                    
                    countSuccess++;
                } catch(Throwable t) {
                    Core.logError(t);
                    countFail++;
                    lastException = t;
                }
                if(countSuccess + countFail >= Integer.MAX_VALUE - 10) {
                    countSuccess = countSuccess / 2;
                    countFail    = countFail    / 2;
                }
                if(Core.checkInterrupt(this.getClass(), "On thread " + getName())) {
                    switches = false;
                    aliveness = false;
                    return; // 인터럽트 발생 시 onFinished 액션을 취하지 않고 바로 종료함
                }
            }
            try { Thread.sleep(gap); } catch(Throwable t) { Core.logError(t); }
        }
        if(onFinished != null) {
            try { onFinished.run(); } catch(Throwable t) { Core.logError(t); }
        }
        switches = false;
        aliveness = false;
    }
    
    /** 마지막 회차 실행 시간을 Millisecond 단위로 반환합니다. */
    public long getLastElapsedTime() {
        return timeMillGap;
    }
    
    /** 가장 오래 실행한 회차 실행 시간을 Millisecond 단위로 반환합니다. */
    public long getMaxElapsedTime() {
        return timeMillMax;
    }
    
    /** 가장 짧게 실행한 회차 실행 시간을 Millisecond 단위로 반환합니다. */
    public long getMinElapsedTime() {
        return timeMillMin;
    }

    /** 쓰레드 이름을 반환합니다. */
    public String getName() {
        return name;
    }

    /** 쓰레드 이름을 지정합니다. */
    public void setName(String name) {
        this.name = name;
    }
    
    /** 프로그램 종료 시 Core 에서 호출됨 */
    public RiskyRunnable getFinishJob() {
        return onFinished;
    }
    
    /** 쓰레드 일시 정지 */
    public void pause() {
        pauseSwitch = true;
    }
    
    /** 쓰레드 일시정지 해제 */
    public void resume() {
        pauseSwitch = false;
    }
    
    /** 일시 정지 여부 반환 */
    public boolean isPaused() {
        return pauseSwitch;
    }
    
    /** 작업 성공률 반환 */
    public double getSuccessfulRatio() {
        if(countSuccess + countFail == 0) return 0.0;
        return ((double) countSuccess) / (((double) countSuccess) + ((double) countFail));
    }
    
    /** 마지막 발생한 예외 반환 */
    public Throwable getLastException() {
        return lastException;
    }
    
    /** 쓰레드 실행 주기를 Millisecond 단위로 설정합니다. */
    public void setGap(long gap) {
        if(gap < 5) gap = 5;
        this.gap = gap;
    }
}
