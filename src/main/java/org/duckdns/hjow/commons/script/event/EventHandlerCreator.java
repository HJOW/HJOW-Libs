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
package org.duckdns.hjow.commons.script.event;

import java.util.Vector;

import org.duckdns.hjow.commons.script.HScriptEngine;
import org.duckdns.hjow.commons.script.ScriptObject;

public class EventHandlerCreator extends ScriptObject {
    private static final long serialVersionUID = -881215625751347430L;
    protected HScriptEngine engine;
    protected transient Vector<EventHandler> handlers;
    
    public EventHandlerCreator(HScriptEngine engine) {
        this.handlers = new Vector<EventHandler>();
        this.engine = engine;
    }
    
    @Override
    public void releaseResource() {
        for(EventHandler h : handlers) {
            h.releaseResource();
        }
        handlers.clear();
    }

    @Override
    public String getPrefixName() {
        return "eventhandler";
    }

    @Override
    public String getInitScript(String accessKey) {
        StringBuilder initScript = new StringBuilder("");
        initScript = initScript.append("function ui_newEventHandler() {                                                      ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".create();                       ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        
        return initScript.toString();
    }

    public EventHandler create() {
        EventHandler newOne = new EventHandler();
        newOne.setEngine(engine);
        handlers.add(newOne);
        return newOne;
    }
}
