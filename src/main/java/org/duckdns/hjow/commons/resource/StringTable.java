package org.duckdns.hjow.commons.resource;

import java.io.Serializable;
import java.util.Properties;

/** 언어 지원을 위한 String Table */
public interface StringTable extends Serializable {
	/** 번역된 값을 반환, 단 스트링테이블에 없는 경우 원래 값 그대로 반환 */
	public String t(String originals);
	/** StringTable 이름 반환 */
	public String getName();
	/** StringTable 데이터 전체를 Properties 객체로 반환 */
	public Properties getData();
}
