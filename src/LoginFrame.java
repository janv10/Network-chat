import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtYourNameLogin;
	private JTextField textIPFieldAddress;
	private JLabel labelIPAdress;
	private JTextField txtPort;
	private JLabel labelPort;
	private JLabel addressFieldDescription;
	private JMenu about, exit, help;
	private JMenuItem aboutAuthors, helpNetworkedChat, quitGame;

	/**
	 * Launch the application.
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

	/**
	 * Create the login frame
	 */
	public LoginFrame() {

		/**
		 * Set look and feel of the system as native application to the system of the
		 * platform
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setResizable(false);
		setTitle("Network Chat Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(480, 380);
		setLocationRelativeTo(null); // center the login window
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); // Login window not re-sizable

		txtYourNameLogin = new JTextField();
		txtYourNameLogin.setBounds(195, 50, 223, 26);
		contentPane.add(txtYourNameLogin);
		txtYourNameLogin.setColumns(10);

		JLabel labelName = new JLabel("Your Name:");
		labelName.setBounds(63, 55, 73, 16);
		contentPane.add(labelName);

		textIPFieldAddress = new JTextField();
		textIPFieldAddress.setBounds(195, 93, 223, 26);
		contentPane.add(textIPFieldAddress);
		textIPFieldAddress.setColumns(10);

		labelIPAdress = new JLabel("IP Address:");
		labelIPAdress.setBounds(63, 98, 73, 16);
		contentPane.add(labelIPAdress);

		txtPort = new JTextField();
		txtPort.setBounds(195, 146, 223, 26);
		contentPane.add(txtPort);
		txtPort.setColumns(10);

		labelPort = new JLabel("Port:");
		labelPort.setBounds(88, 151, 48, 16);
		contentPane.add(labelPort);

		addressFieldDescription = new JLabel("(eg. 172.16.254.1)");
		addressFieldDescription.setBounds(239, 118, 176, 16);
		contentPane.add(addressFieldDescription);

		JLabel labelPortDescription = new JLabel("(eg. 1013)");
		labelPortDescription.setBounds(262, 172, 85, 16);
		contentPane.add(labelPortDescription);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBackground(new Color(102, 102, 102));

		// Action event for logging in
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtYourNameLogin.getText();
				String address = textIPFieldAddress.getText();
				int port = Integer.parseInt(txtPort.getText()); // Read as integer

				login(name, address, port);

			}

		});
		btnLogin.setBounds(150, 246, 180, 64);
		contentPane.add(btnLogin);

		JMenuBar serverMenuBar = new JMenuBar();
		serverMenuBar.setBounds(0, 0, 474, 22);
		contentPane.add(serverMenuBar);

		about = new JMenu("About");
		serverMenuBar.add(about);
		
		aboutAuthors = new JMenuItem("About Authors");
		aboutAuthors.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		about.add(aboutAuthors);
		aboutAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Authors of Networked Chat:\nJahnvi Patel (jpate201)\nPatrick O'Connell (oconne16)\nDeep Mehta (dmehta22)");
			}
		});
		
		help = new JMenu("Help");
		serverMenuBar.add(help);
		
		/*Provide help with connection*/
		helpNetworkedChat = new JMenuItem("How to Connect");
		help.add(helpNetworkedChat);
		helpNetworkedChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "How to Connect:");
			}
		});
		
		
		exit = new JMenu("Exit");
		serverMenuBar.add(exit);
		quitGame = new JMenuItem("Quit");
		quitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		exit.add(quitGame);
		quitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		
	}

	/**
	 * Method for logging in - when JButton Login is pressed
	 */
	private void login(String name, String address, int port) {
		// close the main login window
		dispose();

		// Print for error checking
		// System.out.println(name + "," + address + "," + port);

		new ClientChatWindow(name, address, port);

	}
}
