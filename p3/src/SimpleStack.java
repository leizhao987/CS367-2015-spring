///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  VersionControlApp.java
// File:             SimpleStack.java
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
 * An ordered collection of items, where items are both added and removed 
 * exclusively from the front.
 * @author Lei Zhao, Lin Qi
 */
public class SimpleStack<E> implements StackADT<E> {

	private E[] items;
	private int numItems;
	private static final int INITSIZE = 10;
	
	@SuppressWarnings("unchecked")
	public SimpleStack() {
		numItems = 0;
		items = (E[])(new Object[INITSIZE]);
	}
	
    /**
     * Checks if the stack is empty.
     * @return true if stack is empty; otherwise false
     */
	public boolean isEmpty() {
		return (numItems == 0);
	}
	
	/**
     * Returns (but does not remove) the top item of the stack.
     * @return the top item of the stack
     * @throws EmptyStackException if the stack is empty
     */
	public E peek() throws EmptyStackException {
		if (this.isEmpty()) {
			throw new EmptyStackException();
		}
		else {
			return items[numItems - 1];
		}
	}
	
	/**
     * Pops the top item off the stack and returns it. 
     * @return the top item of the stack
     * @throws EmptyStackException if the stack is empty
     */
	public E pop() throws EmptyStackException {
		if (this.isEmpty()) {
			throw new EmptyStackException();
		}
		else {
			E topItem = items[numItems - 1];
			numItems --;
			return topItem;
		}
	}
	
	 /**
     * Pushes the given item onto the top of the stack.
     * @param item the item to push onto the stack
     * @throws IllegalArgumentException if item is null.
     */
	public void push(E item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		
		if (numItems == items.length) {
			@SuppressWarnings("unchecked")
			E[] tmp = (E[])(new Object[2*items.length]);
			System.arraycopy(items, 0, tmp, 0, items.length);
			items = tmp;
		}
		
		numItems ++;
		items[numItems - 1] = item;
	}
	
	/**
     * Returns the size of the stack.
     * @return the size of the stack
     */
	public int size() {
		return numItems;
	}
	
	/**
     * Returns a string representation of the stack (for printing).
     * @return a string representation of the stack
     */
	public String toString() {
		String str = "";
		
		if (this.isEmpty()) {
			return str;
		}
		
		for (int i = numItems - 1; i >= 0; i--) {
			str += items[i].toString();
			str += "\n";
		}
		
		return str;
	}
}
