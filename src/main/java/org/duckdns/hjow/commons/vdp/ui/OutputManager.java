package org.duckdns.hjow.commons.vdp.ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.duckdns.hjow.commons.vdp.language.Language;
import org.duckdns.hjow.commons.vdp.tracker.Tracker;

public abstract class OutputManager implements org.duckdns.hjow.commons.vdp.tracker.Trackable
{
	protected Kernal kernal;
	protected Language lang;
	public StringBuffer record;
	public static Tracker tracker;
	public OutputManager(Kernal kernal, Tracker tracker, Language lang)
	{
		this.kernal = kernal;
		this.lang = lang;
		if(tracker != null) OutputManager.tracker = tracker;
		record = new StringBuffer();
	}
	
	public static final byte OUTPUT_CONSOLE = 0;
	public static final byte OUTPUT_FRAME = 100;
	public static final byte OUTPUT_CLASSIC_FRAME = 101;
	
	public abstract void setProgrssState(int state, int max);
	public abstract void open();
	public abstract byte getType();
	public abstract void print(String str);
	public abstract void inputString(String str);
	public abstract boolean isGUI();
	public void inputFieldClear()
	{
		
	}
	public void saveRecord(String path) throws IOException
	{
		BufferedWriter bfwriter = new BufferedWriter(new FileWriter(path));
		bfwriter.write(record.toString());
		bfwriter.close();
	}
	public Language getLanguage()
	{
		return lang;
	}
}
