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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hjow.common.data.table.exception.ColumnDuplicationException;
import hjow.common.data.table.exception.NoSuchColumnException;

/** 가장 기본적인 Table 컬렉션 구현체입니다. */
public class SimpleTable extends ConstraintedTable {
	private static final long serialVersionUID = -8622789127722798378L;
	
	protected List<Map<String, Object>> rowStorage;
	protected List<Column> columns;
	
	/** 아무 컬럼 정보도 없는 텅 빈 Table 컬렉션을 만듭니다. */
	public SimpleTable() {
		super();
		columns = new ArrayList<Column>();
		rowStorage = new ArrayList<Map<String,Object>>();
	}
	
	/** 다른 Table 컬렉션으로부터 데이터를 복제합니다. */
	public SimpleTable(Table other) {
		this();
		
		addAll(other);
	}
	
	/** JDBC 조회 결과로부터 얻은 ResultSet 으로부터 데이터를 꺼내 Table 컬렉션을 만듭니다. 행 위치 포인터는 반드시 초기 상태로 들어와야 하며, 처리가 끝나면 포인터가 끝으로 가 있게 됩니다. ResultSet 객체가 이 메소드 내에서 닫히지는 않습니다. */
	public SimpleTable(ResultSet resultSet) throws SQLException {
		this();
		addAll(resultSet);
	}
	
	/** 초기화 여부를 반환합니다. */
	public boolean isInitialized() {
		return (! columns.isEmpty());
	}
	
	@Override
	public synchronized void addAll(Table other) {
		if(isInitialized()) { // 초기화 여부를 컬럼 정보 존재여부로 판단한다. 즉, 이 시점에 컬럼 정보가 있으면 ResultSet 으로부터 컬럼 정보를 꺼내지 않는다.
			for(Column col : other.getColumns()) {
				Column newOne = new Column(col);
				this.columns.add(newOne);
			}
		}
		
		List<Map<String, Object>> targetRows = new ArrayList<Map<String,Object>>();
		
		for(int rdx=0; rdx<other.getRowCount(); rdx++) {
			targetRows.add(other.getRowMap(rdx));
		}
		
		checkAddRowAvailable(targetRows);
		
		for(Map<String, Object> rows : targetRows) {
			addRowMap(rows);
		}
	}
	
	@Override
	public synchronized void addAll(ResultSet resultSet) throws SQLException {
		if(isInitialized()) { // 초기화 여부를 컬럼 정보 존재여부로 판단한다. 즉, 이 시점에 컬럼 정보가 있으면 ResultSet 으로부터 컬럼 정보를 꺼내지 않는다.
			ResultSetMetaData meta = resultSet.getMetaData();
			int colCnt = meta.getColumnCount();
			for(int colIdx = 0; colIdx<colCnt; colIdx++) {
				String colName = meta.getColumnName(colIdx + 1);
				int    colType = meta.getColumnType(colIdx + 1);
				
				columns.add(new Column(colName, colType));
			}
		}
		
		List<Map<String, Object>> targetRows = new ArrayList<Map<String,Object>>();
		
		while(resultSet.next()) {
			Map<String, Object> newRow = new HashMap<String, Object>();
			for(int colIdx=0; colIdx < columns.size(); colIdx++) {
				Column column  = columns.get(colIdx);
				String colName = column.getName();
				int    colType = column.getType();
				
				switch(colType) {
				case Types.BIGINT:
					newRow.put(colName, resultSet.getLong(colName));
					break;
				case Types.DECIMAL:
					newRow.put(colName, resultSet.getBigDecimal(colName));
					break;
				case Types.DOUBLE:
					newRow.put(colName, resultSet.getDouble(colName));
					break;
				case Types.FLOAT:
					newRow.put(colName, resultSet.getFloat(colName));
					break;
				case Types.INTEGER:
					newRow.put(colName, resultSet.getInt(colName));
					break;
				case Types.NUMERIC:
					newRow.put(colName, resultSet.getBigDecimal(colName));
					break;
				case Types.VARCHAR:
					newRow.put(colName, resultSet.getString(colName));
					break;
				case Types.CHAR:
					newRow.put(colName, resultSet.getString(colName));
					break;
				case Types.CLOB:
					newRow.put(colName, resultSet.getClob(colName));
					break;
				case Types.NVARCHAR:
					newRow.put(colName, resultSet.getString(colName));
					break;
				case Types.NCHAR:
					newRow.put(colName, resultSet.getString(colName));
					break;
				case Types.BINARY:
					newRow.put(colName, resultSet.getBinaryStream(colName));
					break;
				case Types.BLOB:
					newRow.put(colName, resultSet.getBlob(colName));
					break;
				case Types.DATE:
					newRow.put(colName, resultSet.getDate(colName));
					break;
				case Types.TIMESTAMP:
					newRow.put(colName, resultSet.getTimestamp(colName));
					break;
				}
			}
			targetRows.add(newRow);
		}
		
		checkAddRowAvailable(targetRows);
		
		for(Map<String, Object> rows : targetRows) {
			addRowMap(rows);
		}
	}

