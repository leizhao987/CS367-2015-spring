///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  VersionControlApp.java
// File:             Document.java
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

/**
 * Represents a document within a repository.
 * @author Lei Zhao, Lin Qi
 *
 */
public class Document {
	
	/* The name of the document. It's a unique identifier for a 
	 * document in a repository. */
	private final String docName;
	
	/* The name of the repository to which the document belongs. */
	private final String repoName;
	
	/* The content of the document. It is allowed to be modified. */
	private String content;
	
	/**
	 * Constructs a document object.
	 * @param docName The name of the document.
	 * @param content The content of the document.
	 * @param repoName The name of the repository to which the document
	 * belongs.
	 * @throws IllegalArgumentException if docName or repoName is null.
	 */
	public Document(String docName, String content, String repoName) {
		if (docName == null || repoName == null) {
			throw new IllegalArgumentException();
		}
		
		this.docName = docName;
		this.content = content;
		this.repoName = repoName;
	}
	
	/**
	 * Returns the name of the document.
	 * @return The name.
	 */
	public String getName() {
		return this.docName;
	}
	
	/**
	 * Returns the name of the repository to which the document belongs.
	 * @return The name of the repository.
	 */
	public String getRepoName(){
		return this.repoName;
	}

	/**
	 * Returns the content of the document.
	 * @return The content.
	 */
	public String getContent() {
		return this.content;
	}
	
	/**
	 * Sets the content of the document.
	 * @param content The content.
	 */
	public void setContent(String content) {
		this.content = content;	
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if (this.docName.equals(((Document)obj).getName())) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		String str = "Document name: " + this.docName + "\n"
				+ "---------------------------------------\n" 
				+ this.content;
		return str;
	}
}