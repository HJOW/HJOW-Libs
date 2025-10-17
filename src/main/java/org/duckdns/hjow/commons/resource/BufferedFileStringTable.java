package org.duckdns.hjow.commons.resource;

import java.io.File;
import java.util.Properties;

/** 버퍼를 사용하는 파일 스트링 테이블 */
public class BufferedFileStringTable extends FileStringTable {
	private static final long serialVersionUID = 2191486883693221767L;
	protected transient StringTableBuffer buffer = new StringTableBuffer();
	public BufferedFileStringTable() {super();}
	public BufferedFileStringTable(File file) { super(file); }
	public BufferedFileStringTable(File file, int maxBufferSize) { super(file); setMaximumLength(maxBufferSize); }
	
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
