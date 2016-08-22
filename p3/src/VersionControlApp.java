///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program 3 - Version Control System
// Files:            VersionControlApp.java, VersionControlDb.java, User.java,
//					 Repo.java, RepoCopy.java, Change.java, ChangeSet.java, 
//                   StackADT.java, QueueADT.java, EmptyStackException.java,
//                   EmptyQueueException.java, Document.java, ErrorType.java,
//                   SimpleStack.java, SimpleQueue.java
// Semester:         CS367 Spring 2015
//
// Author:           Lei Zhao
// Email:            lzhao47@wisc.edu
// CS Login:         lzhao
// Lecturer's Name:  Jim SKrentny
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     Lin Qi
// Email:            lqi3@wisc.edu
// CS Login:         lqi
// Lecturer's Name:  Jim SKrentny 
///////////////////////////////////////////////////////////////////////////////

import java.util.Scanner;

/**
 * Version control application. Implements the command line utility
 * for Version control.
 * @author Lei Zhao, Lin Qi
 *
 */
public class VersionControlApp {

	/* Scanner object on input stream. */
	private static final Scanner scnr = new Scanner(System.in);

	/**
	 * An enumeration of all possible commands for Version control system.
	 */
	private enum Cmd {
		AU, DU,	LI, QU, AR, DR, OR, LR, LO, SU, CO, CI, RC, VH, RE, LD, AD,
		ED, DD, VD, HE, UN
	}

	/**
	 * Displays the main menu help. 
	 */
	private static void displayMainMenu() {
		System.out.println("\t Main Menu Help \n" 
				+ "====================================\n"
				+ "au <username> : Registers as a new user \n"
				+ "du <username> : De-registers a existing user \n"
				+ "li <username> : To login \n"
				+ "qu : To exit \n"
				+"====================================\n");
	}

	/**
	 * Displays the user menu help. 
	 */
	private static void displayUserMenu() {
		System.out.println("\t User Menu Help \n" 
				+ "====================================\n"
				+ "ar <reponame> : To add a new repo \n"
				+ "dr <reponame> : To delete a repo \n"
				+ "or <reponame> : To open repo \n"
				+ "lr : To list repo \n"
				+ "lo : To logout \n"
				+ "====================================\n");
	}

	/**
	 * Displays the repo menu help. 
	 */
	private static void displayRepoMenu() {
		System.out.println("\t Repo Menu Help \n" 
				+ "====================================\n"
				+ "su <username> : To subcribe users to repo \n"
				+ "ci: To check in changes \n"
				+ "co: To check out changes \n"
				+ "rc: To review change \n"
				+ "vh: To get revision history \n"
				+ "re: To revert to previous version \n"
				+ "ld : To list documents \n"
				+ "ed <docname>: To edit doc \n"
				+ "ad <docname>: To add doc \n"
				+ "dd <docname>: To delete doc \n"
				+ "vd <docname>: To view doc \n"
				+ "qu : To quit \n" 
				+ "====================================\n");
	}

	/**
	 * Displays the user prompt for command.  
	 * @param prompt The prompt to be displayed.
	 * @return The user entered command (Max: 2 words).
	 */
	private static String[] prompt(String prompt) {
		System.out.print(prompt);
		String line = scnr.nextLine();
		String []words = line.trim().split(" ", 2);
		return words;
	}

	/**
	 * Displays the prompt for file content.  
	 * @param prompt The prompt to be displayed.
	 * @return The user entered content.
	 */
	private static String promptFileContent(String prompt) {
		System.out.println(prompt);
		String line = null;
		String content = "";
		while (!(line = scnr.nextLine()).equals("q")) {
			content += line + "\n";
		}
		return content;
	}

