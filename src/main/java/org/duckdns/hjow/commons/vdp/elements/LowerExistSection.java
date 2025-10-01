package org.duckdns.hjow.commons.vdp.elements;

import org.duckdns.hjow.commons.vdp.data.*;
import org.duckdns.hjow.commons.vdp.tracker.*;
public abstract class LowerExistSection extends Section
{
	public LowerExistSection(Tracker tracker)
	{
		super(tracker);
	}
	private static final long serialVersionUID = 5177243406102351551L;
	public DoubleData lower;
	@Override
	public boolean hasLower()
	{
		return true;
	}
}
