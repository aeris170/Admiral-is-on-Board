package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.Game;

/**
 * This class extends JFrame & holds the main method. The sole purpose of this
 * class is to create and show a GUI on Java Event Dispatch Thread.
 *
 * @author Doga Oruc
 * @version 14.06.2018
 * @see SwingUtilities
 * @see JFrame
 * @see Game
 */
public final class Window extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5087033667293723326L;

	/**
	 * Instantiates a new window.
	 *
	 * @param nameOfWindow the title of the application window
	 */
	public Window(final String nameOfWindow) {
		super(nameOfWindow);
		super.add(new Game());
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(() -> {
			Window.createAndShowGUI();
		});
	}

	/**
	 * Creates a GUI on EDT.
	 */
	private static void createAndShowGUI() {
		final Window game = new Window("Admiral is on Board");
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// TODO f.setIconImage(icon);
		game.setSize(1509, 732);
		game.setResizable(false);
		game.setLocationByPlatform(true);
		game.setVisible(true);
	}
}
