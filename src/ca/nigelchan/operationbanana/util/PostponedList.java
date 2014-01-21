package ca.nigelchan.operationbanana.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author nigelchan
 *
 * PostponedList postpones any insertions and removals after any iterationss
 */
public class PostponedList<T> implements Iterable<T> {
	
	private LinkedList<T> addList = new LinkedList<T>();
	private ArrayList<T> collection;
	private LinkedList<T> removeList = new LinkedList<T>();

	public PostponedList() {
		collection = new ArrayList<T>();
	}
	
	public PostponedList(int initialSize) {
		collection = new ArrayList<T>(initialSize);
	}
	
	public void add(T item) {
		addList.add(item);
	}
	
	public boolean contains(T item) {
		return (collection.contains(item) && !removeList.contains(item)) || addList.contains(item);
	}
	
	public void remove(T item) {
		addList.remove(item);
		removeList.add(item);
	}

	@Override
	public Iterator<T> iterator() {
		collection.removeAll(removeList);
		removeList.clear();
		collection.addAll(addList);
		addList.clear();
		return collection.iterator();
	}

}
