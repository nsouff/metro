package fr.univparis.metro;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Double;
import java.lang.Integer;

public class PriorityQueue<T> {


    private ArrayList<Pair<T, Double>> tree;
    private HashMap<T, Integer> index;


    public PriorityQueue() {
	tree = new ArrayList<Pair<T, Double>>();
	index = new HashMap<T, Integer>();
    }


    /**
     * Returns true if this priority queue contains no elements.
     * @return true if this priority queue contains no elements
     */
    public boolean isEmpty() {
	return tree.isEmpty();
    }


    /**
     * Inserts the specified element into this priority queue given a key (priority).
     * @param val the element to add
     * @param key the priority of val
     */
    public boolean add(T val, Double key) {
	if( index.containsKey(val) || val == null )
	    return false;

	Pair<T, Double> entry = new Pair<T, Double> (val, key);
	tree.add( entry );

	int i = tree.size() - 1;

	siftUp(i);

	return true;
    }

    /**
     * Move node i up in the tree, as long as needed; used to restore heap condition after insertion.
     */
    private void siftUp(int i) {
	Pair<T, Double> entry = tree.get(i);

	while(i >= 1 && tree.get((i-1)/2).getValue() > entry.getValue()) {
	    tree.set(i, tree.get((i-1)/2));
	    index.put(tree.get((i-1)/2).getObj(), i);

	    i = (i-1)/2;
	}

	tree.set(i, entry);
	index.put(entry.getObj(), i);
    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    public T poll() {
	if( tree.isEmpty() )
	    return null;

	Pair<T, Double> root = tree.get(0);
	Pair<T, Double> last = tree.remove( tree.size() - 1 );

	if( null == index.remove(root.getObj()) )
	    throw new IllegalStateException();

	if( !tree.isEmpty() ) {
	    tree.set(0, last);
	    index.put(last.getObj(), 0);

	    siftDown(0);
	}
	return root.getObj();
    }


    /**
     * Move node i down in the tree; used to restore heap condition after deletion
     */
    private void siftDown(int i) {
	int left = 2*i + 1;
	int right = 2*i + 2;
	int win = i;

	if( left < tree.size() && tree.get(left).getValue() < tree.get(win).getValue() )
	    win = left;
        if ( right < tree.size() && tree.get(right).getValue() < tree.get(win).getValue() )
	    win = right;

	if( win != i ) {
	    swap(win, i);
	    index.put( tree.get(i).getObj(), win);
	    index.put( tree.get(win).getObj(), i);
	    siftDown(win);
	}
    }

    private void swap(int i, int j) {
	Pair<T, Double> tmp = tree.get(i);
	tree.set(i, tree.get(j));
	tree.set(j, tmp);
    }


    /**
     * Update the priority of element val with key. The new key must be lesser than the old key
     * @param val the element for which we want to update its priority
     * @param key the new key lesser than the previous key associated to val
     * @return true if the priority was successfully updated
     */
    public boolean updatePriority(T val, Double key) {
	if( !index.containsKey(val) )
	    return false;

	int i = index.get(val); // index of val in tree

	if( key >= tree.get(i).getValue() )
	    return false;

	tree.get(i).setValue(key); // updating the key

	siftUp(i);

	return true;
    }

    /**
     * Returns true if this queue contains the specified element. More formally, returns true if and only if this queue contains at least one element e such that o.equals(e).
     * @param o object to be checked for containment in this queue
     * @return true if this queue contains the specified element
     */
    public boolean contains(Object o) {
	return index.containsKey(o);
    }
}
