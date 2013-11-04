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
					// check if the format is ok
					// check the username and the password
					// check the auth
					// if something is wrong stop it now, else
					// get the address of the resource
					// connect to the resource
					// send the response to the client
					
					System.out.println ("Server: " + inputLine); 
					out.println(inputLine); 

					if (inputLine.equals("Bye.")) 
						break; 
		        } 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Server socket closed");
		}
		
	}
	
}
