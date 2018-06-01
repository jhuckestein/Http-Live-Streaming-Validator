package mypackage;

final class MasterPlaylist extends Playlist {
	boolean master = true;
	int masterInteger;
	String testStringMaster = "I am a Master Playlist Object regardless of what method is used for visitation.";
	
	/*public void accept (Checker c, int i){
		c.visit(this);                         Apparently, this block doesn't override the FirstTagChecker method to Master
	} */
}
