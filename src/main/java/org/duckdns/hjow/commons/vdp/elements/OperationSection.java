package org.duckdns.hjow.commons.vdp.elements;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
public class OperationSection extends Section // 부호 처리를 위한 임시 클래스
{
	private static final long serialVersionUID = 5497452364902103987L;

	public OperationSection(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public String toBasicString()
	{		
		return String.valueOf(previousOperation);
	}

	@Override
	public Section differential()
	{
		return Section.initialize(Section.CONSTANT);
	}

	@Override
	public boolean isDifferentiable()
	{
		return false;
	}

	@Override
	public int getSectionType()
	{
		return Section.OPERATION;
	}

	@Override
	public String getName()
	{
		return String.valueOf(previousOperation);
	}

	@Override
	public Elements clone()
	{
		Section newOne = Section.initialize(Section.OPERATION);
		newOne.previousOperation = this.previousOperation;
		return newOne;
	}
	@Override
	public DoubleData calculate(DoubleData value)
	{
		
		return null;
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_OPERATION_SECTION;
	}
}
