package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import game.gui.EnemyPanel;
import game.gui.EventLog;
import game.gui.GUIUtils;
import game.gui.PlayerPanel;
import game.gui.ReadyButton;
import game.gui.SCButton;
import game.gui.ShipPanel;
import game.io.DragAndDropListener;
import game.player.Player;
import network.ClientGUI;
import network.ServerGUI;

public final class Game extends JPanel {

	private static final long serialVersionUID = -3076234224002180266L;

	public static final int CONNECT_STATE = 0, PREPARATION_STATE = 1, PLAYER_TURN = 2, ENEMY_TURN = 3, GAME_END_STATE = 4;

	private static Game INSTANCE = null;

	private PlayerPanel p1;
	private ShipPanel s7, s6, s5, s4, s3, s2, s1;
	private SCButton sv1, cl1;
	private ReadyButton b1;
	private EventLog t1;
	private EnemyPanel e1;
	private DragAndDropListener listener;
	private ServerGUI serverGUI;
	private ClientGUI clientGUI;

	private int gameState = 0;
	private boolean eventLogAdded = false;

	public Game() {
		super();
		super.setLayout(null);
		setUpGame();
		super.setBackground(GUIUtils.DARK_BLUE);
		Game.INSTANCE = this;
	}

	private void setUpGame() {
		super.removeAll();

		p1 = new PlayerPanel(GUIUtils.PLAYER_PANEL_POSITION, GUIUtils.PLAYER_PANEL_SIZE);
		s7 = new ShipPanel(GUIUtils.SHIP_7_PANEL_POSITION, GUIUtils.SHIP_7_PANEL_SIZE, 5);
		s6 = new ShipPanel(GUIUtils.SHIP_6_PANEL_POSITION, GUIUtils.SHIP_6_PANEL_SIZE, 4);
		s5 = new ShipPanel(GUIUtils.SHIP_5_PANEL_POSITION, GUIUtils.SHIP_5_PANEL_SIZE, 3);
		s4 = new ShipPanel(GUIUtils.SHIP_4_PANEL_POSITION, GUIUtils.SHIP_4_PANEL_SIZE, 2);
		s3 = new ShipPanel(GUIUtils.SHIP_3_PANEL_POSITION, GUIUtils.SHIP_3_PANEL_SIZE, 2);
		s2 = new ShipPanel(GUIUtils.SHIP_2_PANEL_POSITION, GUIUtils.SHIP_2_PANEL_SIZE, 1);
		s1 = new ShipPanel(GUIUtils.SHIP_1_PANEL_POSITION, GUIUtils.SHIP_1_PANEL_SIZE, 1);
		sv1 = new SCButton(GUIUtils.SERVER_BUTTON_POSITION, GUIUtils.SERVER_BUTTON_SIZE, "sv");
		sv1.addActionListener(e -> {
			clientGUI = new ClientGUI();
			remove(sv1);
			remove(cl1);
			repaint();
		});
		cl1 = new SCButton(GUIUtils.CLIENT_BUTTON_POSITION, GUIUtils.CLIENT_BUTTON_SIZE, "cl");
		cl1.addActionListener(e -> {
			serverGUI = new ServerGUI(GUIUtils.showInputDialog(Game.INSTANCE));
			remove(sv1);
			remove(cl1);
			repaint();
		});
		b1 = new ReadyButton(GUIUtils.READY_BUTTON_POSITION, GUIUtils.READY_BUTTON_SIZE);
		t1 = new EventLog(GUIUtils.EVENT_LOG_POSITION, GUIUtils.EVENT_LOG_SIZE);
		e1 = new EnemyPanel(GUIUtils.ENEMY_PANEL_POSITION, GUIUtils.ENEMY_PANEL_SIZE);

		super.add(sv1);
		super.add(cl1);

		listener = new DragAndDropListener();
		super.addMouseListener(listener);
		super.addMouseMotionListener(listener);
		repaint();
	}

