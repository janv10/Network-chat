/**
 * Launches the server 
 */
package Server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class ServerMain {

	private int port;
	private Server server;
	

	public ServerMain(int port) {
		this.port = port;
		server = new Server(port);

	}

	public static void main(String[] args) {
		int port;
		if (args.length != 1) {
			System.out.println("Usage: java -jar NetworkChat.jar [port]");
			return;
		}
		port = Integer.parseInt(args[0]);
		new ServerMain(port);
	}

	

}
