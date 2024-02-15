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
package hjow.common.script;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import hjow.common.core.Releasable;
import hjow.common.data.Binary;
import hjow.common.script.net.HServerSocket;
import hjow.common.script.net.HSocket;
import hjow.common.util.NetUtil;

public class NetObject extends ScriptObject {
    private static final long serialVersionUID = -7118261219556328630L;
    private static final NetObject uniqueObject = new NetObject();
    protected List<Releasable> releasables;
    private NetObject() { releasables = new Vector<Releasable>();  }
    public static NetObject getInstance() {
        return uniqueObject;
    }
    @Override
    public String getPrefixName() {
        return "net";
    }
    @Override
    public void releaseResource() {
    	super.releaseResource();
    	for(Releasable r : releasables) {
    		try { r.releaseResource(); } catch(Throwable t) {}
    	}
    	releasables.clear();
    }
    @Override
    public String getInitScript(String accessKey) {
        StringBuilder initScript = new StringBuilder("");
        
        initScript = initScript.append("function net_sendPost(url, param, contentType, encoding) {                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".sendPost(url, param, contentType, encoding);       ").append("\n");
        initScript = initScript.append("};                                                                                                      ").append("\n");
        initScript = initScript.append("function net_sendPostBytes(url, param, contentType) {                                                   ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".sendPostBytes(url, param, contentType);            ").append("\n");
        initScript = initScript.append("};                                                                                                      ").append("\n");
        initScript = initScript.append("function net_socket_open(port) {                                                                        ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".openServerSocket(port);                            ").append("\n");
        initScript = initScript.append("};                                                                                                      ").append("\n");
        initScript = initScript.append("function net_socket_connect(host, port) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".connectSocket(host, port);                         ").append("\n");
        initScript = initScript.append("};                                                                                                      ").append("\n");
        
        return initScript.toString();
    }
    
    public String sendPost(Object url, Object parameters, Object contentType, Object parameterEncoding) throws Throwable {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if(parameters != null && parameters instanceof Map<?, ?>) {
            Map<?, ?> rawMap = (Map<?, ?>) parameters;
            Set<?> keys = rawMap.keySet();
            for(Object k : keys) {
                paramMap.put(String.valueOf(k), String.valueOf(rawMap.get(k)));
            }
        }
        
        return NetUtil.sendPost(new URL(String.valueOf(url)), paramMap, String.valueOf(contentType), String.valueOf(parameterEncoding));
    }
    
    public Binary sendPostBytes(Object url, Object parameters, Object contentType) throws MalformedURLException, IOException {
        return new Binary(NetUtil.sendPostBytes(new URL(String.valueOf(url)), (byte[]) parameters, String.valueOf(contentType)));
    }
    
    public HServerSocket openServerSocket(Object port) throws IOException {
    	int portNo = -1;
    	if(port instanceof Number) portNo = ((Number) port).intValue();
    	else portNo = new BigDecimal(String.valueOf(port)).intValue();
    	HServerSocket socket = new HServerSocket(portNo);
    	releasables.add(socket);
    	return socket;
    }
    
    public HSocket connectSocket(Object host, Object port) throws UnknownHostException, IOException {
    	int portNo = -1;
    	if(port instanceof Number) portNo = ((Number) port).intValue();
    	else portNo = new BigDecimal(String.valueOf(port)).intValue();
    	HSocket socket = new HSocket(String.valueOf(host), portNo);
    	releasables.add(socket);
    	return socket;
    }
}
