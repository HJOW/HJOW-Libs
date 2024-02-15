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
package hjow.common.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import hjow.common.core.Core;
import hjow.common.module.CustomModule;
import hjow.common.module.Module;
import hjow.common.module.builtin.BuiltinModule;
import hjow.common.ui.extend.HLabel;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HTextArea;
import hjow.common.ui.extend.HTextField;

public class ModuleViewer extends HPanel {
	private static final long serialVersionUID = 1108555224785833143L;
	protected HLabel lbName, lbShortName;
	protected HTextField tfName, tfShortName;
	protected HTextArea taArea;
	public ModuleViewer() {
		setLayout(new BorderLayout());
		
		HPanel panel = new HPanel();
		HPanel[] panels = new HPanel[2];
		
		panel.setLayout(new GridLayout(panels.length, 1));
		
		for(int idx=0; idx<panels.length; idx++) {
			panels[idx] = new HPanel();
			panels[idx].setLayout(new FlowLayout());
			panel.add(panels[idx]);
		}
		
		lbName = new HLabel(Core.trans("Module Name"));
		tfName = new HTextField(15);
		tfName.setEditable(false);
		
		lbShortName = new HLabel(Core.trans("Short Name"));
		tfShortName = new HTextField(15);
		tfShortName.setEditable(false);
		
		panels[0].add(lbName);
		panels[0].add(tfName);
		
		panels[1].add(lbName);
		panels[1].add(tfShortName);
		
		add(panel, BorderLayout.NORTH);
		
		panel = new HPanel();
		panel.setLayout(new BorderLayout());
		
		taArea = new HTextArea();
		taArea.setEditable(false);
		panel.add(taArea, BorderLayout.CENTER);
		
		add(panel, BorderLayout.CENTER);
	}
	
	public void setModule(Module m) {
		tfName.setText(m.getName());
		tfShortName.setText(m.getShortName());
		taArea.setText(getModuleDesc(m));
	}
	
	protected String getModuleDesc(Module m) {
		StringBuilder results = new StringBuilder("");
		results = results.append(m.getDescription()).append("\n\n");
		
		results = results.append("ID : ").append(m.getId()).append("\n");
		
		boolean isCorrect = false;
		if(m instanceof BuiltinModule) isCorrect = true;
		if(m instanceof CustomModule)  isCorrect = ((CustomModule) m).isCorrect(); // 보안을 위해 CustomModule 사용
		results = results.append("Check : ").append(String.valueOf(isCorrect));
		
		return results.toString();
	}
}
