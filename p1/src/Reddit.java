///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Reddit.java
// Files:            Reddit.java, Post.java, Karma.java, User.java, 
//		             RedditDB.java, PostType.java
// Semester:         CS302 Spring 2015
//
// Author:           Lei Zhao
// Email:            lzhao47@wisc.edu
// CS Login:         lzhao
// Lecturer's Name:  Jim Skrentny
// Lab Section:      none
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.*;
import java.io.*;

/**
 *  Reddit class creates and uses an RedditDB to represent and process 
 *  information. It read user and post information from text files and
 *  then processes user commands. 
 * 
 *  @author Lei Zhao
 */
public class Reddit {
	
	static RedditDB redditDB = new RedditDB();
	
	/**
	 * Call readFile to read data and define a set of commands to process
	 * the user data and post data.
	 * 
	 */
	public static void main(String[] args){
		redditDB.addUser("admin");
		fileRead(args);

		String current_user = "anon";
		System.out.print("[" + current_user + "@reddit]$");
		
		Scanner scn = new Scanner(System.in);
		String currentLine = scn.nextLine();
		if (currentLine.length() < 1) {
			System.out.println("Invalid command!");
			System.out.print("[" + current_user + "@reddit]$");
			currentLine = scn.nextLine();
		}
		while (currentLine != "x") {
			if (currentLine.equals("s")) {
				if (current_user.equals("admin")) {
					List<User> users = redditDB.getUsers();
					Iterator<User> user_itr = users.iterator();
					while (user_itr.hasNext()) {
						User nextUser = user_itr.next();
						System.out.println(nextUser.getName() + "    " 
						+ nextUser.getKarma().getLinkKarma() 
						+ "    " + nextUser.getKarma().getLinkKarma());
					}
				} else {
					System.out.println("Invalid command!");
				}
			} else if (currentLine.charAt(0) == 'd') {
				String username = currentLine.substring(2, currentLine.length());
				User logUser = redditDB.findUser(username);
				if (logUser == null) {
					System.out.println("User " + username + " not found.");
				} else {
					if (current_user.equals(("admin"))) {
						redditDB.delUser(username);
						System.out.println("User " + username + " deleted.");
					} else {
						System.out.println("Invalid command!");
					}
				}
			} else if (currentLine.charAt(0) == 'l' && currentLine.length() > 1) {
				String username = currentLine.substring(2, currentLine.length());
				if (current_user != "anon") {
					System.out.println("User " + current_user + " already logged in.");
				} else if (redditDB.findUser(username) == null) {
					System.out.println("User " + username + " not found.");
				} else {
					current_user = username;
					System.out.println("User " + username + " logged in.");
				}
			} else if (currentLine.charAt(0) == 'l') {
				if (current_user.equals("anon")) {
					System.out.println("No user logged in.");
				} else {
					System.out.println("User " + current_user + " logged out.");
					current_user = "anon";
				}
			} else if (currentLine.equals("f")) {
				System.out.println("Displaying the front page...");
				boolean exitsub = false;
				User logUser = redditDB.findUser(current_user);
				List<Post> postList = redditDB.getFrontpage(logUser); //start
				subMenu(exitsub, postList, current_user);
				System.out.println("Exiting to the main menu...");
				
			} else if (currentLine.charAt(0) == 'r') {
				String subName = currentLine.substring(2, currentLine.length());
				System.out.println("Displaying /r/" + subName + "...");
				boolean exitsub = false;
				User logUser = redditDB.findUser(current_user);
				List<Post> postList = redditDB.getFrontpage(logUser,subName); // start
				subMenu(exitsub, postList, current_user);
				System.out.println("Exiting to the main menu...");
				
			} else if (currentLine.charAt(0) == 'u') {
				String usname = currentLine.substring(2, currentLine.length());
				System.out.println("Displaying /u/" + usname + "...");
				boolean exitsub = false;
				User logUser = redditDB.findUser(current_user);
				if (logUser == null) {
					System.out.println("User " + usname + " not found.");
				} else {
					List<Post> postList = logUser.getPosted();
					subMenu(exitsub, postList, current_user);
					System.out.println("Exiting to the main menu...");
				}
				
			} else {
				System.out.println("Invalid command!");
				//System.out.println(currentLine);
				//System.out.println(current_user);
			}
			System.out.print("[" + current_user + "@reddit]$");
			currentLine = scn.nextLine();
		}
		scn.close();
		System.out.println("Exiting to the real world...");
	}
	
