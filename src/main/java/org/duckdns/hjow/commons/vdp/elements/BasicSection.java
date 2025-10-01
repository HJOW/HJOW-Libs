package org.duckdns.hjow.commons.vdp.elements;
import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
public class BasicSection extends Section
{	
	private static final long serialVersionUID = -790189898285687041L;
	
	public int type = 0;
	
	public BasicSection(Tracker tracker)
	{
		super(tracker);
	}
	public String toBasicString()
	{
		//System.out.println("~~~");
		String rets =  this.previousOperation + coefficient.toBasicString() + this.getName();
		if(exponent.data != 1.0)
		{
			rets = rets + "^" + exponent.toBasicString();
		}
		return rets;
	}
	public int getSectionType()
	{
		return 10;
	}
	@Override
	public Section differential() 
	{
		Section newone = (BasicSection) Section.initialize(Section.BASIC);
		newone.coefficient.data = this.coefficient.data * this.exponent.data;
		newone.exponent.data = this.exponent.data - 1.0;
		newone.previousOperation = this.previousOperation;
		
		
		if(newone.coefficient.data == 0.0)
		{
			newone = (ConstantSection) Section.initialize(Section.ZERO);
		}
		if(newone.exponent.data == 0.0)
		{
			newone = (ConstantSection) Section.initialize(Section.CONSTANT);
			DoubleData insideValue = new DoubleData(tracker);
			insideValue.data = this.coefficient.data * this.exponent.data;
			newone.inside = insideValue;
			newone.previousOperation = this.previousOperation;
		}
		
		return newone.trim();
		/*
		BasicSection newone = new BasicSection();
		newone.coefficient.data = this.coefficient.data * this.exponent.data;
		newone.exponent.data = this.exponent.data - 1.0;
		if(newone.exponent.data == 0.0)
		{
			newone.type = 0;
			DoubleData newInside = new DoubleData();
			newInside.data = 1.0;
			newone.inside = newInside;
		}
		else
		{
			newone.type = this.type;
			newone.inside = this.coefficient;				
		}
		
		return newone;*/		
	}
	@Override
	public boolean isDifferentiable() 
	{
		return true;
	}
	@Override
	public Elements clone() 
	{
		BasicSection newone = (BasicSection) Section.initialize(Section.BASIC);
		newone.coefficient.data = this.coefficient.data;
		newone.exponent.data = this.exponent.data;
		newone.inside = (Data) this.inside.clone();
		newone.previousOperation = this.previousOperation;
		return newone;
	}
	@Override
	public String getName()
	{
		return "x";
	}
	@Override
	public DoubleData calculate(DoubleData value)
	{
		DoubleData newOne = new DoubleData(tracker);
		newOne.data = coefficient.data * Math.pow(value.data, exponent.data);		
		
		return newOne;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_BASIC_SECTION;
	}	
}
