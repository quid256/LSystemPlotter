package main;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import util.LActions;
import util.TableButton;


@SuppressWarnings("serial")
public class RuleTableDataProvider extends AbstractTableModel {
	
	private class RuleTableEntry {
		
		public TableButton.TableButtonData bData;
		private ArrayList<Object> rowData = new ArrayList<Object>();
		public RuleTableEntry(String name, String result, String action, int amount, int rowID) {
			this.bData = new TableButton.TableButtonData("DEL", rowID);
			//System.out.println(rowID);
			rowData.add(bData);
			rowData.add(name);
			rowData.add(result);
			rowData.add(action);
			rowData.add(amount);
		}
		
		public String toString() {
			return "RTE " + rowData.toString();
		}
		
		public ArrayList<Object> getRowData() { return rowData; }
	}
	
	private ArrayList<String> colNames = new ArrayList<String>();
	//private LinkedHashMap<Integer, RuleTableEntry> tableEntries = new LinkedHashMap<Integer, RuleTableEntry>();
	
	private ArrayList<RuleTableEntry> tableEntries = new ArrayList<RuleTableEntry>();
	
	int curIndex = 0;
	
	public RuleTableDataProvider() {
		colNames.add("Remove");
		colNames.add("Identifier");
		colNames.add("Result");
		colNames.add("Action");
		colNames.add("Amount");
		
		addTableEntry("+", "+", LActions.ROTATECW.toString(), 90);
		addTableEntry("-", "-", LActions.ROTATECC.toString(), 90);
		addTableEntry("[", "[", LActions.PUSHSTATE.toString(), -1);
		addTableEntry("]", "]", LActions.POPSTATE.toString(), -1);
		addTableEntry("a", "a+b", LActions.FORWARD.toString(), 5);
		addTableEntry("b", "a-b", LActions.FORWARD.toString(), 5);
	}
	
	private void addTableEntry(String name, String result, String action, int amount) {
		
		tableEntries.add(new RuleTableEntry(name, result, action, amount, curIndex++));
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
		if (col == 4) {
			String strVal = (String)getValueAt(row, 3);
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
		//System.out.println("setValueAt "+ row +", " + col + " to " + value.toString());
		if (col > 0) {
			tableEntries.get(row).getRowData().set(col, value);
	        fireTableCellUpdated(row, col);
		}
    }
	
	public void addRow(String name, String result, String action, int amount) {
		addTableEntry(name, result, action, amount);
		this.fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
	}
	
	public void removeRow(int rowID) {
		
		for (int i = 0; i < tableEntries.size(); i++) {
			//System.out.println("tableEntries["+ i + "].bData.rowID = " + tableEntries.get(i).bData.rowID);
			if (tableEntries.get(i).bData.rowID == rowID) {
				tableEntries.remove(tableEntries.get(i));
				this.fireTableRowsDeleted(i, i);
				break;
			}
		}
	}

	public void clearAll() {
		int oldSize = this.getRowCount();
		tableEntries.clear();
		
		this.fireTableRowsDeleted(0, oldSize);
		
	}

}
