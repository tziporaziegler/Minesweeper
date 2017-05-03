package minesweeper;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;

import javax.swing.JLabel;

public class DisplayFontThread extends Thread {
	private Font font;
	private JLabel bombsLeft;
	private JLabel timer;

	public DisplayFontThread(JLabel bombsLeft, JLabel timer) {
		this.bombsLeft = bombsLeft;
		this.timer = timer;
	}

	@Override
	public void run() {
		try {
			final URL fontUrl = new URL("http://www.webpagepublicity.com/" + "free-fonts/d/Digiface%20Regular.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream());
		}
		catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		font = font.deriveFont(22f);
		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);

		bombsLeft.setFont(font);
		timer.setFont(font);
	}
}
