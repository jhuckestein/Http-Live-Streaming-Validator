package edu.psgv.sweng861;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

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
			logger.warn("The url was bad: {}", e.toString());
		} catch (IOException e) {
			logger.error("There was an IOException: {}", e.toString());
		}
		logger.info("<<checkURL()");
		return statusCode;
	}
	
	//The checkPlaylist method tests to see if EXTM3U is in the first line of the file.  If not, we still print,
	//but the output is tagged as being invalid etc.  This proves it is a valid playlist or MasterPlaylist
	static void checkPlaylist(String firstLine) {
		logger.info(">>checkPlaylist()");
		String testVariable = "#EXTM3U";
		if (firstLine.equals(testVariable)){
			logger.debug("The file is a valid playlist.");
			//System.out.println("This is a valid Playlist file");
		} else {
			logger.debug("The file is not valid EXTM3U.");
			System.out.println("This is not a valid PlayList file, but we will print anyways.");
		}
		logger.info("<<checkPlaylist()");
		return;
	}
	/* checkForMaster looks for #EXT-X-STREAM-INF, if it finds that we create a Master Object.
	 * The method reads the string from getUrl and determines if the Master tag is found in the String.  If 
	 * the string is found the file is a Master, if not the already validated file must be a simple playlist.
	 */
	static boolean checkForMaster(String input){
		logger.info(">>checkForMaster()");
		String testVariable = "#EXT-X-STREAM-INF";
		boolean result = true;
		int checkInteger = input.indexOf(testVariable);
		if (checkInteger > -1) {
			result = true;
			logger.debug("The file was determined to be Master.");
		} else {
			result = false;
			logger.debug("The file was determined to be Simple.");
		}
		logger.info("<<checkForMaster()");
		return result;
	}
	
	/*
	 * checkForPlayfile is used to test whether a particular string has the m3u8 file extension.  In this way
	 * the main program can see whether a line is a variant, and then build a simple playlist object.
	 */
	static boolean checkForPlayfile(String input){
		logger.info(">>checkForPlayfile()");
		String testVariable = "m3u8";
		boolean result = true;
		int checkInteger = input.indexOf(testVariable);
		if (checkInteger > -1){
			result = true;
			logger.info("This is a play file: {}", input);
		} else {
			result = false;
			logger.warn("This is not a play file: {}", input);
		}
		logger.info("<<checkForPlayfile()");
		return result;
	}
	

}