	public static void subMenu(boolean exitsub, List<Post> postList, String current_user) {
		Iterator<Post> post_itr = postList.iterator();
		if (post_itr.hasNext()) {
			Post fp_post = post_itr.next();
			System.out.println(fp_post.getKarma() + "    " + fp_post.getTitle());
			System.out.print("[" + current_user + "@reddit]$");
		} else {
			System.out.println("No posts left to display.");
			exitsub = true;
		}
		
		Scanner sub_console = new Scanner(System.in);
		String subLine = sub_console.nextLine();
		while ((exitsub == false) && (subLine != "x")) {
			switch (subLine) {
				case "a":
					if (current_user.equals("anon")) {
						System.out.println("Login to like post.");
					} else {
						Iterator<Post> like_itr = post_itr; //start
						 /*while (like_itr.hasPrevious()) {
							Post pre_post = like_itr.previous();
							pre_post.upvote();
						}*/
						if (post_itr.hasNext()) {
							Post fp_post = post_itr.next();
							System.out.println(fp_post.getKarma() + "    " + fp_post.getTitle());
						} else {
							System.out.println("No posts left to display.");
							exitsub = true;
						}
					}
					break;
				case "z":
					if (current_user.equals("anon")) {
						System.out.println("Login to dislike post.");
					} else {
						Iterator<Post> dislike_itr = post_itr;
						 /*while (dislike_itr.hasPrevious()) {
							Post pre_post = dislike_itr.previous();
							pre_post.downvote();
						} */
						if (post_itr.hasNext()) {
							Post fp_post = post_itr.next();
							System.out.println(fp_post.getKarma() + "    " + fp_post.getTitle());
						} else {
							System.out.println("No posts left to display.");
							exitsub = true;
						}
					}
					break;
				case "j":
					if (post_itr.hasNext()) {
						Post fp_post = post_itr.next();
						System.out.println(fp_post.getKarma() + "    " + fp_post.getTitle());
					} else {
						System.out.println("No posts left to display.");
						exitsub = true;
					}
					break;
				case "x":
					exitsub = true;
					break;
				default:
					System.out.println("Invalid command!");
					ListIterator<Post> sub_itr = postList.listIterator();
					if (post_itr.hasNext()) {
						Post sub_post = sub_itr.next();
						System.out.println(sub_post.getKarma() + "    " + sub_post.getTitle());
					}
					break;
			}
			if (exitsub == false) {
				System.out.print("[" + current_user + "@reddit]$");
			} else {
				break;
			}
			subLine = sub_console.nextLine();
		}
	}
	
	/**
	 * Read data from text file
	 * 
	 */
	public static void fileRead(String[] args){
		if (args.length == 0) {
			System.out.println("Usage: java Reddit <FileNames>");
			System.exit(0);
		}

		for (int i = 0; i < args.length; i ++) {
			String[] parts = args[i].split(".tx");
			User nextUser = redditDB.addUser(parts[0]);

			try {
				File srcFile = new File(args[i]);
				Scanner fileIn = new Scanner(srcFile);

				String inputLine = fileIn.nextLine();
				String[] subs = inputLine.split(", ");
				for (int j = 0; j < subs.length; j ++) {
					nextUser.subscribe(subs[j]);
				}

				while (fileIn.hasNext()) {
					String nextline = fileIn.nextLine();
					String[] post_strs = nextline.split(", +", 3);
					PostType type = PostType.SELF;
					switch (post_strs[1]) {
						case  "SELF":
							type = PostType.SELF;
							break;
						case  "LINK":
							type = PostType.LINK;
							break;
						case  "COMMENT":
							type = PostType.COMMENT;
							break;
					}
					nextUser.addPost(post_strs[0], type, post_strs[2]);
					// automatically upvote oneself's post
				}

			} catch (FileNotFoundException e) {
				System.out.println("File " + args[i] + " not found");
			}
			// fileIn.close();
		}

	}
}
