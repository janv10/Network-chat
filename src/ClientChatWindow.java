import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;



public class ClientChatWindow {

	private static final long serialVersionUID = 1L;
	
	private String name, address; 
	private int port; 
	private InetAddress ip;
	private Thread send; 
	private DatagramSocket socket; 
	
	public ClientChatWindow(String name, String address, int port ) {
		this.name = name; 
		this.address = address; 
		this.port = port; 
	}

	public String getName() {
		return name; 
	}
	
	public String getAddress() {
		return address;
		
	}
	 public int port() {
		 return port; 
	 }
	
	public boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false; 
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		return true; 
	}

	public String receive() {
		
		/* Create an array of bytes */
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		/* Fill the packet with data, receive from the network*/
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* Store in here */
		String message = new String(packet.getData());
		return message; 
	}
	
	public void send(final byte[] data) {
		send = new Thread("Send") {
			public void run() {
				//send a DatagramPacket
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port ); 
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}; 
		send.start();
	}
	
	
	

}
