/*
Copyright 2019 HJOW

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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.duckdns.hjow.commons.json.JsonArray;
import org.duckdns.hjow.commons.json.JsonObject;

public class HHttpReceived implements Serializable {
    private static final long serialVersionUID = 3605631869795951547L;
    protected Map<String, String> cookies = new HashMap<String, String>();
    protected Map<String, List<String>> headers = new HashMap<String, List<String>>();
    protected int code = -1;
    protected String body;
    
    public HHttpReceived() {
        
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
        
        if(headers.containsKey("Set-Cookie")) {
            List<String> reccookies = headers.get("Set-Cookie");
            StringTokenizer semiTokenizer  = null;
            StringTokenizer equalTokenizer = null;
            for(String c : reccookies) {
                semiTokenizer = new StringTokenizer(c, ";");
                while(semiTokenizer.hasMoreTokens()) {
                    String block = semiTokenizer.nextToken().trim();
                    equalTokenizer = new StringTokenizer(block, "=");
                    String key = equalTokenizer.nextToken();
                    String val = "";
                    if(equalTokenizer.hasMoreTokens()) val = equalTokenizer.nextToken();
                    this.cookies.put(key, val);
                }
            }
        }
    }
    
    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();
        json.put("body", getBody());
        json.put("code", new Integer(getCode()));
        JsonObject jsonHeader = new JsonObject();
        Set<String> keys = getHeaders().keySet();
        for(String k : keys) {
            JsonArray vals = new JsonArray();
            List<String> v = getHeaders().get(k);
            vals.addAll(v);
            jsonHeader.put(k, vals);
        }
        json.put("header", jsonHeader);
        JsonObject jsonCookie = new JsonObject();
        keys = getCookies().keySet();
        for(String k : keys) {
            jsonCookie.put(k, getCookies().get(k));
        }
        json.put("cookie", jsonCookie);
        return json;
    }
}
