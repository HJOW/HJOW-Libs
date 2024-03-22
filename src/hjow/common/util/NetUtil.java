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
package hjow.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import hjow.common.core.Core;
import hjow.common.stream.ChainInputStream;

/**
 * <p>네트워킹 관련 정적 메소드들이 있습니다.</p>
 * 
 * @author HJOW
 * @see HHttpClient
 *
 */
public class NetUtil
{    
    /**
     * <p>POST 방식으로 요청을 보냅니다. 그에 대한 응답을 텍스트로 반환합니다.</p>
     * 
     * @param url : URL
     * @param parameters : 매개 변수들
     * @param contentType : HTTP 컨텐츠 타입 옵션, null 시 application/x-www-form-urlencoded 가 사용됨
     * @param parameterEncoding : 매개 변수 인코딩 방식, null 시 UTF-8 사용
     * @return 서버로부터 받은 텍스트
     * @throws Throwable : 네트워크 문제, URL 문제, 지원되지 않는 인코딩 등
     */
    public static String sendPost(URL url, Map<String, Object> parameters, String contentType, String parameterEncoding) throws Throwable
    {
        InputStream gets = sendPostStream(url, parameters, contentType, parameterEncoding);
        StringBuffer results = new StringBuffer("");
        
        ChainInputStream chainStream = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        
        String reads = "";
        
        try
        {
            chainStream = new ChainInputStream(gets);
            if(DataUtil.isNotEmpty(parameterEncoding)) reader = new InputStreamReader(chainStream.getInputStream(), parameterEncoding);
            else  reader = new InputStreamReader(chainStream.getInputStream(), "UTF-8");
            bufferedReader = new BufferedReader(reader);
            
            while(true)
            {
                reads = bufferedReader.readLine();
                if(reads == null) break;
                
                if(Core.checkInterrupt(NetUtil.class, "On sendPost")) {
                	throw new InterruptedException("On sendPost");
                }
                
                results = results.append(reads + "\n");
            }
            
            return results.toString();
        }
        catch(Throwable e)
        {
            throw e;
        }
        finally
        {
            try
            {
                bufferedReader.close();
            }
            catch(Throwable e)
            {
                
            }
            try
            {
                reader.close();
            }
            catch(Throwable e)
            {
                
            }
            try
            {
                chainStream.close();
            }
            catch(Throwable e)
            {
                
            }
            try
            {
                gets.close();
            }
            catch(Throwable e)
            {
                
            }
        }
    }
    /**
     * <p>POST 방식으로 요청을 보냅니다.</p>
     * 
     * @param url : URL
     * @param parameters : 매개 변수들
     * @param contentType : HTTP 컨텐츠 타입 옵션, null 시 application/x-www-form-urlencoded 가 사용됨
     * @param parameterEncoding : 매개 변수 인코딩 방식, null 시 UTF-8 사용
     * @return 서버로부터의 응답을 받는 입력 스트림
     * @throws IOException
     */
    public static InputStream sendPostStream(URL url, Map<String, Object> parameters, String contentType, String parameterEncoding) throws IOException
    {
        String parameterTexts = "";
        Set<String> parameterKeySet = parameters.keySet();
        String stringed = null;
        for(String s : parameterKeySet)
        {
            stringed = String.valueOf(parameters.get(s));
            if(DataUtil.isNotEmpty(parameterEncoding)) parameterTexts = parameterTexts + "&" + s + "=" + URLEncoder.encode(stringed, parameterEncoding);
            else parameterTexts = parameterTexts + "&" + s + "=" + URLEncoder.encode(stringed, "UTF-8");
        }
        parameterTexts = parameterTexts.trim();
        if(parameterTexts.startsWith("&")) parameterTexts = parameterTexts.substring(1);
        
        if(DataUtil.isNotEmpty(parameterEncoding)) return sendPostStream(url, parameterTexts.getBytes(parameterEncoding), contentType);
        else return sendPostStream(url, parameterTexts.getBytes("UTF-8"), contentType);
    }
    
    /**
     * <p>POST 방식으로 요청을 보냅니다.</p>
     * 
     * @param url : URL
     * @param parameters : 매개 변수 이진 데이터
     * @param contentType : HTTP 컨텐츠 타입 옵션, null 시 application/x-www-form-urlencoded 가 사용됨
     * @return 서버로부터의 응답을 받는 입력 스트림
     * @throws IOException
     */
    public static InputStream sendPostStream(URL url, byte[] parameters, String contentType) throws IOException
    {
        HttpURLConnection connection = null;
        OutputStream out = null;
        
        try
        {            
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            if(contentType != null) connection.setRequestProperty("Content-Type", contentType);
            else connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            out = connection.getOutputStream();
            
            out.write(parameters);
            
            out.flush();
            
            return connection.getInputStream();
        }
        catch(IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                out.close();
            }
            catch(Throwable e)
            {
                
            }
        }        
    }    
    
    /**
     * POST 방식으로 요청을 보내고 byte 배열을 받습니다.
     * 
     * @param url : URL
     * @param parameters : 매개 변수 이진 데이터
     * @param contentType : HTTP 컨텐츠 타입 옵션, null 시 application/x-www-form-urlencoded 가 사용됨
     * @return 서버로부터의 응답을 받은 byte 배열
     * @throws IOException
     */
    public static byte[] sendPostBytes(URL url, byte[] parameters, String contentType) throws IOException {
        InputStream stream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            stream = sendPostStream(url, parameters, contentType);
            byte[] buffers = new byte[1024];
            
            int readEnds = buffers.length;
            while(true) {
                int reads = stream.read(buffers, 0, readEnds);
                if(reads < 0) break;
                
                out.write(buffers, 0, reads);
                if(reads < readEnds) readEnds = reads;
            }
            
        } catch (IOException e) {
            throw e;
        } finally {
            try { stream.close(); } catch(Throwable e) { }
            try { out.close();    } catch(Throwable e) { }
        }
        return out.toByteArray();
    }
}
