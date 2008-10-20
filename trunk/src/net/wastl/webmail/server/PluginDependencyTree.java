/* CVS ID: $Id$ */
package net.wastl.webmail.server;

import java.util.*;

/*
 * PluginDependencyTree.java
 *
 * Created: Sat Sep 11 14:52:22 1999
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
 * @author Sebastian Schaffert
 * @version
 */

public class PluginDependencyTree {
    
    protected Plugin node;
    protected String meprovides;

    protected Vector children;

    public PluginDependencyTree(Plugin p) {
	this.node=p;
	this.meprovides=p.provides();
	children=new Vector();
    }

    public PluginDependencyTree(String s) {
	this.node=null;
	this.meprovides=s;
	children=new Vector();
    }
    
    public boolean provides(String s) {
	return s.equals(meprovides);
    }

    public String provides() {
	String s=meprovides;
	Enumeration e=children.elements();
	while(e.hasMoreElements()) {
	    PluginDependencyTree p=(PluginDependencyTree)e.nextElement();
	    s+=","+p.provides();
	}
	return s;
    }
	

    public boolean addPlugin(Plugin p) {
	if(p.requires().equals(meprovides)) {
	    children.addElement(new PluginDependencyTree(p));
	    return true;
	} else {
	    boolean flag=false;
	    Enumeration e=children.elements();
	    while(e.hasMoreElements()) {
		PluginDependencyTree pt=(PluginDependencyTree)e.nextElement();
		flag = flag || pt.addPlugin(p);
	    }
	    return flag;
	}
    }
	
	

    public void register(WebMailServer parent) {
	if(node!=null) {
	    //System.err.print(node.getName()+" ");
	    //System.err.flush();
	    node.register(parent);
	}

	/* Perform depth-first registraion. Breadth-first would be better, but
	   it will work anyway */
	Enumeration e=children.elements();
	while(e.hasMoreElements()) {
	    PluginDependencyTree p=(PluginDependencyTree)e.nextElement();
	    p.register(parent);
	}
    }
    
} // PluginDependencyTree
