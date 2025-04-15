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
package org.duckdns.hjow.commons.resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.duckdns.hjow.commons.core.Core;

public class ResourceManager {
    /** 해당 클래스의 클래스 경로에 있는 리소스 읽기 스트림을 불러옵니다. */
    public static InputStream getResourceByClass(Class<?> classObj, String name) {
        return classObj.getResourceAsStream(name);
    }
    /** 리소스에서 Properties 값을 불러옵니다. */
    public static Properties loadFrom(String name) {
        return loadFrom(ResourceManager.class, name);
    }
    
    /** 리소스에서 Properties 값을 불러옵니다. */
    public static Properties loadFrom(Class<?> classObj, String name) {
        InputStream inputStream = null;
        try {
            inputStream = getResourceByClass(classObj, name);
            if(inputStream == null) return null;
            
            Properties newProp = new Properties();
            newProp.loadFromXML(inputStream);
            
            inputStream.close();
            inputStream = null;
            
            return newProp;
        } catch(Throwable t) {
            Core.logError(t);
            return null;
        } finally {
            if(inputStream != null) {
                try { inputStream.close(); } catch(Throwable ignores) {}
            }
        }
    }
    
    /** 리소스에서 텍스트 값을 불러옵니다. */
    public static String readFrom(String name) {
        return readFrom(ResourceManager.class, name);
    }
    
    /** 리소스에서 텍스트 값을 불러옵니다. */
    public static String readFrom(Class<?> classObj, String name) {
        InputStream inputStream = null;
        InputStreamReader converter = null;
        BufferedReader reader = null;
        try {
            inputStream = getResourceByClass(classObj, name);
            if(inputStream == null) return null;
            
            converter = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(converter);
            
            StringBuilder results = new StringBuilder("");
            
            boolean isFirst = true;
            while(true) {
                String line = reader.readLine();
                if(line == null) break;
                
                if(! isFirst) results = results.append("\n");
                results = results.append(line);
                
                isFirst = false;
            }
            
            reader.close();
            reader = null;
            
            converter.close();
            converter = null;
            
            inputStream.close();
            inputStream = null;
            
            return results.toString();
        } catch(Throwable t) {
            Core.logError(t);
            return null;
        } finally {
            if(reader != null) {
                try { reader.close(); } catch(Throwable ignores) {}
            }
            if(converter != null) {
                try { converter.close(); } catch(Throwable ignores) {}
            }
            if(inputStream != null) {
                try { inputStream.close(); } catch(Throwable ignores) {}
            }
        }
    }
    
    /** 설정 기본값을 불러옵니다. */
    public static Properties getDefaultConfig() {
        return loadFrom("config.xml");
    }
    
    /** 스트링 테이블 기본값을 불러옵니다. */
    public static Properties getDefaultStringTable() {
        return loadFrom("stringTable.xml");
    }
}
