package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import game.Game;
import game.player.tiles.coordinate.Coordinate;
import game.player.tiles.coordinate.UntouchedCoordinate;

public final class Server {

	static int uniqueId = 0;

	private int port;
	private boolean keepGoing;

	ArrayList<ClientThread> al;

	private ServerGUI sg;
	private SimpleDateFormat sdf;

	public Server(final int port, final ServerGUI sg) {
		this.sg = sg;
		this.port = port;
		sdf = new SimpleDateFormat("HH:mm:ss");
		al = new ArrayList<ClientThread>() {

			private static final long serialVersionUID = -7896866591756916202L;

			@Override
			public boolean add(final ClientThread ct) {
				if (size() < 2) {
					broadcast("Receiving incoming transmission.");
					broadcast("Connection Accepted from " + ct.getUsername() + "@" + ct.getIP());
					super.add(ct);
					return true;
				}
				ct.close();
				return false;
			}
		};
	}

	void display(final String msg) {
		final String time = sdf.format(new Date()) + " " + msg;
		sg.append(time + "\n");
	}

	synchronized void broadcast(final String message) {
		final String time = sdf.format(new Date());
		final String messageLf = time + " " + message + "\n";
		sg.append(messageLf);
		for (int i = al.size(); --i >= 0;) {
			final ClientThread ct = al.get(i);
			if (!ct.write(messageLf)) {
				al.remove(i);
				display("Client " + ct.username + " disconnected");
			}
		}
	}

	synchronized void remove(final int id) {
		for (int i = 0; i < al.size(); ++i) {
			final ClientThread ct = al.get(i);
			if (ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}

	public void start() {
		keepGoing = true;
		try (final ServerSocket serverSocket = new ServerSocket(port)) {
			while (keepGoing) {
				display("Server waiting for Client on port " + port + ".");
				final Socket socket = serverSocket.accept();
				if (!keepGoing) {
					socket.close();
					break;
				}
				final ClientThread t = new ClientThread(socket);
				if (al.add(t)) {
					if (al.size() == 2) {
						Game.get().setGameState(Game.PREPARATION_STATE);
					}
					t.start();
				}
			}
			serverSocket.close();
			for (int i = 0; i < al.size(); ++i) {
				final ClientThread tc = al.get(i);
				tc.sInput.close();
				tc.sOutput.close();
				tc.socket.close();
			}
		} catch (final IOException ex) {
			final String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + ex + "\n";
			display(msg);
			ex.printStackTrace();
		}
	}

	public void stop() {
		keepGoing = false;
	}

	private final class ClientThread extends Thread {

		int id;
		String username;

		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;

		private ChatMessage cm;

		public ClientThread(final Socket socket) {
			id = ++Server.uniqueId;
			this.socket = socket;
			try {
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput = new ObjectInputStream(socket.getInputStream());
				username = (String) sInput.readObject();
			} catch (final IOException ex) {
				ex.printStackTrace();
			} catch (final ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}

		public String getUsername() {
			return username;
		}

		public String getIP() {
			// return socket.getInetAddress().toString();
			return ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().toString().replace("/", "");
		}

		@Override
		public void run() {
			boolean keepGoing = true;
			while (keepGoing) {
				try {
					cm = (ChatMessage) sInput.readObject();
				} catch (final IOException ex) {
					ex.printStackTrace();
				} catch (final ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				switch (cm.getType()) {
					case ChatMessage.DISCONNECT:
						display(username + " disconnected.");
						broadcast(username + "disconnected.");
						Game.get().setGameState(Game.CONNECT_STATE);
						keepGoing = false;
						break;
					case ChatMessage.MESSAGE:
						broadcast(username + ": " + cm.getMessage());
						break;
					case ChatMessage.MOVE:
						sendMove(cm.getCoordinate());
						break;
					case ChatMessage.BOOLEAN:
						sendBoolean(Boolean.parseBoolean(cm.getMessage()));
						break;
					case ChatMessage.UNTOUCHED_COORDINATE:
						sendUntouchedCoordinate(cm.getCoordinate());
						break;
					default:
						break;
				}
			}
			remove(id);
			close();
		}

		private synchronized void sendMove(final Coordinate coord) {
			for (int i = al.size() - 1; i >= 0; i--) {
				final ClientThread ct = al.get(i);
				if (ct != this && !ct.write(coord)) {
					al.remove(i);
					display("Disconnected Client: " + ct.username);
				}
			}
		}

		private synchronized void sendBoolean(final Boolean b) {
			for (int i = al.size() - 1; i >= 0; i--) {
				final ClientThread ct = al.get(i);
				if (ct != this && !ct.write(b)) {
					al.remove(i);
					display("Disconnected Client: " + ct.username);
				}
			}
		}

		private synchronized void sendUntouchedCoordinate(final Coordinate coord) {
			for (int i = al.size() - 1; i >= 0; i--) {
				final ClientThread ct = al.get(i);
				if (ct != this && !ct.write(UntouchedCoordinate.cast(coord))) {
					al.remove(i);
					display("Disconnected Client: " + ct.username);
				}
			}
		}

		boolean write(final Object msg) {
			if (!socket.isConnected()) {
				close();
				return false;
			}
			try {
				sOutput.writeObject(msg);
				return true;
			} catch (final IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}

		void close() {
			try {
				if (sOutput != null) {
					sOutput.close();
				}
				if (sInput != null) {
					sInput.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}