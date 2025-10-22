package org.duckdns.hjow.commons.exception;

public class IllegalScriptPatternException extends KnownRuntimeException {
	private static final long serialVersionUID = 3867552682903247560L;
	public IllegalScriptPatternException() { super(); }
	public IllegalScriptPatternException(Throwable t) { super(t); }
	public IllegalScriptPatternException(String msg) { super(msg); }
	public IllegalScriptPatternException(String msg, Throwable t) { super(msg, t); }
}