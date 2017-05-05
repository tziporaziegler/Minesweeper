package minesweeper;

public class TimerDisplay extends DisplayLabel {
	private static final long serialVersionUID = 1L;

	private int amtTime;

	public TimerDisplay(float colorNum) {
		super(colorNum);
	}

	@Override
	public void increment() {
		setText(++amtTime);
	}

	@Override
	public void decrement() {
	}

	@Override
	public void reset() {
		super.reset();

		amtTime = 0;
		setText(amtTime);
	}
}
