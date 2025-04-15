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
package org.duckdns.hjow.commons.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.duckdns.hjow.commons.script.PublicMethodOpenedClass;
import org.duckdns.hjow.commons.util.DataUtil;
import org.duckdns.hjow.commons.util.SyntaxUtil;

/** JSON 형식의 데이터를 다룰 수 있는 클래스입니다. 이 클래스의 인스턴스는 하나의 JSON 객체의 정보를 포함합니다. */
public class JsonObject extends PublicMethodOpenedClass implements JsonInstance, Map<String, Object> {
    private static final long serialVersionUID = 461225513560905903L;
    protected Map<String, Object> data;
    
    public JsonObject() {
        data = new HashMap<String, Object>();
    }
    
    /** 객체 내에 원소를 삽입합니다. 컬렉션 객체인 경우, JsonObject 혹은 JsonArray 로 변환되어 들어갑니다. */
    @Override
    public Object put(String key, Object obj) {
        Object bef = null;
        if(data != null) bef = get(key);
        if(obj == null) {
            data.put(key, null);
            return bef;
        }
        if(obj instanceof JsonInstance) {
            data.put(key, obj);
            return bef;
        }
        if(obj instanceof Number) {
            if(obj instanceof Integer || obj instanceof Long || obj instanceof BigInteger) {
                obj = ((Number) obj).longValue();
            } else {
                obj = ((Number) obj).doubleValue();
            }
            data.put(key, obj);
            return bef;
        }
        if(obj instanceof Boolean) {
            data.put(key, obj);
            return bef;
        }
        if(obj instanceof CharSequence) {
            data.put(key, obj.toString());
            return bef;
        }
        if(obj instanceof List<?>) {
            JsonArray newArr = new JsonArray();
            for(Object objOne : ((List<?>) obj)) {
                newArr.add(objOne);
            }
            data.put(key, newArr);
            return bef;
        }
        if(obj instanceof Map<?, ?>) {
            JsonObject newObj = new JsonObject();
            Set<?> keys = ((Map<?, ?>) obj).keySet();
            for(Object k : keys) {
                newObj.put(String.valueOf(k), ((Map<?, ?>) obj).get(k));
            }
            
            data.put(key, newObj);
            return bef;
        }
        if(obj instanceof Date) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            obj = formatter.format((Date) obj);
            data.put(key, obj);
            return bef;
        }
        put(key, String.valueOf(obj));
        return bef;
    }
    
    /** 객체 내에서 키를 이용해 원소를 찾아 반환합니다. */
    @Override
    public Object get(Object key) {
        if(key == null) return null;
        key = key.toString();
        if(! data.containsKey(key)) return null;
        return data.get(key);
    }
    
    /** 객체 내에 있는 키들을 반환합니다. */
    public Set<String> keySet() {
        return data.keySet();
    }
    
    /** 객체 내에 해당 키에 대한 원소를 객체에서 제거합니다. */
    public Object remove(String key) {
        return data.remove(key);
    }
    
    /** 객체 내에 원소가 하나라도 있으면 true 가 반환됩니다. */
    public boolean isEmpty() {
        return data.isEmpty();
    }
    
    /** 객체 내 원소들을 객체에서 전부 제거합니다. */
    public void clear() {
        data.clear();
    }
    
    @Override
    public String toJSON() {
        return toJSON(false, false);
    }

    @Override
    public String toJSON(boolean allowLineJumpString, boolean lookFine) {
        return toJSON("", allowLineJumpString, lookFine);
    }
    
    @Override
    public String toJSON(String indent, boolean allowLineJumpString, boolean lookFine) {
        if(indent == null) indent = "";
        String indentNext = "";
        if(indent.equals("")) indentNext = indent + "    ";
        
        Set<String> keys = keySet();
        StringBuilder resultString = new StringBuilder("");
        resultString = resultString.append(indent).append("{");
        boolean isFirst = true;
        for(String k : keys) {
            if(lookFine) resultString = resultString.append("\n");
            resultString = resultString.append(indentNext);
            if(! isFirst) resultString = resultString.append(",");
            
            resultString = resultString.append("\"" + DataUtil.castQuote(true, k) + "\"").append(":");
            
            Object target = get(k);
            if(target instanceof JsonInstance) {
                target = ((JsonInstance) target).toJSON(indentNext, allowLineJumpString, lookFine);
            } else if(target instanceof CharSequence) {
                String content = DataUtil.castQuote(true, target.toString());
                if(! allowLineJumpString) content = SyntaxUtil.lineSinglize(content);
                target = "\"" + content + "\"";
            }
            resultString = resultString.append(target);
            
            isFirst = false;
        }
        if(lookFine) resultString = resultString.append("\n");
        resultString = resultString.append(indent).append("}");
        String res = resultString.toString();
        resultString = null;
        return indent + res;
    }
    
    @Override
    public String toString() {
        return "/* JSON Object */" + toJSON();
    }
    
    /** Map 객체로 변환합니다. */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> keys = keySet();
        
        for(String k : keys) {
            Object obj = get(k);
            if(obj instanceof JsonArray) {
                obj = ((JsonArray) obj).toList();
            } else if(obj instanceof JsonObject) {
                obj = ((JsonObject) obj).toMap();
            }
            map.put(k, obj);
        }
        
        return map;
    }
    
    @Override
    public JsonInstance cloneObject() {
        JsonObject map = new JsonObject();
        Set<String> keys = keySet();
        
        for(String k : keys) {
            Object obj = get(k);
            map.put(k, obj);
        }
        
        return map;
    }
    
    /** JSON 형식의 문자열을 객체 형태로 변환합니다. */
    public static Object parseJson(String jsonStr) {
        return parseJson(jsonStr, false);
    }
    
    /** JSON 형식의 문자열을 객체 형태로 변환합니다. ignorePrimitiveCaseQuotion 가 true 이면, Primitive 값이 들어왔을 때 문자열(숫자가 아닌)이면서 따옴표로 둘러싸이지 않으면 문자열로 취급합니다. */
    protected static Object parseJson(String jsonStr, boolean ignorePrimitiveCaseQuotion) {
        return parseJson(jsonStr, ignorePrimitiveCaseQuotion, false);
    }
    
    /** JSON 형식의 문자열을 객체 형태로 변환합니다. ignorePrimitiveCaseQuotion 가 true 이면, Primitive 값이 들어왔을 때 문자열(숫자가 아닌)이면서 따옴표로 둘러싸이지 않으면 문자열로 취급합니다. */
    protected static Object parseJson(String jsonStr, boolean ignorePrimitiveCaseQuotion, boolean noLineMultilize) {
        if(jsonStr == null) return null;
        
        String jsonTrim = jsonStr.trim();
        if(! noLineMultilize) jsonTrim = SyntaxUtil.lineMultilize(jsonTrim);
        
        // Primitive 처리
        if(jsonTrim.equals("")) return null;
        if(jsonTrim.startsWith("\"") || jsonTrim.startsWith("'")) {
            int indexOfBlock = -1;
            if(jsonTrim.startsWith("\"")) {
                indexOfBlock = jsonTrim.lastIndexOf("\"");
            } else {
                indexOfBlock = jsonTrim.lastIndexOf("'");
            }
            String insides = jsonTrim.substring(1, indexOfBlock);
            
            return DataUtil.reCastQuote(jsonTrim.startsWith("\""), insides);
        } else if(! (jsonTrim.startsWith("{") || jsonTrim.startsWith("["))) {
            if(jsonTrim.equals("null")) return null;
            if(jsonTrim.equals("true") || jsonTrim.equals("false")) return new Boolean(jsonTrim);
            if(jsonTrim.indexOf(".") >= 0) return new BigDecimal(jsonTrim);
            if(ignorePrimitiveCaseQuotion) {
                if(! DataUtil.isInteger(jsonTrim)) {
                    return jsonTrim;
                }
            }
            return new Long(jsonTrim);
        }
        
        // 배열 처리
        if(jsonTrim.startsWith("[")) {
            int indexOfBlock = jsonTrim.lastIndexOf("]");
            String insides = jsonTrim.substring(1, indexOfBlock).trim();
            
            JsonArray arrayObj = new JsonArray();
            List<Integer> delimiterPoints = SyntaxUtil.getDelimiterLocations(insides, ',');
            
            if(delimiterPoints.isEmpty()) {
                if(insides.equals("")) return arrayObj;
                arrayObj.add(parseJson(insides, false, true));
                return arrayObj;
            }
            String one = insides.substring(0, delimiterPoints.get(0).intValue());
            arrayObj.add(parseJson(one.trim(), false, true));
            if(delimiterPoints.size() == 1) {
                String others = insides.substring(delimiterPoints.get(0).intValue() + 1);
                arrayObj.add(parseJson(others.trim(), false, true));
                return arrayObj;
            }
            
            for(int idx=0; idx<delimiterPoints.size(); idx++) {
                String others = null;
                if(idx == delimiterPoints.size() - 1) {
                    others = insides.substring(delimiterPoints.get(idx).intValue() + 1);
                } else {
                    others = insides.substring(delimiterPoints.get(idx).intValue() + 1, delimiterPoints.get(idx + 1).intValue());
                }
                arrayObj.add(parseJson(others.trim(), false, true));
            }
            
            return arrayObj;
        }
        
        // 객체 처리
        if(jsonTrim.startsWith("{")) {
            int indexOfBlock = jsonTrim.lastIndexOf("}");
            String insides = jsonTrim.substring(1, indexOfBlock).trim();
            
            List<Integer> delimiterPoints = SyntaxUtil.getDelimiterLocations(insides, ',');
            JsonObject objects = new JsonObject();
            
            if(delimiterPoints.isEmpty()) {
                List<Integer> insidesColonDelimPoints = SyntaxUtil.getDelimiterLocations(insides, ':');
                if(insidesColonDelimPoints.isEmpty()) {
                    if(DataUtil.isEmpty(insides)) ; // Do nothing
                    else objects.put(String.valueOf(parseJson(insides, true, true)), "true");
                } else {
                    objects.put(String.valueOf(parseJson(insides.substring(0, insidesColonDelimPoints.get(0).intValue()).trim(), true, true)), parseJson(insides.substring(insidesColonDelimPoints.get(0).intValue() + 1).trim(), false, true));
                }
                
                return objects;
            }
            
            String one = insides.substring(0, delimiterPoints.get(0).intValue());
            List<Integer> oneColonDelimPoints = SyntaxUtil.getDelimiterLocations(one, ':');
            String keyOf   = null;
            String valueOf = null;
            if(oneColonDelimPoints.isEmpty()) {
                keyOf   = one;
                valueOf = "true";
            } else {
                keyOf   = one.substring(0, oneColonDelimPoints.get(0).intValue()).trim();
                valueOf = one.substring(oneColonDelimPoints.get(0).intValue() + 1).trim();
            }
            objects.put(String.valueOf(parseJson(keyOf, true, true)), parseJson(valueOf, false, true));
            
            for(int idx=0; idx<delimiterPoints.size(); idx++) {
                String others = null;
                int delimiterLocation = delimiterPoints.get(idx).intValue();
                if(idx == delimiterPoints.size() - 1) {
                    others = insides.substring(delimiterLocation + 1);
                } else {
                    int nextDelimiterLocation = delimiterPoints.get(idx + 1).intValue();
                    others = insides.substring(delimiterLocation + 1, nextDelimiterLocation);
                }
                
                List<Integer> otherColonDelimPoints = SyntaxUtil.getDelimiterLocations(others, ':');
                if(otherColonDelimPoints.isEmpty()) {
                    keyOf   = others;
                    valueOf = "true";
                } else {
                    int colonLocation = otherColonDelimPoints.get(0).intValue();
                    keyOf   = others.substring(0, colonLocation).trim();
                    valueOf = others.substring(colonLocation + 1).trim();
                }
                
                objects.put(String.valueOf(parseJson(keyOf, true, true)), parseJson(valueOf));
            }
            
            return objects;
        }
        
        return null;
    }

    @Override
    public Iterator<Object> iterator() {
        List<Object> iteratorList = new ArrayList<Object>();
        Set<String> keys = data.keySet();
        for(String k : keys) {
            ObjectEntry<String, Object> newEntry = new ObjectEntry<String, Object>();
            newEntry.setKey(k);
            newEntry.setValue(data.get(k));
            iteratorList.add(newEntry);
        }
        return iteratorList.iterator();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public Object remove(Object key) {
        return data.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        if(m == null) return;
        Set<? extends String> keys = m.keySet();
        for(String k : keys) {
            put(k, m.get(k));
        }
    }

    @Override
    public Collection<Object> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return data.entrySet();
    }
}
