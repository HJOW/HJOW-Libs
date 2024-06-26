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

import java.io.ByteArrayOutputStream;

import hjow.common.data.Binary;
import hjow.common.util.SecurityUtil;

public class SecurityObject extends ScriptObject {
    private static final long serialVersionUID = 1109355473872939220L;

    private static final SecurityObject uniqueObject = new SecurityObject();
    private SecurityObject() { }
    public static SecurityObject getInstance() {
        return uniqueObject;
    }
    @Override
    public String getPrefixName() {
        return "security";
    }
    
    @Override
    public String getInitScript(String accessKey) {
        StringBuilder initScript = new StringBuilder("");
        
        initScript = initScript.append("function hash(a, b) {                                                                ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".hash(a, b);                     ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function hashBytes(a, b) {                                                           ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".hashBytes(a, b);                ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function hexString(a) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".hexString(a);                   ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function hexBytes(a) {                                                               ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".hexBytes(a);                    ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function encrypt(a, b, c) {                                                          ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".encrypt(a, b, c);               ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        initScript = initScript.append("function decrypt(a, b, c) {                                                          ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".decrypt(a, b, c);               ").append("\n");
        initScript = initScript.append("};                                                                                   ").append("\n");
        
        return initScript.toString();
    }
    public static byte[] parseBytes(Object originalBytes) {
        byte[] bytes = null;
        if(originalBytes instanceof byte[]) bytes = (byte[]) originalBytes;
        else if(originalBytes instanceof ByteArrayOutputStream) bytes = ((ByteArrayOutputStream) originalBytes).toByteArray();
        else if(originalBytes instanceof Binary) bytes = ((Binary) originalBytes).toByteArray();
        return bytes;
    }
    public String hash(Object originals, Object algorithm) {
        return SecurityUtil.hash(String.valueOf(originals), String.valueOf(algorithm));
    }
    public Binary hashBytes(Object originalBytes, Object algorithm) {
        return new Binary(SecurityUtil.hashBytes(parseBytes(originalBytes) , String.valueOf(algorithm)));
    }
    public String hexString(Object binaries) {
        return SecurityUtil.hexString(parseBytes(binaries));
    }
    public Binary hexBytes(Object hexString) {
        return new Binary(SecurityUtil.hexBytes(String.valueOf(hexString)));
    }
    public String encrypt(Object text, Object key, Object algorithm) {
        return SecurityUtil.encrypt(String.valueOf(text), String.valueOf(key), String.valueOf(algorithm));
    }
    public String decrypt(Object text, Object key, Object algorithm) {
        return SecurityUtil.decrypt(String.valueOf(text), String.valueOf(key), String.valueOf(algorithm));
    }
}
