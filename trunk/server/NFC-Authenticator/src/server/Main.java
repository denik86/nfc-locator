package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JFrame;



public class Main {

	private static int port;
	private static Window win;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		// read the first parameter (port)
		if(args.length != 1) {
			System.out.println("Please specify port number as first parameter");
			System.exit(0);
		} else {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out.println("The specified port is invalid");
				System.exit(0);
			}
		}
		
		// load the database
		UsersDB db = new UsersDB("database", "users", "auth");
		
		// create the main window
		final JFrame frame = new JFrame("NFC Locator Server");
		win = new Window(frame, db, port);
		frame.addWindowListener(new WindowAdapter() {
		
		@Override
		public void windowClosing(WindowEvent e) {
		        frame.setVisible(false);
		        try {
		                Thread.currentThread().sleep(2000);
		        } catch (Exception ex) {
		                ex.printStackTrace();
		        }
		        System.exit(0);
		}
		});
		
		// start window
		Thread window = new Thread(win);
		window.start();
	}

}
