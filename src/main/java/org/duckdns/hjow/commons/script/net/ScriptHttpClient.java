package org.duckdns.hjow.commons.script.net;

import java.io.Serializable;

import org.duckdns.hjow.commons.net.HHttpClient;
import org.duckdns.hjow.commons.net.HHttpReceived;
import org.duckdns.hjow.commons.net.HHttpReceiving;

public class ScriptHttpClient implements Serializable {
    private static final long serialVersionUID = 996681364975462289L;
    protected HHttpClient client = new HHttpClient();
    
    public HHttpReceived request(Object url, Object sendBodys) {
        return client.request(url.toString(), sendBodys == null ? null : sendBodys.toString());
    }
    
    public HHttpReceiving requestStream(Object url, Object sendBodys) {
        return client.requestStream(url.toString(), sendBodys == null ? null : sendBodys.toString());
    }

    public HHttpClient getClient() {
        return client;
    }

    public void setClient(HHttpClient client) {
        this.client = client;
    }
}
