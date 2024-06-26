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
import java.awt.Component;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import hjow.common.core.Core;
import hjow.common.script.PublicMethodOpenedClass;
import hjow.common.ui.extend.HButton;
import hjow.common.ui.extend.HPanel;
import hjow.common.ui.extend.HTextField;

public class ClassicWebPane extends HPanel implements WebPane {
    private static final long serialVersionUID = -7433804879581765293L;
    
    protected JEditorPane view;
    protected JToolBar toolBarControl;
    protected HButton btnBack, btnForward, btnGo;
    protected HTextField tfUrl;
    
    protected transient List<String> history;
    protected transient int currentPointer = -1;

    public ClassicWebPane() {
        this(true);
    }
    
    public ClassicWebPane(boolean controlPanelOnTop) {
        super();
        init();
        if(controlPanelOnTop)
            this.add(toolBarControl, BorderLayout.NORTH);
        else
            this.add(toolBarControl, BorderLayout.SOUTH);
    }
    
    protected void init() {
        this.setLayout(new BorderLayout());
        
        view = new JEditorPane();
        view.setEditable(false);
        view.setContentType("text/html");
        view.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        goPage(e.getURL());
                    } catch (IOException e1) {
                        Core.logError(e1);
                    }
                }
            }
        });
        
        this.add(new JScrollPane(view), BorderLayout.CENTER);
        
        toolBarControl = new JToolBar();
        
        btnBack = new HButton("◀");
        btnForward = new HButton("◀");
        btnGo = new HButton("＃");
        tfUrl = new HTextField(15);
        
        toolBarControl.add(btnBack);
        toolBarControl.add(btnForward);
        toolBarControl.add(tfUrl);
        toolBarControl.add(btnGo);
        
        history = new Vector<String>();
    }
    
    public HButton getBackButton() {
        return btnBack;
    }
    
    public HButton getForwardButton() {
        return btnForward;
    }
    
    public HButton getGoButton() {
        return btnGo;
    }
    
    public HTextField getUrlTextField() {
        return tfUrl;
    }
    
    public void setEnabled(boolean e) {
        btnBack.setEnabled(e);
        btnForward.setEnabled(e);
        btnGo.setEnabled(e);
        tfUrl.setEnabled(e);
    }
    
    public boolean isEnabled() {
        return btnGo.isEnabled();
    }
    
    @Override
    public Component getComponent() {
        return this;
    }
    
    public JEditorPane getEditorPane() {
        return view;
    }
    
    public void setToolbarVisible(boolean v) {
        toolBarControl.setVisible(v);
    }
    
    public boolean isToolbarVisible() {
        return toolBarControl.isVisible();
    }

    @Override
    public void goPage(Object url) throws IOException {
        String targetUrl = null;
        if(url instanceof URL) targetUrl = ((URL)url).toString();
        else targetUrl = String.valueOf(url);
        
        history.add(targetUrl);
        currentPointer = history.size() - 1;
        view.setPage(targetUrl);
        tfUrl.setText(targetUrl);
    }
    
    public List<String> getHistory() {
        List<String> newList = new ArrayList<String>();
        for(String s : history) {
            newList.add(s);
        }
        return newList;
    }
    
    public void goBack() throws IOException {
        if(currentPointer <= 0) return;
        if(history.isEmpty()) return;
        
        currentPointer--;
        goPage(history.get(currentPointer));
    }
    
    public void goForward() throws IOException {
        if(currentPointer >= history.size() - 1) return;
        if(history.isEmpty()) return;
        
        currentPointer++;
        goPage(history.get(currentPointer));
    }
    
    /**
     * 사용 가능한 메소드 이름 리스트를 반환합니다.
     * 
     * @return 메소드 이름 리스트
     */
    public List<String> availables() {
        return PublicMethodOpenedClass.getAvailableMethods(this);
    }
}
