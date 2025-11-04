package org.duckdns.hjow.commons.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.duckdns.hjow.commons.core.Disposeable;

/** 체인형 클래스 로더 */
public class URLChaindClassLoadManager implements Disposeable {
	protected transient List<URLClassLoader> list = new ArrayList<URLClassLoader>();
	
	public URLChaindClassLoadManager(List<File> jarFiles) {
		this(jarFiles, null);
	}
	public URLChaindClassLoadManager(List<File> jarFiles, ClassLoader parent) {
		try {
			if(parent == null) parent = Thread.currentThread().getContextClassLoader();
			
			URLClassLoader last = null;
			for(File f : jarFiles) {
				URL[] urls = new URL[1];
				urls[0] = f.toURI().toURL();
				
				if(last != null) last = new URLClassLoader(urls, last);
				else             last = new URLClassLoader(urls, parent);
				list.add(last);
			}
		} catch(Throwable tx) {
		    throw new RuntimeException(tx);
		}
	}
	
	public URLChaindClassLoadManager(File[] jarFiles) {
		this(jarFiles, null);
	}
	public URLChaindClassLoadManager(File[] jarFiles, ClassLoader parent) {
		try {
			if(parent == null) parent = Thread.currentThread().getContextClassLoader();
			
			URLClassLoader last = null;
			for(File f : jarFiles) {
				URL[] urls = new URL[1];
				urls[0] = f.toURI().toURL();
				
				if(last != null) last = new URLClassLoader(urls, last);
				else             last = new URLClassLoader(urls, parent);
				list.add(last);
			}
		} catch(Throwable tx) {
		    throw new RuntimeException(tx);
		}
	}
	
	public URLClassLoader getLastClassLoader() {
		return list.get(list.size()-1);
	}
	
	public Class<?> load(String className) {
		try {
		    return getLastClassLoader().loadClass(className);
		} catch(Throwable tx) {
		    throw new RuntimeException(tx);
		}
	}
	
	@Override
	public void dispose() {
		for(int idx=list.size()-1; idx>=0; idx--) {
			URLClassLoader c = list.get(idx);
			ClassUtil.closeAll(c);
		}
		list.clear();
	}
}