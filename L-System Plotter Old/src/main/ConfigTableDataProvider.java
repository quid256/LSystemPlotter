package main;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ConfigTableDataProvider extends AbstractTableModel {

	ArrayList<Object> values = new ArrayList<Object>();
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<Class> types = new ArrayList<Class>();
	int rowCount = 0;
	
	private void addOption(String name, Object value, Class type) {
		names.add(name);
		values.add(value);
		types.add(type);
		rowCount++;
	}
	
	public ConfigTableDataProvider() {
		addOption("Iterations", 3, Integer.class);
		
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rowCount;
	}

	@Override
	public String getColumnName(int col) {
		if (col == 0) {
			return "Name";
		} else {
			return "Value";
		}
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		if (col == 0) {
			return names.get(row);
		} else {
			return values.get(row);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int col) {
		if (col == 0) {
			return String.class;
		} else {
			return Integer.class;
		}
    }
	
	public void setValueAt(Object value, int row, int col) {
		
		values.set(row, value);
        fireTableCellUpdated(row, col);
    }
	
	public boolean isCellEditable(int row, int col) {
		return col == 1;
    }

}
