package game.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public abstract class GUIUtils {

	public static final Point PLAYER_PANEL_POSITION = new Point(0, 100);
	public static final Point SHIP_7_PANEL_POSITION = new Point(675, 150);
	public static final Point SHIP_6_PANEL_POSITION = new Point(690, 200);
	public static final Point SHIP_5_PANEL_POSITION = new Point(705, 250);
	public static final Point SHIP_4_PANEL_POSITION = new Point(720, 300);
	public static final Point SHIP_3_PANEL_POSITION = new Point(720, 350);
	public static final Point SHIP_2_PANEL_POSITION = new Point(735, 400);
	public static final Point SHIP_1_PANEL_POSITION = new Point(735, 450);
	public static final Point SERVER_BUTTON_POSITION = new Point(550, 250);
	public static final Point CLIENT_BUTTON_POSITION = new Point(800, 250);
	public static final Point READY_BUTTON_POSITION = new Point(675, 550);
	public static final Point EVENT_LOG_POSITION = new Point(625, 300);
	public static final Point ENEMY_PANEL_POSITION = new Point(900, 100);

	public static final Dimension PLAYER_PANEL_SIZE = new Dimension(605, 605);
	public static final Dimension SHIP_7_PANEL_SIZE = new Dimension(150, 30);
	public static final Dimension SHIP_6_PANEL_SIZE = new Dimension(120, 30);
	public static final Dimension SHIP_5_PANEL_SIZE = new Dimension(90, 30);
	public static final Dimension SHIP_4_PANEL_SIZE = new Dimension(60, 30);
	public static final Dimension SHIP_3_PANEL_SIZE = new Dimension(60, 30);
	public static final Dimension SHIP_2_PANEL_SIZE = new Dimension(30, 30);
	public static final Dimension SHIP_1_PANEL_SIZE = new Dimension(30, 30);
	public static final Dimension SERVER_BUTTON_SIZE = new Dimension(150, 80);
	public static final Dimension CLIENT_BUTTON_SIZE = new Dimension(150, 80);
	public static final Dimension READY_BUTTON_SIZE = new Dimension(150, 80);
	public static final Dimension EVENT_LOG_SIZE = new Dimension(250, 200);
	public static final Dimension ENEMY_PANEL_SIZE = new Dimension(605, 605);

	public static final Color NO_COLOR = new Color(0, 0, 0, 0);
	public static final Color WHITE = Color.WHITE;
	public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
	public static final Color GRAY = new Color(160, 160, 160);
	public static final Color DARK_GRAY = GUIUtils.GRAY.darker();
	public static final Color GREEN = Color.GREEN;
	public static final Color LIGHT_GREEN = GUIUtils.GREEN.brighter();
	public static final Color DARK_GREEN = GUIUtils.GREEN.darker().darker();
	public static final Color RED = Color.RED;
	public static final Color LIGHTER_RED = new Color(255, 0, 0, 100);
	public static final Color LIGHT_RED = new Color(255, 0, 0, 160);
	public static final Color DARK_RED = GUIUtils.RED.darker().darker();
	public static final Color YELLOW = Color.YELLOW;
	public static final Color DARK_BLUE = new Color(22, 44, 66);
	public static final Color LIGHT_BLUE = GUIUtils.DARK_BLUE.brighter().brighter().brighter();

	public static final Font GEORGIA_BOLD_12 = new Font("Georgia", Font.BOLD, 12);
	public static final Font GEORGIA_BOLD_16 = new Font("Georgia", Font.BOLD, 16);
	public static final Font GEORGIA_BOLD_20 = new Font("Georgia", Font.BOLD, 20);
	public static final Font GEORGIA_BOLD_30 = new Font("Georgia", Font.BOLD, 30);
	public static final Font GEORGIA_BOLD_50 = new Font("Georgia", Font.BOLD, 50);

	public static final BasicStroke STROKE_0 = new BasicStroke(0);
	public static final BasicStroke STROKE_1 = new BasicStroke(1);
	public static final BasicStroke STROKE_3 = new BasicStroke(3);
	public static final BasicStroke STROKE_5 = new BasicStroke(5);

	public static final Border LIGHT_BLUE_BORDER = BorderFactory.createLineBorder(GUIUtils.LIGHT_BLUE, 1, false);
	public static final Border LIGHT_BLUE_BORDER_2 = BorderFactory.createLineBorder(GUIUtils.LIGHT_BLUE, 2, false);

	public static BufferedImage EX_MARK;
	static {
		try {
			GUIUtils.EX_MARK = ImageIO.read(GUIUtils.class.getResourceAsStream("/exmark.png"));
		} catch(final IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void recursivePaint(final Container ct) {
		for(final Component c : ct.getComponents()) {
			if(c instanceof Container) {
				c.setBackground(GUIUtils.DARK_BLUE);
				c.setForeground(GUIUtils.WHITE);
				GUIUtils.recursivePaint((Container) c);
			}
		}
	}

	// THANKS TO MadProgrammer on StackOverflow.com, he almost completely wrote
	// this function by himself because I ****ed up while writing it.
	public static int showInputDialog(final Container parent) {

		final JLabel label = new JLabel("Enter an Open Port: ", SwingConstants.LEFT);
		label.setForeground(GUIUtils.WHITE);

		final JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.setOpaque(true);
		panel.setBackground(GUIUtils.DARK_BLUE);
		final JTextField inputField = new JTextField(10);
		inputField.setCaretColor(GUIUtils.WHITE);
		inputField.setBorder(GUIUtils.LIGHT_BLUE_BORDER);
		inputField.requestFocus();
		panel.add(label);
		panel.add(inputField);

		final JButton button = new JButton("OK") {

			private static final long serialVersionUID = -4808194362293478299L;

			@Override
			public int getWidth() {
				return 51;
			}

			@Override
			public int getHeight() {
				return 26;
			}

			@Override
			public void paintComponent(final Graphics g) {
				final Graphics2D g2d = (Graphics2D) g;
				g2d.clearRect(0, 0, getWidth(), getHeight());
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2d.setStroke(GUIUtils.STROKE_0);
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
				g2d.setColor(GUIUtils.WHITE);
				g2d.setFont(GUIUtils.GEORGIA_BOLD_12);
				g2d.drawString("OK", 15, 18);
			}
		};

		final JOptionPane optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_OPTION, UIManager.getIcon("OptionPane.questionIcon"),
				new JButton[] {button}, button);
		button.addActionListener(e -> {
			optionPane.setInputValue(inputField.getText());
			optionPane.setValue(JOptionPane.OK_OPTION);
		});

		optionPane.setOpaque(true);
		optionPane.setBackground(GUIUtils.DARK_BLUE);
		GUIUtils.recursivePaint(optionPane);
		final JDialog d = optionPane.createDialog(parent, "Open port required!");
		d.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(final WindowEvent e) {
				inputField.requestFocus();
			}
		});
		d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		d.setContentPane(optionPane);
		d.pack();
		d.setLocationRelativeTo(parent);
		d.setVisible(true);
		return Integer.parseInt((String) optionPane.getInputValue());
	}

	public static void showEndGameDialog(final Container parent, final String message) {
		final JLabel label = new JLabel(message, SwingConstants.CENTER);
		label.setOpaque(true);
		label.setBackground(GUIUtils.DARK_BLUE);
		label.setForeground(GUIUtils.WHITE);

		final JButton button = new JButton("OK") {

			private static final long serialVersionUID = -4808194362293478299L;

			@Override
			public int getWidth() {
				return 51;
			}

			@Override
			public int getHeight() {
				return 26;
			}

			@Override
			public void paintComponent(final Graphics g) {
				final Graphics2D g2d = (Graphics2D) g;
				g2d.clearRect(0, 0, getWidth(), getHeight());
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2d.setStroke(GUIUtils.STROKE_0);
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
				g2d.setColor(GUIUtils.WHITE);
				g2d.setFont(GUIUtils.GEORGIA_BOLD_12);
				g2d.drawString("OK", 15, 18);
			}
		};
		button.addActionListener(e -> {
			SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
		});

		final JOptionPane optionPane = new JOptionPane(label, JOptionPane.WARNING_MESSAGE, JOptionPane.OK_OPTION, null, new JButton[] {button}, button);
		optionPane.setOpaque(true);
		optionPane.setBackground(GUIUtils.DARK_BLUE);
		GUIUtils.recursivePaint(optionPane);
		final JDialog d = optionPane.createDialog(parent, "Game Over!");
		d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		d.setContentPane(optionPane);
		d.pack();
		d.setLocationRelativeTo(parent);
		d.setVisible(true);
	}
}
