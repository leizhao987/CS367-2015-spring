/**
 * A List is a general container storing a contiguous collection of items,
 * that is position-oriented using zero-based indexing and where duplicates
 * are allowed. 
 */
public class DLinkedList<E> implements ListADT<E> {

	private Listnode<E> head;
	private int numItems;
	
	public DLinkedList() {
		head = null;
		numItems = 0;
	}
	
	/**
	 * Adds item to the end of the List.
	 * 
	 * @param item the item to add
	 * @throws IllegalArgumentException if item is null 
	 */
	public void add(E item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		
		Listnode<E> newNode = new Listnode<E>(item);
		if (head == null) {
			head = newNode;
		} else {
			Listnode<E> curr = head;
			while(curr.getNext() != null) {
				curr = curr.getNext();
			}
			curr.setNext(newNode);
			newNode.setPrev(curr);
		}
		numItems ++;
	}
	
	/**
	 * Adds item at position pos in the List, moving the items originally in 
	 * positions pos through size() - 1 one place to the right to make room.
	 * 
	 * @param pos the position at which to add the item
	 * @param item the item to add
	 * @throws IllegalArgumentException if item is null 
	 * @throws IndexOutOfBoundsException if pos is less than 0 or greater 
	 * than size()
	 */
	public void add(int pos, E item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		if (pos < 0 || pos > numItems) {
			throw new IndexOutOfBoundsException();
		}
		
		Listnode<E> newNode = new Listnode<E>(item);

		if (head == null) {
			head = newNode;
		} else if (pos == 0) {
			newNode.setNext(head);
			head.setPrev(newNode);
			head = newNode;
		} else if (pos == numItems) {
			this.add(item);
			numItems --;
		} else {
			Listnode<E> curr = head;
			for (int i = 0; i < pos - 1; i ++) {
				curr = curr.getNext();
			}
			newNode.setNext(curr.getNext());
			curr.getNext().setPrev(newNode);
			curr.setNext(newNode);
			newNode.setPrev(curr);
		}
		numItems ++;
	}
	
	/**
	 * Returns true if item is in the List (i.e., there is an item x in the List 
	 * such that x.equals(item))
	 * 
	 * @param item the item to check
	 * @return true if item is in the List, false otherwise
	 */
	public boolean contains(E item) {
		if (item == null) {
			throw new IllegalArgumentException();
		}
		if (numItems == 0) {
			return false;
		}
		
		Listnode<E> curr = head;
		do {
			if (curr.getData().equals(item)) {
				return true;
			}
			curr = curr.getNext();
		} while (curr != null);
			
		return false;
	}
	
	/**
	 * Returns the item at position pos in the List.
	 * 
	 * @param pos the position of the item to return
	 * @return the item at position pos
	 * @throws IndexOutOfBoundsException if pos is less than 0 or greater than
	 * or equal to size()
	 */
	public E get(int pos) {
		if (pos < 0 || pos >= numItems) {
			throw new IndexOutOfBoundsException();
		}
		
		Listnode<E> curr = head;
		for (int i = 0; i < pos; i++) {
			curr = curr.getNext();
		}
		return curr.getData();
	}
	
	/**
	 * Returns true iff the List is empty.
	 * 
	 * @return true if the List is empty, false otherwise
	 */
	public boolean isEmpty() {
		if (numItems == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes and returns the item at position pos in the List, moving the items 
	 * originally in positions pos+1 through size() - 1 one place to the left to 
	 * fill in the gap.
	 * 
	 * @param pos the position at which to remove the item
	 * @return the item at position pos
	 * @throws IndexOutOfBoundsException if pos is less than 0 or greater than
	 * or equal to size()
	 */
	public E remove(int pos) {		
		if (pos < 0 || pos >= numItems) {
			throw new IndexOutOfBoundsException();
		}
		
		Listnode<E> curr = head;
		E returned;
		
		if (pos == 0) {
			head = curr.getNext();
			head.setPrev(null);
			returned = curr.getData();
		} else {
			for (int i = 1; i < pos; i++) {
				curr = curr.getNext();
			}
			returned = curr.getNext().getData();
			curr.getNext().getNext().setPrev(curr);
			curr.setNext(curr.getNext().getNext());
		}
		numItems --;
		return returned;
	}
	
	/**
	 * Returns the number of items in the List.
	 * 
	 * @return the number of items in the List
	 */
	public int size() {
		return numItems;
	}

}
