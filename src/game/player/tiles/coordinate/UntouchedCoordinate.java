package game.player.tiles.coordinate;

public final class UntouchedCoordinate extends Coordinate {

	private static final long serialVersionUID = 4535120448797832209L;

	private UntouchedCoordinate(final int x, final int y) {
		super(x, y);
	}

	public static UntouchedCoordinate cast(final Coordinate c) {
		return new UntouchedCoordinate(c.getX(), c.getY());
	}
}
