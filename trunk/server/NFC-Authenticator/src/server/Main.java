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
		
		UsersDB db = new UsersDB("provaDB", "users", "auth");
		//db.addUser("utente1", "ciao");
		//db.removeUser("utente1");
		db.addUser("utente2", "casa");
		//db.addAuth("auth1", "address1");
		//db.removeAuth("auth1");
		db.addAuthToUser("utente1", "auth1");
		if(db.checkAuthUser("utente1", "auth3"))
			System.out.println("CORRETTO:: " +db.getAuthsUsers("utente2")[0]);
		else
			System.out.println("ERRATO");
		db.printAuth();
		db.printUsers();
		
		
		
		
		
		final JFrame frame = new JFrame("NFC Locator Server");
		win = new Window(frame, db, port);
		frame.addWindowListener(new WindowAdapter() {
		
		@Override
		public void windowClosing(WindowEvent e) {
		       // win.stop();
		        frame.setVisible(false);
		        try {
		                Thread.currentThread().sleep(2000);
		        } catch (Exception ex) {
		                ex.printStackTrace();
		        }
		        System.exit(0);
		}
		});
		Thread window = new Thread(win);
		window.start();
		
		//Listener ls = new Listener(port);
		//Thread listener = new Thread(ls);
		//listener.start();
		
		// PROVA
		//Listener runnableAscolto = new Listener(9092);
		//Thread threadAscolto = new Thread(runnableAscolto);
		//threadAscolto.start();
		
		
	}

}
