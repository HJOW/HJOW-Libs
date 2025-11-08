package org.duckdns.hjow.commons.ui;

import java.io.File;
import java.io.Serializable;

/** JComboBox 또는 JList 의 원소로 쓸 수 있는 파일 원소 클래스 */
public class FileElement implements Serializable {
	private static final long serialVersionUID = 7492133287060457675L;
	protected File file;
    protected String label;
    
    public FileElement() {}
    public FileElement(File f) { file = f; }
    public FileElement(File f, String label) { file = f; this.label = label; }
    
    public String getLabel() {
		return label;
	}
    /** 파일명 대신 대체 텍스트 출력을 원할 경우 이 메소드로 내용 입력, null 입력 시 다시 파일명을 사용함. */
	public void setLabel(String label) {
		this.label = label;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public void setFile(File file, String label) {
		setFile(file);
		this.label = label;
	}
    
    @Override
    public String toString() {
    	if(getLabel() != null) return getLabel();
    	if(file == null) return "";
    	return file.toString();
    }
}
