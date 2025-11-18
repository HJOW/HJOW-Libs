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

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.duckdns.hjow.commons.core.CloneableObject;
import org.duckdns.hjow.commons.util.DataUtil;
import org.duckdns.hjow.commons.util.SyntaxUtil;
import org.duckdns.hjow.commons.xml.XMLSerializableObject;

/** JSON 형식의 데이터를 다룰 수 있는 클래스입니다. 이 클래스의 인스턴스는 하나의 JSON 배열의 정보를 포함합니다. */
public class JsonArray extends XMLSerializableObject implements JsonInstance, List<Object> {
    private static final long serialVersionUID = -7917612834023503642L;
    protected List<Object> data;
    
    public JsonArray() {
        data = new ArrayList<Object>();
    }
    
    /** 원소를 삽입합니다. 컬렉션 객체인 경우, JsonObject 혹은 JsonArray 로 변환되어 들어갑니다. */
    @Override
    public boolean add(Object obj) {
        if(obj == null) {
            data.add(null);
            return true;
        }
        if(obj instanceof JsonInstance) {
            data.add(obj);
            return true;
        }
        if(obj instanceof Number) {
            if(obj instanceof Integer || obj instanceof Long || obj instanceof BigInteger) {
                obj = ((Number) obj).longValue();
            } else {
                obj = ((Number) obj).doubleValue();
            }
            data.add(obj);
            return true;
        }
        if(obj instanceof Boolean) {
            data.add(obj);
            return true;
        }
        if(obj instanceof CharSequence) {
            data.add(obj.toString());
            return true;
        }
        if(obj instanceof List<?>) {
            JsonArray newArr = new JsonArray();
            for(Object objOne : ((List<?>) obj)) {
                newArr.add(objOne);
            }
            data.add(newArr);
            return true;
        }
        if(obj instanceof Map<?, ?>) {
            JsonObject newObj = new JsonObject();
            Set<?> keys = ((Map<?, ?>) obj).keySet();
            for(Object k : keys) {
                newObj.put(String.valueOf(k), ((Map<?, ?>) obj).get(k));
            }
            
            data.add(newObj);
            return true;
        }
        if(obj instanceof Date) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            obj = formatter.format((Date) obj);
            data.add(obj);
            return true;
        }
        add(String.valueOf(obj));
        return true;
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
        return toJSON(false, false);
    }
    
    @Override
    public String toJSON(boolean lookFine) {
        return toJSON("    ", false, true);
    }

    @Override
    public String toJSON(boolean allowLineJumpString, boolean lookFine) {
    	if(lookFine) return toJSON("    ", allowLineJumpString, lookFine);
        return toJSON("", allowLineJumpString, lookFine);
    }
    
    @Override
    public String toJSON(String indent, boolean allowLineJumpString, boolean lookFine) {
        if(indent == null) indent = "";
        String indentNext = "";
        if(indent.equals("")) indentNext = indent + "    ";
        
        StringBuilder resultString = new StringBuilder("");
        resultString = resultString.append(indent).append("[");
        boolean isFirst = true;
        for(Object target : data) {
            if(lookFine) resultString = resultString.append("\n");
            resultString = resultString.append(indentNext);
            if(! isFirst) resultString = resultString.append(",");
            
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
        resultString = resultString.append(indent).append("]");
        String res = resultString.toString();
        resultString = null;
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

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    @Override
    public Iterator<Object> iterator() {
        return data.iterator();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(o);
    }

    @Override
    public Object[] toArray() {
        return data.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return data.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return data.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return data.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return data.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
        return data.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return data.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return data.retainAll(c);
    }

    @Override
    public Object set(int index, Object element) {
        Object bef = null;
        if(data.size() > index) bef = data.get(index);
        if(element == null) {
            data.set(index, null);
            return bef;
        }
        if(element instanceof JsonInstance) {
            data.set(index, element);
            return bef;
        }
        if(element instanceof Number) {
            if(element instanceof Integer || element instanceof Long || element instanceof BigInteger) {
                element = ((Number) element).longValue();
            } else {
                element = ((Number) element).doubleValue();
            }
            data.set(index, element);
            return bef;
        }
        if(element instanceof Boolean) {
            data.set(index, element);
            return bef;
        }
        if(element instanceof CharSequence) {
            data.set(index, element.toString());
            return bef;
        }
        if(element instanceof List<?>) {
            JsonArray newArr = new JsonArray();
            for(Object elementOne : ((List<?>) element)) {
                newArr.add(elementOne);
            }
            data.set(index, newArr);
            return bef;
        }
        if(element instanceof Map<?, ?>) {
            JsonObject newObj = new JsonObject();
            Set<?> keys = ((Map<?, ?>) element).keySet();
            for(Object k : keys) {
                newObj.put(String.valueOf(k), ((Map<?, ?>) element).get(k));
            }
            
            data.set(index, newObj);
            return bef;
        }
        if(element instanceof Date) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            element = formatter.format((Date) element);
            data.set(index, element);
            return bef;
        }
        set(index, String.valueOf(element));
        return bef;
    }

    @Override
    public void add(int index, Object element) {
        if(element == null) {
            data.add(index, null);
            return;
        }
        if(element instanceof JsonInstance) {
            data.add(index, element);
            return;
        }
        if(element instanceof Number) {
            if(element instanceof Integer || element instanceof Long || element instanceof BigInteger) {
                element = ((Number) element).longValue();
            } else {
                element = ((Number) element).doubleValue();
            }
            data.add(index, element);
            return;
        }
        if(element instanceof Boolean) {
            data.add(index, element);
            return;
        }
        if(element instanceof CharSequence) {
            data.add(index, element.toString());
            return;
        }
        if(element instanceof List<?>) {
            JsonArray newArr = new JsonArray();
            for(Object elementOne : ((List<?>) element)) {
                newArr.add(elementOne);
            }
            data.add(index, newArr);
            return;
        }
        if(element instanceof Map<?, ?>) {
            JsonObject newObj = new JsonObject();
            Set<?> keys = ((Map<?, ?>) element).keySet();
            for(Object k : keys) {
                newObj.put(String.valueOf(k), ((Map<?, ?>) element).get(k));
            }
            
            data.add(index, newObj);
            return;
        }
        if(element instanceof Date) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            element = formatter.format((Date) element);
            data.add(index, element);
            return;
        }
        add(index, String.valueOf(element));
        return;
    }

    @Override
    public int indexOf(Object o) {
        return data.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return data.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return data.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return data.listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        JsonArray newArr = new JsonArray();
        newArr.setData(data.subList(fromIndex, toIndex));
        return newArr;
    }
    
    /** 자기 자신 반환 */
    public JsonArray getSelf() { return this; }

	@Override
	public Object cloneThis() {
		JsonArray newOne = new JsonArray();
		for(Object obj : this) {
			if(obj instanceof CloneableObject) {
				obj = ((CloneableObject) obj).cloneThis();
			}
			
			newOne.add(obj);
		}
		return newOne;
	}
}
