package org.duckdns.hjow.commons.vdp.work;

public class UpdateGraphWork extends Work
{
	private static final long serialVersionUID = 148471170585324216L;

	@Override
	public int getType()
	{
		return Work.WORK_UPDATE_GRAPH;
	}

	@Override
	public String getDescription()
	{
		return "Graph Update";
	}

}
