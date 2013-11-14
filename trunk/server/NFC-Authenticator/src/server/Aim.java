package server;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Aim extends JPanel{
	
	private int x;
	private int y;
	private int lx;
	private int h;

	public Aim(int clx, int nh)
	{
		lx = clx;
		h = nh;
	}
	
	public void paint(Graphics gg){
		super.paint(gg);
		Graphics2D g = (Graphics2D) gg;
		g.setColor(Color.RED);
		g.drawLine(x+lx,0, x+lx/2, 0);
		if(h <= 0)
		{
			BasicStroke bs = new BasicStroke(1,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,4,new float[]{4},10);

			g.setStroke(bs);

			g.drawLine(x+lx/2,0, x, 0);
		}
		else if(h > 320)
		{
			
			g.drawLine(x+lx/2,0, x+lx/2, 280);
			BasicStroke bs = new BasicStroke(1,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND,4,new float[]{4},10);

			g.setStroke(bs);
			
			g.drawLine(x+lx/2,280, x+lx/2, 320);
			
			
		}
		else
		{
		g.drawLine(x+lx/2,0, x+lx/2, h);
		g.drawLine(x+lx/2,h, x, h);
		}
	}
	
	public void move(int h)
	{
		this.h = h;
	}

}

