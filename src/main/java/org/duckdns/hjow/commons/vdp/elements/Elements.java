package org.duckdns.hjow.commons.vdp.elements;

import java.io.Serializable;
import org.duckdns.hjow.commons.vdp.tracker.*;

public abstract class Elements implements Trackable, Serializable
{
	private static final long serialVersionUID = 533776442584273961L;
	public int version_main = 0;
	public int version_sub = 1;
	public static transient Tracker tracker;
	public Elements(Tracker tracker)
	{
		if(tracker != null) Elements.tracker = tracker;
	}
	public abstract String toBasicString();
	public abstract String getName();
	public abstract Elements clone();
	
	@Override
	public String toString()
	{
		return this.toBasicString();
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
		return Tracker.TRACKABLE_ELEMENTS;
	}
}
