/* CVS ID: $Id$ */
package net.wastl.webmail.misc;

import java.util.*;

/*
 * ExpireableCache.java
 *
 * Created: Fri Sep 17 09:43:10 1999
 *
 * Copyright (C) 2000 Sebastian Schaffert
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/**
 * This class represents a cache that automatically expires objects when a certain fillness
 * factor is reached.
 *
 * @author Sebastian Schaffert
 * @version
 */

public class ExpireableCache extends Thread {
    
    protected Hashtable cache;
    protected MyHeap timestamps;

    protected int capacity;
    protected float expire_factor;

    protected int hits=0;
    protected int misses=0;

    protected boolean shutdown=false;

    public ExpireableCache(int capacity, float expire_factor) {
	super("ExpireableCache");
	setPriority(MIN_PRIORITY);
	cache=new Hashtable(capacity);
	timestamps=new MyHeap(capacity);
	this.capacity=capacity;
	this.expire_factor=expire_factor;
    }

    public ExpireableCache(int capacity) {
	this(capacity,(float).90);
    }
    
    /*
     * Insert an element into the cache
     */
    public synchronized void put(Object key, Object value) {
	/* When the absolute capacity is exceeded, we must clean up */
	if(cache.size()+1 >= capacity) {
	    expireOver();
	}

	long l=System.currentTimeMillis();
	cache.put(key,value);
	timestamps.remove(key);
	timestamps.insert(key,l);
	expireOver();
    }

    public Object get(Object key) {
	long l=System.currentTimeMillis();
	timestamps.remove(key);
	timestamps.insert(key,l);
	return cache.get(key);
    }	


    public synchronized void remove(Object key) {
	cache.remove(key);
	timestamps.remove(key);
	notifyAll();
    }


    protected synchronized void expireOver() {
	while(cache.size()>=(capacity*expire_factor)) {
	    String nk=(String)timestamps.next();
	    cache.remove(nk);
	}
    }

    public void setCapacity(int size) {
	capacity=size;
    }

    public int getCapacity() {
	return capacity;
    }

    public int getUsage() {
	return cache.size();
    }

    public int getHits() {
	return hits;
    }

    public int getMisses() {
	return misses;
    }

    public void hit() {
	hits++;
    }

    public void miss() {
	misses++;
    }

    public void shutdown() {
	shutdown=true;
    }

    public void run() {
	while(!shutdown) {
	    try {
		wait(10000);
	    } catch(InterruptedException e) {}
	    expireOver();
	}
    }

    /**
     * Implement a simple heap that just returns the smallest long variable/Object key pair.
     */
    protected class MyHeap {
	int num_entries;
	long[] values;
	Object[] keys;

	MyHeap(int capacity) {
	    values=new long[capacity+1];
	    keys=new Object[capacity+1];
	    num_entries=0;
	}

	/**
	 * Insert a key/value pair
	 * Reorganize Heap afterwards
	 */
	public void insert(Object key, long value) {
	    keys[num_entries]=key;
	    values[num_entries]=value;
	    num_entries++;

	    increase(num_entries);
	}

	/**
	 * Return and delete the key with the lowest long value. Reorganize Heap.
	 */
	public Object next() {
	    Object ret=keys[0];
	    keys[0]=keys[num_entries-1];
	    values[0]=values[num_entries-1];
	    num_entries--;

	    decrease(1);
	    
	    return ret;
	}


	/**
	 * Remove an Object from the Heap.
	 * Unfortunately not (yet) of very good complexity since we are doing 
	 * a simple linear search here.
	 * @param key The key to remove from the heap
	 */
	public void remove(Object key) {
	    for(int i=0;i<num_entries;i++) {
		if(key.equals(keys[i])) {
		    num_entries--;
		    int cur_pos=i+1;
		    keys[i]=keys[num_entries];
		    decrease(cur_pos);
		    break;
		}
	    }
	}

	/**
	 * Lift an element in the heap structure
	 * Note that the cur_pos is actually one larger than the position in the array!
	 */
	protected void increase(int cur_pos) {
	    while(cur_pos > 1 && values[cur_pos-1] < values[cur_pos/2-1]) {
		Object tmp1=keys[cur_pos/2-1];keys[cur_pos/2-1]=keys[cur_pos-1];keys[cur_pos-1]=tmp1;
		long tmp2=values[cur_pos/2-1];values[cur_pos/2-1]=values[cur_pos-1];values[cur_pos-1]=tmp2;
		cur_pos /= 2;
	    }
	}	    

	/**
	 * Lower an element in the heap structure
	 * Note that the cur_pos is actually one larger than the position in the array!
	 */
	protected void decrease(int cur_pos) {
	    while((cur_pos*2 <= num_entries && values[cur_pos-1] > values[cur_pos*2-1]) ||
		  (cur_pos*2+1 <=num_entries && values[cur_pos-1] > values[cur_pos*2])) {
		int lesser_son;
		if(cur_pos*2+1 <= num_entries) {
		    lesser_son=values[cur_pos*2-1]<values[cur_pos*2]?cur_pos*2:cur_pos*2+1;
		} else {
		    lesser_son=cur_pos*2;
		}
		Object tmp1=keys[cur_pos-1];keys[cur_pos-1]=keys[lesser_son-1];keys[lesser_son-1]=tmp1;
		long tmp2=values[cur_pos-1];values[cur_pos-1]=values[lesser_son-1];values[lesser_son-1]=tmp2;
		cur_pos=lesser_son;
	    }
	}	    
		
    }
} // ExpireableCache
