import java.util.ArrayList;


public class SimpleFileSystem {

	SimpleFolder root;
	ArrayList<User> users;
	SimpleFolder currLoc;
	User currUser;

	//constructor
	public SimpleFileSystem(SimpleFolder _root, ArrayList<User> _users) {
		if (_root == null || _users == null) {
			throw new IllegalArgumentException();
		}
		
		this.root = _root;
		this.users = _users;
		this.reset();
	}

	// resets everything to default values.
	// i.e., currUser to admin.
	// and currLoc = root.
	// It does not delete anything. It just reset the pointers to original values.
	public void reset(){
		if (this.containsUser("admin") == null)
			System.out.println("Admin user cannot be found in reset().");
		this.currUser = this.containsUser("admin");
		this.currLoc = root;
	}


	//gets currUser.
	public User getCurrUser() {
		return currUser;
	}

	//sets the current user to the user with name passed in the argument.
	public boolean setCurrentUser(String name){
		if (name == null) {
			throw new IllegalArgumentException();
		}
		
		User usr = this.containsUser(name);
		if (usr != null) {
			currUser = usr;
			currLoc = root;
			return true;
		}
		return false;
	}


	//checks if the user is contained in the existing users list or not.
	public User containsUser(String name){
		if (name == null) {
			throw new IllegalArgumentException();
		}
		
		for (User usr : users) {
			if (usr.getName().equals(name)) {
				return usr;
			}
		}
		return null;
	}
	
	
	//checks whether curr location contains any file/folder with name name = fname
	public boolean containsFileFolder(String fname){
		return (currLoc.getSubFolder(fname) != null || currLoc.getFile(fname) != null);
	}
	

	//changes the current location. If successful returns true, false otherwise.
	public boolean moveLoc(String argument){
		if (argument == null) {
			throw new IllegalArgumentException();
		}
		
		// this doesn't support a combination of absolute and relative path searching
		SimpleFolder newLoc = currLoc;
		String[] paths = argument.split("/");
				
		if (argument.charAt(0) == '/') {
			newLoc = root;
			if (!root.getName().equals(paths[1]))
				return false;
			
			for (int i = 2; i < paths.length; i++) {
				if ((newLoc = newLoc.getSubFolder(paths[i])) == null)
					return false;
			}
		}
		else if (argument.length() > 2 && argument.substring(0, 3).equals("../")) {
			for (String dir : paths) {
				if (dir.equals("..")) {
					if ((newLoc = newLoc.getParent()) == null)
						return false;
				}
				else {
					if ((newLoc = newLoc.getSubFolder(dir)) == null)
						return false;
				}
			}
		}
		else {
			for (String dir : paths) {
				if ((newLoc = newLoc.getSubFolder(dir)) == null)
					return false;
			}
		}
		
		currLoc = newLoc;
		return true;
	}
	
	//returns the currentlocation.path + currentlocation.name.
	public String getPWD(){
		return ((currLoc.getPath().isEmpty() ? "" : (currLoc.getPath()+"/")) + currLoc.getName());
	}


	//deletes the folder/file identified by the 'name'
	public boolean remove(String name){
		if (name == null) {
			throw new IllegalArgumentException();
		}
		
		if (!this.containsFileFolder(name))
			return false;
		
		SimpleFolder folder = currLoc.getSubFolder(name);
		SimpleFile file = currLoc.getFile(name);
		
		if (folder != null)
			return folder.removeFolder(currUser);
		else
			return file.removeFile(currUser);
	}
	
	//Gives the access 'permission' of the file/folder fname to the user if the 
	//current user is the owner of the fname. If succeed, return true, otherwise false.
	public boolean addUser(String fname, String username, char permission){
		if (fname == null || username == null) {
			throw new IllegalArgumentException();
		}
		
		if ((permission != 'w' && permission != 'r') || 
			 containsUser(username) == null || !containsFileFolder(fname))
			return false;
	
		SimpleFile file = currLoc.getFile(fname);
		SimpleFolder folder = currLoc.getSubFolder(fname);
		
		if (file != null && file.getOwner().equals(currUser)) {
			file.addAllowedUser(new Access(this.containsUser(username),permission));
			return true;
		}
		else if (folder.getOwner().equals(currUser)){
			folder.addAllowedUser(new Access(this.containsUser(username), permission));
			ArrayList<SimpleFile> subFiles = folder.getFiles();
			for (SimpleFile subFile : subFiles)
				subFile.addAllowedUser(new Access(this.containsUser(username), permission));
			return true;
		}
		else
			return false;
	}
	

	//displays the user info in the specified format.
	public boolean printUsersInfo(){
		if (!currUser.equals(this.containsUser("admin"))) {
			return false;
		}
		
		String str = "";
		for (User usr : users) {
			str += usr.toString() + "\n";
		}
		System.out.print(str);
		return true;
	}

	//makes a new folder under the current folder with owner = current user.
	public void mkdir(String name){
		if (name == null) {
			throw new IllegalArgumentException();
		}

		if (this.containsFileFolder(name)) {
			System.out.println("No duplicate folder is allowed");
			return;
		}
		
		SimpleFolder newSub = new SimpleFolder(name, this.getPWD(), currLoc, currUser);
		if (this.containsUser("admin") == null) {
			System.out.println("Admin user cannot be found in mkdir.");
			return;
		}
		
		newSub.addAllowedUser(new Access(currUser, 'w'));
		if (!currUser.getName().equals("admin")) {
			newSub.addAllowedUser(new Access(this.containsUser("admin"), 'w'));			
		}
		
		currLoc.addSubFolder(newSub);
		currUser.addFolder(newSub);
	}


	//makes a new file under the current folder with owner = current user.
	public void addFile(String filename, String fileContent){
		if (filename == null) {
			throw new IllegalArgumentException();
		}
		
		String[] names = filename.split("\\.");

		if (this.containsFileFolder(names[0])) {
			System.out.println("No duplicate file is allowed");
			return;
		}
		
		String content = (fileContent == null) ? "" : fileContent;
		
		SimpleFile newFile = new SimpleFile(names[0], Extension.valueOf(names[1]), this.getPWD(), content, currLoc, currUser);
		
		newFile.addAllowedUser(new Access(currUser, 'w'));
		if (!currUser.getName().equals("admin")) {
			newFile.addAllowedUser(new Access(this.containsUser("admin"), 'w'));			
		}

		currLoc.addFile(newFile);
		currUser.addFile(newFile);
	}


	//prints all the folders and files under the current user for which user has access.
	public void printAll(){		
		for(SimpleFile f : currLoc.getFiles()){
			if(f.containsAllowedUser(currUser.getName()))
			{
				System.out.print(f.getName() + "." + f.getExtension().toString() + " : " + f.getOwner().getName() + " : ");
				for(int i =0; i<f.getAllowedUsers().size(); i++){
					Access a = f.getAllowedUsers().get(i);
					System.out.print("("+a.getUser().getName() + "," + a.getAccessType() + ")");
					if(i<f.getAllowedUsers().size()-1){
						System.out.print(",");
					}
				}
				System.out.println();
			}
		}
		for(SimpleFolder f: currLoc.getSubFolders()){
			if(f.containsAllowedUser(currUser.getName()))
			{
				System.out.print(f.getName() + " : " + f.getOwner().getName() + " : ");
				for(int i =0; i<f.getAllowedUsers().size(); i++){
					Access a = f.getAllowedUsers().get(i);
					System.out.print("("+a.getUser().getName() + "," + a.getAccessType() + ")");
					if(i<f.getAllowedUsers().size()-1){
						System.out.print(",");
					}
				}
				System.out.println();
			}
		}
	}

}