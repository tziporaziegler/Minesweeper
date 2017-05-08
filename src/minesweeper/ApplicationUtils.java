package minesweeper;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public final class ApplicationUtils {

	private ApplicationUtils() {
	}

	public static void setApplicationIcon(JFrame frame) {
		if (System.getProperty("os.name").startsWith("Windows")) {
			try {
				frame.setIconImage(ImageIO.read(frame.getClass().getResource("pics/minesweeperIcon.png")));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (System.getProperty("os.name").contains("Mac")) {
			// Application application = Application.getApplication();
			// Image image = Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("pics/minesweeperIcon.png"));
			// application.setDockIconImage(image);
			final Image image = Toolkit.getDefaultToolkit().getImage(frame.getClass().getResource("pics/minesweeperIcon.png"));
			// com.apple.eawt.Application.getApplication().setDockIconImage(image);

			// How can I call an OS X-specific method for my cross-platform Jar? -
			// http://stackoverflow.com/a/13400962/4660897
			final String className = "com.apple.eawt.Application";
			try {
				final Class<?> cls = Class.forName(className);
				final Object application = cls.newInstance().getClass().getMethod("getApplication").invoke(null);
				application.getClass().getMethod("setDockIconImage", java.awt.Image.class).invoke(application, image);
			}
			catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}
}
