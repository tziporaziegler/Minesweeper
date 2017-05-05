package minesweeper;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class SmileyButton extends JButton {
	private static final long serialVersionUID = 1L;

	private final static EmptyBorder SMILEY_BORDER = new EmptyBorder(0, 0, 0, 0);

	public static final ImageIcon SMILEY_PLAY = new ImageIcon(Board.class.getResource("pics/smileblue.png"));
	private static final ImageIcon SMILEY_GUESS = new ImageIcon(Board.class.getResource("pics/smileguessblue.png"));
	private static final ImageIcon SMILEY_DEAD = new ImageIcon(Board.class.getResource("pics/smiledead.png"));
	private static final ImageIcon SMILEY_WIN = new ImageIcon(Board.class.getResource("pics/smilewin.png"));

	public SmileyButton(ActionListener newGameListener) {
		reset();

		setBackground(null);
		setBorder(SMILEY_BORDER);
		addActionListener(newGameListener);
	}

	// TODO add picture of pressed smiley that will display while smiley is pressed
	// https://docs.oracle.com/javase/7/docs/api/javax/swing/AbstractButton.html#PRESSED_ICON_CHANGED_PROPERTY

	public void guess() {
		setIcon(SMILEY_GUESS);
	}

	public void die() {
		setIcon(SMILEY_DEAD);
	}

	public void win() {
		setIcon(SMILEY_WIN);
	}

	public void reset() {
		setIcon(SMILEY_PLAY);
	}
}
