package network;

import java.io.Serializable;

import game.player.tiles.coordinate.Coordinate;

public final class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	static final int DISCONNECT = 0, MESSAGE = 1, MOVE = 2, BOOLEAN = 3, UNTOUCHED_COORDINATE = 4;

	private int type;

	private String message;
	private Coordinate coord;

	public ChatMessage(final int type, final String msg) {
		this.type = type;
		message = msg;
	}

	public ChatMessage(final int type, final boolean b) {
		this.type = type;
		message = new Boolean(b).toString();
	}

	public ChatMessage(final int type, final Coordinate coord) {
		this.type = type;
		this.coord = coord;
	}

	int getType() {
		return type;
	}

	String getMessage() {
		return message;
	}

	Coordinate getCoordinate() {
		return coord;
	}
}