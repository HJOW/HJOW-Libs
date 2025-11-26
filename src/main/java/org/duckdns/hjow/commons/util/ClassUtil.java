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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.duckdns.hjow.commons.core.Disposeable;
import org.duckdns.hjow.commons.core.Releasable;
import org.duckdns.hjow.commons.exception.KnownRuntimeException;
import org.duckdns.hjow.commons.util.classwrapper.ClassWrapper;

/**
 * 리플렉션을 유용하게 다루는데 사용할 만한 메소드들을 모았습니다.
 * 
 * @author HJOW
 *
 */
public class ClassUtil {
	public static final ClassLoader ROOT_CLASS_LOADER = Thread.currentThread().getContextClassLoader();
	private static final List<URLClassLoader> urlClassLoaders = new ArrayList<URLClassLoader>();
	
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
    
    /** 기본 캐릭터셋 반환 */
    public static String getDefaultCharset() {
    	return Charset.defaultCharset().toString();
    }
    
    /** Java Home 경로 반환 */
    public static File getJavaHome() {
    	String javaHome = System.getProperty("java.home");
    	return new File(javaHome);
    }
    
    /**
     * 클래스 풀네임 (패키지명 포함)과 class 파일이 있는 디렉토리를 입력하여 해당 클래스 객체를 읽어냅니다.
     * 
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
            urlClassLoaders.add(loader);
            return loader.loadClass(className);
        } catch(MalformedURLException t) {
            throw t;
        } catch(ClassNotFoundException t) {
            throw t;
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
    
    /** 현재 이 런타임의 클래스패스들을 반환 */
    public static Set<String> getClasspaths() {
    	String classpaths = System.getProperty("java.class.path");
    	
    	Set<String> list = new HashSet<String>();
    	StringTokenizer classpathTokenizer = new StringTokenizer(classpaths, File.pathSeparator); // File.separator 와는 다름에 주의 !
    	while(classpathTokenizer.hasMoreTokens()) {
    		String res = classpathTokenizer.nextToken().trim();
    		list.add(res);
    	}
    	return list;
    }
    
    /** 현재 이 런타임 내 모든 클래스 이름들을 반환. 단 자바 런타임의 클래스는 반환되지 않으며, * 기호가 들어간 클래스패스는 제외됨. 또한 서블릿 프로젝트의 WEB-INF / lib 디렉토리 내 jar 파일들 또한 보증하지 못함. */
    public static Set<String> getAllClassNames() {
    	Set<String> list = new HashSet<String>();
    	
    	for(String classPaths : getClasspaths()) {
    		if(classPaths.contains("*")) continue;
    		
    		File file = new File(classPaths);
    		if(! file.exists()) continue;
    		
    		if(file.isDirectory()) {
    			list.addAll(getClassNamesFromDirectory(file));
    		} else {
    			String lower = file.getName().toLowerCase();
    			if(! lower.endsWith(".jar")) continue;
    			
    			list.addAll(getClassNamesFromJar(file));
    		}
    	}
    	
    	return list;
    }
    
    /** 해당 디렉토리로부터 클래스 이름들을 찾아 반환. 이 디렉토리를 해당 클래스패스 경로로 생각하고 인식하므로, 안에 있는 다른 리소스 파일이나 jar 파일은 읽지 않음. */
    public static Set<String> getClassNamesFromDirectory(File dir) {
    	if(! dir.exists()) throw new KnownRuntimeException("There is no directory ! " + dir.getAbsolutePath());
    	if(! dir.isDirectory()) throw new KnownRuntimeException("This is not a directory ! " + dir.getAbsolutePath());
    	
    	String nowDir = dir.getAbsolutePath();
    	nowDir = nowDir.replace(File.separator, "/"); // 디렉토리 구분자 /로 통일
    	if(! nowDir.endsWith("/")) nowDir = nowDir + "/";
    	
    	Set<String> list = new HashSet<String>();
    	list.addAll(getClassNamesFromDirectory(dir, ""));
    	return list;
    }
    
    /** 외부에서 직접 호출 금지 ! */
    private static Set<String> getClassNamesFromDirectory(File dir, String packageName) {
    	Set<String> list = new HashSet<String>();
    	File[] files = dir.listFiles();
    	
    	for(File f : files) {
    		if(f.isDirectory()) {
    			list.addAll(getClassNamesFromDirectory(f, packageName + f.getName() + "."));
    		} else {
    			String name = f.getName();
    			if(! name.toLowerCase().endsWith(".class")) continue;
    			name = packageName + name.trim(); // 클래스 풀네임이 필요하므로, 앞에 패키지명 추가
    			
    			// 확장자 제거
    	    	name = name.substring(0, name.length() - 6);
    	    	
    	    	// $ 기호 있는 경우 뒷부분 자르기
    	    	if(name.contains("$")) {
    	    		int dollorIndex = name.indexOf("$");
    	    		name = name.substring(0, dollorIndex);
    	    	}
    			
    			list.add(name);
    		}
    	}
    	
    	return list;
    }
    
