package mypackage;

public class FirstTagChecker extends Checker {
	void visit(MasterPlaylist m){
		System.out.println("I am visiting the MasterPlaylist object");
		m.testString = "This is a MasterPlaylist object";
		//I just added the int i to be different than the playlist method, but it didn't select the correct method.
	}
	
	void visit(Playlist p){
		System.out.println(p.getClass() + " class type");    //This shows a MasterPlaylist class type and could be used to call
		System.out.println("I am visiting a Playlist");      // a certain method etc. for a specific Playlist or Master type.
		p.testString = "This is a regular Playlist object";
	}
}

/*  Maybe the architectural change to make is to just have one visit method.  Then that method can then use "Type" to 
 * determine what kind of object we are visiting, and then call a regular playlist or MasterPlaylist method. 
 */