	public void setGameState(final int gameState) {
		if(gameState == 0) {
			super.removeAll();
			super.add(sv1);
			super.add(cl1);
			revalidate();
			repaint();
		} else if(gameState == 1) {
			super.removeAll();
			super.add(s7);
			super.add(s6);
			super.add(s5);
			super.add(s4);
			super.add(s3);
			super.add(s2);
			super.add(s1);
			super.add(p1);
			super.add(b1);
			super.add(e1);
			revalidate();
			repaint();
		} else if(gameState == 2) {
			if(!eventLogAdded) {
				super.add(t1);
				eventLogAdded = true;
			}
			revalidate();
			repaint();
		} else if(gameState == 3) {
			if(!eventLogAdded) {
				super.add(t1);
				eventLogAdded = true;
			}
			revalidate();
			repaint();
		} else {
			return;
		}
		this.gameState = gameState;
	}

	public int getGameState() {
		return gameState;
	}

	public PlayerPanel getPlayerPanel() {
		return p1;
	}

	public ServerGUI getServerGUI() {
		return serverGUI;
	}

	public ClientGUI getClientGUI() {
		return clientGUI;
	}

	public EventLog getEventLog() {
		return t1;
	}

	public EnemyPanel getEnemyPanel() {
		return e1;
	}

	public DragAndDropListener getListener() {
		return listener;
	}

	public void purgeNetworkObjects() {
		clientGUI = null;
		serverGUI = null;
	}

	public void purgeReadyButton() {
		remove(b1);
	}

	public void checkLoseCondition() {
		if(Player.Player1.getHitCoordinates().size() == Player.TOTAL_SHIP_PART_PER_PLAYER) {
			System.out.println("END!");
			Player.Player1.sendUntouchedCoordinates();
			GUIUtils.showEndGameDialog(Game.INSTANCE, "You Lost!               ");
			super.removeMouseListener(listener);
			super.removeMouseMotionListener(listener);
		}
	}

	public void checkWinCondition() {
		if(Player.Player2.getHitCoordinates().size() == Player.TOTAL_SHIP_PART_PER_PLAYER) {
			System.out.println("END!");
			Player.Player1.sendUntouchedCoordinates();
			GUIUtils.showEndGameDialog(Game.INSTANCE, "You Won!              ");
			super.removeMouseListener(listener);
			super.removeMouseMotionListener(listener);
		}
	}

	public static Game get() {
		if(Game.INSTANCE == null) {
			Game.INSTANCE = new Game();
		}
		return Game.INSTANCE;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setFont(GUIUtils.GEORGIA_BOLD_20);
		if(gameState == 0) {
			g2d.setColor(GUIUtils.YELLOW);
			g2d.drawString("CONNECT YOUR OPPONENT", 605, 45);
			return;
		}
		g2d.setColor(GUIUtils.DARK_GREEN);
		g2d.setFont(GUIUtils.GEORGIA_BOLD_50);
		g2d.drawString("YOUR FLEET", 125, 55);
		g2d.setColor(GUIUtils.DARK_RED);
		g2d.drawString("ENEMY FLEET", 1025, 55);
		g2d.setColor(GUIUtils.DARK_GREEN);
		g2d.setFont(GUIUtils.GEORGIA_BOLD_20);
		g2d.setColor(GUIUtils.YELLOW);
		if(gameState == 1) {
			g2d.drawString("PLACE YOUR SHIPS AND PRESS READY", 540, 45);
			g2d.drawString("RIGHT MOUSE TO ROTATE", 610, 70);
			return;
		}
		g2d.drawString("Event Log", 700, 295);
		if(gameState == 2) {
			g2d.setColor(GUIUtils.DARK_GREEN);
			g2d.drawString("YOUR TURN", 690, 45);
			return;
		}
		if(gameState == 3) {
			g2d.setColor(GUIUtils.DARK_RED);
			g2d.drawString("ENEMIES TURN", 670, 45);
			return;
		}
	}
}
