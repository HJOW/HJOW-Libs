package org.duckdns.hjow.commons.vdp.exceptions;

public class IsNotFunctionException extends CannotTranslateToFunctionException
{
	private static final long serialVersionUID = -4459865423237668642L;
	
	public IsNotFunctionException(String message)
	{
		super(message + " : 입력된 값을 함수로 변환하지 못했습니다.");
	}

}
