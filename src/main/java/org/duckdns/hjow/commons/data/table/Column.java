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
package org.duckdns.hjow.commons.data.table;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import javax.activation.UnsupportedDataTypeException;

import org.duckdns.hjow.commons.data.Binary;
import org.duckdns.hjow.commons.data.MutableNumber;
import org.duckdns.hjow.commons.script.PublicMethodOpenedClass;

public class Column extends PublicMethodOpenedClass {
    private static final long serialVersionUID = -7619758531265009851L;
    public static final int TYPE_STRING  = Types.VARCHAR;
    public static final int TYPE_NUMBER  = Types.NUMERIC;
    public static final int TYPE_BOOLEAN = Types.BOOLEAN;
    public static final int TYPE_BINARY  = Types.BLOB;
    public static final int TYPE_DATE    = Types.DATE;
    
    protected String name;
    protected int type;
    
    /** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
    @Deprecated
    public Column() {
        
    }
    
    public Column(String name, int type) {
        super();
        this.name = correctColumnName(name);
        this.type = convertJdbcType(type);
    }
    
    /** 컬럼 정보를 복사하는 데 사용하면 편리합니다. */
    public Column(Column oldOne) {
        this(oldOne.getName(), oldOne.getType());
    }
    
    /** 컬럼 이름을 규칙에 맞도록 보정합니다. */
    public static String correctColumnName(String columnName) {
        String newColName = columnName.replace(" ", "").replace(".", "").replace(",", "").replace("\t", "").replace("-", "").replace("+", "").replace("*", "").replace("%", "").replace("'", "").replace("\"", "").replace("{", "").replace("}", "").replace("[", "").replace("]", "").toUpperCase().trim();
        if(newColName.equals("")) throw new IllegalArgumentException("The column name cannot be empty string.");
        return newColName;
    }

    /** JDBC의 타입 코드를 변환합니다. */
    public static int convertJdbcType(int colType) {
        switch(colType) {
        case Types.BIGINT:
            return TYPE_NUMBER;
        case Types.DECIMAL:
            return TYPE_NUMBER;
        case Types.DOUBLE:
            return TYPE_NUMBER;
        case Types.FLOAT:
            return TYPE_NUMBER;
        case Types.INTEGER:
            return TYPE_NUMBER;
        case Types.NUMERIC:
            return TYPE_NUMBER;
        case Types.VARCHAR:
            return TYPE_STRING;
        case Types.CHAR:
            return TYPE_STRING;
        case Types.CLOB:
            return TYPE_STRING;
        case Types.NVARCHAR:
            return TYPE_STRING;
        case Types.NCHAR:
            return TYPE_STRING;
        case Types.BINARY:
            return TYPE_BINARY;
        case Types.BLOB:
            return TYPE_BINARY;
        case Types.DATE:
            return TYPE_DATE;
        case Types.TIMESTAMP:
            return TYPE_DATE;
        }
        throw new RuntimeException(new UnsupportedDataTypeException());
    }

    public String getName() {
        return name;
    }

    /** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    /** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
    @Deprecated
    public void setType(int type) {
        this.type = type;
    }
    
    /***/
    public static boolean isEqualValue(int columnType, Object one, Object two) {
        if(one == null && two == null) return true;
        if(one != null && two == null) return false;
        if(one == null && two != null) return false;
        switch(columnType) {
        case Column.TYPE_NUMBER:
            Number rowNum       = (Number) one;
            Number targetRowNum = (Number) two;
            if(rowNum instanceof BigInteger) {
                if(targetRowNum instanceof BigInteger) {
                    if(rowNum.equals(targetRowNum)) return true;
                } else {
                    if(rowNum.equals(new BigInteger(String.valueOf(targetRowNum.longValue())))) return true;
                }
            } else if(rowNum instanceof BigDecimal) {
                if(targetRowNum instanceof BigDecimal) {
                    if(rowNum.equals(targetRowNum)) return true;
                } else {
                    if(rowNum.equals(new BigDecimal(targetRowNum.doubleValue()))) return true;
                }
            } else {
                if(rowNum.equals(targetRowNum)) return true;
            }
            break;
        default:
            if(one.equals(two)) return true;
        }
        
        return false;
    }

