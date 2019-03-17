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
		server = new Server(port, this);

		final JPanel north = new JPanel();
		tPortNumber = new JTextField("  " + port);
		stopStart = new JButton("Stop");
		stopStart.addActionListener(this);
		tPortNumber.setEditable(false);

		final JPanel center = new JPanel(new GridLayout(2, 1));
		chat = new JTextArea(80, 80);
		chat.setEditable(false);
		append("Chat room.\n");
		event = new JTextArea(80, 80);
		event.setEditable(false);
		append("Events log.\n");

		north.add(new JLabel("Port number: "));
		north.add(tPortNumber);
		north.add(stopStart);

		center.add(new JScrollPane(chat));
		center.add(new JScrollPane(event));

		add(north, BorderLayout.NORTH);
		add(center);

		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		addWindowListener(this);
		setSize(400, 600);

		new ServerThread().start();
		autoClient = new ClientGUI();
		autoClient.autoConnect(port);
	}

	public void append(final String str) {
		chat.append(str);
		chat.setCaretPosition(chat.getText().length() - 1);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (server != null) {
			server.stop();
			server = null;
			tPortNumber.setEditable(true);
			stopStart.setText("Start");
			return;
		}
		int port;
		try {
			port = Integer.parseInt(tPortNumber.getText().trim());
		} catch (final InputMismatchException ex) {
			append("Port closed, try an open port!");
			ex.printStackTrace();
			return;
		}
		server = new Server(port, this);
		new ServerThread().start();
		stopStart.setText("Stop");
		tPortNumber.setEditable(false);
	}

	public ClientGUI getAutoClient() {
		return autoClient;
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		if (server != null) {
			server.stop();
			server = null;
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
			server.start();
			stopStart.setText("Start");
			tPortNumber.setEditable(true);
			append("Server crashed\n");
			server = null;
		}
	}
}