package minesweeper;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ApplicationUtils {

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
			Image image = Toolkit.getDefaultToolkit().getImage("pics/minesweeperIcon.png");

			// How can I call an OS X-specific method for my cross-platform Jar? -
			// http://stackoverflow.com/a/13400962/4660897
			String className = "com.apple.eawt.Application";
			try {
				Class<?> cls = Class.forName(className);
				Object application = cls.newInstance().getClass().getMethod("getApplication").invoke(null);
				application.getClass().getMethod("setDockIconImage").invoke(image);
			}
			catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException
					| InstantiationException e) {
				e.printStackTrace();
			}
		}
	}
}
