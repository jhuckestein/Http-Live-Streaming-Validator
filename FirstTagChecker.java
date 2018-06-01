package edu.psgv.sweng861;

/*
 * This class looks at the first line of the file and looks for a valid playlist file with #EXT3MU.  This is where the
 * checkPlaylist method that was in the Validator class has been moved to.
 */
public class FirstTagChecker extends Checker {

	@Override
	void visit(Playlist p) {
		// Note: checkPlaylist takes a string as input, and returns nothing.  So, need to decide
		// how I want to update the Report Object instead of just printing in the method.
		
	}

}
