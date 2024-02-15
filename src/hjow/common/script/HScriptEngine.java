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

import java.util.List;
import java.util.Vector;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import hjow.common.core.Core;
import hjow.common.core.Releasable;
import hjow.common.module.Module;

public class HScriptEngine extends PublicMethodOpenedClass implements Releasable {
	private static final long serialVersionUID = 1282803975720178261L;
	protected transient ScriptEngine engine;
	protected transient List<HScriptEngine> childs;
	
	protected String       name;
	protected long         moduleId = -1;
	
	
	public HScriptEngine(ScriptEngine engine) {
		this.engine = engine;
		this.childs = new Vector<HScriptEngine>();
	}
	
	public HScriptEngine(ScriptEngine engine, String name, Module m) {
		this(engine);
		this.name = name.toString();
		if(m != null) this.moduleId = m.getId();
	}
	
	public String getName() {
		return name;
	}
	
	public long getModuleId() {
		return moduleId;
	}
	
	public void put(Object variableName, Object obj) {
		engine.put(String.valueOf(variableName), obj);
	}
	
	public Object eval(Object scripts) throws ScriptException {
		return engine.eval(String.valueOf(scripts));
	}
	
	public void setInfoFromModule(Module m) {
		this.moduleId = m.getId();
		this.name     = "Main of " + m.getName();
	}
	
	public HScriptEngine makeChild(Core core) {
		HScriptEngine newEngine = core.newEngine("Child of " + getName());
		this.childs.add(newEngine);
		return newEngine;
	}

	@Override
	public void releaseResource() {
		if(this.childs != null) {
			for(HScriptEngine childOne : this.childs) {
				if(childOne == null) continue;
				childOne.releaseResource();
			}
			this.childs.clear();
			this.childs = null;
		}
		
		if(this.engine != null) {
			try {
				Bindings bindingCol = this.engine.getBindings(ScriptContext.ENGINE_SCOPE);
				bindingCol.clear();
			} catch(Throwable ignores) {}
		}
		this.engine = null;
	}
	
	public boolean isAlive() {
		return (this.engine != null);
	}
}
