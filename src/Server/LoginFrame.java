/**
 * Handles the GUI implementation of the Login Frame 
 */
package Server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import javax.swing.JCheckBox;

public class LoginFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JLabel lblIpAddress;
	private JLabel portLabel;
	private JLabel addressLabelDesc;
	private JLabel portLabelDesc;
	private JMenu about;
	private JMenuItem aboutAuthors;

	private Color blue = new Color(33, 150, 243);
	private JTextField textField;
	private JTextField textField_1;

	public LoginFrame() {

		/**
		 * Allow the login window to "look and feel" the same across different operating
		 * systems
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		setResizable(false); // Set the login window not re-sizable
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(450, 480);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtName = new JTextField();
		txtName.setBounds(142, 50, 165, 28);
		contentPane.add(txtName);
		txtName.setColumns(10);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(202, 34, 45, 16);
		contentPane.add(lblName);

		txtAddress = new JTextField();
		txtAddress.setBounds(142, 116, 165, 28);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);

		lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setBounds(186, 96, 77, 16);
		contentPane.add(lblIpAddress);

		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(142, 191, 165, 28);
		contentPane.add(txtPort);

		portLabel = new JLabel("Port:");
		portLabel.setBounds(208, 171, 34, 16);
		contentPane.add(portLabel);

		addressLabelDesc = new JLabel("(eg. 192.168.0.0)");
		addressLabelDesc.setBounds(169, 142, 112, 16);
		contentPane.add(addressLabelDesc);

		portLabelDesc = new JLabel("(eg. 1013)");
		portLabelDesc.setBounds(191, 218, 68, 16);
		contentPane.add(portLabelDesc);

		JButton loginButton = new JButton("Login");
		JCheckBox chckbxGenerateRsaValues = new JCheckBox("Use these values for encryption ");
		chckbxGenerateRsaValues.setBounds(99, 361, 251, 23);
		contentPane.add(chckbxGenerateRsaValues);

		textField = new JTextField();

		textField.setBounds(26, 282, 165, 28);
		contentPane.add(textField);
		textField.setColumns(10);

		JLabel lblEnterRsaQ = new JLabel("Enter RSA Q Value:");
		lblEnterRsaQ.setBounds(271, 261, 142, 16);
		contentPane.add(lblEnterRsaQ);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(248, 282, 165, 28);
		contentPane.add(textField_1);

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String address = txtAddress.getText();
				int port = Integer.parseInt(txtPort.getText());
				String p1 = "0";
				String q1 = "0";
				BigInteger p = new BigInteger(p1);
				BigInteger q = new BigInteger(q1);
				if (textField.getText().equals("") || textField_1.getText().equals("")) {

				} else {
					p1 = textField.getText();
					q1 = textField_1.getText();

					if (chckbxGenerateRsaValues.isSelected() && (q1.length() < 616 || p1.length() < 616)) {
						JOptionPane.showMessageDialog(null,
								"Invalid Prime Number Input\nNo worries we will generate large enough prime values for you!");
					}
					p = new BigInteger(p1);
					q = new BigInteger(q1);
				}

				login(name, address, port, p, q);
			}

		});
		loginButton.setBounds(125, 408, 200, 35);
		loginButton.setBackground(blue);
		loginButton.setBorderPainted(false);
		loginButton.setOpaque(true);
		contentPane.add(loginButton);

		/**
		 * Add a MenuBar for the the Login Window with a About, Help and Exit
		 */
		JMenuBar loginMenuBar = new JMenuBar();
		loginMenuBar.setBounds(0, 0, 474, 22);
		contentPane.add(loginMenuBar);

		about = new JMenu("About");
		loginMenuBar.add(about);

		aboutAuthors = new JMenuItem("About Authors");
		aboutAuthors.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		about.add(aboutAuthors);
		aboutAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Authors of Networked Chat:\nJahnvi Patel (jpate201)\nPatrick O'Connell (oconne16)\nDeep Mehta (dmehta22)");
			}
		});

		JMenu help = new JMenu("Help");
		loginMenuBar.add(help);

		/* Provide help with connection */
		JMenuItem helpNetworkedChat = new JMenuItem("How to Connect");
		help.add(helpNetworkedChat);
		helpNetworkedChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"How to Connect:\nEnter a User Name.\nEnter a IP Address.\nEnter a Port Number.\nOptional: Enter 2048 bit prime values for RSA encryption or simply click on login to begin");
			}
		});

		/* Exit and quit */
		JMenu exit = new JMenu("Exit");
		loginMenuBar.add(exit);
		JMenuItem quitGame = new JMenuItem("Quit");
		quitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		exit.add(quitGame);

		JLabel lblEnterPrimeNumber = new JLabel("Enter RSA P Value:");
		lblEnterPrimeNumber.setBounds(43, 261, 123, 16);
		contentPane.add(lblEnterPrimeNumber);

		JLabel lblmustBe = new JLabel("(MUST be 2048 bits) ");
		lblmustBe.setBounds(43, 309, 165, 16);
		contentPane.add(lblmustBe);

		JLabel label = new JLabel("(MUST be 2048 bits) ");
		label.setBounds(271, 309, 136, 16);
		contentPane.add(label);

		quitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				System.exit(0);
			}
		});

	}

	/**
	 * Close the login window and launch the login window
	 * 
	 * @param name
	 * @param address
	 * @param port
	 */

	private void login(String name, String address, int port, BigInteger p, BigInteger q) {
		dispose();

		new ClientWindow(name, address, port, p, q);
	}

	/**
	 * Main method to launch the login window
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}