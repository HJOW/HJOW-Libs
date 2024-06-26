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

import java.util.List;
import java.util.Vector;

import hjow.common.core.Releasable;
import hjow.common.script.PublicMethodOpenedClass;
import hjow.common.util.DataUtil;
import hjow.common.util.SyntaxUtil;

public class SimpleDatabase extends PublicMethodOpenedClass implements Releasable {
    private static final long serialVersionUID = -5543218073216561300L;
    
    protected String      name;
    protected List<Table> tables           = new Vector<Table>();
    
    public SimpleDatabase() {
        
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
    
    public void addTable(Table tableOne) {
        tables.add(tableOne);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void releaseResource() {
        for(Table t : tables) {
            if(t instanceof Releasable) {
                ((Releasable) t).releaseResource();
            }
        }
        tables.clear();
    }
    
    public Table executeSQL(String sql) {
        if(sql == null) throw new NullPointerException();
        if(DataUtil.isEmpty(sql)) return null;
        List<String> eachSql = SyntaxUtil.getTokens(sql, ';');
        Table results = null;
        for(String s : eachSql) {
            results = executeSQLOne(s);
        }
        return results;
    }
    
    protected Table executeSQLOne(String sqlOne) {
        sqlOne = sqlOne.trim();
        Table results = null;
        
        List<String> eachBlock = SyntaxUtil.getTokens(sqlOne, ' ', '\t', '\n');
        if(eachBlock.isEmpty()) return null;
        
        String kindOf = eachBlock.get(0).toUpperCase();
        
        if(kindOf.equals("SELECT")) {
            results = executeSelectBlocks(eachBlock);
        }
        
        return results;
    }
    
    protected Table executeSelectBlocks(List<String> blocks) {
        Table results = null;
        
        String tableName = null;
        boolean meetFrom = false;
        boolean meetWhiles = false;
        
        for(String b : blocks) {
            String upperCases = b.toUpperCase().trim();
            
            if(! meetFrom) {
                if(upperCases.equals("FROM")) {
                    meetFrom = true;
                    continue;
                }
                
                
            } else if(meetFrom && (! meetWhiles)) {
                if(tableName != null) {
                    tableName = b;
                }
                
                
            }
            
            
            
        }
        
        return results;
    }
}