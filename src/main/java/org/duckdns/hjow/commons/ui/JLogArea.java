package org.duckdns.hjow.commons.ui;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.duckdns.hjow.commons.core.Disposeable;
import org.duckdns.hjow.commons.core.Releasable;
import org.duckdns.hjow.commons.util.ClassUtil;

public class JLogArea extends JScrollPane implements Disposeable, Releasable, LogComponent {
    private static final long serialVersionUID = -8363517413706955543L;

    protected JTextArea ta = new JTextArea();
    protected List<String> list = new LinkedList<String>();
    protected StringBuilder buffer = new StringBuilder();
    
    protected transient BufferedWriter outputStream = null;
    protected int counts = 0;
    
    protected boolean firsts = false;
    
    public JLogArea() {
        super();
        setViewportView(ta);
        ta.setEditable(false);
    }
    
    /** 새로고침 */
    public synchronized void refreshFull() {
        buffer.setLength(0);
        
        firsts = true;
        for(String str : list) {
            if(firsts) { buffer = buffer.append("\n"); firsts = false; }
            buffer = buffer.append(str);
        }
        ta.setText(buffer.toString());
        buffer.setLength(0);
        
        ta.setCaretPosition(ta.getDocument().getLength() - 1);
    }
    
    /** 로그 텍스트 지정 (리스트도 모두 초기화 후 세팅됨) */
    public void setText(String msg) {
        list.clear();
        list.add(msg);
        counts = 1;
        
        if(outputStream != null) {
        	try {
        	    StringTokenizer lineTokenizer = new StringTokenizer(msg, "\n");
        	    while(lineTokenizer.hasMoreTokens()) {
        	        outputStream.write(lineTokenizer.nextToken());
        	        outputStream.newLine();
        	    }
        	} catch(IOException ex) {
        		ClassUtil.closeAll(outputStream);
        		outputStream = null;
        		ex.printStackTrace();
        		log("Error : " + ex.getMessage());
        	}
        }
        
        refreshFull();
    }
    
    /** 현재 로그 텍스트 반환 */
    public String getText() {
        return ta.getText();
    }
    
    /** 로그 초기화 (리스트 및 버퍼도 모두 삭제) */
    public void clear() {
        buffer.setLength(0);
        list.clear();
        counts = 0;
        ta.setText("");
        setCaretLastPosition();
    }
    
    /** 스크롤을 마지막 위치로 이동 */
    public void setCaretLastPosition() {
        int loc = ta.getDocument().getLength() - 1;
        if(loc < 0) loc = 0;
        ta.setCaretPosition(loc);
    }
    
    /** 줄 자동띄움 사용여부 지정 */
    public void setLineWrap(boolean wrap) {
        ta.setLineWrap(wrap);
    }
    
    /** 로그 출력 */
    @Override
    public synchronized void log(String msg) {
        list.add(msg);
        counts++;
        
        if(counts == 0) ta.append(msg);
        else ta.append("\n" + msg);
        
        setCaretLastPosition();
        
        if(outputStream != null) {
        	try {
        	    StringTokenizer lineTokenizer = new StringTokenizer(msg, "\n");
        	    while(lineTokenizer.hasMoreTokens()) {
        	        outputStream.write(lineTokenizer.nextToken());
        	        outputStream.newLine();
        	    }
        	} catch(IOException ex) {
        		ClassUtil.closeAll(outputStream);
        		outputStream = null;
        		ex.printStackTrace();
        		log("Error : " + ex.getMessage());
        	}
        }
    }
    
    /** 로그 메시지를 문자열 리스트로 반환 (setText 또는 clear 시 삭제되므로 유의 !) */
    public List<String> getLogList() {
        List<String> list = new ArrayList<String>();
        list.addAll(this.list);
        return list;
    }

    @Override
    public void dispose() {
        clear();
        closeWriter();
    }

	@Override
	public void releaseResource() {
		dispose();
	}
	
	/** 추가 출력 스트림 설정 (로그 출력 시 이 스트림으로도 출력됨) */
	public void setWriter(Writer writer) {
		if(outputStream != null) {
        	ClassUtil.closeAll(outputStream);
    		outputStream = null;
        }
		
		if(writer instanceof BufferedWriter) outputStream = (BufferedWriter) writer;
		else                                 outputStream = new BufferedWriter(writer);
	}
	
	/** 추가 출력 스트림 닫기 */
	public void closeWriter() {
		if(outputStream != null) {
        	ClassUtil.closeAll(outputStream);
    		outputStream = null;
        }
	}
}
