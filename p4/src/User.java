import java.util.ArrayList;

public class User {
	
	private String name; //name of the user.
	private ArrayList<SimpleFile> files;//list of files owned/created by user
	private ArrayList<SimpleFolder> folders;//list of folder owned by user.

	public User(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		files = new ArrayList<SimpleFile>();
		folders = new ArrayList<SimpleFolder>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException();
		}
		
		if (obj instanceof User) {
			User objUser = (User)obj;
			if (objUser.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	//returns the name of the user.
	public String getName() {
		return name;
	}

	//returns the list of files owned by the user.
	public ArrayList<SimpleFile> getFiles() {
		return files;
	}

	//adds the file to the list of files owned by the user.
	public void addFile(SimpleFile newfile) {
		if (newfile == null) {
			throw new IllegalArgumentException();
		}
		
		files.add(newfile);
	} 
	
	//removes the file from the list of owned files of the user.
	public boolean removeFile(SimpleFile rmFile){
		if (rmFile == null) {
			throw new IllegalArgumentException();
		}
		
		return files.remove(rmFile);
	}

	//returns the list of folders owned by the user.
	public ArrayList<SimpleFolder> getFolders() {
		return folders;
	}

	//adds the folder to the list of folders owned by the user.
	public void addFolder(SimpleFolder newFolder) {
		if (newFolder == null) {
			throw new IllegalArgumentException();
		}
		
		folders.add(newFolder);
	}

	//removes the folder from the list of folders owned by the user.
	public boolean removeFolder(SimpleFolder rmFolder){
		if (rmFolder == null) {
			throw new IllegalArgumentException();
		}
		
		return folders.remove(rmFolder);
	}
	
	//returns the string representation of the user object.
	@Override
	public String toString() {
		String retType = name + "\n";
		retType = retType + "Folders owned :\n";
		for(SimpleFolder preFolder : folders){
			retType = retType + "\t" + preFolder.getPath() + "/" + preFolder.getName() + "\n";
		}
		retType = retType + "\nFiles owned :\n"; 
		for(SimpleFile preFile : files){
			retType = retType + "\t" + preFile.getPath() + "/" + preFile.getName() + "." + preFile.getExtension().toString() + "\n";
		}
		return retType;
	}

}