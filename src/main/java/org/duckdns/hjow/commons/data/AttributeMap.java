package org.duckdns.hjow.commons.data;

import java.util.Set;

import org.duckdns.hjow.commons.json.JsonObject;

/** Map<String, String> 대안 클래스, Kotlin 과 사용 시 ClassFormatError 방지 용도 */
public class AttributeMap extends java.util.HashMap<String, Object> {
	private static final long serialVersionUID = 4314017342641734729L;
	public AttributeMap() { super(); }
    
	/** JSON 으로 변환 */
	public JsonObject toJSON() {
		JsonObject json = new JsonObject();
		Set<String> keys = keySet();
		for(String k : keys) { json.put(k, get(k)); }
		return json;
	}
}