	@Override
	public synchronized void addRowMap(Map<String, Object> rowOne) {
		Map<String, Object> realRow = new HashMap<String, Object>();
		for(Column col : columns) {
			realRow.put(col.getName(), Column.validateColumnTypedData(this, col.getName(), rowOne.get(col.getName())));
			addId(realRow);
		}
		
		checkAddRowAvailable(realRow);
		
		rowStorage.add(realRow);
	}
	
	@Override
	public synchronized void addRow(Map<Column, Object> rowOne) {
		Map<String, Object> realRow = new HashMap<String, Object>();
		for(Column col : columns) {
			realRow.put(col.getName(), Column.validateColumnTypedData(this, col.getName(), rowOne.get(col)));
			addId(realRow);
		}
		
		checkAddRowAvailable(realRow);
		
		rowStorage.add(realRow);
	}
	
	@Override
	public void actEachRow(RowIterateAction action) {
		for(Map<String, Object> rows : rowStorage) {
			Map<String, Object> cloned = new HashMap<String, Object>();
			cloned.putAll(rows);
			cloned.remove("[SERIAL]");
			action.onEachRow(cloned);
		}
	}
	
	@Override
	public void editEachRow(RowIterateAction action) {
		for(Map<String, Object> rows : rowStorage) {
			Map<String, Object> cloned = new HashMap<String, Object>();
			cloned.putAll(rows);
			cloned.remove("[SERIAL]");
			action.onEachRow(cloned);
			addId(cloned);
			rows.putAll(cloned);
		}
	}
	
	@Override
	protected void actDetailEachRow(RowIterateAction action) {
		for(Map<String, Object> rows : rowStorage) {
			action.onEachRow(rows);
		}
	}

	@Override
	public synchronized void removeRow(int rowIdx) {
		checkRemoveRowAvailable(rowStorage.get(rowIdx));
		rowStorage.remove(rowIdx);
	}

	@Override
	public void clear() {
		checkRemoveRowAvailable(rowStorage);
		rowStorage.clear();
	}

	@Override
	public void sort(Comparator<Map<String, Object>> comparator) {
		Collections.sort(rowStorage, comparator);
	}

	@Override
	public void set(int rowIdx, String columnName, Object newData) {
		columnName = Column.correctColumnName(columnName);
		Map<String, Object> targetRow = rowStorage.get(rowIdx);
		checkModifyRowData(targetRow, columnName, newData);
		targetRow.put(columnName, Column.validateColumnTypedData(this, columnName, newData));
	}
	
	@Override
	public void set(String columnName, Object newData) {
		columnName = Column.correctColumnName(columnName);
		checkModifyRowData(rowStorage, columnName, newData);
		for(Map<String, Object> rows : rowStorage) {
			rows.put(columnName, newData);
		}
	}

