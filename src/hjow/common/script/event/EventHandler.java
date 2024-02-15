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
package hjow.common.script.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import hjow.common.core.Core;
import hjow.common.core.Releasable;
import hjow.common.script.HScriptEngine;
import hjow.common.util.DataUtil;

public class EventHandler implements Releasable, ActionListener, ItemListener, WindowListener, ListSelectionListener, ChangeListener, KeyListener, MouseListener, MouseMotionListener, RowSelectListener {
    private static final long serialVersionUID = 3147062562404648396L;
    
    protected transient HScriptEngine engine;
    protected Map<String, String> scripts;
    
    public EventHandler() {
        scripts = new HashMap<String, String>();
    }
    
    public EventHandler(HScriptEngine engine, Map<String, String> scripts) {
        super();
        this.engine = engine;
        this.scripts.putAll(scripts);
    }
    
    public void setAction(Object eventType, Object scriptContent) {
        scripts.put(String.valueOf(eventType), String.valueOf(scriptContent));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        try {
            String scriptContent = scripts.get("change");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__change_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        try {
            String scriptContent = scripts.get("list_selection");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__list_selection_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        try {
            String scriptContent = scripts.get("window_open");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__window_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            String scriptContent = scripts.get("window_closing");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        try {
            String scriptContent = scripts.get("window_close");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__window_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
        try {
            String scriptContent = scripts.get("window_iconify");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__window_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        try {
            String scriptContent = scripts.get("window_deiconify");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__window_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
        try {
            String scriptContent = scripts.get("window_activate");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__window_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        try {
            String scriptContent = scripts.get("window_deactivate");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__window_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        try {
            String scriptContent = scripts.get("item");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__item_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String scriptContent = scripts.get("action");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__action_event_source", e.getSource());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        try {
            String scriptContent = scripts.get("key_type");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__key_event_source", e.getSource());
            engine.put("__key_event_code", e.getKeyCode());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            String scriptContent = scripts.get("key_press");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__key_event_source", e.getSource());
            engine.put("__key_event_code", e.getKeyCode());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        try {
            String scriptContent = scripts.get("key_release");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__key_event_source", e.getSource());
            engine.put("__key_event_code", e.getKeyCode());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            String scriptContent = scripts.get("mouse_drag");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__mouse_event_source", e.getSource());
            engine.put("__mouse_event_click_count", e.getClickCount());
            engine.put("__mouse_event_location_on_screen", e.getLocationOnScreen());
            engine.put("__mouse_event_point", e.getPoint());
            engine.put("__mouse_event_button", e.getButton());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        try {
            String scriptContent = scripts.get("mouse_move");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__mouse_event_source", e.getSource());
            engine.put("__mouse_event_click_count", e.getClickCount());
            engine.put("__mouse_event_location_on_screen", e.getLocationOnScreen());
            engine.put("__mouse_event_point", e.getPoint());
            engine.put("__mouse_event_button", e.getButton());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            String scriptContent = scripts.get("mouse_click");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__mouse_event_source", e.getSource());
            engine.put("__mouse_event_click_count", e.getClickCount());
            engine.put("__mouse_event_location_on_screen", e.getLocationOnScreen());
            engine.put("__mouse_event_point", e.getPoint());
            engine.put("__mouse_event_button", e.getButton());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            String scriptContent = scripts.get("mouse_press");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__mouse_event_source", e.getSource());
            engine.put("__mouse_event_click_count", e.getClickCount());
            engine.put("__mouse_event_location_on_screen", e.getLocationOnScreen());
            engine.put("__mouse_event_point", e.getPoint());
            engine.put("__mouse_event_button", e.getButton());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        try {
            String scriptContent = scripts.get("mouse_release");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__mouse_event_source", e.getSource());
            engine.put("__mouse_event_click_count", e.getClickCount());
            engine.put("__mouse_event_location_on_screen", e.getLocationOnScreen());
            engine.put("__mouse_event_point", e.getPoint());
            engine.put("__mouse_event_button", e.getButton());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        try {
            String scriptContent = scripts.get("mouse_enter");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__mouse_event_source", e.getSource());
            engine.put("__mouse_event_click_count", e.getClickCount());
            engine.put("__mouse_event_location_on_screen", e.getLocationOnScreen());
            engine.put("__mouse_event_point", e.getPoint());
            engine.put("__mouse_event_button", e.getButton());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        try {
            String scriptContent = scripts.get("mouse_exit");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__mouse_event_source", e.getSource());
            engine.put("__mouse_event_click_count", e.getClickCount());
            engine.put("__mouse_event_location_on_screen", e.getLocationOnScreen());
            engine.put("__mouse_event_point", e.getPoint());
            engine.put("__mouse_event_button", e.getButton());
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }
    
    @Override
    public void rowSelected(RowSelectedEvent e) {
        try {
            String scriptContent = scripts.get("row_selected");
            if(DataUtil.isEmpty(scriptContent)) return;
            engine.put("__row_selected_event_source"   , e.getSource());
            engine.put("__row_selected_event_sql"      , e.getSql());
            engine.put("__row_selected_event_row_index", e.getRowIndex());
            
            Map<String, Object> newMap = new HashMap<String, Object>();
            newMap.putAll(e.getRow());
            engine.put("__row_selected_event_row", newMap);
            
            engine.eval(scriptContent);
        } catch(Throwable t) {
            Core.logError(t);
        }
    }

    @Override
    public void releaseResource() {
        scripts.clear();
        scripts = null;
        engine = null;
    }

    public HScriptEngine getEngine() {
        return engine;
    }

    public void setEngine(HScriptEngine engine) {
        this.engine = engine;
    }

    public Map<String, String> getScripts() {
        return scripts;
    }

    public void setScripts(Map<String, String> scripts) {
        this.scripts = scripts;
    }
}
