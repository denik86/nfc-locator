package server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Map extends JPanel{
	
	class Status
	{	private int x, y, dx, dy;
		private boolean visible;
		public Status(int x, int y, int dx, int dy)
		{
			this.x = x;
			this.y = y;
			this.dx = dx;
			this.dy = dy;
			visible = false;
		}
	}
	
	private static final long serialVersionUID = 1L;
	
    Hashtable<String, Status> ht;
    public int color;

	public Map () {
		ht = new Hashtable<String, Status>();
		color = 0;
        try {
        	String current;
        	BufferedReader br = new BufferedReader(new FileReader("coord.txt"));
        	while ((current = br.readLine()) != null ) 
        	{
        		String[] cArray = current.split(",");
        		ht.put(cArray[0], new Status(Integer.parseInt(cArray[1]), Integer.parseInt(cArray[2]), Integer.parseInt(cArray[3]), Integer.parseInt(cArray[4])));	
        	}
        	br.close();    	
    		this.setLayout(null);
       

        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
	
	public void setOn(String s)
	{
		Status st = ht.get(s);
		st.visible = true;
		ht.remove(s);
		ht.put(s, st);
		repaint();
	}
	
	public void setOff(String s)
	{
		Status st = ht.get(s);
		st.visible = false;
		ht.remove(s);
		ht.put(s, st);
		repaint();
	}

	public void paint(Graphics g){
		super.paint(g);
		Graphics g2d = (Graphics2D) g;
		g2d.setColor(Color.decode("000033"));
		g2d.fillRect(0, 0, 500, 500);
		g2d.setColor(Color.decode("606060"));  
		Iterator<String> iterator = ht.keySet().iterator();
		while(iterator.hasNext())
		{
			String name = (String) iterator.next();
			g2d.drawRect(ht.get(name).x, ht.get(name).y, ht.get(name).dx, ht.get(name).dy);
			g2d.drawString(name, ht.get(name).x+5, ht.get(name).y+15);
		}

		if(color == 1)
			g2d.setColor(Color.GREEN);  
		else if(color == 2)
			g2d.setColor(Color.RED);
		iterator = ht.keySet().iterator();
		while(iterator.hasNext())
		{
			String name = (String) iterator.next();
			if(ht.get(name).visible)
				g2d.fillRect(ht.get(name).x, ht.get(name).y, ht.get(name).dx, ht.get(name).dy);
		}
	}
}
