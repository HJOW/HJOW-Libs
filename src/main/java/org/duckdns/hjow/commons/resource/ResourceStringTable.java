package org.duckdns.hjow.commons.resource;

import java.io.InputStream;
import java.util.Properties;

import org.duckdns.hjow.commons.util.ClassUtil;

/** 언어 지원을 위한 String Table, 메모리 절약을 위해 필요할 때마다 매번 불러오는 방식 사용 */
public class ResourceStringTable implements StringTable {
	private static final long serialVersionUID = 2578702307643853144L;
	protected String     name = "Unnamed";
	protected String     resourcePath = null;
	public ResourceStringTable() {}
	public ResourceStringTable(String resourcePath) { this(); this.resourcePath = resourcePath; }
	
	protected Properties load() {
		InputStream resources = null;
		Properties prop = new Properties();
		try {
			resources = ResourceStringTable.class.getResourceAsStream(resourcePath);
			if(resources == null) return prop;
			
			String lower = resourcePath.toLowerCase();
			if(lower.endsWith(".xml")) prop.loadFromXML(resources);
			else                       prop.load(resources);
		} catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			ClassUtil.closeAll(resources);
		}
		return prop;
	}
	
	@Override
	public String t(String originals) {
		Properties prop = load();
		if(prop.contains(originals)) return prop.getProperty(originals);
		return originals;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public Properties getData() {
		return load();
	}
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public void setName(String name) {
		this.name = name;
	}
}
