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

import javax.swing.JTable;

import hjow.common.data.table.Table;

public class HTable extends JTable {
	private static final long serialVersionUID = -1065931820424312860L;
	
	public HTable() {
		super(new HTableModel());
	}
	
	public HTable(Table table) {
		super(new HTableModel(table));
	}
	
	public void setTable(Table table) {
		setModel(new HTableModel(table));
	}
}
