package org.duckdns.hjow.commons.vdp.exceptions;

public class CannotTranslateToFunctionException extends NumberFormatException
{
	private static final long serialVersionUID = -818202430192145154L;
	public CannotTranslateToFunctionException(String message)
	{
		super(message);
	}
}
