/* CVS ID: $Id$ */
package net.wastl.webmail.server;

import java.util.*;
import net.wastl.webmail.debug.ErrorHandler;
import net.wastl.webmail.exceptions.*;

/*
 * ConnectionTimer.java
 *
 * Created: Tue Feb  2 12:27:43 1999
 *
 * Copyright (C) 1999-2000 Sebastian Schaffert
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
 *
 *
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */
public class ConnectionTimer extends Thread {

    private Vector connections;
    private static final long sleep_interval=1000;
    
    public ConnectionTimer() {
	connections=new Vector();
	this.start();
    }
   
    public void printStatus() {
	System.err.println(" Vulture: "+connections.size()+" connections in queue");
    }	

    public void addTimeableConnection(TimeableConnection c) {
	synchronized(connections) {
	    connections.addElement(c);
	}
    }

    public void removeTimeableConnection(TimeableConnection c) {
	synchronized(connections) {
	    connections.removeElement(c);
	}
    }

    public void removeAll() {
	Enumeration e;
	synchronized(connections) {
	    e=connections.elements();
	}
	while(e.hasMoreElements()) {
	    TimeableConnection t=(TimeableConnection)e.nextElement();
	    t.timeoutOccured();
	}
    }

    public void run() {
	Enumeration e;
	while(true) {
	    synchronized(connections) {
		e=connections.elements();
	    }
	    while(e.hasMoreElements()) {
		TimeableConnection t=(TimeableConnection)e.nextElement();
		if(System.currentTimeMillis() - t.getLastAccess() > t.getTimeout()) {
		    t.timeoutOccured();
		}
	    }
	    try { this.sleep(sleep_interval); } catch(InterruptedException ex) { new ErrorHandler(ex); }
	}
    }
} // ConnectionTimer