    /** 해당 jar 파일로부터 클래스 이름들을 찾아 반환 */
    public static Set<String> getClassNamesFromJar(File jarFile) {
    	if(! jarFile.exists()) throw new KnownRuntimeException("There is no jar ! " + jarFile.getAbsolutePath());
    	if(jarFile.isDirectory()) throw new KnownRuntimeException("This is not a file ! " + jarFile.getAbsolutePath());
    	Set<String> list = new HashSet<String>();
    	
    	JarFile jarInst = null;
    	try {
    	    jarInst = new JarFile(jarFile, false, JarFile.OPEN_READ);
    	    Enumeration<JarEntry> entries = jarInst.entries();
    	    while(entries.hasMoreElements()) {
    	    	JarEntry entry = entries.nextElement();
    	    	String name = entry.getName();
    	    	name = name.replace("/", ".");
    	    	
    	    	// 디렉토리와 리소스들 제외
    	    	if(! name.endsWith(".class")) continue;
    	    	if(entry.isDirectory()) continue;
    	    	// 확장자 제거
    	    	name = name.substring(0, name.length() - 6);
    	    	
    	    	// $ 기호 있는 경우 뒷부분 자르기
    	    	if(name.contains("$")) {
    	    		int dollorIndex = name.indexOf("$");
    	    		name = name.substring(0, dollorIndex);
    	    	}
    	    	
    	    	// 목록에 추가
    	    	list.add(name);
    	    }
    	} catch(Exception ex) {
    		throw new RuntimeException(ex.getMessage(), ex);
    	} finally {
    		ClassUtil.closeAll(jarInst);
    	}
    	return list;
    }
    
    /** 클래스 이름으로 클래스 객체 불러오기, 초기화 (static 블록 호출) 여부 지정 가능. (주의 ! 반환된 Class 객체가 나타내는 대상이 클래스가 아닌, 인터페이스나 어노테이션일 수도 있음.) */
    public static Class<?> forName(String className, boolean initialize) throws ClassNotFoundException {
    	ClassLoader loader = Thread.currentThread().getContextClassLoader();
    	return Class.forName(className, initialize, loader);
    }
    
    /** 해당 클래스/인터페이스의 자식 클래스들 모두 찾기 (반환되는 목록에서는 인터페이스와 어노테이션은 제외됨) */
    public static Set<Class<?>> getChildClasses(Class<?> parent, boolean initialize) {
    	Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(String className : getAllClassNames()) {
        	try {
        	    Class<?> classOne = forName(className, false); // 일단 초기화하지 않고 불러오기
        	    if(classOne.isInterface() ) continue; // 인터페이스 건너뛰기
        	    if(classOne.isAnnotation()) continue; // 어노테이션 건너뛰기
        	    
        	    // 부모관계 아니면 건너뛰기
        	    if(parent.isInterface()) {
        	    	if(! parent.isAssignableFrom(classOne)) continue;
        	    } else {
        	    	if(! parent.isInstance(classOne)) continue;
        	    }
        	    
        	    if(initialize) classOne = forName(className, true); // 초기화 필요 시, 다시 불러오기
                classSet.add(classOne);
        	} catch(ClassNotFoundException ex) {
        		System.out.println(className + " not found !");
        	} catch(NoClassDefFoundError ex) {
        		System.out.println(className + " cannot be load ! " + ex.getMessage());
        	} catch(UnsupportedClassVersionError ex) {
        		System.out.println(className + " class version is unsupported !" + ex.getMessage());
        	} catch(Throwable ex) {
        	    throw new RuntimeException(ex.getMessage(), ex);
        	}
        }
        return classSet;
    }
    
    /** 리플렉션을 통해 해당 객체의 메소드 호출 */
    public static Object invokeMethod(Object instance, String methodName, Object ... params) {
    	try {
    		Class<?> classObj = instance.getClass();
    		
    		if(params == null) params = new Object[0];
    		Class<?>[] paramClasses = new Class<?>[params.length];
    		for(int idx=0; idx<paramClasses.length; idx++) {
    			paramClasses[idx] = params[idx].getClass();
    		}
    		
    		Method mthd = classObj.getMethod(methodName, paramClasses);
    		return mthd.invoke(instance, params);
    	} catch(Exception ex) {
    		throw new RuntimeException(ex.getMessage(), ex);
    	}
    }
    
