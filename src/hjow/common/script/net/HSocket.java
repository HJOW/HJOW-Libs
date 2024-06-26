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
package hjow.common.script.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import hjow.common.core.Releasable;

public class HSocket extends Socket implements Releasable {
    private static final long serialVersionUID = -8910240308523803495L;
    public HSocket() {
        super();
    }
    public HSocket(String host, int port) throws UnknownHostException, IOException {
        super(host, port);
    }
    @Override
    public void releaseResource() {
        try { close(); } catch(Throwable t) {}
    }
}
