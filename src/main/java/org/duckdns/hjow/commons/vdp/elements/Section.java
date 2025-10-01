package org.duckdns.hjow.commons.vdp.elements;
import java.io.Serializable;
import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.differential.*;
import org.duckdns.hjow.commons.vdp.exceptions.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
import org.duckdns.hjow.commons.vdp.work.*;
import org.duckdns.hjow.commons.vdp.elements.*;
public abstract class Section extends Elements implements Serializable
{
	private static final long serialVersionUID = -790189898285687041L;
	
	public int depart = 0; 
	
	public char previousOperation; // + - * /
	public DoubleData coefficient;
	public Data inside;
	public DoubleData exponent;	
	
	
	public Section(Tracker tracker)
	{
		super(tracker);
	}
	
	public abstract String toBasicString();
	public abstract Section differential();
	public abstract boolean isDifferentiable();
	public abstract DoubleData calculate(DoubleData value);
	public boolean hasLower()
	{
		return false;
	}
	public Section trim()
	{
		Section newOne = (Section) this.clone();
		if(inside != null && inside.getDataType() == Data.SECTION)
		{
			((SectionData) newOne.inside).contents = ((SectionData) newOne.inside).contents.trim();
		}
		return newOne;
	}
	public void setPreviousOperation(char op)
	{
		previousOperation = op;
	}
	
	public abstract int getSectionType();
	
	public static final int NULL = 0;	
	public static final int CONSTANT = 1;
	public static final int MULTIPLE = 2;
	public static final int ZERO = 9;
	public static final int BASIC = 10;
	public static final int SINE = 20;
	public static final int COSINE = 21;
	public static final int TANGENT = 22;
	public static final int LN = 40;
	public static final int LOG = 41;
	public static final int EXP = 50;
	public static final int AXP = 51;
	public static final int OPERATION = 90;
	
	public static Section initialize()
	{
		return initialize(Section.CONSTANT);		
	}
	public static Section initialize(int type)
	{
		Section sect;
		DoubleData temp;
		switch(type)
		{
		case Section.NULL:
			sect = new NullSection(tracker);
			break;
		case Section.CONSTANT:
			sect = new ConstantSection(tracker);
			break;
		case Section.ZERO:
			sect = new ConstantSection(tracker);
			break;
		case Section.BASIC:
			sect = new BasicSection(tracker);
			break;
		case Section.SINE:
			sect = new SineSection(tracker);
			break;
		case Section.COSINE:
			sect = new CosineSection(tracker);
			break;
		case Section.TANGENT:
			sect = new TangentSection(tracker);
			break;
		case Section.LN:
			sect = new LnSection(tracker);
			break;
		case Section.LOG:
			sect = new LogSection(tracker);
			break;
		case Section.MULTIPLE:
			sect = new MultipleSection(tracker);
			break;
		case Section.OPERATION:
			sect = new OperationSection(tracker);
			break;
		case Section.EXP:
			sect = new ExpSection(tracker);
			break;
		default:
			sect = new ConstantSection(tracker);
		}		
		sect.coefficient = new DoubleData(tracker);
		sect.coefficient.data = 1.0;
		sect.exponent = new DoubleData(tracker);
		sect.exponent.data = 1.0;
		sect.previousOperation = '+';
		if(type == Section.ZERO)
		{
			sect.coefficient.data = 0.0;
			sect.exponent.data = 1.0;
		}
		else if(type == Section.MULTIPLE)
		{
			((MultipleSection) sect).contents = new Section[1];
		}
		temp = new DoubleData(tracker);		
		temp.data = 1.0;
		if(type == Section.ZERO) temp.data = 0.0;
		sect.inside = temp;	
		//System.out.println("초기화된 타입 : " + sect.getSectionType() + ", " + sect.toBasicString());
		return sect;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_SECTION;
	}
}
