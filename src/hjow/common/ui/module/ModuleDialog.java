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
package hjow.common.ui.module;

import java.awt.BorderLayout;

import hjow.common.module.Module;
import hjow.common.ui.UI;
import hjow.common.ui.extend.HDialog;

public class ModuleDialog extends HDialog {
	private static final long serialVersionUID = 1833120693223506595L;
	
	protected transient Module module;
	public ModuleDialog(Module module) {
		this.module = module;
		setTitle(module.getName());
		setSize(500, 400);
		
		setLayout(new BorderLayout());
		add(module.getComponent(), BorderLayout.CENTER);
	}
	
	/** 모듈 실행이 호출되었을 때 UI에서 이 메소드가 호출됩니다. */
	public void showFromUI(UI ui) {
		setVisible(true);
		module.initSecond(ui);
	}
}
