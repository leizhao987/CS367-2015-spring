///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  VersionControlApp.java
// File:             SimpleQueue.java
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
 * An ordered collection of items, where items are added to the back and removed
 * from the front.
 * @author Lei Zhao, Lin Qi
 * 
 */
public class SimpleQueue<E> implements QueueADT<E> {
	
	private E[] items;
	private int numItems, frontIndex, rearIndex;
	private static final int INITSIZE = 10;
	
	@SuppressWarnings("unchecked")
	public SimpleQueue() {
		numItems = 0;
		frontIndex = -1;
		rearIndex = -1;
		items = (E[])(new Object[INITSIZE]);
	}
	
	/**
     * Checks if the queue is empty.
     * @return true if queue is empty; otherwise false.
     */
	public boolean isEmpty() {
		return (numItems == 0);
	}
	
	/**
     * Increases the index according to its position.
     * @return 0 if index is in the last pos; otherwise index + 1.
     */
	private int incrementIndex(int index) {
		if (index == items.length - 1) {
			return 0;
		}
		else
			return index + 1;
	}
	
	/**
     * removes and returns the front item of the queue.
     * @return the front item of the queue.
     * @throws EmptyQueueException if the queue is empty.
     */
	public E dequeue() throws EmptyQueueException {
		if (this.isEmpty()) {
			throw new EmptyQueueException();
		}
		
		E frontItem = items[frontIndex];
		frontIndex = incrementIndex(frontIndex);
		numItems --;
		if (numItems == 0) {
			frontIndex = -1;
			rearIndex = -1;
		}
		return frontItem;
	}
	
	/**
     * Adds an item to the rear of the queue.
     * @param item the item to add to the queue.
     * @throws IllegalArgumentException if item is null.
     */
	public void enqueue(E item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		
		if (numItems == items.length) {
			@SuppressWarnings("unchecked")
			E[] tmp = (E[])(new Object[2*items.length]);
			System.arraycopy(items, frontIndex, tmp, frontIndex,
					         items.length - frontIndex);
			if (frontIndex != 0) {
				System.arraycopy(items, 0, tmp, items.length, frontIndex);
			}
			items = tmp;
			rearIndex = numItems + frontIndex - 1;
		}
		
		if (numItems == 0) {
			frontIndex ++;
		}
		
		rearIndex = incrementIndex(rearIndex);
		
		items[rearIndex] = item;
		numItems ++;
	}
	
	/**
     * Returns (but does not remove) the front item of the queue.
     * @return the top item of the stack.
     * @throws EmptyQueueException if the queue is empty.
     */
	public E peek() throws EmptyQueueException {
		if (this.isEmpty()) {
			throw new EmptyQueueException();
		}
		else
			return items[frontIndex];
	}
	
	/**
     * Returns the size of the queue.
     * @return the size of the queue
     */
	public int size() {
		return numItems;
	}
	
	/**
     * Returns a string representation of the queue (for printing).
     * @return a string representation of the queue.
     */
	public String toString() {
		String str = "";
		
		if (this.isEmpty()) {
			return str;
		}
		
		int front = frontIndex;
		do {
			str += items[front].toString();
			str += "\n";
			front = incrementIndex(front);
		} while (front != incrementIndex(rearIndex));
		
		return str;
	}
}
