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
package hjow.common.script.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hjow.common.core.Core;
import hjow.common.data.Binary;
import hjow.common.script.ScriptObject;
import hjow.common.script.event.RowSelectListener;
import hjow.common.script.event.RowSelectedEvent;
import hjow.common.util.ClassUtil;

/** 스크립트 내에서 JDBC 액세스를 가능하도록 해주는 인스턴스의 설계도입니다. 접속할 때마다 새 인스턴스가 생성되므로 Prefix, InitScript 는 사용하지 않습니다. */
public class JDBCConnection extends ScriptObject {
    private static final long serialVersionUID = 6969765981092212331L;
    protected Connection conn;
    protected List<RowSelectListener> listeners;
    
    public JDBCConnection(Connection conn) {
        this.conn = conn;
        this.listeners = new ArrayList<RowSelectListener>();
    }
    
    @Override
    public void releaseResource() {
        super.releaseResource();
        removeSelectionListeners();
        close();
    }
    
    public void addRowSelectListener(RowSelectListener handler) {
        this.listeners.add(handler);
    }
    
    public void removeSelectionListeners() {
        this.listeners.clear();
    }
    
    public void removeRowSelectionListener(RowSelectListener handler) {
        this.listeners.remove(handler);
    }
    
    /** 접속을 해제합니다. 트랜잭션 롤백 메시지를 자동으로 보냅니다. */
    public void close() {
        boolean isClosedOpt = false;
        try { isClosedOpt = conn.isClosed(); } catch(Throwable ignores) {}
        if(isClosedOpt) {
            ClassUtil.closeAll(conn);
        } else {
            try { 
                conn.rollback();
            } catch(Throwable t) {
                Core.logError(t);
            }
            ClassUtil.closeAll(conn);
        }
        conn = null;
    }
    
    @Override
    public String getPrefixName() {
        return "jdbc_connection";
    }

    @Override
    public String getInitScript(String accessKey) {
        return null;
    }

    /** 접속 끊김 여부를 반환합니다. */
    public boolean isClosed() {
        if(conn == null) return true;
        
        boolean isClosedOpt = false;
        try { isClosedOpt = conn.isClosed(); } catch(Throwable ignores) {}
        return isClosedOpt;
    }
    
    /** 트랜잭션 커밋 메시지를 보냅니다. */
    public void commit() {
        try { conn.commit(); } catch(Throwable t) { throw new JDBCException(t); }
    }
    
    /** 트랜잭션 롤백 메시지를 보냅니다. */
    public void rollback() {
        try { conn.rollback(); } catch(Throwable t) { throw new JDBCException(t); }
    }
    
    /** SELECT 구문을 실행합니다. 조회 결과가 테이블 형태의 객체로 반환됩니다. 매개변수를 넣어야 할 경우 SQL문 안에 물음표(?) 기호를 매개변수 자리마다 넣고 params 리스트에 순서대로 매개변수 값들을 넣습니다. */
    public JDBCTable select(Object sql, List<Object> params) {
        JDBCTable table = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(String.valueOf(sql));
            
            int paramIndex = 0;
            for(Object p : params) {
                setParameterObject(pst, paramIndex, p);
                paramIndex++;
            }
            
            rs = pst.executeQuery();
            table = new DefaultJdbcTable(this, rs, listeners, String.valueOf(sql));
        } catch(Throwable t) {
            throw new JDBCException(t);
        } finally {
            try { rs.close();  } catch(Throwable ignores) {}
            try { pst.close(); } catch(Throwable ignores) {}
        }
        
