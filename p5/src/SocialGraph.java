///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  SocialNetworkingApp.java
// File:             SocialGraph.java
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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * This class represents a simplified social network. It extends 
 * UndirectedGraph<String> with methods specific to social networking.
 * @author Lei Zhao, Lin Qi
 *
 */
public class SocialGraph extends UndirectedGraph<String> {
	
    /**
     * Creates an empty social graph.
     * 
     * DO NOT MODIFY THIS CONSTRUCTOR.
     */
    public SocialGraph() {
        super();
    }

    /**
     * Creates a graph from a preconstructed hashmap.
     * This method will be used to test your submissions. You will not find this
     * in a regular ADT.
     * 
     * DO NOT MODIFY THIS CONSTRUCTOR.
     * DO NOT CALL THIS CONSTRUCTOR ANYWHERE IN YOUR SUBMISSION.
     * 
     * @param hashmap adjacency lists representation of a graph that has no
     * loops and is not a multigraph.
     */
    public SocialGraph(HashMap<String, ArrayList<String>> hashmap) {
        super(hashmap);
    }
    
    /**
     * Uses modified Breadth First Search to examine the vertices at depth N 
     * and track the shortest path to each depth.
     * @param pFrom The person from which the BFS starts.
     * @param N The depth of the BFS.
     * @param pTo The person at the depth of N.
     * @param pathList The list storing the path from "pFrom" to "pTo".
     * @return The set of vertices at depth N.
     * @throws IllegalArgumentException if "pFrom" is null.
     */
    private Set<String> getVerticesAtDepthN(String pFrom, int N, String pTo, ArrayList<String> pathList) {
    	if (pFrom == null) {
    		throw new IllegalArgumentException();
    	}
    	
    	if (N < 0) {
    		N = hashmap.size();
    		if (pTo == null || pathList == null) {
    			System.out.println("pTo and pathList are not specified");
    		}
    	}
    	
    	// map each vertex to its predecessor to track the path betw. two vertices
    	HashMap<String, String> predecessors = new HashMap<String, String>();
    	
    	// store vertices at a specific depth
    	Set<String> friendsAtDepthN = new HashSet<String>();
    	
    	ArrayList<String> frontier = new ArrayList<String>();
    	HashMap<String, Integer> depths = new HashMap<String, Integer>();
    	Set<String> explored = new HashSet<String>();
    	
    	frontier.add(pFrom);
    	depths.put(pFrom, 0);
    	explored.add(pFrom);
    	boolean done = false;
    	while (!frontier.isEmpty() && !done) {
    		String vertex = frontier.remove(0);
    		
    		if (depths.get(vertex).equals(N))
				friendsAtDepthN.add(vertex);
    		else if (depths.get(vertex) > N) break;
    		
    		for (String successor : getNeighbors(vertex)) {
    			if (explored.contains(successor)) continue;
    			explored.add(successor);
    			depths.put(successor, depths.get(vertex)+1);
    			frontier.add(successor);
    			predecessors.put(successor, vertex);
    			if (successor.equals(pTo)) {
    				done = true;
    				break;
    			}
    		}
    	}
    	
    	if (N == hashmap.size() && pTo != null) {
    		// return path between pFrom to pTo
    		Stack<String> stackPath = new Stack<String>();
    		
    		if (predecessors.get(pTo) == null) return null;
    		
    		String vertex = pTo;
    		while (vertex != null) {
        		stackPath.push(vertex);
        		vertex = predecessors.get(vertex);
    		}
    		
    		while (!stackPath.isEmpty()) {
    			pathList.add(stackPath.pop());
    		}
    		
    		return null;
    	}
    	else {
    		// return friends at depth N
    		return friendsAtDepthN;
    	}
    }
    
    /**
     * Returns a set of 2-degree friends of person.
     * @param person The person whose 2-degree friends are to be examined.
     * @return The 2-degree friends of "person" if this person exists in the graph,
     * return null otherwise.
     * @throws IllegalArgumentException if "person" is null.
     */
    public Set<String> friendsOfFriends(String person) {
        if (person == null) {
        	throw new IllegalArgumentException();
        }
        
        return getVerticesAtDepthN(person, 2, null, null);
    }

    /**
     * Returns the shortest path between two people in the graph.
     * @param pFrom The person from which the shortest path is examined.
     * @param pTo The person to which the shortest path is examined.
     * @return The shortest path between "pFrom" and "pTo" if both exist in the
     * graph and there is a path between them, null otherwise.
     * @throws IllegalArgumentException if "pFrom" or "pTo" is null.
     */
    public List<String> getPathBetween(String pFrom, String pTo) {
    	if (pFrom == null || pTo == null) {
    		throw new IllegalArgumentException();
    	}
    	
    	ArrayList<String> pathList = new ArrayList<String>();
    	getVerticesAtDepthN(pFrom, -1, pTo, pathList);
    	return pathList;
    }

    /**
     * Returns a pretty-print of this graph in adjacency matrix form.
     * People are sorted in alphabetical order, "X" denotes friendship.
     * 
     * This method has been written for your convenience (e.g., for debugging).
     * You are free to modify it or remove the method entirely.
     * THIS METHOD WILL NOT BE PART OF GRADING.
     *
     * NOTE: this method assumes that the internal hashmap is valid (e.g., no
     * loop, graph is not a multigraph). USE IT AT YOUR OWN RISK.
     *
     * @return pretty-print of this graph
     */
    public String pprint() {
        // Get alphabetical list of people, for prettiness
        List<String> people = new ArrayList<String>(this.hashmap.keySet());
        Collections.sort(people);

        // String writer is easier than appending tons of strings
        StringWriter writer = new StringWriter();

        // Print labels for matrix columns
        writer.append("   ");
        for (String person: people)
            writer.append(" " + person);
        writer.append("\n");

        // Print one line of social connections for each person
        for (String source: people) {
            writer.append(source);
            for (String target: people) {
                if (this.getNeighbors(source).contains(target))
                    writer.append("  X ");
                else
                    writer.append("    ");
            }
            writer.append("\n");

        }

        // Remove last newline so that multiple printlns don't have empty
        // lines in between
        String string = writer.toString();
        return string.substring(0, string.length() - 1);
    }

}