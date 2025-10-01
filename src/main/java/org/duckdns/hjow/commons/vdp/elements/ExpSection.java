package org.duckdns.hjow.commons.vdp.elements;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
public class ExpSection extends Section
{
    private static final long serialVersionUID = -304606208625166904L;
	
	public ExpSection(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public String toBasicString() 
	{
		String rets = this.previousOperation + " " + coefficient.toBasicString() + " " + this.getName() +"(" + inside.toBasicString() + ")";
		if(exponent.data != 1.0)
		{
			rets = rets + "^" + String.valueOf(exponent);
		}
		return rets;
	}

	@Override
	public Section differential() 
	{
		MultipleSection newOne = (MultipleSection) Section.initialize(Section.MULTIPLE);
		ExpSection newEx = (ExpSection) this.clone();
		newOne.previousOperation = this.previousOperation;
		newEx.previousOperation = '+';
		if(this.exponent.data == 1.0)
		{
			newOne.contents = new Section[2];
			Section one, two;
			if(inside.getDataType() == Data.DOUBLE) one = Section.initialize(Section.CONSTANT);
			else if(inside.getDataType() == Data.SECTION) one = ((SectionData) inside).contents.differential();
			else one = Section.initialize(Section.CONSTANT);
			if(inside.getDataType() == Data.DOUBLE) return Section.initialize(Section.ZERO);
			else if(inside.getDataType() == Data.SECTION) two = (Section) this.clone();
			else return Section.initialize(Section.ZERO);
			two.previousOperation = '*';
			newOne.contents[0] = one;
			newOne.contents[1] = two;
		}
		else
		{
			newOne.contents = new Section[3];
			Section one, two, three;
			one = (Section) newEx.clone();
			one.coefficient.data = 1.0;
			one.previousOperation = '+';
			newOne.coefficient = (DoubleData) one.coefficient.clone();
			newOne.coefficient.data *= exponent.data;
			one.exponent.data = exponent.data - 1.0;
			newEx.exponent.data = 1.0;
			two = newEx.differential();
			if(inside.getDataType() == Data.DOUBLE) return Section.initialize(Section.ZERO);
			else if(inside.getDataType() == Data.SECTION) three = ((SectionData) inside).contents.differential();
			else return Section.initialize(Section.ZERO);
			two.previousOperation = '*';
			three.previousOperation = '*';
			
			newOne.contents[0] = one;
			newOne.contents[1] = two;
			newOne.contents[2] = three;
		}
		return newOne;
	}

	@Override
	public boolean isDifferentiable() 
	{
		return true;
	}

	@Override
	public int getSectionType() 
	{
		return Section.EXP;
	}

	@Override
	public Elements clone()
	{
		ExpSection newOne = (ExpSection) Section.initialize(Section.EXP);
		newOne.coefficient = (DoubleData) this.coefficient.clone();
		newOne.inside = (Data) this.inside.clone();
		newOne.previousOperation = this.previousOperation;
		newOne.exponent = (DoubleData) this.exponent.clone();
		return newOne;
	}

	@Override
	public String getName()
	{
		return "exp";
	}

	@Override
	public DoubleData calculate(DoubleData value)
	{
		DoubleData newOne = new DoubleData(tracker);
		if(inside.getDataType() == Data.DOUBLE) newOne.data = coefficient.data * Math.pow(Math.exp(((DoubleData)inside).data), exponent.data);
		else if(inside.getDataType() == Data.SECTION)
		{
			newOne.data = coefficient.data * Math.pow(Math.exp(((SectionData) inside).contents.calculate(value).data), exponent.data);
		}
		return newOne;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_EXP_SECTION;
	}
}
