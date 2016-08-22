///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program 5 - Social Network
// Files:            SocialNetworkingApp.java, SocialGraph.java,
//					 UndirectedGraph.java, GraphADT.java
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


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Social Networking application. Implements the simplified social networking
 * app via a command line interface, users can login, create/destroy friendships,
 * make queries about their social circle, and log out.
 * @author Lei Zhao, Lin Qi
 *
 */
public class SocialNetworkingApp {

    /**
     * Returns a social network as defined in the file 'filename'.
     * See assignment handout on the expected file format.
     * @param filename filename of file containing social connection data
     * @return The SocialGraph object initialized with input social info.
     * @throws FileNotFoundException if file does not exist
     */
    public static SocialGraph loadConnections(String filename) throws FileNotFoundException {
        if (filename == null) {
        	throw new IllegalArgumentException();
        }
        
        graph = new SocialGraph();
        
        File inFile = new File(filename);
        Scanner f = new Scanner(inFile);
        
        while (f.hasNext()) {
        	String nLine = f.nextLine();
        	String[] usrs = nLine.split(" ");
        	graph.addVertex(usrs[0]);
        	
        	for (int i = 1; i < usrs.length; i++) {
        		graph.addVertex(usrs[i]);
        		graph.addEdge(usrs[0], usrs[i]);
        	}
        }
        f.close();
        return graph;
    }

    static Scanner stdin = new Scanner(System.in);
    static SocialGraph graph;
    static String prompt = ">> ";  // Command prompt

    /**
     * Access main menu options to login or exit the application.
     * 
     * THIS METHOD HAS BEEN IMPLEMENTED FOR YOU.
     */
    public static void enterMainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.print(prompt);
            String[] tokens = stdin.nextLine().split(" ");
            String cmd = tokens[0];
            String person = (tokens.length > 1 ? tokens[1] : null);

            switch(cmd) {
                case "login":
                    System.out.println("Logged in as " + person);
                    enterSubMenu(person);
                    System.out.println("Logged out");
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }

    /**
     * Access submenu options to view the social network from the perspective
     * of currUser. Assumes currUser exists in the network.
     * @param currUser
     */
    public static void enterSubMenu(String currUser) {

        // Define the set of commands that have no arguments or one argument
        String commands =
                "friends fof logout print\n" +
                "connection friend unfriend";
        Set<String> noArgCmds = new HashSet<String>
                (Arrays.asList(commands.split("\n")[0].split(" ")));
        Set<String> oneArgCmds = new HashSet<String>
                (Arrays.asList(commands.split("\n")[1].split(" ")));

        boolean logout = false;
        while (!logout) {
            System.out.print(prompt);

            // Read user commands
            String[] tokens = stdin.nextLine().split(" ");
            String cmd = tokens[0];
            String otherPerson = (tokens.length > 1 ? tokens[1] : null);

            // Reject erroneous commands
            // You are free to do additional error checking of user input, but
            // this isn't necessary to receive a full grade.
            if (tokens.length == 0) continue;
            if (!noArgCmds.contains(cmd) && !oneArgCmds.contains(cmd)) {
                System.out.println("Invalid command");
                continue;
            }
            if (oneArgCmds.contains(cmd) && otherPerson == null) {
                System.out.println("Did not specify person");
                continue;
            }

            switch(cmd) {

            case "connection": {
            	List<String> path = graph.getPathBetween(currUser, otherPerson);
                if (path.size() <= 1) {
                	System.out.println("You are not connected to " + otherPerson);
                }
                else {
                	String printPath = "[";
                	for (String p : path) {
                		printPath += p;
                		printPath += ", ";
                	}
                	System.out.println(printPath.substring(0, printPath.length()-2) + "]");
                }
                break;
            }

            case "friends": {
                Set<String> friendsAt1 = graph.getNeighbors(currUser);
                if (friendsAt1.isEmpty()) {
                	System.out.println("You do not have any friends");
                }
                else {
                	ArrayList<String> friendsList = new ArrayList<String>(friendsAt1);
                	Collections.sort(friendsList);
                	String printFriends = "[";
                	for (String f : friendsList) {
                		printFriends += f;
                		printFriends += ", ";
                	}
                	System.out.println(printFriends.substring(0, printFriends.length()-2) + "]");
                }
                break;
            }

            case "fof": {
                Set<String> fof = graph.friendsOfFriends(currUser);
                if (fof.isEmpty()) {
                	System.out.println("You do not have any friends of friends");
                }
                else {
                	ArrayList<String> fofList = new ArrayList<String>(fof);
                	Collections.sort(fofList);
                	String printFoF = "[";
                	for (String ff : fofList) {
                		printFoF += ff;
                		printFoF += ", ";
                	}
                	System.out.println(printFoF.substring(0, printFoF.length()-2) + "]");
                }
                break;
            }

            case "friend": {
                if (graph.addEdge(currUser, otherPerson))
                	System.out.println("You are now friends with " + otherPerson);
                else
                	System.out.println("You are already friends with " + otherPerson);
                break;
            }

            case "unfriend": {
                if (graph.getNeighbors(currUser).contains(otherPerson)) {
                	graph.removeEdge(currUser, otherPerson);
                	System.out.println("You are no longer friends with " + otherPerson);
                }
                else
                	System.out.println("You are already not friends with " + otherPerson);
                break;
            }

            case "print": {
                // This command is left here for your debugging needs.
                // You may want to call graph.toString() or graph.pprint() here
            	// You are free to modify this or remove this command entirely.
            	//
                // YOU DO NOT NEED TO COMPLETE THIS COMMAND
                // THIS COMMAND WILL NOT BE PART OF GRADING
            	System.out.println(graph.pprint());
                break;
            }

            case "logout":
                logout = true;
                break;
            }  // End switch
        }
    }

    /**
     * Commandline interface for a social networking application.
     *
     * THIS METHOD HAS BEEN IMPLEMENTED FOR YOU.
     *
     * @param args
     */
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java SocialNetworkingApp <filename>");
            return;
        }
        try {
            graph = loadConnections(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        enterMainMenu();

    }

}