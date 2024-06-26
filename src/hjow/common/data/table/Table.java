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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/** 관계형 모델의 구조를 갖는 데이터 컬렉션입니다. */
public interface Table extends Serializable, Iterable<Map<String, Object>> {
    /** 테이블 이름을 반환합니다. null 일 수 있습니다. */
    public String getName();
    /** 컬럼 정보를 반환합니다. */
    public Column getColumn(String colName);
    /** 컬럼 정보들을 반환합니다. */
    public List<Column> getColumns();
    /** 컬럼 이름들을 반환합니다. */
    public List<String> getColumnNames();
    /** 컬럼의 타입들을 반환합니다. */
    public List<Integer> getColumnTypes();
    /** 특정 번째의 컬럼의 이름을 반환합니다. */
    public String getColumnName(int colIdx);
    /** 특정 컬럼의 타입을 알아냅니다. */
    public int getColumnType(String columnName);
    /** 컬럼 수를 반환합니다. */
    public int getColumnCount();
    /** 행의 수를 구합니다. */
    public int getRowCount();
    /** 특정 행을 Map 으로 반환합니다. */
    public Map<String, Object> getRowMap(int rowIdx);
    /** 특정 행을 반환합니다. */
    public Row getRow(int rowIdx);
    /** 특정 행의 특정 컬럼 데이터를 반환합니다. */
    public Object get(int rowIdx, String columnName);
    /** 각 행별로 작업을 수행합니다. 작업 내에서 행의 데이터 변경은 적용되지 않습니다. */
    public void actEachRow(RowIterateAction action);
    /** JSON 형식 문자열로 변환합니다. */
    public String toJSON();
}
