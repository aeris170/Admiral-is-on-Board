package network;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;

import game.Game;
import game.gui.GUIUtils;
import game.player.Player;
import game.player.tiles.coordinate.Coordinate;
import game.player.tiles.coordinate.UntouchedCoordinate;

public final class Client {

	private int port;
	private String serverIP, username;

	ObjectInputStream sInput;
	ObjectOutputStream sOutput;

	private Socket socket;

	private ClientGUI cg;

	Client(final String serverIP, final int port, final String username, final ClientGUI cg) {
		this.serverIP = serverIP;
		this.port = port;
		this.username = username;
		this.cg = cg;
	}

	void display(final String msg) {
		this.cg.append(msg);
	}

	void disconnect() {
		try {
			if (this.sInput != null) {
				this.sInput.close();
			}
			if (this.sOutput != null) {
				this.sOutput.close();
			}
			if (this.socket != null) {
				this.socket.close();
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		Game.get().setGameState(Game.CONNECT_STATE);
	}

	public boolean start() {
		try {
			this.socket = new Socket("localhost", this.port);
			this.sInput = new ObjectInputStream(this.socket.getInputStream());
			this.sOutput = new ObjectOutputStream(this.socket.getOutputStream());
			final Thread listener = new Thread(new ServerListener());
			listener.start();
			this.sOutput.writeObject(this.username);
			return true;
		} catch (final Exception ex) {
			display("Error connecting to server:" + ex);
			ex.printStackTrace();
			return false;
		}
	}

	public synchronized void sendDisconnect() {
		try {
			this.sOutput.writeObject(new ChatMessage(ChatMessage.DISCONNECT, ""));
		} catch (final IOException ex) {
			display("Cannot establish connection with server. Restart connection or re-download");
			disconnect();
			ex.printStackTrace();
		}
	}

	public synchronized void sendMessage(final String msg) {
		try {
			this.sOutput.writeObject(new ChatMessage(ChatMessage.MESSAGE, msg));
		} catch (final IOException ex) {
			display("Cannot establish connection with server. Restart connection or re-download");
			disconnect();
			ex.printStackTrace();
		}
	}

	public synchronized void sendBoolean(final Boolean b) {
		try {
			this.sOutput.writeObject(new ChatMessage(ChatMessage.BOOLEAN, b));
		} catch (final IOException ex) {
			display("Cannot establish connection with server. Restart connection or re-download");
			disconnect();
			ex.printStackTrace();
		}
	}

	public synchronized void serializeMove(final Coordinate coord) {
		try {
			this.sOutput.writeObject(new ChatMessage(ChatMessage.MOVE, coord));
		} catch (final IOException ex) {
			display("Cannot establish connection with server. Restart connection or re-download");
			disconnect();
			ex.printStackTrace();
		}
	}

	public void serializeUntouchedCoordinate(final UntouchedCoordinate coord) {
		try {
			this.sOutput.writeObject(new ChatMessage(ChatMessage.UNTOUCHED_COORDINATE, coord));
		} catch (final IOException ex) {
			display("Cannot establish connection with server. Restart connection or re-download");
			disconnect();
			ex.printStackTrace();
		}
	}

	public synchronized void deserializeMove(final Coordinate coord) {
		try {
			final Game game = Game.get();
			final boolean hit = Player.Player1.checkHit(coord.getX(), coord.getY());
			this.sOutput.writeObject(new ChatMessage(ChatMessage.BOOLEAN, hit));
			Player.Player1.getFiredCoordinates().add(coord);
			if (hit) {
				Player.Player1.hit(coord);
			} else {
				Player.Player1.miss(coord);
				game.setGameState(Game.PLAYER_TURN);
			}
			game.getEventLog().append(Color.RED.darker().darker(), "Enemy shot " + coord.getX() + " | " + coord.getY() + ". " + (hit ? "Hit!" : "Miss!"));
			game.repaint();
			game.checkLoseCondition();
		} catch (final IOException ex) {
			display("Cannot establish connection with server. Restart connection or re-download");
			disconnect();
			ex.printStackTrace();
		}
	}

	public synchronized static void deserializeUntouchedCoordinate(final UntouchedCoordinate coord) {
		Player.Player2.addToUntouchedCoordinates(coord);
		Game.get().repaint();
	}

	private final class ServerListener implements Runnable {

		public ServerListener() {}

		@Override
		public void run() {
			while (true) {
				try {
					final Object data = Client.this.sInput.readObject();
					if (data instanceof String) {
						display((String) data);
					} else if ((data instanceof Coordinate) && !(data instanceof UntouchedCoordinate)) {
						deserializeMove((Coordinate) data);
					} else if (data instanceof Boolean) {
						final Game game = Game.get();
						if (!Player.Player2.isReady() && (Boolean) data) {
							Player.Player2.markReady();
							if (Player.Player1.isReady() && Player.Player2.isReady()) {
								game.purgeReadyButton();
								game.setGameState(Game.ENEMY_TURN);
							}
						} else {
							final Queue<Coordinate> queuedCoordinates = Game.get().getListener().getLastHits();
							final Coordinate topMostCoordinate = queuedCoordinates.poll();
							if ((Boolean) data) {
								Player.Player2.hit(topMostCoordinate);
								game.getEventLog().append(GUIUtils.DARK_GREEN, "You shot " + topMostCoordinate.getX() + " | " + topMostCoordinate.getY() + ". " + "Hit!");
							} else {
								Player.Player2.miss(topMostCoordinate);
								game.getEventLog().append(GUIUtils.DARK_GREEN,
								        "You shot " + topMostCoordinate.getX() + " | " + topMostCoordinate.getY() + ". " + "Miss!");
								game.setGameState(Game.ENEMY_TURN);
							}
							game.repaint();
							game.checkWinCondition();
						}
					} else if (data instanceof UntouchedCoordinate) {
						Client.deserializeUntouchedCoordinate((UntouchedCoordinate) data);
					} else {
						throw new RuntimeException("Client received unidentified data from server! Client.java, 151");
					}
				} catch (IOException | ClassNotFoundException ex) {
					display("Disconnected! Either server closed the connection, or server didn't respond.");
					disconnect();
					ex.printStackTrace();
					break;
				}
			}
		}
	}

}