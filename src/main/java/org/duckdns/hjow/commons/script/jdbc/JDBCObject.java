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
package org.duckdns.hjow.commons.script.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.duckdns.hjow.commons.script.ScriptObject;
import org.duckdns.hjow.commons.util.DataUtil;

public class JDBCObject extends ScriptObject {
    private static final long serialVersionUID = 396504181991617599L;
    private static final JDBCObject uniqueObject = new JDBCObject();
    
    protected transient List<JDBCConnection> connections;
    
    private JDBCObject() { 
        connections = new Vector<JDBCConnection>();
    }
    
    public static JDBCObject getInstance() {
        return uniqueObject;
    }
    @Override
    public String getPrefixName() {
        return "jdbc";
    }
    
    @Override
    public void releaseResource() {
        super.releaseResource();
        closeAllConnections();
    }
    
    @Override
    public String getInitScript(String accessKey) {
        StringBuilder initScript = new StringBuilder("");
        
        initScript = initScript.append("function jdbc_connect(url, user, pw, className) {                                       ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".connect(url, user, pw, className); ").append("\n");
        initScript = initScript.append("};                                                                                      ").append("\n");
        initScript = initScript.append("function jdbc_closeAllConnections() {                                                   ").append("\n");
        initScript = initScript.append("    return " + getPrefixName() + "_" + accessKey + ".closeAllConnections();             ").append("\n");
        initScript = initScript.append("};                                                                                      ").append("\n");
        
        return initScript.toString();
    }
    
    @SuppressWarnings("unchecked")
    public JDBCConnection connect(Object jdbcUrl, Object user, Object password, Object className) throws SQLException {
        if(DataUtil.isEmpty(jdbcUrl)  || String.valueOf(jdbcUrl).equals("undefined"))  throw new NullPointerException("Need JDBC URL");
        if(DataUtil.isEmpty(user)     || String.valueOf(user).equals("undefined"))     throw new NullPointerException("Need username");
        if(DataUtil.isEmpty(password) || String.valueOf(password).equals("undefined")) throw new NullPointerException("Need password");
        
        if(! (DataUtil.isEmpty(className)  || String.valueOf(className).equals("undefined"))) {
            Class<? extends Driver> classObj = null;
            try {
                classObj = (Class<? extends Driver>) Class.forName(String.valueOf(className));
            } catch(Throwable t) {}
            
            if(classObj == null) throw new NullPointerException("Cannot found driver class.");
        }
        
        Connection conn = DriverManager.getConnection(String.valueOf(jdbcUrl), String.valueOf(user), String.valueOf(password));
        JDBCConnection connObj = new JDBCConnection(conn);
        connections.add(connObj);
        return connObj;
    }
    
    public void closeAllConnections() {
        for(JDBCConnection c : connections) {
            c.releaseResource();
        }
        connections.clear();
    }
}
