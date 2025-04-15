/*
 
 Copyright 2015 HJOW

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

import java.lang.reflect.Method;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>이 클래스에는 보안에 관련된 다양한 정적 메소드들이 있습니다.</p>
 * 
 * @author HJOW
 *
 */
public class SecurityUtil
{
    /**
     * <p>텍스트의 해시값을 구합니다.</p>
     * 
     * @param text : 원래의 텍스트
     * @param algorithm : 해싱 알고리즘
     * @return 해시값
     */
    public static String hash(String text, String algorithm)
    {
        MessageDigest digest = null;
        String methods = algorithm;
        if(methods == null) methods = "SHA-256";
        
        try
        {
            digest = MessageDigest.getInstance(methods);
            
            byte[] beforeBytes = text.getBytes("UTF-8");
            byte[] afterBytes = digest.digest(beforeBytes);

            return hexString(afterBytes);
        }
        catch(Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * <p>바이너리의 해시값을 구합니다.</p>
     * 
     * @param beforeBytes : 원래의 바이너리
     * @param algorithm   : 해싱 알고리즘
     * @return 해시값
     */
    public static byte[] hashBytes(byte[] beforeBytes, String algorithm)
    {
        MessageDigest digest = null;
        String methods = algorithm;
        if(methods == null) methods = "SHA-256";
        
        try
        {
            digest = MessageDigest.getInstance(methods);
            return digest.digest(beforeBytes);
        }
        catch(Throwable e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 바이너리를 16진수 형태의 문자열로 변환합니다. 문자열 형태로 바이너리 데이터를 전달하는 데 사용됩니다.
     * 
     * @param bytes : 바이너리
     * @return 16진수 문자열
     */
    public static String hexString(byte[] bytes) {
        StringBuffer results = new StringBuffer("");
        
        for(int i=0; i<bytes.length; i++)
        {
            results.append(Integer.toString(((bytes[i] & 0xf0) >> 4), 16) );
            results.append(Integer.toString((bytes[i] & 0x0f), 16));
        }
                                
        return String.valueOf(results);
    }
    
    /**
     * 16진수 문자열을 다시 바이너리로 변환합니다.
     * 
     * @param hexString : 바이너리 정보가 담긴 16진수 문자열 (모든 문자열이 호환되지 않음 ! 16진수 문자열만 사용 가능)
     * @return 바이너리 원본
     */
    public static  byte[] hexBytes(String hexString) {
        byte[] results = new byte[hexString.length() / 2];
        for(int idx=0; idx<hexString.length(); idx+=2) {
            results[(int) idx / 2] = (byte) (( Character.digit(hexString.charAt(idx), 16) << 4 ) + Character.digit(hexString.charAt(idx + 1), 16));
        }
        
        return results;
    }
    
    /** 바이너리를 BASE64 문자열로 변환합니다. */
    public static String base64String(byte[] bytes) {
        try {
            // JDK 11 에서 javax.xml.bind 가 빠짐 (별도 라이브러리로 분리됨)
            // 시도는 하되, 없으면 apache common codec 액세스 시도
            Class<?> javaxXml = Class.forName("javax.xml.bind.DatatypeConverter");
            Method mthd = javaxXml.getMethod("printBase64Binary", byte[].class);
            Object res = mthd.invoke(null, bytes);
            return res.toString();
        } catch(Throwable t) {
            if((t instanceof ClassNotFoundException) || (t instanceof NoSuchMethodException)) {
                try {
                    Class<?> javaxXml = Class.forName("jakarta.xml.bind.DatatypeConverter");
                    Method mthd = javaxXml.getMethod("printBase64Binary", byte[].class);
                    Object res = mthd.invoke(null, bytes);
                    return res.toString();
                } catch(Throwable t2) {
                    if((t2 instanceof ClassNotFoundException) || (t2 instanceof NoSuchMethodException)) {
                        try {
                            Class<?> apacheCodec = Class.forName("org.apache.commons.codec.binary.Base64");
                            Method mthd = apacheCodec.getMethod("encodeBase64String", byte[].class);
                            Object res = mthd.invoke(null, bytes);
                            return res.toString();
                        } catch(Throwable t3) {
                            if(t instanceof ClassNotFoundException) {
                                throw new RuntimeException("Cannot run BASE64. Please add Apache Commons Codec, or JAXB Library.", t3);
                            }
                            throw new RuntimeException(t3.getMessage(), t3);
                        }
                    }
                    throw new RuntimeException(t2.getMessage(), t2);
                }
            } else {
                throw new RuntimeException(t.getMessage(), t);
            }
        }
    }
    
    /**
     * <p>텍스트를 암호화합니다.</p>
     * 
     * @param text : 대상이 되는 텍스트
     * @param key : 암호화에 쓰일 비밀번호
     * @param algorithm : 암호화 방법 (null 시 AES 사용)
     * @return 암호화된 텍스트
     */
    public static String encrypt(String text, String key, String algorithm)
    {
        try
        {
            String passwords = hash(key, null);
            String methods = algorithm;
            if(methods == null) methods = "AES";
            
            String paddings;
            int need_keySize = -1;
            boolean useIv = false;
            
            byte[] befores = text.trim().getBytes("UTF-8");
            byte[] keyByte = passwords.getBytes("UTF-8");
            
            if(methods.equalsIgnoreCase("DES"))
            {
                paddings = "DES/CBC/PKCS5Padding";
                need_keySize = 8;
                useIv = true;
            }
            else if(methods.equalsIgnoreCase("DESede"))
            {
                paddings = "TripleDES/ECB/PKCS5Padding";
                need_keySize = 24;
                useIv = true;
            }
            else if(methods.equalsIgnoreCase("AES"))
            {
                paddings = "AES";
                need_keySize = 16;
                useIv = false;
            }
            else return null;
            
            byte[] checkKeyByte = new byte[need_keySize];
            byte[] ivBytes = new byte[checkKeyByte.length];
            
            for(int i=0; i<checkKeyByte.length; i++)
            {
                if(i < keyByte.length)
                {
                    checkKeyByte[i] = keyByte[i];
                }
                else
                {
                    checkKeyByte[i] = 0;
                }
            }
            keyByte = checkKeyByte;
            
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, algorithm);
            IvParameterSpec ivSpec = null;
            if(useIv) ivSpec = new IvParameterSpec(ivBytes);
            
            Cipher cipher = null;
            byte[] outputs;
            
            try
            {
                cipher = Cipher.getInstance(paddings);
                if(useIv)
                {
                    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
                }
                else
                {
                    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                }
                
                outputs = new byte[cipher.getOutputSize(befores.length)];
                for(int i=0; i<outputs.length; i++)
                {
                    outputs[i] = 0;
                }
                int enc_len = cipher.update(befores, 0, befores.length, outputs, 0);
                enc_len = enc_len + cipher.doFinal(outputs, enc_len);
                
                return hexString(outputs);
            }
            catch(Throwable e)
            {
                throw new RuntimeException(e);
            }
        }
        catch(Throwable e)
        {
            
        }
        return null;
    }
    
    /**
     * <p>암호화된 텍스트를 복호화해 원래의 텍스트를 구합니다.</p>
     * 
     * @param text : 암호화된 텍스트
     * @param key : 암호화에 쓰인 비밀번호
     * @param algorithm : 암호화에 쓰인 암호화 방법 (null 시 AES 로 간주)
     * @return 원래의 텍스트
     */
    public static String decrypt(String text, String key, String algorithm)
    {
        try
        {
            String passwords = hash(key, null);
            String methods = algorithm;
            if(methods == null) methods = "AES";
            
            String paddings;
            int need_keySize = -1;
            boolean useIv = false;
            
            byte[] befores = hexBytes(text);
            byte[] keyByte = passwords.getBytes("UTF-8");
            
            if(methods.equalsIgnoreCase("DES"))
            {
                paddings = "DES/CBC/PKCS5Padding";
                need_keySize = 8;
                useIv = true;
            }
            else if(methods.equalsIgnoreCase("DESede"))
            {
                paddings = "TripleDES/ECB/PKCS5Padding";
                need_keySize = 168;
                useIv = true;
            }
            else if(methods.equalsIgnoreCase("AES"))
            {
                paddings = "AES";
                need_keySize = 16;
                useIv = false;
            }
            else return null;
            
            byte[] checkKeyByte = new byte[need_keySize];
            byte[] ivBytes = new byte[checkKeyByte.length];
            for(int i=0; i<checkKeyByte.length; i++)
            {
                if(i < keyByte.length)
                {
                    checkKeyByte[i] = keyByte[i];
                }
                else
                {
                    checkKeyByte[i] = 0;
                }
            }
            keyByte = checkKeyByte;
            
            
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, methods);
            IvParameterSpec ivSpec = null;
            if(useIv) ivSpec = new IvParameterSpec(ivBytes);
            
            Cipher cipher = null;
            
            
            try
            {
                cipher = Cipher.getInstance(paddings);
                if(useIv)
                {
                    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
                }
                else
                {
                    cipher.init(Cipher.DECRYPT_MODE, keySpec);
                }
                byte[] outputs = new byte[cipher.getOutputSize(befores.length)];
                for(int i=0; i<outputs.length; i++)
                {
                    outputs[i] = 0;
                }
                int enc_len = cipher.update(befores, 0, befores.length, outputs, 0);
                enc_len = enc_len + cipher.doFinal(outputs, enc_len);            
                
                return new String(outputs, "UTF-8").trim();
            }
            catch(Throwable e)
            {
                throw new RuntimeException(e);
            }
        }
        catch(Throwable e)
        {
            
        }
        return null;
    }
}
