package org.duckdns.hjow.commons.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.duckdns.hjow.commons.data.CompressedDocument;
import org.duckdns.hjow.commons.util.FileUtil;

/** cdoc 내용 출력 컴포넌트 */
public class CDOCViewPanel extends JPanel {
	private static final long serialVersionUID = 7989653074897326923L;
	protected JPanel pnAtch;
	protected JScrollPane scroll;
	
    protected JTextPane pane;
    protected JButton btnDownload;
    protected JLabel lbFile;
    
    protected JFileChooser chooser;
    
    protected transient CompressedDocument doc;
    
    /** 컴포넌트 객체 생성 */
	public CDOCViewPanel() {
    	setLayout(new BorderLayout());
    	
    	JPanel pnMain = new JPanel();
    	pnMain.setLayout(new BorderLayout());
    	add(pnMain, BorderLayout.CENTER);
    	
    	pane = new JTextPane();
    	pane.setEditable(false);
    	scroll = new JScrollPane(pane);
    	
    	pnMain.add(scroll, BorderLayout.CENTER);
    	
    	pnAtch = new JPanel();
    	pnAtch.setLayout(new FlowLayout(FlowLayout.CENTER));
    	pnMain.add(pnAtch, BorderLayout.SOUTH);
    	
    	btnDownload = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));
    	pnAtch.add(btnDownload);
    	btnDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onDownloadRequested();
			}
		});
    	
    	lbFile = new JLabel();
    	pnAtch.add(lbFile);
    	
    	chooser = new JFileChooser();
    	chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	chooser.setMultiSelectionEnabled(false);
    	chooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Zip file (.zip)";
			}
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().trim().endsWith(".zip");
			}
		});
    	
    	pnAtch.setVisible(false);
    }
	
	protected void onDownloadRequested() {
		int sel = chooser.showSaveDialog(null);
		if(sel != JFileChooser.APPROVE_OPTION) return;
		
		File file = chooser.getSelectedFile();
		try {
			FileUtil.writeBytes(file, doc.getBinaries());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
		}
	}
	
	/** cdoc 문서 출력 */
	public void setDocument(CompressedDocument doc) {
		if(doc == null) {
			this.doc = null;
			pane.setText("");
			return;
		}
		
		this.doc = doc.cloneDocument();
		pane.setText(doc.getContent());
		pane.setContentType(doc.getContentType());
		
		if(doc.getBinaries() != null) {
			byte[] binaries = doc.getBinaries();
			if(binaries.length >= 1) {
				lbFile.setText(binaries.length + " bytes");
				pnAtch.setVisible(true);
			}
		}
	}
	
	/** cdoc 문서 반환 */
	public CompressedDocument getDocument() {
		return doc;
	}
	
	/** 스크롤 컴포넌트 객체 반환 */
	public JScrollPane getScrollPane() {
		return scroll;
	}
}