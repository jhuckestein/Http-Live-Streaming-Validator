package edu.psgv.sweng861;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* 
 * This visitor checks to make sure that each segment does not exceed the maximum target duration for the playlist.
 */
public class SegmentLengthChecker extends Checker {
	
	private static final Logger logger = LogManager.getLogger();

	/* This method iterates over the p.Playlist array looking for #EXT-X-TARGETDURATION and sets maxDuration.
	 * Just to make sure it also checks to see if this tag appears more than once in the array, which would be a 
	 * violation.  When a playlist file with #EXTINF is too long a validationReport entry is submitted which details
	 * the offending file.
	 */
	@Override
	void visit(Playlist p) {
		logger.info(">>SegmentLengthChecker Playlist()");
		int errorCount = 0;
		int tracker = 0;
		int maxDuration = 0;
		float fileDuration = 0;
		int duration = 0;
		String delims = "[:,]";
		String test = "#EXT-X-TARGETDURATION";
		String durationTag = "#EXTINF";
		for (int i = 0; i<p.playlist.size(); i++){     //Find the maxDuration and check for multiple tags.
			String line = p.playlist.get(i);
			if (line.contains(test)){
				tracker++;
				String[] tokens = line.split(delims);
				maxDuration = Integer.parseInt(tokens[1]);
			} else if (line.contains(durationTag)){
				String[] tokens = line.split(delims);      //Get the string from the #EXTINF line
				fileDuration = Float.valueOf(tokens[1]);   //Get the float value for the file length
				duration = (int)Math.ceil(fileDuration);   //Round the value to the next integer
				if (duration > maxDuration){               //Test for violation
					String nextLine = p.playlist.get(i + 1);    //The violating file is the next line
					p.addReport("Error on line: " + (i+1) + " maximum Media Segment duration :" + nextLine);
					errorCount++;
				}
			}
			if (tracker > 1){
				logger.error("There are too many EXT-X-TARGETDURATION tags in the playlist {}", tracker);
				p.addReport("There are too many instances of EXT-X-TARGETDURATION in the playlist " + tracker);
			}
		}
		p.addReport("There are: " + errorCount + " maximum Media Segment duration violations");
		p.addReport("=============================================================");
		p.addReport("=============================================================");
		logger.info("<<SegmentLengthChecker Playlist()");
	}
	
	void visitMaster(MasterPlaylist p){
		logger.info(">>SegmentLengthChecker MasterPlaylist()");
		for (int i=0; i<p.variantFiles.size(); i++){
			p.addReport("Report of Maximum Duration of Media Segment Not Exceeded for: " + p.variantFiles.get(i));
			Playlist playlistElement = p.listOfPlayObject.get(i);       //Get the i object in the listOfPlayObject array
			int errorCount = 0;
			int tracker = 0;                                            //Tracks multiple EXT-X-TARGETDURATION instances
			int maxDuration = 0;                                        //EXT-X-TARGETDURATION length in seconds
			float fileDuration = 0;                                     //EXTINF length in seconds
			int duration = 0;
			String delims = "[:,]";                                     //Delimeter for lines in Playlist
			String test = "#EXT-X-TARGETDURATION";
			String durationTag = "#EXTINF";
			for (int k = 0; k<playlistElement.playlist.size(); k++){     //Iterate to find the maxDuration and check for multiple tags.
				String line = playlistElement.playlist.get(k);
				if (line.contains(test)){                            //Test to see if EXT-X-TARGETDURATION is in line
					tracker++;                                       //If > 1 then multiple instances of TARGETDURATION
					String[] tokens = line.split(delims);            //Parse to get the length number
					maxDuration = Integer.parseInt(tokens[1]);
				} else if (line.contains(durationTag)){
					String[] tokens = line.split(delims);      //Get the string from the #EXTINF line
					fileDuration = Float.valueOf(tokens[1]);   //Get the float value for the file length
					duration = (int)Math.ceil(fileDuration);   //Round the value to the next integer
					if (duration > maxDuration){               //Test for violation
						String nextLine = playlistElement.playlist.get(k + 1);    //The violating file is the next line
						p.addReport("Error on line: " + (k +1)+ " maximum Media Segment duration :" + nextLine);
						errorCount++;
					}
				}
				if (tracker > 1){
					logger.error("There are too many EXT-X-TARGETDURATION tags in the playlist {}", tracker);
					p.addReport("There are too many instances of EXT-X-TARGETDURATION in the playlist " + tracker);
				}
			}
			p.addReport("There are: " + errorCount + " maximum Media Segment duration violations");
			p.addReport("=============================================================");
		}
		p.addReport("=============================================================");
		p.addReport("=============================================================");
		logger.info("<<SegmentLengthChecker MasterPlaylist()");
	}

}
