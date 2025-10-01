package org.duckdns.hjow.commons.vdp.elements;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
public class SineSection extends Section
{
	private static final long serialVersionUID = 4967732618704423765L;

	public SineSection(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public String toBasicString() 
	{
		String rets = this.previousOperation + coefficient.toBasicString() + this.getName() +"(" + inside.toBasicString() + ")";
		if(exponent.data != 1)
		{
			rets = rets + "^" + exponent.toBasicString();
		}
		return rets;				
	}

	@Override
	public Section differential() 
	{
		Section newone;
		MultipleSection multis = (MultipleSection) Section.initialize(Section.MULTIPLE);
		
		if(this.inside.getDataType() == Data.DOUBLE || this.inside.getDataType() == Data.DECIMAL)
		{
			newone = (ConstantSection) Section.initialize(Section.CONSTANT);
			DoubleData zeros = new DoubleData(tracker);
			zeros.data = 0.0;
			newone.inside = zeros;
			return newone;
		}
		else
		{
			boolean secondExist = false;
			
			if(this.exponent.data == 1.0)
			{
				CosineSection firstDerivate = (CosineSection) Section.initialize(Section.COSINE);
				
				firstDerivate.coefficient.data = this.coefficient.data;
				firstDerivate.exponent.data = this.exponent.data;
				firstDerivate.previousOperation = this.previousOperation;
				
				Section secondDerivate = Section.initialize();
				
				SectionData insideData = (SectionData) this.inside.clone();
				Section insides = (Section) insideData.contents.clone();				
				
				if(insides.getSectionType() == Section.BASIC && insides.exponent.data == 1.0)
				{
					firstDerivate.coefficient.data = firstDerivate.coefficient.data * insides.coefficient.data;
					firstDerivate.inside = (Data) this.inside.clone();					
				}
				else
				{					
					secondDerivate = insides.differential();
					if(secondDerivate.previousOperation == '-')
					{
						secondDerivate.coefficient.data *= -1.0;
					}
					secondDerivate.previousOperation = '*';
					firstDerivate.inside = (Data) this.inside.clone();	
					secondExist = true;
					//System.out.println("    여러 항 : " + secondDerivate.toBasicString());
				}
				
				//System.out.println("    미분 결과 : " + firstDerivate.toBasicString());
				MultipleSection multip = (MultipleSection) Section.initialize(Section.MULTIPLE);
				if(secondExist) multip.contents = new Section[2];
				else return firstDerivate;
				multip.contents[0] = firstDerivate.trim();
				multip.contents[1] = secondDerivate.trim();
				
				return multip;
			}
			else
			{
				SineSection firstDerivate = (SineSection) Section.initialize(Section.SINE);
				
				firstDerivate.coefficient.data = this.coefficient.data * this.exponent.data;
				firstDerivate.exponent.data = this.exponent.data - 1.0;
				firstDerivate.previousOperation = this.previousOperation;
				
				CosineSection secondDerivate = (CosineSection) Section.initialize(Section.COSINE);
				Section thirdDerivate = Section.initialize();
				
				SectionData insideData = (SectionData) this.inside.clone();
				Section insides = insideData.contents;
								
				secondDerivate.inside = (Data) this.inside.clone();
				
				if(insides.getSectionType() == Section.BASIC && insides.exponent.data == 1.0)
				{
					firstDerivate.coefficient.data = firstDerivate.coefficient.data * insides.coefficient.data;
				}
				else
				{
					insides = (Section) insides.clone();
					thirdDerivate = insides.differential();
					firstDerivate.coefficient.data = firstDerivate.coefficient.data * thirdDerivate.coefficient.data;
					thirdDerivate.coefficient.data = 1.0;
					if(thirdDerivate.previousOperation == '-') firstDerivate.coefficient.data *= -1.0;
					thirdDerivate.previousOperation = '*';
					secondExist = true;
				}
				
				if(secondDerivate.previousOperation == '-')
				{
					secondDerivate.coefficient.data *= (-1.0);
				}
				secondDerivate.previousOperation = '*';
				
				if(secondExist) multis.contents = new Section[3];
				else multis.contents = new Section[2];
				firstDerivate.inside = insideData.clone();
				multis.contents[0] = firstDerivate.trim();
				multis.contents[1] = secondDerivate.trim();
				if(secondExist) multis.contents[2] = thirdDerivate.trim();
				
				if(multis.contents.length == 1)
				{
					return multis.contents[0];
				}
				else newone = multis;
				return newone;
			}		
		}
	}
	
	@Override
	public int getSectionType() 
	{
		return 20;
	}

	@Override
	public boolean isDifferentiable() 
	{
		return true;
	}

	@Override
	public Elements clone() 
	{
		SineSection newone = (SineSection) Section.initialize(Section.SINE);
		newone.coefficient.data = this.coefficient.data;
		newone.exponent.data = this.exponent.data;
		newone.inside = (Data) this.inside.clone();
		newone.previousOperation = this.previousOperation;
		return newone;
	}

	@Override
	public String getName()
	{
		return "sin";
	}
	@Override
	public DoubleData calculate(DoubleData value)
	{
		DoubleData newOne = new DoubleData(tracker);
		if(inside.getDataType() == Data.DOUBLE) newOne.data = coefficient.data * Math.pow(Math.sin(((DoubleData)inside).data), exponent.data);
		else if(inside.getDataType() == Data.SECTION)
		{
			newOne.data = coefficient.data * Math.pow(Math.sin(((SectionData) inside).contents.calculate(value).data), exponent.data);
		}
		return newOne;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_SINE_SECTION;
	}

}
