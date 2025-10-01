package org.duckdns.hjow.commons.vdp.elements;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.differential.*;
import org.duckdns.hjow.commons.vdp.exceptions.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
import org.duckdns.hjow.commons.vdp.work.*;
import org.duckdns.hjow.commons.vdp.elements.*;
public class LogSection extends LowerExistSection
{
	private static final long serialVersionUID = -2347628666292035873L;
	
	public LogSection(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public String toBasicString() 
	{
		String rets = this.previousOperation + " " + coefficient.toBasicString() + this.getName() + lower.toBasicString() + "(" + inside.toBasicString() + ")";
		if(exponent.data != 1.0)
		{
			rets = rets + "^" + String.valueOf(exponent);
		}
		return rets;
	}

	@Override
	public Section differential() 
	{
		return null;
	}

	@Override
	public boolean isDifferentiable() 
	{
		return false;
	}

	@Override
	public int getSectionType() 
	{
		return 31;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_LOG_SECTION;
	}
	@Override
	public DoubleData calculate(DoubleData value)
	{
		DoubleData newOne = new DoubleData(tracker);
		if(inside.getDataType() == Data.DOUBLE) newOne.data = coefficient.data * Math.pow(Math.log(((DoubleData)inside).data), exponent.data);
		else if(inside.getDataType() == Data.SECTION)
		{
			newOne.data = coefficient.data * Math.pow(Math.log(((SectionData) inside).contents.calculate(value).data), exponent.data);
		}
		return newOne;
	}
	@Override
	public String getName()
	{
		return "log_";
	}
	@Override
	public Elements clone()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
