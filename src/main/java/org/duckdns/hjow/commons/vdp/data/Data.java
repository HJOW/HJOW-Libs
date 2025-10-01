package org.duckdns.hjow.commons.vdp.data;
import java.io.Serializable;
import org.duckdns.hjow.commons.vdp.tracker.*;
import org.duckdns.hjow.commons.vdp.elements.*;

public abstract class Data extends Elements implements Serializable
{	
	private static final long serialVersionUID = -3106367930545468931L;
	public static final byte DOUBLE = 0;
	public static final byte DECIMAL = 1;
	public static final byte SECTION = 10;
	public abstract byte getDataType();
	public abstract String toBasicString();
	public abstract Section differential();		
	public Data(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_DATA;
	}
}
