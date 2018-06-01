package edu.psgv.sweng861;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * This visitor checks to ensure that the playlist object does not mix and match Playlist and MasterPlaylist file 
 * extensions.  Playlist should not have .m3u8 and MasterPlaylist should not have .ts files.
 */
public class URISequenceChecker extends Checker {

	private static final Logger logger = LogManager.getLogger();

	/*
	 * The Playlist visitor method checks for the condition that every EXTINF tag should be followed by a .ts play file.
	 */
	void visit(Playlist p) {
		logger.info(">>URISequenceChecker Playlist()");
		String playlistTestVariable = "#EXTINF";
		String masterPlaylistTest = ".m3u8";
		String masterVariantTag = "EXT-X-STREAM-INF";
		String playlistTestCondition = ".ts";
		int errorCount = 0;
		for (int i = 0; i<p.playlist.size(); i++){
			String line = p.playlist.get(i);                                            //Get the whole string
			if (line.contains(playlistTestVariable)){                                   //Line has #EXTINF
				String nextLine = p.playlist.get(i+1);                                  //Get the next line to check
				int endLine = nextLine.length();                                        //Find out line length to parse
				int endPoint = nextLine.lastIndexOf('.');                               //Find where . is at the end
				CharSequence testSequence = nextLine.subSequence(endPoint, endLine);    //Get the .end of the line characters
				if (testSequence.equals(playlistTestCondition)){                        //Test if nextLine has .ts
					logger.info("This line has correct .ts file next {}", i);
				} else if (testSequence.equals(masterPlaylistTest)){                    //Test if .m3u8 on next line (wrong)
					logger.info("This line has incorrect .m3u8 file next line {}", i);
					p.addReport("Error on line: " + i + " where variant tag follows EXTINF.");
					errorCount++;
				} 
			} else if (line.contains(masterVariantTag)){
				logger.info("This line has incorrect Variant EXT-X-STREAM-INF tag {}", i);
				p.addReport("Error on line: " + i + " variant tag EXT-X-STREAM-INF in a playlist file.");
				errorCount++;
			}
		}
		p.addReport("There are: " + errorCount + " Playlist EXTINF tag followed by play .ts file sequence violations");
		p.addReport("=============================================================");
		p.addReport("=============================================================");
		logger.info("<<URISequenceChecker Playlist()");
	}
	
	void visit(MasterPlaylist p){
		logger.info(">>URISequenceChecker MasterPlaylist()");
		p.addReport("Report of MasterPlaylist tag sequence violations");
		String masterPlaylistTest = ".m3u8";
		String masterVariantTag = "EXT-X-STREAM-INF";
		String playlistTestVariable = "#EXTINF";
		String playlistTestCondition = ".ts";
		int tracker = 0;
		for (int j=0; j<p.masterVariantList.size(); j++){
			String masterLine = p.masterVariantList.get(j);
			System.out.println("masterLine =" + masterLine.toString());      //Look here **************************
			if (masterLine.contains(masterVariantTag)){
				String nextMasterLine = p.masterVariantList.get(j+1);
				if (nextMasterLine.contains(masterPlaylistTest)){
					logger.info("This line has correct .m3u8 file next line {}", j);                 
				} else {
					p.addReport("Line " + j + " is not follwed by a variant .m3u8 file");
					logger.error("This line {} is not followed by the correct .m3u8 file next line", j);
					tracker++;
				}
			} else if (masterLine.contains(playlistTestVariable)){
				logger.error("Line {} has a Playlist EXTINF tag", j);
				p.addReport("Line " + j + " has a Playlist EXTINF tag in the MasterPlaylist");
				tracker++;
			} else if (masterLine.contains(playlistTestCondition)){
				logger.error("Line {} has a Playlist .ts play file in the MasterPlaylist", j);
				p.addReport("Line " + j + " has a .ts play file in the MasterPlaylist");
				tracker++;
			}
		}
		p.addReport("There are " + tracker + " URI Sequence errors in the Master Playlist");
		p.addReport("=============================================================");
		for (int i=0; i<p.variantFiles.size(); i++){
			p.addReport("Report of Playlist tag sequence violations for Variant: " + p.variantFiles.get(i));
			Playlist playlistElement = p.listOfPlayObject.get(i);       //Get the i object in the listOfPlayObject array
			String allLine = playlistElement.toString();                //Convert to a string to convert to string[]
			String delimeter = "[\n]+";                                 //Delimeter for the playlist object
			String[] tokenObject = allLine.split(delimeter);            //Convert to a string we can index
			int errorCount = 0;
			for (int k = 0; k<tokenObject.length; k++){
				String line = tokenObject[k];
				if (line.contains(playlistTestVariable)){
					String nextLine = tokenObject[k+1];
					if (nextLine.contains(playlistTestCondition)){
						logger.info("This line has correct .ts file next line {}", k);
					} else {
						logger.info("This line does not have correct .ts play file next line {}", k);
						p.addReport("Line " + k + " is not followed by a play .ts file");
						errorCount++;
					}
				}
				p.addReport("There are: " + errorCount + " Variant EXTINF tag followed by play file .ts violations");
				p.addReport("=============================================================");
			}
		}
		p.addReport("=============================================================");
		p.addReport("=============================================================");
		logger.info("<<URISequenceChecker MasterPlaylist()");
	}

}
