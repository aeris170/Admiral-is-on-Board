package network;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.InputMismatchException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import game.Game;
import game.gui.GUIUtils;
import game.gui.ScrollBarUI;

public final class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 5035312287003366101L;

	private JLabel label;
	private JTextField tf;

	JTextField tfServer;

	private JTextField tfPort;
	private JButton login, logout;
	private JTextArea ta;

	private boolean connected;
	private Client client;

	public ClientGUI() {
		super("Chat");

		final JPanel northPanel = new JPanel(new GridLayout(3, 1));
		final JPanel serverAndPort = new JPanel(new GridLayout(1, 5, 1, 10));
		this.tfServer = new JTextField("");
		this.tfServer.setCaretColor(GUIUtils.WHITE);
		this.tfServer.setBorder(GUIUtils.LIGHT_BLUE_BORDER);
		this.tfPort = new JTextField("");
		this.tfPort.setCaretColor(GUIUtils.WHITE);
		this.tfPort.setBorder(GUIUtils.LIGHT_BLUE_BORDER);
		this.tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		serverAndPort.add(new JLabel("Server Address: "));
		serverAndPort.add(this.tfServer);
		serverAndPort.add(new JLabel(""));
		serverAndPort.add(new JLabel("Port Number: "));
		serverAndPort.add(this.tfPort);
		serverAndPort.add(new JLabel(""));
		northPanel.add(serverAndPort);

		this.label = new JLabel("Enter your username below (DEFAULT guest)", SwingConstants.CENTER);
		northPanel.add(this.label);
		this.tf = new JTextField("guest");
		this.tf.setBackground(Color.WHITE);
		this.tf.setCaretColor(GUIUtils.WHITE);
		this.tf.setBorder(GUIUtils.LIGHT_BLUE_BORDER);
		northPanel.add(this.tf);
		add(northPanel, BorderLayout.NORTH);

		this.ta = new JTextArea("", 80, 80);
		final JPanel centerPanel = new JPanel(new GridLayout(1, 1));
		final JScrollPane sp = new JScrollPane(this.ta, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setBorder(GUIUtils.LIGHT_BLUE_BORDER_2);
		sp.getVerticalScrollBar().setUI(new ScrollBarUI());
		sp.getHorizontalScrollBar().setUI(new ScrollBarUI());
		centerPanel.add(sp);
		this.ta.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);

		this.login = new JButton("Connect") {

			private static final long serialVersionUID = -4808194362293478299L;

			@Override
			public int getWidth() {
				return 81;
			}

			@Override
			public int getHeight() {
				return 26;
			}

			@Override
			public void paintComponent(final Graphics g) {
				final Graphics2D g2d = (Graphics2D) g;
				g2d.clearRect(0, 0, getWidth(), getHeight());
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2d.setStroke(GUIUtils.STROKE_0);
				g2d.setColor(GUIUtils.LIGHTER_RED);
				if (getModel().isRollover()) {
					g2d.setColor(GUIUtils.LIGHT_RED);
				}
				if (getModel().isPressed()) {
					g2d.setColor(GUIUtils.RED);
				}
				if (!getModel().isEnabled()) {
					g2d.setColor(GUIUtils.GRAY);
				}
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.setColor(GUIUtils.RED);
				g2d.drawRect(0, 0, getWidth(), getHeight());
				g2d.setColor(GUIUtils.WHITE);
				g2d.setFont(GUIUtils.GEORGIA_BOLD_12);
				g2d.drawString("CONNECT", 8, 18);
			}
		};
		this.login.addActionListener(this);

		this.logout = new JButton("Disconnect") {

			private static final long serialVersionUID = -4808194362293478299L;

			@Override
			public int getWidth() {
				return 98;
			}

			@Override
			public int getHeight() {
				return 26;
			}

			@Override
			public void paintComponent(final Graphics g) {
				final Graphics2D g2d = (Graphics2D) g;
				g2d.clearRect(0, 0, getWidth(), getHeight());
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2d.setStroke(GUIUtils.STROKE_0);
				g2d.setColor(GUIUtils.LIGHTER_RED);
				if (getModel().isRollover()) {
					g2d.setColor(GUIUtils.LIGHT_RED);
				}
				if (getModel().isPressed()) {
					g2d.setColor(GUIUtils.RED);
				}
				if (!getModel().isEnabled()) {
					g2d.setColor(GUIUtils.GRAY);
				}
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.setColor(GUIUtils.RED);
				g2d.drawRect(0, 0, getWidth(), getHeight());
				g2d.setColor(GUIUtils.WHITE);
				g2d.setFont(GUIUtils.GEORGIA_BOLD_12);
				g2d.drawString("DISCONNECT", 5, 18);
			}
		};
		this.logout.addActionListener(this);

		this.logout.setEnabled(false);
		final JPanel southPanel = new JPanel();
		southPanel.add(this.login);
		southPanel.add(this.logout);
		add(southPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		this.tf.requestFocus();
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(final WindowEvent e) {
				ClientGUI.this.tfServer.requestFocus();
			}

			@Override
			public void windowClosing(final WindowEvent e) {
				final Game game = Game.get();
				game.setGameState(Game.CONNECT_STATE);
				game.purgeNetworkObjects();
			}
		});

		GUIUtils.recursivePaint(this);
	}

	public void append(final String str) {
		this.ta.append(str);
		this.ta.setCaretPosition(this.ta.getText().length() - 1);
	}

	public void connectionFailed() {
		this.login.setEnabled(true);
		this.logout.setEnabled(false);
		this.label.setText("Enter your username below (DEFAULT guest)");
		this.tf.setText("guest");
		this.tfServer.setEditable(true);
		this.tfPort.setEditable(true);
		this.tf.removeActionListener(this);
		this.connected = false;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final Object o = e.getSource();
		if (o == this.logout) {
			this.client.sendDisconnect();
			connectionFailed();
			Game.get().setGameState(Game.CONNECT_STATE);
			return;
		}
		if (this.connected) {
			this.client.sendMessage(this.tf.getText());
			this.tf.setText("");
			return;
		}
		if (o == this.login) {
			final String username = this.tf.getText().trim();
			final String server = this.tfServer.getText().trim();
			final String portNumber = this.tfPort.getText().trim();
			if ((username.length() == 0) || (server.length() == 0) || (portNumber.length() == 0)) {
				return;
			}
			int port;
			try {
				port = Integer.parseInt(portNumber);
			} catch (final InputMismatchException ex) {
				append("Cannot extablish connection to server. Check if your port is open!");
				ex.printStackTrace();
				return;
			}
			this.client = new Client(server, port, username, this);
			if (this.client.start()) {
				this.tf.setText("");
				this.tfPort.setText("" + port);
				this.label.setText("Enter your message below");
				append("Connection to server established. Welcome to the Chat room.\n");
				this.connected = true;
				this.login.setEnabled(false);
				this.logout.setEnabled(true);
				this.tfServer.setEditable(false);
				this.tfPort.setEditable(false);
				this.tf.addActionListener(this);
				Game.get().setGameState(Game.PREPARATION_STATE);
			}
		}
	}

	public void autoConnect(final int port) {
		String IPAdress = "";
		try {
			final URL IPBot = new URL("http://bot.whatismyipaddress.com");
			final BufferedReader sc = new BufferedReader(new InputStreamReader(IPBot.openStream()));
			IPAdress = sc.readLine().trim();
		} catch (final IOException ex) {
			ex.printStackTrace();
			System.exit(-27);
		}
		this.client = new Client(IPAdress, port, "host", this);
		this.tfServer.setText(IPAdress);
		if (this.client.start()) {
			this.tf.setText("");
			this.tfPort.setText("" + port);
			this.label.setText("Enter your message below");
			append("Server succesfully created, waiting for client. Welcome to the Chat room.\n");
			this.connected = true;
			this.login.setEnabled(false);
			this.logout.setEnabled(true);
			this.tfServer.setEditable(false);
			this.tfPort.setEditable(false);
			this.tf.addActionListener(this);
		}
	}

	public Client getClient() {
		return this.client;
	}
}