	@Override
	public List<String> getColumnNames() {
		List<String> columnNameList = new ArrayList<String>();
		for(Column c : columns) {
			columnNameList.add(c.getName());
		}
		return columnNameList;
	}

	@Override
	public List<Integer> getColumnTypes() {
		List<Integer> typeList = new ArrayList<Integer>();
		for(Column c : columns) {
			typeList.add(c.getType());
		}
		return typeList;
	}

	@Override
	public String getColumnName(int colIdx) {
		return columns.get(colIdx).getName();
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public int getColumnType(String targetColumnName) {
		targetColumnName = Column.correctColumnName(targetColumnName);
		int colIdx = -1;
		for(int idx=0; idx<columns.size(); idx++) {
			if(columns.get(idx).equals(targetColumnName)) {
				colIdx = idx;
				break;
			}
		}
		if(colIdx < 0) throw new NoSuchColumnException(targetColumnName);
		return columns.get(colIdx).getType();
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public int getRowCount() {
		return rowStorage.size();
	}

	@Override
	public Map<String, Object> getRowMap(int rowIdx) {
		Map<String, Object> currentRow = rowStorage.get(rowIdx);
		Map<String, Object> newRow = new HashMap<String, Object>();
		for(String c : getColumnNames()) {
			newRow.put(c, currentRow.get(c));
		}
		return newRow;
	}
	
	@Override
	public Row getRow(int rowIdx) {
		Row row = new Row();
		Map<String, Object> currentRow = rowStorage.get(rowIdx);
		for(Column c : columns) {
			row.put(c, currentRow.get(c.getName()));
		}
		return row;
	}

	@Override
	public Object get(int rowIdx, String columnName) {
		columnName = Column.correctColumnName(columnName);
		int idx = getColumnNames().indexOf(columnName);
		if(idx < 0) throw new NoSuchColumnException(columnName);
		return rowStorage.get(rowIdx).get(columnName);
	}

	@Override
	public void dropColumn(String columnName) {
		columnName = Column.correctColumnName(columnName);
		int idx = getColumnNames().indexOf(columnName);
		if(idx < 0) throw new NoSuchColumnException(columnName);
		
		checkDropColumn(columnName);
		
		columns.remove(idx);
		
		for(Map<String, Object> rows : rowStorage) {
			rows.remove(columnName);
		}
	}

	@Override
	public void addColumn(String columnName, int columnType, Object defaultValue) {
		addColumn(new Column(columnName, columnType), defaultValue);
	}
	
	@Override
	public void addColumn(Column col, Object defaultValue) {
		if(getColumnNames().contains(col.getName())) throw new ColumnDuplicationException(col.getName());
		
		checkAddColumn(this, col.getName());
		columns.add(new Column(col));
		
		if(defaultValue != null) {
			for(Map<String, Object> rows : rowStorage) {
				rows.put(col.getName(), defaultValue);
			}
		}
	}

	/** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
	@Deprecated
	public List<Map<String, Object>> getRowStorage() {
		return rowStorage;
	}

	/** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
	@Deprecated
	public void setRowStorage(List<Map<String, Object>> rowStorage) {
		this.rowStorage = rowStorage;
	}
	@Override
	public List<Column> getColumns() {
		List<Column> newList = new ArrayList<Column>();
		for(Column c : columns) {
			newList.add(new Column(c));
		}
		return newList;
	}

	/** 직접 이 메소드를 사용하지 마세요. 직렬화 시에 JVM에서 쓰라고 만든 메소드입니다. 직접 사용 시 제약조건 관련 문제가 발생할 수 있습니다. */
	@Deprecated
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	@Override
	public Column getColumn(String colName) {
		colName = Column.correctColumnName(colName);
		for(Column c : columns) {
			if(c.getName().equals(colName)) return c;
		}
		return null;
	}
}
