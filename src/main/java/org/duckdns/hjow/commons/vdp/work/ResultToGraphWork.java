package org.duckdns.hjow.commons.vdp.work;

public class ResultToGraphWork extends Work
{
	private static final long serialVersionUID = -3763954517062540939L;

	@Override
	public int getType()
	{
		return Work.WORK_RESULT_TO_GRAPH;
	}

	@Override
	public String getDescription()
	{
		return "결과식을 그래프로 그렸습니다.";
	}

}
