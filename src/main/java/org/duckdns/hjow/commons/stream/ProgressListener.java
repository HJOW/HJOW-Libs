package org.duckdns.hjow.commons.stream;

/** 어떤 작업의 진행 상황을 보고하기 위한 인터페이스 */
public interface ProgressListener {
    public void onProgress(long now, long max, String msg);
}
