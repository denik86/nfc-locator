package server;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JPanel implements Runnable {
	
	static JFrame frame;
	static boolean stop = false;
	
	public Window (JFrame frame) {
		this.frame = frame;
	}
	
	public void stop () {
		stop = true;
	}
	
	public void run () {
		// definire la finestra
		while (!stop) {
			// aggiorna la finestra
		}
	}

}
