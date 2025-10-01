package org.duckdns.hjow.commons.vdp.ui;

import org.duckdns.hjow.commons.vdp.language.Language;
import org.duckdns.hjow.commons.vdp.tracker.Tracker;

public class ConsoleOutput extends OutputManager
{

	public static final int CONSOLE_INPUT_SECTION = 0;
	public static final int CONSOLE_DIFFERENTIAL = 1;
	public static final int CONSOLE_INPUT_VALUE = 2;
	public static final int CONSOLE_EXIT = 3;
		
	public ConsoleOutput(Kernal kernal, Tracker tracker, Language lang)
	{
		super(kernal, tracker, lang);
	}


	@Override
	public void open()
	{
			
	}

	@Override
	public byte getType()
	{
		return OutputManager.OUTPUT_CONSOLE;
	}


	@Override
	public void print(String str)
	{
		System.out.print(str);
		record.append(str);
	}


	@Override
	public void setProgrssState(int state, int max)
	{
		
	}


	@Override
	public void inputString(String str)
	{
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addThis()
	{
		tracker.addPoint(this);
		
	}


	@Override
	public void addThis(boolean normal)
	{
		tracker.addPoint(this, normal);
		
	}
	@Override
	public void addThis(boolean normal, String message)
	{
		tracker.addPoint(this, normal, message);
		
	}

	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_CONSOLE_OUTPUT;
	}


	@Override
	public boolean isGUI()
	{
		return false;
	}

}
