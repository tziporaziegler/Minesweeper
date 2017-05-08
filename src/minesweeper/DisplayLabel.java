package minesweeper;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

public abstract class DisplayLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	private static final BevelBorder DISPLAY_BORDER = new BevelBorder(BevelBorder.LOWERED, Color.GRAY, Color.GRAY);
	private static final Format DISPLAY_FORMATTER = new DecimalFormat(" 000 ");

	protected float currentColorNum;
	private final float originalColorNum;

	public DisplayLabel(float colorNum) {
		originalColorNum = colorNum;

		reset();

		setBackground(Color.BLACK);
		setOpaque(true);
		setBorder(DISPLAY_BORDER);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(Fonts.DISPLAY_FONT);
	}

	public void setText(int textNum) {
		setText(DISPLAY_FORMATTER.format(textNum));
	}

	public void setForeground(float colorNum) {
		currentColorNum = colorNum;
		setForeground(Color.getHSBColor(colorNum, 1, 1));
	}

	public abstract void increment();

	public abstract void decrement() throws Exception;

	public void reset() {
		setForeground(originalColorNum);
	}
}
