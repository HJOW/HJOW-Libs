package org.duckdns.hjow.commons.vdp.data;

import org.duckdns.hjow.commons.vdp.elements.Section;
import org.duckdns.hjow.commons.vdp.tracker.Tracker;
public class SectionData extends Data
{
	private static final long serialVersionUID = 114817294718924L;
	public Section contents;
	
	public SectionData(Tracker tracker)
	{
		super(tracker);
	}
	@Override
	public byte getDataType() 
	{
		return Data.SECTION;
	}

	@Override
	public String toBasicString() 
	{
		return contents.toBasicString();
	}

	@Override
	public Section differential() 
	{
		return contents.differential();
	}

	@Override
	public Data clone() 
	{
		SectionData newone = new SectionData(tracker);
		newone.contents = this.contents;
		return newone;
	}
	@Override
	public String getName()
	{
		return contents.getName();
	}
	@Override
	public int getTrackableClassType()
	{
		return Tracker.TRACKABLE_SECTION_DATA;
	}
}
