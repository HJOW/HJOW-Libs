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
import java.sql.DriverManager;

import org.duckdns.hjow.commons.script.jdbc.JDBCConnection;

/**
 * <p>JDBC 사용을 위한 메소드들이 있습니다.</p>
 * 
 * @author HJOW
 *
 */
public class JDBCUtil {
    public static JDBCConnection connect(String jdbcClass, String jdbcUrl, String jdbcId, String jdbcPw) {
        try {
            Class.forName(jdbcClass);
            Connection rawOne = DriverManager.getConnection(jdbcUrl, jdbcId, jdbcPw);
            return new JDBCRConnection(rawOne);
        } catch(Throwable t) {
            throw new RuntimeException(t.getMessage(), t);
        }
    }
}