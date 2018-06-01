package edu.psgv.sweng861;

/*
 * This is the abstract Visitor class which the FirstTagChecker, SegmentLengthChecker, URISequenceChecker, and 
 * TagValidityChecker will extend.  They visit Playlist and MasterPlaylist objects hence the abstract method.
 */
public abstract class Checker {
	abstract void visit(Playlist p);
	abstract void visitMaster(MasterPlaylist p);
}
