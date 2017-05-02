package minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;

public class Cell extends JButton {
	private static final long serialVersionUID = 1L;
	private final boolean isBomb;
	private boolean flagged;
	private boolean unlocked;
	private final int row;
	private final int col;
	private int numBombNeighbors;

	public Cell(boolean isBomb, int row, int col, Font font) throws FontFormatException, IOException {
		setBackground(Color.LIGHT_GRAY);
		setFont(font);
		// setFont(new Font("Acens", Font.BOLD, 15));

		setBorder(new BevelBorder(BevelBorder.RAISED));
		unlocked = false;
		flagged = false;
		this.isBomb = isBomb;
		this.row = row;
		this.col = col;
	}

	public void unlock() {
		setBorder(new MatteBorder(1, 0, 0, 1, Color.GRAY));
		switch (numBombNeighbors) {
			case 0:
				break;
			case 1:
				setForeground(Color.BLUE);
				break;
			case 2:
				setForeground(Color.decode("#197519"));
				break;
			case 3:
				setForeground(Color.RED);
				break;
			case 4:
				setForeground(Color.decode("#000066"));
				break;
			case 5:
				setForeground(Color.decode("#6E2500"));
				break;
			case 6:
				setForeground(Color.decode("#1E8595"));
				break;
			case 7:
				setForeground(Color.BLACK);
				break;
			case 8:
				setForeground(Color.GRAY);
				break;
		}

		if (numBombNeighbors != 0) {
			setText(String.valueOf(numBombNeighbors));
		}
		unlocked = true;
	}

	public void flag() {
		setIcon(new ImageIcon(getClass().getResource("pics/flag.png")));
		flagged = true;
	}

	public void unflag() {
		setIcon(new ImageIcon(getClass().getResource("")));
		flagged = false;
	}

	public void explode() {
		setBorder(new MatteBorder(1, 0, 0, 1, Color.GRAY));
		setIcon(new ImageIcon(getClass().getResource("pics/mine.png")));
		unlocked = true;
	}

	public void clickExplode() {
		// FIXME red bomb disapears when hover over
		setBorder(new MatteBorder(1, 0, 0, 1, Color.GRAY));
		setIcon(new ImageIcon(getClass().getResource("pics/hitmine.gif")));
		setBackground(Color.RED);
		unlocked = true;
	}

	public void wrongify() {
		setBorder(new MatteBorder(1, 0, 0, 1, Color.GRAY));
		setIcon(new ImageIcon(getClass().getResource("pics/wrongmine.png")));
		unlocked = true;
	}

	public void depress() {
		setBorder(new MatteBorder(1, 0, 0, 1, Color.GRAY));
		time.start();
	}

	Timer time = new Timer(300, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!flagged && !unlocked) {
				setBorder(new BevelBorder(BevelBorder.RAISED));
			}
		}
	});

	public boolean isBomb() {
		return isBomb;
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getNumBombNeighbors() {
		return numBombNeighbors;
	}

	public void setNumBombNeighbors(int numBombNeighbors) {
		this.numBombNeighbors = numBombNeighbors;
	}

	public void setIsUnlocked(boolean b) {
		unlocked = b;
	}
}
