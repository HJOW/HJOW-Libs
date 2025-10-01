package org.duckdns.hjow.commons.vdp.data;

import org.duckdns.hjow.commons.vdp.tracker.*;
import org.duckdns.hjow.commons.vdp.elements.*;
public abstract class NumberData extends Data
{
	private static final long serialVersionUID = -618481590335439082L;

	
	public abstract byte getDataType();
	public abstract String toBasicString();
	public abstract Section differential();
	
	public abstract void plus(NumberData data);
	public abstract void minus(NumberData data);
	public abstract void multiply(NumberData data);
	public abstract void divide(NumberData data);
	public abstract void abs();
	public abstract byte compareTo(NumberData other);
	public abstract double getDouble();
	
	public NumberData(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_NUMBER_DATA;
	}
}