        return table;
    }
    
    /** SELECT 구문을 실행합니다. 조회 결과를 반환하지 않으나 RowSelectedListener 에는 이벤트를 발생시킵니다. 매개변수를 넣어야 할 경우 SQL문 안에 물음표(?) 기호를 매개변수 자리마다 넣고 params 리스트에 순서대로 매개변수 값들을 넣습니다. */
    public void selectNoReturn(Object sql, List<Object> params) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(String.valueOf(sql));
            
            int paramIndex = 0;
            for(Object p : params) {
                setParameterObject(pst, paramIndex, p);
                paramIndex++;
            }
            
            rs = pst.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            
            List<String>  columnNames = new ArrayList<String>();
            List<Integer> columnTypes = new ArrayList<Integer>();
            
            int colCnt = metaData.getColumnCount();
            for(int idx=0; idx<colCnt; idx++) {
                String  colName = metaData.getColumnName(idx + 1);
                Integer colType = metaData.getColumnType(idx + 1);
                
                columnNames.add(colName);
                columnTypes.add(colType);
            }
            
            int rowIdx = 0;
            while(rs.next()) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                
                for(int idx=0; idx<columnNames.size(); idx++) {
                    int    type = columnTypes.get(idx).intValue();
                    String name = columnNames.get(idx);
                    
                    Serializable val = DefaultJdbcTable.getObjectFrom(rs, type, name);
                    
                    if(val != null && (val instanceof byte[])) val = new Binary((byte[])val);
                    rowMap.put(name, val);
                }
                
                for(RowSelectListener l : listeners) {
                    RowSelectedEvent e = new RowSelectedEvent(metaData, rowMap, rowIdx, String.valueOf(sql));
                    l.rowSelected(e);
                }
                
                rowIdx++;
            }
        } catch(Throwable t) {
            throw new JDBCException(t);
        } finally {
            try { rs.close();  } catch(Throwable ignores) {}
            try { pst.close(); } catch(Throwable ignores) {}
        }
    }
    
    /** SQL 구문을 실행합니다. 데이터를 조회하는 데 사용할 수 없지만, INSERT / UPDATE / DELETE 등의 구문 사용 시 변경된 행 수를 반환합니다. 매개변수를 넣어야 할 경우 SQL문 안에 물음표(?) 기호를 매개변수 자리마다 넣고 params 리스트에 순서대로 매개변수 값들을 넣습니다. */
    public int update(Object sql, List<Object> params) {
        PreparedStatement pst = null;
        int resultCnt = -1;
        try {
            pst = conn.prepareStatement(String.valueOf(sql));
            
            int paramIndex = 0;
            for(Object p : params) {
                setParameterObject(pst, paramIndex, p);
                paramIndex++;
            }
            
            resultCnt = pst.executeUpdate();
        } catch(Throwable t) {
            throw new JDBCException(t);
        } finally {
            try { pst.close(); } catch(Throwable ignores) {}
        }
        return resultCnt;
    }
    
    /** update 메소드를 대신 사용해 주세요. 기능이 동일합니다. */
    @Deprecated
    public int insert(Object sql, List<Object> params) {
        return update(sql, params);
    }
    
    /** update 메소드를 대신 사용해 주세요. 기능이 동일합니다. */
    @Deprecated
    public int delete(Object sql, List<Object> params) {
        return update(sql, params);
    }
    
    /** 매개변수 타입에 따라 PreparedStatement 의 메소드를 다르게 호출해 줍니다. */
    public static void setParameterObject(PreparedStatement pst, int parameterIndex, Object p) {
        int paramIndex = parameterIndex + 1;
        try {
            if(p == null) {
                pst.setNull(paramIndex, Types.VARCHAR);
            } else if(p instanceof Integer || p instanceof Long || p instanceof BigInteger) {
                pst.setLong(paramIndex, ((Number) p).longValue());
            } else if(p instanceof BigDecimal) {
                pst.setBigDecimal(paramIndex, (BigDecimal) p);
            } else if(p instanceof Float || p instanceof Double) {
                pst.setDouble(paramIndex, ((Number) p).doubleValue());
            } else if(p instanceof Boolean) {
                pst.setBoolean(paramIndex, (Boolean) p);
            } else if(p instanceof Binary) {
                ByteArrayInputStream fakeStream = new ByteArrayInputStream(((Binary) p).toByteArray());
                pst.setBlob(paramIndex, fakeStream);
            } else if(p instanceof byte[]) {
                ByteArrayInputStream fakeStream = new ByteArrayInputStream((byte[]) p);
                pst.setBlob(paramIndex, fakeStream);
            } else if(p instanceof ByteArrayInputStream) {
                pst.setBlob(paramIndex, (InputStream) p);
            } else if(p instanceof java.util.Date) {
                java.sql.Date sqlDate = new java.sql.Date(((java.util.Date) p).getTime());
                pst.setDate(paramIndex, sqlDate); 
            } else {
                pst.setString(paramIndex, String.valueOf(p));
            }
        } catch(Throwable t) {
            throw new JDBCException(t);
        }
    }
}
