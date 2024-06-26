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
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import hjow.common.core.Core;
import hjow.common.script.net.ScriptHttpClient;

public class WebObject extends ScriptObject {
    private static final long serialVersionUID = 3844963995027549279L;
    protected HScriptEngine engine;
    public WebObject(HScriptEngine engine) { 
        this.engine = engine;
    }
    @Override
    public String getPrefixName() {
        return "web";
    }
    @Override
    public String getInitScript(String accessKey) {
        StringBuilder initScript = new StringBuilder("");
        
        initScript = initScript.append("function web_create() {                                                                                 ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".create();                                          ").append("\n");
        initScript = initScript.append("};                                                                                                      ").append("\n");
        initScript = initScript.append("function web_server(port) {                                                                             ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".createWithPort(port);                              ").append("\n");
        initScript = initScript.append("};                                                                                                      ").append("\n");
        initScript = initScript.append("function web_client() {                                                                                 ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".client();                                          ").append("\n");
        initScript = initScript.append("};                                                                                                      ").append("\n");
        return initScript.toString();
    }
    
    public Object create() {
        try {
            Class<?> webServerClass = Class.forName("hjow.common.script.web.WebServerObject");
            Constructor<?> cons = webServerClass.getConstructor(HScriptEngine.class);
            return cons.newInstance(engine);
        } catch(ClassNotFoundException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        } catch (Throwable e) {
            Core.logError(e);
            return null;
        }
    }
    
    public Object createWithPort(Object port) throws IOException {
        try {
            Class<?> webServerClass = Class.forName("hjow.common.script.web.WebServerObject");
            Constructor<?> cons = webServerClass.getConstructor(HScriptEngine.class);
            Object server = cons.newInstance(engine);
            
            Method initMethod = webServerClass.getMethod("init", Object.class);
            initMethod.invoke(server, port);
            return server;
        } catch(ClassNotFoundException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        } catch (Throwable e) {
            Core.logError(e);
            return null;
        }
    }
    
    public Object client() {
        return new ScriptHttpClient();
    }
}
