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
package org.duckdns.hjow.commons.module;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.script.ScriptException;

import org.duckdns.hjow.commons.core.Core;
import org.duckdns.hjow.commons.json.JsonObject;
import org.duckdns.hjow.commons.script.HScriptEngine;
import org.duckdns.hjow.commons.thread.ScriptThread;
import org.duckdns.hjow.commons.ui.ScriptJsonPane;
import org.duckdns.hjow.commons.ui.UI;
import org.duckdns.hjow.commons.util.DataUtil;
import org.duckdns.hjow.commons.util.GUIUtil;
import org.duckdns.hjow.commons.util.SecurityUtil;

public abstract class CustomAbstractModule implements Module {
    private static final long serialVersionUID = 7327448123327454433L;
    protected String shortName, longName;
    protected long id;
    protected String description;
    protected int componentType;
    protected String checkCode;
    protected Integer shortcutKey, shortcutMask;
    protected String scriptInit, scriptAfterInit, scriptBeforeExit, scriptRun, scriptThread;
    protected String jsonUi;
    
    protected transient HScriptEngine engine;
    protected transient String accessKey;
    protected transient Map<String, String> storage;
    protected transient ScriptThread thread;
    protected transient ScriptJsonPane pane;
    
    public CustomAbstractModule() {
        
    }
    
    public CustomAbstractModule(File file, HScriptEngine engine, String accessKey) {
        load(file, engine, accessKey);
    }
    
    public CustomAbstractModule(Properties prop, HScriptEngine engine, String accessKey) {
        load(prop, engine, accessKey);
    }
    
    protected void load(File file, HScriptEngine engine, String accessKey) {
        Properties prop = new Properties();
        
        FileInputStream stream = null;
        InputStream additionalStream = null;
        InputStreamReader readerConverter = null;
        BufferedReader reader = null;
        
        String path = file.getAbsolutePath();
        path = path.toLowerCase();
        
        try {
            stream = new FileInputStream(file); 
            if(path.endsWith(".zmodule") || path.endsWith(".zjmodule"))
                additionalStream = new GZIPInputStream(stream);
            
            InputStream finalStream = stream;
            
            if(additionalStream == null)
                finalStream = stream;
            else
                finalStream = additionalStream;
            
            if(path.endsWith(".jmodule") || path.endsWith(".zjmodule")) {
                readerConverter = new InputStreamReader(finalStream, "UTF-8");
                reader = new BufferedReader(readerConverter);
                
                StringBuilder jsonStr = new StringBuilder("");
                boolean isFirst = true;
                while(true) {
                    String line = reader.readLine();
                    if(line == null) break;
                    if(! isFirst) jsonStr = jsonStr.append("\n");
                    jsonStr = jsonStr.append(line);
                    isFirst = false;
                }
                JsonObject jsonObj = (JsonObject) DataUtil.parseJson(jsonStr.toString());
                Set<String> keys = jsonObj.keySet();
                for(String k : keys) {
                    prop.setProperty(k, String.valueOf(jsonObj.get(k)));
                }
            } else if(path.endsWith(".zmodule") || path.endsWith(".xmodule")) {
                prop.loadFromXML(finalStream);
            } else {
                prop.load(finalStream);
            }
        } catch(Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            if(reader != null) {
                try { reader.close(); } catch(IOException ex) {}
            }
            if(readerConverter != null) {
                try { readerConverter.close(); } catch(IOException ex) {}
            }
            if(additionalStream != null) {
                try { additionalStream.close(); } catch(IOException ex) {}
            }
            if(stream != null) {
                try { stream.close(); } catch(IOException ex) {}
            }
        }
        
        load(prop, engine, accessKey);
    }
    
    protected void load(Properties prop, HScriptEngine engine, String accessKey) {
        shortName = prop.getProperty("SHORT_NAME");
        longName = prop.getProperty("NAME");
        id = Long.parseLong(prop.getProperty("ID"));
        description = prop.getProperty("DESCRIPTION");
        componentType = Integer.parseInt(prop.getProperty("COMPONENT_TYPE"));
        scriptInit = prop.getProperty("SCRIPT_INIT");
        scriptAfterInit = prop.getProperty("SCRIPT_AFTER_INIT");
        scriptRun = prop.getProperty("SCRIPT_RUN");
        scriptThread = prop.getProperty("SCRIPT_THREAD");
        scriptBeforeExit = prop.getProperty("SCRIPT_BEFORE_EXIT");
        checkCode = prop.getProperty("CHECK_CODE");
        jsonUi = prop.getProperty("UI");
        
        engine.setInfoFromModule(this);
        
        loadShortcutOpt(prop);
        
        this.engine = engine;
        this.accessKey = accessKey;
        
        pane = null;
        if(jsonUi != null) {
            pane = new ScriptJsonPane((JsonObject) DataUtil.parseJson(jsonUi));
        }
    }
    
    @Override
    public void initThread(Core core) {
        if(DataUtil.isEmpty(scriptThread)) return;
        
        thread = core.newModuleThread(this, engine, "Module - " + getName(), scriptThread, null);
        thread.start();
    }
    
