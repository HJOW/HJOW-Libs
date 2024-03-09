package hjow.common.json;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*

Copyright 2024 HJOW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


With json.simple libs, more features available.
json.simple : https://code.google.com/archive/p/json-simple/

*/
public class JsonCompatibleUtil {
	/** json.simple 라이브러리 사용 가능여부 반환 */
    public static boolean isJsonSimpleAvail() {
    	try { 
    		Class.forName("org.json.simple.parser.JSONParser");
    		return true;
    	} catch(ClassNotFoundException e) {
    		return false;
    	}
    }
    
    /** json.simple 에서 제공하는 객체 타입 JSONObject, JSONArray 를 각각 JsonObject, JsonArray 로 변환 */
    public static JsonInstance toJsonInstance(Object obj) {
    	if(obj == null) return null;
    	if(obj instanceof Map<?, ?>) {
    		JsonObject json = new JsonObject();
    		Map<?, ?> map = (Map<?, ?>) obj;
    		Set<?> keys = map.keySet();
    		for(Object o : keys) {
    			json.put(o.toString(), map.get(o));
    		}
    		return json;
    	}
    	if(obj instanceof List<?>) {
    		JsonArray arr = new JsonArray();
    		List<?> list = (List<?>) obj;
    		for(int idx=0; idx<list.size(); idx++) {
    			arr.add(list.get(idx));
    		}
    		return arr;
    	}
    	if(obj instanceof Iterator<?>) {
    		JsonArray arr = new JsonArray();
    		Iterator<?> ite = (Iterator<?>) obj;
    		while(ite.hasNext()) {
    			arr.add(ite.next());
    		}
    		return arr;
    	}
    	throw new RuntimeException("Unsupported parameter type");
    }
    
    private static boolean warnJsonParsingOther = true;
    /** 문자열을 Json 객체로 변환. json.simple 라이브러리 사용 가능 시 해당 라이브러리로 변환 시도 */
    public static Object parseJson(Object jsonobj) {
    	if(jsonobj == null) return null;
    	
    	if(jsonobj instanceof JsonObject) return jsonobj;
    	if(jsonobj instanceof JsonArray ) return jsonobj;
    	
    	String jsonStr = jsonobj.toString().trim();
    	
    	if(isJsonSimpleAvail()) {
    		try {
    			Class<?> classParser = Class.forName("org.json.simple.parser.JSONParser");
        		Object parserInst = classParser.newInstance();
        		Method parseMthd  = classParser.getMethod("parse", String.class);
        		Object parsed = parseMthd.invoke(parserInst, jsonStr);
        		return toJsonInstance(parsed);
    		} catch(ClassNotFoundException ex) {
    			if(warnJsonParsingOther) System.out.println("Exception when JSON parsing by json.simple libs. " + ex.getMessage() + " Trying to parse other method...");
    			warnJsonParsingOther = false;
    		} catch(Throwable ex) {
    			throw new RuntimeException(ex.getMessage(), ex);
    		}
    	}
    	return JsonObject.parseJson(jsonStr);
    }
}
