package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener implements Runnable {

	private int port;
	public boolean stop = false;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private UsersDB users;
	
	public Listener (int port, UsersDB users) {
		this.users = users;
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
				inputLine = in.readLine();
		        if(inputLine != null) {
					// TODO
					// check if the format is ok ("username:password:resource")
					String[] data = inputLine.split(":");
					if(data.length == 3) {
						// check username and password
						if(users.checkUser(data[0], data[1])) {
							// check auth
							if (true /* users.checkUserAuth(data[0], data[2]) */) {
								String[] address = "localhost:9094".split(":"); // users.getAddress(data[2]).split(":");
								try {
									InetAddress host = InetAddress.getByName(address[0]);
									Socket resourceSocket = new Socket(host, Integer.parseInt(address[1]));
									PrintWriter resourceOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(resourceSocket.getOutputStream())));
									BufferedReader resourceIn = new BufferedReader(new InputStreamReader(resourceSocket.getInputStream()));
									resourceOut.println("o"+data[0]);
									resourceOut.flush();
									resourceSocket.setSoTimeout(10000); // set readLine timeout to 10 sec
									String answer = resourceIn.readLine();
									out.println(data[0]+":OK");
									resourceOut.close();
									resourceIn.close();
									resourceSocket.close();
									in.close();
									out.close();
									clientSocket.close();
								} catch (Exception e) {
									// e.printStackTrace();
									out.println("An error has occurred while connecting with the resource");
									in.close();
									out.close();
									clientSocket.close();
								}
							} else {
								out.println("You don't have the authorization to access this resource");
								in.close();
								out.close();
								clientSocket.close();
							}
						} else {
							out.println("Invalid username or password.");
							in.close();
							out.close();
							clientSocket.close();
						}
					} else {
						out.println("Protocol mismatch.");
						in.close();
						out.close();
						clientSocket.close();
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
