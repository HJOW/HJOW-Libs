/*
Copyright 2024 HJOW

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.duckdns.hjow.commons.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.duckdns.hjow.commons.util.ClassUtil;

/**
 * 
 * @author HJOW
 *
 */
public class HHttpClient {
    protected Map<String, String> cookies = new HashMap<String, String>();
    protected String charset = "UTF-8";
    protected String method  = "POST";
    protected String contentType = "text/html; charset=utf-8";
    
    /** HTTP 요청을 보냅니다. */
    @SuppressWarnings("unused")
    public HHttpReceived request(String url, String sendBodys) {
        URLConnection conn = null;
        HttpURLConnection conn1 = null;
        HttpsURLConnection conn2 = null;
        OutputStream out1 = null;
        InputStream in1 = null;
        InputStreamReader in2 = null;
        BufferedReader in3 = null;
        Throwable caught = null;
        try {
            HHttpReceived recv = new HHttpReceived();
            
            if(! url.startsWith("http://") || url.startsWith("https://")) {
                url = "http://" + url;
            }
            
            Set<String> sendingCookies = cookies.keySet();
            StringBuilder cookie = new StringBuilder("");
            for(String s : sendingCookies) {
                if(cookie.length() >= 1) cookie = cookie.append(";");
                cookie = cookie.append(s).append("=").append(this.cookies.get(s));
            }
            
            if(url.startsWith("http://")) {
                conn1 = (HttpURLConnection) new URL(url).openConnection();
                conn1.setDoOutput(true);
                conn1.setRequestMethod(method);
                conn1.setRequestProperty("Cookie", cookie.toString().trim());
                conn1.setRequestProperty("Content-Type", contentType);
                conn1.connect();
                conn = conn1;
            } else {
                conn2 = (HttpsURLConnection) new URL(url).openConnection();
                conn2.setDoOutput(true);
                conn2.setRequestMethod(method);
                conn2.setRequestProperty("Cookie", cookie.toString().trim());
                conn2.setRequestProperty("Content-Type", contentType);
                conn2.connect();
                conn = conn1;
            }
            
            out1 = conn.getOutputStream();
            if(sendBodys != null) out1.write(sendBodys.getBytes(charset));
            out1.flush();
            out1.close();
            out1 = null;
            
            int code = -1;
            if(conn1 != null) code = conn1.getResponseCode();
            if(conn2 != null) code = conn2.getResponseCode();
            recv.setCode(code);
            
            Map<String, List<String>> headers = conn.getHeaderFields();
            recv.setHeaders(headers);
            this.cookies.putAll(recv.getCookies());
            
            StringBuilder bodys = new StringBuilder("");
            in1 = conn.getInputStream();
            in2 = new InputStreamReader(in1, charset);
            in3 = new BufferedReader(in2);
            
            while(true) {
                String line = in3.readLine();
                if(line == null) break;
                bodys = bodys.append("\n").append(line);
            }
            recv.setBody(bodys.toString().trim());
            bodys = null;
            
            ClassUtil.closeAll(conn, out1, in3, in2, in1);
            return recv;
        } catch(Throwable t) {
            caught = t;
        } finally {
            ClassUtil.closeAll(conn, out1, in3, in2, in1);
        }
        
        if(caught != null) throw new RuntimeException(caught.getMessage(), caught);
        return null;
    }
    
    /** HTTP 요청을 보내고, 응답을 InputStream 객체로 받아 반환합니다. 접속 연결이 끊어지지 않은 상태이므로, 사용 종료 후 close() 를 호출해야 합니다. */
    @SuppressWarnings("unused")
    public HHttpReceiving requestStream(String url, String sendBodys) {
        URLConnection conn = null;
        HttpURLConnection conn1 = null;
        HttpsURLConnection conn2 = null;
        OutputStream out1 = null;
        Throwable caught = null;
        try {
            HHttpReceiving recv = new HHttpReceiving();
            
            if(! url.startsWith("http://") || url.startsWith("https://")) {
                url = "http://" + url;
            }
            
            Set<String> sendingCookies = cookies.keySet();
            StringBuilder cookie = new StringBuilder("");
            for(String s : sendingCookies) {
                if(cookie.length() >= 1) cookie = cookie.append(";");
                cookie = cookie.append(s).append("=").append(this.cookies.get(s));
            }
            
            if(url.startsWith("http://")) {
                conn1 = (HttpURLConnection) new URL(url).openConnection();
                conn1.setDoOutput(true);
                conn1.setRequestMethod(method);
                conn1.setRequestProperty("Cookie", cookie.toString().trim());
                conn1.setRequestProperty("Content-Type", contentType);
                conn1.connect();
                conn = conn1;
            } else {
                conn2 = (HttpsURLConnection) new URL(url).openConnection();
                conn2.setDoOutput(true);
                conn2.setRequestMethod(method);
                conn2.setRequestProperty("Cookie", cookie.toString().trim());
                conn2.setRequestProperty("Content-Type", contentType);
                conn2.connect();
                conn = conn1;
            }
            recv.setConnection(conn);
            
            out1 = conn.getOutputStream();
            if(sendBodys != null) out1.write(sendBodys.getBytes(charset));
            out1.flush();
            out1.close();
            out1 = null;
            
            int code = -1;
            if(conn1 != null) code = conn1.getResponseCode();
            if(conn2 != null) code = conn2.getResponseCode();
            recv.setCode(code);
            
            Map<String, List<String>> headers = conn.getHeaderFields();
            recv.setHeaders(headers);
            this.cookies.putAll(recv.getCookies());
            
            recv.setReceiving(conn.getInputStream());
            
            ClassUtil.closeAll(out1);
            return recv;
        } catch(Throwable t) {
            caught = t;
        } finally {
            ClassUtil.closeAll(out1);
        }
        
        if(caught != null) throw new RuntimeException(caught.getMessage(), caught);
        return null;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
