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
package hjow.common.core;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.script.ScriptEngineManager;

import hjow.common.lib.LibraryBox;
import hjow.common.module.AdvancedModule;
import hjow.common.module.CustomAbstractModule;
import hjow.common.module.CustomModule;
import hjow.common.module.Module;
import hjow.common.module.builtin.BuiltinModule;
import hjow.common.module.builtin.TaskManager;
import hjow.common.resource.ResourceManager;
import hjow.common.script.FileObject;
import hjow.common.script.HScriptEngine;
import hjow.common.script.MathObject;
import hjow.common.script.NetObject;
import hjow.common.script.PrimitiveObject;
import hjow.common.script.RuntimeObject;
import hjow.common.script.ScriptObject;
import hjow.common.script.SecurityObject;
import hjow.common.script.UIObject;
import hjow.common.script.WebObject;
import hjow.common.script.event.EventHandlerCreator;
import hjow.common.script.jdbc.JDBCObject;
import hjow.common.thread.HThread;
import hjow.common.thread.ModuleThread;
import hjow.common.thread.RiskyRunnable;
import hjow.common.thread.ScriptThread;
import hjow.common.ui.HThreadStatusPanel;
import hjow.common.ui.PropertiesEditor;
import hjow.common.ui.UI;
import hjow.common.util.ClassUtil;
import hjow.common.util.DataUtil;
import hjow.common.util.FileUtil;

/**
 * HJOW 의 프레임워크 핵심 클래스입니다. 단일 인스턴스만 존재하며, 이 곳에서 프로퍼티 등의 요소들을 관리합니다.
 */
public class Core {
    public static final String VERSION_STRING = "2024.03.17 04:00";
    public static final long   VERSION_DAILY  = parseVersion();
    public static boolean restartEnabled = false;
    public String APP_VERSION    = "";
    
    private static final Core core = new Core();
    private Core() {}
    
    protected static List<String> logQueue = new Vector<String>();
    
    protected boolean isPrepared = false;
    protected Properties prop, stringTable;
    protected String appShortName, appLongName;
    protected Vector<Releasable> resources;
    protected Vector<Module> modules;
    protected Map<Long, Map<String, String>> moduleStorages;
    protected List<LibraryBox> libBoxes;
    protected Map<String, String> appParam;
    
    protected ScriptEngineManager globalEngineManager;
    protected String globalAccessKey = null;
    protected List<ScriptObject> globalScriptObjects;
    protected List<ScriptObject> eachScriptObjects;
    protected List<HScriptEngine> storeEnginesTemp;
    
    protected TaskManager taskManager;
    
    protected List<HThread> threads;
    
    protected UI ui;
    
    protected String[] preparedArgs;
    
