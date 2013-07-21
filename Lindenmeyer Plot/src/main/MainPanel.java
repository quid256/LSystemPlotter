package main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
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
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener, TableModelListener {
	
	RuleTableDataProvider dataProvider;
	JTable table;
	JFrame plotFrame;
	public JFrame mainFrame;
	PlotPanel plotPanel;
	JProgressBar progressBar, subProgressBar;
	JSpinner iterSpinner;
	private JTextField textField;
	
	private ColorPickerComponent gradBegColorPicker, gradEndColorPicker;
	
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("L-System Fractal Plotter");
		mainFrame.getContentPane().setLayout(new GridLayout(1, 1, 0, 0));
		
		MainPanel mP = new MainPanel();
		mainFrame.getContentPane().add(mP);
		mP.mainFrame = mainFrame;
		
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		

	}
	
	public MainPanel() {
		setAlignmentY(Component.TOP_ALIGNMENT);
		setAlignmentX(Component.LEFT_ALIGNMENT);
		
		
		
		setPreferredSize(new Dimension(640, 250));
		
		dataProvider = new RuleTableDataProvider();
		dataProvider.addTableModelListener(this);
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        JPanel panel = new JPanel();
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(panel);
        
        table = new JTable(dataProvider);
        table.setPreferredScrollableViewportSize(new Dimension(200, 50));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn actionColumn = table.getColumnModel().getColumn(2);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        
       /* JTable opTable = new JTable(configDataProvider);
        opTable.setPreferredScrollableViewportSize(new Dimension(200, 100));
        opTable.setFillsViewportHeight(true);
        opTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane_1 = new JScrollPane(opTable);
        panel.add(scrollPane_1);*/
        
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem(LActions.FORWARD.toString());
        comboBox.addItem(LActions.NOTHING.toString());
        comboBox.addItem(LActions.ROTATECW.toString());
        comboBox.addItem(LActions.ROTATECC.toString());
        comboBox.addItem(LActions.PUSHSTATE.toString());
        comboBox.addItem(LActions.POPSTATE.toString());
        
        actionColumn.setCellEditor(new DefaultCellEditor(comboBox));
        
        
        JPanel mainSubPanel = new JPanel();
        mainSubPanel.setLayout(new BoxLayout(mainSubPanel, BoxLayout.Y_AXIS));
        
		add(mainSubPanel);
		
        JPanel opPanel = new JPanel();
        opPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        opPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        opPanel.setLayout(new BoxLayout(opPanel, BoxLayout.Y_AXIS));
        opPanel.setMaximumSize(new Dimension(20, 500));
        this.add(opPanel);
        
		JButton addBttn = new JButton("Add Row");
		addBttn.setAlignmentY(Component.TOP_ALIGNMENT);
		addBttn.setMaximumSize(new Dimension(150, 26));
		addBttn.setVerticalAlignment(SwingConstants.TOP);
		addBttn.setVerticalTextPosition(SwingConstants.TOP);
		addBttn.setActionCommand("AddElement");
		addBttn.addActionListener(this);
		opPanel.add(addBttn);
		
		JButton remBttn = new JButton("Remove Row");
		remBttn.setAlignmentY(Component.TOP_ALIGNMENT);
		remBttn.setMaximumSize(new Dimension(150, 26));
		remBttn.setActionCommand("remElement");
		remBttn.addActionListener(this);
		opPanel.add(remBttn);
		
		
		JButton plotBttn = new JButton("Plot");
		plotBttn.setAlignmentY(Component.TOP_ALIGNMENT);
		plotBttn.setMaximumSize(new Dimension(150, 26));
		plotBttn.setActionCommand("Plot");
		plotBttn.addActionListener(this);
		opPanel.add(plotBttn);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setMaximumSize(new Dimension(150, 15));
		opPanel.add(verticalStrut);
		
		gradBegColorPicker = new ColorPickerComponent(Color.BLACK);
		gradBegColorPicker.setAlignmentY(Component.TOP_ALIGNMENT);
		gradBegColorPicker.setPreferredSize(new Dimension(10, 0));
		gradBegColorPicker.setBounds(0, 0, 50, 30);
		gradBegColorPicker.setMaximumSize(new Dimension(150, 20));
		gradBegColorPicker.setText("Beginning Color");
		opPanel.add(gradBegColorPicker);
		
		gradEndColorPicker = new ColorPickerComponent(Color.BLACK);
		gradEndColorPicker.setAlignmentY(Component.TOP_ALIGNMENT);
		gradEndColorPicker.setText("Ending Color");
		gradEndColorPicker.setPreferredSize(new Dimension(10, 0));
		gradEndColorPicker.setMaximumSize(new Dimension(150, 20));
		opPanel.add(gradEndColorPicker);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setMaximumSize(new Dimension(150, 15));
		opPanel.add(verticalStrut_1);
		
		JLabel lblIterations = new JLabel("Iterations:");
		lblIterations.setAlignmentY(Component.TOP_ALIGNMENT);
		opPanel.add(lblIterations);
		
		iterSpinner = new JSpinner();
		iterSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		iterSpinner.setAlignmentY(Component.TOP_ALIGNMENT);
		iterSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
		iterSpinner.setPreferredSize(new Dimension(150, 20));
		iterSpinner.setMaximumSize(new Dimension(150, 20));
		opPanel.add(iterSpinner);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		verticalStrut_2.setMaximumSize(new Dimension(150, 15));
		opPanel.add(verticalStrut_2);
		
		JLabel lblStartingString = new JLabel("Starting String:");
		opPanel.add(lblStartingString);
		
		textField = new JTextField();
		textField.setAlignmentY(Component.TOP_ALIGNMENT);
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		textField.setMaximumSize(new Dimension(150, 20));
		opPanel.add(textField);
		
		
		plotFrame = new JFrame("Fractal Plot");
		//plotFrame.getContentPane().setLayout(new BoxLayout(plotFrame, BoxLayout.Y_AXIS));
		
		plotPanel = new PlotPanel();
		
		JButton closeButton = new JButton("Save");
		closeButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		closeButton.setActionCommand("saveFile");
		closeButton.addActionListener(this);
		plotPanel.add(closeButton);
		
		JLabel coords = new JLabel("0, 0");
		plotPanel.coordsLabel = coords;
		plotPanel.add(coords);
		
		plotFrame.getContentPane().add(plotPanel);
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
			CalcProgressPanel p = new CalcProgressPanel(progressFrame, textField.getText(), dataProvider, (Integer)iterSpinner.getValue(), gradBegColorPicker.getColor(), gradEndColorPicker.getColor());
			progressFrame.getContentPane().add(p);
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
