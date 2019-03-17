package game.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import game.player.Player;
import game.player.tiles.coordinate.Coordinate;

public final class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 3440641188594063602L;

	private Player owner;

	public PlayerPanel(final Point location, final Dimension size) {
		super();
		this.owner = Player.Player1;
		super.setLocation(location);
		super.setSize(size);
		super.setBackground(GUIUtils.DARK_BLUE);
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(GUIUtils.STROKE_5);
		for (int i = 0; i < this.owner.getTiles().length; i++) {
			for (int j = 0; j < this.owner.getTiles()[i].length; j++) {
				g2d.setColor(GUIUtils.WHITE);
				g2d.drawRect(i * 60, j * 60, 60, 60);
			}
		}
		for (int i = 0; i < this.owner.getTiles().length; i++) {
			for (int j = 0; j < this.owner.getTiles()[i].length; j++) {
				if (this.owner.getTiles()[i][j]) {
					g2d.setColor(GUIUtils.DARK_GRAY);
					g2d.drawRect(i * 60, j * 60, 60, 60);
					g.setColor(GUIUtils.LIGHT_GREEN);
					g.fillRect((i * 60) + 3, (j * 60) + 3, 55, 55);
				}
			}
		}
		final List<Coordinate> hitCoordinates = this.owner.getHitCoordinates();
		if (!hitCoordinates.isEmpty()) {
			for (int i = 0; i < hitCoordinates.size(); i++) {
				final Coordinate hit = hitCoordinates.get(i);
				final int xx = hit.getX();
				final int yy = hit.getY();
				g2d.setColor(GUIUtils.DARK_RED);
				g2d.drawLine(xx * 60, yy * 60, (xx * 60) + 60, (yy * 60) + 60);
				g2d.drawLine((xx * 60) + 60, yy * 60, xx * 60, (yy * 60) + 60);
			}
		}
		final List<Coordinate> missedCoordinates = this.owner.getMissedCoordinates();
		if (!missedCoordinates.isEmpty()) {
			for (int i = 0; i < missedCoordinates.size(); i++) {
				final Coordinate miss = missedCoordinates.get(i);
				final int xx = miss.getX();
				final int yy = miss.getY();
				g2d.setColor(GUIUtils.GRAY);
				g.fillRect((xx * 60) + 3, (yy * 60) + 3, 55, 55);
			}
		}
	}
}
