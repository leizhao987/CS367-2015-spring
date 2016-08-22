///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Reddit.java
// File:             User.java
// Semester:         CS302 Spring 2015
//
// Author:           Lei Zhao, lzhao47@wisc.edu
// CS Login:         lzhao
// Lecturer's Name:  Jim Skrentny
// Lab Section:      none
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.List;
import java.util.ArrayList;

/**
 *  User class represents the data associated with a single user.
 * 
 *  @author Lei Zhao
 */
public class User {
	final private String name;
	final private Karma karma;
	private List<String> subscribed;
	private List<Post> posted;
	private List<Post> liked;
	private List<Post> disliked;

	/**
	 * Creates an instance of the User class with the specified name and a 
	 * newly initialized instance of Karma. The Lists will be initialized 
	 * to new ArrayLists of the relevant types as applicable.
	 * 
	 * @param name User name.
	 */
	public User(String name) {
		//TODO
		this.name = name;
		karma = new Karma();
		subscribed = new ArrayList<String>();
		posted = new ArrayList<Post>();
		liked = new ArrayList<Post>();
		disliked = new ArrayList<Post>();
	}

	/**
	 * Return the name of the current user.
	 * 
	 * @return User name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the karma of the current user.
	 * 
	 * @return The karma of the current user.
	 */
	public Karma getKarma() {
		return this.karma;
	}

	/**
	 * Returns a copy of the list of subreddits the user is subscribed to;
	 * the list itself should not be exposed.
	 * 
	 * @return A copy of the list of subreddits the user is subscribed to
	 */
	public List<String> getSubscribed() {
		List<String> copy_list = new ArrayList<String>(this.subscribed);
		return copy_list;
	}

	/**
	 * Returns a copy of the list of posts posted by the user; the list 
	 * itself should not be exposed.
	 * 
	 * @return A copy of the list of posts posted by the user.
	 */
	public List<Post> getPosted() {
		List<Post> copy_list = new ArrayList<Post>(this.posted);
		return copy_list;
	}

	/**
	 * Returns a copy of the list of posts liked by the user; the list itself
	 * should not be exposed.
	 * 
	 * @return a copy of the list of posts liked by the use.
	 */
	public List<Post> getLiked() {
		List<Post> copy_list = new ArrayList<Post>(this.liked);
		return copy_list;
	}

	/**
	 * Returns a copy of the list of posts disliked by the user; 
	 * the list itself should not be exposed.
	 * 
	 * @return a copy of the list of posts liked by the use.
	 */
	public List<Post> getDisliked() {
		List<Post> copy_list = new ArrayList<Post>(this.disliked);
		return copy_list;
	}

	/**
	 * Add the specified subreddit to the List of subscribed subreddits 
	 * if the user is not already subscribed. If the user is already 
	 * subscribed, unsubscribe from the subreddit.
	 * 
	 * @return a copy of the list of posts liked by the use.
	 */
	public void subscribe(String subreddit) {
		if (subscribed.contains(subreddit)) {
			this.unsubscribe(subreddit);
		} else {
			subscribed.add(subreddit);
		}
	}

	/**
	 * Remove the specified subreddit from the List of subscribed subreddits 
	 * if present; if not, do nothing.
	 * 
	 * @return a copy of the list of posts liked by the use.
	 */
	public void unsubscribe(String subreddit) {
		if (subscribed.contains(subreddit)) {
			subscribed.remove(subreddit);
		}
	}

	/**
	 * Instantiate a new post with the appropriate parameters and add it to 
	 * the list of posts posted by the user.
	 * 
	 * @return a copy of the list of posts liked by the use.
	 */
	public Post addPost(String subreddit, PostType type, String title) {
		Post new_post = new Post(this, subreddit, type, title);
		this.like(new_post);
		posted.add(new_post);
		return new_post;
	}

	/**
	 * Upvote the post and add it to the List of liked posts if not already
	 * liked; else undo the like.
	 */
	public void like(Post post) {
		if (disliked.contains(post)) {
			undoDislike(post);
		} else if (liked.contains(post)) {
			undoLike(post);
		} else {
			post.upvote();
			liked.add(post);
		}
	}

	/**
	 * Remove the post from the list of liked posts and update its karma
	 * appropriately.
	 */
	public void undoLike(Post post) {
		if (liked.contains(post)) {
			liked.remove(post);
			post.downvote();
			post.downvote();
		}
		
	}

	/**
	 * Downvote the post and add it to the List of disliked posts if not already 
	 * disliked; else undo the dislike.
	 */
	public void dislike(Post post) {
		if (liked.contains(post)) {
			undoLike(post);
		} else if (disliked.contains(post)) {
			undoDislike(post);
		} else {
			post.downvote();
			disliked.add(post);
		}
	}

	/**
	 * Remove the post from the list of disliked posts and update its karma appropriately.
	 */
	public void undoDislike(Post post) {
		if (disliked.contains(post)) {
			disliked.remove(post);
			post.upvote();
			post.downvote();
		}
	}
}