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
package hjow.common.module.builtin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import hjow.common.core.Core;
import hjow.common.module.CustomAbstractModule;
import hjow.common.module.CustomModule;
import hjow.common.module.Module;
import hjow.common.ui.JsonPane;
import hjow.common.ui.SwingUI;
import hjow.common.ui.UI;
import hjow.common.ui.extend.HButton;
import hjow.common.ui.extend.HComboBox;
import hjow.common.ui.extend.HDialog;
import hjow.common.ui.extend.HLabel;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HSpinner;
import hjow.common.ui.extend.HTabbedPane;
import hjow.common.ui.extend.HTextField;

/** 스크립트 기반 모듈을 개발할 수 있습니다. */
public class ModuleEditor extends BuiltinModule implements ActionListener {
	private static final long serialVersionUID = 6688928363276467562L;
	
	protected String modulePath;
	
	protected HPanel pnMain, pnBasicInfo;
	protected HLabel lbName, lbShortName, lbCompType, lbId;
	protected HTextField tfName, tfShortName, tfCheckCode;
	protected HSpinner spId;
	protected JEditorPane taDesc, taUi, taInitFirst, taAfterInit, taRun, taThread, taBeforeExit;
	protected HButton btnTestUi;
	protected HComboBox cbCompType;
	protected JMenuBar menuBar;
	protected JMenuItem mFileSave, mFileLoad, mFileNew;
	protected JFileChooser chooser;
	protected JInternalFrame frame;
	
	protected HTabbedPane tabPane;
	protected HDialog dialogTest;
	
	protected transient CustomAbstractModule target;
	protected transient JsonPane     tempPane;
	protected transient JScrollPane  tempScroll;
	
	public ModuleEditor(Core core) {
		modulePath = core.getModulePath();
	}

	@Override
	public String getShortName() {
		return "module_editor";
	}

	@Override
	public String getName() {
		return Core.trans("Module Editor");
	}

	@Override
	public long getId() {
		return 346346093486093486L;
	}

	@Override
	public String getDescription() {
		return "Tool to develop new module.";
	}

	@Override
	public int getComponentType() {
		return Module.DESKTOP;
	}

	@Override
	public Component getComponent() {
		return pnMain;
	}
	
	protected void newEmptyCustomModule() {
		target = new CustomModule();
		target.setId(new Random().nextLong());
		target.setCheckCode("");
		moduleToUI();
	}
	
	protected void moduleToUI() {
		tfName.setText(target.getLongName());
		tfShortName.setText(target.getShortName());
		spId.setValue(new Long(target.getId()));
		switch(target.getComponentType()) {
		case Module.DESKTOP:
			cbCompType.setSelectedItem("DESKTOP");
			break;
		case Module.DIALOG:
			cbCompType.setSelectedItem("DIALOG");
			break;
		case Module.HIDDEN:
			cbCompType.setSelectedItem("HIDDEN");
			break;
		case Module.NONE:
			cbCompType.setSelectedItem("NONE");
			break;
		}
		
		taDesc.setText(target.getDescription());
		taInitFirst.setText(target.getScriptInit());
		taAfterInit.setText(target.getScriptAfterInit());
		taRun.setText(target.getScriptRun());
		taThread.setText(target.getScriptThread());
		taBeforeExit.setText(target.getScriptBeforeExit());
		taUi.setText(target.getJsonUi());
	}
	
	protected void uiToModule() {
		target.setLongName(tfName.getText());
		
		String shortName = tfShortName.getText().replace(" ", "_").replace("\t", "_").replace("\n", "_").toLowerCase();
		target.setShortName(shortName);
		tfShortName.setText(shortName);
		
		if(spId.isEnabled()) {
			target.setId(((Number) spId.getValue()).longValue());
		}
		
		String selectedCompType = String.valueOf(cbCompType.getSelectedItem());
		if(selectedCompType.equals("DESKTOP")) {
			target.setComponentType(Module.DESKTOP);
		}
		if(selectedCompType.equals("DIALOG")) {
			target.setComponentType(Module.DIALOG);
		}
		if(selectedCompType.equals("HIDDEN")) {
			target.setComponentType(Module.HIDDEN);
		}
		if(selectedCompType.equals("NONE")) {
			target.setComponentType(Module.NONE);
		}
		
		target.setDescription(taDesc.getText());
		target.setScriptInit(taInitFirst.getText());
		target.setScriptAfterInit(taAfterInit.getText());
		target.setScriptRun(taRun.getText());
		target.setScriptThread(taThread.getText());
		target.setScriptBeforeExit(taBeforeExit.getText());
		target.setJsonUi(taUi.getText());
		target.setCheckCode(target.buildCheckCode());
	}