    /** 데이터를 컬럼 타입에 맞게 변형시켜 반환합니다. 그래도 타입에 맞지 않으면 예외를 발생시킵니다. */
    public static Object validateColumnTypedData(Table table, String columnName, Object newData) {
        int colType = table.getColumnTypes().get(table.getColumnNames().indexOf(columnName));
        boolean colTypeCorrect = false;
        if(newData == null) {
            colTypeCorrect = true;
        }
        if((! colTypeCorrect) && colType == TYPE_BINARY) {
            if(newData instanceof byte[]) {
                colTypeCorrect = true;
                return new Binary((byte[]) newData);
            } else if(newData instanceof Binary) {
                colTypeCorrect = true;
            } else if(newData instanceof ByteArrayOutputStream) {
                colTypeCorrect = true;
                return new Binary(((ByteArrayOutputStream) newData).toByteArray());
            } else if(newData instanceof Blob) {
                colTypeCorrect = true;
                
                ByteArrayOutputStream collector = new ByteArrayOutputStream();
                InputStream stream = null;
                        
                try {
                    stream = ((Blob) newData).getBinaryStream();
                    byte[] buffer = new byte[1024];
                    
                    int ends = buffer.length;
                    while(true) {
                        int reads = stream.read(buffer, 0, ends);
                        if(reads < 0) break;
                        
                        collector.write(buffer, 0, reads);
                    }
                    return new Binary(collector.toByteArray());
                } catch(Throwable t) {
                    throw new RuntimeException(t);
                } finally {
                    if(stream != null) {
                        try { stream.close();   } catch(Throwable ignores) {}
                    }
                    if(collector != null) {
                        try { collector.close();   } catch(Throwable ignores) {}
                    }
                }
            } else if(newData instanceof InputStream) {
                ByteArrayOutputStream collector = new ByteArrayOutputStream();
                InputStream stream = null;
                try {
                    stream = (InputStream) newData;
                    byte[] buffer = new byte[1024];
                    
                    int ends = buffer.length;
                    while(true) {
                        int reads = stream.read(buffer, 0, ends);
                        if(reads < 0) break;
                        
                        collector.write(buffer, 0, reads);
                    }
                    return new Binary(collector.toByteArray());
                } catch(Throwable t) {
                    throw new RuntimeException(t);
                } finally {
                    if(stream != null) {
                        try { stream.close();   } catch(Throwable ignores) {}
                    }
                    if(collector != null) {
                        try { collector.close();   } catch(Throwable ignores) {}
                    }
                }
            }
        }
        if((! colTypeCorrect) && colType == TYPE_BOOLEAN) {
            if(newData instanceof Boolean) {
                colTypeCorrect = true;
            }
        }
        if((! colTypeCorrect) && colType == TYPE_NUMBER) {
            if(newData instanceof Number) {
                colTypeCorrect = true;
                
                Number numberObj = (Number) newData;
                if(newData instanceof Integer) {
                    return new Long((long) (numberObj).intValue());
                }
                if(newData instanceof Long) {
                    return new Long(numberObj.longValue());
                }
                if(newData instanceof Float) {
                    return new Double((double) numberObj.floatValue());
                }
                if(newData instanceof Double) {
                    return new Double(numberObj.doubleValue());
                }
                if(newData instanceof MutableNumber) {
                    MutableNumber mutableObj = (MutableNumber) newData;
                    if(mutableObj.hasFloatingValue()) {
                        return new Double(mutableObj.doubleValue());
                    } else {
                        return new Long(mutableObj.longValue());
                    }
                }
            }
        }
        if((! colTypeCorrect) && colType == TYPE_STRING) {
            if(newData instanceof String) {
                colTypeCorrect = true;
            } else if(newData instanceof CharSequence) {
                colTypeCorrect = true;
                return newData.toString();
            } else if(newData instanceof Clob) {
                Reader reader = null;
                BufferedReader buffered = null;
                colTypeCorrect = true;
                try {
                    reader = ((Clob) newData).getCharacterStream();
                    buffered = new BufferedReader(reader);
                    
                    StringBuilder collections = new StringBuilder("");
                    while(true) {
                        String oneLine = buffered.readLine();
                        if(oneLine == null) break;
                        
                        if(collections.length() >= 1) collections = collections.append("\n");
                        collections = collections.append(oneLine);
                    }
                    return collections.toString();
                } catch(Throwable t) {
                    throw new RuntimeException(t);
                } finally {
                    if(buffered != null) {
                        try { buffered.close(); } catch(Throwable ignores) {}
                    }
                    if(reader != null) {
                        try { reader.close(); } catch(Throwable ignores) {}
                    }
                }
            }
        }
        if((! colTypeCorrect) && colType == TYPE_DATE) {
            if(newData instanceof java.sql.Date) {
                colTypeCorrect = true;
                return new java.util.Date(((java.sql.Date) newData).getTime());
            } else if(newData instanceof Timestamp) {
                colTypeCorrect = true;
                return new java.util.Date(((Timestamp) newData).getTime());
            } else if(newData instanceof java.util.Date) {
                colTypeCorrect = true;
            } else if(newData instanceof Calendar) {
                colTypeCorrect = true;
                return new java.util.Date(((Calendar) newData).getTimeInMillis());
            } else if(newData instanceof Number) {
                colTypeCorrect = true;
                return new java.util.Date(((Number) newData).longValue());
            }
        }
        if(! colTypeCorrect) throw new IllegalArgumentException("Mismatch column type");
        return newData;
    }
}
