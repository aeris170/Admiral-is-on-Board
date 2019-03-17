package game.player;

import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.player.tiles.Tiles;
import game.player.tiles.coordinate.Coordinate;
import game.player.tiles.coordinate.UntouchedCoordinate;

public final class Player {

	public static final int TOTAL_SHIP_PART_PER_PLAYER = 18;

	public static final Player Player1 = new Player();
	public static final Player Player2 = new Player();

	private boolean ready;
	private Tiles tiles;
	private List<Coordinate> firedCoordinates;
	private List<Coordinate> missedCoordinates;
	private List<Coordinate> hitCoordinates;
	private List<UntouchedCoordinate> untouchedCoordinates;

	private Player() {
		this.ready = false;
		this.tiles = new Tiles();
		this.firedCoordinates = new ArrayList<>();
		this.missedCoordinates = new ArrayList<>();
		this.hitCoordinates = new ArrayList<>();
		this.untouchedCoordinates = new ArrayList<>();
	}

	public boolean isReady() {
		return this.ready;
	}

	public void markReady() {
		this.ready = true;
	}

	public boolean[][] getTiles() {
		return this.tiles.getTiles();
	}

	public List<Coordinate> getFiredCoordinates() {
		return this.firedCoordinates;
	}

	public List<Coordinate> getHitCoordinates() {
		return this.hitCoordinates;
	}

	public List<Coordinate> getMissedCoordinates() {
		return this.missedCoordinates;
	}

	public List<UntouchedCoordinate> getUntouchedCoordinates() {
		return this.untouchedCoordinates;
	}

	public void addToCoordinates(final Coordinate coordinate) {
		this.firedCoordinates.add(coordinate);
	}

	public void addToUntouchedCoordinates(final UntouchedCoordinate coord) {
		this.untouchedCoordinates.add(coord);
	}

	public void hit(final Coordinate coordinate) {
		this.hitCoordinates.add(coordinate);
	}

	public void miss(final Coordinate coordinate) {
		this.missedCoordinates.add(coordinate);
	}

	public boolean hasPlacedAllShips() {
		int counter = 0;
		final boolean[][] tiles = this.tiles.getTiles();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				if (tiles[i][j]) {
					counter++;
				}
			}

		}
		return counter == Player.TOTAL_SHIP_PART_PER_PLAYER;
	}

	public void sendUntouchedCoordinates() {
		final Game game = Game.get();
		final boolean[][] tiles = this.tiles.getTiles();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				final Coordinate candidate = new Coordinate(i, j);
				if (tiles[i][j] && !this.hitCoordinates.contains(candidate)) {
					if (game.getClientGUI() != null) {
						game.getClientGUI().getClient().serializeUntouchedCoordinate(UntouchedCoordinate.cast(candidate));
					} else if (game.getServerGUI() != null) {
						game.getServerGUI().getAutoClient().getClient().serializeUntouchedCoordinate(UntouchedCoordinate.cast(candidate));
					}
				}
			}
		}
	}

	public boolean placeShip(final int length, final int orientation, final int x, final int y) {
		if (orientation == 0) {
			return this.tiles.placeVerticalShip(length, x, y);
		} else if (orientation == 1) {
			return this.tiles.placeHorizontalShip(length, x, y);
		} else {
			return false;
		}
	}

	public boolean checkHit(final int x, final int y) {
		return this.tiles.checkHit(x, y);
	}

	public String tilesPrettyPrint() {
		return this.tiles.prettyPrint();
	}
}