    /** 리플렉션을 통해 해당 클래스의 static 메소드 호출 */
    public static Object invokeStaticMethod(String classFullName, String methodName, Object ... params) {
    	try {
    		Class<?> classObj = Class.forName(classFullName);
    		
    		if(params == null) params = new Object[0];
    		Class<?>[] paramClasses = new Class<?>[params.length];
    		for(int idx=0; idx<paramClasses.length; idx++) {
    			paramClasses[idx] = params[idx].getClass();
    		}
    		
    		Method mthd = classObj.getMethod(methodName, paramClasses);
    		return mthd.invoke(null, params);
    	} catch(Exception ex) {
    		throw new RuntimeException(ex.getMessage(), ex);
    	}
    }
    
    /** 해당 jar 파일을 통해 클래스를 불러올 수 있는 클래스로더 생성 */
	public static URLClassLoader newClassLoader(File file) {
		return newClassLoader(file, ROOT_CLASS_LOADER);
	}
	
	/** 해당 jar 파일을 통해 클래스를 불러올 수 있는 클래스로더 생성 */
	public static URLClassLoader newClassLoader(File file, ClassLoader parent) {
		File[] files = new File[1];
		files[0] = file;
		return newClassLoader(files, parent);
	}
	
	/** 해당 jar 파일을 통해 클래스를 불러올 수 있는 클래스로더 생성 - 주의 ! jar 파일인 경우 여러 파일을 넣으면 인식하지 못할 수 있음. */
	public static URLClassLoader newClassLoader(File[] files) {
		return newClassLoader(files, ROOT_CLASS_LOADER);
	}
	
	/** 해당 jar 파일을 통해 클래스를 불러올 수 있는 클래스로더 생성 - 주의 ! jar 파일인 경우 여러 파일을 넣으면 인식하지 못할 수 있음. */
	public static URLClassLoader newClassLoader(File[] files, ClassLoader parent) {
		try {
			URL[] urls = new URL[files.length];
			for(int idx=0; idx<urls.length; idx++) {
				urls[idx] = files[idx].toURI().toURL();
			}
			
			return newClassLoader(urls, parent);
		} catch(RuntimeException ex) {
			throw ex;
		} catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	/** 해당 jar 파일을 통해 클래스를 불러올 수 있는 클래스로더 생성 */
	public static URLClassLoader newClassLoader(URL url) {
		return newClassLoader(url, ROOT_CLASS_LOADER);
	}
	
	/** 해당 jar 파일을 통해 클래스를 불러올 수 있는 클래스로더 생성 */
	public static URLClassLoader newClassLoader(URL url, ClassLoader parent) {
		URL[] urls = new URL[1];
		urls[0] = url;
		return newClassLoader(urls, parent);
	}
	
	/** 해당 jar 파일을 통해 클래스를 불러올 수 있는 클래스로더 생성 - 주의 ! jar 파일인 경우 여러 파일을 넣으면 인식하지 못할 수 있음. */
	public static URLClassLoader newClassLoader(URL[] urls) {
		return newClassLoader(urls, ROOT_CLASS_LOADER);
	}
	
	/** 해당 jar 파일을 통해 클래스를 불러올 수 있는 클래스로더 생성 - 주의 ! jar 파일인 경우 여러 파일을 넣으면 인식하지 못할 수 있음. */
	public static URLClassLoader newClassLoader(URL[] urls, ClassLoader parent) {
		try {
		    URLClassLoader newOne = new URLClassLoader(urls, parent);
		    urlClassLoaders.add(newOne);
		    
		    return newOne;
		} catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	/** ClassWrapper 들의 List 로부터 Class 리스트 생성 */
    public static List<Class<?>> getClassListsFrom(List<ClassWrapper> wrappers) {
    	List<Class<?>> list = new ArrayList<Class<?>>();
    	for(ClassWrapper cls : wrappers) {
    		list.add(cls.getWrappedClass());
    	}
    	return list;
    }
    
    /** 모든 URLClassLoader 닫기 
     * @see ClassUtil.closeAllURLClassLoaders
     */
    @Deprecated
	public static synchronized void closeAll() {
		closeAllURLClassLoaders();
	}
	
	/** 모든 URLClassLoader 닫기 */
	public static synchronized void closeAllURLClassLoaders() {
		ClassUtil.closeAll(urlClassLoaders);
		urlClassLoaders.clear();
	}
}
