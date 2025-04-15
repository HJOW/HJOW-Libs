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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;

import org.duckdns.hjow.commons.data.table.constraint.PrimaryKey;

/** 편집 가능한 Table 컬렉션입니다. */
public interface EditableTable extends Table {
    /** 행을 추가합니다. */
    public void addRowMap(Map<String, Object> rowOne);
    /** 행을 추가합니다. */
    public void addRow(Map<Column, Object> rowOne);
    /** 행을 제거합니다. */
    public void removeRow(int rowIdx);
    /** 행을 모두 제거합니다. */
    public void clear();
    /** 행들을 정렬합니다. */
    public void sort(Comparator<Map<String, Object>> comparator);
    /** 특정 행 특정 컬럼의 데이터를 변경합니다. */
    public void set(int rowIdx, String columnName, Object newData);
    /** 모든 행의 특정 컬럼의 데이터를 변경합니다. */
    public void set(String columnName, Object newData);
    /** 다른 테이블로부터 데이터를 복제합니다. 해당 테이블이 이 테이블에 없는 컬럼을 가질 경우 해당 데이터는 건너뛰게 됩니다. */
    public void addAll(Table other);
    /** JDBC 조회 결과로부터 얻은 ResultSet 으로부터 데이터를 꺼내 넣습니다. 행 위치 포인터는 반드시 초기 상태로 들어와야 하며, 처리가 끝나면 포인터가 끝으로 가 있게 됩니다. ResultSet 객체가 이 메소드 내에서 닫히지는 않습니다. */
    public void addAll(ResultSet resultSet) throws SQLException;
    /** 각 행별로 작업을 수행합니다. 작업 내에서 행의 데이터 변경이 가능합니다. */
    public void editEachRow(RowIterateAction action);
    /** Primary Key 를 반환합니다. null 일 수 있습니다. */
    public PrimaryKey getPrimaryKey();
}
