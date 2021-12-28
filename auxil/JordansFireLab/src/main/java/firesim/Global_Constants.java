
package firesim;

import java.util.Random;

public class Global_Constants {
	
	// 0.0 to 1.0, increase for greater probability of fire spreading
	public static final double PROBABILITY_OF_FIRE = 0.58;
	
	// change to increase the chance that a sapling will sprout from 
	// a fertile cell, 0.0 to 1.0
	public static final double TREE_REGROWTH_RATE = 0.05;
	
	// change this to alter the screen refresh rate
	public static final int REFRESH_DELAY = 60;
	
	public static final int NUM_FIRES_AT_START = 3;
	
	public static final int GRID_WIDTH = 250;
	public static final int GRID_HEIGHT = 250;
	
	public static final int CELL_WIDTH = 5;
	public static final int CELL_HEIGHT = 5;
	
	public static final double NOT_ON_FIRE = 0.0;
	public static final double SMOLDERING  = 0.05 * PROBABILITY_OF_FIRE;
	public static final double BURNING     = 0.12 * PROBABILITY_OF_FIRE;
	public static final double BLAZING     = 0.25 * PROBABILITY_OF_FIRE;
	
	public static final Random RNG = new Random();

}
