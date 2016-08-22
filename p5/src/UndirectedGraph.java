///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  SocialNetworkingApp.java
// File:             UndirectedGraph.java
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements GraphADT with an adjacency lists representation.
 * @author Lei Zhao, Lin Qi
 *
 */
public class UndirectedGraph<V> implements GraphADT<V>{

	// Stores the vertices of this graph, and their adjacency lists.
	// It's protected rather than private so that subclasses can access it.
    protected HashMap<V, ArrayList<V>> hashmap;

    /**
     * Constructor creating an empty undirected graph.
     */
    public UndirectedGraph() {
        this.hashmap = new HashMap<V, ArrayList<V>>();
    }

    /**
     * Constructor initializing an undirected graph.
     * @param hashmap
     */
    public UndirectedGraph(HashMap<V, ArrayList<V>> hashmap) {
        if (hashmap == null) throw new IllegalArgumentException();
        this.hashmap = hashmap;
    }

    /**
     * Adds the specified vertex to this graph.
     * @param vertex The vertex added to the graph.
     * @return Return true if vertex doesn't exist in the graph and is now
     * successfully added to the graph. Return false otherwise.
     * @throws IllegalArgumentException if vertex is null.
     */
    @Override
    public boolean addVertex(V vertex) {
    	if (vertex == null) {
    		throw new IllegalArgumentException();
    	}
    	
    	if (getAllVertices().contains(vertex)) return false;
    	
    	hashmap.put(vertex, new ArrayList<V>());
        return true;
    }

    /**
     * Adds an edge between two vertices in the graph.
     * @param v1, v2 The vertices between which an edge is to be added.
     * @return Return true if both vertices exist in the graph and there is no edge
     * between them. Return false otherwise.
     * @throws IllegalArgumentException if either of two vertices is null, or
     * doesn't exist in the graph.
     */
    @Override
    public boolean addEdge(V v1, V v2) {
    	if (v1 == null || v2 == null ||
    	   !getAllVertices().contains(v1) || !getAllVertices().contains(v2)) {
    		throw new IllegalArgumentException();
    	}
    	
    	if (v1.equals(v2) || getNeighbors(v1).contains(v2)) return false;
    	
    	hashmap.get(v1).add(v2);
    	hashmap.get(v2).add(v1);
  
        return true;
    }

    /**
     * Get the set of neighbors of a vertex in the graph.
     * @param vertex The vertex whose neighbors are to be obtained.
     * @return Return A set of vertices who are neighbors of current vertex.
     * @throws IllegalArgumentException if vertex is null or doesn't exist
     * in the graph.
     */
    @Override
    public Set<V> getNeighbors(V vertex) {
    	if (vertex == null || !getAllVertices().contains(vertex)) {
    		throw new IllegalArgumentException();
    	}
    	
    	Set<V> neighborSet = new HashSet<V>(hashmap.get(vertex));
        return neighborSet;
    }

    /**
     * Removes the edge between two vertices in the graph.
     * @param v1, v2 The vertices between which the edge is to be removed.
     * @throws IllegalArgumentException if either of the vertices is null.
     */
    @Override
    public void removeEdge(V v1, V v2) {
    	if (v1 == null || v2 == null) {
    		throw new IllegalArgumentException();
    	}
    	
    	if (getAllVertices().contains(v1) && getAllVertices().contains(v2) 
    		&& getNeighbors(v1).contains(v2)) {
    		hashmap.get(v1).remove(v2);
    		hashmap.get(v2).remove(v1);
    	}
    }

    /**
     * Returns a set of all vertices in the graph.
     * @return A set of all the vertices in this graph.
     */
    @Override
    public Set<V> getAllVertices() {
        return hashmap.keySet();
    }

    /* (non-Javadoc)
     * Returns a print of this graph in adjacency lists form.
     * 
     * This method has been written for your convenience (e.g., for debugging).
     * You are free to modify it or remove the method entirely.
     * THIS METHOD WILL NOT BE PART OF GRADING.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        for (V vertex: this.hashmap.keySet()) {
            writer.append(vertex + " -> " + hashmap.get(vertex) + "\n");
        }
        return writer.toString();
    }

}