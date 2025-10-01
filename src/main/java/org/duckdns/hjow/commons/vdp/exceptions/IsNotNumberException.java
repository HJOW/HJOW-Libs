package org.duckdns.hjow.commons.vdp.exceptions;

public class IsNotNumberException extends CannotTranslateToFunctionException
{
	private static final long serialVersionUID = 7331448642705524027L;

	public IsNotNumberException(String message)
	{
		super(message + " : 입력된 값을 숫자로 변환하지 못했습니다.");
	}

}
