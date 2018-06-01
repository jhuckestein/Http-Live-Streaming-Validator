package mypackage;

public class Playlist {
	String testString = new String();
	public void accept (Checker c){
		c.visit(this);
	}
}
