package Server;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Server implements Runnable {

	private List<ServerClient> clients = new ArrayList<ServerClient>();
	private List<Integer> clientResponse = new ArrayList<Integer>();

	private DatagramSocket socket;
	private int port;
	private boolean running = false;
	private Thread run, manage, send, receive;
	private final int MAX_ATTEMPTS = 5;

	private boolean raw = false;
	
	/*
	private BigInteger p = new BigInteger("1147714873");
	private BigInteger q = new BigInteger("640125991");
	private BigInteger n = p.multiply(q);
	private BigInteger phi = new BigInteger("734682118676723280");
	private BigInteger exp = new BigInteger("13");
	private BigInteger dec = new BigInteger("395598063902850997");
	
	*/
	int BIT_LENGTH = 256;

	// Generate random primes
	Random rand = new SecureRandom();

	/*
	
	private BigInteger p = new BigInteger("61515713759448956718224427239234819678807298860503807478747734036937189372339");
	private BigInteger q = new BigInteger("55442513974044353044574467937747871965915026620748625359014933606491729226307");
	private BigInteger n = p.multiply(q);
	private BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
	private BigInteger exp = new BigInteger("1111111111111");
	private BigInteger one = new BigInteger("-1");
	public BigInteger dec = exp.modPow(one, phi);
	*/


	
	private BigInteger p = new BigInteger(BIT_LENGTH / 2, 100, rand);
	private BigInteger q = new BigInteger(BIT_LENGTH / 2, 100, rand);
	private BigInteger n = p.multiply(q);
	private BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
	private BigInteger exp = new BigInteger("1111111111111");
	private BigInteger one = new BigInteger("-1");
	public BigInteger dec = exp.modPow(one, phi);
	//decrypt = decrypt.mod(n);
	

	/*
	 hello
104 101 108 108 111
messageAscii ^ e mod n -> cypher
104101108108111^13 mod 734682120464564143 = 327853684713202128

327853684713202128 ----cypher message

To decipher

cypher^d mod n

327853684713202128^395598063902850997 mod 734682120464564143

=104 101 108 108 111
	 */
	public String RSAdecryption(String sometext) {
		BigInteger solveMe = new BigInteger(sometext);
		BigInteger solved = solveMe.modPow(dec, n);
		String answer = solved.toString();
		StringBuilder answerString = new StringBuilder();
		String finalSolution;
		int stringLength = answer.length();
		char a, b, c;
		int A, B, C;
		int character;
		int aChar;
		char print;
 		for (int i = 0; i+2 < stringLength; i+=3) {
			a = answer.charAt(i);
			b = answer.charAt(i+1);
			c = answer.charAt(i+2);
			A = Character.getNumericValue(a);
			B = Character.getNumericValue(b);
			C = Character.getNumericValue(c);
			character = C + (10*B) + (100*A);
			character -= 100;
			print = (char) character;
			System.out.println(print);
			answerString.append(print);
		}
		finalSolution = answerString.toString();
		System.out.println("Final answer:" + finalSolution);
		return finalSolution;
	}
	
	public String RSAencryption(String sometext) {
		/**System.out.println("p: " +  p);
		System.out.println("q: " +  q);
		System.out.println("n: " +  n);
		
		System.out.println("phi: " +  phi);
		System.out.println("decrpty: " +  dec);
		**/
		System.out.println("In patricks encrpytion, string being encrypted: " + sometext);
		int characterVal;
	    StringBuilder newString = new StringBuilder();
	    for (char c : sometext.toCharArray()) {
	    	characterVal = (int) c;
	    	characterVal += 100;
	    	if (characterVal != 100) {
	    		newString.append(characterVal);
	    	}
	    }
	    BigInteger message = new BigInteger(newString.toString());
	    System.out.println("Encrpyted message is: " + message);
		BigInteger raised = message.modPow(exp,n);
		System.out.println("Encrpyted message is: " + raised);
		String ret = raised.toString();
		System.out.println("Encrpyted message is in string is: " + ret);
		return ret;
	}
	
	public Server(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this, "Server");
		run.start();
	}

	public void run() {
		running = true;
		System.out.println("Server started on port " + port);
		manageClients();
		receive();
		Scanner scanner = new Scanner(System.in);
		while (running) {
			String text = scanner.nextLine();
			if (!text.startsWith("/")) {
				sendToAll("/m/Server: " + text + "/e/");
				continue;
			}
			text = text.substring(1);
			if (text.equals("raw")) {
				if (raw)
					System.out.println("Raw mode off.");
				else
					System.out.println("Raw mode on.");
				raw = !raw;
			} else if (text.equals("clients")) {
				System.out.println("Clients:");
				System.out.println("========");
				for (int i = 0; i < clients.size(); i++) {
					ServerClient c = clients.get(i);
					System.out.println(c.name + "(" + c.getID() + "): " + c.address.toString() + ":" + c.port);
				}
				System.out.println("========");
			} else if (text.startsWith("kick")) {
				String name = text.split(" ")[1];
				int id = -1;
				boolean number = true;
				try {
					id = Integer.parseInt(name);
				} catch (NumberFormatException e) {
					number = false;
				}
				if (number) {
					boolean exists = false;
					for (int i = 0; i < clients.size(); i++) {
						if (clients.get(i).getID() == id) {
							exists = true;
							break;
						}
					}
					if (exists)
						disconnect(id, true);
					else
						System.out.println("Client " + id + " doesn't exist! Check ID number.");
				} else {
					for (int i = 0; i < clients.size(); i++) {
						ServerClient c = clients.get(i);
						if (name.equals(c.name)) {
							disconnect(c.getID(), true);
							break;
						}
					}
				}
			} else if (text.equals("help")) {
				printHelp();
			} else if (text.equals("quit")) {
				quit();
			} else {
				System.out.println("Unknown command.");
				printHelp();
			}
		}
		scanner.close();
	}

	private void printHelp() {
		System.out.println("Here is a list of all available commands:");
		System.out.println("=========================================");
		System.out.println("/raw - enables raw mode.");
		System.out.println("/clients - shows all connected clients.");
		System.out.println("/kick [users ID or username] - kicks a user.");
		System.out.println("/help - shows this help message.");
		System.out.println("/quit - shuts down the server.");
	}

	private void manageClients() {
		manage = new Thread("Manage") {
			public void run() {
				while (running) {
					sendToAll("/i/server");
					sendStatus();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < clients.size(); i++) {
						ServerClient c = clients.get(i);
						if (!clientResponse.contains(c.getID())) {
							if (c.attempt >= MAX_ATTEMPTS) {
								disconnect(c.getID(), false);
							} else {
								c.attempt++;
							}
						} else {
							clientResponse.remove(new Integer(c.getID()));
							c.attempt = 0;
						}
					}
				}
			}
		};
		manage.start();
	}

	/**
	 * Until we have at least one connection, send the status of the client to the
	 * server Get to the user ("/u/"), get each of their names ("/n/"), mark it with
	 * ("/e/") and send it to all the users
	 */
	private void sendStatus() {
		if (clients.size() <= 0)
			return;
		String users = "/u/";
		for (int i = 0; i < clients.size() - 1; i++) {
			users += clients.get(i).name + "/n/";
		}
		users += clients.get(clients.size() - 1).name + "/e/";
		sendToAll(users);
	}

	/**
	 * Method to allow the server to receive packets of data sent from Clients and
	 * Server
	 */
	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (SocketException e) {
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
				}
			}
		};
		receive.start();
	}

	/**
	 * If the string starts with an ("/m/") its a message, that must be sent to all connections
	 * cut out the "/m/" part, mark it with "/e/" and print out the message on to the console 
	 * 
	 * Get the list of all the active client list and send the message to their respective address and port 
	 * @param message - message being sent 
	 */
	private void sendToAll(String message) {
		if (message.startsWith("/m/")) {
			String text = message.substring(3);
			text = text.split("/e/")[0];
			System.out.println(message);
			String hi = RSAencryption(text);
			System.out.println(hi);
			String hi2 = RSAdecryption(hi);
			System.out.println(hi2);
			
		}
		for (int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.address, client.port);
		}
	}

	/**
	 * Create a thread to prep the message (data) received to be sent to other 
	 * start the sending method 
	 * @param data - byte of message received
	 * @param address- where its going 
	 * @param port - what its going on 
	 */
	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	/**
	 * Send method helper - take in the message and send it back with it's string converted to bytes 
	 * mark it with "/e/"
	 * @param message
	 * @param address
	 * @param port
	 */
	private void send(String message, InetAddress address, int port) {
		message += "/e/";
		send(message.getBytes(), address, port);
	}

	/**
	 * For each connected client generate a unique identifier. This program connects up to 10,000 clients
	 * After which, the unique clients ID's start to repeat. Can be changed in class UniqueIdentifier.
	 * 
	 * @param packet
	 */
	private void process(DatagramPacket packet) {
		String string = new String(packet.getData());
		if (raw)
			System.out.println(string);
		if (string.startsWith("/c/")) {
			int id = UniqueIdentifier.getIdentifier();
			String name = string.split("/c/|/e/")[1];
			System.out.println(name + "(" + id + ") connected!");
			clients.add(new ServerClient(name, packet.getAddress(), packet.getPort(), id));
			String ID = "/c/" + id;
			send(ID, packet.getAddress(), packet.getPort());
		} 
		else if (string.startsWith("/m/")) {
			sendToAll(string);
		} 
		else if (string.startsWith("/d/")) {
			String id = string.split("/d/|/e/")[1];
			disconnect(Integer.parseInt(id), true);
		} 
		else if (string.startsWith("/i/")) {
			clientResponse.add(Integer.parseInt(string.split("/i/|/e/")[1]));
		} 
		else {
			System.out.println(string);
		}
	}

	private void quit() {
		for (int i = 0; i < clients.size(); i++) {
			disconnect(clients.get(i).getID(), true);
		}
		running = false;
		socket.close();
	}

	private void disconnect(int id, boolean status) {
		ServerClient c = null;
		boolean existed = false;
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getID() == id) {
				c = clients.get(i);
				clients.remove(i);
				existed = true;
				break;
			}
		}
		if (!existed)
			return;
		String message = "";
		if (status) {
			message = "Client " + c.name + " (" + c.getID() + ") @ " + c.address.toString() + ":" + c.port
					+ " disconnected.";
		} else {
			message = "Client " + c.name + " (" + c.getID() + ") @ " + c.address.toString() + ":" + c.port
					+ " timed out.";
		}
		System.out.println(message);
	}

}
