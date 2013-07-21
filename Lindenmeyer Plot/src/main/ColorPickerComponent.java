package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ColorPickerComponent extends JLabel implements MouseListener {
	
	private Color col;
	
	public ColorPickerComponent(Color initialCol) {
		
		this.addMouseListener(this);
		
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		
		col = initialCol;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		
		g.setColor(col);
		g.fillRect(0,0, this.getWidth() -1 , this.getHeight() - 1);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		float lum = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null)[2];
		
		if (lum < 0.5) {
			this.setForeground(new Color(255, 255, 255));
		} else {
			this.setForeground(new Color(0, 0, 0));
		}
		
		super.paintComponent(g);
		//this.label.paint(g);
		
	}
	
	public Color getColor() {
		return col;
	}
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		
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
		Color c = JColorChooser.showDialog(this, "Select Color", this.col);
		if (c != null) {
			this.col = c;
			repaint();
		}
		
	}
}
