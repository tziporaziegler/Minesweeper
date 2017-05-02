package fireworks;

import java.awt.Color;

public interface Particle {
	public abstract void setX(double x);

	public abstract double getX();

	public abstract void setY(double y);

	public abstract double getY();

	public abstract void setWidth(int width);

	public abstract int getWidth();

	public abstract void setHeight(int height);

	public abstract int getHeight();

	public abstract void setxVelocity(double xVelocity);

	public abstract double getxVelocity();

	public abstract void setyVelocity(double yVelocity);

	public abstract double getyVelocity();

	public abstract void setColor(Color color);

	public abstract Color getColor();
}
