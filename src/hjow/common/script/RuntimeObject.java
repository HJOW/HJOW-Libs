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
package hjow.common.script;

import java.util.ArrayList;
import java.util.List;

import hjow.common.core.Core;
import hjow.common.thread.ScriptThread;
import hjow.common.ui.extend.ScriptCanvas;

public class RuntimeObject extends ScriptObject {
	private static final long serialVersionUID = 6454477454303885291L;
	protected HScriptEngine engine;
	protected transient Core core;
	protected transient List<ScriptCanvas> canvases = new ArrayList<ScriptCanvas>();;
    public RuntimeObject(Core core, HScriptEngine engine) {
    	this.core = core;
    	this.engine = engine;
    	this.canvases = new ArrayList<ScriptCanvas>();
    }
    @Override
    public String getPrefixName() {
        return "runtime";
    }
    @Override
	public void releaseResource() {
		super.releaseResource();
		for(ScriptCanvas c : this.canvases) {
			c.releaseResource();
		}
		this.canvases.clear();
		this.engine = null;
	}
	@Override
	public String getInitScript(String accessKey) {
		StringBuilder initScript = new StringBuilder("");
        
        initScript = initScript.append("function runtime_gc() {                                                                    ").append("\n");
        initScript = initScript.append("    " + getPrefixName() + "_" + accessKey + ".gc();                                        ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function sleep(t) {                                                                        ").append("\n");
        initScript = initScript.append("    " + getPrefixName() + "_" + accessKey + ".sleep(t);                                    ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function runtime_maxMemory() {                                                             ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".maxMemory();                          ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function runtime_freeMemory() {                                                            ").append("\n");
        initScript = initScript.append("    return  " + getPrefixName() + "_" + accessKey + ".freeMemory();                        ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function runtime_totalMemory() {                                                           ").append("\n");
        initScript = initScript.append("    return  " + getPrefixName() + "_" + accessKey + ".totalMemory();                       ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function tryWithoutReturn(sc) {                                                            ").append("\n");
        initScript = initScript.append("    return  " + getPrefixName() + "_" + accessKey + ".tryWithoutReturn(sc);                ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function subEngine() {                                                                     ").append("\n");
        initScript = initScript.append("    return  " + getPrefixName() + "_" + accessKey + ".subEngine();                         ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function newThread(a, b) {                                                                 ").append("\n");
        initScript = initScript.append("    return  " + getPrefixName() + "_" + accessKey + ".subEngine(a, b);                     ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
        initScript = initScript.append("function ui_createCanvas(sc) {                                                             ").append("\n");
        initScript = initScript.append("    return  " + getPrefixName() + "_" + accessKey + ".createCanvas(sc);                    ").append("\n");
        initScript = initScript.append("};                                                                                         ").append("\n");
		return initScript.toString();
	}
	
	public void gc() {
		System.gc();
	}
	
	public void sleep(Object timeMillsObj) throws InterruptedException {
		long millis = 1;
		if(timeMillsObj == null) {
			millis = 100;
		} else if(timeMillsObj instanceof Number) {
			millis = ((Number) timeMillsObj).longValue();
		} else if(String.valueOf(timeMillsObj).equals("undefined")) {
			millis = new Long(String.valueOf(timeMillsObj));
		} else {
			millis = new Long(String.valueOf(timeMillsObj));
		}
		Thread.sleep(millis);
	}
	
	public Long maxMemory() {
		return new Long(Runtime.getRuntime().maxMemory());
	}
	
	public Long freeMemory() {
		return new Long(Runtime.getRuntime().freeMemory());
	}
	
	public Long totalMemory() {
		return new Long(Runtime.getRuntime().totalMemory());
	}
	
	public String tryWithoutReturn(Object scripts) {
		try { engine.eval(String.valueOf(scripts)); } catch(Throwable t) { return t.getMessage(); }
		return null;
	}
	
	public HScriptEngine subEngine() {
		return engine.makeChild(core);
	}
	
	public ScriptThread newThread(Object scriptsOnThread, Object scriptOnEnd) {
		String onThread = String.valueOf(scriptsOnThread);
		String onEnd = scriptOnEnd == null ? null : String.valueOf(scriptOnEnd);
		ScriptThread thread = core.newThread(engine, "On Script-Runtime called", onThread, onEnd);
		return thread;
	}
	
	public ScriptCanvas createCanvas(Object scripts) {
		ScriptCanvas canvasObj = new ScriptCanvas(engine, String.valueOf(scripts));
		canvases.add(canvasObj);
		return canvasObj;
	}
}
