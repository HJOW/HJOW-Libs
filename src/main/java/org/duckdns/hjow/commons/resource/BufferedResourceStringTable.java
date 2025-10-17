package org.duckdns.hjow.commons.resource;

import java.util.Properties;

/** 버퍼를 사용하는 리소스 스트링 테이블 */
public class BufferedResourceStringTable extends ResourceStringTable {
	private static final long serialVersionUID = 2191486883693221767L;
	protected transient StringTableBuffer buffer = new StringTableBuffer();
	public BufferedResourceStringTable() {super();}
	public BufferedResourceStringTable(String resourcePath) { super(resourcePath); }
	public BufferedResourceStringTable(String resourcePath, int maxBufferSize) { super(resourcePath); setMaximumLength(maxBufferSize); }
	
	@Override
	public String t(String originals) {
		String res = buffer.work(originals);
		if(res != null) return res;
		
		Properties prop = load();
		if(prop.containsKey(originals)) res = prop.getProperty(originals);
		else res = originals;
		
		buffer.register(originals, res);
		
		return res;
	}
	
	/** 버퍼 크기 지정 */
	public void setMaximumLength(int maximumLength) {
		buffer.maximumLength = maximumLength;
	}
}
