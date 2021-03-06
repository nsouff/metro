package fr.univparis.metro;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Double;
import java.lang.Integer;
/**
 * A priority queue where all the element are linked with a weight to order them
 */
public class PriorityQueue<T> {


  private ArrayList<Pair<T, Double>> tree;
  private HashMap<T, Integer> index;

 /**
  * Creates a empty PriorityQueue
  */
  public PriorityQueue() {
    tree = new ArrayList<Pair<T, Double>>();
    index = new HashMap<T, Integer>();
  }
 /**
  * Creates a PriorityQueue with the specified initial capacity
  * @param initialCapacity the initial capacity of the PriorityQueue
  */
  public PriorityQueue(int initialCapacity) {
    tree = new ArrayList<Pair<T, Double>>(initialCapacity);
    index = new HashMap<T, Integer>(initialCapacity);
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
  * @return true if the elemet has been added
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

    while(i >= 1 && tree.get((i-1)/2).getValue().compareTo(entry.getValue()) > 0) {
      tree.set(i, tree.get((i-1)/2));
      index.put(tree.get(i).getObj(), i);

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

    if( left < tree.size() && tree.get(left).getValue().compareTo(tree.get(win).getValue()) < 0 )
    win = left;
    if ( right < tree.size() && tree.get(right).getValue().compareTo(tree.get(win).getValue()) < 0 )
    win = right;

    if( win != i ) {
      swap(win, i);
      index.put( tree.get(i).getObj(), i);
      index.put( tree.get(win).getObj(), win);
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

    if( key.compareTo(tree.get(i).getValue()) >= 0 )
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

 /**
  * Removes a single instance of the specified element from this queue, if it is present.
  * More formally, removes an element e such that o.equals(e), if this queue contains one or more such elements.
  * Returns true if and only if this queue contained the specified element (or equivalently, if this queue changed as a result of the call).
  * @param o the element to be removed from this queue, if present
  * @return true if this queue changed as a result of the call
  */
  public boolean remove(Object o) {
    if( !index.containsKey(o) )
    return false;

    Pair<T, Double> last = tree.remove( tree.size() - 1 );
    int pos = index.remove(o);

    if ( pos == tree.size() )
    return true;

    if( !tree.isEmpty() ) {
      tree.set(pos, last);
      index.put(last.getObj(), pos);

      siftDown(pos);
    }
    return true;
  }
}
