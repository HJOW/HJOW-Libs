package org.duckdns.hjow.commons.vdp.differential;

import java.util.Vector;

import org.duckdns.hjow.commons.vdp.elements.MultipleSection;
import org.duckdns.hjow.commons.vdp.elements.Section;
import org.duckdns.hjow.commons.vdp.tracker.Trackable;
import org.duckdns.hjow.commons.vdp.tracker.Tracker;

public class DifferentialBlock implements Trackable // 곱셈 또는 나눗셈으로만 묶인 단위
{
	public Vector<Section> sections;
	public char previousOperation;
	private Tracker tracker;
	public DifferentialBlock(Tracker tracker)
	{
		sections = new Vector<Section>();
		if(tracker != null) this.tracker = tracker;
	}
	public DifferentialBlock differential()
	{
		Vector<Section> resultVector = new Vector<Section>();
		for(int i=0; i<sections.size(); i++)
		{
			MultipleSection mu = (MultipleSection) Section.initialize(Section.MULTIPLE);
			mu.contents = new Section[sections.size()];
			for(int j=0; j<sections.size(); j++)
			{
				if(j == i)
				{
					mu.contents[j] = sections.get(i).differential();
				}
				else
				{
					mu.contents[j] = (Section) sections.get(i).clone();
				}
			}
			mu.previousOperation = '+';
			resultVector.add(mu);
		}
		DifferentialBlock newOne = new DifferentialBlock(tracker);
		newOne.previousOperation = '+';
		newOne.sections = resultVector;
		return newOne;
	}
	public String toBasicString()
	{
		MultipleSection mu = (MultipleSection) Section.initialize(Section.MULTIPLE);
		mu.contents = new Section[sections.size()];
		for(int i=0; i<sections.size(); i++)
		{
			mu.contents[i] = sections.get(i);
		}
		return mu.toBasicString();
	}
	@Override
	public void addThis()
	{
		tracker.addPoint(this);
		
	}
	@Override
	public void addThis(boolean normal)
	{
		tracker.addPoint(this, normal);
		
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_DIFFERENTIAL_BLOCK;
	}
	@Override
	public void addThis(boolean normal, String message)
	{
		tracker.addPoint(this, normal, message);
		
	}
}
