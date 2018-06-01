package mypackage;


public abstract class Checker {
	abstract void visit(Playlist p);
	//abstract void visit(MasterPlaylist p);
	
	//So, why can't the compiler tell the difference between a MasterPlaylist, and a Playlist?  A seemingly simple example of
	//Polymorphism doesn't seem to work.  A child class should be able to substitute for a parent class.

}