	/**
	 * Validates if the input has exactly 2 elements. 
	 * @param input The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput2(String[] input) {
		if (input.length != 2) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}

	/**
	 * Validates if the input has exactly 1 element. 
	 * @param input The user input.
	 * @return True, if the input is valid, false otherwise.
	 */
	private static boolean validateInput1(String[] input) {
		if (input.length != 1) {
			System.out.println(ErrorType.UNKNOWN_COMMAND);
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the Cmd equivalent for a string command. 
	 * @param strCmd The string command.
	 * @return The Cmd equivalent.
	 */
	private static Cmd stringToCmd(String strCmd) {
		try {
			return Cmd.valueOf(strCmd.toUpperCase().trim());
		}
		catch (IllegalArgumentException e){
			return Cmd.UN;
		}
	}

	/**
	 * Handles add user. Checks if a user with name "username" already exists; 
	 * if exists the user is not registered. 
	 * @param username The user name.
	 * @return USER_ALREADY_EXISTS if the user already exists, 
	 * SUCCESS otherwise.
	 */
	private static ErrorType handleAddUser(String username) {
		if (VersionControlDb.addUser(username) != null) {
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.USERNAME_ALREADY_EXISTS;
		}
	}

	/**
	 * Handles delete user. Checks if a user with name "username" exists; if 
	 * does not exist nothing is done. 
	 * @param username The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleDelUser(String username) {
		User user = VersionControlDb.findUser(username); 
		if (user == null) {
			return ErrorType.USER_NOT_FOUND;
		}
		else {
			VersionControlDb.delUser(user);
			return ErrorType.SUCCESS;
		}
	}

	/**
	 * Handles a user login. Checks if a user with name "username" exists; 
	 * if does not exist nothing is done; else the user is taken to the 
	 * user menu. 
	 * @param username The user name.
	 * @return USER_NOT_FOUND if the user does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleLogin(String username) {
		User currUser = VersionControlDb.findUser(username);
		if (currUser != null) {
			System.out.println(ErrorType.SUCCESS);
			processUserMenu(currUser);
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.USER_NOT_FOUND;
		}
	}

	/**
	 * Handles add repo. Checks if a repo with name "repoName" already exists; 
	 * if exists the repo is not created. 
	 * @param repoName The name of the repository to be added. 
	 * @param user The user who is creating the repository.
	 * @return REPONAME_ALREADY_EXISTS if the repo already exists, 
	 * SUCCESS otherwise.
	 */
	private static ErrorType handleAddRepo(String repoName, User user) {
		if (VersionControlDb.addRepo(repoName, user) != null) {
			user.subscribeRepo(repoName);
			return ErrorType.SUCCESS;
		}
		else {
			return ErrorType.REPONAME_ALREADY_EXISTS;
		}
	}
	
	/**
	 * Handles delete repo. Checks if a repo with name "repoName" exists; if 
	 * it exists and the logged-in user is the admin, then delete the repo.
	 * Otherwise nothing is done. 
	 * @param repoName The repo name.
	 * @param user The logged-in user
	 * @return REPO_NOT_FOUND if the user does not exists, ACCESS_DENIED if
	 * the logged-in user is not the admin, SUCCESS otherwise.
	 */
	private static ErrorType handleDelRepo(String repoName, User user) {
		Repo r = VersionControlDb.findRepo(repoName);
		if (r == null) {
			return ErrorType.REPO_NOT_FOUND;
		}
		else if (!(r.getAdmin()).equals(user)) {
			return ErrorType.ACCESS_DENIED;
		}
		else {
			VersionControlDb.delRepo(r);
			return ErrorType.SUCCESS;
		}
	}
	

	
	/**
	 * Processes the main menu commands.
	 * 
	 */
	public static void processMainMenu() {

		String mainPrompt = "[anon@root]: ";
		boolean execute = true;

		while (execute) {
			String[] words = prompt(mainPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case AU:
				if (validateInput2(words)) {
					System.out.println(handleAddUser(words[1].trim()));
				}
				break;
			case DU:
				if (validateInput2(words)) {
					System.out.println(handleDelUser(words[1].trim())); 
				}
				break;
			case LI:
				if (validateInput2(words)) {
					System.out.println(handleLogin(words[1].trim()));
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayMainMenu();
				}
				break;
			case QU:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	
	/**
	 * Processes the user menu commands for a logged in user.
	 * @param logInUser The logged in user.
	 * @throws IllegalArgumentException in case any argument is null.
	 */
	public static void processUserMenu(User logInUser) {

		if (logInUser == null) {
			throw new IllegalArgumentException();
		}

		String userPrompt = "[" + logInUser.getName() + "@root" + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(userPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case AR:
				if (validateInput2(words)) {
					System.out.println(handleAddRepo(words[1].trim(), 
							logInUser));
				}
				break;
			case DR:
				if (validateInput2(words)) {
					System.out.println(handleDelRepo(words[1].trim(), 
							logInUser));
				}
				break;
			case LR:
				if (validateInput1(words)) {
					System.out.println(logInUser.toString());
				}
				break;
			case OR:
				if (validateInput2(words)) {
					ErrorType type = handleOpenRepo(words[1].trim(), logInUser);
					System.out.println(type);
					if (type.equals(ErrorType.SUCCESS)) {
						processRepoMenu(logInUser, words[1].trim());
						System.out.println(ErrorType.SUCCESS);
					}
				}
				break;
			case LO:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayUserMenu();
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	
	/**
	 * Handles open repo. Checks if a repo with name "repoName" exists; if 
	 * it exists and logged-in user subscribes to the repo, then open it.
	 * Otherwise nothing is done.
	 * @param repoName The repo name.
	 * @param user The logged-in user
	 * @return REPO_NOT_FOUND if the user does not exists, REPO_NOT_SUBSCRIBED
	 * if the logged-in user does not subscribes to it, SUCCESS otherwise.
	 */
	private static ErrorType handleOpenRepo(String repoName, User user) {
		Repo r = VersionControlDb.findRepo(repoName);
		if (r == null) {
			return ErrorType.REPO_NOT_FOUND;
		}
		else if (!user.isSubRepo(repoName)) {
			return ErrorType.REPO_NOT_SUBSCRIBED;
		}
		else {
			if (user.getWorkingCopy(repoName) == null) {
				user.checkOut(repoName);
			}
			return ErrorType.SUCCESS;
		}
	}
	
	
	/**
	 * Handles subscribe user to a repo. Checks if a user with name "userName"
	 * exists; if it exists and logged-in user is the admin of the repo, then
	 * subscribe the user with name "userName" to this repo. Otherwise nothing
	 * is done.
	 * @param userName The user name to be subscribed
	 * @param user The logged-in user
	 * @param repoName The repo name.
	 * @return REPO_NOT_FOUND if the user does not exists, ACCESS_DENIED if
	 * logged-in user is not admin of the repo. SUCCESS otherwise.
	 */
	private static ErrorType handleSubUser(String userName, 
			                               User user, String repoName) {
		if (VersionControlDb.findUser(userName) == null) {
			return ErrorType.USER_NOT_FOUND;
		}
		else if (!(VersionControlDb.findRepo(repoName).getAdmin()).
				equals(user)) {
			return ErrorType.ACCESS_DENIED;
		}
		else {
			VersionControlDb.findUser(userName).subscribeRepo(repoName);
			return ErrorType.SUCCESS;
		}
	}
	
	/**
	 * Handles edit document. Checks if a doc with name "docName" exits in the
	 * logged-in user's local working copy of the repo; if it exists, prompts
	 * the user to enter the new content for the doc, sets the doc's content and
	 * add the changes to the users's pending checkin. Otherwise nothing is done.
	 * @param docName The doc name
	 * @param user The logged-in user
	 * @param repoName The repo name.
	 * @return DOC_NOT_FOUND if the doc does not exists, SUCCESS otherwise.
	 */
	private static ErrorType handleEditDoc(String docName, 
			                               User user, String repoName) {
		Document doc = user.getWorkingCopy(repoName).getDoc(docName);
		if (doc == null) {
			return ErrorType.DOC_NOT_FOUND;
		}
		else {
			String docPrompt = "Enter the file content and press q to quit: ";
			String docContent = promptFileContent(docPrompt);
			doc.setContent(docContent);
			user.addToPendingCheckIn(doc, Change.Type.EDIT, repoName);
			return ErrorType.SUCCESS;
		}
	}
	
	/**
	 * Handles add document. Checks if a doc with name "docName" exits in the
	 * logged-in user's local working copy of the repo; if it does not exist,
	 * prompts the user to enter the content for the doc, create a new doc and
	 * add it to the working copy and add the changes to the pending checkin. 
	 * Otherwise nothing is done.
	 * @param docName The doc name
	 * @param user The logged-in user
	 * @param repoName The repo name.
	 * @return DOCNAME_ALREADY_EXISTS if the doc exists, SUCCESS otherwise.
	 */
	private static ErrorType handleAddDoc(String docName, 
			                              User user, String repoName) {
		if (user.getWorkingCopy(repoName).getDoc(docName) != null) {
			return ErrorType.DOCNAME_ALREADY_EXISTS;
		}
		else {
			String docPrompt = "Enter the file content and press q to quit: ";
			String docContent = promptFileContent(docPrompt);
			Document doc = new Document(docName, docContent, repoName);
			user.getWorkingCopy(repoName).addDoc(doc);
			user.addToPendingCheckIn(doc, Change.Type.ADD, repoName);
			return ErrorType.SUCCESS;
		}
		
	}
	
	/**
	 * Handles delete document. Checks if a doc with name "docName" exits in the
	 * logged-in user's local working copy of the repo; if it exists, delete the
	 * doc from the working copy and add the changes to the pending checkins.
	 * Otherwise nothing is done.
	 * @param docName The doc name
	 * @param user The logged-in user
	 * @param repoName The repo name.
	 * @return DOC_NOT_FOUND if the doc does not exist, SUCCESS otherwise.
	 */
	private static ErrorType handleDelDoc(String docName, 
			                              User user, String repoName) {
		Document doc = user.getWorkingCopy(repoName).getDoc(docName);
		if (doc == null) {
			return ErrorType.DOC_NOT_FOUND;
		}
		else {
			user.getWorkingCopy(repoName).delDoc(doc);
			user.addToPendingCheckIn(doc, Change.Type.DEL, repoName);
			return ErrorType.SUCCESS;
		}
	}
	
	/**
	 * Handles display document. Checks if a doc with name "docName" exits in the
	 * logged-in user's local working copy of the repo; if it does not exist, 
	 * nothing is done.
	 * @param docName The doc name
	 * @param user The logged-in user
	 * @param repoName The repo name.
	 * @return DOC_NOT_FOUND if the doc does not exist, SUCCESS otherwise.
	 */
	private static void handleDisplayDoc(String docName, 
			                             User user, String repoName) {
		Document doc = user.getWorkingCopy(repoName).getDoc(docName);
		if (doc == null) {
			System.out.println(ErrorType.DOC_NOT_FOUND);
		}
		else {
			System.out.println(doc.toString());
		}
	}
	
	/**
	 * Handles review document. Checks if there are any pending checkins in
	 * the repo and if the logged-in user is the admin of the repo; if there
	 * are no pending checkins or the logged-in user is not the admin, nothing
	 * is done.
	 * @param user The logged-in user
	 * @param repoName The repo name.
	 * @return NO_PENDING_CHECKINS if there are no pending checkins for repo,
	 * ACCESS_DENIED if logged-in user is not the admin, SUCCESS otherwise.
	 */
	private static ErrorType handleRevCheckin(User user, String repoName) {
		Repo r = VersionControlDb.findRepo(repoName);
		if (r.getCheckInCount() == 0) {
			return ErrorType.NO_PENDING_CHECKINS;
		}
		else if (!(VersionControlDb.findRepo(repoName).getAdmin()).
				equals(user)) {
			return ErrorType.ACCESS_DENIED;
		}
		else {
			ChangeSet nextCheckIn = r.getNextCheckIn(user);
			while (nextCheckIn != null) {
				System.out.println(nextCheckIn.toString());
				System.out.print("Approve changes? Press y to accept: ");
				if (scnr.nextLine().trim().equals("y")) {
					r.approveCheckIn(user, nextCheckIn);
				}
				nextCheckIn = r.getNextCheckIn(user);
			}
			return ErrorType.SUCCESS;
		}
	}
	
	/**
	 * Process the repo menu commands for a logged in user and current
	 * working repository.
	 * @param logInUser The logged in user. 
	 * @param currRepo The current working repo.
	 * @throws IllegalArgumentException in case any argument is null.
	 */
	public static void processRepoMenu(User logInUser, String currRepo) {

		if (logInUser  == null || currRepo == null) {
			throw new IllegalArgumentException();
		}

		String repoPrompt = "["+ logInUser.getName() + "@" + currRepo + "]: ";
		boolean execute = true;

		while (execute) {

			String[] words = prompt(repoPrompt);
			Cmd cmd = stringToCmd(words[0]);

			switch (cmd) {
			case SU:
				if (validateInput2(words)) {
					System.out.println(handleSubUser(words[1].trim(), 
							logInUser, currRepo));
				}
				break;
			case LD:
				if (validateInput1(words)) {
					System.out.println(logInUser.getWorkingCopy(currRepo).toString());
				}
				break;
			case ED:
				if (validateInput2(words)) {
					System.out.println(handleEditDoc(words[1].trim(), 
							logInUser, currRepo));
				}					
				break;
			case AD:
				if (validateInput2(words)) {
					System.out.println(handleAddDoc(words[1].trim(), 
							logInUser, currRepo));
				}
				break;
			case DD:
				if (validateInput2(words)) {
					System.out.println(handleDelDoc(words[1].trim(), 
							logInUser, currRepo));
				}
				break;
			case VD:
				if (validateInput2(words)) {
					handleDisplayDoc(words[1].trim(), logInUser, currRepo);
				}
				break;
			case CI:
				if (validateInput1(words)) {
					System.out.println(logInUser.checkIn(currRepo));
				}
				break;
			case CO:
				if (validateInput1(words)) {
					System.out.println(logInUser.checkOut(currRepo));
				}
				break;
			case RC:
				if (validateInput1(words)) {
					System.out.println(handleRevCheckin(logInUser, currRepo));
				}
				break;
			case VH:
				if (validateInput1(words)) {
					System.out.println(VersionControlDb.findRepo(currRepo).getVersionHistory());
				}
				break;
			case RE:	
				if (validateInput1(words)) {
					System.out.println(VersionControlDb.findRepo(currRepo).revert(logInUser));
				}
				break;
			case HE:
				if (validateInput1(words)) {
					displayRepoMenu();
				}
				break;
			case QU:
				if (validateInput1(words)) {
					execute = false;
				}
				break;
			default:
				System.out.println(ErrorType.UNKNOWN_COMMAND);
			}

		}
	}

	/**
	 * The main method. Simulation starts here.
	 * @param args Unused
	 */
	public static void main(String []args) {
		try {
			processMainMenu(); 
		}
		// Any exception thrown by the simulation is caught here.
		catch (Exception e) {
			System.out.println(ErrorType.INTERNAL_ERROR);
			// Uncomment this to print the stack trace for debugging purpose.
			// e.printStackTrace();
		}
		// Any clean up code goes here.
		finally {
			System.out.println("Quitting the simulation.");
		}
	}
}