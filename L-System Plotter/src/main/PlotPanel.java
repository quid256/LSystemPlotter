package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import turtle.Turtle;

@SuppressWarnings("serial")
public class PlotPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private BufferedImage img;
	private Turtle t;
	private Graphics imgGraphics;
	private Point curPos;
	private boolean isDragging = false;
	private Point pressedPoint = new Point(0, 0);
	private final Dimension canvasSize = new Dimension(2000, 2000);
	
	public JLabel coordsLabel;
	private final JFileChooser fc = new JFileChooser();
	
	public PlotPanel() {
		setPreferredSize(new Dimension(500, 500));
		setCursor(new Cursor(Cursor.MOVE_CURSOR));
		setBackground(Color.white);
		img = new BufferedImage(canvasSize.width, canvasSize.height, BufferedImage.TYPE_INT_RGB);
		imgGraphics = img.createGraphics();

		coordsLabel = new JLabel("0, 0");
		this.add(coordsLabel);
		
		t = new Turtle(canvasSize.width / 2, canvasSize.height / 2, imgGraphics);
		curPos = new Point((500 - canvasSize.width) / 2, (500 - canvasSize.height) / 2);
		addMouseListener(this);
		addMouseMotionListener(this);
		
	}
	
	public void clearImg() {
		imgGraphics.setColor(Color.white);
		imgGraphics.fillRect(0, 0, canvasSize.width, canvasSize.height);
	}
	
	
	public void load(String instructions, int segLength, RuleTableDataProvider dataProvider) {
		System.out.println("load");
		//imgGraphics.setColor(Color.red);
		t.moveTo(canvasSize.width / 2, canvasSize.height / 2);
		t.setAngle(270);
		t.segPos = 0;
		t.drawFromInstructions(instructions, segLength, dataProvider);
		repaint();
	}
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(img, curPos.x, curPos.y, canvasSize.width, canvasSize.height,null);
		super.paintComponents(g);
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		System.out.println("pressed");
		isDragging = true;
		pressedPoint = arg0.getPoint();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		System.out.println("released");
		isDragging = false;
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (isDragging) {
			System.out.println("dragging");
			int dx = arg0.getX() - pressedPoint.x;
			int dy = arg0.getY() - pressedPoint.y;
			
			this.curPos.x = this.curPos.x + dx;
			this.curPos.y = this.curPos.y + dy;
			
			System.out.println(coordsLabel);
			System.out.println(curPos);
			System.out.println(canvasSize);
			this.coordsLabel.setText((curPos.x + canvasSize.width / 2 - 250) + ", " + (curPos.y + canvasSize.height / 2 - 250));
			
			pressedPoint = arg0.getPoint();
			
			repaint();
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void savePic() {
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				ImageIO.write(img, "bmp", file);
			} catch (IOException e) {
				System.out.println("failure to writeout");
				e.printStackTrace();
			}
		}
		
	}
}
