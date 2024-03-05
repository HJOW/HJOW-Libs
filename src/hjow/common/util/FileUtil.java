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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import hjow.common.core.Core;

/**
 * 파일 사용 관련 유틸리티
 */
public class FileUtil {
    
    /** 디렉토리 안에 있는 파일을 File 객체 형태로 반환합니다. */
    public static File fileIn(File directory, String fileName) {
        return fileIn(directory, fileName, false);
    }
    /** 디렉토리 안에 있는 파일을 File 객체 형태로 반환합니다. forceMake 를 true 지정 시 파일이 존재하지 않으면 이름을 만들어 반환합니다. */
    public static File fileIn(File directory, String fileName, boolean forceMake) {
        if(! directory.exists()) return null;
        
        File[] insides = directory.listFiles();
        for(File f : insides) {
            String currentName = f.getName();
            if(fileName.equals(currentName)) return f;
        }
        if(! forceMake) return null;
        
        String dirPath = directory.getAbsolutePath();
        if(! (dirPath.endsWith("/") || dirPath.endsWith(File.separator))) {
            dirPath += File.separator;
        }
        
        return new File(dirPath + fileName);
    }
    /** Properties 객체를 파일로부터 불러옵니다. */
    public static Properties loadProperties(File configFile, boolean makeIfNotExist) throws IOException {
        return loadProperties(configFile, makeIfNotExist, null);
    }
    
    /** Properties 객체를 파일로부터 불러옵니다. makeIfNotExist 를 true 로 설정 시, 기존 파일이 존재하지 않을 경우 파일을 새로 생성한 후 다시 불러옵니다. 이 때 defaultValues 가 지정되어 있으면 생성된 파일에 기본값을 부여합니다. */
    public static Properties loadProperties(File configFile, boolean makeIfNotExist, Properties defaultValues) throws IOException {
        FileInputStream inputStream = null;
        try {
            if(configFile == null) return null;
            String fileAbsPath = configFile.getAbsolutePath();
            fileAbsPath = fileAbsPath.toLowerCase();
            
            if(makeIfNotExist && (! configFile.exists())) {
                Properties newProp = new Properties();
                if(defaultValues != null) newProp.putAll(defaultValues);
                if(newProp.isEmpty()) newProp.setProperty("SAMPLE_KEY", "SAMPLE_VALUE");
                saveProperties(configFile, newProp);
            }
            
            inputStream = new FileInputStream(configFile);
            Properties prop = new Properties();
            if(defaultValues != null) prop.putAll(defaultValues);
            
            if(fileAbsPath.endsWith(".xml"))
                prop.loadFromXML(inputStream);
            else
                prop.load(inputStream);
            
            inputStream.close();
            inputStream = null;
            return prop;
        } catch(IOException t) {
            throw t;
        } finally {
            if(inputStream != null) {
                try { inputStream.close(); } catch(Throwable ignores) {}
            }
        }
    }
    
    /** Properties 를 파일에 기록합니다. 이미 파일이 있으면 덮어 씁니다. */
    public static void saveProperties(File configFile, Properties prop) throws IOException {
    	FileOutputStream outputStream = null;
    	if(prop == null) throw new NullPointerException();
    	if(configFile == null) throw new NullPointerException();
    	
        String fileAbsPath = configFile.getAbsolutePath();
        fileAbsPath = fileAbsPath.toLowerCase();
        
		try {
			outputStream = new FileOutputStream(configFile);
			
			if(fileAbsPath.endsWith(".xml"))
				prop.storeToXML(outputStream, "");
            else
            	prop.store(outputStream, "");
			
			outputStream.close();
			outputStream = null;
		} catch(IOException t) {
			throw t;
		} finally {
			if(outputStream != null) {
				try { outputStream.close(); } catch(Throwable t) {}
			}
		}
    }
    
