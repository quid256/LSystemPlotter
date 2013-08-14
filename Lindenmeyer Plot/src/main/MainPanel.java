package main;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;

import util.ColorPickerComponent;
import util.LActions;
import util.TableButton;
import util.TableButton.IButtonEditorListener;
import calc.CalcProgressPanel;


@SuppressWarnings("serial")
public class MainPanel extends JPanel implements ActionListener, TableModelListener, IButtonEditorListener {
	
	RuleTableDataProvider dataProvider;
	JTable table;
	JFrame plotFrame;
	public JFrame mainFrame;
	PlotPanel plotPanel;
	JProgressBar progressBar, subProgressBar;
	JSpinner iterSpinner;
	private JTextField txtA;
	
	private ColorPickerComponent gradBegColorPicker, gradEndColorPicker;
	
	private JFileChooser fc;
	
	public static void main(String[] args) {
		
		try {
		    UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		JFrame mainFrame = new JFrame();
		try {
			mainFrame.setIconImage(ImageIO.read(MainPanel.class.getClassLoader().getResource("iconPic.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("LindenPlot - Fractal0");
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
		
		
		
		setPreferredSize(new Dimension(530, 325));
		
		dataProvider = new RuleTableDataProvider();
		dataProvider.addTableModelListener(this);
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension(1000, 10));
        panel.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(panel);
        
        table = new JTable(dataProvider);
        table.setCellSelectionEnabled(true);
        table.setPreferredScrollableViewportSize(new Dimension(200, 50));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn actionColumn = table.getColumnModel().getColumn(3);
        TableColumn nameColumn = table.getColumnModel().getColumn(0);
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMaximumSize(new Dimension(1000, 32767));
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
        
        
        nameColumn.setCellRenderer(new TableButton.ButtonRenderer());
        nameColumn.setCellEditor( new TableButton.ButtonEditor(new JCheckBox(), this));
        
        
        
        JPanel mainSubPanel = new JPanel();
        mainSubPanel.setLayout(new BoxLayout(mainSubPanel, BoxLayout.Y_AXIS));
        
		add(mainSubPanel);
		
        JPanel opPanel = new JPanel();
        opPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        opPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        opPanel.setLayout(new BoxLayout(opPanel, BoxLayout.Y_AXIS));
        opPanel.setMaximumSize(new Dimension(150, 500));
        this.add(opPanel);
        
		JButton addBttn = new JButton("Add Identifier");
		addBttn.setAlignmentY(Component.TOP_ALIGNMENT);
		addBttn.setMaximumSize(new Dimension(150, 26));
		addBttn.setVerticalAlignment(SwingConstants.TOP);
		addBttn.setVerticalTextPosition(SwingConstants.TOP);
		addBttn.setActionCommand("AddElement");
		addBttn.addActionListener(this);
		opPanel.add(addBttn);
		
		
		JButton plotBttn = new JButton("Plot");
		plotBttn.setAlignmentY(Component.TOP_ALIGNMENT);
		plotBttn.setMaximumSize(new Dimension(150, 26));
		plotBttn.setActionCommand("Plot");
		plotBttn.addActionListener(this);
		opPanel.add(plotBttn);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		opPanel.add(verticalStrut);
		
		gradBegColorPicker = new ColorPickerComponent(Color.BLACK);
		gradBegColorPicker.setAlignmentY(Component.TOP_ALIGNMENT);
		gradBegColorPicker.setPreferredSize(new Dimension(10, 20));
		gradBegColorPicker.setBounds(0, 0, 50, 30);
		gradBegColorPicker.setMaximumSize(new Dimension(150, 20));
		gradBegColorPicker.setText("Beginning Color");
		opPanel.add(gradBegColorPicker);
		
		Component verticalStrut_2 = Box.createVerticalStrut(10);
		opPanel.add(verticalStrut_2);
		
		gradEndColorPicker = new ColorPickerComponent(Color.BLACK);
		gradEndColorPicker.setAlignmentY(Component.TOP_ALIGNMENT);
		gradEndColorPicker.setText("Ending Color");
		gradEndColorPicker.setPreferredSize(new Dimension(150, 20));
		gradEndColorPicker.setMaximumSize(new Dimension(150, 20));
		opPanel.add(gradEndColorPicker);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		opPanel.add(verticalStrut_1);
		
		JLabel lblIterations = new JLabel("Iterations:");
		lblIterations.setAlignmentY(Component.TOP_ALIGNMENT);
		opPanel.add(lblIterations);
		
		iterSpinner = new JSpinner();
		iterSpinner.setModel(new SpinnerNumberModel(5, 1, 255, 1));
		iterSpinner.setAlignmentY(Component.TOP_ALIGNMENT);
		iterSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
		iterSpinner.setMaximumSize(new Dimension(150, 35));
		opPanel.add(iterSpinner);
		
		JLabel lblStartingString = new JLabel("Starting String:");
		opPanel.add(lblStartingString);
		
		txtA = new JTextField();
		txtA.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtA.setText("a");
		txtA.setAlignmentY(Component.TOP_ALIGNMENT);
		txtA.setMaximumSize(new Dimension(150, 27));
		opPanel.add(txtA);
		
		Component verticalStrut_3 = Box.createVerticalStrut(10);
		opPanel.add(verticalStrut_3);
		
		JButton saveBttn = new JButton("Save");
		saveBttn.setActionCommand("Save");
		saveBttn.addActionListener(this);
		saveBttn.setMaximumSize(new Dimension(150, 26));
		saveBttn.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		opPanel.add(saveBttn);
		
		JButton loadBttn = new JButton("Load");
		loadBttn.setActionCommand("Load");
		loadBttn.addActionListener(this);
		loadBttn.setMaximumSize(new Dimension(150, 26));
		loadBttn.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		opPanel.add(loadBttn);
		
		JButton helpBttn = new JButton("Help");
		helpBttn.addActionListener(this);
		helpBttn.setMaximumSize(new Dimension(150, 26));
		helpBttn.setAlignmentY(1.0f);
		helpBttn.setActionCommand("Help");
		opPanel.add(helpBttn);
		
		
		fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(new FileNameExtensionFilter("LindenPlot Data Format (*.lpdf)", "lpdf"));
		
/*		plotFrame = new JFrame("Fractal Plot");
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
		plotFrame.setResizable(false);*/
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String aC = ae.getActionCommand();
		//System.out.println(ae.getActionCommand());
		
		if (aC == "AddElement") {
			dataProvider.addRow("a", "a+a", LActions.FORWARD.toString(), 1);
		} else if (aC == "Help") {
			JFrame helpFrame = new JFrame("Help");
			helpFrame.setResizable(false);
			helpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			helpFrame.setLocationRelativeTo(null);
			//helpFrame.setSize(new Dimension(530, 235));
			try {
				helpFrame.setIconImage(ImageIO.read(MainPanel.class.getClassLoader().getResource("iconPic.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			HelpPanel p = new HelpPanel();
			System.out.println("ll");
			helpFrame.getContentPane().add(p);
			System.out.println("ll");
			helpFrame.pack();
			System.out.println("ll");
			helpFrame.setVisible(true);
			
		} else if (aC == "Plot") {
			JFrame progressFrame = new JFrame("Constructing L-System...");
			progressFrame.setResizable(false);
			progressFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			progressFrame.setLocationRelativeTo(null);
			try {
				progressFrame.setIconImage(ImageIO.read(MainPanel.class.getClassLoader().getResource("iconPic.png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			CalcProgressPanel p = new CalcProgressPanel(progressFrame, txtA.getText(), dataProvider, (Integer)iterSpinner.getValue(), gradBegColorPicker.getColor(), gradEndColorPicker.getColor());
			progressFrame.getContentPane().add(p);
			progressFrame.pack();
			progressFrame.setVisible(true);
			
		} else if (aC == "Save") {
			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File sf = fc.getSelectedFile();
				
				if (!sf.getName().endsWith(".lpdf")) {
					sf = new File(sf + ".lpdf");
				}
				
				
				
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter(sf));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String content = "";
				
				Color begcolor = gradBegColorPicker.getColor();
				Color endcolor = gradEndColorPicker.getColor();
			
				content += Integer.toString(begcolor.getRed());
				content += " "+Integer.toString(begcolor.getGreen());
				content += " "+Integer.toString(begcolor.getBlue());
				
				content += " "+Integer.toString(endcolor.getRed());
				content += " "+Integer.toString(endcolor.getGreen());
				content += " "+Integer.toString(endcolor.getBlue());
				
				content += " " + Integer.toString((int)iterSpinner.getValue());
				
				content += " " + txtA.getText();
				
				for (int i = 0; i < dataProvider.getRowCount(); i++) {

					content += " " + (String)dataProvider.getValueAt(i, 1) + " ";
					content += (String)dataProvider.getValueAt(i, 2) + " ";
					
					for (int acID = 0; acID < LActions.values().length; acID++) {
						if (LActions.values()[acID].toString().equals((String)dataProvider.getValueAt(i,3))) {
							content += Integer.toString(acID);
							break;
						}
					}
					
					content += " " + (Integer)dataProvider.getValueAt(i, 4);
					
				}

				
				try {
					writer.write(content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.mainFrame.setTitle("LindenPlot - " + sf.getName());
			}
		} else if (aC == "Load") {
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File of = fc.getSelectedFile();
				
				if (of == null) {
					return;
				}
				
				BufferedReader r = null;
				
				try {
					r = new BufferedReader(new FileReader(of));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String content = "";
				try {
					content = r.readLine();
					r.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String[] data = content.split(" ");
				
				Color begColor = new Color(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
				gradBegColorPicker.setColor(begColor);
				gradBegColorPicker.repaint();
				
				Color endColor = new Color(Integer.parseInt(data[3]),Integer.parseInt(data[4]),Integer.parseInt(data[5]));
				gradEndColorPicker.setColor(endColor);
				gradEndColorPicker.repaint();
				
				iterSpinner.setValue(Integer.parseInt(data[6]));
				
				
				
				txtA.setText(data[7]);
				
				dataProvider.clearAll();
				
				for (int i = 8; i < data.length; i++) {
					dataProvider.addRow(data[i], data[i + 1], LActions.values()[Integer.parseInt(data[i + 2])].toString(), Integer.parseInt(data[i + 3]));
					i += 3;
				}
				
				this.mainFrame.setTitle("LindenPlot - " + of.getName());
				
			}
		}
	}

	@Override
	public void tableChanged(TableModelEvent arg0) {
		if (arg0.getType() == TableModelEvent.UPDATE) {
			if (arg0.getColumn() == 1) {
				int col = arg0.getColumn();
				int row = arg0.getFirstRow();
				String oldval = (String)dataProvider.getValueAt(row, col);
				if (oldval.length() > 1) {
					dataProvider.setValueAt(oldval.substring(0, 1), row, col);
				}
			} else if (arg0.getColumn() == 3) {
				int col = arg0.getColumn();
				int row = arg0.getFirstRow();
				String curVal = (String)dataProvider.getValueAt(row, col);
				if (curVal.equals(LActions.NOTHING.toString()) || curVal.equals(LActions.POPSTATE.toString()) || curVal.equals(LActions.PUSHSTATE.toString())) {
					dataProvider.setValueAt(-1, row, 4);
				} else if (curVal.equals(LActions.ROTATECC.toString()) || curVal.equals(LActions.ROTATECW.toString())) {
					dataProvider.setValueAt(90, row, 4);
				} else if (curVal.equals(LActions.FORWARD.toString())) {
					dataProvider.setValueAt(1, row, 4);
				}
			}
		}
		
	}

	@Override
	public void buttonEditorPressed(int assocRowID) {
		dataProvider.removeRow(assocRowID);
		
	}

	
	

}
