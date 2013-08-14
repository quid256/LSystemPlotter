package calc;

import java.util.HashMap;

import javax.swing.SwingWorker;

import util.LActions;

import main.RuleTableDataProvider;


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
			String identifier = (String)dataProvider.getValueAt(i, 1);
			if (!mapping.containsKey(identifier)) {
				
				mapping.put(identifier, (String)dataProvider.getValueAt(i, 2));
				acmapping.put(identifier, (String)dataProvider.getValueAt(i, 3));
			} else {
				this.listen.calculatorError(new Exception("Multiple results for identifier: " + (String)dataProvider.getValueAt(i, 1)));
				return null;
			}
		}
		
		for (String identifier : mapping.keySet()) {
			int diffVal;
			if (acmapping.get(identifier) == LActions.POPSTATE.toString()) {
				diffVal = 1;
			} else if (acmapping.get(identifier) == LActions.PUSHSTATE.toString()) {
				diffVal = -1;
			} else {
				diffVal = 0;
			}
			
			String outputChar = mapping.get(identifier);
			int realDiffVal = 0;
			for (int i = 0; i < outputChar.length(); i++) {
				if (acmapping.get(outputChar.substring(i, i + 1)) == LActions.POPSTATE.toString()) {
					realDiffVal++;
				} else if (acmapping.get(outputChar.substring(i, i + 1)) == LActions.PUSHSTATE.toString()) {
					realDiffVal--;
				}
			}
			
			if (realDiffVal != diffVal) {
				if (diffVal == 1) {
					this.listen.calculatorError(new Exception("identifier '" + identifier + "' needs to have 1 more pop than push character"));
				} else if (diffVal == -1) {
					this.listen.calculatorError(new Exception("identifier '" + identifier + "' needs to have 1 more pop than push character"));
				} else {
					this.listen.calculatorError(new Exception("identifier '" + identifier + "' needs to have the same number of pop and push characters"));
				}
				return null;
			}
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
