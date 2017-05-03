package fireworks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Fireworks extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	private Firework[] firework;
	private double gravity = 0.05;
	private int FPS = 100;
	private long lastFpsSet = System.currentTimeMillis();

	public Fireworks() {
		firework = new Firework[500];

		add(new ImagePanel());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		pack();
		setLocationRelativeTo(null);
		setBackground(new Color(0, 0, 0, 0));
		setVisible(true);

		new Thread(this).start();

		// Fireworks display closes if mouse is clicked
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		// Fireworks display automatically runs for 15 seconds and then closes
		final Timer time = new Timer(15000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		time.start();
	}

	// make panel transparent
	@SuppressWarnings("serial")
	public class ImagePanel extends JPanel {
		public ImagePanel() {
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (final Firework firework2 : firework) {
				if (firework2 != null) {
					for (final FireworkFlameParticle FFP : firework2.fireworkFlameParticle) {
						if (FFP != null) {
							g.setColor(FFP.getColor());
							g.fillRect((int) FFP.getX(), (int) FFP.getY(), FFP.getWidth(), FFP.getHeight());
						}
					}
					if (firework2.hasExploded()) {
						for (final FireworkParticle fireworkParticle : firework2.fireworkParticle) {
							if (fireworkParticle != null) {
								g.setColor(fireworkParticle.getColor());
								g.fillRect((int) fireworkParticle.getX(), (int) fireworkParticle.getY(),
										fireworkParticle.getWidth(), fireworkParticle.getHeight());
							}
						}
					}
					else {
						g.setColor(firework2.getColor());
						g.fillRect((int) firework2.getX(), (int) firework2.getY(), firework2.getWidth(),
								firework2.getHeight());
					}
				}
			}
		}

		// set size of the display
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(700, 500);
		}

	}

	public void process() {
		for (int i = 0; i < firework.length; i++) {
			if (firework[i] != null) {
				for (int i2 = 0; i2 < firework[i].fireworkFlameParticle.length; i2++) {
					final FireworkFlameParticle FFP = firework[i].fireworkFlameParticle[i2];
					if (FFP != null) {
						FFP.setX(firework[i].fireworkFlameParticle[i2].getX()
								+ firework[i].fireworkFlameParticle[i2].getxVelocity());
						FFP.setY(firework[i].fireworkFlameParticle[i2].getY()
								+ firework[i].fireworkFlameParticle[i2].getyVelocity());
						final Color ffpColor = FFP.getColor();
						final int minusAlpha = (int) (Math.random() * Math.random() * 15);
						if (ffpColor.getAlpha() - minusAlpha <= 0) {
							firework[i].fireworkFlameParticle[i2] = null;
							continue;
						}
						FFP.setyVelocity(FFP.getyVelocity() + gravity / 4);
						FFP.setColor(new Color(ffpColor.getRed(), ffpColor.getGreen(), ffpColor.getBlue(),
								ffpColor.getAlpha() - minusAlpha));
					}
				}
				if (firework[i].hasExploded()) {
					for (int i2 = 0; i2 < firework[i].fireworkParticle.length; i2++) {
						final FireworkParticle FP = firework[i].fireworkParticle[i2];
						if (FP != null) {
							FP.setX(FP.getX() + FP.getxVelocity());
							FP.setY(FP.getY() + FP.getyVelocity());
							if (FP.getX() > getPreferredSize().width || FP.getX() < 0
									|| FP.getY() > getPreferredSize().height) {
								firework[i].fireworkParticle[i2] = null;
								continue;
							}
							FP.setyVelocity(FP.getyVelocity() + gravity);
							final int minusAlpha = (int) (Math.random() * 5);
							final Color fwColor = FP.getColor();
							if (fwColor.getAlpha() - minusAlpha <= 0) {
								firework[i].fireworkParticle[i2] = null;
								continue;
							}
							FP.setColor(new Color(fwColor.getRed(), fwColor.getGreen(), fwColor.getBlue(),
									fwColor.getAlpha() - minusAlpha));
						}
					}
					boolean noMoreParticles = true;
					for (int i3 = 0; i3 < firework[i].fireworkParticle.length; i3++) {
						if (firework[i].hasExploded() && firework[i].fireworkParticle[i3] != null) {
							noMoreParticles = false;
							break;
						}
					}
					if (noMoreParticles) {
						firework[i] = null;
					}
				}
				else {
					firework[i].setY(firework[i].getY() - firework[i].getyVelocity());
					for (int i2 = 0; i2 < 2; i2++) {
						for (int i3 = 0; i3 < firework[i].fireworkFlameParticle.length; i3++) {
							if (firework[i].fireworkFlameParticle[i3] == null) {
								final int size = 1 + (int) (Math.random() * 4);
								firework[i].fireworkFlameParticle[i3] = new FireworkFlameParticle(
										firework[i].getX() + firework[i].getWidth() / 2 - size / 2,
										firework[i].getY() + firework[i].getHeight(), size, size,
										Math.random() * 0.5 - Math.random() * 0.5, new Color(
												100 + (int) (Math.random() * 155), 25 + (int) (Math.random() * 50), 0));
								break;
							}
						}
					}
					if (firework[i].getY() <= firework[i].getEndY()) {
						firework[i].setExploded(true);
						for (int i2 = 0; i2 < firework[i].fireworkParticle.length; i2++) {
							double xVelocity = 0;
							double yVelocity = 0;
							if ((int) (Math.random() * 2) == 1) {
								xVelocity = 0.1 + Math.random() * Math.random() * 5;
							}
							else {
								xVelocity = -0.1 - Math.random() * Math.random() * 5;
							}
							if ((int) (Math.random() * 2) == 1) {
								yVelocity = 0.1 + Math.random() * Math.random() * 5;
							}
							else {
								yVelocity = -0.1 - Math.random() * Math.random() * 5;
							}
							
							final int size = 1 + (int) (Math.random() * 4);
							
							firework[i].fireworkParticle[i2] = new FireworkParticle(firework[i].getX(),
									firework[i].getY(), size, size, xVelocity, yVelocity,
									new Color((int) (Math.random() * 0xFFFFFF)));
						}
					}
				}
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (System.currentTimeMillis() - lastFpsSet >= 1000) {
					lastFpsSet = System.currentTimeMillis();
				}

				// this launched 5 fireworks each round
				for (int i = 0; i < 5; i++) {
					if (firework[i] == null) {
						firework[i] = new Firework((int) (Math.random() * getPreferredSize().width),
								getPreferredSize().height, 5, 20, 5, (int) (Math.random() * getPreferredSize().height),
								new Color((int) (Math.random() * 0xFFFFFF)));
						break;
					}
				}

				repaint();
				process();
				Thread.sleep(1000 / FPS);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}