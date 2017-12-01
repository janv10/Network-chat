import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;

public class ClientChatWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String name, address; 
	private int port; 
	private JTextField sendMessageField;
	private JTextArea txtrChathistory;

	/**
	 * Window for clientChat
	 */
	public ClientChatWindow(String name, String address, int port) {
		setTitle("Client Chat ");
		this.name = name; 
		this.address = address; 
		this.port = port; 
		makeWindow(); 
		reportConsole("Attempting a connection to: " + address + ", Port Number: " + port + ", User: " + name);


	
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
		
		txtrChathistory = new JTextArea();
		
		txtrChathistory.setEditable(false);		//make chat history window not editable 
		GridBagConstraints gbc_txtrChathistory = new GridBagConstraints();
		gbc_txtrChathistory.insets = new Insets(0, 0, 5, 5);
		gbc_txtrChathistory.fill = GridBagConstraints.BOTH;
		gbc_txtrChathistory.gridx = 1;
		gbc_txtrChathistory.gridy = 1;
		gbc_txtrChathistory.gridwidth = 2; 
		gbc_txtrChathistory.insets = new Insets(10, 5, 0, 0);
		contentPane.add(txtrChathistory, gbc_txtrChathistory);
		
		
		
		sendMessageField = new JTextField();
		
		//Allow 'Enter' Button to be used to send messages to the chat 
		sendMessageField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(sendMessageField.getText());
				}
				
				
			}
		});
		GridBagConstraints gbc_sendMessageField = new GridBagConstraints();
		gbc_sendMessageField.insets = new Insets(0, 0, 0, 5);
		gbc_sendMessageField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sendMessageField.gridx = 1;
		gbc_sendMessageField.gridy = 2;
		contentPane.add(sendMessageField, gbc_sendMessageField);
		sendMessageField.setColumns(10);
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				send(sendMessageField.getText());
			}
		});
		
		GridBagConstraints gbc_sendButton = new GridBagConstraints();
		gbc_sendButton.insets = new Insets(0, 0, 0, 5);
		gbc_sendButton.gridx = 2;
		gbc_sendButton.gridy = 2;
		contentPane.add(sendButton, gbc_sendButton);
		setVisible(true);
		sendMessageField.requestFocusInWindow(); //allow text to be typed on the send message field 
		
	}
	
	private void send(String message) {
		
		//Empty messages are not printed 
		if (message.equals("")) return; 
		
		//Message to the chat window 
		reportConsole(message);
		
		//Clear the typed message from the field
		sendMessageField.setText("");
		
	}
	
	public void reportConsole(String message) {
		txtrChathistory.append(message + "\n\r");
		
	}
}
