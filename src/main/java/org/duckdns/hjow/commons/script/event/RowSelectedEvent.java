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
package org.duckdns.hjow.commons.script.event;

import java.util.EventObject;
import java.util.Map;

public class RowSelectedEvent extends EventObject {
    private static final long serialVersionUID = -7203311896373296855L;
    protected Map<String, Object> row;
    protected int rowIndex;
    protected String sql;
    
    public RowSelectedEvent(Object source) {
        super(source);
    }
    
    public RowSelectedEvent(Object source, Map<String, Object> row, int rowIdx, String sql) {
        super(source);
        this.row = row;
        this.rowIndex = rowIdx;
        this.sql = sql;
    }

    public Map<String, Object> getRow() {
        return row;
    }

    public void setRow(Map<String, Object> row) {
        this.row = row;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
    
}
