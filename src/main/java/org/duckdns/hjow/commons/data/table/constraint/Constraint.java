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
package org.duckdns.hjow.commons.data.table.constraint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.duckdns.hjow.commons.data.table.Column;
import org.duckdns.hjow.commons.data.table.EditableTable;
import org.duckdns.hjow.commons.data.table.exception.ConstraintViolationException;
import org.duckdns.hjow.commons.script.PublicMethodOpenedClass;

/** 제약 조건을 지정하는 데 사용되는 클래스입니다. 외래키는 이 클래스로 관리할 수 없습니다. */
public abstract class Constraint extends PublicMethodOpenedClass implements Serializable {
    private static final long serialVersionUID = 3592748545096057666L;
    
    protected List<String> columnNames;
    protected String name;
    
    /** 행을 하나 추가할 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException canAddRowAvailable(EditableTable table, Map<String, Object> targetRow) {
        return null;
    }
    
    /** 행 여럿을 추가할 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException canAddRowAvailable(EditableTable table, List<Map<String, Object>> targetRow) {
        for(Map<String, Object> rowOne : targetRow) {
            ConstraintViolationException ones = canAddRowAvailable(table, rowOne);
            if(ones != null) return ones;
        }
        return null;
    }
    
    /** 행 하나를 제거할 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException canRemoveRowAvailable(EditableTable table, Map<String, Object> targetRow) {
        return null;
    }
    
    /** 행 여럿을 제거할 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException canRemoveRowAvailable(EditableTable table, List<Map<String, Object>> targetRow) {
        for(Map<String, Object> rowOne : targetRow) {
            ConstraintViolationException ones = canRemoveRowAvailable(table, rowOne);
            if(ones != null) return ones;
        }
        return null;
    }
    
    /** 행 하나의 값을 변경할 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException canModifyRowData(EditableTable table, Map<String, Object> targetRow, String columnName, Object newValue) {
        return null;
    }
    
    /** 행 여럿의 값을 일괄 변경할 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException canModifyRowData(EditableTable table, List<Map<String, Object>> targetRow, String columnName, Object newValue) {
        for(Map<String, Object> rowOne : targetRow) {
            ConstraintViolationException ones = canModifyRowData(table, rowOne, columnName, newValue);
            if(ones != null) return ones;
        }
        return null;
    }
    
    /** 컬럼 하나를 제거할 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException canDropColumn(EditableTable table, String columnName) {
        return null;
    }
    
    /** 컬럼 하나를 추가할 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException canAddColumn(EditableTable table, String columnName) {
        return null;
    }
    
    /** 이 제약 조건이 추가될 때 테이블 컬렉션에서 호출됩니다. null 반환 시 정상 동작으로 취급하고, 그 외 뭔가를 리턴하면 제약조건 위반으로 처리됩니다. */
    public ConstraintViolationException checkTotal(EditableTable table) {
        return null;
    }

    /** 이 제약조건에 속한 컬럼 이름들을 반환합니다. */
    public List<String> getColumnNames() {
        List<String> newList = new ArrayList<String>();
        newList.addAll(columnNames);
        return newList;
    }

    /** 이 제약조건의 이름을 반환합니다. */
    public String getName() {
        return name;
    }

    /** 이 제약조건의 이름을 설정합니다. */
    public void setName(String name) {
        name = Column.correctColumnName(name);
        this.name = name;
    }
    
    /** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
    @Deprecated
    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }
}
