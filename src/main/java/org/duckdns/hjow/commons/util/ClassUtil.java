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
package org.duckdns.hjow.commons.util;

import java.io.Closeable;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.duckdns.hjow.commons.core.Disposeable;
import org.duckdns.hjow.commons.core.Releasable;

/**
 * 리플렉션을 유용하게 다루는데 사용할 만한 메소드들을 모았습니다.
 * 
 * @author HJOW
 *
 */
public class ClassUtil {
	/** 자바 버전 반환 */
    public static int getJavaMajorVersion() {
        String ver = System.getProperty("java.version");
        StringTokenizer dotTokenizer = new StringTokenizer(ver, ".");
        String strMajor;
        if(ver.startsWith("1.")) {
            dotTokenizer.nextToken();
            strMajor = dotTokenizer.nextToken().trim();
        } else {
            strMajor = dotTokenizer.nextToken().trim();
        }
        return Integer.parseInt(strMajor.trim());
    }
    
    /** 운영체제가 Windows 인지 확인 */
    public static boolean isWindowsOS() {
        String osName = System.getProperty("os.name");
        if(osName.toLowerCase().trim().startsWith("windows")) return true;
        return false;
    }
    
    /** Java Home 경로 반환 */
    public static File getJavaHome() {
    	String javaHome = System.getProperty("java.home");
    	return new File(javaHome);
    }
    
    /**
     * 클래스 풀네임 (패키지명 포함)과 class 파일이 있는 디렉토리를 입력하여 해당 클래스 객체를 읽어냅니다.
     * 
     * @param className : 클래스 풀네임
     * @param directory : class 파일이 위치한 디렉토리
     * @return 클래스 객체 (해당 클래스의 인스턴스가 아님, 그러나 이 클래스 객체를 통해 인스턴스를 생성할 수 있음)
     */
    public static Class<?> newInstanceFromClassFile(String className, File directory) throws MalformedURLException, ClassNotFoundException {
        URLClassLoader loader = null;
        try {
            loader = new URLClassLoader(new URL[] {
                    new URL("file://" + directory.getAbsolutePath())
            });
            return loader.loadClass(className);
        } catch(MalformedURLException t) {
            throw t;
        } catch(ClassNotFoundException t) {
            throw t;
        } finally {
            try { 
                if(loader != null) {
                    Method closeMethod = loader.getClass().getMethod("close");
                    closeMethod.invoke(loader);
                } 
            } catch(Throwable t1) {}
        }
    }
    
