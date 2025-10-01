package org.duckdns.hjow.commons.vdp.tracker;

import java.io.Serializable;

public class TrackablePoint implements Serializable
{
	private static final long serialVersionUID = -2666473370440150270L;
	public long index;
	public Trackable object;
	public boolean normal = true;
	public String message = "";
	public TrackablePoint(long index)
	{
		this.index = index;
	}
	public TrackablePoint(long index, Trackable object)
	{
		this.index = index;
		this.object = object;
	}
	public TrackablePoint(long index, Trackable object, boolean normal)
	{
		this.index = index;
		this.object = object;
		this.normal = normal;
	}
	public TrackablePoint(long index, Trackable object, boolean normal, String message)
	{
		this.index = index;
		this.object = object;
		this.normal = normal;
		this.message = message;
	}
}
