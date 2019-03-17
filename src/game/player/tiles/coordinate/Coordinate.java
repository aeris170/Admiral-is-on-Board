package game.player.tiles.coordinate;

import java.io.Serializable;

public class Coordinate implements Serializable {

	private static final long serialVersionUID = 7589922924110199104L;

	private int x;
	private int y;

	public Coordinate(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return this.x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return this.x + " | " + this.y;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Coordinate)) {
			return false;
		}
		final Coordinate o2 = (Coordinate) o;
		if ((this.x == o2.x) && (this.y == o2.y)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (this.x * 17) + (this.y * 93);
	}
}
