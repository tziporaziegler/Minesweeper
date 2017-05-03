package fireworks;

import java.awt.Color;

public class Firework implements Particle {
	public FireworkParticle[] fireworkParticle;
	public FireworkFlameParticle[] fireworkFlameParticle;

	private double x;
	private double y;
	private int width;
	private int height;
	private double xVelocity;
	private double yVelocity;
	private int endY;
	private Color color;
	private boolean exploded;

	public Firework(double x, double y, int width, int height, double yVelocity, int endY, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xVelocity = 0.0; // Not used.
		this.yVelocity = yVelocity;
		this.endY = endY;
		this.color = color;
		setExploded(false);
		this.fireworkParticle = new FireworkParticle[200];
		this.fireworkFlameParticle = new FireworkFlameParticle[200];
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	@Override
	public double getxVelocity() {
		return xVelocity;
	}

	@Override
	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	@Override
	public double getyVelocity() {
		return yVelocity;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public int getEndY() {
		return endY;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Color getColor() {
		return color;
	}

	public void setExploded(boolean exploded) {
		this.exploded = exploded;
	}

	public boolean hasExploded() {
		return exploded;
	}

}
