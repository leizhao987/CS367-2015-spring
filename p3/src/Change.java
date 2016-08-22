///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  VersionControlApp.java
// File:             Change.java
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
 * Represents a change (EDIT/ADD/DELETE) done to a particular document.
 * @author Lei Zhao, Lin Qi
 *
 */
public class Change {

	/* The changed document. */
	private Document doc;
	
	/* An enumeration denoting type of change: EDIT/ADD/DELETE. */
	public enum Type { ADD, DEL, EDIT };

	/* The type of change */
	private Type type;
	
	/**
	 * Constructs a change object.
	 * @param doc The changed document.
	 * @param type The change type.
	 * @throws IllegalArgumentException for any null arguments.
	 */
	public Change(Document doc, Type type) {
		
    	if (doc == null || type == null) {
			throw new IllegalArgumentException();
		}
    	
		this.doc = doc;
		this.type = type;
	}
	
	/**
	 * Returns the document contained in the change.
	 * @return The document.
	 */
	public Document getDoc() {
		return this.doc;
	}
	
	/**
	 * Returns the type of change.
	 * @return The change type.
	 */
	public Type getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		String str = "";
		str += "Doc name: " + this.doc.getName() + "\n";
		str += "Change Type: " + this.type;
		return str;
	}
}