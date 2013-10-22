package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;



public class Main {

	private static final String usersFile = "users";
	private static int port;
	private static Window win;
	
	public static void main(String[] args) {
		
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
		
		// initialize user
		try {
			Users users = new Users(usersFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Listener ls = new Listener(port);
		Thread listener = new Thread(ls);
		listener.start();
		
		
	}

}
