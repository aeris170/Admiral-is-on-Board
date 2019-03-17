package network;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.InputMismatchException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.dosse.upnp.UPnP;

public final class ServerGUI extends JFrame implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;

	private JTextArea chat, event;
	JButton stopStart;
	JTextField tPortNumber;

	Server server;

	private ClientGUI autoClient;

	public ServerGUI(final int port) {
		super("Chat Server");
		UPnP.openPortTCP(port);
		this.server = new Server(port, this);

		final JPanel north = new JPanel();
		this.tPortNumber = new JTextField("  " + port);
		this.stopStart = new JButton("Stop");
		this.stopStart.addActionListener(this);
		this.tPortNumber.setEditable(false);

		final JPanel center = new JPanel(new GridLayout(2, 1));
		this.chat = new JTextArea(80, 80);
		this.chat.setEditable(false);
		append("Chat room.\n");
		this.event = new JTextArea(80, 80);
		this.event.setEditable(false);
		append("Events log.\n");

		north.add(new JLabel("Port number: "));
		north.add(this.tPortNumber);
		north.add(this.stopStart);

		center.add(new JScrollPane(this.chat));
		center.add(new JScrollPane(this.event));

		add(north, BorderLayout.NORTH);
		add(center);

		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		addWindowListener(this);
		setSize(400, 600);

		new ServerThread().start();
		this.autoClient = new ClientGUI();
		this.autoClient.autoConnect(port);
	}

	public void append(final String str) {
		this.chat.append(str);
		this.chat.setCaretPosition(this.chat.getText().length() - 1);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (this.server != null) {
			this.server.stop();
			this.server = null;
			this.tPortNumber.setEditable(true);
			this.stopStart.setText("Start");
			return;
		}
		int port;
		try {
			port = Integer.parseInt(this.tPortNumber.getText().trim());
		} catch (final InputMismatchException ex) {
			append("Port closed, try an open port!");
			ex.printStackTrace();
			return;
		}
		this.server = new Server(port, this);
		new ServerThread().start();
		this.stopStart.setText("Stop");
		this.tPortNumber.setEditable(false);
	}

	public ClientGUI getAutoClient() {
		return this.autoClient;
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		if (this.server != null) {
			this.server.stop();
			this.server = null;
		}
		dispose();
	}

	@Override
	public void windowClosed(final WindowEvent e) {}

	@Override
	public void windowOpened(final WindowEvent e) {}

	@Override
	public void windowIconified(final WindowEvent e) {}

	@Override
	public void windowDeiconified(final WindowEvent e) {}

	@Override
	public void windowActivated(final WindowEvent e) {}

	@Override
	public void windowDeactivated(final WindowEvent e) {}

	private final class ServerThread extends Thread {

		public ServerThread() {}

		@Override
		public void run() {
			ServerGUI.this.server.start();
			ServerGUI.this.stopStart.setText("Start");
			ServerGUI.this.tPortNumber.setEditable(true);
			append("Server crashed\n");
			ServerGUI.this.server = null;
		}
	}
}