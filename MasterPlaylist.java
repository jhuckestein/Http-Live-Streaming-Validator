package edu.psgv.sweng861;

import java.util.ArrayList;


/*The MasterPlaylist has an ArrayLIst of variants, and also contains a variable number of playlist, one for each 
 * variant that exists int the first ArrayList.  The ArrayLists are just strings.
 */
public class MasterPlaylist extends Playlist {
	
	
	/*
	 * The constructor takes the read string, and parses them into the array elements which compose the 
	 * Master Variant ArrayList.  A no args constructor for "implicit" had to be added to make eclipse happy.
	 */
	public MasterPlaylist (){}
	
	ArrayList<String> masterVariantList = new ArrayList<String>();        //The full list from the MasterPlaylist file
	ArrayList<Playlist> listOfPlayObject = new ArrayList<Playlist>();     //List of all the playlist objects
	ArrayList<String> variantFiles = new ArrayList<String>();             //Just the simple Playlist variants 
	String delims = "[\n]+";
	
	public MasterPlaylist (String input){
		logger.info(">>MasterPlaylist()");
		String[] tokens = input.split(delims);
		for (int i=0; i < tokens.length; i++){
			masterVariantList.add(tokens[i]);
		}
		logger.info("<<MasterPlaylist()");
	}
	
	/*The constructor creates a list of Play Objects above which are the list of play files for a specific
	 * variant.  The JavaURLConnectionReader will call getURLContents to create a new Playlist and populate 
	 * the listOfPlayObject with these objects.
	 */
	
	public void addPlaylist(Playlist input){
		logger.info(">>addPlaylist()");
		listOfPlayObject.add(input);
		logger.info("<<addPlaylist()");
	}
	
	/*
	 * It is helpful to keep track of the variant files for later report printing.  The addVariantFile method adds
	 * a string to the variantFiles arraylist which contains the full variant file URL.
	 */
	public void addVariantFile(String input){
		logger.info(">>addVariantFile()");
		variantFiles.add(input);
		logger.info("<<addVariantFile()");
	}
	
	public void accept (Checker c){
		c.visitMaster(this);          //This method has to match the Checker methods to select the correct one in the 
	}                                 //concrete visitor classes, or you will just get whatever Playlist has as the method.
}
