package main;

import java.util.HashMap;

import javax.swing.SwingWorker;

public class LSystemCalculator extends SwingWorker<Void, Void>{

	RuleTableDataProvider dataProvider;
	String initString;
	public int iterations;
	CalculatorProgressListener listen;
	
	public LSystemCalculator(String initString, RuleTableDataProvider dataProvider, int iterations, CalculatorProgressListener listen) {
		this.initString = initString;
		this.dataProvider = dataProvider;
		this.iterations = iterations;
		this.listen = listen;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		HashMap<String, String> mapping = new HashMap<String, String>();
		HashMap<String, String> acmapping = new HashMap<String, String>();
		for (int i = 0; i < dataProvider.getRowCount(); i++) {
			mapping.put((String)dataProvider.getValueAt(i, 0), (String)dataProvider.getValueAt(i, 1));
			acmapping.put((String)dataProvider.getValueAt(i, 0), (String)dataProvider.getValueAt(i, 2));
		}
		String curIter = initString;
		if (curIter.length() < 1) {
			this.listen.calculatorError(new Exception("Starting string needs to be longer than 0 characters!"));
			return null;
		}
		int progress = 0;
		setProgress(0);
		for (int i = 0; i < iterations; i++) {
			int totLength = curIter.length();
			int curIndex = curIter.length() - 1;
			int curCharProgress = 0;
			while (curIndex >= 0) {
				String curChar = curIter.substring(curIndex, curIndex + 1);
				if (mapping.containsKey(curChar)) {
					curIter = curIter.substring(0, curIndex) + mapping.get(curIter.substring(curIndex, curIndex + 1)) + curIter.substring(curIndex + 1);
					curIndex--;
					curCharProgress += 1;
					this.listen.calculatorCharDone(curCharProgress * 1.0 / totLength);
				} else {
					this.listen.calculatorError(new Exception("Unidentified Character Found: " + curChar));
					return null;
				}
			}
			progress += 1;
			this.listen.calculatorIterDone(progress * 1.0 / iterations);
		}
		
		
		this.listen.calculatorFinish(curIter, getLongestLength(curIter, 0, acmapping));
		return null;
	}
	
	
	protected int getLongestLength(String iterString, int startIndex, HashMap<String, String> strActions) {
		int totLineLength = 0;
		int longestLength = 0;
		int totStrLength = iterString.length();
		
		for (int i = startIndex; i < totStrLength; i++) {
			String action = strActions.get(iterString.substring(i, i+1));
			
			if (action == LActions.PUSHSTATE.toString()) {
				int subLength = getLongestLength(iterString, i + 1, strActions);
				if (longestLength - totLineLength < subLength)
					longestLength = subLength + totLineLength;
				int depth = 1;

				while (depth != 0) {
					i++;
					if (strActions.get(iterString.substring(i, i + 1)) == LActions.POPSTATE.toString()) {
						depth--;
					} else
					if (strActions.get(iterString.substring(i, i + 1)) == LActions.PUSHSTATE.toString()) {
						depth++;
					}
				}
				System.out.println(i);
			} else
			if (action == LActions.POPSTATE.toString()) {
				return longestLength;
			} else
			if (action == LActions.FORWARD.toString()) {
				totLineLength++;
				longestLength = Math.max(totLineLength, longestLength);
			}
		}
		
		return longestLength; //should only happen in first call
	}
	
	
}
