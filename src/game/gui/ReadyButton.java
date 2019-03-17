package game.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import game.Game;
import game.player.Player;

public final class ReadyButton extends CustomButton {

	private static final long serialVersionUID = 8679953310338283374L;

	private boolean ready;

	public ReadyButton(final Point location, final Dimension size) {
		super();
		super.setLocation(location);
		super.setSize(size);
		addActionListener(e -> {
			final Game game = Game.get();
			final Player player = Player.Player1;
			final Player enemy = Player.Player2;

			if (player.hasPlacedAllShips()) {
				player.markReady();
				if (game.getServerGUI() != null) {
					game.getServerGUI().getAutoClient().getClient().sendBoolean(true);
				} else if (game.getClientGUI() != null) {
					game.getClientGUI().getClient().sendBoolean(true);
				}
				this.ready = true;
				if (player.isReady() && enemy.isReady()) {
					game.purgeReadyButton();
					game.setGameState(Game.PLAYER_TURN);
				}
			}
		});
		this.ready = false;
	}

	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D g2d = (Graphics2D) g;
		g2d.clearRect(0, 0, getWidth(), getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setStroke(GUIUtils.STROKE_3);
		g2d.setColor(GUIUtils.LIGHTER_RED);
		if (getModel().isRollover()) {
			g2d.setColor(GUIUtils.LIGHT_RED);
		}
		if (getModel().isPressed()) {
			if (this.ready) {
				g2d.setColor(GUIUtils.GREEN);
			} else {
				g2d.setColor(GUIUtils.RED);
			}
		}
		if (this.ready) {
			g2d.setColor(GUIUtils.GREEN);
		}
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(GUIUtils.WHITE);
		g2d.setFont(GUIUtils.GEORGIA_BOLD_30);
		g2d.drawString("READY", 17, 50);
	}
}