    protected void loadShortcutOpt(Properties prop) {
        shortcutKey = null;
        shortcutMask = null;
        
        String shortcutKeyOpt  = prop.getProperty("SHORTCUT_KEY");
        String shortcutMaskOpt = prop.getProperty("SHORTCUT_MASK");
        
        if(shortcutKeyOpt == null || shortcutMaskOpt == null) return;
        if(DataUtil.isEmpty(shortcutKeyOpt)) return;
        if(DataUtil.isEmpty(shortcutMaskOpt)) return;
        
        shortcutKeyOpt = shortcutKeyOpt.trim();
        shortcutMaskOpt = shortcutMaskOpt.trim();
        
        Integer convertKeyOpt = GUIUtil.convertKeyStroke(shortcutKeyOpt);
        if(convertKeyOpt != null) shortcutKey = convertKeyOpt;
        else shortcutKey = new Integer(shortcutKeyOpt);
        
        Integer convertMaskOpt = GUIUtil.convertMaskStroke(shortcutMaskOpt);
        if(convertMaskOpt != null) shortcutMask = convertMaskOpt;
        else shortcutMask = new Integer(shortcutMaskOpt);
    }
    
    @Override
    public boolean hasShortcut() {
        return shortcutKey != null;
    }
    
    public boolean isPrepared() {
        return engine != null && accessKey != null;
    }
    
    /** 모듈 사용을 중단합니다. */
    public void release() {
        if(this.scriptBeforeExit != null) {
            try { this.engine.eval(this.scriptBeforeExit); } catch(Throwable t) { Core.logError(t); }
        }
        
        releaseResource();
    }
    
