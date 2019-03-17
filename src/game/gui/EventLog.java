package game.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public final class EventLog extends JPanel {

	private static final long serialVersionUID = 1571854589904395683L;

	private JScrollPane sp;
	private ColorPane cp;

	public EventLog(final Point location, final Dimension size) {
		cp = new ColorPane();
		sp = new JScrollPane(cp, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		cp.setEditable(true);
		cp.setFont(GUIUtils.GEORGIA_BOLD_16);
		cp.setBackground(GUIUtils.DARK_BLUE);
		sp.setBorder(null);
		sp.setPreferredSize(new Dimension((int) (size.getWidth() - 10), (int) (size.getHeight() - 10)));
		sp.getVerticalScrollBar().setUI(new ScrollBarUI());
		super.setOpaque(false);
		super.add(sp);
		super.setLocation(location);
		super.setSize(size);
	}

	public void append(final Color c, final String s) {
		boolean willScroll = false;
		if (shouldScroll()) {
			willScroll = true;
		}
		cp.append(c, s + "\n");
		if (willScroll) {
			final JScrollBar sb = sp.getVerticalScrollBar();
			sb.setValue(sb.getMaximum());
		}
		repaint();
	}

	public boolean shouldScroll() {
		final int minimumValue = sp.getVerticalScrollBar().getValue() + sp.getVerticalScrollBar().getVisibleAmount();
		final int maximumValue = sp.getVerticalScrollBar().getMaximum();
		return maximumValue == minimumValue;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.setColor(GUIUtils.LIGHT_BLUE);
		g.fillRect(0, 0, 250, 200);
	}
}
