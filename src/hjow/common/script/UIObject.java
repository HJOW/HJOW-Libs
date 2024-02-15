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

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import hjow.common.data.Binary;
import hjow.common.ui.ClassicWebPane;
import hjow.common.ui.DefaultFileFilter;
import hjow.common.ui.ScriptJsonPane;
import hjow.common.ui.UI;
import hjow.common.ui.extend.HButton;
import hjow.common.ui.extend.HCheckBox;
import hjow.common.ui.extend.HComboBox;
import hjow.common.ui.extend.HDialog;
import hjow.common.ui.extend.HLabel;
import hjow.common.ui.extend.HList;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HProgressBar;
import hjow.common.ui.extend.HRadioButton;
import hjow.common.ui.extend.HSpinner;
import hjow.common.ui.extend.HSplitPane;
import hjow.common.ui.extend.HTabbedPane;
import hjow.common.ui.extend.HTextArea;
import hjow.common.ui.extend.HTextField;
import hjow.common.util.DataUtil;
import hjow.common.util.FileUtil;

public class UIObject extends ScriptObject {
    private static final long serialVersionUID = -9108983266605186351L;
    protected transient UI ui;
    
    
    public UIObject() {
        
    }
    
    public UIObject(UI ui) {
        super();
        this.ui = ui;
    }
    
    @Override
    public void releaseResource() {
        super.releaseResource();
        this.ui = null;
    }
    
    @Override
    public String getPrefixName() {
        return "uimanager";
    }
    
    @Override
    public String getInitScript(String accessKey) {
        StringBuilder initScript = new StringBuilder("");
        
        initScript = initScript.append("function ui_create(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".create(a);                      ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function ui_createsByJson(a) {                                                       ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".createsByJson(a);               ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function ui_scroll(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".wrapScrollPane(a);              ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function alert(a) {                                                                  ").append("\n");
        initScript = initScript.append("    " + getPrefixName() + "_" + accessKey + ".alert(a);                              ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function confirm(a) {                                                                ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".askConfirm(a);                  ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function askInput(a) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".askInput(a);                    ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function askFileOpen(a, b) {                                                         ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".askFileOpen(a, b);              ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function askFileSave(a, b, c) {                                                      ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".askFileSave(a, b, c);           ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        
        return initScript.toString();
    }
    public Object createsByJson(Object jsonText) {
    	return new ScriptJsonPane(jsonText);
    }
    public Object create(Object componentType) {
        String compType = String.valueOf(componentType);
        compType = compType.toLowerCase();
        
        if(compType.equals("dialog")) {
            return new HDialog(ui.getFrame());
        }
        if(compType.equals("panel")) {
            return new HPanel();
        }
        if(compType.equals("tab")) {
            return new HTabbedPane();
        }
        if(compType.equals("split")) {
            return new HSplitPane();
        }
        if(compType.equals("label")) {
            return new HLabel();
        }
        if(compType.equals("textfield")) {
            return new HTextField();
        }
        if(compType.equals("textarea")) {
            return new HTextArea();
        }
        if(compType.equals("text_pane")) {
            return new JTextPane();
        }
        if(compType.equals("editor_pane")) {
            return new JEditorPane();
        }
        if(compType.equals("web_pane")) {
            return new ClassicWebPane();
        }
        if(compType.equals("table")) {
            return new JTable();
        }
        if(compType.equals("table_model")) {
            return new DefaultTableModel();
        }
        if(compType.equals("button")) {
            return new HButton();
        }
        if(compType.equals("checkbox")) {
            return new HCheckBox();
        }
        if(compType.equals("button_group")) {
            return new ButtonGroup();
        }
        if(compType.equals("radio")) {
            return new HRadioButton();
        }
        if(compType.equals("combobox")) {
            return new HComboBox();
        }
        if(compType.equals("list")) {
            return new HList();
        }
        if(compType.equals("progress")) {
        	return new HProgressBar();
        }
        if(compType.equals("spinner")) {
        	return new HSpinner();
        }
        if(compType.equals("list_model")) {
            return new DefaultListModel();
        }
        if(compType.equals("classic_dialog")) {
        	return new Dialog(ui.getFrame());
        }
        if(compType.equals("classic_panel")) {
        	return new Panel();
        }
        if(compType.equals("classic_label")) {
        	return new Label();
        }
        if(compType.equals("classic_textfield")) {
        	return new TextField();
        }
        if(compType.equals("classic_textarea")) {
        	return new TextArea();
        }
        if(compType.equals("classic_button")) {
        	return new Button();
        }
        if(compType.equals("classic_checkbox")) {
        	return new java.awt.Checkbox();
        }
        if(compType.equals("classic_list")) {
        	return new java.awt.List();
        }
        if(compType.equals("layout_border")) {
            return new BorderLayout();
        }
        if(compType.equals("layout_flow")) {
            return new FlowLayout();
        }
        if(compType.equals("layout_card")) {
            return new CardLayout();
        }
        return null;
    }
    public Object wrapScrollPane(Object component) {
        return new JScrollPane((Component) component);
    }
    public void alert(Object msg) {
        JOptionPane.showMessageDialog(ui.getFrame(), String.valueOf(msg));
    }
    public boolean askConfirm(Object msg) {
        return JOptionPane.showConfirmDialog(ui.getFrame(), String.valueOf(msg), "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    public String askInput(Object msg) {
        return JOptionPane.showInputDialog(ui.getFrame(), String.valueOf(msg));
    }
    private JFileChooser createFileChooser(Object filePrefix, Object fileDesc) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        
        String prefixes = "";
        String desc     = "";
        
        if(filePrefix != null && (! String.valueOf(filePrefix).equals("undefined"))) prefixes = String.valueOf(filePrefix);
        if(fileDesc   != null && (! String.valueOf(fileDesc  ).equals("undefined"))) desc     = String.valueOf(fileDesc);
        
        final String pfx = prefixes;
        final String dc  = desc;
        
        if(! (DataUtil.isEmpty(prefixes) || prefixes.equals("*"))) {
        	DefaultFileFilter filter = new DefaultFileFilter(pfx, dc);
            fileChooser.setFileFilter(filter);
        }
        return fileChooser;
    }
    public Binary askFileOpen(Object filePrefix, Object fileDesc) throws IOException {
        JFileChooser fileChooser = createFileChooser(filePrefix, fileDesc);
        int selections = fileChooser.showOpenDialog(ui.getFrame());
        if(selections != JFileChooser.APPROVE_OPTION) return null;
        
        File selectedFile = fileChooser.getSelectedFile();
        return new Binary(FileUtil.readBytes(selectedFile));
    }
    public boolean askFileSave(Object filePrefix, Object fileDesc, Object data) throws IOException {
        if(data == null) throw new NullPointerException("data cannot be null");
        
        JFileChooser fileChooser = createFileChooser(filePrefix, fileDesc);
        int selections = fileChooser.showSaveDialog(ui.getFrame());
        if(selections != JFileChooser.APPROVE_OPTION) return false;
        
        File selectedFile = fileChooser.getSelectedFile();
        
        byte[] byteArray = null;
        if(data instanceof byte[]) byteArray = (byte[]) data;
        else if(data instanceof ByteArrayOutputStream) byteArray = ((ByteArrayOutputStream) data).toByteArray();
        else if(data instanceof Binary) byteArray = ((Binary) data).toByteArray();
        else byteArray = String.valueOf(data).getBytes("UTF-8");
        
        FileUtil.writeBytes(selectedFile, byteArray);
        return true;
    }
}
