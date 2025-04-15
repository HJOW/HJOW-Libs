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
package org.duckdns.hjow.commons.module.builtin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.duckdns.hjow.commons.core.Core;
import org.duckdns.hjow.commons.module.Module;
import org.duckdns.hjow.commons.script.HScriptEngine;
import org.duckdns.hjow.commons.ui.UI;
import org.duckdns.hjow.commons.ui.extend.HButton;
import org.duckdns.hjow.commons.ui.extend.HPanel;
import org.duckdns.hjow.commons.ui.extend.HTabbedPane;
import org.duckdns.hjow.commons.ui.extend.HTextArea;
import org.duckdns.hjow.commons.ui.extend.HTextField;
import org.duckdns.hjow.commons.util.DataUtil;
import org.duckdns.hjow.commons.util.GUIUtil;

public class ScriptModule extends BuiltinModule implements ActionListener, KeyListener {
    private static final long serialVersionUID = -8937679782493113414L;
    protected HPanel mainPanel;
    protected HPanel pnOneLine, pnMultiLine;
    protected HTabbedPane tabPane;
    protected HTextArea logArea;
    protected JEditorPane multiArea;
    protected HTextField scriptField;
    protected HButton btnRunField, btRunArea;
    
    public static final int HISTORY_MAXIMUM = 100;
    protected transient int historyPoint = -1;
    protected transient List<String> history = new Vector<String>();
    
    protected HScriptEngine engine;
    
    public ScriptModule() {
        
    }
    
    public ScriptModule(Core core) {
        super(core);
        engine = core.newEngine("Main engine of Script Console");
    }
    
    @Override
    public String getShortName() {
        return "script_console";
    }

    @Override
    public String getName() {
        return Core.trans("Script Console");
    }

    @Override
    public long getId() {
        return 3159238589237589235L;
    }

    @Override
    public String getDescription() {
        return "You can run scripts.";
    }

    @Override
    public int getComponentType() {
        return Module.DESKTOP;
    }

    @Override
    public Component getComponent() {
        return mainPanel;
    }

    @Override
    public void initFirst() {
        mainPanel = new HPanel();
        mainPanel.setLayout(new BorderLayout());
        
        tabPane = new HTabbedPane();
        
        logArea = new HTextArea();
        logArea.setLineWrap(true);
        logArea.setEditable(false);
        
        mainPanel.add(tabPane, BorderLayout.CENTER);
        
        pnOneLine = new HPanel();
        pnOneLine.setLayout(new BorderLayout());
        
        pnOneLine.add(new JScrollPane(logArea), BorderLayout.CENTER);
        
        HPanel pnControl = new HPanel();
        pnOneLine.add(pnControl, BorderLayout.SOUTH);
        
        pnControl.setLayout(new BorderLayout());
        
        scriptField = new HTextField();
        scriptField.addKeyListener(this);
        btnRunField = new HButton(Core.trans("Run"));
        
        pnControl.add(scriptField, BorderLayout.CENTER);
        pnControl.add(btnRunField, BorderLayout.EAST);
        
        scriptField.addActionListener(this);
        btnRunField.addActionListener(this);
        
        tabPane.add(Core.trans("Single Line"), pnOneLine);
        
        pnMultiLine = new HPanel();
        pnMultiLine.setLayout(new BorderLayout());
        
        multiArea = new JEditorPane();
        multiArea.setContentType("text/javascript");
        pnMultiLine.add(new JScrollPane(multiArea), BorderLayout.CENTER);
        
        HPanel pnMultiControl = new HPanel();
        pnMultiControl.setLayout(new FlowLayout());
        
        btRunArea = new HButton(Core.trans("Run"));
        pnMultiControl.add(btRunArea);
        
        pnMultiLine.add(pnMultiControl, BorderLayout.SOUTH);
        
        btRunArea.addActionListener(this);
        
        tabPane.add(Core.trans("Multi Line"), pnMultiLine);
    }

    @Override
    public void initSecond(UI ui) {
        
    }
    
    @Override
    public void run() {
        
    }
    
    @Override
    public void releaseResource() {
        pnOneLine.removeAll();
        pnMultiLine.removeAll();
        tabPane.removeAll();
        mainPanel.removeAll();
        scriptField.removeActionListener(this);
        btnRunField.removeActionListener(this);
        btRunArea.removeActionListener(this);
        engine = null;
        super.releaseResource();
    }
    
    /** 텍스트 영역에 로그를 출력합니다. */
    public void log(Object obj) {
        logArea.append("\n");
        logArea.append(String.valueOf(obj));
        Core.log(obj);
        logArea.setCaretPosition(logArea.getDocument().getLength() - 1);
    }
    
    /** 텍스트 영역에 오류 발생 로그를 출력합니다. */
    public void logError(Throwable t, boolean details) {
        StringBuilder traces = new StringBuilder("");
        traces = traces.append(Core.trans("Error") + " : " + t.getMessage());
        
        if(details) {
            StackTraceElement[] elements = t.getStackTrace();
            for(StackTraceElement e : elements) {
                traces.append("\n").append("  at ").append(e.toString());
            }
        }
        
        log(traces.toString());
    }
    
    /** 스크립트 실행 전 로그 출력에 사용됩니다. */
    protected void logScripts(String contents) {
        if(contents == null || (! contents.contains("\n"))) {
            log(">> " +  contents);
            return;
        }
        
        StringBuilder logContents = new StringBuilder("");
        StringTokenizer lineTokenizer = new StringTokenizer(contents, "\n");
        
        logContents.append(">> SCRIPT STARTS //").append("\n");
        while(lineTokenizer.hasMoreTokens()) {
            logContents.append(lineTokenizer.nextToken()).append("\n");
        }
        logContents.append("// SCRIPT ENDS >>").append("\n");
        log(logContents.toString());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj == scriptField || obj == btnRunField || obj == btRunArea) {
            String contents = null;
            
            if(obj == btRunArea) {
                contents = multiArea.getText();
                multiArea.setEditable(false);
                tabPane.setSelectedComponent(pnOneLine);
            } else {
                contents = scriptField.getText();
                scriptField.setEditable(false);
                
                if(history == null) history = new Vector<String>();
                if(history.contains(contents)) history.remove(contents);
                if(history.size() >= HISTORY_MAXIMUM) history.remove(history.size() - 1);
                history.add(contents);
                historyPoint = -1;
            }
            
            Object results = null;
            try {
                logScripts(contents);
                if(DataUtil.isEmpty(contents)) return;
                
                results = engine.eval(contents);
            } catch(Throwable t) {
                logError(t, false);
                Core.logError(t);
            }
            log(results);
            
            if(obj == btRunArea) {
                multiArea.setEditable(true);
            } else {
                scriptField.setEditable(true);
                scriptField.setText("");
                scriptField.requestFocus();
            }
        }
    }

    @Override
    public Integer getShortcutKey() {
        return GUIUtil.convertKeyStroke("S");
    }

    @Override
    public Integer getShortcutMask() {
        return GUIUtil.convertMaskStroke("ALT");
    }

    @Override
    public boolean hasShortcut() {
        return true;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Object obj = e.getSource();
        if(obj == scriptField) {
            if(history == null) history = new Vector<String>();
            
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                if(historyPoint < history.size() - 1) {
                    historyPoint++;
                    scriptField.setText(history.get(historyPoint));
                }
            } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                if(historyPoint > 0) {
                    historyPoint--;
                    scriptField.setText(history.get(historyPoint));
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
