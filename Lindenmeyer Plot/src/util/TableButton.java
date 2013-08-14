package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public abstract class TableButton {
	@SuppressWarnings("serial")
	public static class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(Color.red);
				setBackground(UIManager.getColor("Button.background"));
			}
			
			
			TableButtonData bData = (TableButtonData)value;
			
			setText(bData.name);
		
			//setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	/**
	 * @version 1.0 11/09/98
	 */

	@SuppressWarnings("serial")
	public static class ButtonEditor extends DefaultCellEditor {
		protected JButton button;

		private boolean isPushed;
		
		private TableButtonData bData;

		private IButtonEditorListener beListener;
		
		public ButtonEditor(JCheckBox checkBox, IButtonEditorListener beListener) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
			
			this.beListener = beListener;
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			
			
			
			bData = (TableButtonData)value;
			button.setText(bData.name);
			isPushed = true;
			return button;
		}

		public Object getCellEditorValue() {
			
			if (isPushed) {

				beListener.buttonEditorPressed(bData.rowID);
				//JOptionPane.showMessageDialog(button, label + ": Ouch!");
			}
			isPushed = false;
			return bData;
		}

		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}
	}
	
	
	public static interface IButtonEditorListener {
		public void buttonEditorPressed(int assocRowID);
	}
	
	public static class TableButtonData {
		public String name;
		public int rowID;
		
		public TableButtonData(String name, int rowID) {
			this.name=  name;
			this.rowID = rowID;
			//System.out.println(this.rowID);
		}
		
		public String toString() {
			return "TableButtonData [name = " + name + ", rowID = " + rowID + "]";
		}
	}

}