    private static long parseVersion() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            return formatter.parse(VERSION_STRING).getTime();
        } catch(Throwable t) { logError(t); }
        return 0;
    }
    
    /**
     * 코어 인스턴스를 준비하고 반환합니다.
     *
     * @param appShortName the app short name
     * @param appLongName the app long name
     * @return the core
     */
    public static final Core loadCore(String appShortName, String appLongName) {
        return loadCore(appShortName, appLongName, "1", null);
    }
    
    /**
     * 코어 인스턴스를 준비하고 반환합니다.
     *
     * @param appShortName the app short name
     * @param appLongName the app long name
     * @return the core
     */
    public static final Core loadCore(String appShortName, String appLongName, String[] args) {
        return loadCore(appShortName, appLongName, "1", args);
    }
    
    /**
     * 코어 인스턴스를 준비하고 반환합니다.
     *
     * @param appShortName the app short name
     * @param appLongName the app long name
     * @return the core
     */
    public static final Core loadCore(String appShortName, String appLongName, String version) {
        return loadCore(appShortName, appLongName, version, null);
    }
    
    /**
     * 코어 인스턴스를 준비하고 반환합니다.
     *
     * @param appShortName the app short name
     * @param appLongName the app long name
     * @return the core
     */
    public static final Core loadCore(String appShortName, String appLongName, String version, String[] args) {
        core.prepare(appShortName, appLongName);
        core.APP_VERSION = version;
        core.preparedArgs = args;
        core.appParam = ClassUtil.convertAppParams(args);
        if(core.appParam == null) core.appParam = new HashMap<String, String>();
        return core;
    }
    
    /** 프로그램 실행 시 입력받은 매개변수 값을 반환합니다. */
    public static String getApplicationParameter(String keyOf) {
    	return core.appParam.get(keyOf);
    }
    
    /** 프로그램 실행 시 입력받은 매개변수 키들을 반환합니다. */
    public static List<String> getApplicationParameterKeys() {
    	Set<String> keys = core.appParam.keySet();
    	List<String> keyList = new ArrayList<String>();
    	keyList.addAll(keys);
    	return keyList;
    }
    
    /** 코어 인스턴스를 그대로 반환합니다. */
    private static Core getCore() {
        return core;
    }
    
    /**
     * 중요 리소스들을 불러옵니다.
     */
    protected void prepare(String appShortName, String appLongName) {
        if(isPrepared) return;
        
        this.appShortName = appShortName.trim().replace(" ", "").toLowerCase();
        this.appLongName = appLongName;
        this.resources = new Vector<Releasable>();
        this.moduleStorages = new HashMap<Long, Map<String,String>>();
        this.storeEnginesTemp = new ArrayList<HScriptEngine>();
        this.threads = new ArrayList<HThread>();
        
        resources = new Vector<Releasable>();
        modules = new Vector<Module>();
        
        try {
            File tempDir = getTemporaryPath();
            if(tempDir != null && (! tempDir.exists())) tempDir.mkdirs();
        } catch(Throwable t) {
            logError(t);
        }
        
        loadConfig();
        loadStringTable();
        
        this.globalEngineManager = new ScriptEngineManager();
        globalScriptObjects = new Vector<ScriptObject>();
        eachScriptObjects = new Vector<ScriptObject>();
        libBoxes = new Vector<LibraryBox>();
        
        prepareGlobalScopes();
        loadBoxes();
        
        loadModules();
        isPrepared = true;
        
        log("The core v" + VERSION_STRING + " is prepared.");
    }
    
    /** 응용 프로그램 이름을 반환합니다. */
    public String getAppName() {
        return appLongName;
    }
    
    /** 패키지를 불러옵니다. */
    protected void loadBoxes() {
    	String boxClass = "hjow.common.lib.AdvancedBox";
    	prop.setProperty("advanced_box_loaded", String.valueOf(loadBox(boxClass, true)));
    	
    	boxClass = "hjow.common.lib.BundledBox";
    	loadBox(boxClass, true);
    	
    	String additionalClassOpt = getProperty("library_box_classes");
    	if(! DataUtil.isEmpty(additionalClassOpt)) {
    		StringTokenizer semicolonTokenizer = new StringTokenizer(additionalClassOpt, ";");
    		while(semicolonTokenizer.hasMoreTokens()) {
    			boxClass = semicolonTokenizer.nextToken().trim();
    			loadBox(boxClass, false);
    		}
    	}
    	
    	for(LibraryBox p : libBoxes) {
    		try { p.init(this); } catch(Throwable t) { logError(t); }
    	}
    }
    
    @SuppressWarnings("unchecked")
	protected boolean loadBox(String className, boolean ignoreClassNotFound) {
    	LibraryBox boxOne = null;
    	try {
    		Class<? extends LibraryBox> packageClass = (Class<? extends LibraryBox>) Class.forName(className);
    		if(packageClass == null) return false;
    		
    		boxOne = packageClass.newInstance();
    		boxOne.init(this);
    		libBoxes.add(boxOne);
    		return true;
    	} catch(ClassNotFoundException e) {
    		if(! ignoreClassNotFound) log("Cannot found the class " + className + " as a library box.");
    	} catch(Throwable t) {
    		logError(t);
    	} finally {
    		if(boxOne != null && this.ui != null) boxOne.onPrepareUI(this, this.ui); // 이미 UI를 불러온 이후라면...
    	}
    	return false;
    }
    
    /** 전역적으로 사용할 수 있는 스크립트 객체들을 불러옵니다. */
    private void prepareGlobalScopes() {
    	int randomRange = 10000000;
        int randomNo = (((int) (Math.random() * ((randomRange - 2) - (randomRange / 10)))) + (randomRange / 10));
        globalAccessKey = String.valueOf(randomNo);
        globalEngineManager.put("comp_" + globalAccessKey, new HashMap<String, Object>());
        globalEngineManager.put("globalStorage", new HashMap<String, Object>());
        
        globalScriptObjects.add(PrimitiveObject.getInstance());
        globalScriptObjects.add(MathObject.getInstance());
        globalScriptObjects.add(SecurityObject.getInstance());
        globalScriptObjects.add(NetObject.getInstance());
        globalScriptObjects.add(JDBCObject.getInstance());
        globalScriptObjects.add(FileObject.getInstance());
        
        loadClassScriptObjects();
        
        for(ScriptObject obj : globalScriptObjects) {
            globalEngineManager.put(obj.getPrefixName() + "_" + globalAccessKey, obj);
        }
        
        prepareBasicInfo();
    }
    
    /** 스크립트 내에서 액세스할 수 있는 기본적인 시스템 정보들을 담습니다. */
    protected void prepareBasicInfo() {
        Map<String, String> info = getSystemInfo();
        List<String> infoKeys = new ArrayList<String>();
        infoKeys.addAll(info.keySet());
        globalEngineManager.put("basic_info" + "_" + globalAccessKey, info);
        globalEngineManager.put("basic_info_keys" + "_" + globalAccessKey, infoKeys);
    }
    
    /** 스크립트 엔진을 생성합니다. */
    public HScriptEngine newEngine(String name) {
    	return newEngine(name, null);
    }
    
    /** 스크립트 엔진을 생성합니다. */
    public HScriptEngine newEngine(String name, Module m) {
        HScriptEngine engine = new HScriptEngine(globalEngineManager.getEngineByName("JavaScript"), name, m);
        resources.add(engine);
        
        engine.put("localStorage", new HashMap<String, Object>());
        
        // storeEnginesTemp 는 UI 객체가 투입되면 null 처리되며 이 때 getInitScript 를 호출함. 그래서 여기서 호출할 필요가 없음.
        // 단, storeEnginesTemp 가 이미 null 이라면 이후 getInitScript 를 호출하지 않으므로 이 곳에서 호출해 줘야 함
        if(storeEnginesTemp == null) {
            try {
                engine.eval(getInitScript());
            } catch(Throwable t) {
                logError(t);
            }
            prepareUIEachObjects(engine);
        } else if(storeEnginesTemp != null) {
            storeEnginesTemp.add(engine);
        }
        
        // Library Box 불러오기
        for(LibraryBox box : libBoxes) {
        	try {
        		List<ScriptObject> objList = box.advancedObjects(this);
            	if(objList != null) {
            		for(ScriptObject obj : objList) {
            			if(obj.getPrefixName() == null) continue;
                		engine.put(obj.getPrefixName(), obj);
                	}
            	}
        	} catch(Throwable t) {
        		logError(t);
        	}
        }
        
        return engine;
    }
    
    private String getInitScript() {
        StringBuilder initEngine = new StringBuilder("");
        initEngine = initEngine.append("function f_setCustomComponents(id, comp) {                      ").append("\n");
        initEngine = initEngine.append("    comp_" + globalAccessKey + ".push(String(id), comp);        ").append("\n");
        initEngine = initEngine.append("};                                                              ").append("\n");
        
        initEngine = initEngine.append("function f_getCustomComponents(id, comp) {                      ").append("\n");
        initEngine = initEngine.append("    return comp_" + globalAccessKey + ".get(String(id));        ").append("\n");
        initEngine = initEngine.append("};                                                              ").append("\n");
        
        initEngine = initEngine.append("function f_getSystemInfo(key) {                                 ").append("\n");
        initEngine = initEngine.append("    return basic_info_" + globalAccessKey + ".get(String(key)); ").append("\n");
        initEngine = initEngine.append("};                                                              ").append("\n");
        
        initEngine = initEngine.append("function f_getSystemInfoKeys() {                                ").append("\n");
        initEngine = initEngine.append("    return basic_info_keys_" + globalAccessKey + ";             ").append("\n");
        initEngine = initEngine.append("};                                                              ").append("\n");
        
        for(ScriptObject obj : globalScriptObjects) {
        	String inits = obj.getInitScript(globalAccessKey);
        	if(DataUtil.isEmpty(inits)) continue;
            initEngine = initEngine.append(inits).append("\n");
        }
        
        initEngine = initEngine.append(ResourceManager.readFrom("init_availables.js")).append("\n");
        initEngine = initEngine.append(getInitScriptFileContents());
        
        return initEngine.toString();
    }
    
    /**
     * 스크립트 엔진 생성 직후 실행할 추가 js 파일들을 불러옵니다.
     */
    protected String getInitScriptFileContents() {
    	String additionalLibOpt = getProperty("init_script_files");
        if(DataUtil.isEmpty(additionalLibOpt)) return "";
        
        StringBuilder initEngine = new StringBuilder("");
        
        StringTokenizer semicolonTokenizer = new StringTokenizer(additionalLibOpt, ";");
        while(semicolonTokenizer.hasMoreTokens()) {
        	try {
	        	String fileOne = semicolonTokenizer.nextToken();
	        	if(DataUtil.isEmpty(fileOne)) continue;
	        	
	        	initEngine = initEngine.append(FileUtil.readString(new File(fileOne), "UTF-8"));
        	} catch(Throwable t) {
        		logError(t);
        	}
        }
        
        return initEngine.toString();
    }
    
    /** 프로그램 종료를 준비합니다. 모든 쓰레드와 모듈을 점차적으로 중단시키고 순환 참조들을 끊어내며 임시파일 폴더를 비웁니다. */
    protected static synchronized void disposeAll() {
    	Core core = getCore();
        
    	// 쓰레드 중단
        if(core.threads != null) {
            for(HThread th : core.threads) {
                try { th.stop(); } catch(Throwable t) {}
                
                if(th.getFinishJob() != null) {
                    try { th.getFinishJob(); } catch(Throwable t) { logError(t); }
                }
                
                try { th.releaseResource(); } catch(Throwable t) {}
            }
            core.threads.clear();
        }
        
        HThreadStatusPanel.releaseAll();
        
        // 커스텀 모듈의 종료 이벤트 호출
        if(core.modules != null) {
        	for(Module m : core.modules) {
            	if(m instanceof CustomAbstractModule) {
            		try { ((CustomAbstractModule) m).release(); } catch(Throwable t) {}
            	}
            }
        }
        
        // 모듈 스토리지 저장
        try { core.saveModuleStorageParents(); } catch(Throwable t) { t.printStackTrace(); }
        
        // UI 닫기
        if(core.ui != null) {
            try { core.ui.releaseResource(); } catch(Throwable t) {}
        }
        
        // 0.5초 후 쓰레드 인터럽트 개시 (인터럽트 개시되면 쓰레드 종료 후 수행해야 할 작업도 수행하지 않게 됨)
        try { Thread.sleep(500); } catch(Throwable ignores) {}
        interruptAll = true;
        // 인터럽트를 개시해도 현재 반복 차례를 종료해야 쓰레드가 종료되므로 0.5초 더 기다림
        try { Thread.sleep(500); } catch(Throwable ignores) {}
        
        // 스크립트 엔진 내 전역 객체들 정리
        if(core.globalScriptObjects != null) {
        	for(ScriptObject globalObj : core.globalScriptObjects) {
                try { globalObj.releaseResource(); } catch(Throwable t) {}
            }
            core.globalScriptObjects.clear();
        }
        
        // 개별 스크립트 객체들 정리
        for(ScriptObject eachObj : core.eachScriptObjects) {
            try { eachObj.releaseResource(); } catch(Throwable t) {}
        }
        core.eachScriptObjects.clear();
        
        // 모듈 정리
        for(Module m : core.modules) {
            try { m.releaseResource(); } catch(Throwable t) {}
        }
        core.modules.clear();
        
        // 외부 라이브러리 박스 정리
        for(LibraryBox b : core.libBoxes) {
        	try { b.releaseResource(); } catch(Throwable t) {}
        }
        core.libBoxes.clear();
        
        // 기타 리소스 정리
        for(Releasable rel : core.resources) {
            try { rel.releaseResource(); } catch(Throwable t) {}
        }
        core.resources.clear();
        
        // 임시 스크립트 엔진 저장소 비우기
        if(core.storeEnginesTemp != null)
            core.storeEnginesTemp.clear();
        
        // 임시폴더 비우기
        cleanTemporaryPath();
        
        // 설정 비우기
        core.prop.clear();
        
        // 변수들 비우기
        core.resources           = null;
        core.moduleStorages      = null;
        core.storeEnginesTemp    = null;
        core.threads             = null;
        core.resources           = null;
        core.modules             = null;
        core.globalEngineManager = null;
        core.globalScriptObjects = null;
        core.eachScriptObjects   = null;
        core.libBoxes            = null;
        core.ui                  = null;
        
        // 준비 상태표시 되돌리기
        core.isPrepared = false;
        
        // 인터럽트 돌려놓기
        interruptAll = false;
    }
    
    /**
     * 프로그램을 완전히 종료합니다.
     */
    public static void exit() {
    	try { disposeAll(); } catch(Throwable t) { t.printStackTrace(); }
        System.exit(0);
    }
    
    /** 응용 프로그램을 다시 시작합니다. */
    public static void restart() {
    	disposeAll();
    	try { Thread.sleep(1000); } catch(Throwable t) {}
    	try {
    		Application.run(core.appShortName, core.appLongName, core.APP_VERSION, core.preparedArgs);
    	} catch(Throwable t) {
    		t.printStackTrace();
    		System.exit(0);
    	}
    }
    
    /** 쓰레드 중단 표식 */
    protected static boolean interruptAll = false;
    /** 쓰레드에서 호출됩니다. true 반환 시 쓰레드가 중단됩니다. */
    public static boolean checkInterrupt(Class<?> classObj, String location) {
        return interruptAll;
    }
    
    /**
     * 설정 폴더 경로를 반환합니다.
     */
    public String getConfigPath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + appShortName + File.separator;
    }
    
    /**
     * 모듈 경로를 반환합니다.
     */
    public String getModulePath() {
        return getConfigPath() + File.separator + "modules" + File.separator;
    }
    
    /**
     * 설정을 불러옵니다.
     */
    protected void loadConfig() {
        File configDir = new File(getConfigPath());
        if(! configDir.exists()) configDir.mkdirs();
        
        try {
            File configFile = FileUtil.fileIn(configDir, "config.xml", true);
            prop = FileUtil.loadProperties(configFile, true, ResourceManager.getDefaultConfig());
        } catch(Throwable t) {
            logError(t);
        }
    }
    
    /**
     * 스트링 테이블을 불러옵니다.
     */
    protected void loadStringTable() {
        File configDir = new File(getConfigPath());
        if(! configDir.exists()) configDir.mkdirs();
        
        File configFile = null;
        try {
            
            String customStringTableOpt = getProperty("string_table");
            if(! DataUtil.isEmpty(customStringTableOpt)) {
                configFile = new File(customStringTableOpt);
            } else {
                String fileName = "stringTable.xml";
                configFile = FileUtil.fileIn(configDir, fileName, true);
            }
            
            stringTable = FileUtil.loadProperties(configFile, true, ResourceManager.getDefaultStringTable());
        } catch(Throwable t) {
            logError(t);
        }
    }
    
    /** 모듈에서 사용되는 스토리지 데이터를 한꺼번에 불러옵니다. */
    protected Properties loadModuleStorageParents() {
        File configDir = new File(getConfigPath());
        if(! configDir.exists()) configDir.mkdirs();
        
        try {
            File configFile = FileUtil.fileIn(configDir, "storage_contents.xml", true);
            Properties parentProp = FileUtil.loadProperties(configFile, true);
            return parentProp;
        } catch(Throwable t) {
            logError(t);
        }
        return new Properties();
    }
    
    /** 모듈에서 사용되는 스토리지 데이터를 한꺼번에 저장합니다. */
    protected void saveModuleStorageParents() {
        Properties parentProp = new Properties();
        
        Set<Long> keysOfIds = moduleStorages.keySet();
        for(Long id : keysOfIds) {
            try {
                Map<String, String> storageObj = moduleStorages.get(id);
                if(storageObj == null) continue;
                
                Properties storageProp = new Properties();
                Set<String> keysOf = storageObj.keySet();
                for(String k : keysOf) {
                    if(storageObj.get(k) == null) continue;
                    storageProp.setProperty(k, storageObj.get(k));
                }
                
                String xmlContents = DataUtil.toXML(storageProp);
                parentProp.setProperty(id.toString(), xmlContents);
            } catch(Throwable t) {
                logError(t);
            }
        }
        
        File configDir = new File(getConfigPath());
        if(! configDir.exists()) configDir.mkdirs();
        
        OutputStream stream = null;
        try {
            File configFile = FileUtil.fileIn(configDir, "storage_contents.xml", true);
            stream = new FileOutputStream(configFile);
            parentProp.storeToXML(stream, "");
            stream.close();
            stream = null;
        } catch(Throwable t) {
            logError(t);
        } finally {
            if(stream == null) return;
            try { stream.close(); } catch(Throwable ignores) {}
        }
    }
    
    /**
     * 설정 값을 반환합니다.
     * 
     * @param key : 필요한 설정 키
     * @return 설정 값
     */
    public static String getProperty(String key) {
    	String propOpt = getCore().prop.getProperty(key);
    	if(propOpt == null) getCore().prop.setProperty(key, "");
        return propOpt;
    }
    
    /**
     * 설정 키가 존재하는지 확인합니다.
     * 
     * @param key : 확인할 설정 키
     * @return 존재 여부
     */
    public static boolean hasPropertyKey(String key) {
    	return getCore().prop.containsKey(key);
    }
    
    /**
     * Properties 를 편집할 수 있는 컴포넌트에 현재 설정값들을 넘겨줍니다.
     * 
     * @param editorComponent : 컴포넌트
     */
    public static void sendPropertiesOnComponent(PropertiesEditor editorComponent) {
    	Properties newProp = new Properties();
    	Set<String> keys = getCore().prop.stringPropertyNames();
    	for(String k : keys) {
    		newProp.setProperty(k, getCore().prop.getProperty(k));
    	}
    	editorComponent.setProp(newProp);
    }
    
    /**
     * Properties 편집 컴포넌트로부터 설정값들을 넘겨받습니다.
     * 
     * @param editorComponent : 컴포넌트
     */
    public static void setPropertiesFromComponent(PropertiesEditor editorComponent) {
    	getCore().prop = editorComponent.getProp();
    }
    
    /**
     * Properties 편집 컴포넌트로부터 설정값들을 넘겨받고 파일에도 저장합니다.
     * 
     * @param editorComponent : 컴포넌트
     */
    public static void saveConfig(PropertiesEditor editorComponent) {
    	File configDir = new File(getCore().getConfigPath());
        if(! configDir.exists()) configDir.mkdirs();
        
        try {
            File configFile = FileUtil.fileIn(configDir, "config.xml", true);
            Properties propGets = editorComponent.getProp();
            FileUtil.saveProperties(configFile, propGets);
            getCore().prop = propGets;
        } catch(Throwable t) {
            logError(t);
        }
    }
    
    /**
     * 스트링 테이블을 통해 입력받은 문자열을 번역합니다. 스트링 테이블에서 찾을 수 없으면 원본이 그대로 반환됩니다.
     * 
     * @param originals : 원본 문자열
     * @return 번역된 문자열
     */
    public static String trans(String originals) {
    	if(getCore() == null) return originals;
    	if(getCore().stringTable == null) return originals;
        return getCore().stringTable.getProperty(originals, originals);
    }
    
    /**
     * 파일로부터 사용자 정의 모듈을 불러옵니다.
     */
    public CustomAbstractModule readModuleFromFile(File f) {
    	return new CustomModule(f, newEngine(""), globalAccessKey);
    }
    
    /**
     * 모듈을 불러옵니다.
     */
    protected void loadModules() {
        File moduleDir = new File(getModulePath());
        if(! moduleDir.exists()) moduleDir.mkdirs();
        File[] moduleList = moduleDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String path = pathname.getAbsolutePath();
                path = path.toLowerCase();
                return path.endsWith(".xmodule") || path.endsWith(".zmodule") || path.endsWith(".pmodule") || path.endsWith(".jmodule") || path.endsWith(".zjmodule");
            }
        });
        
        loadBuiltinModules();
        loadClassModules();
        
        // Library Box 불러오기
        for(LibraryBox box : libBoxes) {
        	try {
        		List<Module> boxModules = box.advancedModules(this);
            	if(boxModules != null) {
            		modules.addAll(boxModules);
            	}
        	} catch(Throwable t) {
        		logError(t);
        	}
        }
        
        // 파일에서 불러오기
        for(File f : moduleList) {
            CustomAbstractModule m = readModuleFromFile(f);
            modules.add(m);
        }
        
        Properties parentProp = loadModuleStorageParents();
        
        for(Module m : modules) {
        	moduleLoadCommonProcess(parentProp, m);
        }
    }
    
    private void moduleLoadCommonProcess(Properties parentProp, Module m) {
    	Map<String, String> storage = new HashMap<String, String>();
        
        String keyStr = String.valueOf(m.getId());
        if(parentProp.containsKey(keyStr)) {
            String xmlContents = parentProp.getProperty(keyStr);
            Properties storageProp = DataUtil.fromXML(xmlContents);
            if(storageProp == null) storageProp = new Properties();
            
            Set<String> keys = storageProp.stringPropertyNames();
            for(String k : keys) {
                storage.put(k, storageProp.getProperty(k));
            }
        }
        
        m.prepareStorage(storage);
        moduleStorages.put(new Long(m.getId()), storage);
        
        m.initFirst();
        m.initThread(this);
    }
    
    /** 실행이 완전히 완료된 이후에 외부 모듈을 불러올 때 사용합니다. */
    public boolean loadModuleOnRuntime(Module m, boolean attachUI) {
    	if(modules.contains(m)) return false;
    	modules.add(m);
    	
    	Properties parentProp = loadModuleStorageParents();
    	moduleLoadCommonProcess(parentProp, m);
    	
    	if(ui != null && attachUI) ui.attach(m);
        
        return true;
    }
    
    /** 기본 제공 모듈을 불러옵니다. */
    protected void loadBuiltinModules() {
    	modules.addAll(BuiltinModule.getBuiltinModules(this));
    }
    
    /** Java 컴파일된 모듈을 불러옵니다. */
    @SuppressWarnings("unchecked")
    protected void loadClassModules() {
        String classModuleOpt = getProperty("module_classes");
        if(DataUtil.isEmpty(classModuleOpt)) return;
        
        StringTokenizer semicolonTokenizer = new StringTokenizer(classModuleOpt, ";");
        while(semicolonTokenizer.hasMoreTokens()) {
            try {
                Class<? extends Module> classObj = (Class<? extends Module>) Class.forName(semicolonTokenizer.nextToken().trim());
                Module moduleInstance = classObj.newInstance();
                if(moduleInstance instanceof AdvancedModule) {
                    AdvancedModule advModule = (AdvancedModule) moduleInstance;
                    HScriptEngine newEngine = newEngine("");
                    newEngine.setInfoFromModule(advModule);
                    advModule.prepare(this, newEngine, globalAccessKey);
                }
                modules.add(moduleInstance);
            } catch(Throwable t) {
                logError(t);
            }
        }
    }
    
    /** Java 컴파일된 스크립트 객체들을 불러옵니다. */
    @SuppressWarnings("unchecked")
    protected void loadClassScriptObjects() {
        String classModuleOpt = getProperty("script_classes");
        if(DataUtil.isEmpty(classModuleOpt)) return;
        
        StringTokenizer semicolonTokenizer = new StringTokenizer(classModuleOpt, ";");
        while(semicolonTokenizer.hasMoreTokens()) {
            try {
                Class<? extends ScriptObject> classObj = (Class<? extends ScriptObject>) Class.forName(semicolonTokenizer.nextToken().trim());
                ScriptObject scriptObjectInstances = classObj.newInstance();
                globalScriptObjects.add(scriptObjectInstances);
            } catch(Throwable t) {
                logError(t);
            }
        }
    }
    
    /**
     * 현재 설정된 UI 객체를 반환합니다.
     */
    public UI getUI() {
    	return this.ui;
    }
    
    /**
     * UI 객체를 설정합니다.
     */
    public UI prepareUI(Class<? extends UI> uiClass) throws InstantiationException, IllegalAccessException {
    	if(this.ui != null) {
    		this.ui.releaseResource();
    		this.ui = null;
    	}
    	
    	UI uiObj = uiClass.newInstance();
    	uiObj.init(this);
        
        UIObject uiManager = new UIObject(uiObj);
        globalScriptObjects.add(uiManager);
        globalEngineManager.put(uiManager.getPrefixName() + "_" + globalAccessKey, uiManager);
        
        if(storeEnginesTemp != null) {
            // 기존에 이미 getInitScript 호출한 엔진이 있으면 다시 호출해줌
            for(HScriptEngine engine : storeEnginesTemp) {
                try { engine.eval(getInitScript()); } catch(Throwable t) { logError(t); }
                prepareUIEachObjects(engine);
            }
            
            storeEnginesTemp.clear();
            storeEnginesTemp = null;
        }
        
        for(Module m : modules) {
        	uiObj.attach(m);
        }
        
        for(LibraryBox p : libBoxes) {
    		try { p.onPrepareUI(this, uiObj); } catch(Throwable t) { logError(t); }
    	}
        
        log("UI system is prepared.");
        this.ui = uiObj;
        return uiObj;
    }
    
    /** 스크립트 엔진 별로 따로 처리해야 하는 UI 객체들을 설정합니다. */
    protected void prepareUIEachObjects(HScriptEngine engine) {
        ScriptObject obj = new EventHandlerCreator(engine);
        engine.put(obj.getPrefixName() + "_" + globalAccessKey, obj);
        try { engine.eval(obj.getInitScript(globalAccessKey)); } catch(Throwable t) {  logError(t); }
        eachScriptObjects.add(obj);
        
        obj = new RuntimeObject(this, engine);
        engine.put(obj.getPrefixName() + "_" + globalAccessKey, obj);
        try { engine.eval(obj.getInitScript(globalAccessKey)); } catch(Throwable t) {  logError(t); }
        eachScriptObjects.add(obj);
        
        obj = new WebObject(engine);
        engine.put(obj.getPrefixName() + "_" + globalAccessKey, obj);
        try { engine.eval(obj.getInitScript(globalAccessKey)); } catch(Throwable t) {  logError(t); }
        eachScriptObjects.add(obj);
    }
    
    /**
     * 새로운 쓰레드 객체를 반환합니다.
     */
    public HThread newThread(String name, RiskyRunnable onThread, RiskyRunnable onFinished) {
        HThread thread = new HThread(onThread, onFinished);
        name = name.trim();
        thread.setName(name);
        threads.add(thread);
        if(taskManager != null) taskManager.setThreadList(this, threads);
        return thread;
    }
    /**
     * 새로운 스크립트 기반 쓰레드 객체를 반환합니다.
     */
    public ScriptThread newThread(HScriptEngine engine, String name, String scriptOnThread, String scriptOnEnd) {
        ScriptThread thread = new ScriptThread(scriptOnThread, scriptOnEnd, engine);
        name = name.trim();
        thread.setName(name);
        threads.add(thread);
        if(taskManager != null) taskManager.setThreadList(this, threads);
        return thread;
    }
    
    
    /** 커스텀 모듈이 활성화될 때 이 메소드를 호출하여 쓰레드 객체를 받아갑니다. */
    public ModuleThread newModuleThread(Module m, HScriptEngine engine, String name, String scriptOnThread, String scriptOnEnd) {
        ModuleThread thread = new ModuleThread(m.getId(), scriptOnThread, scriptOnEnd, engine);
        name = name.trim();
        thread.setName(name);
        threads.add(thread);
        return thread;
    }
    
    /** 시스템 관련 정보들이 담긴 Map 을 반환합니다. */
    public static Map<String, String> getSystemInfo() {
        Map<String, String> basicInfo = new HashMap<String, String>();
        basicInfo.put("hjow_lib"         , "hjow_lib version " + VERSION_STRING);
        basicInfo.put("app.version"      , core.APP_VERSION);
        basicInfo.put("application_name" , getCore().appLongName);
        basicInfo.put("app_short_name"   , getCore().appShortName);
        basicInfo.put("path.separator"   , System.getProperty("path.separator"));
        basicInfo.put("file.separator"   , System.getProperty("file.separator"));
        basicInfo.put("os.name"          , System.getProperty("os.name"));
        basicInfo.put("os.arch"          , System.getProperty("os.arch"));
        basicInfo.put("user.name"        , System.getProperty("user.name"));
        basicInfo.put("user.country"     , System.getProperty("user.country"));
        basicInfo.put("user.language"    , System.getProperty("user.language"));
        basicInfo.put("java.version"     , System.getProperty("java.version"));
        basicInfo.put("java.home"        , System.getProperty("java.home"));
        basicInfo.put("java.runtime.name", System.getProperty("java.runtime.name"));
        basicInfo.put("java.vm.version"  , System.getProperty("java.vm.version"));
        basicInfo.put("java.vm.vendor"   , System.getProperty("java.vm.vendor"));
        basicInfo.put("java.io.tmpdir"   , System.getProperty("java.io.tmpdir"));
        basicInfo.put("file.separator"   , System.getProperty("file.separator"));
        return basicInfo;
    }
    
    /** 임시 폴더 경로를 반환합니다. */
    public static File getTemporaryPath() {
        Map<String, String> basicInfo = getSystemInfo();
        
        String tmpDir = basicInfo.get("java.io.tmpdir");
        if(! DataUtil.isEmpty(tmpDir)) {
            tmpDir = tmpDir.replace("/", File.separator);
            tmpDir = tmpDir.trim();
            
            if(! tmpDir.endsWith(File.separator)) tmpDir += File.separator;
            tmpDir += getCore().appShortName + File.separator + "temp";
            
            return new File(tmpDir);
        }
        
        return null;
    }
    
    /** 임시 폴더를 비웁니다. */
    public static void cleanTemporaryPath() {
        File tempPath = getTemporaryPath();
        if(tempPath == null) return;
        if(! tempPath.exists()) return;
        
        File[] lists = tempPath.listFiles();
        for(File f : lists) {
            FileUtil.delete(f);
        }
    }
    
    /** 스크립트 엔진에서 파일 읽기 액세스가 가능한 파일인지 확인합니다. */
    public static boolean canAccessReadOnScript(File file) {
        String paths = file.getAbsolutePath();
        paths = paths.replace("/", File.separator);
        paths = paths.trim();
        
        File tempPath = getTemporaryPath();
        
        if(tempPath != null && paths.startsWith(tempPath.getAbsolutePath().replace("/", File.separator).trim())) return true;
        if(paths.startsWith(getCore().getConfigPath())) return true;
        
        return false;
    }
    
    /** 스크립트 엔진에서 파일 쓰기 액세스가 가능한 파일인지 확인합니다. */
    public static boolean canAccessWriteOnScript(File file) {
        String paths = file.getAbsolutePath();
        paths = paths.replace("../", "");
        paths = paths.replace("/", File.separator);
        paths = paths.trim();
        
        File tempPath = getTemporaryPath();
        
        if(tempPath != null && paths.startsWith(tempPath.getAbsolutePath().replace("/", File.separator).trim())) return true;
        
        return false;
    }
    
    /** 로그 출력 */
    public static synchronized void log(Object obj) {
    	String str = String.valueOf(obj);
    	
        UI ui = getCore().ui;
        if(ui != null) {
        	if(logQueue != null && logQueue.size() >= 1) {
        		for(String s : logQueue) {
        			ui.log(s);
        		}
        		logQueue.clear();
        	}
        	ui.log(str);
        } else {
        	if(logQueue == null) logQueue = new Vector<String>();
        	if(logQueue.size() >= 100) {
        		logQueue.remove(0);
        	}
        	logQueue.add(str);
        }
        System.out.println(obj);
    }
    
    /** 오류 발생 로그 출력 */
    public static void logError(Throwable t) {
        log(DataUtil.traceException(t));
    }
}
