package org.duckdns.hjow.commons.data;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.duckdns.hjow.commons.util.FileUtil;

/** 압축된 문서를 다루는 클래스 */
public class CompressedDocument implements Serializable {
	private static final long serialVersionUID = -5631776628196578986L;
	protected String content = "";
	protected String contentType = "text/plain";
	/** 빈 객체 생성, Content-Type 는 일반 텍스트 (text/plain) 으로 초기화. */
    public CompressedDocument() {}
    /** 직렬화된 하나의 문자열을 입력하여 객체 생성 */
    public CompressedDocument(String serializedContent) {
    	this();
    	loadSerializedContent(serializedContent);
    }
    /** 파일로부터 불러와 객체 생성 */
    public CompressedDocument(File file, String charset) throws IOException {
    	this();
    	String reads = FileUtil.readString(file, charset, GZIPInputStream.class);
    	loadSerializedContent(reads);
    }
    /** 파일로부터 불러와 객체 생성 */
    public CompressedDocument(File file) throws IOException {
    	this(file, "UTF-8");
    }
    /** 필드 각 내용을 직접 입력해 객체 생성 */
	public CompressedDocument(String content, String contentType) {
		this();
		this.content = content;
		this.contentType = contentType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/** 직렬화된 하나의 문자열에서 각 내용 추출해 이 객체에 데이터 입력 */
	public void loadSerializedContent(String serializedContent) {
		StringBuilder res = new StringBuilder("");
    	StringTokenizer lineTokenizer = new StringTokenizer(serializedContent, "\n");
    	boolean firsts  = true;
    	boolean seconds = false;
    	while(lineTokenizer.hasMoreTokens()) {
    		if(firsts)       { setContentType(lineTokenizer.nextToken().trim()); firsts  = false; seconds = true; }
    		else if(seconds) { res = res.append(lineTokenizer.nextToken());      seconds = false;                 }
    		else             { res = res.append("\n").append(lineTokenizer.nextToken());                          }
    	}
    	setContent(res.toString());
	}
	
	/** Content-Type 과 컨텐츠 내용을 하나의 문자열로 직렬화하여 반환. 첫 줄에는 Content-Type, 그다음 줄부터 컨텐츠 내용이 탑재됨. */
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder(getContentType().trim());
		StringTokenizer lineTokenizer = new StringTokenizer(getContent(), "\n");
		while(lineTokenizer.hasMoreTokens()) {
			res = res.append("\n").append(lineTokenizer.nextToken());
		}
		return res.toString();
	}
	
	/** 파일로 쓰기 */
	public void write(File file) throws IOException {
		write(file, "UTF-8");
	}
	
	/** 파일로 쓰기 */
	public void write(File file, String charset) throws IOException {
		FileUtil.writeString(file, charset, toString(), GZIPOutputStream.class);
	}
	
	/** 파일 확장자 */
	public static final String FILE_EXT = "cdoc";
	
	/** 파일 형식 설명자 */
	public static final String FILE_DESC = "압축된 문서";
}
