package main;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class CalcProgressPanel extends JPanel implements CalculatorProgressListener {
	private JProgressBar progressBar, subProgressBar;
	private RuleTableDataProvider dataProvider;
	private JFrame parent;
	private Color gradBegCol, gradEndCol;
	
	public CalcProgressPanel(JFrame parent, String initString, RuleTableDataProvider dataProvider, int iterations, Color gradBegCol, Color gradEndCol) {
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		add(progressBar);
		
		subProgressBar = new JProgressBar(0, 100);
		subProgressBar.setValue(0);
		subProgressBar.setStringPainted(true);
		add(subProgressBar);
		
		this.dataProvider = dataProvider;
		this.parent = parent;
		
		this.gradBegCol = gradBegCol;
		this.gradEndCol = gradEndCol;
		
		new LSystemCalculator(initString, dataProvider, iterations, this).execute();
	}

	@Override
	public void calculatorFinish(String pattern, int segLength) {
		//System.out.println("finished");
		PlotPanel p = new PlotPanel();
		p.clearImg();
		p.load(pattern, segLength, dataProvider, gradBegCol, gradEndCol);
		
		JFrame plotFrame = new JFrame("Fractal Plot");
		plotFrame.add(p);
		plotFrame.pack();
		plotFrame.setVisible(true);
		
		parent.dispose();
		
	}

	@Override
	public void calculatorIterDone(double progress) {
		progressBar.setValue((int) (100 * progress));
		
	}

	@Override
	public void calculatorCharDone(double progress) {
		subProgressBar.setValue((int) (100 * progress));
		
	}

	@Override
	public void calculatorError(Exception e) {
		parent.dispose();
		JOptionPane.showMessageDialog(this, e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
		
	}
}
