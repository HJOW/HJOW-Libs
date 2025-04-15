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
package org.duckdns.hjow.commons.script.jdbc;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.duckdns.hjow.commons.data.table.Table;
import org.duckdns.hjow.commons.script.event.RowSelectListener;

public interface JDBCTable extends Table {
    /** ResultSet 으로부터 데이터를 꺼내 테이블 내용을 채웁니다. ResultSet 객체의 행 포인터는 반드시 초기 상태여야 합니다. ResultSet 객체는 닫히게 됩니다. */
    public void init(JDBCConnection conn, ResultSet resultSet, List<RowSelectListener> listeners, String sql);
    
    /** get 메소드의 ScriptEngine 호환 버전 */
    public Object getValue(Object rowIndex, Object colInfo);
    
    /** getRow 메소드의 ScriptEngine 호환 버전 */
    public Map<String, Object> getRowAsMap(Object rowIndex);
    
}
