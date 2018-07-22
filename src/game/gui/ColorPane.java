package game.gui;

/*
 * Java Swing, 2nd Edition By Marc Loy, Robert Eckstein, Dave Wood, James
 * Elliott, Brian Cole ISBN: 0-596-00408-7 Publisher: O'Reilly
 */
// ColorPane.java
// A simple extension of JTextPane that allows the user to easily append
// colored text to the document.
//

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * The Class ColorPane. Java Swing, 2nd Edition By Marc Loy, Robert Eckstein,
 * Dave Wood, James Elliott, Brian Cole ISBN: 0-596-00408-7 Publisher: O'Reilly
 * ColorPane.java A simple extension of JTextPane that allows the user to easily
 * append colored text to the document.
 */
public final class ColorPane extends JTextPane {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -6368155746795082556L;

	/**
	 * Appends a colored text to the colored pane.
	 *
	 * @param c the color of the text to append
	 * @param s the text to append
	 */
	public void append(final Color c, final String s) {
		// StyleContext
		final StyleContext sc = StyleContext.getDefaultStyleContext();
		final AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		// int len = getDocument().getLength(); // same value as
		// getText().length();
		// setCaretPosition(len); // place caret at the end (with no selection)
		// setCharacterAttributes(aset, false);
		try {
			// credits to camickr from stackoverflow.com
			getStyledDocument().insertString(getStyledDocument().getLength(), s, aset);
		} catch(final BadLocationException ex) {
			ex.printStackTrace();
		}
		// replaceSelection(s); // there is no selection, so inserts at caret
	}
}
