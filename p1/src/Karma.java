///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Reddit.java
// File:             Karma.java
// Semester:         CS302 Spring 2015
//
// Author:           Lei Zhao, lzhao47@wisc.edu
// CS Login:         lzhao
// Lecturer's Name:  Jim Skrentny
// Lab Section:      none
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 *  Karma class represents the points accrued by a single user. It has two 
 *  subtypes: linkKarma and commentKarma, relating to LINK and COMENT 
 *  PostType respectively.
 * 
 *  @author Lei Zhao
 */
public class Karma {
	private int linkKarma;
	private int commentKarma;

	/**
	 * Creates an instance of the Karma class with link and comment karmas 
	 * initialized to zero.
	 */
	public Karma() {
		this.linkKarma = 0;
		this.commentKarma = 0;
	}

	/**
	 * Increases the karma of this type by two for the current instance.
	 * 
	 * @param type Type of the post.
	 */
	public void upvote(PostType type) {
		switch (type) {
			case SELF:
				break;
			case LINK:
				this.linkKarma += 2;
				break;
			case COMMENT:
				this.commentKarma += 2;
				break;
		}
	}

	/**
	 * Decreases the karma of this type by one for the current instance.
	 * 
	 * @param type Type of the post.
	 */
	public void downvote(PostType type) {
		switch (type) {
			case SELF:
				break;
			case LINK:
				this.linkKarma -= 1;
				break;
			case COMMENT:
				this.commentKarma -= 1;
				break;
		}
	}

	/**
	 * Returns the linkKarma associated with the current instance.
	 * 
	 * @return The linkKarma with current instance. 
	 */
	public int getLinkKarma() {
		return this.linkKarma;
	}

	/**
	 * Returns the commentKarma associated with the current instance.
	 * 
	 * @return The commentKarma associated with the current instance.
	 */
	public int getCommentKarma() {
		return this.commentKarma;
	}
}