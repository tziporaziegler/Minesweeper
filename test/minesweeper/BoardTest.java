package minesweeper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BoardTest {

	@Test
	public void testBoardSetUpUnder500MS() {
		final long startTime = System.currentTimeMillis();
		new Board(184, 247, 9, 9, 10, 14);

		final long endTime = System.currentTimeMillis();
		final long totalMS = (endTime - startTime);

		System.out.println("new Board() time: " + totalMS + "ms");
		assertTrue(totalMS < 500);
	}
}
