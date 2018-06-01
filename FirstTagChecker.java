package edu.psgv.sweng861;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * This class looks at the first line of the file and looks for a valid playlist file with #EXT3MU.  This is where the
 * checkPlaylist method that was in the Validator class has been moved to.
 */
public class FirstTagChecker extends Checker {
	
	private static final Logger logger = LogManager.getLogger();
	
	/*
	 * This method visits the MasterPlaylist object, and checks to see if the first element of MasterVariantList
	 * contains the EXTM3U tag.  It then updates the object's validationReport as appropriate.
	 */
	void visitMaster(MasterPlaylist p){
		logger.info(">>FirstTagChecker MasterPlaylist()");
		String testVariable = "#EXTM3U";
		
		if (p.master == true){           //This is a MasterPlaylist object
			String firstLine = p.masterVariantList.get(0);
			if (firstLine.equals(testVariable)){
				logger.debug("The object is a valid Master Playlist.");
				p.addReport("This is a valid Master Playlist.");
			} else {
				logger.error("The object is NOT a valid Playlist.");
				p.addReport("This is NOT a valid Master Playlist with #EXTM3U at the first line, but we will continue validation.");
			}
		} else {
			logger.error("A  Master Playlist object was handed to the first tag checker, but was marked as Playlist.");
		}
		
		//Now that the MasterPlaylist has been determined to be valid or invalid, we need to do the same for the 
		//individual variant playlist objects by looking at element[0].
		for (int i=0; i<p.listOfPlayObject.size(); i++){
			Playlist playlistElement = p.listOfPlayObject.get(i);     //Get each object sequentially
			String checkLine = playlistElement.playlist.get(0);       //Retrieve the playlist object first line
			if (checkLine.equals(testVariable)){                      //Test if the first line tag is #EXTM3U
				p.addReport("This variant is valid: " + p.variantFiles.get(i));
				logger.info("This variant is valid: ", p.variantFiles.get(i));
			} else {
				p.addReport("This variant is Not a valid playlist with #EXTM3U at the first line: " + p.variantFiles.get(i));
				logger.error("This variant is NOT valid: ", p.variantFiles.get(i));
			}
		}
		p.addReport("=============================================================");
		p.addReport("=============================================================");
		logger.info("<<FirstTagChecker MasterPlaylist()");
	}
	/*
	 * The logic here is to look at the object.master field as a logic check.  If a playlist it should be set to false and
	 * if a Master to true.  The first method below looks at the first element of the playlist array to look for the 
	 * EXTM3U tag, and then updates the playlist's validationReport array as appropriate.
	 */
	
	void visit(Playlist p) {
		
		logger.info(">>FirstTagChecker Playlist()");
		String testVariable = "#EXTM3U";
		
		if (p.master == false){	
			String firstLine = p.playlist.get(0);
			if (firstLine.equals(testVariable)){
			    logger.debug("The object is a valid Playlist.");
			    p.addReport("This is a valid Playlist.");
			} else {
				logger.error("The object is NOT a valid Playlist.");
				p.addReport("This is not a valid Playlist with #EXTM3U as the first line, but we will continue validation.");
			}
		} else {
			logger.error("A playlist object was handed to the first tag checker, but was marked as Master.");
		}
		p.addReport("=============================================================");
		p.addReport("=============================================================");
		logger.info("<<FirstTagChecker visitPlaylist()");
	}
}
