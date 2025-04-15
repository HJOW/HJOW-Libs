/*
Copyright 2024 HJOW

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCRConnection extends org.duckdns.hjow.commons.script.jdbc.JDBCConnection {
    private static final long serialVersionUID = -297456075878898595L;

    public JDBCRConnection(Connection conn) {
        super(conn);
    }
    
    public Connection getRaw() {
        return conn;
    }
    
    public PreparedStatement prepareRawStatement(String sql) {
        try {
            return getRaw().prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}