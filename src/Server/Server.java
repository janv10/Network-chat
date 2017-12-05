package Server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable {
	
	private int port; 
	private DatagramSocket socket; 
	private boolean running = false; 
	
	private Thread run, manage, receive, send; 

	
	public Server (int port) {
		this.port = port; 
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		run = new Thread(this, "Server");
	}

	public void run() {
		running = true; 
		manageClients(); 
		receive(); 
		
	}
	
	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					//managing
				}
			}
		}; 
		manage.start(); 
		
	}
	
	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while (running) {
					//Receiving
				}
				
			}
		}; 
		receive.start(); 
		
	}
}
