package fr.univparis.metro;

import java.lang.Double;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class PriorityQueueTest {

    PriorityQueue<String> pQueue;

    @Before
    public void initPriorityQueue() {
	pQueue = new PriorityQueue<String>();

	pQueue.add("A", 5.0);

	pQueue.add("B", 10.0);
	pQueue.add("C", 7.0);

	pQueue.add("D", 12.0);
	pQueue.add("E", 15.0);
	pQueue.add("F", 11.0);
	pQueue.add("G", 8.0);
    }

    @Test
    public void isEmptyTest() {
	PriorityQueue<String> tmp = new PriorityQueue<String>();
	assertTrue( tmp.isEmpty() );

	assertFalse( pQueue.isEmpty() );
    }
    
    @Test
    public void addTest() {
	assertTrue( pQueue.add("H",17.0));

	assertFalse( pQueue.add("H",0.0));

	pQueue.add("J", Double.NEGATIVE_INFINITY);
	assertEquals("J", pQueue.poll());
    }

    @Test
    public void pollTest() {
	assertEquals("A", pQueue.poll());
	assertEquals("C", pQueue.poll());
	assertEquals("G", pQueue.poll());
	assertEquals("B", pQueue.poll());
	assertEquals("F", pQueue.poll());
	assertEquals("D", pQueue.poll());
	assertEquals("E", pQueue.poll());
	assertNull(pQueue.poll());
    }    
}
