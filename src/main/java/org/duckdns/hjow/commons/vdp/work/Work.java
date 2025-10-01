package org.duckdns.hjow.commons.vdp.work;

import java.io.Serializable;

public abstract class Work implements Serializable
{
	private static final long serialVersionUID = 4918560765994075914L;
	private boolean done = false;
	private int error = 0; // 0 : Normal
	
	public static final int WORK_INPUT = 100;
	public static final int WORK_DIFFERENTIAL = 101;
	public static final int WORK_UPDATE_GRAPH = 102;
	public static final int WORK_INSERT_VALUE = 103;
	
	public static final int WORK_RESULT_TO_GRAPH = 201;
	public static final int WORK_DIFFERENT_TO_GRAPH = 202;
	
	public static final int WORK_DIFFERENT_TO_RESULT = 301;
	
	public static final int ERROR_NONE = 0;
	
	public abstract int getType();
	public abstract String getDescription();
	public synchronized boolean isDone()
	{
		return done;
	}
	public synchronized void done()
	{
		done = true;
	}
	public synchronized int getErrorCode()
	{
		return error;
	}
	public synchronized void setErrorCode(int error)
	{
		this.error = error;
	}
}
