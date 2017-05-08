package minesweeper;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class TopPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final BevelBorder TOP_PANEL_BORDER = new BevelBorder(BevelBorder.LOWERED);

	private float colorNum = 0;

	private final BombsDisplay bombsLeftDisplay;
	private final TimerDisplay timerDisplay;
	private final SmileyButton smileyButton;

	private ScheduledExecutorService executor;

	private final Runnable timerRun = new Runnable() {
		public void run() {
			timerDisplay.increment();
		}
	};

	private final Runnable colorChangeRun = new Runnable() {
		public void run() {
			colorNum += .02;
			timerDisplay.setForeground(colorNum);
			bombsLeftDisplay.setForeground(colorNum);
		}
	};

	public TopPanel(int numBombs, int gap, ActionListener newGameListener) {
		initializePanelDesign(gap);

		colorNum = 0;

		bombsLeftDisplay = new BombsDisplay(numBombs, colorNum);
		add(bombsLeftDisplay);

		smileyButton = new SmileyButton(newGameListener);
		add(smileyButton);

		timerDisplay = new TimerDisplay(colorNum);
		add(timerDisplay);
	}

	private void initializePanelDesign(int gap) {
		// TODO modify gap to also look good on Windows
		setLayout(new FlowLayout(FlowLayout.CENTER, gap, 3));

		// add breaks - empty spaces in flow layout
		// topPanel.setLayout(new GridLayout(1, 7));

		setBorder(TOP_PANEL_BORDER);
		setBackground(Color.LIGHT_GRAY);
	}

	public void reset() {
		colorNum = 0;

		bombsLeftDisplay.reset();
		smileyButton.reset();
		timerDisplay.reset();

		shutDownTime();
	}

	public void startTime() {
		executor = Executors.newScheduledThreadPool(2);
		executor.scheduleAtFixedRate(timerRun, 0, 1, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(colorChangeRun, 0, 10, TimeUnit.SECONDS);
	}

	public void shutDownTime() {
		if (executor != null) {
			executor.shutdown();
		}
	}

	public void resetSmiley() {
		smileyButton.reset();
	}

	public void guessSmiley() {
		smileyButton.guess();
	}

	public void incrementBombs() {
		bombsLeftDisplay.increment();
	}

	public void decrementBombs() throws Exception {
		bombsLeftDisplay.decrement();
	}

	public void triggerWin() {
		bombsLeftDisplay.setText(0);
		smileyButton.win();
	}

	public void triggerDie() {
		smileyButton.die();
	}
}
