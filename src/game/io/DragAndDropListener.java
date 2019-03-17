package game.io;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.swing.event.MouseInputAdapter;

import game.Game;
import game.gui.EnemyPanel;
import game.gui.PlayerPanel;
import game.gui.ShipPanel;
import game.player.Player;
import game.player.tiles.coordinate.Coordinate;

public final class DragAndDropListener extends MouseInputAdapter {

	private Point location;
	private MouseEvent pressed;

	private ShipPanel movingShip = null;
	private Queue<Coordinate> lastHits = new ArrayDeque<>();

	private static Coordinate translateforEnemyPanel(final int x, final int y) {
		return new Coordinate((x - 900) / 60, (y - 100) / 60);
	}

	public Queue<Coordinate> getLastHits() {
		return this.lastHits;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent) */
	@Override
	public void mousePressed(final MouseEvent e) {
		final int clickedMouseButton = e.getButton();
		if (clickedMouseButton == MouseEvent.BUTTON1) {
			this.pressed = e;
			final Game game = Game.get();
			if (game.getComponentAt(e.getPoint()) instanceof ShipPanel) {
				this.movingShip = (ShipPanel) game.getComponentAt(e.getPoint());
				this.movingShip.setSize(this.movingShip.getWidth() * 2, this.movingShip.getHeight() * 2);
				this.movingShip.setLocation(this.pressed.getX() - (this.movingShip.getLength() * 7), this.pressed.getY() - 30);
			}
			if (game.getComponentAt(this.pressed.getPoint()) instanceof EnemyPanel) {
				if (game.getGameState() == Game.PLAYER_TURN) {
					final Coordinate clicked = DragAndDropListener.translateforEnemyPanel(this.pressed.getX(), this.pressed.getY());
					if (!Player.Player2.getFiredCoordinates().contains(clicked)) {
						Player.Player2.addToCoordinates(clicked);
						if (game.getClientGUI() != null) {
							game.getClientGUI().getClient().serializeMove(clicked);
						} else if (game.getServerGUI() != null) {
							game.getServerGUI().getAutoClient().getClient().serializeMove(clicked);
						}
						this.lastHits.add(clicked);
					}
				}
			}
			game.repaint();
		} else if (clickedMouseButton == MouseEvent.BUTTON3) {
			if (this.movingShip != null) {
				this.movingShip.setSize(this.movingShip.getHeight(), this.movingShip.getWidth());
				this.movingShip.setVertical(!this.movingShip.isVertical());
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent) */
	@Override
	public void mouseReleased(final MouseEvent e) {
		final int clickedMouseButton = e.getButton();
		final Game game = Game.get();
		if (clickedMouseButton == MouseEvent.BUTTON1) {
			if (this.movingShip != null) {
				this.movingShip.setLocation(this.movingShip.getInitialLocation());
				this.movingShip.setSize(this.movingShip.getLength() * 30, 30);
				if (game.getComponentAt(e.getPoint()) instanceof PlayerPanel) {
					if (this.movingShip.isVertical()) {
						if (Player.Player1.placeShip(this.movingShip.getLength(), 0, e.getX() / 60, (e.getY() - 100) / 60)) {
							game.remove(this.movingShip);
						}
					} else {
						if (Player.Player1.placeShip(this.movingShip.getLength(), 1, e.getX() / 60, (e.getY() - 100) / 60)) {
							game.remove(this.movingShip);
						}
					}
				} else {
					this.movingShip.setVertical(false);
				}
				this.movingShip.setVertical(false);
				this.movingShip = null;
			}
		}
		game.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent) */
	@Override
	public void mouseDragged(final MouseEvent e) {
		if (this.movingShip != null) {
			this.location = this.movingShip.getLocation(this.location);
			this.movingShip.setLocation((this.location.x - this.pressed.getX()) + e.getX(), (this.location.y - this.pressed.getY()) + e.getY());
			this.pressed = e;
		}
	}
}
