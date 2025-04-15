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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JInternalFrame;

import org.duckdns.hjow.commons.core.Core;
import org.duckdns.hjow.commons.module.Module;

public abstract class BuiltinModule implements Module {
    private static final long serialVersionUID = -3081933575268127873L;
    
    protected Map<String, String> storage;
    protected Core core;

    public BuiltinModule() {
        
    }
    public BuiltinModule(Core core) {
        this.core = core;
    }
    @Override
    public void releaseResource() {
        this.storage = null;
        this.core = null;
    }
    
    @Override
    public Integer getShortcutKey() {
        return null;
    }

    @Override
    public Integer getShortcutMask() {
        return null;
    }

    @Override
    public boolean hasShortcut() {
        return false;
    }
    
    @Override
    public void prepareStorage(Map<String, String> storageObj) {
        
    }
    
    @Override
    public void initThread(Core core) {
        
    }
    
    public void initFrame(JInternalFrame internalFrame) {
        
    }
    
    public static final List<BuiltinModule> getBuiltinModules(Core core) {
        List<BuiltinModule> modules = new ArrayList<BuiltinModule>();
        
        modules.add(new ScriptModule(core));
        modules.add(new ModuleEditor(core));
        
        /*
        taskManager = new TaskManager(this);
        taskManager.setThreadList(core, threads);
        modules.add(taskManager);
        */
        
        return modules;
    }
}
