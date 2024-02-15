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
package hjow.common.data.table.constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hjow.common.data.table.Column;
import hjow.common.data.table.EditableTable;
import hjow.common.data.table.Row;
import hjow.common.data.table.exception.ConstraintViolationException;

/** Primary Key 선언에 사용됩니다. */
public class PrimaryKey extends Constraint {
	private static final long serialVersionUID = 3369565292065789126L;
	
	/** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
	@Deprecated
	public PrimaryKey() { }
	
	public PrimaryKey(String ... columnNames) {
		for(String c : columnNames) {
			this.columnNames.add(c);
		}
	}
	
	@Override
	public ConstraintViolationException canAddRowAvailable(EditableTable table, Map<String, Object> targetRow) {
		Iterator<Map<String, Object>> iterator = table.iterator();
		
		while(iterator.hasNext()) {
			Map<String, Object> nowRow = iterator.next();
			int sameColCnt = 0;
			
			for(String col : columnNames) {
				Column column = table.getColumn(col);
				Object rowVal = Column.validateColumnTypedData(table, column.getName(), nowRow.get(column.getName()));
				Object targetRowVal = targetRow.get(col);
				
				if(rowVal       == null) return new ConstraintViolationException(this);
				if(targetRowVal == null) return new ConstraintViolationException(this);
				
				if(Column.isEqualValue(column.getType(), rowVal, targetRowVal)) {
					sameColCnt++;
				}
			}
			
			if(sameColCnt >= columnNames.size()) {
				return new ConstraintViolationException(this);
			}
		}
		return null;
	}
	
	protected List<String> getSortedColumnNames() {
		List<String> newColNames = new ArrayList<String>();
		newColNames.addAll(columnNames);
		Collections.sort(newColNames);
		return newColNames;
	}
	
	@Override
	public ConstraintViolationException canAddRowAvailable(EditableTable table, List<Map<String, Object>> targetRow) {
		for(Map<String, Object> rowOne : targetRow) {
			ConstraintViolationException ones = canAddRowAvailable(table, rowOne);
			if(ones != null) return ones;
		}
		return null;
	}
	
	@Override
	public ConstraintViolationException canModifyRowData(EditableTable table, Map<String, Object> targetRow, String columnName, Object newValue) {
		columnName = Column.correctColumnName(columnName);
		Iterator<Map<String, Object>> iterator = table.iterator();
		
		List<Row> rowsListsList = new ArrayList<Row>();
		
		while(iterator.hasNext()) {
			Map<String, Object> nowRow = iterator.next();
			Map<Column, Object> rowMap = new HashMap<Column, Object>(); 
			
			for(String col : columnNames) {
				Column column = table.getColumn(col);
				Object rowVal = Column.validateColumnTypedData(table, column.getName(), nowRow.get(column.getName()));
				if(column.getName().equals(columnName)) {
					rowVal = newValue;
				}
				
				if(rowVal == null) return new ConstraintViolationException(this);
				
				rowMap.put(column, rowVal);
			}
			
			Row rowObj = new Row(rowMap);
			
			if(rowsListsList.contains(rowObj)) return new ConstraintViolationException(this);
			rowsListsList.add(rowObj);
		}
		
		return null;
	}
	
	@Override
	public ConstraintViolationException canDropColumn(EditableTable table, String columnName) {
		if(columnNames.contains(Column.correctColumnName(columnName))) return new ConstraintViolationException(this);
		return null;
	}
	
	@Override
	public ConstraintViolationException checkTotal(EditableTable table) {
		Iterator<Map<String, Object>> iterator = table.iterator();
		
		List<Row> rowsListsList = new ArrayList<Row>();
		
		while(iterator.hasNext()) {
			Map<String, Object> nowRow = iterator.next();
			Map<Column, Object> rowMap = new HashMap<Column, Object>(); 
			
			for(String col : columnNames) {
				Column column = table.getColumn(col);
				Object rowVal = Column.validateColumnTypedData(table, column.getName(), nowRow.get(column.getName()));
				
				if(rowVal == null) return new ConstraintViolationException(this);
				
				rowMap.put(column, rowVal);
			}
			
			Row rowObj = new Row(rowMap);
			
			if(rowsListsList.contains(rowObj)) return new ConstraintViolationException(this);
			rowsListsList.add(rowObj);
		}
		
		return null;
	}
}
