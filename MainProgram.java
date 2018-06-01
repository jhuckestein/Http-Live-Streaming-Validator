package edu.psgv.sweng861;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/*
 * This is the main program where all of the execution logic and steps are located.  In HLS2 this was JavaUrlConnectionReader
 * but this became too bloated.  Now more classes have been compartmentalized to ensure decoupling and prevent 
 * spaghetti code from developing.
 */
public class MainProgram {

	private static final Logger logger = LogManager.getLogger();
	public static void main(String[] args) {
		logger.info(">>main()");
		//The first thing is to determine whether we are in batch mode or not.
		
		if (args.length > 0 ) {                       //Here is what happens in batch mode
			System.out.println("Program HLS3.1 running in batch mode.");
			System.out.println("Enter the full directory path and filename for the file containing URLs of Playlists and MasterPlaylists: ");
			Scanner scanner = new Scanner(System.in);
			File textFile = new File(scanner.nextLine());                    //assign the input to a textFile
			System.out.println("The batch file that was entered was: " + textFile.getName());
			
			//Now read the contents of the file line by line, and call getUrlContents
			try(BufferedReader br = new BufferedReader(new FileReader(textFile))){
				String line;
				while ((line = br.readLine()) != null){
					//Now check the URL to see if the file specified is valid
					int urlCheck = Validator.checkURL(line);
					if (urlCheck == 200){
						//System.out.println("The Valid URL was: " + line);
						String output = JavaUrlConnectionReader.getUrlContents(line);    //the line from file is valid retrieve the URL
						if (Validator.checkForMaster(output)){
							System.out.println("This file contains a Master Playlist: " + line);
						
							//We need the root of the URL, so we need to get it from line
							int endPoint = line.lastIndexOf('/');
							endPoint++;                          //this is necessary to pick up the slash
							CharSequence rootURL = line.subSequence(0,  endPoint);
							
							//Create a MasterPlaylist with the masterVariantList
							MasterPlaylist masterPlay = new MasterPlaylist(output);
							masterPlay.master = true;
							
							
							retrieveVariants(masterPlay, rootURL);
			
							
							// You still have to instantiate the checker classes before passing them to the accept method.
							// *******************************************************************************************
							
							FirstTagChecker firstTagChecker = new FirstTagChecker();
							SegmentLengthChecker segmentLengthChecker = new SegmentLengthChecker();
							URISequenceChecker uriSequenceChecker = new URISequenceChecker();
							masterPlay.accept(firstTagChecker);     
							masterPlay.accept(segmentLengthChecker);
							masterPlay.accept(uriSequenceChecker);
							// masterPlay.accept(tagValidityChecker);
							
							printReport(masterPlay);
							
							
						}  else {
							System.out.println("This file contains a Simple Playlist: " + line);
							//Now create a new playlist object
							Playlist simplePlaylist = new Playlist(output);  //Simple Object created
							simplePlaylist.master=false;
						
							
							FirstTagChecker firstTagChecker = new FirstTagChecker();
							SegmentLengthChecker segmentLengthChecker = new SegmentLengthChecker();
							URISequenceChecker uriSequenceChecker = new URISequenceChecker();
							simplePlaylist.accept(firstTagChecker);     
							simplePlaylist.accept(segmentLengthChecker);
							simplePlaylist.accept(uriSequenceChecker);
							
							printReport(simplePlaylist);
							
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
				System.out.println("Can't seem to find the file: " + textFile.toString());
			} catch (IOException e1) {
				System.out.println("Can't seem to read the file" + textFile.toString());
			}
			scanner.close();
							
			
			
		} else {                                      //Here is what happens in command mode
			System.out.println("This is HLS Validator 3.1 Command Line"); 
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the URL of the playlist, or 0 to quit:");
			String urlEntry = scanner.next();
			
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
						String output = JavaUrlConnectionReader.getUrlContents(urlEntry);   //Use the URL directly, no buffered reader
						//System.out.println(output);                 //Here I could take the output, ID, and load into an object
						if (Validator.checkForMaster(output)){
							System.out.println("This input was a Master Playlist");
							
							//I need the root of the URL, so parse from the right on urlEntry
							int endPoint = urlEntry.lastIndexOf('/');
							endPoint++;                               //This is necessary to pick up the slash
							CharSequence rootURL = urlEntry.subSequence(0, endPoint);
						
							
							//Create a MasterPlaylist with the masterVariantList
							MasterPlaylist masterPlay = new MasterPlaylist(output);
							masterPlay.master = true;
							
							
							retrieveVariants(masterPlay, rootURL);
						
							FirstTagChecker firstTagChecker = new FirstTagChecker();
							SegmentLengthChecker segmentLengthChecker = new SegmentLengthChecker();
							URISequenceChecker uriSequenceChecker = new URISequenceChecker();
							masterPlay.accept(firstTagChecker);     
							masterPlay.accept(segmentLengthChecker);
							masterPlay.accept(uriSequenceChecker);
							// masterPlay.accept(tagValidityChecker);
							
							printReport(masterPlay);
							
							
							
						} else {
							System.out.println("This input was a Simple Playlist");
							//Now create a new playlist object
							Playlist simplePlaylist = new Playlist(output);  //Simple Object created
							simplePlaylist.master=false;
							
							FirstTagChecker firstTagChecker = new FirstTagChecker();
							SegmentLengthChecker segmentLengthChecker = new SegmentLengthChecker();
							URISequenceChecker uriSequenceChecker = new URISequenceChecker();
							simplePlaylist.accept(firstTagChecker);     
							simplePlaylist.accept(segmentLengthChecker);
							simplePlaylist.accept(uriSequenceChecker);
							
							printReport(simplePlaylist);
							
							
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
		
		logger.info("<<main()");
	}
	
	/*The retrieveVariants method is used to take the masterVariantList which was created as the first arraylist
	 * of the MasterPlaylist object, and retrieve all of the variants to populate them into the Playlist array of 
	 * the MasterPlaylist object which contains all of the simple Playlist objects for Master.
	 */
	private static void retrieveVariants(MasterPlaylist masterPlay, CharSequence rootURL){
		logger.info(">>retrieveVariants()");
		for (int i = 0; i< masterPlay.masterVariantList.size(); i++) {
			//Check to see if the line is a playlist file
			String element = masterPlay.masterVariantList.get(i);
			if (Validator.checkForPlayfile(element) == true) {
				String playlistURL = rootURL + element;       //We now have the URL for the playlist
				masterPlay.addVariantFile(playlistURL);
				String playListOutput = JavaUrlConnectionReader.getUrlContents(playlistURL);    //Retrieve the playlist
				Playlist simplePlaylist = new Playlist(playListOutput);  //Create Playlist object
				masterPlay.addPlaylist(simplePlaylist);  //add the simplePlaylist object to the listOfPlayObject in masterPlay
			}
		}
		logger.info("<<retrieveVariants()");
		return;
	}
	
	/* The printReport method allows me to print out any Playlist or MasterPlaylist object as they all have an arraylist
	 * which was populated by the checkers for formatting.  Just iterate through the array elements and print.
	 */
	private static void printReport(Playlist p){
		logger.info(">>printReport()");
		for (int i=0; i<p.validationReport.size(); i++){
			System.out.println(p.validationReport.get(i));
		}
		logger.info("<<printReport()");
	}
					
}
