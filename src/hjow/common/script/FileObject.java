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
package hjow.common.script;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import hjow.common.core.Core;
import hjow.common.data.Binary;
import hjow.common.json.JsonObject;
import hjow.common.util.DataUtil;
import hjow.common.util.FileUtil;

public class FileObject extends ScriptObject {
	private static final long serialVersionUID = 5797355511846866518L;
	private static final FileObject uniqueObject = new FileObject();
    private FileObject() { }
    public static FileObject getInstance() {
        return uniqueObject;
    }
    @Override
    public String getPrefixName() {
        return "file";
    }
	@Override
	public String getInitScript(String accessKey) {
		StringBuilder initScript = new StringBuilder("");
		
		initScript = initScript.append("function file_exists(f) {                                                                     ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".exists(f);                               ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_isDirectory(f) {                                                                ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".isDirectory(f);                          ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_listIn(f) {                                                                     ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".listFiles(f);                            ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_makeDirectory(f) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".makeDirectory(f);                        ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_mkdir(f) {                                                                      ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".makeDirectory(f);                        ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_delete(f) {                                                                     ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".deleteFile(f);                           ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
		
        initScript = initScript.append("function file_readBytes(f) {                                                                  ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".readBytes(f);                            ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_writeBytes(f, b) {                                                              ").append("\n");
        initScript = initScript.append("    " + getPrefixName() + "_" + accessKey + ".writeBytes(f, b);                               ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_readString(f, c) {                                                              ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".readString(f, c);                        ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_writeString(f, b, c) {                                                          ").append("\n");
        initScript = initScript.append("    " + getPrefixName() + "_" + accessKey + ".writeString(f, b, c);                           ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_readProp(f) {                                                                   ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".readMap(f);                              ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
        initScript = initScript.append("function file_writeProp(f, b) {                                                               ").append("\n");
        initScript = initScript.append("    " + getPrefixName() + "_" + accessKey + ".writeMap(f, b);                                 ").append("\n");
        initScript = initScript.append("};                                                                                            ").append("\n");
		return initScript.toString();
	}
	
	protected File convertFile(Object filePath) {
		if(filePath instanceof File) return (File) filePath;
		return new File(String.valueOf(filePath));
	}
	protected Binary convertBinary(Object binaryObj) {
		if(binaryObj instanceof Binary) return (Binary) binaryObj;
		if(binaryObj instanceof ByteArrayOutputStream) return new Binary(((ByteArrayOutputStream) binaryObj).toByteArray());
		if(binaryObj instanceof byte[]) return new Binary((byte[]) binaryObj);
		throw new RuntimeException("Cannot convert into binary data.");
	}
	
	public boolean exists(Object filePath) {
		File file = convertFile(filePath);
		return file.exists();
	}
	
	public boolean isDirectory(Object filePath) {
		File file = convertFile(filePath);
		return file.isDirectory();
	}
	
	public List<String> listFiles(Object filePath) {
		File file = convertFile(filePath);
		
		File[] lists = file.listFiles();
		List<String> results = new ArrayList<String>();
		
		for(File f : lists) {
			results.add(f.getName());
		}
		
		return results;
	}
	
	public boolean makeDirectory(Object filePath) {
		File file = convertFile(filePath);
		if(! Core.canAccessWriteOnScript(file)) throw new NoPrivilegeException("Cannot access those path for write.");
		return file.mkdirs();
	}
	
	public void deleteFile(Object filePath) {
		File file = convertFile(filePath);
		if(! Core.canAccessWriteOnScript(file)) throw new NoPrivilegeException("Cannot access those path for write.");
		
		FileUtil.delete(file);
	}
	
	public Binary readBytes(Object filePath) throws IOException {
		File file = convertFile(filePath);
		if(! Core.canAccessReadOnScript(file)) throw new NoPrivilegeException("Cannot access those path for read.");
		return new Binary(FileUtil.readBytes(file));
	}
    
    public void writeBytes(Object filePath, Object binaries) throws IOException {
    	File file = convertFile(filePath);
		if(! Core.canAccessWriteOnScript(file)) throw new NoPrivilegeException("Cannot access those path for write.");
		FileUtil.writeBytes(file, convertBinary(binaries).toByteArray());
    }
    
    public String readString(Object filePath, Object charset) throws IOException {
    	File file = convertFile(filePath);
		if(! Core.canAccessReadOnScript(file)) throw new NoPrivilegeException("Cannot access those path for read.");
		if(DataUtil.isEmpty(charset)) charset = "UTF-8";
		return FileUtil.readString(file, String.valueOf(charset));
    }
    
    public void writeString(Object filePath, Object charset, Object text) throws IOException {
    	File file = convertFile(filePath);
		if(! Core.canAccessWriteOnScript(file)) throw new NoPrivilegeException("Cannot access those path for write.");
		if(DataUtil.isEmpty(charset)) charset = "UTF-8";
    	FileUtil.writeString(file, String.valueOf(charset), String.valueOf(text));
    }
    
    public Map<Object, Object> readProp(Object filePath) throws IOException {
    	File file = convertFile(filePath);
		if(! Core.canAccessReadOnScript(file)) throw new NoPrivilegeException("Cannot access those path for read.");
    	Properties prop = FileUtil.loadProperties(file, false);
    	if(prop == null) return null;
    	
    	Map<Object, Object> newMap = new HashMap<Object, Object>();
    	Set<String> keys = prop.stringPropertyNames();
    	for(String k : keys) {
    		newMap.put(k, prop.getProperty(k));
    	}
    	return newMap;
    }
    
    public void writeProp(Object filePath, Object mapObj) throws IOException {
    	File file = convertFile(filePath);
		if(! Core.canAccessWriteOnScript(file)) throw new NoPrivilegeException("Cannot access those path for write.");
		
		if(mapObj == null) throw new NullPointerException();
		
		Properties prop = new Properties();
		if(mapObj instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) mapObj;
			
			Set<?> keys = map.keySet();
			for(Object k : keys) {
				prop.setProperty(String.valueOf(k), String.valueOf(map.get(k)));
			}
			
		} else if(mapObj instanceof JsonObject) {
			JsonObject obj = (JsonObject) mapObj;
			
			Set<String> keys = obj.keySet();
			for(String k : keys) {
				prop.setProperty(k, String.valueOf(obj.get(k)));
			}
		}
		
		FileUtil.saveProperties(file, prop);
    }
}
class NoPrivilegeException extends RuntimeException {
	private static final long serialVersionUID = 303221399540514336L;
	public NoPrivilegeException() {
		
	}
	public NoPrivilegeException(String msg) {
		super(msg);
	}
	
}