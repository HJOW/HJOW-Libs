package org.duckdns.hjow.commons.vdp.work;

public class DifferentToGraphWork extends Work
{
	private static final long serialVersionUID = -5702848091072984950L;

	@Override
	public int getType()
	{
		return Work.WORK_DIFFERENT_TO_GRAPH;
	}

	@Override
	public String getDescription()
	{
		return "미분된 식을 그래프로 그렸습니다.";
	}

}
