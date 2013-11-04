package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener implements Runnable {

	private int port;
	public boolean stop = false;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	
	public Listener (int port) {
		// TODO add userDB reference
		this.port = port;
	}
	
	public void stop () {
		this.stop = true;
		try {
			serverSocket.close();
			if(clientSocket != null) {
				clientSocket.close();
			}
		} catch (IOException e) {
			System.out.println("Listener thread closed");
		}
		
	}
	
	public void run () {
		try {
			System.out.println("Listener thread started");
			serverSocket = new ServerSocket(port);
			while (!stop) {
				clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
	                    true); 
				BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) 
		        { 
					// TODO
					// check if the format is ok ("username:password:resource")
					String[] data = inputLine.split(":");
					if(data.length == 3) {
						// check username and password
						if(true /* users.checkUser(inputLine[0], inputLine[1]) */) {
							// check auth
							if (true /* users.checkUserAuth(inputLine[0], inputLine[2]) */) {
								String address = "prova"; // users.getAddress(inputLine[2]);
								// TODO connect to the resource
								// send the response to the client
							} else {
								out.println("You don't have the authorization to access this resource");
							}
						} else {
							out.println("Invalid username or password.");
						}
					} else {
						out.println("Protocol mismatch.");
						in.close();
						out.close();
						clientSocket.close();
						break;
					}
					
					/*System.out.println ("Server: " + inputLine); 
					out.println(inputLine); 
					if (inputLine.equals("Bye.")) 
						break; */
		        } 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Server socket closed");
		}
		
	}
	
}
