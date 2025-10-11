package org.duckdns.hjow.commons.resource;

import java.util.Properties;

import org.duckdns.hjow.commons.resource.StringTable;

/** 다른 스트링 테이블을 Wrapper 로 포함해 대행하는 클래스 */
public class BrokerStringTable implements StringTable {
	private static final long serialVersionUID = -1072075853155908566L;
	protected String      name;
	protected StringTable originalInstance;

	public BrokerStringTable() {}
	public BrokerStringTable(StringTable stringTable) { this(); setOriginalInstance(stringTable); }
	public BrokerStringTable(String name, StringTable stringTable) { this(); setName(name); setOriginalInstance(stringTable); }
	
	@Override
	public String t(String originals) {
		if(originalInstance == null) return originals;
		return originalInstance.t(originals);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Properties getData() {
		if(originalInstance == null) return new Properties();
		return originalInstance.getData();
	}

	public StringTable getOriginalInstance() {
		return originalInstance;
	}

	public void setOriginalInstance(StringTable originalInstance) {
		this.originalInstance = originalInstance;
	}

	public void setName(String name) {
		this.name = name;
	}
}
