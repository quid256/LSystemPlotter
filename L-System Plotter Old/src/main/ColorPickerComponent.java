package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class ColorPickerComponent extends JComponent implements MouseListener {
	
	private Color col;
	
	public ColorPickerComponent() {
		this.addMouseListener(this);
		col = Color.red;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		g.setColor(col);
		g.fillRect(0,0, this.getWidth() -1 , this.getHeight() - 1);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		
	}
	
	public Color getColor() {
		return col;
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.col = JColorChooser.showDialog(this, "Select Color", this.col);
		repaint();
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
