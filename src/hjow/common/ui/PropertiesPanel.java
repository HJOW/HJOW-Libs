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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import hjow.common.core.Core;
import hjow.common.ui.extend.HButton;
import hjow.common.ui.extend.HLabel;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HTabbedPane;
import hjow.common.ui.extend.HTextArea;
import hjow.common.ui.extend.HTextField;

/**
 * Properties 를 읽기 / 편집할 수 있는 컴포넌트입니다.
 * 
 * @author HJOW
 *
 */
public class PropertiesPanel extends HPanel implements ActionListener, PropertiesEditor {
	private static final long serialVersionUID = 326074666093812186L;
	protected transient Properties prop;
	
	protected HTabbedPane tabPane;
	
	protected HPanel pnEdit, pnEditGridLeft, pnEditGridCenter;
	
	protected HPanel pnControl;
	protected List<HPanel> pnLeftElements, pnCenterElements;
	protected List<HLabel> lbs;
	protected List<HTextArea> txAreas;
	protected List<HButton> btRemoves;
	
	protected HTextField tfNewKey;
	protected HButton btAdd;

	/**
	 * Properties 를 읽기 / 편집할 수 있는 컴포넌트를 만듭니다.
	 */
	public PropertiesPanel() {
		super();
		
		prop = new Properties();
		
		setLayout(new BorderLayout());
		
		tabPane = new HTabbedPane();
		add(tabPane, BorderLayout.CENTER);
		
		pnEdit = new HPanel();
		tabPane.add(Core.trans("Edit Mode"), pnEdit);
		
		pnEdit.setLayout(new BorderLayout());
		
		HPanel pnEditGrid = new HPanel();
		pnEdit.add(new JScrollPane(pnEditGrid), BorderLayout.CENTER);
		
		pnEditGrid.setLayout(new BorderLayout());
		
		pnEditGridLeft = new HPanel();
		pnEditGridCenter = new HPanel();
		
		pnEditGrid.add(pnEditGridLeft, BorderLayout.WEST);
		pnEditGrid.add(pnEditGridCenter, BorderLayout.CENTER);
		
		pnControl = new HPanel();
		pnEdit.add(pnControl, BorderLayout.NORTH);
		
		pnControl.setLayout(new FlowLayout());
		
		tfNewKey = new HTextField(15);
		btAdd = new HButton("+");
		
		pnControl.add(tfNewKey);
		pnControl.add(btAdd);
		
		btAdd.addActionListener(this);
		
		refresh();
	}
	
	/**
	 * 현재 설정된 Properties 값에 맞게 컴포넌트들을 다시 배치합니다.
	 */
	protected synchronized void refresh() {
		if(pnLeftElements   == null) pnLeftElements   = new Vector<HPanel>();
		if(pnCenterElements == null) pnCenterElements = new Vector<HPanel>();
		if(lbs              == null) lbs              = new Vector<HLabel>();
		if(txAreas          == null) txAreas          = new Vector<HTextArea>();
		if(btRemoves        == null) btRemoves        = new Vector<HButton>();
		
		pnEditGridLeft.removeAll();
		pnEditGridCenter.removeAll();
		
		for(HPanel p : pnLeftElements) {
			p.removeAll();
		}
		pnLeftElements.clear();
		
		for(HPanel p : pnCenterElements) {
			p.removeAll();
		}
		pnCenterElements.clear();
		
		lbs.clear();
		txAreas.clear();
		
		for(HButton btn : btRemoves) {
			btn.removeActionListener(this);
		}
		btRemoves.clear();
		
		if(prop == null) prop = new Properties();
		Set<String> keys = prop.stringPropertyNames();
		
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		int elementHeight = 30;
		
		for(String keyOne : keys) {
			HPanel pnLeft = new HPanel();
			HPanel pnCenter = new HPanel();
			
			pnLeft.setMinimumSize(new Dimension(100, elementHeight));
			pnLeft.setMaximumSize(new Dimension((int) (scrSize.getWidth() / 2), elementHeight));
			
			pnCenter.setMinimumSize(new Dimension(100, elementHeight));
			pnCenter.setMaximumSize(new Dimension((int) (scrSize.getWidth() * 2), elementHeight));
			
			pnLeft.setLayout(new FlowLayout(FlowLayout.LEFT));
			pnCenter.setLayout(new BorderLayout());
			
			HLabel lb = new HLabel(keyOne);
			HTextArea tx = new HTextArea();
			
			tx.setText(prop.getProperty(keyOne));
			
			HButton btnRemove = new HButton("-");
			
			lbs.add(lb);
			txAreas.add(tx);
			btRemoves.add(btnRemove);
			
			btnRemove.addActionListener(this);
			
			pnLeft.add(btnRemove);
			pnLeft.add(lb);
			pnCenter.add(new JScrollPane(tx));
			
			pnLeftElements.add(pnLeft);
			pnCenterElements.add(pnCenter);
		}
		
		pnEditGridLeft.setLayout(new GridLayout(pnLeftElements.size(), 1));
		pnEditGridCenter.setLayout(new GridLayout(pnCenterElements.size(), 1));
		
		for(HPanel p : pnLeftElements) {
			pnEditGridLeft.add(p);
		}
		
		for(HPanel p : pnCenterElements) {
			pnEditGridCenter.add(p);
		}
		
		if(this.isVisible()) {
			this.setVisible(false);
			this.setVisible(true);
		}
	}
	
	/** 현재 컴포넌트에 입력/편집된 값들을 Properties 에 적용시킵니다. */
	protected void applyUIEdits() {
		Properties editted = new Properties();
		for(int idx=0; idx<lbs.size(); idx++) {
			String keyOf = lbs.get(idx).getText();
			String value = txAreas.get(idx).getText();
			
			editted.setProperty(keyOf, value);
		}
		
		prop = editted;
	}

	@Override
	public Properties getProp() {
		applyUIEdits();
		return prop;
	}

	@Override
	public synchronized void setProp(Properties prop) {
		this.prop = prop;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj == btAdd) {
			prop.setProperty(tfNewKey.getText(), "");
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
			return;
		}
		
		for(int idx=0; idx<btRemoves.size(); idx++) {
			HButton btRemove = btRemoves.get(idx);
			if(obj == btRemove) {
				String keyOf = lbs.get(idx).getText();
				prop.remove(keyOf);
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						refresh();
					}
				});
				return;
			}
		}
	}
}
