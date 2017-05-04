package minesweeper;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class SetUpTopPanelThread extends Thread {
	private static final BevelBorder DISPLAY_BORDER = new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.GRAY);

	private JPanel topPanel;
	private JLabel bombsLeft;
	private JLabel timer;
	private JButton smiley;
	private ActionListener newGameListen;
	private float colorNum;
	private int gap;

	public SetUpTopPanelThread(JPanel topPanel, JLabel bombsLeft, JLabel timer, JButton smiley,
			ActionListener newGameListen, float colorNum, int gap) {
		this.topPanel = topPanel;
		this.bombsLeft = bombsLeft;
		this.timer = timer;
		this.smiley = smiley;
		this.newGameListen = newGameListen;
		this.colorNum = colorNum;
		this.gap = gap;
	}

	@Override
	public void run() {
		// TODO test if gap is good on windows
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, gap, 3));
		// add breaks - empty spaces in flow layout
		// topPanel.setLayout(new GridLayout(1, 7));
		topPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		topPanel.setBackground(Color.LIGHT_GRAY);

		// create font that will be used for bombsLeft and timer
		new DisplayFontThread(bombsLeft, timer).start();

		// create bombs left display
		addDisplay(bombsLeft);

		// create smiley display
		// TODO add picture of pressed smiley that will display while smiley is pressed
		smiley.setIcon(Board.SMILEY_PLAY);
		smiley.setBackground(null);
		smiley.setBorder(new EmptyBorder(0, 0, 0, 0));
		smiley.addActionListener(newGameListen);
		topPanel.add(smiley);

		// create timer display
		addDisplay(timer);
	}

	private void addDisplay(JLabel label) {
		label.setBackground(Color.BLACK);
		label.setForeground(Color.getHSBColor(colorNum, 1, 1));
		label.setOpaque(true);
		label.setBorder(DISPLAY_BORDER);
		label.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(label);
	}
}
