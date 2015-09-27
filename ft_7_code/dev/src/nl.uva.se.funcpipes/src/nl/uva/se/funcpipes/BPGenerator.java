package nl.uva.se.funcpipes;

import java.util.Random;

public class BPGenerator {

	private static int minimumVal = 50;
	private static int maximumVal = 160;
	private static Random rand = new Random();
	
	public BPGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public static BloodPressure generateBP() {

		int bp = 50 + rand.nextInt(maximumVal - minimumVal + 1);
		return new BloodPressure(bp);
	}
}
