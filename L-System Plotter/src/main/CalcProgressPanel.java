package main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class CalcProgressPanel extends JPanel implements CalculatorProgressListener {
	private JProgressBar progressBar, subProgressBar;
	private RuleTableDataProvider dataProvider;
	private JFrame parent;
	
	public CalcProgressPanel(JFrame parent, String initString, RuleTableDataProvider dataProvider, int iterations) {
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
		
		
		new LSystemCalculator(initString, dataProvider, iterations, this).execute();
	}

	@Override
	public void calculatorFinish(String pattern, int segLength) {
		//System.out.println("finished");
		PlotPanel p = new PlotPanel();
		p.clearImg();
		p.load(pattern, segLength, dataProvider);
		
		JFrame plotFrame = new JFrame("Fractal Plot");
		plotFrame.add(p);
		plotFrame.pack();
		plotFrame.setVisible(true);
		
		System.out.println(parent);
		
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
}