    @Override
    public void releaseResource() {
        if(this.thread != null) {
            try { this.thread.releaseResource(); } catch(Throwable t) { Core.logError(t); }
            this.thread = null;
        }
        if(this.pane != null) {
            try { this.pane.releaseResource(); } catch(Throwable t) { Core.logError(t); }
            try { engine.put("module_ui", null); } catch(Throwable t) { }
            this.pane = null;
        }
        this.storage = null;
        this.engine = null;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public String getName() {
        return longName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getComponentType() {
        return componentType;
    }

    @Override
    public Component getComponent() {
        Object obj = null;
        try {
            obj = engine.eval("f_getCustomComponents('" + getId() + "');");
        } catch(Throwable t) {
            Core.logError(t);
        }
        return (Component) obj;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public HScriptEngine getEngine() {
        return engine;
    }

    public void setEngine(HScriptEngine engine) {
        this.engine = engine;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComponentType(int componentType) {
        this.componentType = componentType;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public String getJsonUi() {
        return jsonUi;
    }

    public void setJsonUi(String jsonUi) {
        this.jsonUi = jsonUi;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getScriptInit() {
        return scriptInit;
    }

    public void setScriptInit(String scriptInit) {
        this.scriptInit = scriptInit;
    }

    public String getScriptAfterInit() {
        return scriptAfterInit;
    }

    public void setScriptAfterInit(String scriptAfterInit) {
        this.scriptAfterInit = scriptAfterInit;
    }

    public String getScriptBeforeExit() {
        return scriptBeforeExit;
    }
    
    public String getScriptRun() {
        return scriptRun;
    }

    public void setScriptRun(String scriptRun) {
        this.scriptRun = scriptRun;
    }

    public void setScriptBeforeExit(String scriptBeforeExit) {
        this.scriptBeforeExit = scriptBeforeExit;
    }

    @Override
    public Integer getShortcutKey() {
        return shortcutKey;
    }

    public void setShortcutKey(Integer shortcutKey) {
        this.shortcutKey = shortcutKey;
    }

    @Override
    public Integer getShortcutMask() {
        return shortcutMask;
    }

    public void setShortcutMask(Integer shortcutMask) {
        this.shortcutMask = shortcutMask;
    }

    public Map<String, String> getStorage() {
        return storage;
    }

    public void setStorage(Map<String, String> storage) {
        this.storage = storage;
    }

    public String getScriptThread() {
        return scriptThread;
    }

    public void setScriptThread(String scriptThread) {
        this.scriptThread = scriptThread;
    }

    @Override
    public void initFirst() {
        if(pane != null) {
            try {
                engine.put("module_ui", pane);
                engine.eval("f_setCustomComponents(" + this.getId() + ", module_ui)");
            } catch (ScriptException e) {
                Core.logError(e);
            }
        }
        
        try {
            engine.eval(getScriptInit());
        } catch (ScriptException e) {
            Core.logError(e);
        }
    }

    @Override
    public void initSecond(UI ui) {
        try {
            engine.eval(getScriptAfterInit());
        } catch (ScriptException e) {
            Core.logError(e);
        }
    }
    
    @Override
    public void run() {
        try {
            engine.eval(getScriptRun());
        } catch (ScriptException e) {
            Core.logError(e);
        }
    }

    @Override
    public void prepareStorage(Map<String, String> storageObj) {
        this.storage = storageObj;
        engine.put("modulestr_" + accessKey, storageObj);
        try {
            StringBuilder storageInit = new StringBuilder("");
            storageInit = storageInit.append("function f_storage_get(k) {                                 ").append("\n");
            storageInit = storageInit.append("    return modulestr_" + accessKey + ".get(String(k));      ").append("\n");
            storageInit = storageInit.append("};                                                          ").append("\n");
            storageInit = storageInit.append("function f_storage_set(k, v) {                              ").append("\n");
            storageInit = storageInit.append("    modulestr_" + accessKey + ".set(String(k), String(v));  ").append("\n");
            storageInit = storageInit.append("};                                                          ").append("\n");
            engine.eval(storageInit.toString());
        } catch(Throwable t) {
            Core.logError(t);
        }
    }
    
    /** 모듈을 파일로 저장합니다. */
    public void saveFile(File file) {
        Properties prop = new Properties();
        
        prop.setProperty("SHORT_NAME" , shortName);
        prop.setProperty("NAME"       , longName);
        prop.setProperty("ID", String.valueOf(id));
        prop.setProperty("DESCRIPTION", description);
        prop.setProperty("COMPONENT_TYPE", String.valueOf(componentType));
        prop.setProperty("SCRIPT_INIT", scriptInit);
        prop.setProperty("SCRIPT_AFTER_INIT", scriptAfterInit);
        prop.setProperty("SCRIPT_RUN", scriptRun);
        prop.setProperty("SCRIPT_THREAD", scriptThread);
        prop.setProperty("SCRIPT_BEFORE_EXIT", scriptBeforeExit);
        prop.setProperty("CHECK_CODE", checkCode);
        prop.setProperty("SHORTCUT_KEY" , String.valueOf(shortcutKey));
        prop.setProperty("SHORTCUT_MASK", String.valueOf(shortcutMask));
        prop.setProperty("UI", jsonUi);
        
        FileOutputStream stream = null;
        OutputStream additionalStream = null;
        OutputStreamWriter writerConverter = null;
        BufferedWriter writer = null;
        
        String path = file.getAbsolutePath();
        path = path.toLowerCase();
        
        try {
            stream = new FileOutputStream(file); 
            if(path.endsWith(".zmodule") || path.endsWith(".zjmodule"))
                additionalStream = new GZIPOutputStream(stream);
            
            OutputStream finalStream = stream;
            
            if(additionalStream == null)
                finalStream = stream;
            else
                finalStream = additionalStream;
            
            if(path.endsWith(".jmodule") || path.endsWith(".zjmodule")) {
                writerConverter = new OutputStreamWriter(finalStream, "UTF-8");
                writer = new BufferedWriter(writerConverter);
                
                JsonObject jsonObj = new JsonObject();
                Set<String> keys = prop.stringPropertyNames();
                for(String k : keys) {
                    jsonObj.put(k, String.valueOf(prop.get(k)));
                }
                StringTokenizer lineTokenizer = new StringTokenizer(jsonObj.toJSON(), "\n");
                boolean isFirst = true;
                while(lineTokenizer.hasMoreTokens()) {
                    if(! isFirst) writer.newLine();
                    writer.write(lineTokenizer.nextToken());
                    isFirst = false;
                }
                writer.close();
                writer = null;
                writerConverter.close();
                writerConverter = null;
            } else if(path.endsWith(".zmodule") || path.endsWith(".xmodule")) {
                prop.storeToXML(finalStream, "");
            } else {
                prop.store(finalStream, "");
            }
        } catch(Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            if(writer != null) {
                try { writer.close(); } catch(IOException ex) {}
            }
            if(writerConverter != null) {
                try { writerConverter.close(); } catch(IOException ex) {}
            }
            if(additionalStream != null) {
                try { additionalStream.close(); } catch(IOException ex) {}
            }
            if(stream != null) {
                try { stream.close(); } catch(IOException ex) {}
            }
        }
    }
    
    /** 사용자 정의 모듈의 패리티를 구합니다. */
    public String buildCheckCode() {
        return buildCheckCodeOf(this);
    }
    
    /** 패리티 검사를 수행해 결과를 반환합니다. */
    public boolean isCorrect() {
        return buildCheckCode().equals(this.checkCode);
    }
    
    /** 사용자 정의 모듈의 패리티를 구합니다. */
    public static String buildCheckCodeOf(CustomAbstractModule m) {
        StringBuilder sums = new StringBuilder("");
        sums = sums.append(String.valueOf(m.getId())).append("\n");
        sums = sums.append(m.getShortName()).append("\n");
        sums = sums.append(m.getName()).append("\n");
        sums = sums.append(m.getDescription()).append("\n");
        sums = sums.append(String.valueOf(m.getComponentType())).append("\n");
        sums = sums.append(m.getScriptInit()).append("\n");
        sums = sums.append(m.getScriptAfterInit()).append("\n");
        sums = sums.append(m.getScriptRun()).append("\n");
        sums = sums.append(m.getScriptThread()).append("\n");
        sums = sums.append(m.getScriptBeforeExit()).append("\n");
        sums = sums.append(m.getJsonUi()).append("\n");
        return SecurityUtil.hash(sums.toString(), "SHA-512");
    }
}
