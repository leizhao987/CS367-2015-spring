import java.util.ArrayList;

public class SimpleFile {
	private String name;
	private Extension extension;
	private String content;
	private User owner;
	private ArrayList<Access> allowedUsers;
	private String path;
	private SimpleFolder parent;
	
	public SimpleFile(String name, Extension extension, String path, 
			          String content, SimpleFolder parent, User owner) {
		if (name == null || extension == null || path == null || 
			content == null || parent == null || owner == null) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		this.extension = extension;
		this.path = path;
		this.content = content;
		this.parent = parent;
		this.owner = owner;
		
		allowedUsers = new ArrayList<Access>();
	}
	
	//returns the path variable.
	public String getPath(){
		return path;
	}

	//return the parent folder of this file.
	public SimpleFolder getParent() {
		return parent;
	}

	//returns the name of the file.
	public String getName() {
		return name;
	}

	//returns the extension of the file.
	public Extension getExtension() {
		return extension;
	}

	//returns the content of the file.
	public String getContent() {
		return content;
	}

	//returns the owner user of this file.
	public User getOwner() {
		return owner;
	}

	//returns the list of allowed user of this file.
	public ArrayList<Access> getAllowedUsers() {
		return allowedUsers;
	}

	//adds a new access(user+accesstype pair) to the list of allowed user.
	public void addAllowedUser(Access newAllowedUser) {
		if (newAllowedUser == null) {
			throw new IllegalArgumentException();
		}
		allowedUsers.add(newAllowedUser);
	}
	
	//adds a list of the accesses to the list of allowed users.
	public void addAllowedUsers(ArrayList<Access> newAllowedUser) {
		if (newAllowedUser == null) {
			throw new IllegalArgumentException();
		}
		allowedUsers.addAll(newAllowedUser);
	}
	
	
	// returns true if the user name is in allowedUsers.
	// Otherwise return false.
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
	
	
	//removes the file for all users.
	//If the user is owner of the file or the admin or the user has 'w' access,
	//then it is removed for everybody.
	public boolean removeFile(User removeUsr){
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
		
		this.getParent().getFiles().remove(this);
		return owner.removeFile(this);
	}
	
	//returns the string representation of the file.
	@Override
	public String toString() {
		String retString = "";
		retString = name + "." + extension.name() + "\t" + owner.getName() + "\t" ;
		for(Access preAccess : allowedUsers){
			retString = retString + preAccess + " ";
		}
		retString = retString + "\t\"" + content + "\"";
		return retString;
	}
	
}