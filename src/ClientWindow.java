/**
 * Handle GUI side of the client 
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class ClientWindow extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMessage;
	private JTextArea chatHistory;
	private DefaultCaret updateCaret;
	private Thread run, listen; 
	private boolean running = false; 
	
	private Client client; 
	
	/**
	 * Window for clientChat
	 */
	public ClientWindow(String name, String address, int port) {
		setTitle("Client Chat ");
		
		client = new Client(name, address, port);
		
		boolean connect = client.openConnection(address);
		
		/* failed connection handling */
		if (!connect) {
			System.err.println("Connection Failed!"); 
			reportConsole ("Connection Failed!");
		}
		
		
		makeWindow(); 
		reportConsole("Attempting a connection to: " + address + ", Port Number: " + port + ", User: " + name);
		String connection = "/c/" + name; 
		client.send(connection.getBytes()); 
		running = true;
		run = new Thread(this, "Running Thread"); 
		run.start(); 
	}
	

	/**
	 * Method for building Client Window
	 */
	private void makeWindow() {
		/**
		 * Set look and feel of the system as native application to the system of the
		 * platform
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(880,550);
		setLocationRelativeTo(null);		//center the window
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu aboutMenu = new JMenu("About");
		menuBar.add(aboutMenu);
		
		JMenuItem aboutAuthors = new JMenuItem("About Authors");
		aboutAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Authors of Networked Chat:\nJahnvi Patel (jpate201)\nPatrick O'Connell (oconne16)\nDeep Mehta (dmehta22)");
			}
		});
		aboutMenu.add(aboutAuthors);
		
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		
		JMenuItem helpToChat = new JMenuItem("How to Chat");
		helpToChat.addActionListener(new ActionListener() {	
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "How to Chat:");
				
			}
		});
		helpMenu.add(helpToChat);
		
		JMenu exitMenu = new JMenu("Exit");
		menuBar.add(exitMenu);
		
		JMenuItem clientQuit = new JMenuItem("Quit");
		clientQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		clientQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitMenu.add(clientQuit);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		//Add Grigbag layout for the chat messages 
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{28, 815, 30, 7};
		gbl_contentPane.rowHeights = new int[]{35, 475, 40};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		//Move text area in to the scroll pane to make it scroll-able 
		chatHistory = new JTextArea();
		chatHistory.setEditable(false);		//make chat history window not editable 
		JScrollPane scroll = new JScrollPane(chatHistory);		//allow chat history to be scroll-able 
		GridBagConstraints scrollConstraits = new GridBagConstraints();
		scrollConstraits.insets = new Insets(0, 0, 5, 5);
		scrollConstraits.fill = GridBagConstraints.BOTH;
		scrollConstraits.gridx = 0;
		scrollConstraits.gridy = 0;
		scrollConstraits.gridwidth = 3;
		scrollConstraits.gridheight = 2;
		scrollConstraits.insets = new Insets(10, 5, 0, 0);
		contentPane.add(scroll, scrollConstraits);
		
		
		
		txtMessage = new JTextField();
		
		//Allow 'Enter' Button to be used to send messages to the chat 
		txtMessage.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(txtMessage.getText());
				}
				
				
			}
		});
		GridBagConstraints gbc_sendMessageField = new GridBagConstraints();
		gbc_sendMessageField.insets = new Insets(0, 0, 0, 5);
		gbc_sendMessageField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sendMessageField.gridx = 0;
		gbc_sendMessageField.gridy = 2;
		gbc_sendMessageField.gridwidth = 2;
		contentPane.add(txtMessage, gbc_sendMessageField);
		txtMessage.setColumns(10);
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				send(txtMessage.getText());
			}
		});
		
		GridBagConstraints gbc_sendButton = new GridBagConstraints();
		gbc_sendButton.insets = new Insets(0, 0, 0, 5);
		gbc_sendButton.gridx = 2;
		gbc_sendButton.gridy = 2;
		contentPane.add(sendButton, gbc_sendButton);
		setVisible(true);
		txtMessage.requestFocusInWindow(); //allow text to be typed on the send message field 
		
	}
	
	public void run() {
		listen(); 	
	}
	
	private void send(String message) {
		
		//Empty messages are not printed 
		if (message.equals("")) return; 
		
		//User name to be printed with the message 
		message = client.getName() + ": " + message; 
		
		//Message to the chat window 
		reportConsole(message);
		
		message = "/m/" + message;
		
		client.send(message.getBytes()); 
		
		//Clear the typed message from the field
		txtMessage.setText("");
		
	}

	/**
	 * Attempt a connection and send the packet to the send method above 
	 */
	public void listen() {
		listen = new Thread("Listen"){
			public void run() {
				while (running) {
				String message = client.receive();
				if (message.startsWith("/c/")) {
					client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
					reportConsole ("Successfully Connected to the Server!" + "Your unique ID: "+ client.getID());
				}
				}
			}
		}; 
		
		listen.start();
		
	}
	
	public void reportConsole(String message) {
		chatHistory.append(message + "\n\r");

		//Update where the current chat is so scroll panel starts at the bottom when new typed message comes 
		chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
		
		
	}
	

}
