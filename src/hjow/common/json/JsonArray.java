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
package hjow.common.json;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hjow.common.util.DataUtil;

/** JSON 형식의 데이터를 다룰 수 있는 클래스입니다. 이 클래스의 인스턴스는 하나의 JSON 배열의 정보를 포함합니다. */
public class JsonArray implements JsonInstance {
    private static final long serialVersionUID = -7917612834023503642L;
    protected List<Object> data;
    
    public JsonArray() {
        data = new ArrayList<Object>();
    }
    
    /** 원소를 삽입합니다. */
    public void add(Object obj) {
        if(obj == null) {
            data.add(null);
            return;
        }
        if(obj instanceof JsonInstance) {
            data.add(obj);
            return;
        }
        if(obj instanceof Number) {
            if(obj instanceof Integer || obj instanceof Long || obj instanceof BigInteger) {
                obj = ((Number) obj).longValue();
            } else {
                obj = ((Number) obj).doubleValue();
            }
            data.add(obj);
            return;
        }
        if(obj instanceof Boolean) {
            data.add(obj);
            return;
        }
        if(obj instanceof CharSequence) {
            data.add(obj.toString());
            return;
        }
        if(obj instanceof List<?>) {
            JsonArray newArr = new JsonArray();
            for(Object objOne : ((List<?>) obj)) {
                newArr.add(objOne);
            }
            data.add(newArr);
            return;
        }
        if(obj instanceof Map<?, ?>) {
            JsonObject newObj = new JsonObject();
            Set<?> keys = ((Map<?, ?>) obj).keySet();
            for(Object k : keys) {
                newObj.put(String.valueOf(k), ((Map<?, ?>) obj).get(k));
            }
            
            data.add(newObj);
            return;
        }
        if(obj instanceof Date) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            obj = formatter.format((Date) obj);
            data.add(obj);
            return;
        }
        add(String.valueOf(obj));
    }
    
    /** 해당 번째의 원소를 반환합니다. */
    public Object get(int idx) {
        return data.get(idx);
    }
    
    /** 담겨 있는 원소 수를 반환합니다. */
    public int size() {
        return data.size();
    }
    /** 해당 번째의 원소를 배열(리스트)에서 제거합니다. 제거한 원소를 반환합니다. */
    public Object remove(int idx) {
        return data.remove(idx);
    }
    /** 원소들을 전부 배열(리스트)에서 제거합니다. */
    public void clear() {
        data.clear();
    }
    
    @Override
    public JsonInstance cloneObject() {
    	JsonArray newArr = new JsonArray();
    	for(Object obj : data) {
    		newArr.add(obj);
    	}
    	return newArr;
    }
    
    @Override
    public String toJSON() {
    	return toJSON(false);
    }

    @Override
    public String toJSON(boolean allowLineJump) {
        StringBuilder resultString = new StringBuilder("");
        resultString = resultString.append("[");
        boolean isFirst = true;
        for(Object target : data) {
            if(! isFirst) resultString = resultString.append(",");
            
            if(target instanceof JsonInstance) target = ((JsonInstance) target).toJSON();
            else if(target instanceof CharSequence) target = "\"" + DataUtil.castQuote(true, target.toString()) + "\"";
            resultString = resultString.append(target);
            
            isFirst = false;
        }
        resultString = resultString.append("]");
        String res = resultString.toString();
        resultString = null;
        if(! allowLineJump) res = res.replace("\n", "\\" + "n");
        return res;
    }
    
    @Override
    public String toString() {
    	return "/* JSON Array */" + toJSON();
    }
    
    /** List 객체로 변환합니다. */
    public List<Object> toList() {
    	List<Object> list = new ArrayList<Object>();
    	
    	for(Object obj : data) {
    		Object objRes = obj;
    		if(objRes instanceof JsonArray) {
    			objRes = ((JsonArray) objRes).toList();
    		} else if(obj instanceof JsonObject) {
    			objRes = ((JsonObject) objRes).toMap();
    		}
    		list.add(objRes);
    	}
    	
    	return list;
    }

	@Override
	public Iterator<Object> iterator() {
		return data.iterator();
	}
}
