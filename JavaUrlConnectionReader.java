package edu.psgv.sweng861;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*This is the main start of the program with JavaUrlConnectionReader.  Im not sure this is the best name
 * but, it was a good place to start.  The switch at the beginning allows me to determine batch mode or 
 * command line mode.  Then the command line keeps going until the user enters 0.
 */
public class JavaUrlConnectionReader {
	//put the Logger in for the JavaUrlConnectionReader class
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		logger.info(">>main()");
		
		if (args.length > 0 ) {
			System.out.println("Program running in batch mode.");
			String fileName = "D:\\eclipse-java-neon-1a-win32-x86_64\\eclipse\\Workspace-Sweng861\\HLS1\\src\\main\\java\\edu\\psgv\\sweng861\\urlListFile";
			File textFile = new File(fileName);                    //assign the name of urlListFile to textFile
			
			//Now read the contents of the file line by line, and call getUrlContents
			try(BufferedReader br = new BufferedReader(new FileReader(textFile))){
				String line;
				while ((line = br.readLine()) != null){
					String output = getUrlContents(line);
					System.out.println(output);
				}
			} catch (FileNotFoundException e) {
				System.out.println("Can't seem to find the file: " + textFile.toString());
			} catch (IOException e1) {
				System.out.println("Can't seem to read the file" + textFile.toString());
			}	
	
		} else {                          //This is the other case where the command line is used.
			System.out.println("This is HLS Validator 1.0 Command Line"); 
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the URL of the playlist, or 0 to quit:");
			String urlEntry = scanner.next();
			logger.info("urlEntry Length was: {}", urlEntry.length());
			
			//This next do block is to run while we take inputs from the user
			boolean validInput = true;
			
			
			do {
				if (urlEntry.charAt(0) == '0'){                            //case where user wants to quit
					System.out.println("Thank you the program will end now.");
					validInput = false;
					
				} else {	                                                  //The user attempted to type in a URL
					logger.info("Command mode was chosen.");
		
					//Call checkURL to see if it is valid and proceed if 200
					int urlCheck = Validator.checkURL(urlEntry);
					if (urlCheck == 200){                                  //the good case we want
						System.out.println("The Valid URL was: " + urlEntry);
						String output = getUrlContents(urlEntry);   //Use the URL directly, no buffered reader
						System.out.println(output);
						System.out.println("Enter another URL for a playlist, or 0 to quit: ");
						urlEntry = scanner.next();
					} else if (urlCheck == 404) {                         //good URL but no file found at location
						logger.info("The URL supplied had a 404 no file error: {}", urlEntry);
						System.out.println("The URL returned 404 file not found, please check the URL: " + urlEntry);
						System.out.println("Enter another URL for a playlist, or 0 to quit: ");
						urlEntry = scanner.next();
					} else {                                              //all other error scenarios for URL
						logger.info("The URL supplied was not valid {}", urlEntry);
						System.out.println("You did not give a valid URL: " + urlEntry);
						System.out.println("Enter another URL for a playlist, or 0 to quit: ");
						urlEntry = scanner.next();
					}
				}
			} while (validInput);
			
			
			scanner.close();
			logger.info("<<main()");
		}		
	}
	
/* The getUrlContents method is what is used to pass a string to get the URL.  If the URL
 * is bad, then it prints that condition out, but continues to read the file.  If the file
 * is not a simple html, then there is logic to open different types of files.	
 */
	private static String getUrlContents(String theUrl){      //This method gets the contents of the URL and prints
		logger.info(">>getUrlContents()");
		String content = "";
		
		try {
			URL url = new URL(theUrl);
			logger.debug("The URL was: {}", url.toString());
			URLConnection urlConnection = url.openConnection();
			logger.debug("The urlConnection was: {}", urlConnection.toString());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			logger.debug("The bufferedReader was successful.");
			String line;                                 
			int i = 0;                                        //integer to test if this is the first line.
			while ((line = bufferedReader.readLine()) != null){   
				if (i == 0){
					Validator.checkPlaylist(line);            //if the integer = 0 then see if EXMT3U is in line
					i++;
				}
				content += line + "\n";      
			}
			bufferedReader.close();
			
			
		} catch (MalformedURLException e){
			logger.debug("The url was bad: {}", e.toString());
		} catch (IOException e) {
			logger.debug("There was a problem reading contents of the URL: {}", e.toString());
		} catch (Exception e){
			logger.debug("There was a general exception that occured: {}", e.toString());
		}
		logger.info("<<getUrlContents()");
		return content;
	}

}
