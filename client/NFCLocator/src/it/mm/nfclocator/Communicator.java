package it.mm.nfclocator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class Communicator implements Runnable {
	
	private Reader reader;
	private String address;
	private String port;
	private String user;
	private String password;
	
	public Communicator(Reader reader, String address, String port, String user, String password) {
		this.reader = reader;
		this.address = address;
		this.port = port;
		this.user = user;
		this.password = password;
	}
	
	public void run() {
		String message;
		try {
			InetAddress serverAddr = InetAddress.getByName(address);
			Log.d("Client", "Connecting...");
			Socket socket = new Socket(serverAddr, Integer.parseInt(port));
			message = "Connected, sending data...";
			Log.d("Client", message);
			reader.updateConnectionStatus(false, false, message);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request = user + ":" + password;
			out.println(request);
			String response = in.readLine();
			if(response.equals(user+":OK")) {
				// success
				message = "Access granted";
				Log.d("Client", message);
				reader.updateConnectionStatus(true, false, message);
			} else {
				// unsuccess
				message = "Access denied";
				Log.d("Client", message);
				reader.updateConnectionStatus(true, true, message);
			}
			in.close();
			out.close();
			socket.close();
			// TODO test
			
		} catch (NumberFormatException e1) {
			String error = "The port " + port + " is not valid";
			Log.d("Client", error, e1);
			reader.updateConnectionStatus(true, true, error);
		} catch (UnknownHostException e2) {
			String error = "The address " + port + "is not valid";
			Log.d("Client", error, e2);
			reader.updateConnectionStatus(true, true, error);
		} catch (IOException e3) {
			String error = "The connection has been closed due to a network error";
			Log.d("Client", error, e3);
			reader.updateConnectionStatus(true, true, error);
		}
	}

}
