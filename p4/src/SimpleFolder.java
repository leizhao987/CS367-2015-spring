import java.util.ArrayList;

public class SimpleFolder {

	private String name;
	private String path;//absolute path of the folder.
	private SimpleFolder parent;
	private User owner;
	private ArrayList<SimpleFolder> subFolders;
	private ArrayList<SimpleFile> files;
	private ArrayList<Access> allowedUsers;

	public SimpleFolder(String name, String path, SimpleFolder parent, User owner) {
		if (name == null || path == null || owner == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.path = path;
		this.parent = parent;
		this.owner = owner;
		
		subFolders = new ArrayList<SimpleFolder>();
		files = new ArrayList<SimpleFile>();
		allowedUsers = new ArrayList<Access>();
	}
	
	
	//checks if user - "name" is allowed to access this folder or not. 
	//If yes, return true, otherwise false.
	public boolean containsAllowedUser(String name){
		if (name == null) {
			throw new IllegalArgumentException();
		}
		
		for (Access usr : allowedUsers) {
			if (usr.getUser().getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	//checks if this folder contains the child folder identified by 'fname'.
	// If it does contain then it returns the folder otherwise returns null.
	public SimpleFolder getSubFolder(String fname){
		if (fname == null) {
			throw new IllegalArgumentException();
		}
		
		for (SimpleFolder subFolder : subFolders) {
			if (subFolder.getName().equals(fname)) {
				return subFolder;
			}
		}
		return null;
	}


	//checks if this folder contains the child file identified by "fname".
	// If it does contain, return File otherwise null.
	public SimpleFile getFile(String fname){
		if (fname == null) {
			throw new IllegalArgumentException();
		}
		
		for (SimpleFile file : files) {
			if (file.getName().equals(fname)) {
				return file;
			}
		}
		return null;
	}


	//returns the owner of the folder.
	public User getOwner() {
		return owner;
	}

	//returns the name of the folder.
	public String getName() {
		return name;
	}

	//returns the path of this folder.
	public String getPath() {
		return path;
	}

	//returns the Parent folder of this folder.
	public SimpleFolder getParent() {
		return parent;
	}

	//returns the list of all folders contained in this folder.
	public ArrayList<SimpleFolder> getSubFolders() {
		return subFolders;
	}

	//adds a folder into this folder.
	public void addSubFolder(SimpleFolder subFolder) {
		if (subFolder == null) {
			throw new IllegalArgumentException();
		}
		
		subFolders.add(subFolder);
	}

	//returns the list of files contained in this folder.
	public ArrayList<SimpleFile> getFiles() {
		return files;
	}

	//add the file to the list of files contained in this folder.
	public void addFile(SimpleFile file) {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		
		files.add(file);
	}

	//returns the list of allowed user to this folder.
	public ArrayList<Access> getAllowedUsers() {
		return allowedUsers;
	}

	//adds another user to the list of allowed user of this folder.
	public void  addAllowedUser(Access allowedUser) {
		if (allowedUser == null) {
			throw new IllegalArgumentException();
		}
		
		allowedUsers.add(allowedUser);
	}

	//adds a list of allowed user to this folder.
	public void addAllowedUser(ArrayList<Access> allowedUser) {
		allowedUsers.addAll(allowedUser);
	}

	//If the user is owner of this folder or the user is admin or the user has 'w' privilege
	//, then delete this folder along with all its content.
	public boolean removeFolder(User removeUsr){
		if (removeUsr == null) {
			throw new IllegalArgumentException();
		}
		
		boolean hasAccess = false;
		for (Access ac : allowedUsers) {
			if (ac.getUser().equals(removeUsr) && ac.getAccessType() == 'w')
				hasAccess = true;
		}
		if (!hasAccess)
			return false;
		
		int num_files = this.getFiles().size();
		for (int i = num_files - 1; i >= 0; i--)
			this.getFiles().get(i).removeFile(this.getFiles().get(i).getOwner());
		
		// recursively deletes all the sub files and sub folders in this dir
		int num_folders = this.getSubFolders().size();
		for (int j = num_folders -1; j >= 0; j--) {
			this.getSubFolders().get(j).removeFolder(this.getSubFolders().get(j).getOwner());
		}
		
		this.getParent().getSubFolders().remove(this);
		return this.getOwner().removeFolder(this);
	}


	//returns the string representation of the Folder object.
	@Override
	public String toString() {
		String retString = "";
		retString = path + "/" + name + "\t" + owner.getName() + "\t";
		for(Access preAccess: allowedUsers){
			retString = retString + preAccess + " ";
		}

		retString = retString + "\nFILES:\n";

		for(int i=0;i<files.size();i++){
			retString = retString + files.get(i);
			if(i != files.size()-1)
				retString = retString + "\n";

		}				
		return retString;
	}


}