	@Override
	public void initFirst() {
		pnMain = new HPanel();
		pnMain.setLayout(new BorderLayout());
		
		tabPane = new HTabbedPane();
		pnMain.add(tabPane, BorderLayout.CENTER);
		
		HPanel pnBasicInfoParent = new HPanel();
		tabPane.add(Core.trans("Basic"), new JScrollPane(pnBasicInfoParent));
		
		pnBasicInfoParent.setLayout(new BorderLayout());
		
		pnBasicInfo = new HPanel();
		pnBasicInfoParent.add(pnBasicInfo, BorderLayout.NORTH);
		
		HPanel[] panels = new HPanel[5];
		pnBasicInfo.setLayout(new GridLayout(panels.length, 1));
		
		for(int idx=0; idx<panels.length; idx++) {
			panels[idx] = new HPanel();
			panels[idx].setLayout(new FlowLayout());
			pnBasicInfo.add(panels[idx]);
		}
		
		lbName = new HLabel(Core.trans("Module Name"));
		tfName = new HTextField(15);
		
		panels[0].add(lbName);
		panels[0].add(tfName);
		
		lbShortName = new HLabel(Core.trans("Short Name"));
		tfShortName = new HTextField(15);
		
		panels[1].add(lbShortName);
		panels[1].add(tfShortName);
		
		lbId = new HLabel(Core.trans("ID"));
		spId = new HSpinner();
		spId.setEnabled(false);
		
		panels[2].add(lbId);
		panels[2].add(spId);
		
		Vector<String> compType = new Vector<String>();
		compType.add("DESKTOP");
		compType.add("DIALOG");
		compType.add("HIDDEN");
		compType.add("NONE");
		cbCompType = new HComboBox(compType);
		cbCompType.setSelectedIndex(0);
		
		lbCompType = new HLabel(Core.trans("Component Type"));
		
		panels[3].add(lbCompType);
		panels[3].add(cbCompType);
		
		
		HPanel pnTab = new HPanel();
		pnTab.setLayout(new BorderLayout());
		tabPane.add(Core.trans("Description"), new JScrollPane(pnTab));
		
		taDesc = new JEditorPane();
		pnTab.add(new JScrollPane(taDesc));
		
		
		pnTab = new HPanel();
		pnTab.setLayout(new BorderLayout());
		tabPane.add(Core.trans("UI JSON"), new JScrollPane(pnTab));
		
		taUi = new JEditorPane();
		taUi.setContentType("text/json");
		pnTab.add(new JScrollPane(taUi), BorderLayout.CENTER);
		
		HPanel pnControlUI = new HPanel();
		pnTab.add(pnControlUI, BorderLayout.SOUTH);
		
		pnControlUI.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		btnTestUi = new HButton(Core.trans("Test"));
		btnTestUi.addActionListener(this);
		pnControlUI.add(btnTestUi);
		
		pnTab = new HPanel();
		pnTab.setLayout(new BorderLayout());
		tabPane.add(Core.trans("Script On Initialize"), new JScrollPane(pnTab));
		
		taInitFirst = new JEditorPane();
		taInitFirst.setContentType("text/javascript");
		pnTab.add(new JScrollPane(taInitFirst));
		
		
		pnTab = new HPanel();
		pnTab.setLayout(new BorderLayout());
		tabPane.add(Core.trans("Script After Initialize"), new JScrollPane(pnTab));
		
		taAfterInit = new JEditorPane();
		taAfterInit.setContentType("text/javascript");
		pnTab.add(new JScrollPane(taAfterInit));
		
		
		pnTab = new HPanel();
		pnTab.setLayout(new BorderLayout());
		tabPane.add(Core.trans("Script At Called"), new JScrollPane(pnTab));
		
		taRun = new JEditorPane();
		taRun.setContentType("text/javascript");
		pnTab.add(new JScrollPane(taRun));
		
		
		pnTab = new HPanel();
		pnTab.setLayout(new BorderLayout());
		tabPane.add(Core.trans("Script On Background"), new JScrollPane(pnTab));
		
		taThread = new JEditorPane();
		taThread.setContentType("text/javascript");
		pnTab.add(new JScrollPane(taThread));
		
		
		pnTab = new HPanel();
		pnTab.setLayout(new BorderLayout());
		tabPane.add(Core.trans("Script Before Exit"), new JScrollPane(pnTab));
		
		taBeforeExit = new JEditorPane();
		taBeforeExit.setContentType("text/javascript");
		pnTab.add(new JScrollPane(taBeforeExit));
		
		chooser = new JFileChooser(modulePath);
		SwingUI.setFileFilter(chooser, modulePath);
		
		newEmptyCustomModule();
	}
	
