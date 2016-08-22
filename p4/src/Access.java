
public class Access {
	
	private User user;
	private char accessType;
	
	public Access(User user, char accessType) {
		if (user == null) {
			throw new IllegalArgumentException();
		}
		
		if (accessType != 'w' && accessType != 'r') { // debug: other letters for accessType
			throw new IllegalArgumentException();
		}
		
		this.user = user;
		this.accessType = accessType;
	}

	public User getUser() {
		return this.user;
	}

	public char getAccessType() {
		return this.accessType;
	}

	public void setAccessType(char accessType) {
		if (accessType != 'w' && accessType != 'r') { // debug: other letters for accessType
			throw new IllegalArgumentException();
		}
		this.accessType = accessType;
	}
	
	@Override
	public String toString() {
		return (user.getName() + ":" + accessType);
	}
	
}