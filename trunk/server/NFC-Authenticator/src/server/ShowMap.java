package server;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ShowMap extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
    Map m;
    Image image;
    int[][] elements;
    int nElements;

	public ShowMap (String s) {
		
        this.setSize(500,500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		elements = new int[20][2];
		nElements = 0;
		
		//ImageIcon receivedIcon = new ImageIcon("c:\\image.jpg");
		//Panel.setBackground(new ImageIcon("3.jpg"));
		//Background = getImage(getCodeBase(),"pic3.gif");
		
        try {
        	String current;
        	String[] coords = new String[3];
        	boolean find = false;
        	BufferedReader br = new BufferedReader(new FileReader("coord.txt"));
        	System.out.println("PRIMA");
        	while ((current = br.readLine()) != null && !find) 
        	{
        		coords = current.split(",");
        		if(coords[0].equals(s))
        			find = true;	
        	}
        	br.close();    	
        	System.out.println("Coordinate "+ coords[1] + ", " + coords[2]);
        	
        	
        	m = new Map(Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
        	m.add(29,13);
        	m.add(50,20);
        	//this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("torre.png")))));
        	this.getContentPane().add(m);
        	

        	
        
        } catch (IOException e) {
        	e.printStackTrace();
        	System.out.println("vjdjdj");
        }
	}
		/*
		mageIcon i = new ImageIcon("torre.p­ng");
		image = i.getImage();
		g.drawImage(image, 0, 0, null);
		g.setColor(Color.BLACK);
		g.drawRect(40, 40, 30, 30);
		g.setColor(Color.RED);
		g.drawOval(80, 80, 30, 30);
		g.fillArc(140, 140, 30, 30, 180, 10);
		g.drawLine(100, 300, 350, 400); 
	}
	*/
	
	class Map extends JPanel {
		
		private Image img;
		private int x, y;
		
		public Map(int xx, int yy)
		{
			x = xx;
			y = yy;
			img = Toolkit.getDefaultToolkit().createImage("torre.png");
		}
		public void paint(Graphics g){
				
			//for array ciclo
			g.drawImage(img,0,0,null);
			for(int i = 0; i < nElements; i++)
			{
				g.fillRect(elements[i][0], elements[i][1], 20, 20);
			}
			
		}
		
		public void add(int a, int b)
		{
			elements[nElements][0] = a;
			elements[nElements][1] = b;
			nElements++;
		}
	}
}


