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
		return lastHits;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent) */
	@Override
	public void mousePressed(final MouseEvent e) {
		final int clickedMouseButton = e.getButton();
		if (clickedMouseButton == MouseEvent.BUTTON1) {
			pressed = e;
			final Game game = Game.get();
			if (game.getComponentAt(e.getPoint()) instanceof ShipPanel) {
				movingShip = (ShipPanel) game.getComponentAt(e.getPoint());
				movingShip.setSize(movingShip.getWidth() * 2, movingShip.getHeight() * 2);
				movingShip.setLocation(pressed.getX() - movingShip.getLength() * 7, pressed.getY() - 30);
			}
			if (game.getComponentAt(pressed.getPoint()) instanceof EnemyPanel) {
				if (game.getGameState() == Game.PLAYER_TURN) {
					final Coordinate clicked = DragAndDropListener.translateforEnemyPanel(pressed.getX(), pressed.getY());
					if (!Player.Player2.getFiredCoordinates().contains(clicked)) {
						Player.Player2.addToCoordinates(clicked);
						if (game.getClientGUI() != null) {
							game.getClientGUI().getClient().serializeMove(clicked);
						} else if (game.getServerGUI() != null) {
							game.getServerGUI().getAutoClient().getClient().serializeMove(clicked);
						}
						lastHits.add(clicked);
					}
				}
			}
			game.repaint();
		} else if (clickedMouseButton == MouseEvent.BUTTON3) {
			if (movingShip != null) {
				movingShip.setSize(movingShip.getHeight(), movingShip.getWidth());
				movingShip.setVertical(!movingShip.isVertical());
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
			if (movingShip != null) {
				movingShip.setLocation(movingShip.getInitialLocation());
				movingShip.setSize(movingShip.getLength() * 30, 30);
				if (game.getComponentAt(e.getPoint()) instanceof PlayerPanel) {
					if (movingShip.isVertical()) {
						if (Player.Player1.placeShip(movingShip.getLength(), 0, e.getX() / 60, (e.getY() - 100) / 60)) {
							game.remove(movingShip);
						}
					} else {
						if (Player.Player1.placeShip(movingShip.getLength(), 1, e.getX() / 60, (e.getY() - 100) / 60)) {
							game.remove(movingShip);
						}
					}
				} else {
					movingShip.setVertical(false);
				}
				movingShip.setVertical(false);
				movingShip = null;
			}
		}
		game.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent) */
	@Override
	public void mouseDragged(final MouseEvent e) {
		if (movingShip != null) {
			location = movingShip.getLocation(location);
			movingShip.setLocation(location.x - pressed.getX() + e.getX(), location.y - pressed.getY() + e.getY());
			pressed = e;
		}
	}
}
