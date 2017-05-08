package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import fireworks.Fireworks;

public class Board extends JFrame {
	private static final long serialVersionUID = 1L;

	private final int numBombs;
	private final int numCells;
	private int numCellsToUncover;

	// grid components - new to store variables to start new game
	private JPanel grid;
	private final int cols;
	private final int rows;
	private Cell[][] cells;

	private boolean gameOver = false;
	private boolean firstClick = true;

	private TopPanel topPanel;

	private MouseAdapter cellMouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (gameOver) {
				return;
			}

			topPanel.guessSmiley();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (gameOver) {
				return;
			}

			topPanel.resetSmiley();

			Cell cell = (Cell) e.getSource();

			if (firstClick) {
				cell = firstClick(cell);
			}

			if (cell.isUnlocked()) {
				if (SwingUtilities.isRightMouseButton(e) && SwingUtilities.isLeftMouseButton(e)) {
					rightAndLeftClick(cell);
				}
			}
			else {
				if (SwingUtilities.isRightMouseButton(e)) {
					rightClick(cell);
				}
				else {
					leftClick(cell);
				}
			}
		}
	};

	public Board(int width, int height, int rows, int cols, int numBombs, int gap) {
		this.rows = rows;
		this.cols = cols;
		// TODO make numBobs calculate when create actual bomb
		this.numBombs = numBombs;
		numCells = rows * cols;

		setSize(width, height);
		setTitle("Minesweeper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new BorderLayout());
		ApplicationUtils.setApplicationIcon(this);

		initializeTopPanel(gap);
		initializeGrid();

		setVisible(true);
	}

	private void initializeTopPanel(int gap) {
		final ActionListener newGameListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!firstClick) {
					firstClick = true;
					gameOver = false;
					refreshCells();
					topPanel.reset();
					repaint();
				}
			}
		};

		topPanel = new TopPanel(numBombs, gap, newGameListener);
		add(topPanel, BorderLayout.NORTH);
	}

	private void initializeGrid() {
		grid = new JPanel();

		grid.setLayout(new GridLayout(cols, rows));
		grid.setBorder(new BevelBorder(BevelBorder.LOWERED));
		grid.setBackground(Color.LIGHT_GRAY);

		add(grid, BorderLayout.CENTER);

		refreshCells();
	}

	private void refreshCells() {
		numCellsToUncover = numCells - numBombs;

		grid.removeAll();

		cells = new Cell[cols][rows];

		final ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < numCells; i++) {
			list.add(i);
		}
		Collections.shuffle(list);

		int position = 0;
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				final Cell cell = new Cell(list.get(position) < numBombs, row, col, cellMouseAdapter);
				cells[col][row] = cell;

				grid.add(cell);
				position++;
			}
		}

		// After the board is initialize, loop through all the cells and set the numBombNeighbors.
		// By setting it here, getting rid of the need of ever calling this method again.
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				final Cell cell = cells[col][row];
				if (!cell.isBomb()) {
					cell.setNumBombNeighbors(getNumBombNeighbors(col, row));
				}
			}
		}
	}

	private Cell firstClick(Cell cell) {
		final int row = cell.getRow();
		final int col = cell.getCol();

		do {
			firstClick = false;

			// reset board as long as first click is a bomb so that never get out on first turn
			if (cell.isBomb()) {
				// new game (actionListener and method)
				// cannot flag before click at least one box
				firstClick = true;
				refreshCells();
				cell = cells[col][row];
			}

		} while (firstClick);

		topPanel.startTime();

		return cell;
	}

	private void rightClick(Cell cell) {
		if (cell.isFlagged()) {
			cell.unflag();
			topPanel.incrementBombs();
		}
		else {
			try {
				topPanel.decrementBombs();
				cell.flag();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void leftClick(Cell cell) {
		if (!cell.isFlagged()) {
			if (cell.isBomb()) {
				cell.clickExplode();
				looseGame();
			}
			else {
				final int numNeighbors = cell.getNumBombNeighbors();
				if (numNeighbors == 0) {
					unlockNeighbors(cell);
				}
				else {
					cell.unlock();
				}
				numCellsToUncover--;
				if (numCellsToUncover == 0) {
					winGame();
				}
			}
		}
	}

	private void rightAndLeftClick(Cell cell) {
		final int row = cell.getRow();
		final int col = cell.getCol();

		if (getNumFlagNeighbors(col, row) == cell.getNumBombNeighbors()) {
			unlockNeighbors(cell);
			if (numCellsToUncover == 0) {
				winGame();
			}
		}
		else {
			depressNeighbors(col, row);
		}
	}

	private int getNumFlagNeighbors(int i, int j) {
		int counter = 0;

		final int startK = i > 0 ? -1 : 0;
		final int endK = i < cols - 1 ? 2 : 1;
		final int startM = j > 0 ? -1 : 0;
		final int endM = j < rows - 1 ? 2 : 1;

		// go to each neighbor and check if flagged
		for (int k = startK; k < endK; k++) {
			for (int m = startM; m < endM; m++) {

				// skip center box
				final boolean centerBox = k == 0 && m == 0;

				final Cell cell = cells[i + k][j + m];

				if (!centerBox && cell.isFlagged()) {
					counter++;
				}
			}
		}
		return counter;
	}

	private int getNumBombNeighbors(int i, int j) {
		int counter = 0;

		final int startK = i > 0 ? -1 : 0;
		final int endK = i < cols - 1 ? 2 : 1;
		final int startM = j > 0 ? -1 : 0;
		final int endM = j < rows - 1 ? 2 : 1;

		// go to each neighbor and check if flagged
		for (int k = startK; k < endK; k++) {
			for (int m = startM; m < endM; m++) {

				// skip center box
				final boolean centerBox = k == 0 && m == 0;

				final Cell cell = cells[i + k][j + m];

				if (!centerBox && cell.isBomb()) {
					counter++;
				}
			}
		}
		return counter;
	}

	private void unlockNeighbors(Cell currentCell) {
		final Stack<Cell> stack = new Stack<Cell>();
		stack.push(currentCell);
		while (!stack.isEmpty()) {
			final Cell cell = stack.pop();
			final int row = cell.getCol();
			final int col = cell.getRow();
			cell.unlock();

			// check all neighbors
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {

					// i != 0 || j != 0 - skip center box
					if (isCell(row + i, col + j) && (i != 0 || j != 0)) {

						final Cell thisCell = cells[row + i][col + j];

						// the following check is only for flag unlock and not 0 unlock
						if (thisCell.isBomb() && !thisCell.isFlagged()) {
							looseGame();
						}

						if (!thisCell.isUnlocked() && !thisCell.isFlagged()) {
							thisCell.unlock();
							numCellsToUncover--;
							if (thisCell.getNumBombNeighbors() == 0) {
								stack.push(thisCell);
							}
						}
					}
				}
			}
		}
	}

	private void depressNeighbors(int col, int row) {
		// go to each neighbor
		for (int k = -1; k < 2; k++) {
			for (int m = -1; m < 2; m++) {

				// k != 0 || m != 0 - skip center box
				if (isCell(col + k, row + m) && (k != 0 || m != 0)) {
					final Cell cell = cells[col + k][row + m];
					if (!cell.isUnlocked() && !cell.isFlagged()) {
						cell.depress();
					}
				}
			}
		}
	}

	private boolean isCell(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}

	private void endGame() {
		gameOver = true;
		topPanel.shutDownTime();
	}

	private void looseGame() {
		endGame();

		topPanel.triggerDie();

		for (final Cell[] cellRow : cells) {
			for (final Cell cell : cellRow) {
				if (cell.isFlagged()) {
					if (cell.isBomb()) {
						cell.setIsUnlocked(true);
					}
					else {
						cell.wrongify();
					}
				}
				else if (!cell.isUnlocked()) {
					if (cell.isBomb()) {
						cell.explode();
					}
					else {
						cell.setIsUnlocked(true);
					}
				}

			}
		}
	}

	private void winGame() {
		endGame();

		topPanel.triggerWin();

		for (final Cell[] cellRow : cells) {
			for (final Cell cell : cellRow) {
				if (!cell.isUnlocked()) {

					cell.setIsUnlocked(true);

					if (cell.isBomb()) {
						cell.flag();
					}

				}
			}
		}

		new Fireworks();
	}
}