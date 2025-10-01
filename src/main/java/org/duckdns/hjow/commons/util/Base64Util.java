package org.duckdns.hjow.commons.util;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

/** 바이너리 혹은 문자열을 BASE64 문자열로 인코딩, 혹은 반대로 디코딩할 때 사용할 수 있는 Util, Java 6 호환 */
public class Base64Util {
    public static String encode(byte[] originalBinaries) {
        return DatatypeConverter.printBase64Binary(originalBinaries);
    }

    public static byte[] decode(String base64String) {
        return DatatypeConverter.parseBase64Binary(base64String);
    }

    public static String encodeString(String originalStr) {
    	try {
            byte[] originalBinaries = originalStr.getBytes("UTF-8");
            return encode(originalBinaries);
    	} catch(UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
    }
    
    public static String decodeString(String base64String) {
    	try { return new String(decode(base64String), "UTF-8"); } catch(UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
    }
}
