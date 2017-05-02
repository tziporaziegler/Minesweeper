package minesweeper;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class Minesweeper extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JButton beginner;
	private final JButton intermediate;
	private final JButton expert;

	public Minesweeper() {
		setSize(150, 125);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridLayout(3, 1));

		// TODO read in high score from serialized file based on level choose
		// at end compare numbers, reset if new HS member, and save new file
		beginner = new JButton("BEGINNER");
		beginner.setBackground(Color.decode("#5CE62E"));
		addMouse(beginner);
		ActionListener beginListen = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					// TODO test if gap is good on windows
					new Board(184, 247, 9, 9, 10, 14);
				}
				catch (FontFormatException | IOException e) {
					e.printStackTrace();
				}
				dispose();
			}
		};
		beginner.addActionListener(beginListen);
		add(beginner);

		intermediate = new JButton("INTERMEDIATE");
		intermediate.setBackground(Color.decode("#FFD633"));
		addMouse(intermediate);
		ActionListener interListen = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					new Board(324, 387, 16, 16, 40, 61);
				}
				catch (FontFormatException | IOException e) {
					e.printStackTrace();
				}
				dispose();
			}
		};
		intermediate.addActionListener(interListen);
		add(intermediate);

		expert = new JButton("EXPERT");
		expert.setBackground(Color.decode("#FF2A2A"));
		addMouse(expert);
		ActionListener expertListen = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					new Board(604, 387, 30, 16, 99, 154);
				}
				catch (FontFormatException | IOException e) {
					e.printStackTrace();
				}
				dispose();
			}
		};
		expert.addActionListener(expertListen);
		add(expert);

		// TODO Custom: Any values from 8 × 8 or 9 × 9 to 30 × 24 field, with 10 to 668 mines.
		// figure out ratio of board pixel size per cell

		setVisible(true);
	}

	public void addMouse(JButton button) {
		Border border = new BevelBorder(BevelBorder.RAISED);

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBorder(border);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBorder(null);
			}
		});
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new Minesweeper();
	}
}