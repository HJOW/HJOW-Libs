package org.duckdns.hjow.commons.core;

/** 사용 종료 시 후속조치가 필요한 경우 구현, 리소스보다 큰 개념일 경우 사용 */
public interface Disposeable {
    public void dispose();
}
