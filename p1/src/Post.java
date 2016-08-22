///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Reddit.java
// File:             Post.java
// Semester:         CS302 Spring 2015
//
// Author:           Lei Zhao, lzhao47@wisc.edu
// CS Login:         lzhao
// Lecturer's Name:  Jim Skrentny
// Lab Section:      none
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 * Post class represents a single post that keeps track of the user who posted 
 * it, the subreddit it was posted to, the type of the post (of type PostType,
 * also provided), the title of the post and the karma (points) received by it. 
 * 
 *  @author Lei Zhao
 */
public class Post {
	final private User user;
	final private String subreddit;
	final private PostType type;
	final private String title;
	private int karma;

	/**
	 * Constructs a post with the specified attributes as applicable. 
	 * A newly created post should be assigned a karma of zero.
	 * 
	 * @param user The user of this post.
	 * @param subreddit The subreddit this post is posted to.
	 * @param type The type of this post.
	 * @param title The title of this post.
	 * 
	 * @return The post object.
	 */
	public Post(User user, String subreddit, PostType type, String title) {
		this.user = user;
		this.subreddit = subreddit;
		this.type = type;
		this.title = title;
		this.karma = 0;
	}
	
	/**
	 * Increases the karma of this post and the relevant karma of the user 
	 * who created the post by two each.
	 */
	public void upvote() {
		karma += 2;
		this.user.getKarma().upvote(this.type);
	}

	/**
	 * Decreases the karma of this post and the relevant karma of the user 
	 * who created the post by one each.
	 */
	public void downvote() {
		karma -= 1;
		this.user.getKarma().downvote(this.type);
	}

	/**
	 * Returns the user who created this post.
	 * 
	 * @return The user object of this post.
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Returns the subreddit this was posted to.
	 * 
	 * @return The subreddit this post is posted to.
	 */
	public String getSubreddit() {
		return this.subreddit;
	}

	/**
	 * Returns the type of the post.
	 * 
	 * @return This post type.
	 */
	public PostType getType() {
		return this.type;
	}

	/**
	 * Returns the title of the post.
	 * 
	 * @return The post title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns the karma aggregated by the post.
	 * 
	 * @return The post karma.
	 */
	public int getKarma() {
		return this.karma;
	}
}