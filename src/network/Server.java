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
		this.sdf = new SimpleDateFormat("HH:mm:ss");
		this.al = new ArrayList<ClientThread>() {

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
		final String time = this.sdf.format(new Date()) + " " + msg;
		this.sg.append(time + "\n");
	}

	synchronized void broadcast(final String message) {
		final String time = this.sdf.format(new Date());
		final String messageLf = time + " " + message + "\n";
		this.sg.append(messageLf);
		for (int i = this.al.size(); --i >= 0;) {
			final ClientThread ct = this.al.get(i);
			if (!ct.write(messageLf)) {
				this.al.remove(i);
				display("Client " + ct.username + " disconnected");
			}
		}
	}

	synchronized void remove(final int id) {
		for (int i = 0; i < this.al.size(); ++i) {
			final ClientThread ct = this.al.get(i);
			if (ct.id == id) {
				this.al.remove(i);
				return;
			}
		}
	}

	public void start() {
		this.keepGoing = true;
		try (final ServerSocket serverSocket = new ServerSocket(this.port)) {
			while (this.keepGoing) {
				display("Server waiting for Client on port " + this.port + ".");
				final Socket socket = serverSocket.accept();
				if (!this.keepGoing) {
					socket.close();
					break;
				}
				final ClientThread t = new ClientThread(socket);
				if (this.al.add(t)) {
					if (this.al.size() == 2) {
						Game.get().setGameState(Game.PREPARATION_STATE);
					}
					t.start();
				}
			}
			serverSocket.close();
			for (int i = 0; i < this.al.size(); ++i) {
				final ClientThread tc = this.al.get(i);
				tc.sInput.close();
				tc.sOutput.close();
				tc.socket.close();
			}
		} catch (final IOException ex) {
			final String msg = this.sdf.format(new Date()) + " Exception on new ServerSocket: " + ex + "\n";
			display(msg);
			ex.printStackTrace();
		}
	}

	public void stop() {
		this.keepGoing = false;
	}

	private final class ClientThread extends Thread {

		int id;
		String username;

		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;

		private ChatMessage cm;

		public ClientThread(final Socket socket) {
			this.id = ++Server.uniqueId;
			this.socket = socket;
			try {
				this.sOutput = new ObjectOutputStream(socket.getOutputStream());
				this.sInput = new ObjectInputStream(socket.getInputStream());
				this.username = (String) this.sInput.readObject();
			} catch (final IOException ex) {
				ex.printStackTrace();
			} catch (final ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}

		public String getUsername() {
			return this.username;
		}

		public String getIP() {
			// return socket.getInetAddress().toString();
			return (((InetSocketAddress) this.socket.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
		}

		@Override
		public void run() {
			boolean keepGoing = true;
			while (keepGoing) {
				try {
					this.cm = (ChatMessage) this.sInput.readObject();
				} catch (final IOException ex) {
					ex.printStackTrace();
				} catch (final ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				switch (this.cm.getType()) {
					case ChatMessage.DISCONNECT:
						display(this.username + " disconnected.");
						broadcast(this.username + "disconnected.");
						Game.get().setGameState(Game.CONNECT_STATE);
						keepGoing = false;
						break;
					case ChatMessage.MESSAGE:
						broadcast(this.username + ": " + this.cm.getMessage());
						break;
					case ChatMessage.MOVE:
						sendMove(this.cm.getCoordinate());
						break;
					case ChatMessage.BOOLEAN:
						sendBoolean(Boolean.parseBoolean(this.cm.getMessage()));
						break;
					case ChatMessage.UNTOUCHED_COORDINATE:
						sendUntouchedCoordinate(this.cm.getCoordinate());
						break;
					default:
						break;
				}
			}
			remove(this.id);
			close();
		}

		private synchronized void sendMove(final Coordinate coord) {
			for (int i = Server.this.al.size() - 1; i >= 0; i--) {
				final ClientThread ct = Server.this.al.get(i);
				if ((ct != this) && !ct.write(coord)) {
					Server.this.al.remove(i);
					display("Disconnected Client: " + ct.username);
				}
			}
		}

		private synchronized void sendBoolean(final Boolean b) {
			for (int i = Server.this.al.size() - 1; i >= 0; i--) {
				final ClientThread ct = Server.this.al.get(i);
				if ((ct != this) && !ct.write(b)) {
					Server.this.al.remove(i);
					display("Disconnected Client: " + ct.username);
				}
			}
		}

		private synchronized void sendUntouchedCoordinate(final Coordinate coord) {
			for (int i = Server.this.al.size() - 1; i >= 0; i--) {
				final ClientThread ct = Server.this.al.get(i);
				if ((ct != this) && !ct.write(UntouchedCoordinate.cast(coord))) {
					Server.this.al.remove(i);
					display("Disconnected Client: " + ct.username);
				}
			}
		}

		boolean write(final Object msg) {
			if (!this.socket.isConnected()) {
				close();
				return false;
			}
			try {
				this.sOutput.writeObject(msg);
				return true;
			} catch (final IOException e) {
				display("Error sending message to " + this.username);
				display(e.toString());
			}
			return true;
		}

		void close() {
			try {
				if (this.sOutput != null) {
					this.sOutput.close();
				}
				if (this.sInput != null) {
					this.sInput.close();
				}
				if (this.socket != null) {
					this.socket.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}