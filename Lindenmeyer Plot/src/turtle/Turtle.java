package turtle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Stack;

import util.LActions;


import main.RuleTableDataProvider;

@SuppressWarnings("serial")
public class Turtle extends Point2D.Double {
	public double angle;
	public Stack<TurtleState> prevStates;
	private Graphics g;
	public int segPos = 0;
	private Color gradBegCol;
	private Color gradEndCol;
	
	public Turtle(double x, double y, Graphics g) {
		super(x, y);
		this.g = g;
		prevStates = new Stack<TurtleState>();
	}
	
	public void forward(double distance, double colorIndex) {
		try {
		g.setColor(gradient(gradBegCol, gradEndCol, colorIndex));
		} catch (Exception e){
			System.out.println("FAILURE!");
			e.printStackTrace();
		}
		if (angle % 90 == 0) {
			//System.out.println("90s");
			//System.out.println(angle);
			if (angle == 0.0) {
				//System.out.println(0);
				this.lineTo(x + distance, y);
			} else if (angle == 90.0) {
				//System.out.println(90);
				this.lineTo(x, y + distance);
			} else if (angle == 180.0) {
				//System.out.println(180);
				this.lineTo(x - distance, y);
			} else if (angle == 270.0) {
				//System.out.println(270);
				this.lineTo(x, y - distance);
			}
			
/*			double newx = x + Math.round(Math.cos(angle * Math.PI / 180)) * distance;
			double newy = y + Math.round(Math.sin(angle * Math.PI / 180)) * distance;
			
			
			this.lineTo(newx, newy);*/
		} else {
			double newx = x + Math.cos(angle * Math.PI / 180) * distance;
			double newy = y + Math.sin(angle * Math.PI / 180) * distance;
			
			
			this.lineTo(newx, newy);
		}
	}
	
	public void turnCW(double amount) {
		angle = (angle + amount) % 360;
	}
	
	public void turnCC(double amount) {
		angle = ((angle - amount) + 360) % 360;
	}
	
	public void setAngle(double angle) {
		this.angle =angle;
	}
	
	public void pushState() {
		//System.out.println("pushing");
		prevStates.push(new TurtleState(x, y, angle, segPos));
		//System.out.println("pushed");
	}
	
	public void popState() {
		//System.out.println("popping");
		TurtleState lastTS = prevStates.pop();
		x = lastTS.x;
		y = lastTS.y;
		segPos = lastTS.distTraveled;
		angle = lastTS.angle;
	}
	
	public void moveTo(Point2D.Double newPos) {
		this.x = newPos.x;
		this.y = newPos.y;
	}
	
	public void moveTo(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void lineTo(Point2D.Double newPos) {
		this.lineTo(newPos.x, newPos.y);
	}
	
	public void lineTo(double x, double y) {
		int thisintx = (int)Math.round(this.x);
		int thisinty = (int)Math.round(this.y);
		int intx = (int)Math.round(x);
		int inty = (int)Math.round(y);
		g.drawLine(thisintx, thisinty, intx, inty);
		this.x = x;
		this.y = y;
	}
	
	public void drawFromInstructions(String instructions, int segLength, RuleTableDataProvider dataProvider) {
		System.out.println("Drawing from: " + instructions);
		System.out.println("segLength: " + segLength);
		
		HashMap<Character, String> instructionMapping = new HashMap<Character, String>();
		HashMap<Character, Integer> amountMapping = new HashMap<Character, Integer>();
		for (int row = 0; row < dataProvider.getRowCount(); ++row) {
			instructionMapping.put(((String)dataProvider.getValueAt(row, 0)).charAt(0), (String)dataProvider.getValueAt(row, 2));
			amountMapping.put(((String)dataProvider.getValueAt(row, 0)).charAt(0), (Integer)dataProvider.getValueAt(row, 3));
		}
		
		for (int i = 0; i < instructions.length(); ++i) {
			String action = instructionMapping.get(instructions.charAt(i));
			int amount = amountMapping.get(instructions.charAt(i));
			
			if (action == LActions.FORWARD.toString()) {
				forward(amount, segPos * 1.0 / segLength);
				segPos++;
				
			} else if (action == LActions.NOTHING.toString()) {
				continue;
				
			} else if (action == LActions.ROTATECW.toString()) {
				turnCW(amount);
				
			} else if (action == LActions.ROTATECC.toString()) {
				turnCC(amount);
				
			} else if (action == LActions.PUSHSTATE.toString()) {
				pushState();
				
			} else if (action == LActions.POPSTATE.toString()) {
				popState();
				
			}
		}
	}
	
	private Color gradient(Color c1, Color c2, double amnt) {
		int red = (int) (c2.getRed() * amnt + c1.getRed() * (1 - amnt));
		int green = (int) (c2.getGreen() * amnt + c1.getGreen() * (1 - amnt));
		int blue = (int) (c2.getBlue() * amnt + c1.getBlue() * (1 - amnt));
		
		return new Color(red, green, blue);
	}

	public void setColorScheme(Color gradBegCol, Color gradEndCol) {
		this.gradBegCol = gradBegCol;
		this.gradEndCol = gradEndCol;
		
	}
}
