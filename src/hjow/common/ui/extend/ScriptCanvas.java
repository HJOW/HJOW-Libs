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
package hjow.common.ui.extend;

import java.awt.Graphics;

import hjow.common.core.Core;
import hjow.common.core.Releasable;
import hjow.common.script.HScriptEngine;

public class ScriptCanvas extends HPanel implements Releasable {
	private static final long serialVersionUID = -5404220987500881602L;
	protected transient HScriptEngine engine;
	protected transient String scriptPaint;
	
	public ScriptCanvas(HScriptEngine engine, String script) {
		this.engine = engine;
		this.scriptPaint = script;
	}
	
	@Override
	public void paint(Graphics g) {
		if(engine == null) {
			super.paint(g);
			return;
		}
		ScriptGraphics gWrap = new ScriptGraphics(g);
		engine.put("graphics", gWrap);
		try { engine.eval(scriptPaint); } catch(Throwable t) { Core.logError(t); }
		gWrap.releaseResource();
		super.paint(g);
	}

	@Override
	public void releaseResource() {
		this.engine = null;
	}
}
