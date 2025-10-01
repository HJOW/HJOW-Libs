package org.duckdns.hjow.commons.vdp.work;

public class InputWork extends Work
{
	private static final long serialVersionUID = 6232153413878773358L;

	@Override
	public int getType()
	{
		return Work.WORK_INPUT;
	}

	@Override
	public String getDescription()
	{
		return "Input sections";
	}
}
