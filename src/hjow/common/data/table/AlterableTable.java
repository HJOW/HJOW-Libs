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

/** 컬럼의 변경까지 가능한 Table 컬렉션입니다. */
public interface AlterableTable extends EditableTable {
    /** 컬럼을 하나 제거합니다. */
    public void dropColumn(String columnName);
    /** 컬럼을 추가합니다. */
    public void addColumn(String columnName, int columnType, Object defaultValue);
    /** 컬럼을 추가합니다. */
    public void addColumn(Column col, Object defaultValue);
}
