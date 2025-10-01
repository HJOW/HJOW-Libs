package org.duckdns.hjow.commons.vdp.work;

public class InsertValueWork extends Work
{
	private static final long serialVersionUID = -4562099541068174869L;

	@Override
	public int getType()
	{
		return Work.WORK_INSERT_VALUE;
	}

	@Override
	public String getDescription()
	{
		return "Calculate value";
	}

}
