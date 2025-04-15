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
package org.duckdns.hjow.commons.ui.extend;

import java.awt.Window;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.duckdns.hjow.commons.script.PublicMethodOpenedClass;

public class HDialog extends JDialog implements AlphaRatioEditable {
    private static final long serialVersionUID = -1279622377824601191L;

    public HDialog() {
        
    }
    
    public HDialog(Window window) {
        super(window);
    }
    
    public HDialog(JFrame frame, boolean isModal) {
        super(frame, isModal);
    }
    
    public HDialog(JDialog frame, boolean isModal) {
        super(frame, isModal);
    }
    
    public List<String> availables() {
        return PublicMethodOpenedClass.getAvailableMethods(this);
    }
    
    @Override
    public void setAlphaRatio(float alphaRatio) {
        if(alphaRatio < 1.0) getRootPane().setOpaque(true);
        else getRootPane().setOpaque(false);
        getRootPane().putClientProperty("window.alpha", new Float(alphaRatio));
    }
}
