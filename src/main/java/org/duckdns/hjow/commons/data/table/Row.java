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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.duckdns.hjow.commons.json.JsonObject;

public class Row extends HashMap<Column, Object> implements Serializable {
    private static final long serialVersionUID = 2032072820408598680L;
    
    public Row() {
        
    }
    
    public Row(Map<Column, Object> rowData) {
        putAll(rowData);
    }
    
    @Override
    public boolean equals(Object others) {
        if(others == null) return false;
        if(! (others instanceof Row)) {
            return false;
        }
        
        Row otherObj = (Row) others;
        Set<Column> columns = keySet();
        
        for(Column col : columns) {
            Object one = get(col);
            Object other = otherObj.get(col);
            
            if(one == null && other != null) return false;
            if(one != null && other == null) return false;
            if(one != null && other != null) {
                if(! (Column.isEqualValue(col.getType(), one, other))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public JsonObject toJSONObject() {
        JsonObject obj = new JsonObject();
        Set<Column> cols = keySet();
        for(Column c : cols) {
            obj.put(c.getName(), get(c));
        }
        return obj;
    }
    
    public String toJSON() {
        return toJSONObject().toJSON();
    }
}
