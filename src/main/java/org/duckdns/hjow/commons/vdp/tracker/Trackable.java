package org.duckdns.hjow.commons.vdp.tracker;

public interface Trackable
{
	public void addThis();
	public void addThis(boolean normal);
	public void addThis(boolean normal, String message);
	public int getTrackableClassType();
}