    /**
     * 파일로부터 바이너리를 읽습니다.
     * 
     * @param targetFile 파일 객체
     * @return byte 배열
     * @throws IOException
     */
    public static byte[] readBytes(File targetFile) throws IOException {
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            inputStream = new FileInputStream(targetFile);
            byte[] buffers = new byte[1024];
            
            int readEnds = buffers.length;
            while(true) {
                int reads = inputStream.read(buffers, 0, readEnds);
                if(reads < 0) break;
                
                outputStream.write(buffers, 0, reads);
                if(reads < readEnds) readEnds = reads;
            }
            
        } catch (IOException e) {
            throw e;
        } finally {
            try { inputStream.close();  } catch(Throwable e) { }
            try { outputStream.close(); } catch(Throwable e) { }
        }
        return outputStream.toByteArray();
    }
    
    /**
     * 바이너리를 파일로 씁니다. 파일이 이미 존재하면 덮어 씁니다.
     * 
     * @param targetFile 파일 객체
     * @param byteArray  바이너리 데이터
     * @throws IOException
     */
    public static void writeBytes(File targetFile, byte[] byteArray) throws IOException {
        OutputStream outputStream = null;
        
        try {
            outputStream = new FileOutputStream(targetFile);
            outputStream.write(byteArray);
            try { outputStream.close(); } catch(Throwable e) { }
        } catch (IOException e) {
            throw e;
        } finally {
            try { outputStream.close(); } catch(Throwable e) { }
        }
    }
    
    /**
     * 텍스트를 읽습니다.
     * 
     * @param targetFile : 파일 객체
     * @param charSet    : 캐릭터셋
     * @return 읽은 텍스트
     * @throws IOException
     */
    public static String readString(File targetFile, String charSet) throws IOException {
    	if(DataUtil.isEmpty(charSet)) charSet = "UTF-8";
    	return new String(readBytes(targetFile), charSet);
    }
    
    /**
     * 텍스트를 읽습니다.
     * 
     * @param targetFile : 파일 객체
     * @return 읽은 텍스트
     * @throws IOException
     */
    public static String readString(File targetFile) throws IOException {
    	return readString(targetFile, null);
    }
    
    /**
     * 텍스트를 파일에 기록합니다. 파일이 이미 존재하면 덮어 씁니다.
     * 
     * @param targetFile : 파일 객체
     * @param charSet    : 캐릭터셋
     * @param text 기록할 내용
     * @throws IOException
     */
    public static void writeString(File targetFile, String charSet, String text) throws IOException {
    	if(DataUtil.isEmpty(charSet)) charSet = "UTF-8";
    	writeBytes(targetFile, text.getBytes(charSet));
    }
    
    /**
     * 텍스트를 파일에 기록합니다. 파일이 이미 존재하면 덮어 씁니다.
     * 
     * @param targetFile : 파일 객체
     * @param text 기록할 내용
     * @throws IOException
     */
    public static void writeString(File targetFile, String text) throws IOException {
    	writeString(targetFile, null, text);
    }
    
    /** 파일을 삭제합니다. 디렉토리인 경우 그 안의 디렉토리까지 비우기를 시도합니다. */
    public static void delete(File file) {
        delete(file, 0);
    }
    private static void delete(File file, int recursiveDepth) {
        if(file == null) return;
        if(! file.exists()) return;
        
        if(file.isDirectory()) {
            File[] insides = file.listFiles();
            for(File f : insides) {
                if(recursiveDepth >= 100) throw new RuntimeException("Directory depths are deep so much !");
                delete(f, recursiveDepth + 1);
            }
        }
        
        Core.log("Deleting file : " + file.getAbsolutePath());
        file.delete();
    }
    
    /** 스트림을 순서대로 닫습니다. 하나 이상 예외가 발생하더라도 일단 모두 닫기를 시도합니다. */
    public static void closeAll(java.io.Closeable ... closeables) {
    	if(closeables == null) return;
    	for(java.io.Closeable c : closeables) {
    		ClassUtil.closeAll(c);
    	}
    }
    
    /** 스트림을 순서대로 닫습니다. 하나 이상 예외가 발생하더라도 일단 모두 닫기를 시도합니다. */
    public static void closeAll(List<java.io.Closeable> closeables) {
    	if(closeables == null) return;
    	ClassUtil.closeAll(closeables);
    }
}
