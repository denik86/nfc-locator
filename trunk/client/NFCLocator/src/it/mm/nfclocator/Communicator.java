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
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
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
	private Context context;
	
	public Communicator(Handler handler, String address, String port, String user, String password, String location, Context context) {
		this.handler = handler;
		this.address = address;
		this.port = port;
		this.user = user;
		this.password = password;
		this.location = location;
		this.context = context;
	}
	
	public void run() {
		
		/*
		 * The method simpleSocketConnection uses a simple socket to connect to the server,
		 * and should be used only for debug purpose.
		 */
		//this.simpleSocketConnection();
		this.SSLSocketConnection();
	}
	
	private void updateConnectionStatus(int code, String status) {
		Message msg = handler.obtainMessage();
		Bundle bundle = new Bundle();
		bundle.putInt("code", code);
		bundle.putString("status", status);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	private void simpleSocketConnection() {
		String message;
		try {
			
			InetAddress serverAddr = InetAddress.getByName(address);
			message = "Connecting...";
			Log.d("Client", message);
			updateConnectionStatus(-1, message);
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(serverAddr, Integer.parseInt(port)), 10000);
			message = "Connected, sending request...";
			Log.d("Client", message);
			updateConnectionStatus(0, message);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request = user + ":" + password + ":" + location;
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
	
	private void SSLSocketConnection() {
		String message;
		try {
			
			InetAddress serverAddr = InetAddress.getByName(address);
			message = "Starting secure connection...";
			Log.d("Client", message);
			updateConnectionStatus(-1, message);
			
			// starting SSL connection
			char[] passphrase = "ssltestcert".toCharArray();
			KeyStore ksTrust = KeyStore.getInstance("BKS");
			ksTrust.load(context.getResources().openRawResource(R.raw.ssltestcert), passphrase);
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			tmf.init(ksTrust);
			// Create a SSLContext with the certificate
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
			SSLSocketFactory socketFactory = sslContext.getSocketFactory();
			SSLSocket socket = (SSLSocket) socketFactory.createSocket();
			socket.connect(new InetSocketAddress(serverAddr, Integer.parseInt(port)), 10000);
			final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
			socket.setEnabledCipherSuites(enabledCipherSuites);
			
			message = "Connected, sending request...";
			Log.d("Client", message);
			updateConnectionStatus(0, message);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request = user + ":" + password + ":" + location; // TODO
			out.println(request);
			out.flush();
			String response = in.readLine();
			if((user+":OK").equals(response)) {
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
		} catch (GeneralSecurityException e4) {
			String error = "The server cannot be trusted, please contact the administrator";
			Log.d("Client", error, e4);
			updateConnectionStatus(3, error);
			e4.printStackTrace();
		} catch (NotFoundException e5) {
			// This should never happen 
			e5.printStackTrace();
		}
	}

}
