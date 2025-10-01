package org.duckdns.hjow.commons.vdp.data;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.differential.*;
import org.duckdns.hjow.commons.vdp.exceptions.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
import org.duckdns.hjow.commons.vdp.work.*;
import org.duckdns.hjow.commons.vdp.elements.*;

public class DoubleData extends NumberData 
{
	private static final long serialVersionUID = -3147040026320675891L;
	public double data = 0.0;
	
	public DoubleData(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public byte getDataType() 
	{
		return Data.DOUBLE;
	}
	@Override
	public String toBasicString() 
	{
		return String.valueOf(data);
	}
	@Override
	public Section differential() 
	{
		ConstantSection newone = new ConstantSection(tracker);
		newone.coefficient = new DoubleData(tracker);
		newone.coefficient.data = 0.0;
		DoubleData inside_newOne = new DoubleData(tracker);
		inside_newOne.data = 0.0;
		newone.inside = inside_newOne;
		newone.exponent = new DoubleData(tracker);
		newone.exponent.data = 1.0;
		return newone;
	}
	@Override
	public Data clone()
	{
		DoubleData newone = new DoubleData(tracker);
		newone.data = this.data;
		return newone;
	}
	@Override
	public String getName()
	{
		return String.valueOf(data);
	}
	@Override
	public void plus(NumberData data)
	{
		this.data = this.data + ((DoubleData)data).data;
	}
	@Override
	public void minus(NumberData data)
	{
		this.data = this.data - ((DoubleData)data).data;
	}
	@Override
	public void multiply(NumberData data)
	{
		this.data = this.data * ((DoubleData)data).data;
	}
	@Override
	public void divide(NumberData data)
	{
		this.data = this.data / ((DoubleData)data).data;
	}
	@Override
	public void abs()
	{
		this.data = Math.abs(this.data);		
	}
	@Override
	public byte compareTo(NumberData other)
	{
		if(this.data > ((DoubleData)other).data)
		{
			return 1;
		}
		else if(this.data < ((DoubleData)other).data)
		{
			return -1;
		}
		else return 0;
	}
	@Override
	public double getDouble()
	{
		return data;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_DOUBLE_DATA;
	}
}
