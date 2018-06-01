package mypackage;

public class MainProgram {

	public static void main(String[] args) {
		FirstTagChecker firstTagCheckerp = new FirstTagChecker();
		
		MasterPlaylist masterPlaylist = new MasterPlaylist();
		masterPlaylist.accept(firstTagCheckerp);
		System.out.println(masterPlaylist.testString);
		System.out.println(masterPlaylist.testStringMaster);

	}

}
