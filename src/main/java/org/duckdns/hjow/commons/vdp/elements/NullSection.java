package org.duckdns.hjow.commons.vdp.elements;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
public class NullSection extends Section
{
	private static final long serialVersionUID = 2816677496485814891L;

	public NullSection(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public String toBasicString() 
	{
		return "Null";
	}

	@Override
	public int getSectionType() 
	{
		return 0;
	}

	@Override
	public Section differential() 
	{
		return new NullSection(tracker);
	}
	@Override
	public boolean isDifferentiable() 
	{
		return false;
	}

	@Override
	public Elements clone() 
	{
		return Section.initialize(Section.NULL);
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public DoubleData calculate(DoubleData value)
	{
		// TODO 자동 생성된 메소드 스텁
		return null;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_NULL_SECTION;
	}
}
