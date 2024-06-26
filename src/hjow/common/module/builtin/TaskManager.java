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
package hjow.common.module.builtin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;

import hjow.common.core.Core;
import hjow.common.module.CustomAbstractModule;
import hjow.common.thread.HThread;
import hjow.common.thread.RiskyRunnable;
import hjow.common.ui.HThreadStatusPanel;
import hjow.common.ui.UI;
import hjow.common.ui.extend.HPanel;

public class TaskManager extends BuiltinModule {
    private static final long serialVersionUID = 3400568096826735883L;
    protected HPanel pnMain, pnContents;
    
    protected transient HThread thread;
    protected transient List<HThread> threadList;
    protected transient List<HThreadStatusPanel> pnStatuses;
    
    public TaskManager(Core core) {
        thread = core.newThread("Thread list refresher", new RiskyRunnable() {
            @Override
            public void run() throws Throwable {
                
            }
        }, null);
        
        threadList = new ArrayList<HThread>();
        pnStatuses = new ArrayList<HThreadStatusPanel>();
    }
    
    public void setThreadList(Core core, List<HThread> threadList) {
        if(core == null) return;
        this.threadList = threadList;
        
        refresh();
    }
    
    protected  synchronized void refresh() {
        if(pnContents == null) return;
        
        pnContents.removeAll();
        for(HThreadStatusPanel p : pnStatuses) {
            p.releaseResource();
        }
        pnStatuses.clear();
        pnContents.setLayout(new GridLayout(threadList.size() + 2, 1));
        
        for(HThread t : threadList) {
            HThreadStatusPanel ht = new HThreadStatusPanel();
            ht.setThread(t);
            pnStatuses.add(ht);
            pnContents.add(ht);
        }
    }

    @Override
    public String getShortName() {
        return "task_manager";
    }

    @Override
    public String getName() {
        return "Task Manager";
    }

    @Override
    public long getId() {
        return 3463346575602386236L;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getComponentType() {
        return CustomAbstractModule.DESKTOP;
    }

    @Override
    public Component getComponent() {
        return pnMain;
    }

    @Override
    public void initFirst() {
        pnMain = new HPanel();
        thread.start();
        
        pnMain.setLayout(new BorderLayout());
        
        pnContents = new HPanel();
        pnMain.add(new JScrollPane(pnContents), BorderLayout.NORTH);
        
        HPanel pnDummy = new HPanel();
        pnMain.add(pnDummy, BorderLayout.CENTER);
    }

    @Override
    public void initSecond(UI ui) {
        refresh();
    }

    @Override
    public void run() {
        
    }
    
    @Override
    public void releaseResource() {
        if(thread != null) {
            thread.releaseResource();
            thread = null;
        }
        if(threadList != null) {
            threadList.clear();
            threadList = null;
        }
    }
}
