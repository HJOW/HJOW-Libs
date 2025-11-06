package org.duckdns.hjow.commons.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.duckdns.hjow.commons.exception.KnownRuntimeException;

/** 스크립트 관련 Util */
public class ScriptUtil {
	/** 스크립트 주요 문법 변환, 오리지널 스크립트는 JavaScript 언어로 넣어야 함. */
    public static String convert(String originalScripts, String targetLanguage) {
    	if(targetLanguage == null) targetLanguage = "javascript";
    	targetLanguage = targetLanguage.toLowerCase();
    	if(targetLanguage.equals("javascript") || targetLanguage.equals("js") || targetLanguage.equals("ecmascript") || targetLanguage.equals("nashorn")) return originalScripts;
    	
    	if(targetLanguage.equals("kotlin") || targetLanguage.equals("kt") || targetLanguage.equals("kts")) {
    		originalScripts = originalScripts.replace("function ", "fun ");
    	}
    	
    	throw new KnownRuntimeException("Unsupported language " + targetLanguage);
    }
    
    /** 해당 언어의 엔진 생성 */
    public static ScriptEngine newEngine(ScriptEngineManager manager, String scriptLanguage) {
    	if(scriptLanguage == null) scriptLanguage = "JavaScript";
    	
    	// 엔진 생성
        ScriptEngine engine = manager.getEngineByName(scriptLanguage);
        
        if(engine == null && ("JavaScript".equals(scriptLanguage) || "js".equals(scriptLanguage) || "ECMAScript".equals(scriptLanguage))) {
        	scriptLanguage = "nashorn";
        	engine = manager.getEngineByName(scriptLanguage); 
        }
        return engine;
    }
}
