package edu.psgv.sweng861;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/*The Validator class contains the logic for error checking the URL, validating a file, and validating 
 * that the file begins with EXTM3U.  It also contains the logic to determine if the playlist is master or 
 * a simple playlist
 */
public class Validator {
	private static final Logger logger = LogManager.getLogger();
	
	/*The checkURL method allows the main program to call it and get the response code for the HTTP GET.
	 * The main program can then decide if the return value is 200 to proceed, otherwise discard the URL.
	 */
	static int checkURL(String theURL){
		logger.info(">>checkURL()");
		int statusCode = 8;
		try {
			URL url = new URL(theURL); 
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			statusCode = http.getResponseCode();
			
		} catch (MalformedURLException e) {
			logger.debug("The url was bad: {}", e.toString());
		} catch (IOException e) {
			logger.debug("There was an IOException: {}", e.toString());
		}
		logger.info("<<checkURL()");
		return statusCode;
	}
	
	static void checkPlaylist(String firstLine) {
		logger.info(">>checkPlaylist()");
		String testVariable = "#EXTM3U";
		if (firstLine.equals(testVariable)){
			logger.debug("The file is a valid playlist.");
		} else {
			logger.debug("The file is not valid EXTM3U.");
			System.out.println("This is not a valid PlayList file, but we will print anyways.");
		}
		return;
	}
	// The fileType method here where the MIME type reflects Master or Simple Playlist
	

}