    /**
     * 클래스 풀네임 (패키지명 포함)과 class 파일이 있는 디렉토리를 입력하여 해당 클래스 객체를 읽어냅니다. newInstanceFromClassFile 메소드와 달리 try catch 문을 사용할 필요가 없습니다.
     * 단, 해당 클래스를 찾지 못하면 null을 반환합니다.
     * 
     * @param className : 클래스 풀네임
     * @param directory : class 파일이 위치한 디렉토리
     * @return 클래스 객체 (해당 클래스의 인스턴스가 아님, 그러나 이 클래스 객체를 통해 인스턴스를 생성할 수 있음)
     */
    public static Class<?> newInstanceFromClassFileRuntime(String className, File directory) {
        try {
            return newInstanceFromClassFile(className, directory);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    /**
     * 문자열 배열에서 매개변수를 읽어냅니다.
     * 
     * @param args 매개변수 입력 값들
     * @return
     */
    public static Map<String, String> convertAppParams(String[] args) {
        if(args == null) return null;
        Map<String, String> resultMap = new HashMap<String, String>();
        
        if(args.length == 0) return resultMap;
        if(args.length == 1) {
            String argOne = args[0].trim();
            if(argOne.startsWith("--")) {
                resultMap.put(argOne.substring("--".length()), "true");
            } else {
                resultMap.put("value", args[0]);
            }
        } else {
            String value = null;
            String keyCurrent = null;
            for(String a : args) {
                String argOne = a.trim();
                if(argOne.startsWith("--")) {
                    if(keyCurrent != null) {
                        keyCurrent = argOne.substring("--".length());
                        resultMap.put(keyCurrent, "true");
                        keyCurrent = null;
                        continue;
                    }
                    keyCurrent = argOne;
                } else {
                    if(keyCurrent == null) {
                        if(value != null) continue;
                        value = argOne;
                        resultMap.put("value", argOne);
                        continue;
                    }
                    resultMap.put(keyCurrent, argOne);
                    keyCurrent = null;
                }
            }
        }
        
        return resultMap;
    }
    
    /** 작업을 몇 밀리초 후에 수행합니다. */
    public static void runAfter(final Runnable r, final long timeMills) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(timeMills); } catch(Throwable ignores) { }
                r.run();
            }
        });
        thread.start();
    }
    
    /** set 메소드 캐멀 표기법을 소문자와 언더바를 이용한 표기법으로 바꿉니다. */
    public static String camelSetMethodToUnderbarWords(String setMethodName) {
        String removeSetKeyword = setMethodName.substring("set".length());
        char[] camels = removeSetKeyword.toCharArray();
        
        StringBuilder newMethodName = new StringBuilder("");
        for(int idx=0; idx<camels.length; idx++) {
            char charOne = camels[idx];
            if(! String.valueOf(charOne).toLowerCase().equals(String.valueOf(charOne))) { // 소문자로 바꿨는데 다르면 == 대문자
                charOne = String.valueOf(charOne).toLowerCase().charAt(0); // 소문자로 바꾸고
                newMethodName = newMethodName.append("_"); // 글자 추가하기 전에 앞에 언더바 붙이고
            }
            
            newMethodName = newMethodName.append(String.valueOf(charOne));
        }
        
        String resultFirst = newMethodName.toString();
        while(resultFirst.startsWith("_")) {
            resultFirst = resultFirst.substring(1);
        }
        return resultFirst;
    }
    
    /** 체인형 클래스 로더로 클래스 로딩, 디렉토리 입력 불가 !  */
    public static Class<?> loadChainedClass(File[] jarFile, String className) {
    	URLChaindClassLoadManager cl = null;
    	try {
    		cl = new URLChaindClassLoadManager(jarFile);
    		return cl.load(className);
    	} catch(Throwable tx) {
    		throw new RuntimeException(tx.getMessage(), tx);
    	} finally {
    		ClassUtil.closeAll(cl);
    	}
    }
    
    /** 체인형 클래스 로더로 클래스 로딩, 디렉토리 입력 불가 !  */
    public static Class<?> loadChainedClass(File[] jarFile, ClassLoader parent, String className) {
    	URLChaindClassLoadManager cl = null;
    	try {
    		cl = new URLChaindClassLoadManager(jarFile, parent);
    		return cl.load(className);
    	} catch(Throwable tx) {
    		throw new RuntimeException(tx.getMessage(), tx);
    	} finally {
    		ClassUtil.closeAll(cl);
    	}
    }
    
    /** 체인형 클래스 로더로 클래스 로딩, 디렉토리 입력 불가 !  */
    public static Class<?> loadChainedClass(List<File> jarFile, String className) {
    	URLChaindClassLoadManager cl = null;
    	try {
    		cl = new URLChaindClassLoadManager(jarFile);
    		return cl.load(className);
    	} catch(Throwable tx) {
    		throw new RuntimeException(tx.getMessage(), tx);
    	} finally {
    		ClassUtil.closeAll(cl);
    	}
    }
    
    /** 체인형 클래스 로더로 클래스 로딩, 디렉토리 입력 불가 !  */
    public static Class<?> loadChainedClass(List<File> jarFile, ClassLoader parent, String className) {
    	URLChaindClassLoadManager cl = null;
    	try {
    		cl = new URLChaindClassLoadManager(jarFile, parent);
    		return cl.load(className);
    	} catch(Throwable tx) {
    		throw new RuntimeException(tx.getMessage(), tx);
    	} finally {
    		ClassUtil.closeAll(cl);
    	}
    }
    
    /** 
     * 
     * 스트림을 순서대로 닫습니다. 하나 이상 예외가 발생하더라도 일단 모두 닫기를 시도합니다. 
     *     Closeable 인터페이스 구현체이거나, java.sql.Connection 객체인 경우 close 메소드를 호출합니다.
     *     그 외의 경우, close 메소드를 리플렉션으로 접근해 호출합니다.
     *     close 메소드가 없는 경우, dispose 메소드를 리플렉션으로 접근해 호출합니다.
     *     이 메소드도 없다면 표준 출력으로 경고가 출력되고 다음으로 넘어갑니다.
     */
    public static void closeAll(Object ... closeables) {
        if(closeables == null) return;
        for(Object c : closeables) {
            if(c == null) continue;
            try { 
                if(c instanceof Closeable          ) { ((Closeable) c).close();            continue; } 
                if(c instanceof java.sql.Connection) { ((java.sql.Connection) c).close();  continue; }
                if(c instanceof Releasable         ) { ((Releasable) c).releaseResource(); continue; }
                if(c instanceof Disposeable        ) { ((Disposeable) c).dispose();        continue; }
                Method mthd = null;
                
                try {
                    mthd = c.getClass().getMethod("close");
                    mthd.invoke(c);
                } catch(NoSuchMethodException e) {
                    mthd = c.getClass().getMethod("dispose");
                    mthd.invoke(c);
                }
                
            } catch(Throwable t) { System.out.println("Warn ! Exception occured when closing " + c.getClass().getName() + " - ( " + t.getClass().getName() + ") " + t.getMessage()); }
        }
    }
    
    /** 
     * 
     * 스트림을 순서대로 닫습니다. 하나 이상 예외가 발생하더라도 일단 모두 닫기를 시도합니다. 
     *     Closeable 인터페이스 구현체이거나, java.sql.Connection 객체인 경우 close 메소드를 호출합니다.
     *     그 외의 경우, close 메소드를 리플렉션으로 접근해 호출합니다.
     *     close 메소드가 없는 경우, dispose 메소드를 리플렉션으로 접근해 호출합니다.
     *     이 메소드도 없다면 표준 출력으로 경고가 출력되고 다음으로 넘어갑니다.
     */
    public static void closeAll(List<java.io.Closeable> closeables) {
        if(closeables == null) return;
        for(Object c : closeables) {
            if(c == null) continue;
            try {
                if(c instanceof Closeable          ) { ((Closeable) c).close();            continue; } 
                if(c instanceof java.sql.Connection) { ((java.sql.Connection) c).close();  continue; }
                if(c instanceof Releasable         ) { ((Releasable) c).releaseResource(); continue; }
                if(c instanceof Disposeable        ) { ((Disposeable) c).dispose();        continue; }
                Method mthd = null;
                
                try {
                    mthd = c.getClass().getMethod("close");
                    mthd.invoke(c);
                } catch(NoSuchMethodException e) {
                    mthd = c.getClass().getMethod("dispose");
                    mthd.invoke(c);
                }
                
            } catch(Throwable t) { System.out.println("Warn ! Exception occured when closing " + c.getClass().getName() + " - ( " + t.getClass().getName() + ") " + t.getMessage()); }
        }
    }
}
