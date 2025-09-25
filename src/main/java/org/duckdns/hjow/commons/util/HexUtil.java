package org.duckdns.hjow.commons.util;

import java.io.UnsupportedEncodingException;

/** 바이너리 혹은 문자열을 HEX문자열로 인코딩, 혹은 반대로 디코딩할 때 사용할 수 있는 Util */
public class HexUtil {
    public static String encode(byte[] originalBinaries) {
        StringBuilder res = new StringBuilder("");
        for(byte b : originalBinaries) {
            res = res.append(String.format("%02x", b & 0xff));
        }
        return res.toString().trim();
    }
    
    public static byte[] decode(String hexString) {
        if(hexString.length() % 2 != 0) hexString = "0" + hexString;
        byte[] res = new byte[hexString.length() / 2];
        for(int idx=0; idx<hexString.length(); idx+= 2) {
            String byteStr = hexString.substring(idx, idx + 2);
            int byteVal = Integer.parseInt(byteStr, 16);
            res[idx/2] = (byte) byteVal;
        }
        
        return res;
    }
    
    public static String encodeString(String originalStr) {
    	try {
            byte[] originalBinaries = originalStr.getBytes("UTF-8");
            return encode(originalBinaries);
    	} catch(UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
    }
    
    public static String decodeString(String hexString) {
    	try { return new String(decode(hexString), "UTF-8"); } catch(UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
    }
}
