package main;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class RuleTableDataProvider extends AbstractTableModel {
	
	private class RuleTableEntry {
		
		public ArrayList<Object> rowData = new ArrayList<Object>();
		public RuleTableEntry(String name, String result, String action, int amount) {
			rowData.add(name);
			rowData.add(result);
			rowData.add(action);
			rowData.add(amount);
		}
	}
	
	private ArrayList<String> colNames = new ArrayList<String>();
	private ArrayList<RuleTableEntry> tableEntries = new ArrayList<RuleTableEntry>();
	
	public RuleTableDataProvider() {
		colNames.add("identifier");
		colNames.add("result");
		colNames.add("action");
		colNames.add("amount");
		
		tableEntries.add(new RuleTableEntry("+", "+", LActions.ROTATECW.toString(), 90));
		tableEntries.add(new RuleTableEntry("-", "-", LActions.ROTATECC.toString(), 90));
		tableEntries.add(new RuleTableEntry("[", "[", LActions.PUSHSTATE.toString(), -1));
		tableEntries.add(new RuleTableEntry("]", "]", LActions.POPSTATE.toString(), -1));
	}
	
	@Override
	public int getColumnCount() {
		return colNames.size();
	}

	@Override
	public int getRowCount() {
		return tableEntries.size();
	}

	@Override
	public String getColumnName(int col) {
        return colNames.get(col);
    }
	
	@Override
	public Object getValueAt(int row, int col) {
		return tableEntries.get(row).rowData.get(col);
	}
	
	public boolean isCellEditable(int row, int col) {
		if (col == 3) {
			String strVal = (String)getValueAt(row, 2);
			if (strVal.equals(LActions.NOTHING.toString()) || strVal.equals(LActions.POPSTATE.toString()) || strVal.equals(LActions.PUSHSTATE.toString())) {
				return false;
			}
		}
		
        return true;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }
	
	public void setValueAt(Object value, int row, int col) {
		tableEntries.get(row).rowData.set(col, value);
        fireTableCellUpdated(row, col);
    }
	
	public void addRow(String name, String result, String action, int amount) {
		tableEntries.add(new RuleTableEntry(name, result, action, amount));
		this.fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
	}
	
	public void removeRow(int rowIndex) {
		tableEntries.remove(rowIndex);
		this.fireTableRowsDeleted(rowIndex, rowIndex);
	}

}
