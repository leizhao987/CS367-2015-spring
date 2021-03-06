///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  VersionControlApp.java
// File:             Repo.java
// Semester:         CS367 Spring 2015
//
// Author:           Lei Zhao
// Email:            lzhao47@wisc.edu
// CS Login:         lzhao
// Lecturer's Name:  Jim Skrentny
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     Lin Qi
// Email:            lqi3@wisc.edu
// CS Login:         lqi
// Lecturer's Name:  Jim SKrentny
///////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a repository which stores and tracks changes to a collection of 
 * documents.
 * @author Lei Zhao, Lin Qi
 *
 */
public class Repo {
	
	/* The current version of the repo. */
	private int version;
	
	/* The name of the repo. It's a unique identifier for a repository. */
	private final String repoName;
	
	/* The user who is the administrator of the repo. */
	private final User admin;
	
	/* The collection(list) of documents in the repo. */
	private final List<Document> docs;
	
	/* The check-ins queued by different users for admin approval. */
	private final QueueADT<ChangeSet> checkIns;
	
	/* The stack of copies of the repo at points when any check-in was applied. */
	private final StackADT<RepoCopy> versionRecords; 

	/**
	 * Constructs a repo object.
	 * @param admin The administrator for the repo.
	 * @param reponame The name of the repo.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public Repo(User admin, String repoName) {
		if (admin == null) {
			throw new IllegalArgumentException();
		}
		
		if (repoName == null) {
			throw new IllegalArgumentException();
		}
		
		this.version = 0;
		this.admin = admin;
		this.repoName =  repoName;
		this.docs = new ArrayList<Document>();
		this.checkIns = new SimpleQueue<ChangeSet>();
		this.versionRecords = new SimpleStack<RepoCopy>();
		
		versionRecords.push(new RepoCopy(this.repoName, 0, this.docs));
	}
	
	/**
	 * Return the name of the repo.
	 * @return The name of the repository.
	 */
	public String getName() {
		return this.repoName;
	}
	
	/**
	 * Returns the user who is administrator for this repository.
	 * @return The admin user.
	 */
	public User getAdmin() {
		return this.admin;
	}
	
	/**
	 * Returns a copy of list of all documents in the repository.
	 * @return A list of documents.
	 */
	public List<Document> getDocuments() {
		return new ArrayList<Document>(this.docs);
	}
	
	/**
	 * Returns a document with a particular name within the repository.
	 * @param searchName The name of document to be searched.
	 * @return The document if found, null otherwise.
	 * @throws IllegalArgumentException if any argument is null.
	 */
	public Document getDocument(String searchName) {
    	if (searchName == null) {
			throw new IllegalArgumentException();
		}
    	
		for (Document d : this.docs) {
			if (d.getName().equals(searchName)) {
				return d;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the current version of the repository.
	 * @return The version of the repository.
	 */
	public int getVersion() {
		return this.version;
	}
	
	/**
	 * Returns the number of versions (or changes made) for this repository.
	 * @return The version count.
	 */
	public int getVersionCount() {
		return versionRecords.size();
	}
	
	/**
	 * Returns the history of changes made to the repository. 
	 * @return The string containing the history of changes.
	 */
	public String getVersionHistory() {
		return versionRecords.toString();
	}
	
	/**
	 * Returns the number of pending check-ins queued for approval.
	 * @return The count of changes.
	 */
	public int getCheckInCount() {
		return checkIns.size();
	}
	
	
	/**
	 * Queue a new check-in for admin approval.
	 * @param checkIn The check-in to be queued.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public void queueCheckIn(ChangeSet checkIn) {
		if (checkIn == null) {
			throw new IllegalArgumentException();
		}
		
		checkIns.enqueue(checkIn);
	}
	
	/**
	 * Returns and removes the next check-in in the queue 
	 * if the requesting user is the administrator.
	 * @param requestingUser The user requesting for the change set.
	 * @return The checkin if the requestingUser is the admin and a checkin
	 * exists, null otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ChangeSet getNextCheckIn(User requestingUser) {
		if (requestingUser == null) {
			throw new IllegalArgumentException();
		}
		
		try {
			if (requestingUser.equals(admin))
				return checkIns.dequeue();
			else
				return null;
		} catch (EmptyQueueException ex) {
			return null;
		}
	}
	
	/**
	 * Applies the changes contained in a particular checkIn and adds
	 * it to the repository if the requesting user is the administrator.
 	 * Also saves a copy of changed repository in the versionRecords.
	 * @param requestingUser The user requesting the approval.
	 * @param checkIn The checkIn to approve.
	 * @return ACCESS_DENIED if requestingUser is not the admin, SUCCESS 
	 * otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType approveCheckIn(User requestingUser, ChangeSet checkIn) {
		if (requestingUser == null || checkIn == null) {
			throw new IllegalArgumentException();
		}
		
		if (!requestingUser.equals(admin)) {
			return ErrorType.ACCESS_DENIED;
		}
		
		Change nextChange = checkIn.getNextChange();
		while (nextChange != null) {
			Document doc = nextChange.getDoc();
			Change.Type type = nextChange.getType();
			
			switch (type) {
			  	case ADD:
			  		if (!this.docs.contains(doc)) {
			  			docs.add(doc);
			  		}
			  		break;
			  		
			  	case DEL:
			  		this.docs.remove(doc);
			  		break;
			  		
			  	case EDIT:
			  		for (Document target : this.docs) {
			  			if (target.getName().equals(doc.getName())) {
			  				target.setContent(doc.getContent());
			  				break;
			  			}
			  		}
			  		break;
			  		
			  	default:
			  		System.out.println("Invalid Change Type.");
					break;
			}
			
			nextChange = checkIn.getNextChange();
		}
		
		version ++;
		RepoCopy newCheckIn = new RepoCopy(this.repoName, this.version, this.docs);
		
		versionRecords.push(newCheckIn);		
		
		return ErrorType.SUCCESS;
	}
	
	/**
	 * Reverts the repository to the previous version if present version is
	 * not the oldest version and the requesting user is the administrator.
	 * @param requestingUser The user requesting the revert.
	 * @return ACCESS_DENIED if requestingUser is not the admin, 
	 * NO_OLDER_VERSION if the present version is the oldest version, SUCCESS 
	 * otherwise.
	 * @throws IllegalArgumentException if any argument is null. 
	 */
	public ErrorType revert(User requestingUser) {
		if (requestingUser == null) {
			throw new IllegalArgumentException();
		}
		
		if (!requestingUser.equals(admin)) {
			return ErrorType.ACCESS_DENIED;
		}
		
		try {
			this.versionRecords.pop();
			RepoCopy prev = this.versionRecords.peek();
			List<Document> prevDocs = prev.getDocuments();
			this.docs.clear();
			this.docs.addAll(prevDocs);
		} catch (EmptyStackException e) {
			versionRecords.push(new RepoCopy(this.repoName, 0, new ArrayList<Document>()));
			return ErrorType.NO_OLDER_VERSION;
		}
		
		this.version --;
		
		return ErrorType.SUCCESS;
	}
}