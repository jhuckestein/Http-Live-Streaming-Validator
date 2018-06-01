package edu.psgv.sweng861;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*This is the main start of the program with JavaUrlConnectionReader for HLS2.  This version runs in both Batch Mode
 * and Command Line mode.
 */
public class JavaUrlConnectionReader {
	// Put the Logger in as part of the requirements.
	private static final Logger logger = LogManager.getLogger();
		

/* The getUrlContents method is what is used to pass a string to get the URL.  If the URL
 * is bad, then it prints that condition out, but continues to read the file.  If the file
 * is not a simple html, then there is logic to open different types of files.	
 */
	public static String getUrlContents(String theUrl){      //This method gets the contents of the URL and prints
		logger.info(">>getUrlContents()");
		String content = "";
		
		try {
			URL url = new URL(theUrl);
			logger.debug("The URL was: {}", url.toString());
			URLConnection urlConnection = url.openConnection();
			logger.info("The urlConnection was: {}", urlConnection.toString());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			logger.debug("The bufferedReader was successful.");
			String line;                                 
			//int i = 0;                                        //integer to test if this is the first line.
			while ((line = bufferedReader.readLine()) != null){   
				//if (i == 0){
				//	Validator.checkPlaylist(line);   //This needs to come out for logic reasons and check in main part.
				//	i++;
				//}
				content += line + "\n";      
			}
			bufferedReader.close();
			
			
		} catch (MalformedURLException e){
			logger.error("The url was bad: {}", e.toString());
		} catch (IOException e) {
			logger.error("There was a problem reading contents of the URL: {}", e.toString());
		} catch (Exception e){
			logger.error("There was a general exception that occured: {}", e.toString());
		}
		logger.info("<<getUrlContents()");
		return content;    //above I built up a bunch of lines with carriage returns.  This returns all of it.
	}

}
