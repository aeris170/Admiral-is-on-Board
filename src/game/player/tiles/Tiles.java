package game.player.tiles;

public final class Tiles {

	private boolean[][] tiles;

	public Tiles() {
		this.tiles = new boolean[10][10];
	}

	public boolean placeVerticalShip(final int length, final int x, final int y) {
		if ((y + length) < (this.tiles.length + 1)) {
			if ((y > 0) && this.tiles[x][y - 1]) {
				return false;
			}
			for (int i = 0; i < length; i++) {
				if ((x > 0) && this.tiles[x - 1][y + i]) {
					return false;
				}
				if ((x < (this.tiles[i].length - 1)) && this.tiles[x + 1][y + i]) {
					return false;
				}
			}
			if (((y + length) < this.tiles.length) && this.tiles[x][y + length]) {
				return false;
			}
			for (int i = 0; i < length; i++) {
				if (this.tiles[x][y + i]) {
					return false;
				}
			}
			for (int i = 0; i < length; i++) {
				this.tiles[x][y + i] = true;
			}
			return true;
		}
		return false;
	}

	public boolean placeHorizontalShip(final int length, final int x, final int y) {
		if ((x + length) < (this.tiles.length + 1)) {
			if ((x > 0) && this.tiles[x - 1][y]) {
				return false;
			}
			for (int i = 0; i < length; i++) {
				if ((y > 0) && this.tiles[x + i][y - 1]) {
					return false;
				}
				if ((y < (this.tiles[i].length - 1)) && this.tiles[x + i][y + 1]) {
					return false;
				}
			}
			if (((x + length) < this.tiles.length) && this.tiles[x + length][y]) {
				return false;
			}
			for (int i = 0; i < length; i++) {
				if (this.tiles[x + i][y] == true) {
					return false;
				}
			}
			for (int i = 0; i < length; i++) {
				this.tiles[x + i][y] = true;
			}
			return true;
		}
		return false;
	}

	public boolean checkHit(final int x, final int y) {
		return this.tiles[x][y];
	}

	public boolean[][] getTiles() {
		return this.tiles;
	}

	public String prettyPrint() {
		String s = "";
		for (final boolean[] ba : this.tiles) {
			for (final boolean b : ba) {
				if (b) {
					s += "[T]";
				} else {
					s += "[f]";
				}
			}
			s += "\n";
		}
		return s;
	}
}
