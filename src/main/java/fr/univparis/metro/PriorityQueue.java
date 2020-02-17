package fr.univparis.metro;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Double;
import java.lang.Integer;

public class PriorityQueue<E> {

    private class Pair {
        E val;
	Double key;
	
	public Pair(E v, Double k) {
	    val = v;
	    key = k;
	}
    }
	    
    private ArrayList<Pair> tree;
    private HashMap<E, Integer> index;
    
    public PriorityQueue() {
	tree = new ArrayList<Pair>();
	index = new HashMap<E, Integer>();
    }


    /** 
     * Inserts the specified element into this priority queue given a key (priority).
     * @param val the element to add
     * @param key the priority of val
     */ 
    public boolean add(E val, Double key) {
	if( index.containsKey(val) )
	    return false;

	Pair entry = new Pair(val,key);
	tree.add( entry );
    
	int i = tree.size() - 1;

	/* Move entry up in the tree, as long as needed; used to restore heap condition after insertion. */
	while(i >= 1 && tree.get((i-1)/2).key > key) {
	    tree.set(i, tree.get((i-1)/2)); 
	    index.put(tree.get((i-1)/2).val, i);

	    i = (i-1)/2;	    
	}
	
	tree.set(i, entry);
	index.put(val, i);
	return true;
    }    
}