	@Override
	public void initFrame(JInternalFrame internalFrame) {
		this.frame = internalFrame;
		
    	menuBar = new JMenuBar();
    	internalFrame.setJMenuBar(menuBar);
    	
    	JMenu mFile = new JMenu(Core.trans("File"));
    	menuBar.add(mFile);
    	
    	mFileNew = new JMenuItem(Core.trans("New Module"));
    	mFileNew.addActionListener(this);
    	mFile.add(mFileNew);
    	
    	mFileLoad = new JMenuItem(Core.trans("Load"));
    	mFileLoad.addActionListener(this);
    	mFile.add(mFileLoad);
    	
    	mFileSave = new JMenuItem(Core.trans("Save"));
    	mFileSave.addActionListener(this);
    	mFile.add(mFileSave);
    }

	@Override
	public void initSecond(UI ui) {
		if(dialogTest == null) {
			dialogTest = new HDialog(ui.getFrame());
			dialogTest.setLayout(new BorderLayout());
			dialogTest.setSize(450, 350);
		}
	}

	@Override
	public void run() {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob = e.getSource();
		if(ob == mFileNew) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					newEmptyCustomModule();
				}
			});
		} else if(ob == mFileLoad) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					actionLoad();
				}
			});
		} else if(ob == mFileSave) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					actionSave();
				}
			});
		} else if(ob == btnTestUi) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					actionTest();
				}
			});
		}
	}
	
	protected void actionLoad() {
		int sel = chooser.showOpenDialog(frame);
		if(sel != JFileChooser.APPROVE_OPTION) return;
		
		CustomAbstractModule befores = target;
		
		File file = chooser.getSelectedFile();
		
		target = new CustomModule(file, null, null);
		moduleToUI();
		befores.releaseResource();
	}
	
	protected void actionSave() {
		uiToModule();
		int sel = chooser.showSaveDialog(frame);
		if(sel != JFileChooser.APPROVE_OPTION) return;
		
		File file = chooser.getSelectedFile();
		
		String checkingPrefix = file.getAbsolutePath().toLowerCase();
		if(! (checkingPrefix.endsWith(".zmodule") || checkingPrefix.endsWith(".zjmodule") || checkingPrefix.endsWith(".jmodule") || checkingPrefix.endsWith(".xmodule"))) {
			file = new File(file.getAbsolutePath() + ".zmodule");
		}
		
		target.saveFile(file);
	}
	
	protected synchronized void actionTest() {
		if(dialogTest == null) {
			dialogTest = new HDialog();
			dialogTest.setLayout(new BorderLayout());
			dialogTest.setSize(450, 350);
		}
		
		dialogTest.setVisible(false);
		btnTestUi.setEnabled(false);
		
		if(tempPane != null) {
			tempPane.releaseResource();
		}
		
		if(tempScroll != null) {
			dialogTest.getContentPane().remove(tempScroll);
			tempScroll.removeAll();
		}
		
		try {
			String jsonContent = taUi.getText();
			tempPane = new JsonPane(jsonContent);
			tempScroll = new JScrollPane(tempPane.getRootComponent());
			
			dialogTest.setLayout(new BorderLayout());
			dialogTest.getContentPane().add(tempScroll, BorderLayout.CENTER);
		} catch(Throwable t) {
			Core.logError(t);
			JOptionPane.showMessageDialog(frame, Core.trans("Error") + " : " + t.getMessage());
			
			if(tempPane != null) tempPane.releaseResource();
			
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dialogTest.setVisible(true);
				btnTestUi.setEnabled(true);
			}
		});
	}
}
