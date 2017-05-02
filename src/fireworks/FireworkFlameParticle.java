package fireworks;

import java.awt.Color;

public class FireworkFlameParticle implements Particle {
	private double x;
	private double y;
	private int width;
	private int height;
	private double xVelocity;
	private double yVelocity;
	private Color color;

	// No yVelocity as it just uses gravity constant to go down.
	public FireworkFlameParticle(double x, double y, int width, int height, double xVelocity, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xVelocity = xVelocity;
		this.yVelocity = 0; // Not used.
		this.color = color;
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

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Color getColor() {
		return color;
	}
}
