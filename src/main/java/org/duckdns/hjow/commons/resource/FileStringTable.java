package org.duckdns.hjow.commons.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.duckdns.hjow.commons.util.ClassUtil;

/** 언어 지원을 위한 StringTable, 메모리 절약을 위해 필요할 때마다 매번 불러오는 방식 사용 */
public class FileStringTable implements StringTable {
	private static final long serialVersionUID = -2808486991696121069L;
	protected String     name = "Unnamed";
	protected File       file = null;
	protected Properties data = new Properties();
	public FileStringTable() {}
	public FileStringTable(File file) { this(); this.file = file; }
	
	protected Properties load() {
		InputStream resources = null;
		Properties prop = new Properties();
		try {
			resources = new FileInputStream(file);
			
			String lower = file.getName().toLowerCase();
			if(lower.endsWith(".xml")) prop.loadFromXML(resources);
			else                       prop.load(resources);
		} catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			ClassUtil.closeAll(resources);
		}
		return prop;
	}
	
	/** StringTable 변경사항 파일에 저장 */
	public void save() {
		String lower = file.getName().toLowerCase();
		OutputStream outs = null;
		try {
			outs = new FileOutputStream(file);
			if(lower.endsWith(".xml")) load().storeToXML(outs, "");
			else                       load().store(outs, "");
		} catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			ClassUtil.closeAll(outs);
		}
	}
	
	/** StringTable 데이터 입력 */
	public void set(String originalString, String translatedString, boolean saveNow) {
		data.setProperty(originalString, translatedString);
		if(saveNow) save();
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
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setData(Properties data) {
		this.data = data;
	}
}
