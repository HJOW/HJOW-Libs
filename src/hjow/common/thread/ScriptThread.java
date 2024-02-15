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

import hjow.common.script.HScriptEngine;

public class ScriptThread extends HThread {
    private static final long serialVersionUID = -9199571237633508151L;
    protected transient String scriptOnThread, scriptOnEnd;
    protected transient HScriptEngine engine;
    public ScriptThread() {
        
    }
    
    public ScriptThread(String scriptOnThread, String scriptOnEnd, HScriptEngine engine) {
        this.engine = engine;
        this.scriptOnThread = scriptOnThread;
        this.scriptOnEnd = scriptOnEnd;
        this.onThread = new RiskyRunnable() {
            @Override
            public void run() throws Throwable {
                if(getEngine() == null) {
                    stop();
                    return;
                }
                getEngine().eval(getScriptOnThread());
            }
        };
        this.onFinished = new RiskyRunnable() {
            @Override
            public void run() throws Throwable {
                if(getEngine() == null) return;
                if(getScriptOnEnd() == null) return;
                getEngine().eval(getScriptOnEnd());
            }
        };
    }
    
    protected HScriptEngine getEngine() {
        return engine;
    }
    
    protected String getScriptOnThread() {
        return scriptOnThread;
    }
    
    protected String getScriptOnEnd() {
        return scriptOnEnd;
    }
    
    @Override
    public void releaseResource() {
        super.releaseResource();
        this.engine = null;
    }
}
