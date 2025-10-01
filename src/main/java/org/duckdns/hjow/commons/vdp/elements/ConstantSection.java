package org.duckdns.hjow.commons.vdp.elements;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
public class ConstantSection extends Section
{
	private static final long serialVersionUID = -391955571401225066L;
	
	public ConstantSection(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public String toBasicString() 
	{
		return this.previousOperation + this.inside.toBasicString();
	}

	@Override
	public Section differential() 
	{
		ConstantSection newone = (ConstantSection) Section.initialize(Section.ZERO);
		/*
		ConstantSection newone = new ConstantSection();
		DoubleData newInside = new DoubleData();
		newInside.data = 0.0;
		newone.inside = newInside;*/
		return newone.trim();
	}

	@Override
	public int getSectionType() 
	{
		return Section.CONSTANT;
	}

	@Override
	public boolean isDifferentiable() 
	{
		return true;
	}

	@Override
	public Elements clone() 
	{
		ConstantSection newone = (ConstantSection) Section.initialize(Section.CONSTANT);
		newone.coefficient.data = this.coefficient.data;
		newone.exponent.data = this.exponent.data;
		newone.inside = (Data) this.inside.clone();
		newone.previousOperation = this.previousOperation;
		return newone;
	}

	@Override
	public String getName()
	{
		return "";
	}

	@Override
	public DoubleData calculate(DoubleData value)
	{
		DoubleData newOne = (DoubleData) inside.clone();
		return newOne;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_CONSTANT_SECTION;
	}
}
