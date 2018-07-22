package game.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ScrollBarUI extends BasicScrollBarUI {

	final Dimension d = new Dimension(0, 0);

	@Override
	protected JButton createDecreaseButton(final int orientation) {
		return new JButton() {

			private static final long serialVersionUID = -4322114750031668096L;

			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};
	}

	@Override
	protected JButton createIncreaseButton(final int orientation) {
		return new JButton() {

			private static final long serialVersionUID = 2515535956924569053L;

			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};
	}

	@Override
	protected void paintTrack(final Graphics g, final JComponent c, final Rectangle r) {
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(GUIUtils.DARK_BLUE);
		g2d.fillRect(r.x, r.y, r.width, r.height);
	}

	@Override
	protected void paintThumb(final Graphics g, final JComponent c, final Rectangle r) {
		final Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color color = null;
		final JScrollBar sb = (JScrollBar) c;
		if(!sb.isEnabled() || (r.width > r.height)) {
			return;
		} else if(isDragging) {
			color = GUIUtils.GRAY;
		} else if(isThumbRollover()) {
			color = GUIUtils.LIGHT_GRAY;
		} else {
			color = GUIUtils.WHITE;
		}
		g2.setPaint(color);
		g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);
		g2.setPaint(color);
		g2.drawRoundRect(r.x, r.y, r.width, r.height, 20, 20);
		g2.dispose();
	}

	@Override
	protected void setThumbBounds(final int x, final int y, final int width, final int height) {
		super.setThumbBounds(x, y, width, height);
		scrollbar.repaint();
	}
}