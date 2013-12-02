package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Listener implements Runnable {

	private int port;
	public boolean stop = false;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private UsersDB users;
	private Window window;
	
	public Listener (int port, UsersDB users, Window window) {
		
		this.window = window;
		this.users = users;
		this.port = port;
	}
	
	public void stop () {
		this.stop = true;
		try {
			serverSocket.close();
			window.signal(0,null, "## Server Stopped");
			if(clientSocket != null) {
				clientSocket.close();
			}
		} catch (IOException e) {
			System.out.println("Listener thread closed");
		}
	}
	
	public void run () {
		try {
			Security.addProvider(new BouncyCastleProvider());
			System.out.println("Listener thread started");
			window.signal(0,null, "## Server Started");
			serverSocket = this.getServerSocket(port, true);
			while (!stop) {
				clientSocket = serverSocket.accept();	
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); 
				BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
				String inputLine;
				inputLine = in.readLine();
		        if(inputLine != null) {
					// check if the format is ok ("username:password:resource")
					String[] data = inputLine.split(":");
					if(data.length == 3) {
						// check username and password
						if(users.checkUser(data[0], data[1])) {
							window.signal(0,null, "User '"+data[0]+"' authenticated");
							// check auth
							if (users.checkAuthUser(data[0], data[2])) {
								window.signal(0,null, "User '"+data[0]+"' request the access to resource '"+data[2]+"'");
								String location = users.authLocation(data[2]);
								
								try {
									if(location == null || location.split(":").length != 2)
										throw new IllegalArgumentException();
									String[] address = location.split(":");
									InetAddress host = InetAddress.getByName(address[0]);
									Socket resourceSocket = new Socket(host, Integer.parseInt(address[1]));
									PrintWriter resourceOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(resourceSocket.getOutputStream())));
									BufferedReader resourceIn = new BufferedReader(new InputStreamReader(resourceSocket.getInputStream()));
									resourceOut.println("o"+data[0]);
									resourceOut.flush();
									resourceSocket.setSoTimeout(10000); // set readLine timeout to 10 sec
									String answer = resourceIn.readLine(); // used as ack
									out.println(data[0]+":OK");
									resourceOut.close();
									resourceIn.close();
									resourceSocket.close();
									in.close();
									out.close();
									clientSocket.close();
									window.signal(1, data[2], "Access garanted [user: '"+data[0]+"', resource: '"+data[2]+"']");
								} catch (IllegalArgumentException e1) {
									out.println("An error has occurred while connecting with the resource");
									System.err.println("The location in the auth " + data[2] + " is not valid!");
									in.close();
									out.close();
									clientSocket.close();
								} catch (Exception e) {
									out.println("An error has occurred while connecting with the resource");
									in.close();
									out.close();
									clientSocket.close();
								}
							} else {
								out.println("You don't have the authorization to access this resource");
								window.signal(2, data[2], "Access denied [user: '"+data[0]+"', resource: '"+data[2]+"']");
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
		        } 
			}
		} catch (IOException e) {
			System.out.println("Server socket closed");
		}
		
	}
	
	/**
	 * Return a ServerSocket listening on the specified port. Can be used the SSL.
	 * @param port to use
	 * @param secure true if SSL is required
	 * @return
	 * @throws IOException
	 */
	private ServerSocket getServerSocket(int port, boolean secure) throws IOException {
		if(secure) {
			SSLServerSocketFactory ssf = getServerSocketFactory();
			
			SSLServerSocket server = (SSLServerSocket) ssf.createServerSocket(port);
			
			final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
			server.setEnabledCipherSuites(enabledCipherSuites);
			return server;
		} else
			return new ServerSocket(port);
	}
	
	private static SSLServerSocketFactory getServerSocketFactory()
	{
		SSLServerSocketFactory ssf = null;
		
		try {
			SSLContext ctx;
			KeyManagerFactory kmf;
			KeyStore ks;
			char[] passphrase = "ssltestcert".toCharArray();
			ctx = SSLContext.getInstance("TLS");
			kmf = KeyManagerFactory.getInstance("SunX509");
			ks = KeyStore.getInstance("BKS");
			ks.load(new FileInputStream("raw/ssltestcert"), passphrase);
			kmf.init(ks, passphrase);
			ctx.init(kmf.getKeyManagers(), null, null);
			ssf = ctx.getServerSocketFactory();
			return ssf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
