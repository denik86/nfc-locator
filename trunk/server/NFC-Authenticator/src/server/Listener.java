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
	
	public Listener (int port) {
		this.port = port;
	}
	
	public void stop () {
		this.stop = true;
	}
	
	public void run () {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), 
                    true); 
			BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
	        { 
	         System.out.println ("Server: " + inputLine); 
	         out.println(inputLine); 

	         if (inputLine.equals("Bye.")) 
	             break; 
	        } 
			while (!stop) {
				// accept
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
