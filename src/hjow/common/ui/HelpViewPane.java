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
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import hjow.common.help.Help;
import hjow.common.help.HelpPage;
import hjow.common.ui.extend.HList;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HSplitPane;

public class HelpViewPane extends HPanel implements ListSelectionListener {
    private static final long serialVersionUID = -5943997918760431561L;

    protected HSplitPane split;
    protected HList list;
    protected JEditorPane pane;
    
    public HelpViewPane() {
        setLayout(new BorderLayout());
        
        split = new HSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(split, BorderLayout.CENTER);
        
        list = new HList();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        split.setLeftComponent(new JScrollPane(list));
        
        pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setEditable(false);
        
        split.setRightComponent(new JScrollPane(pane));
        split.setDividerLocation(0.3);
        
        registerListener();
    }
    
    public void setDividerLocation(double divideProbability) {
        split.setDividerLocation(divideProbability);
    }
    
    public void setHelpContent(Help help) {
        if(help == null) {
            list.setListData(new Vector<HelpPage>());
            pane.setText("");
            return;
        }
        
        help.setLocale(System.getProperty("user.language"));
        list.setListData(help.getPages());
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Object obj = e.getSource();
        if(obj == list) {
            HelpPage selectedItem = (HelpPage) list.getSelectedValue();
            pane.setContentType("text/html");
            pane.setText(selectedItem.getContent());
        }
    }
    
    public void registerListener() {
        list.addListSelectionListener(this);
    }
    
    public void unregisterEvents() {
        list.removeListSelectionListener(this);
    }
}
