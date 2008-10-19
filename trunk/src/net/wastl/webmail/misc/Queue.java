/* CVS ID: $Id$ */
package net.wastl.webmail.misc;

import java.util.*;

/**
 * Queue.java
 *
 * Created: Sat Sep 11 16:43:06 1999
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
 *
 * @author Sebastian Schaffert
 * @version
 */

public class Queue  {

    Vector contents;

    public Queue() {
	contents=new Vector();
    }
    

    public void queue(Object o) {
	contents.addElement(o);
    }

    public boolean isEmpty() {
	return contents.isEmpty();
    }

    public Object next() {
	Object o=contents.firstElement();
	contents.removeElementAt(0);
	return o;
    }
} // Queue
