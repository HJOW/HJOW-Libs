package org.duckdns.hjow.commons.exception;

/** 알려진 예외 (예상치 못한 예외가 아닌, 필요에 의해 발생시키는 류의 예외) 를 다루는 클래스 */
public class KnownRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -6484528004766220868L;
	public KnownRuntimeException() { super(); }
    public KnownRuntimeException(String message) { super(message); }
    public KnownRuntimeException(Throwable causes) { super(causes); }
    public KnownRuntimeException(String message, Throwable causes) { super(message, causes); }
}
