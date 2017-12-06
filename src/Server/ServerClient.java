package Server;

import java.net.InetAddress;

public class ServerClient {
	
	public String name; 			//Name of the user 
	public InetAddress address; 	//Computer address
	public int port; 			//Port number 
	private final int ID; 		//to differentiate one client from another 
	public int attempt = 0; 

	public ServerClient(String name, InetAddress address, int port, final int ID ) {
		this.name = name; 
		this.address = address; 
		this.port = port; 
		this.ID = ID; 		
	}
	
	public int getID() {
		return ID; 
	}
}
