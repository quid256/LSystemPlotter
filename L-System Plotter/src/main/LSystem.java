package main;

import java.util.HashMap;

public class LSystem {
	
	public static String getIteratedString(RuleTableDataProvider dataProvider, int iterations, String initial) throws UnknownCharacterException {
		HashMap<String, String> mapping = new HashMap<String, String>();
		for (int i = 0; i < dataProvider.getRowCount(); i++) {
			mapping.put((String)dataProvider.getValueAt(i, 0), (String)dataProvider.getValueAt(i, 1));
		}
		String curIter = initial;
		for (int i = 0; i < iterations; i++) {
			int curIndex = curIter.length() - 1;
			while (curIndex >= 0) {
				String curChar = curIter.substring(curIndex, curIndex + 1);
				if (mapping.containsKey(curChar)) {
					curIter = curIter.substring(0, curIndex) + mapping.get(curIter.substring(curIndex, curIndex + 1)) + curIter.substring(curIndex + 1);
					curIndex--;
				} else {
					throw new UnknownCharacterException(curChar);
				}
			}
		}
		
		return curIter;
	}

}
