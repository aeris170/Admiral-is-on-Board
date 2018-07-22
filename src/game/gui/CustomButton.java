package game.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class CustomButton extends JButton {

	private static final long serialVersionUID = 7167603612415060943L;

	public CustomButton() {
		super();
	}

	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;
		g2d.clearRect(0, 0, getWidth(), getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setStroke(GUIUtils.STROKE_1);
		g2d.setColor(GUIUtils.LIGHTER_RED);
		if(getModel().isRollover()) {
			g2d.setColor(GUIUtils.LIGHT_RED);
		}
		if(getModel().isPressed()) {
			g2d.setColor(GUIUtils.RED);
		}
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(GUIUtils.RED);
		g2d.drawRect(0, 0, getWidth(), getHeight());
	}
}
