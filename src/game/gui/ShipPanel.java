package game.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public final class ShipPanel extends JPanel {

	private static final long serialVersionUID = -8468753150250871605L;

	private int shipLength;
	private boolean vertical = false;
	private Point initialLocation;

	public ShipPanel(final Point location, final Dimension size, final int shipLength) {
		super();
		super.setOpaque(false);
		super.setLocation(location);
		super.setSize(size);
		this.shipLength = shipLength;
		initialLocation = location;
	}

	public int getLength() {
		return shipLength;
	}

	public Point getInitialLocation() {
		return initialLocation;
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical(final boolean vertical) {
		this.vertical = vertical;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(GUIUtils.STROKE_5);
		if (!vertical) {
			for (int i = 0; i < shipLength; i++) {
				g2d.setColor(GUIUtils.LIGHT_GRAY.brighter());
				g2d.drawRect(i * getWidth() / shipLength, 0, getWidth(), getHeight());
				g.setColor(GUIUtils.DARK_GREEN);
				g.fillRect(i * getWidth() / shipLength + getWidth() / 15 / shipLength, getHeight() / 15, 13 * getWidth() / 15 / shipLength, 13 * getHeight() / 15);
			}
		} else {
			for (int i = 0; i < shipLength; i++) {
				g2d.setColor(GUIUtils.LIGHT_GRAY.brighter());
				g2d.drawRect(0, i * getHeight() / shipLength, getWidth(), getHeight());
				g.setColor(GUIUtils.DARK_GREEN);
				g.fillRect(getWidth() / 15, i * getHeight() / shipLength + getHeight() / 15 / shipLength, 13 * getWidth() / 15, 13 * getHeight() / 15);
			}
		}
	}
}