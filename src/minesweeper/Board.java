package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import fireworks.Fireworks;

// import com.apple.eawt.Application;

public class Board extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final Format DISPLAY_FORMATTER = new DecimalFormat(" 000 ");

	public static final ImageIcon SMILEY_PLAY = new ImageIcon(Board.class.getResource("pics/smileblue.png"));
	private static final ImageIcon SMILEY_GUESS = new ImageIcon(Board.class.getResource("pics/smileguessblue.png"));
	private static final ImageIcon SMILEY_DEAD = new ImageIcon(Board.class.getResource("pics/smiledead.png"));
	private static final ImageIcon SMILEY_WIN = new ImageIcon(Board.class.getResource("pics/smilewin.png"));

	private final int numBombs;
	private final int numCells;
	private int numCellsToUncover;

	// top panel components
	private final JLabel bombsLeft;
	private int numBombsLeft;
	private final JButton smiley;
	private final JLabel timer;
	private int amtTime;

	// grid components - new to store variables to start new game
	private JPanel grid;
	private final int cols;
	private final int rows;
	private Cell[][] cells;
	private Font cellFont;

	private ScheduledExecutorService executor;
	private float colorNum;
	private boolean firstClick;
	private boolean end;

	// smiley action listener - resets board, timer and smiley
	private ActionListener newGameListen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (!firstClick) {
				executor.shutdown();
				colorNum = 0;
				smiley.setIcon(new ImageIcon(getClass().getResource("pics/smileblue.png")));
				amtTime = 0;
				timer.setForeground(Color.getHSBColor(colorNum, 1, 1));
				updateTimer();
				try {
					newGame();
				}
				catch (FontFormatException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	};

	// if number of flags reach amount of bombs, 000 flashed for .6 seconds
	private Timer time = new Timer(600, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			bombsLeft.setForeground(Color.getHSBColor(colorNum, 1, 1));
		}
	});

	private MouseAdapter cellMouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (!end) {
				smiley.setIcon(SMILEY_GUESS);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Cell cell = (Cell) e.getSource();
			int row1 = cell.getRow();
			int col1 = cell.getCol();
			if (firstClick) {

				boolean newG;
				do {
					newG = false;
					// reset board as long as first click is a bomb so that never get
					// out on first turn
					if (cell.isBomb()) {
						row1 = cell.getRow();
						col1 = cell.getCol();
						// new game (actionListener and method)
						// cannot flag before click at least one box
						newG = true;
						try {
							newGame();
						}
						catch (FontFormatException | IOException e1) {
							e1.printStackTrace();
						}
						cell = cells[col1][row1];
					}
				} while (newG);

				resetTimer();
				firstClick = false;
			}
			if (!cell.isUnlocked()) {
				if (SwingUtilities.isRightMouseButton(e)) {
					if (cell.isFlagged()) {
						cell.unflag();
						updateBombsLeft(++numBombsLeft);
					}
					else {
						if (numBombsLeft > 0) {
							cell.flag();
							updateBombsLeft(--numBombsLeft);
						}
						else {
							time.start();
							bombsLeft.setForeground(Color.YELLOW);
						}
					}
				}
				else {
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
			}
			else if (SwingUtilities.isRightMouseButton(e) && SwingUtilities.isLeftMouseButton(e)) {
				final int row = cell.getRow();
				final int col = cell.getCol();

				if (getNumFlagNeighbors(col, row) == cell.getNumBombNeighbors()) {
					unlockNeighbors(cell);
					if (numCellsToUncover == 0) {
						winGame();
					}
				}
				else if (!end) {
					depressNeighbors(col, row);
				}
			}
			if (!end) {
				smiley.setIcon(SMILEY_PLAY);
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

		// For Window
		try {
			setIconImage(ImageIO.read(getClass().getResource("pics/minesweeperIcon.png")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// For Mac OS X
		// Application application = Application.getApplication();
		// Image image = Toolkit.getDefaultToolkit().getImage("pics/minesweeperIcon.png");
		// application.setDockIconImage(image);

		colorNum = 0;
		final JPanel topPanel = new JPanel();
		bombsLeft = new JLabel();
		timer = new JLabel();
		smiley = new JButton();
		new SetUpTopPanelThread(topPanel, bombsLeft, timer, smiley, newGameListen, colorNum, gap).start();
		add(topPanel, BorderLayout.NORTH);

		setCellFont();
		initializeGrid();

		amtTime = 0;
		updateTimer();
		updateBombsLeft(numBombsLeft);

		setVisible(true);
	}

	private void setCellFont() {
		try {
			final URL fontUrl = new URL("http://www.webpagepublicity.com/free-fonts/t/Tin%20Birdhouse.ttf");
			cellFont = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
			cellFont = cellFont.deriveFont(15f);
			final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(cellFont);
		}
		catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeGrid() {
		grid = new JPanel();

		grid.setLayout(new GridLayout(cols, rows));
		grid.setBorder(new BevelBorder(BevelBorder.LOWERED));
		grid.setBackground(Color.LIGHT_GRAY);

		refreshCells();

		add(grid, BorderLayout.CENTER);
	}

	private void refreshCells() {
		end = false;
		firstClick = true;

		numBombsLeft = numBombs;
		numCellsToUncover = numCells - numBombs;

		grid.removeAll();

		cells = new Cell[cols][rows];

		final ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < numCells; i++) {
			list.add(i);
		}
		Collections.shuffle(list);

		// FIXME check if mixed up col and row
		int position = 0;
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				if (list.get(position) < numBombs) {
					cells[col][row] = new Cell(true, row, col, cellFont);
				}
				else {
					cells[col][row] = new Cell(false, row, col, cellFont);
				}
				final Cell cell = cells[col][row];

				cell.addMouseListener(cellMouseAdapter);

				grid.add(cell);
				position++;
			}
		}

		// After the board is initialize, loop through all the cells and set the numBombNeighbors.
		// By setting it here, getting rid of the need of ever calling this method again.
		for (int col = 0; col < cols; col++) {
			for (int row = 0; row < rows; row++) {
				final Cell cell = cells[col][row];
				if (cell.isBomb()) {
					numBombsLeft++;
				}
				else {
					cell.setNumBombNeighbors(getNumBombNeighbors(col, row));
				}
			}
		}
	}

	private void newGame() throws FontFormatException, IOException {
		refreshCells();

		bombsLeft.setForeground(Color.getHSBColor(colorNum, 1, 1));
		updateBombsLeft(numBombsLeft);
		repaint();
	}

	// create timer
	private void resetTimer() {
		amtTime = 0;
		updateTimer();
		executor = Executors.newScheduledThreadPool(1);

		executor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				amtTime++;
				updateTimer();
			}
		}, 0, 1, TimeUnit.SECONDS);

		executor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				colorNum += .02;
				timer.setForeground(Color.getHSBColor(colorNum, 1, 1));
				bombsLeft.setForeground(Color.getHSBColor(colorNum, 1, 1));
			}
		}, 0, 10, TimeUnit.SECONDS);
	}

	private void updateBombsLeft(int numBombs) {
		bombsLeft.setText(DISPLAY_FORMATTER.format(numBombs));
	}

	private void updateTimer() {
		timer.setText(DISPLAY_FORMATTER.format(amtTime));
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
		end = true;
		executor.shutdown();
	}

	private void looseGame() {
		endGame();
		updateBombsLeft(numBombsLeft);

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
		smiley.setIcon(SMILEY_DEAD);
	}

	private void winGame() {
		endGame();
		updateBombsLeft(0);
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

		smiley.setIcon(SMILEY_WIN);
		new Fireworks();
	}
}