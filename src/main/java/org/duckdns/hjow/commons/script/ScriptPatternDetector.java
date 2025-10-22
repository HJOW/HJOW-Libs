package org.duckdns.hjow.commons.script;

import java.util.ArrayList;
import java.util.List;

import org.duckdns.hjow.commons.exception.IllegalScriptPatternException;

/** 스크립트에서 특정 패턴 (예: 리플렉션 사용) 탐지 */
public class ScriptPatternDetector {
    public ScriptPatternDetector() {}
    
    /** 리플렉션 사용 여부 검사, 발견 시 IllegalScriptPatternException 예외 발생 */
    public void checkReflection(String str) {
    	List<String> pattern = new ArrayList<String>();
    	pattern.add("Class.");
    	pattern.add(".class");
    	pattern.add(".getClass(");
    	check(str, pattern);
    }
    
    /** 스크립트를 입력받아 패턴 검사, 발견 시 IllegalScriptPatternException 예외 발생 */
    public void check(String str, List<String> pattern) {
    	char[] chars = str.toCharArray();
    	str = null;
    	
    	char quote = ' ';
    	StringBuilder collector = new StringBuilder("");
    	for(int i=0; i<chars.length; i++) {
    		char charOne = chars[i];
    		
    		if(quote == '\'') {
    			if(charOne == '\'') {
    				quote = ' ';
    				// 따옴표 내부 내용은 검사하지 않음
    				collector.setLength(0);
    				continue;
    			} else {
    				collector = collector.append(String.valueOf(charOne));
    			}
    		} else if(quote == '"') {
    			if(charOne == '"') {
    				quote = ' ';
    				// 따옴표 내부 내용은 검사하지 않음
    				collector.setLength(0);
    				continue;
    			} else {
    				collector = collector.append(String.valueOf(charOne));
    			}
    		} else if(quote == '`') {
    			if(charOne == '`') {
    				quote = ' ';
    				// 따옴표 내부 내용은 검사하지 않음
    				collector.setLength(0);
    				continue;
    			} else {
    				collector = collector.append(String.valueOf(charOne));
    			}
    		} else {
    			if(charOne == '\'' || charOne == '"' || charOne == '`') {
    				checkIn(collector.toString().trim(), pattern);
    				collector.setLength(0);
    				quote = charOne;
    				continue;
    			} else {
    				collector = collector.append(String.valueOf(charOne));
    			}
    		}
    	}
    	
    	String lefts = collector.toString().trim();
    	checkIn(lefts, pattern);
    }
    
    /** 따옴표 영역이 제거된 영역 내에서 공백 취급 문자 제거 후 패턴 검사, 발견 시 IllegalScriptPatternException 예외 발생 */
    protected void checkIn(String block, List<String> pattern) {
    	char[] chars = block.toCharArray();
    	block = null;
    	
    	// 공백 취급 문자 (없애도 키워드가 이어질 수 있는 문자) 제거해 다시 취합
    	StringBuilder collector = new StringBuilder("");
    	for(int i=0; i<chars.length; i++) {
    		char charOne = chars[i];
    		if(charOne == ' ' || charOne == '\n' || charOne == '\t' || charOne == '\r' || Character.isSpaceChar(charOne)) continue;
    		collector = collector.append(String.valueOf(charOne));
    	}
    	
    	block = collector.toString().trim();
    	collector.setLength(0);
    	
    	// 패턴 탐지
    	for(String p : pattern) {
    		if(block.contains(p)) throw new IllegalScriptPatternException("Pattern voilation detected. '" + p + "'");
    	}
    }
}