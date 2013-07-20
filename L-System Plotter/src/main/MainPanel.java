package main;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;


@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener, TableModelListener {
	
	RuleTableDataProvider dataProvider;
	ConfigTableDataProvider configDataProvider;
	JTable table;
	JFrame plotFrame;
	public JFrame mainFrame;
	PlotPanel plotPanel;
	JProgressBar progressBar, subProgressBar;
	
	
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("L-System Fractal Plotter");
		mainFrame.setLayout(new GridLayout(1, 1, 0, 0));
		
		MainPanel mP = new MainPanel();
		mainFrame.add(mP);
		mP.mainFrame = mainFrame;
		
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	
	public MainPanel() {
		
		
		
		setPreferredSize(new Dimension(640, 480));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		dataProvider = new RuleTableDataProvider();
		dataProvider.addTableModelListener(this);
		
		table = new JTable(dataProvider);
        table.setPreferredScrollableViewportSize(new Dimension(200, 300));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn actionColumn = table.getColumnModel().getColumn(2);
        
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem(LActions.FORWARD.toString());
        comboBox.addItem(LActions.NOTHING.toString());
        comboBox.addItem(LActions.ROTATECW.toString());
        comboBox.addItem(LActions.ROTATECC.toString());
        comboBox.addItem(LActions.PUSHSTATE.toString());
        comboBox.addItem(LActions.POPSTATE.toString());
        
        actionColumn.setCellEditor(new DefaultCellEditor(comboBox));
        
        configDataProvider = new ConfigTableDataProvider();
        
        JTable opTable = new JTable(configDataProvider);
        opTable.setPreferredScrollableViewportSize(new Dimension(200, 100));
        opTable.setFillsViewportHeight(true);
        opTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
        JPanel mainSubPanel = new JPanel();
        mainSubPanel.setLayout(new BoxLayout(mainSubPanel, BoxLayout.Y_AXIS));
        mainSubPanel.add(new JScrollPane(table));
		mainSubPanel.add(new JScrollPane(opTable));
        
		add(mainSubPanel);
		
        JPanel opPanel = new JPanel();
        opPanel.setLayout(new BoxLayout(opPanel, BoxLayout.Y_AXIS));
        this.add(opPanel);
        
		JButton addBttn = new JButton("Add Row");
		addBttn.setActionCommand("AddElement");
		addBttn.addActionListener(this);
		opPanel.add(addBttn);
		
		JButton remBttn = new JButton("Remove Row");
		remBttn.setActionCommand("remElement");
		remBttn.addActionListener(this);
		opPanel.add(remBttn);
		
		
		JButton plotBttn = new JButton("Plot");
		plotBttn.setActionCommand("Plot");
		plotBttn.addActionListener(this);
		opPanel.add(plotBttn);
		
		
		
		
		plotFrame = new JFrame("Fractal Plot");
		//plotFrame.setLayout(new BoxLayout(plotFrame, BoxLayout.Y_AXIS));
		
		plotPanel = new PlotPanel();
		
		JButton closeButton = new JButton("Save");
		closeButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		closeButton.setActionCommand("saveFile");
		closeButton.addActionListener(this);
		plotPanel.add(closeButton);
		
		JLabel coords = new JLabel("0, 0");
		plotPanel.coordsLabel = coords;
		plotPanel.add(coords);
		
		plotFrame.add(plotPanel);
		//plotFrame.add(closeButton);
		plotFrame.setSize(500, 500);
		plotFrame.pack();
		plotFrame.setResizable(false);
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String aC = ae.getActionCommand();
		
		if (aC == "AddElement") {
			dataProvider.addRow("a", "a+a", LActions.FORWARD.toString(), 1);
		} else if (aC == "remElement") {
			if (table.getSelectedRow() != -1) {
				dataProvider.removeRow(table.getSelectedRow());
			}
		} else if (aC == "Plot") {
			JFrame progressFrame = new JFrame("Constructing L-System...");
			CalcProgressPanel p = new CalcProgressPanel(progressFrame, "a", dataProvider, (Integer)configDataProvider.getValueAt(0, 1) );
			progressFrame.add(p);
			progressFrame.pack();
			progressFrame.setVisible(true);
			
		} else if (aC == "saveFile") {
			plotPanel.savePic();
			
		}
		
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
		if (arg0.getType() == TableModelEvent.UPDATE) {
			if (arg0.getColumn() == 0) {
				int col = arg0.getColumn();
				int row = arg0.getFirstRow();
				String oldval = (String)dataProvider.getValueAt(row, col);
				if (oldval.length() > 1) {
					dataProvider.setValueAt(oldval.substring(0, 1), row, col);
				}
			} else if (arg0.getColumn() == 2) {
				int col = arg0.getColumn();
				int row = arg0.getFirstRow();
				String curVal = (String)dataProvider.getValueAt(row, col);
				if (curVal.equals(LActions.NOTHING.toString()) || curVal.equals(LActions.POPSTATE.toString()) || curVal.equals(LActions.PUSHSTATE.toString())) {
					dataProvider.setValueAt(-1, row, 3);
				} else if (curVal.equals(LActions.ROTATECC.toString()) || curVal.equals(LActions.ROTATECW.toString())) {
					dataProvider.setValueAt(90, row, 3);
				} else if (curVal.equals(LActions.FORWARD.toString())) {
					dataProvider.setValueAt(1, row, 3);
				}
			}
		}
		
	}

	
	

}
