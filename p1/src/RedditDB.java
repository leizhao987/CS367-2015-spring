///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  Reddit.java
// File:             RedditDB.java
// Semester:         CS302 Spring 2015
//
// Author:           Lei Zhao, lzhao47@wisc.edu
// CS Login:         lzhao
// Lecturer's Name:  Jim Skrentny
// Lab Section:      none
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *  RedditDB class stores a List of all the user information
 * 
 *  @author Lei Zhao
 */
public class RedditDB {
	private List<User> users;

	public RedditDB() {
		this.users = new ArrayList<User>();
	}

	public List<User> getUsers() {
		List<User> copy_user = new ArrayList<User>(users);
		return copy_user;
	}

	public User addUser(String name) {
		if (findUser(name) == null) {
			User new_user = new User(name);
			users.add(new_user);
			return new_user;
		} else {
			return null;
		}
	}

	public User findUser(String name) {
		ListIterator<User> itr = users.listIterator();
		while (itr.hasNext()) {
			User nextUser = itr.next();
			if (nextUser.getName().equals(name)) {
				return nextUser;
			}
		}
		return null;
	}

	public boolean delUser(String name) {
		User foundUser = findUser(name);
		if (foundUser == null) {
			return false;
		} else {
			List<Post> delPost = foundUser.getPosted();
			ListIterator<Post> post_itr = delPost.listIterator();
			ListIterator<User> user_itr = users.listIterator();
			
			while (user_itr.hasNext()) {
				User nextUser = user_itr.next();
				while (post_itr.hasNext()) {
					Post nextPost = post_itr.next();
					nextUser.undoLike(nextPost);
					nextUser.undoDislike(nextPost);
				}
				
				List<Post> userPost = nextUser.getPosted();
				ListIterator<Post> p_itr = userPost.listIterator();
				while (p_itr.hasNext()) {
					Post p_post = p_itr.next();
					foundUser.undoLike(p_post);
					foundUser.undoDislike(p_post);
				}
			}
			users.remove(foundUser);
			return true;
		}
	}

	public List<Post> getFrontpage(User user) {
		List<Post> postList = new ArrayList<Post>();
		if (user == null) {
			ListIterator<User> user_itr = users.listIterator();
			while (user_itr.hasNext()) {
				User nextUser = user_itr.next();
				List<Post> userPost = nextUser.getPosted();
				postList.addAll(userPost);
			}
			return postList;
		} else {
			List<String> subscribed = user.getSubscribed();
			ListIterator<String> sub_itr = subscribed.listIterator();
			while (sub_itr.hasNext()) {
				String sub = sub_itr.next();
				postList.addAll(this.getFrontpage(user, sub));
			}
			return postList;
		}
	}

	public List<Post> getFrontpage(User user, String subreddit) {
		List<Post> postList = new ArrayList<Post>();
		if (user == null) {
			List<Post> posts = this.getFrontpage(null);
			ListIterator<Post> post_itr = posts.listIterator();
			while (post_itr.hasNext()) {
				Post nextPost = post_itr.next();
				if (nextPost.getSubreddit().equals(subreddit)) {
					postList.add(nextPost);
				}
			}
			return postList;

		} else {
			List<Post> liked = user.getLiked();
			List<Post> disliked = user.getDisliked();
			
			List<Post> posts = this.getFrontpage(null, subreddit);
			ListIterator<Post> post_itr = posts.listIterator();
			while (post_itr.hasNext()) {
				Post nextPost = post_itr.next();

				if (nextPost.getUser().equals(user)) {
					postList.add(nextPost);
				} else if (!(liked.contains(nextPost)) 
						&& !(disliked.contains(nextPost))) {
					postList.add(nextPost);
				}
			}
			return postList;
		}
	}
}