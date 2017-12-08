/**
 * Handles the GUI implementation of the Client Frame Window 
 */

package Server;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ClientWindow extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea history;
	private DefaultCaret caret;
	private Thread run, listen;
	private Client client;

	private boolean running = false;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem displayOnlineUsers;
	private JMenuItem mntmExit;
	private OnlineUsers users;

	private Color blue = new Color(33, 150, 243);
	private JMenu mnAbout;
	private JMenuItem aboutAuthorsMenuItem;
	private JMenu helpMenu;
	private JMenuItem howToChatMenuItem;

	public ClientWindow(String name, String address, int port, BigInteger p, BigInteger q) {

		client = new Client(name, address, port, p, q);
		boolean connect = client.openConnection(address);

		// If system fails to connect - print an error message
		if (!connect) {
			System.err.println("Connection failed!");
			console("Connection failed!");
		}
		createWindow();

		console("Attempting a connection to address: " + address + " Port Number: " + port + ", User: " + name);

		// Display name of the current user on their chat window
		setTitle("Chat Window - " + name);

		String connection = "/c/" + name + "/e/";
		client.send(connection.getBytes());
		users = new OnlineUsers();
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}

	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 800);
		setLocationRelativeTo(null);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		displayOnlineUsers = new JMenuItem("Online Users");
		displayOnlineUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				users.setVisible(true);
			}
		});
		mnFile.add(displayOnlineUsers);

		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				running = false;

				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		aboutAuthorsMenuItem = new JMenuItem("About Authors");
		aboutAuthorsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Authors of Networked Chat:\nJahnvi Patel (jpate201)\nPatrick O'Connell (oconne16)\nDeep Mehta (dmehta22)");

			}
		});
		mnAbout.add(aboutAuthorsMenuItem);

		helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		howToChatMenuItem = new JMenuItem("How to Chat");
		howToChatMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"How to Chat:\nSimply type the message you wish to send into the text input box and click on the 'Send' button "
								+ "or press 'Enter' to send the message to all the connected clients.\n\nTo view the list of online users, click on File Menu and Online Users.  ");
			}
		});
		helpMenu.add(howToChatMenuItem);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 28, 815, 30, 7 }; // SUM = 880
		gbl_contentPane.rowHeights = new int[] { 25, 485, 40 }; // SUM = 550
		contentPane.setLayout(gbl_contentPane);

		history = new JTextArea();
		history.setEditable(false);
		JScrollPane scroll = new JScrollPane(history);
		caret = (DefaultCaret) history.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		scrollConstraints.weightx = 1;
		scrollConstraints.weighty = 1;
		scrollConstraints.insets = new Insets(0, 5, 0, 0);
		contentPane.add(scroll, scrollConstraints);

		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(txtMessage.getText(), true);
				}
			}
		});
		GridBagConstraints txtM = new GridBagConstraints();
		txtM.insets = new Insets(0, 0, 0, 5);
		txtM.fill = GridBagConstraints.HORIZONTAL;
		txtM.gridx = 0;
		txtM.gridy = 2;
		txtM.gridwidth = 2;
		txtM.weightx = 1;
		txtM.weighty = 0;
		contentPane.add(txtMessage, txtM);
		txtMessage.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				send(txtMessage.getText(), true);
			}
		});
		GridBagConstraints sendButton = new GridBagConstraints();
		btnSend.setBackground(blue);
		btnSend.setBorderPainted(false);
		btnSend.setOpaque(true);
		sendButton.insets = new Insets(0, 0, 0, 5);
		sendButton.gridx = 2;
		sendButton.gridy = 2;
		sendButton.weightx = 0;
		sendButton.weighty = 0;
		contentPane.add(btnSend, sendButton);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect, false);
				running = false;
				client.close();
			}
		});

		setVisible(true);

		txtMessage.requestFocusInWindow();
	}

	/**
	 * Method to run listen thread
	 */
	public void run() {
		listen();
	}

	/**
	 * Method which takes in a string and boolean and adds flag /m/ and /e/ to mark
	 * start and end of the message
	 * 
	 * @param message
	 * @param text
	 */
	private void send(String message, boolean text) {
		if (message.equals(""))
			return;
		if (text) {
			message = client.getName() + ": " + message;
			message = "/m/" + message + "/e/";
			txtMessage.setText("");
		}
		client.send(message.getBytes());
	}

	/**
	 * Method to create listen thread, which while running receives messages from
	 * the client. Prints unique client ID
	 */
	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while (running) {
					String message = client.receive();
					if (message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
						console("Successfully connected to server! Unique Client ID: " + client.getID());
					} else if (message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						console(text);
					} else if (message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						send(text, false);
					} else if (message.startsWith("/u/")) {
						String[] u = message.split("/u/|/n/|/e/");
						users.update(Arrays.copyOfRange(u, 1, u.length - 1));
					}
				}
			}
		};
		listen.start();
	}

	public void console(String message) {
		history.append(message + "\n\r");
		history.setCaretPosition(history.getDocument().getLength());
	}
}