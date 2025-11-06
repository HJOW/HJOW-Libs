package org.duckdns.hjow.commons.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.duckdns.hjow.commons.exception.KnownRuntimeException;

/** 바이너리 혹은 문자열을 BASE64 문자열로 인코딩, 혹은 반대로 디코딩할 때 사용할 수 있는 Util, Java 6 이상 호환 */
public class Base64Util {
	private static final Object J8Base64Encoder = getJava8Base64Encoder();
	private static final Object J8Base64Decoder = getJava8Base64Decoder();
	private static final Method J6Base64Encoder = getJava6Encoder();
	private static final Method J6Base64Decoder = getJava6Decoder();
	
	/** BASE64 인코딩 */
    public static String encode(byte[] originalBinaries) {
        Method mthd;
        
    	if(J8Base64Encoder != null) {
    		try {
    		    Class<?> j8class = J8Base64Encoder.getClass();
    		    mthd = j8class.getMethod("encodeToString", byte[].class);
    		    return (String) mthd.invoke(J8Base64Encoder, originalBinaries);
    		} catch (SecurityException e) {} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {} catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
    	}
    	
    	try {
			return (String) J6Base64Encoder.invoke("printBase64Binary", originalBinaries);
		} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {} catch (InvocationTargetException e) {}
    	throw new KnownRuntimeException("This JRE does not support BASE64");
    }

    /** BASE64 디코딩 */
    public static byte[] decode(String base64String) {
        Method mthd;
        
        if(J8Base64Decoder != null) {
        	try {
    		    Class<?> j8class = J8Base64Decoder.getClass();
    		    mthd = j8class.getMethod("decode", String.class);
    		    return (byte[]) mthd.invoke(J8Base64Decoder, base64String);
    		} catch (SecurityException e) {} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {} catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
        }
    
        try {
			return (byte[]) J6Base64Decoder.invoke("parseBase64Binary", base64String);
		} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {} catch (InvocationTargetException e) {}
    	throw new KnownRuntimeException("This JRE does not support BASE64");
    }

    /** BASE64 인코딩 */
    public static String encodeString(String originalStr) {
    	try {
            byte[] originalBinaries = originalStr.getBytes("UTF-8");
            return encode(originalBinaries);
    	} catch(UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
    }
    
    /** BASE64 디코딩 */
    public static String decodeString(String base64String) {
    	try { return new String(decode(base64String), "UTF-8"); } catch(UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
    }
    
    private static Method getJava6Decoder() {
    	try {
    		Class<?> dataTypeClass = Class.forName("javax.xml.bind.DatatypeConverter");
    		return dataTypeClass.getMethod("parseBase64Binary", String.class);
    	} catch(ClassNotFoundException ex) {  } catch (SecurityException e) {} catch (NoSuchMethodException e) {} catch (IllegalArgumentException e) {}
    	return null;
    }
    
    private static Method getJava6Encoder() {
    	try {
    		Class<?> dataTypeClass = Class.forName("javax.xml.bind.DatatypeConverter");
    		return dataTypeClass.getMethod("printBase64Binary", byte[].class);
    	} catch(ClassNotFoundException ex) {  } catch (SecurityException e) {} catch (NoSuchMethodException e) {} catch (IllegalArgumentException e) {}
    	return null;
    }
    
    private static Object getJava8Base64Decoder() {
    	if(ClassUtil.getJavaMajorVersion() >= 8) {
    		try { 
    			Class<?> base64Class = Class.forName("java.util.Base64");
    		    Method mthd = base64Class.getMethod("getDecoder");
    		    
    		    Object decoder = mthd.invoke(null);
    		    base64Class = decoder.getClass();
    		    
    		    base64Class.getMethod("decode", byte[].class);
    		    return decoder;
    		} catch(ClassNotFoundException ex) {  } catch (SecurityException e) {} catch (NoSuchMethodException e) {} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {} catch (InvocationTargetException e) {}
    	}
    	return null;
    }
    
    private static Object getJava8Base64Encoder() {
    	if(ClassUtil.getJavaMajorVersion() >= 8) {
    		try { 
    			Class<?> base64Class = Class.forName("java.util.Base64");
    		    Method mthd = base64Class.getMethod("getEncoder");
    		    
    		    Object decoder = mthd.invoke(null);
    		    base64Class = decoder.getClass();
    		    
    		    base64Class.getMethod("encode", byte[].class);
    		    return decoder;
    		} catch(ClassNotFoundException ex) {  } catch (SecurityException e) {} catch (NoSuchMethodException e) {} catch (IllegalArgumentException e) {} catch (IllegalAccessException e) {} catch (InvocationTargetException e) {}
    	}
    	return null;
    }
}
