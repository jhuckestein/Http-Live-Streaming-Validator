package edu.psgv.sweng861;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.org.apache.bcel.internal.generic.Visitor;

/*The Playlist class is a simple variant and is just an ArrayList of strings.
 * The constructor below is responsible for taking in a string input, and parsing it into the ArrayList.
 */
@SuppressWarnings("unused")
public class Playlist {
	
	boolean master;
	ArrayList<String> playlist = new ArrayList<String>();
	ArrayList<String> validationReport = new ArrayList<String>();
	String delims = "[\n]+";
	protected static final Logger logger = LogManager.getLogger();
	
	/*
	 * The constructor takes the read string, and parses them into the array elements. The null 
	 * constructor was added first to satisfy the inheritance issues with MasterPlaylist with the compiler.
	 */
	public Playlist (){};
	
	public Playlist (String input){
		logger.info(">>Playlist()");
		String[] tokens = input.split(delims);
		for (int i=0; i < tokens.length; i++){
			playlist.add(tokens[i]);
		}
		logger.info("<<Playlist()");
	}
	
	public void addReport(String input){
		validationReport.add(input);
	}
	
	public void accept (Checker c){
		c.visit(this);
	}
	
	
}
