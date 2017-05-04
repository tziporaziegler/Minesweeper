package minesweeper;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class Minesweeper extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final int LEVEL_MENU_WIDTH = 150;
	private static final int LEVEL_MENU_HEIGHT = 125;
	private static final Border MOUSE_HOVER_BORDER = new BevelBorder(BevelBorder.RAISED);

	private int numberOfButtons;

	private Minesweeper() {
		setSize(LEVEL_MENU_WIDTH, LEVEL_MENU_HEIGHT);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		// TODO read in high score from serialized file based on level choose
		// at end compare numbers, reset if new HS member, and save new file

		addLevelOptionButtons();
		setLayout(new GridLayout(numberOfButtons, 1));

		setVisible(true);
	}

	/**
	 * Create and add the level option buttons to the level option JFrame
	 * <p>
	 * Options currently include Beginner, Intermediate and Expert levels.
	 */
	private void addLevelOptionButtons() {
		addButton("BEGINNER", "#5CE62E", 184, 247, 9, 9, 10, 14);
		addButton("INTERMEDIATE", "#FFD633", 324, 387, 16, 16, 40, 61);
		addButton("EXPERT", "#FF2A2A", 604, 387, 30, 16, 99, 154);

		// TODO Custom: Any values from 8 × 8 or 9 × 9 to 30 × 24 field, with 10 to 668 mines.
		// figure out ratio of board pixel size per cell
	}

	/**
	 * Create and add a new level start button to the Minesweeper level option start menu.
	 * <p>
	 * Options currently include Beginner, Intermediate and Expert levels.
	 * 
	 * @param name The text to display on the level option button
	 * @param color The color of the level option button
	 * @param boardHeight The height of the playing board
	 * @param boardWidth The width of the playing board
	 * @param boardRows The number of rows in the playing board
	 * @param boardCols The number of columns in the playing board
	 * @param boardNumBombs The number of bombs in the playing board
	 * @param boardGap The amount of gap spacing to use in the playing board
	 */
	private void addButton(String name, String color, int boardHeight, int boardWidth, int boardRows, int boardCols,
			int boardNumBombs, int boardGap) {
		// create a new start button
		JButton button = new JButton(name);

		// set button background color
		button.setBackground(Color.decode(color));

		// add mouse hover action
		addMouseAction(button);

		// Add click listener
		// When the button is clicked, create a new playing board and dispose of the level options frame.
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new Board(boardHeight, boardWidth, boardRows, boardCols, boardNumBombs, boardGap);
				dispose();
			}
		});

		// add the start button to the Mineweeper JFrame
		add(button);

		numberOfButtons++;
	}

	/**
	 * This method adds a border to the button when the mouse hovers over the button.
	 * The border is removed as soon as the mouse exits the button area.
	 * 
	 * @param button The button to add the MouseListener to
	 */
	private void addMouseAction(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBorder(MOUSE_HOVER_BORDER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBorder(null);
			}
		});
	}

	/**
	 * Start a new Minesweeper game!
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Start a new Minesweeper game
		new Minesweeper();
	}
}