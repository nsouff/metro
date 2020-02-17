package fr.univparis.metro;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Double;
import java.lang.Integer;

public class PriorityQueue<E> {

    private class Pair {

	public Pair(E v, Double k) {
	    val = v;
	    key = k;
	}
	
        E val;
	Double key;
    }
	    
    private ArrayList<Pair> tree;
    private HashMap<E, Integer> index;


    public PriorityQueue() {
	tree = new ArrayList<Pair>();
	index = new HashMap<E, Integer>();
    }


    
}
