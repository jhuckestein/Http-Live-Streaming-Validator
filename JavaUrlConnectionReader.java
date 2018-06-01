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
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*This is the main start of the program with JavaUrlConnectionReader for HLS2.  This version runs in both Batch Mode
 * and Command Line mode.
 */
public class JavaUrlConnectionReader {
	// Put the Logger in as part of the requirements.
	private static final Logger logger = LogManager.getLogger();
	
	public static void main(String[] args) {
		logger.info(">>main()");
		
		if (args.length > 0 ) {
			System.out.println("Program running in batch mode.");
			String fileName = "D:\\eclipse-java-neon-1a-win32-x86_64\\eclipse\\Workspace-Sweng861\\HLS1\\src\\main\\java\\edu\\psgv\\sweng861\\urlListFile";
			//File textFile = new File(fileName);                    //assign the name of urlListFile to textFile
			
			//Now read the contents of the file line by line, and call getUrlContents
			try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
				String line;
				while ((line = br.readLine()) != null){
					//Now check the URL to see if the file specified is valid
					int urlCheck = Validator.checkURL(line);
					if (urlCheck == 200){
						System.out.println("The Valid URL was: " + line);
						String output = getUrlContents(line);    //the line from file is valid retrieve the URL
						if (Validator.checkForMaster(output)){
							System.out.println("This is a Master Playlist");
							
							//We need the root of the URL, so we need to get it from line
							int endPoint = line.lastIndexOf('/');
							endPoint++;                          //this is necessary to pick up the slash
							CharSequence rootURL = line.subSequence(0,  endPoint);
							
							//Create a MasterPlaylist with the masterVariantList
							MasterPlaylist masterPlay = new MasterPlaylist(output);
							
							//Next I need to have a loop which takes the variant list, extracts the URL, creates a new
							//playlist object, and saves it into the Master Playlist object.
							
							for (int i = 0; i<masterPlay.masterVariantList.size(); i++) {
								//Check to see if the line is a playlist file
								String element = masterPlay.masterVariantList.get(i);
								if (Validator.checkForPlayfile(element) == true) {
									String playlistURL = rootURL + element;       //We now have the URL for the playlist
									String playListOutput = getUrlContents(playlistURL);    //Retrieve the playlist
									Playlist simplePlaylist = new Playlist(playListOutput);  //Create Playlist object
									masterPlay.addPlaylist(simplePlaylist);  //add the simplePlaylist object to the listOfPlayObject in masterPlay
								}
							}
							//Now that a MasterPlaylist was successfully created print it out in a format where the playlist is
							//printed out right after the variant list etc.
							int playlistIterator = 0;
							for (int i=0; i<masterPlay.masterVariantList.size(); i++) {
								System.out.println(masterPlay.masterVariantList.get(i));
								if (Validator.checkForPlayfile(masterPlay.masterVariantList.get(i)) == true){
									Playlist variant = masterPlay.listOfPlayObject.get(playlistIterator);
									System.out.println("============Variant Play Files For this File============");
									for (int j=0; j<variant.playlist.size(); j++) {
										System.out.println(variant.playlist.get(j));
									}
									playlistIterator++;
								}
							}
						}  else {
							System.out.println("This input was a Simple Playlist");
							//Now create a new playlist object
							Playlist simplePlaylist = new Playlist(output);  //Simple Object created
							
							//Now, print out the simple object as the report
							for(int i = 0; i< simplePlaylist.playlist.size(); i++) {
								System.out.println(simplePlaylist.playlist.get(i));
							}
						}
					} else if (urlCheck == 404){            //good URL but no file found at location
						logger.error("The URL supplied had a 404 no file error: {}", line);
						System.out.println("The URL returned 404 file not found, please check the URL: " + line);
					} else {                                //all other error scenarios for first given URL
						logger.error("The URL supplied was not valid {}", line);
						System.out.println("You did not give a valid URL: " + line);
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("Can't seem to find the file: " + fileName);
			} catch (IOException e1) {
				System.out.println("Can't seem to read the file" + fileName);
			}	
	
		} else {                          //This is the other case where the command line is used.
			System.out.println("This is HLS Validator 2.0 Command Line"); 
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the URL of the playlist, or 0 to quit:");
			String urlEntry = scanner.next();
			logger.trace("urlEntry Length was: {}", urlEntry.length());
			
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
						//System.out.println(output);                 //Here I could take the output, ID, and load into an object
						if (Validator.checkForMaster(output)){
							System.out.println("This input was a Master Playlist");
							
							//I need the root of the URL, so parse from the right on urlEntry
							int endPoint = urlEntry.lastIndexOf('/');
							endPoint++;                               //This is necessary to pick up the slash
							CharSequence rootURL = urlEntry.subSequence(0, endPoint);
						
							
							//Create a MasterPlaylist with the masterVariantList
							MasterPlaylist masterPlay = new MasterPlaylist(output);
							
							//Next I need to have a loop which takes the variant list, extracts the URL, creates a new
							//playlist object, and saves it into the Master Playlist object.
							
							for (int i = 0; i< masterPlay.masterVariantList.size(); i++) {
								//Check to see if the line is a playlist file
								String element = masterPlay.masterVariantList.get(i);
								if (Validator.checkForPlayfile(element) == true) {
									String playlistURL = rootURL + element;       //We now have the URL for the playlist
									System.out.println("This is a playlist file: " + playlistURL.toString());
									String playListOutput = getUrlContents(playlistURL);    //Retrieve the playlist
									Playlist simplePlaylist = new Playlist(playListOutput);  //Create Playlist object
									masterPlay.addPlaylist(simplePlaylist);  //add the simplePlaylist object to the listOfPlayObject in masterPlay
								}
							}
							//Now that a MasterPlaylist was successfully created print it out in a format where the playlist is
							//printed out right after the variant list etc.
							
							//This is just printing out the MasterVariant list for formatting purposes.
							for (int i=0; i<masterPlay.masterVariantList.size(); i++) {
								System.out.println("Printing the MasterVariantList ==================================");
								System.out.println(masterPlay.masterVariantList.get(i));
								System.out.println("Finished");
							}
							
							
							int playlistIterator = 0;
							for (int i=0; i<masterPlay.masterVariantList.size(); i++) {
								System.out.println(masterPlay.masterVariantList.get(i));
								if (Validator.checkForPlayfile(masterPlay.masterVariantList.get(i)) == true){
									Playlist variant = masterPlay.listOfPlayObject.get(playlistIterator);
									System.out.println("============Variant Play Files For this File============");
									for (int j=0; j<variant.playlist.size(); j++) {
										System.out.println(variant.playlist.get(j));
									}
									playlistIterator++;
								}
							}
							
							
						} else {
							System.out.println("This input was a Simple Playlist");
							//Now create a new playlist object
							Playlist simplePlaylist = new Playlist(output);  //Simple Object created
							
							//Now, print out the simple object as the report
							for(int i = 0; i< simplePlaylist.playlist.size(); i++) {
								System.out.println(simplePlaylist.playlist.get(i));
							}
							
						}
						System.out.println("Enter another URL for a playlist, or 0 to quit: ");
						urlEntry = scanner.next();
					} else if (urlCheck == 404) {                         //good URL but no file found at location
						logger.error("The URL supplied had a 404 no file error: {}", urlEntry);
						System.out.println("The URL returned 404 file not found, please check the URL: " + urlEntry);
						System.out.println("Enter another URL for a playlist, or 0 to quit: ");
						urlEntry = scanner.next();
					} else {                                              //all other error scenarios for URL
						logger.error("The URL supplied was not valid {}", urlEntry);
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
			logger.info("The urlConnection was: {}", urlConnection.toString());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			logger.debug("The bufferedReader was successful.");
			String line;                                 
			int i = 0;                                        //integer to test if this is the first line.
			while ((line = bufferedReader.readLine()) != null){   
				if (i == 0){
					Validator.checkPlaylist(line);
					i++;
				}
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
