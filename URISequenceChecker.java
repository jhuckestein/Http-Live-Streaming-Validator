package edu.psgv.sweng861;

/*
 * This visitor checks to ensure that the playlist object does not mix and match Playlist and MasterPlaylist file 
 * extensions.  Playlist should not have .m3u8 and MasterPlaylist should not have .ts files.
 */
public class URISequenceChecker extends Checker {

	@Override
	void visit(Playlist p) {
		// This has no predecessor method.  In this case we don't want to mix and match the Playlist and MasterPlaylist
		// entries.  This means a Playlist should not have .m3u8 extension, and MasterPlaylists should not have .ts files.
		
	}

}
