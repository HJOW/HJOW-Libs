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
package hjow.common.data.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import hjow.common.data.MutableInteger;
import hjow.common.data.table.constraint.Constraint;
import hjow.common.data.table.constraint.PrimaryKey;
import hjow.common.data.table.exception.ConstraintDuplicationException;
import hjow.common.data.table.exception.ConstraintViolationException;
import hjow.common.data.table.exception.PrimaryKeyDuplicationException;
import hjow.common.json.JsonArray;
import hjow.common.script.PublicMethodOpenedClass;

/** 제약 조건을 가지는 테이블 컬렉션의 상위 클래스로 제약 조건 리스트를 보유하고 있습니다. */
public abstract class ConstraintedTable extends PublicMethodOpenedClass implements AlterableTable {
    private static final long serialVersionUID = -1611288591463272579L;
    
    protected List<Constraint> constraints = new Vector<Constraint>();
    protected String name = null;
    
    protected ConstraintedTable() {
        if(constraints == null) constraints = new Vector<Constraint>();
    }
    
    protected void checkAddRowAvailable(Map<String, Object> targetRow) {
        for(Constraint c : constraints) {
            ConstraintViolationException ex = c.canAddRowAvailable(this, targetRow);
            if(ex != null) throw ex;
        }
    }
    
    protected void checkAddRowAvailable(List<Map<String, Object>> targetRow) {
        for(Constraint c : constraints) {
            ConstraintViolationException ex = c.canAddRowAvailable(this, targetRow);
            if(ex != null) throw ex;
        }
    }
    
    protected void checkRemoveRowAvailable(Map<String, Object> targetRow) {
        for(Constraint c : constraints) {
            ConstraintViolationException ex = c.canRemoveRowAvailable(this, targetRow);
            if(ex != null) throw ex;
        }
    }
    
    protected void checkRemoveRowAvailable(List<Map<String, Object>> targetRow) {
        for(Constraint c : constraints) {
            ConstraintViolationException ex = c.canRemoveRowAvailable(this, targetRow);
            if(ex != null) throw ex;
        }
    }
    
    protected void checkModifyRowData(Map<String, Object> targetRow, String columnName, Object newValue) {
        for(Constraint c : constraints) {
            ConstraintViolationException ex = c.canModifyRowData(this, targetRow, columnName, newValue);
            if(ex != null) throw ex;
        }
    }
    
    protected void checkModifyRowData(List<Map<String, Object>> targetRow, String columnName, Object newValue) {
        for(Constraint c : constraints) {
            ConstraintViolationException ex = c.canModifyRowData(this, targetRow, columnName, newValue);
            if(ex != null) throw ex;
        }
    }
    
    protected void checkDropColumn(String columnName) {
        for(Constraint c : constraints) {
            ConstraintViolationException ex = c.canDropColumn(this, columnName);
            if(ex != null) throw ex;
        }
    }
    
    protected void checkAddColumn(EditableTable table, String columnName) {
        for(Constraint c : constraints) {
            ConstraintViolationException ex = c.canAddColumn(this, columnName);
            if(ex != null) throw ex;
        }
    }
    
    @Override
    public Iterator<Map<String, Object>> iterator() {
        final List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
        actDetailEachRow(new RowIterateAction() {
            @Override
            public void onEachRow(Map<String, Object> rowOne) {
                Map<String, Object> exposerRow = new HashMap<String, Object>();
                for(String colName : getColumnNames()) {
                    exposerRow.put(colName, rowOne.get(colName));
                }
                rows.add(exposerRow);
            }
        });
        return rows.iterator();
    }
    
    /** 각 행별로 수행할 디테일한 작업을 합니다. 복제된 객체가 아닌 행 컬렉션에 직접 액세스가 가능합니다. */
    protected abstract void actDetailEachRow(RowIterateAction action);
    
    /** 각 행에 부여할 고유값을 생성합니다. */
    protected synchronized long createNewId() {
        final MutableInteger newId = new MutableInteger(0);
        
        actDetailEachRow(new RowIterateAction() {
            @Override
            public void onEachRow(Map<String, Object> rowOne) {
                Object idObj = rowOne.get("[SERIAL]");
                long idVal = -1;
                if(idObj instanceof Number) {
                    idVal = ((Number) idObj).longValue();
                } else {
                    idVal = new Long(String.valueOf(idObj));
                }
                
                if(newId.longValue() < idVal) newId.setValue(idVal);
            }
        });
        
        return newId.longValue();
    }
    
    /** 제약 조건을 추가합니다. */
    public synchronized void addConstraint(final Constraint newCon) {
        String constraintName = newCon.getName();
        for(int idx=0; idx<constraints.size(); idx++) {
            Constraint cons = constraints.get(idx);
            if(cons.getName().equals(constraintName)) throw new ConstraintDuplicationException(constraintName);
            if((newCon instanceof PrimaryKey) && (cons instanceof PrimaryKey)) throw new PrimaryKeyDuplicationException();
        }
        newCon.checkTotal(this);
        constraints.add(newCon);
    }
    
    /** 제약 조건을 제거합니다. */
    public synchronized void dropConstraint(String constraintName) {
        constraintName = Column.correctColumnName(constraintName);
        for(int idx=0; idx<constraints.size(); idx++) {
            Constraint cons = constraints.get(idx);
            if(cons.getName().equals(constraintName)) {
                constraints.remove(idx);
                return;
            }
        }
    }
    
    /** 자기자신 객체를 반환합니다. 익명클래스 내에서 활용하면 편리합니다. */
    protected ConstraintedTable getSelf() {
        return this;
    }
    
    /** 행에 고유값을 부여합니다. */
    protected synchronized void addId(Map<String, Object> rows) {
        rows.put("[SERIAL]", new Long(createNewId()));
    }
    
    @Override
    public PrimaryKey getPrimaryKey() {
        for(Constraint c : constraints) {
            if(c instanceof PrimaryKey) {
                return (PrimaryKey) c;
            }
        }
        
        return null;
    }

    /** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
    @Deprecated
    public List<Constraint> getConstraints() {
        return constraints;
    }

    /** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
    @Deprecated
    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public String getName() {
        return name;
    }

    /** 테이블 이름을 설정합니다. */
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toJSON() {
        JsonArray arr = new JsonArray();
        int rowIdx = getRowCount();
        for(int idx=0; idx<rowIdx; idx++) {
            arr.add(getRow(idx).toJSONObject());
        }
        return arr.toJSON();
    }
}
