package org.duckdns.hjow.commons.vdp.work;

public class DifferentialWork extends Work
{
	private static final long serialVersionUID = 1488468116573861594L;

	@Override
	public int getType()
	{
		return Work.WORK_DIFFERENTIAL;
	}

	@Override
	public String getDescription()
	{
		return "Differential";
	}

}
