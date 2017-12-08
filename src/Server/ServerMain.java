package Server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class ServerMain {

	private int port;
	private Server server;
	/*int BIT_LENGTH = 2048;
	Random rand = new SecureRandom();
	private BigInteger p = new BigInteger(BIT_LENGTH / 2, 100, rand);
	private BigInteger q = new BigInteger(BIT_LENGTH / 2, 100, rand);
	private  BigInteger n = p.multiply(q);
	private BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
	private BigInteger exp = new BigInteger("1111111111111");
	private BigInteger one = new BigInteger("-1");
	private BigInteger dec = exp.modPow(one, phi);*/
	

	
	
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
		//LoginFrame.main(args);
	}
	
	/*public BigInteger getDec() {
		return dec;
	}
	
	public BigInteger getN() {
		return n;
	}
	
	public BigInteger getPhi() {
		return phi;
	}
	
	public BigInteger getExp() {
		return exp;
	}*/
	
	
	

}
