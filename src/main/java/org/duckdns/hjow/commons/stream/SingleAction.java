package org.duckdns.hjow.commons.stream;

/** SimultaneousWork 사용을 위한 인터페이스 */
public interface SingleAction {
	public void run(int index) throws Throwable;
}