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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import hjow.common.data.Binary;
import hjow.common.data.table.Column;
import hjow.common.data.table.SimpleTable;
import hjow.common.json.JsonArray;
import hjow.common.script.event.RowSelectListener;
import hjow.common.script.event.RowSelectedEvent;

public class DefaultJdbcTable extends SimpleTable implements JDBCTable {
    private static final long serialVersionUID = 2369303907184689149L;
    
    public DefaultJdbcTable() {
    	super();
    }
    
    public DefaultJdbcTable(JDBCConnection conn, ResultSet resultSet, List<RowSelectListener> listeners, String sql) {
        this();
        init(conn, resultSet, listeners, sql);
    }
    
    @Override
    public void init(JDBCConnection conn, ResultSet resultSet, List<RowSelectListener> listeners, String sql) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            
            int colCnt = metaData.getColumnCount();
            for(int idx=0; idx<colCnt; idx++) {
                String  colName = metaData.getColumnName(idx + 1);
                Integer colType = metaData.getColumnType(idx + 1);
                
                columns.add(new Column(colName, colType));
            }
            
            int rowIdx = 0;
            while(resultSet.next()) {
                List<Serializable> rowOne = new Vector<Serializable>();
                
                Map<String, Object> rowMap = new HashMap<String, Object>();
                // if(listeners != null && (! listeners.isEmpty())) rowMap = new HashMap<String, Object>();
                
                for(int idx=0; idx<columns.size(); idx++) {
                    int    type = columns.get(idx).getType();
                    String name = columns.get(idx).getName();
                    
                    Serializable val = getObjectFrom(resultSet, type, name);
                    
                    if(val != null && (val instanceof byte[])) val = new Binary((byte[])val);
                    
                    if(val == null) rowOne.add("");
                    else rowOne.add(val);
                    
                    rowMap.put(name, val);
                }
                
                rowStorage.add(rowMap);
                
                if(rowMap != null) {
                    for(RowSelectListener l : listeners) {
                        RowSelectedEvent e = new RowSelectedEvent(conn, rowMap, rowIdx, sql);
                        l.rowSelected(e);
                    }
                }
                
                rowIdx++;
            }
            
        } catch(Throwable t) {
            throw new JDBCException(t);
        }
    }
    
    @SuppressWarnings("unlikely-arg-type")
	@Override
    public Object getValue(Object rowIndex, Object colInfo) {
        if(colInfo instanceof Number) {
            return getRowAsMap(new BigDecimal(String.valueOf(rowIndex)).intValue()).get(((Number) colInfo).intValue());
        }
        
        String colName = String.valueOf(colInfo);
        
        for(int idx=0; idx<columns.size(); idx++) {
            if(colName.equals(columns.get(idx).getName())) {
                return getValue(rowIndex, idx);
            }
        }
        
        throw new JDBCException(new RuntimeException("Cannot found the column."));
    }
    
    @Override
    public Map<String, Object> getRowAsMap(Object rowIndex) {
        return getRowMap(new BigDecimal(String.valueOf(rowIndex)).intValue());
    }
    
    @Override
    public String toJSON() {
        JsonArray arr = new JsonArray();
        int rowIdx = getRowCount();
        for(int idx=0; idx<rowIdx; idx++) {
            arr.add(getRowMap(idx));
        }
        return arr.toJSON();
    }
    
    @Override
    public String toString() {
        return toJSON();
    }
    
    /** ResultSet 에서 컬럼 타입에 맞게 데이터를 뽑아냅니다. */
    public static Serializable getObjectFrom(ResultSet resultSet, int colType, String colName) throws SQLException {
        switch(colType) {
        case Types.NULL:
            return null;
        case Types.CHAR:
            return resultSet.getString(colName);
        case Types.VARCHAR:
            return resultSet.getString(colName);
        case Types.NVARCHAR:
            return resultSet.getNString(colName);
        case Types.LONGVARCHAR:
            return resultSet.getString(colName);
        case Types.LONGNVARCHAR:
            return resultSet.getNString(colName);
        case Types.NUMERIC:
            return resultSet.getBigDecimal(colName);
        case Types.INTEGER:
            return resultSet.getInt(colName);
        case Types.BIGINT:
            return resultSet.getLong(colName);
        case Types.FLOAT:
            return resultSet.getFloat(colName);
        case Types.DOUBLE:
            return resultSet.getDouble(colName);
        case Types.DECIMAL:
            return resultSet.getBigDecimal(colName);
        case Types.BOOLEAN:
            return resultSet.getBoolean(colName);
        case Types.CLOB:
            return fromClob(resultSet.getClob(colName));
        case Types.NCLOB:
            return fromClob(resultSet.getNClob(colName));
        case Types.BLOB:
            return fromBlob(resultSet.getBlob(colName));
        case Types.DATE:
            return resultSet.getDate(colName);
        case Types.TIMESTAMP:
            return resultSet.getTimestamp(colName);
        case Types.BINARY:
            return fromBinary(resultSet.getBinaryStream(colName));
        case Types.JAVA_OBJECT:
            return (Serializable) resultSet.getObject(colName);
        }
        
        throw new JDBCException(new RuntimeException("Unsupported data type"));
    }
    
    /** CLOB 에서 문자열을 뽑아냅니다. CLOB 객체는 닫히게 됩니다. */
    public static String fromClob(Clob clob) {
        Reader reader = null;
        BufferedReader buffered = null;
        StringBuilder results = new StringBuilder("");
        try {
            reader = clob.getCharacterStream();
            buffered = new BufferedReader(reader);
            
            boolean isFirst = true;
            while(true) {
                String line = buffered.readLine();
                if(line == null) break;
                
                if(! isFirst) results = results.append("\n");
                
                results = results.append(line);
                isFirst = false;
            }
            
        } catch(Throwable t) {
            throw new JDBCException(t);
        } finally {
            try { buffered.close(); } catch(Throwable ignores) {}
            try { reader.close();   } catch(Throwable ignores) {}
            try { clob.free();      } catch(Throwable ignores) {}
        }
        
        return results.toString();
    }
    
    /** CLOB 에서 문자열을 뽑아냅니다. CLOB 객체는 닫히게 됩니다. */
    public static String fromClob(NClob clob) {
        Reader reader = null;
        BufferedReader buffered = null;
        StringBuilder results = new StringBuilder("");
        try {
            reader = clob.getCharacterStream();
            buffered = new BufferedReader(reader);
            
            boolean isFirst = true;
            while(true) {
                String line = buffered.readLine();
                if(line == null) break;
                
                if(! isFirst) results = results.append("\n");
                
                results = results.append(line);
                isFirst = false;
            }
            
        } catch(Throwable t) {
            throw new JDBCException(t);
        } finally {
            try { buffered.close(); } catch(Throwable ignores) {}
            try { reader.close();   } catch(Throwable ignores) {}
            try { clob.free();      } catch(Throwable ignores) {}
        }
        
        return results.toString();
    }
    
    public static byte[] fromBlob(Blob blob) {
        InputStream stream = null;
        ByteArrayOutputStream fakeStream = new ByteArrayOutputStream();
        try {
            stream = blob.getBinaryStream();
            byte[] buffer = new byte[1024];
            
            int range = buffer.length;
            while(true) {
                int reads = stream.read(buffer, 0, range);
                if(reads < 0) break;
                fakeStream.write(buffer, 0, reads);
                if(range > reads) range = reads;
            }
        } catch(Throwable t) {
            throw new JDBCException(t);
        } finally {
            try { stream.close();     } catch(Throwable ignores) {}
            try { fakeStream.close(); } catch(Throwable ignores) {}
        }
        return fakeStream.toByteArray();
    }
    
    public static byte[] fromBinary(InputStream stream) {
        ByteArrayOutputStream fakeStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            
            int range = buffer.length;
            while(true) {
                int reads = stream.read(buffer, 0, range);
                if(reads < 0) break;
                fakeStream.write(buffer, 0, reads);
                if(range > reads) range = reads;
            }
        } catch(Throwable t) {
            throw new JDBCException(t);
        } finally {
            try { fakeStream.close(); } catch(Throwable ignores) {}
        }
        return fakeStream.toByteArray();
    }

	@Override
	public Map<String, Object> getRowMap(int rowIdx) {
		return getRowMap(new Integer(rowIdx));
	}

	@Override
	public Object get(int rowIdx, String columnName) {
		return getValue(new Integer(rowIdx), columnName);
	}
}
