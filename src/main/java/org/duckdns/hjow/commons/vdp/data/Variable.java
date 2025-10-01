package org.duckdns.hjow.commons.vdp.data;

import org.duckdns.hjow.commons.vdp.tracker.*;
import org.duckdns.hjow.commons.vdp.elements.*;
public class Variable extends Data 
{
	private static final long serialVersionUID = 4411069137703588997L;
	public String name;
	
	public Variable(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public byte getDataType() 
	{
		return 100;
	}

	@Override
	public String toBasicString() 
	{
		return new String(name);
	}

	@Override
	public Section differential()
	{
		ConstantSection newone = (ConstantSection) Section.initialize(Section.CONSTANT);		
		return newone;
	}

	@Override
	public Elements clone()
	{
		Variable newone = new Variable(tracker);
		newone.name = this.name;
		return newone;
	}

	@Override
	public String getName()
	{		
		return name;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_VARIABLE;
	}
}
