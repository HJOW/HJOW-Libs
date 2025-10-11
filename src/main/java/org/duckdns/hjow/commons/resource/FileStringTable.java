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
	
	public FileStringTable() {}
	public FileStringTable(File file) { this(); this.file = file; }
	
	/** 불러오기 */
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
	
	/** StringTable 데이터 입력 */
	public synchronized void set(String originalString, String translatedString) {
		Properties prop = load();
		setData(prop);
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
	/** StringTable 데이터를 합병, 매개변수로 들어온 데이터가 더 낮은 우선 순위로 적용 */
	public synchronized void setDataAsBasic(Properties basicData) {
		Properties prop = new Properties();
		prop.putAll(basicData);
		prop.putAll(load());
		setData(prop);
	}
	/** StringTable 데이터 전체를 교체 (기존 데이터 삭제 후 적용) */
	public void setData(Properties data) {
		setData(data, true);
	}
	/** StringTable 데이터를 한꺼번에 변경해 저장, 기존 데이터 삭제 후 적용하거나, 또는 기존 데이터를 두고 변경된 데이터만 덮어 씌울 수도 있음 */
	public synchronized void setData(Properties data, boolean replaces) {
		String lower = file.getName().toLowerCase();
		OutputStream outs = null;
		try {
			Properties prop = data;
			if(! replaces) {
				prop = load();
				prop.putAll(data);
			}
			
			outs = new FileOutputStream(file);
			if(lower.endsWith(".xml")) prop.storeToXML(outs, "");
			else                       prop.store(outs, "");
		} catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			ClassUtil.closeAll(outs);
		}
	}
}
