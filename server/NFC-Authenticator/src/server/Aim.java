package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Aim extends JPanel{
	
	private int x;
	private int y;
	private int lx;
	private int ly;

	public Aim(int cx, int cy, int clx, int cly)
	{
		x = cy;
		y = cy;
		lx = clx;
		ly = cly;
		
	}
	
	public void paint(Graphics gg){
		super.paint(gg);
		Graphics2D g = (Graphics2D) gg;
		g.setColor(Color.RED);
		g.drawLine(x,y, lx/2, y);
		g.drawLine(lx/2,y, lx/2, ly);
		g.drawLine(lx/2,ly, lx, ly);
		g.setColor(Color.BLUE);
		g.drawOval(x,y,10,10);
		g.setColor(Color.GREEN);
		g.fillOval(x+1,y+1,9,9);
		
	}

}

