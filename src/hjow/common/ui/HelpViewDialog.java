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
import java.awt.Window;

import hjow.common.help.Help;
import hjow.common.ui.extend.HDialog;

public class HelpViewDialog extends HDialog {
    private static final long serialVersionUID = -1060896739838799051L;
    
    protected HelpViewPane viewPane;
    public HelpViewDialog(Window win) {
        super(win);
        setSize(550, 400);
        
        setLayout(new BorderLayout());
        
        viewPane = new HelpViewPane();
        add(viewPane, BorderLayout.CENTER);
    }
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(visible) {
            viewPane.setDividerLocation(0.3);
        }
    }
    
    public void setDividerLocation(double divideProbability) {
        viewPane.setDividerLocation(divideProbability);
    }
    
    public void registerListener() {
        viewPane.registerListener();
    }
    
    public void unregisterEvents() {
        viewPane.unregisterEvents();
    }
    
    public void setHelpContent(Help help) {
        viewPane.setHelpContent(help);
    }
}
