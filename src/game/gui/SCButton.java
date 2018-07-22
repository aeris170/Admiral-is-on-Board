package game.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

public final class SCButton extends CustomButton {

	private static final long serialVersionUID = 7167603612415060943L;

	private String type;

	public SCButton(final Point location, final Dimension size, final String type) {
		super();
		super.setLocation(location);
		super.setSize(size);
		this.type = type;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setColor(GUIUtils.WHITE);
		g2d.setFont(GUIUtils.GEORGIA_BOLD_30);
		if(type.equals("sv")) {
			g2d.drawString("CLIENT", 14, 50);
		} else if(type.equals("cl")) {
			g2d.drawString("SERVER", 8, 50);
		}
	}
}
