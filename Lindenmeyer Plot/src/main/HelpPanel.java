package main;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class HelpPanel extends JPanel{
	public HelpPanel() {
		
		JTextPane jtp = new JTextPane();
		jtp.setEditable(false);
		jtp.setContentType("text/html");
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("HelpFile.html")));
		String fullText = "";
		
		
		String partText;
		try {
			while ( (partText = br.readLine()) != null) {
				fullText = fullText.concat(partText + "\n");
			}
		} catch (IOException e1) {
			System.out.println("Unable to load help file");
		}
		
		
		jtp.setText(fullText);
		jtp.setCaretPosition(0);
		JScrollPane jsp = new JScrollPane(jtp);
	
		jsp.setMaximumSize(new Dimension(200, 100));
		jsp.setPreferredSize(new Dimension(530, 325));
		add(jsp);
		
	}
}
