package minesweeper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FontsTest {

	@Test
	public void testFontsSetUpUnder150MS() {
		long startTime = System.currentTimeMillis();
		
		new Fonts();

		long endTime = System.currentTimeMillis();
		long totalMS = endTime - startTime;

		System.out.println("new Fonts() time: " + totalMS + "ms");
		assertTrue(totalMS < 150);
	}
}
