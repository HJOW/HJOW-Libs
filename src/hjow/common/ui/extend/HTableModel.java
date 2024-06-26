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
package hjow.common.ui.extend;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import hjow.common.data.table.Column;
import hjow.common.data.table.Table;

public class HTableModel extends DefaultTableModel {
    private static final long serialVersionUID = -3327938946058763421L;
    protected boolean editable = false;
    public HTableModel() {
        super();
    }
    public HTableModel(Vector<Object> data, Vector<String> columns) {
        super(data, columns);
    }
    public HTableModel(Table table) {
        super(getDataList(table), getColumnList(table));
    }
    public boolean isEditable() {
        return editable;
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
       return editable;
    }
    
    protected static Vector<String> getColumnList(Table table) {
        Vector<String> columnContent = new Vector<String>();
        List<Column> cols = table.getColumns();
        for(Column c : cols) {
            columnContent.add(c.getName());
        }
        return columnContent;
    }
    protected static Vector<Object> getDataList(Table table) {
        Vector<Object> data = new Vector<Object>();
        Vector<String> columnContent = getColumnList(table);
        for(Map<String, Object> rowOne : table) {
            Vector<String> rowData = new Vector<String>();
            
            for(String c : columnContent) {
                Object obj = rowOne.get(c);
                if(obj == null) obj = "";
                rowData.add(String.valueOf(obj));
            }
            
            data.add(rowData);
        }
        return data;
    }
}
