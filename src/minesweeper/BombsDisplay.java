package minesweeper;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class BombsDisplay extends DisplayLabel {
	private static final long serialVersionUID = 1L;

	private final int numBombs;
	private int numBombsLeft;

	// if number of flags reach amount of bombs, 000 flashed for .6 seconds
	private final Timer tooManyBombsTimer = new Timer(600, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			setForeground(currentColorNum);
		}
	});

	public BombsDisplay(int numBombs, float colorNum) {
		super(colorNum);

		this.numBombs = numBombs;
		reset();
	}

	@Override
	public void increment() {
		setText(++numBombsLeft);
	}

	@Override
	public void decrement() throws Exception {
		if (numBombsLeft > 0) {
			setText(--numBombsLeft);
		}
		else {
			flashYellowNumbers();
			throw new Exception("Bomb limit reached.");
		}
	}

	@Override
	public void reset() {
		super.reset();

		numBombsLeft = numBombs;
		setText(numBombsLeft);
	}

	private void flashYellowNumbers() {
		// restart the timer to ensure that it runs for the full 600ms
		if (tooManyBombsTimer.isRunning()) {
			tooManyBombsTimer.restart();
		}
		else {
			tooManyBombsTimer.start();
		}

		setForeground(Color.YELLOW);
	}
}
