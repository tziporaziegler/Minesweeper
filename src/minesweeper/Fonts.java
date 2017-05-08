package minesweeper;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

public final class Fonts {

	public static Font CELL_FONT;
	public static Font DISPLAY_FONT;

	private static final GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();

	public Fonts() {
		new Thread() {
			@Override
			public void run() {
				// CELL_FONT = setFont("http://www.webpagepublicity.com/free-fonts/t/Tin%20Birdhouse.ttf", 15f);

				CELL_FONT = setFont("fonts/TinBirdhouse.ttf", 15f);
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				// DISPLAY_FONT = setFont("http://www.webpagepublicity.com/free-fonts/d/Digiface%20Regular.ttf", 22f);

				DISPLAY_FONT = setFont("fonts/DigifaceRegular.ttf", 22f);
			}
		}.start();
	}

	private Font setFont(String fontPath, float fontSize) {
		try {
			// final URL fontUrl = new URL(fontPath);
			// final Font font = Font.createFont(Font.TRUETYPE_FONT, fontUrl.openStream()).deriveFont(fontSize);

			final Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(fontPath))
					.deriveFont(fontSize);

			GE.registerFont(font);

			return font;
		}
		catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
