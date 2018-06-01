package edu.psgv.sweng861;

/* 
 * This visitor checks to make sure that each segment does not exceed the maximum target duration for the playlist.
 */
public class SegmentLengthChecker extends Checker {

	@Override
	void visit(Playlist p) {
		// This method doesn't have a predecessor, but look at the #EXT-X-TARGETDURATION tag and pull out the 
		// integer to the right of that.  Then the playfiles need to be less than or equal to that number.
		
	}

}
