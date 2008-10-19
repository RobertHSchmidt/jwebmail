/* CVS ID: $Id$ */
package net.wastl.webmail.misc;

import net.wastl.webmail.server.*;
import java.io.*;

/**
 * StreamConnector.java
 *
 * Created: Tue Sep  7 14:47:10 1999
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
 * Used to write to a OutputStream in a separate Thread to avoid blocking.
 *
 * @author Sebastian Schaffert
 * @version
 */
public class StreamConnector extends Thread {
    
    InputStream in;
    ByteStore b;
    int size;
    boolean ready=false;
    
    public StreamConnector(InputStream sin, int size) {
	super();
	in=sin;
	this.size=size;
	b=null;
	this.start();
    }
    
    public void run() {
	b=ByteStore.getBinaryFromIS(in,size);
	ready=true;
    }
    
    public ByteStore getResult() {
	while(!ready) {
	    try {
		sleep(500);
		System.err.print(".");
	    } catch(InterruptedException ex) {
	    }
	}
	System.err.println();
	return b;
    }
   
} // StreamConnector
