package server;

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
		// TODO ascolta la porta
		
	}
	
}
