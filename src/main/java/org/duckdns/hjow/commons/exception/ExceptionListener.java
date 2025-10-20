package org.duckdns.hjow.commons.exception;

/** 예외 발생 이벤트 관련 인터페이스 */
public interface ExceptionListener {
    public void onExceptionOccured(Throwable t, String additionalMessage);
}
