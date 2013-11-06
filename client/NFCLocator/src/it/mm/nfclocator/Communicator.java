package it.mm.nfclocator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Communicator implements Runnable {
	
	private Handler handler;
	private String address;
	private String port;
	private String user;
	private String password;
	private String location;
	
	public Communicator(Handler handler, String address, String port, String user, String password, String location) {
		this.handler = handler;
		this.address = address;
		this.port = port;
		this.user = user;
		this.password = password;
		this.location = location;
	}
	
	public void run() {
		String message;
		try {
			InetAddress serverAddr = InetAddress.getByName(address);
			message = "Connecting...";
			Log.d("Client", message);
			updateConnectionStatus(-1, message);
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(serverAddr, Integer.parseInt(port)), 10000);
			message = "Connected, sending data...";
			Log.d("Client", message);
			updateConnectionStatus(0, message);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request = user + ":" + password + ":" + location; // TODO
			out.println(request);
			out.flush();
			String response = in.readLine();
			if(response.equals(user+":OK")) {
				// success
				message = "Access granted";
				Log.d("Client", message);
				updateConnectionStatus(1, message);
			} else {
				// unsuccess
				message = "Access denied";
				Log.d("Client", message);
				updateConnectionStatus(2, response);
			}
			in.close();
			out.close();
			socket.close();
			// TODO test
			
		} catch (NumberFormatException e1) {
			String error = "The port " + port + " is not valid";
			Log.d("Client", error, e1);
			updateConnectionStatus(3, error);
		} catch (UnknownHostException e2) {
			String error = "The address " + port + "is not valid";
			Log.d("Client", error, e2);
			updateConnectionStatus(3, error);
		} catch (IOException e3) {
			String error = "The connection has been closed due to a network error";
			Log.d("Client", error, e3);
			updateConnectionStatus(3, error);
		}
	}
	
	private void updateConnectionStatus(int code, String status) {
		Message msg = handler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putInt("code", code);
		bundle.putString("status", status);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}

}
