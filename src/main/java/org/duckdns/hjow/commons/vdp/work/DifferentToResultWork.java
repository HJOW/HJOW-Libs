package org.duckdns.hjow.commons.vdp.work;

public class DifferentToResultWork extends Work
{
	private static final long serialVersionUID = -8411657652646433337L;

	@Override
	public int getType()
	{
		return Work.WORK_DIFFERENT_TO_RESULT;
	}

	@Override
	public String getDescription()
	{
		return "미분된 식 란에 있는 식을 결과식 란으로 옮겼습니다.";
	}

}
