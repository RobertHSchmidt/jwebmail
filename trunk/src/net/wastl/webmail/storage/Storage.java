/* CVS ID: $Id$ */
package net.wastl.webmail.storage;

import org.w3c.dom.*;

import java.util.Hashtable;
import java.util.Properties;
import java.io.IOException;

import net.wastl.webmail.misc.ExpireableCache;
import net.wastl.webmail.exceptions.WebMailException;

/*
 * Storage.java
 *
 * Created: Oct 2002
 *
 * Copyright (C) 1999-2002 Sebastian Schaffert
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
 * This class specifies an abstract class used to store persistent DOM data.
 * 
 * @see Storable
 */
public abstract class Storage {

    protected ExpireableCache cache;
    
    protected Properties options;

    public Storage(Properties options) {
	this.options=options;

	int cache_size=100;
	float cache_expire=(float)0.9;
	try {
	    cache_size=Integer.parseInt(getProperty("webmail.cache.size"));
	} catch(Exception e) {}
	try {
	    cache_expire=Float.parseFloat(getProperty("webmail.cache.expire"));
	} catch(Exception e) {}

	this.cache=new ExpireableCache(cache_size,cache_expire);
    }

    private static Hashtable instances = new Hashtable();

    /**
     * Storages are singletons, thus this is the method to create them.
     */
    public static Storage getInstance(String classname, Properties options) 
	throws WebMailException {
	try {
	    if(instances.get(classname) == null) {
		Class c = Class.forName(classname);
		Class[] argtypes={Class.forName("java.util.Properties")};
		Object[] args={options};
		Storage instance=(Storage)c.getConstructor(argtypes).newInstance(args);
		instances.put(classname,instance);
		return instance;
	    } else {
		return (Storage)instances.get(classname);
	    }
	} catch(NoSuchMethodException ex) {
	    throw new WebMailException("Class "+classname+" did not have an appropriate constructor");
	} catch(ClassNotFoundException ex) {
	    throw new WebMailException("Class "+classname+" not found.");
	} catch(Exception ex) {
	    throw new WebMailException("An error occured",ex);
	}
    }

    public String getProperty(String key) {
	return (String)options.get(key);
    }

    /**
     * Save the Storable that is provided as argument by the means of this
     * storage implementation. Wrapper method to _saveStorable.
     *
     * @param path the path where to store the data. The implementing subclasses
     * need to take care of properly representing UNIX-like paths.
     * @param data the data to store.
     */
    public void saveStorable(String path, Storable data) throws WebMailException {
	try {
	    _saveStorable(path,data.getDocumentInstance());
	} catch(IOException ex) {
	    throw new WebMailException("IO Exception while saving storable",ex);
	}
    }


    /**
     * Save the Storable that is provided as argument by the means of this
     * storage implementation.
     *
     * @param path the path where to store the data. The implementing subclasses
     * need to take care of properly representing UNIX-like paths.
     * @param data the data to store.
     */
    public abstract void _saveStorable(String path, Document data) 
	throws WebMailException,IOException;


    /**
     * Load a Storable from the specified path. Wrapper method to _loadStorable,
     * which implements a caching strategy.
     *
     * @param path the path where to store the data. The implementing subclasses
     * need to take care of properly representing UNIX-like paths.
     * @param c the class to create. A new instance will be created where the 
     * constructor takes as a sole parameter a Document representing the
     * data.
     */
    public Storable loadStorable(String path, Class c) 
	throws WebMailException {
	if(cache.get(path) != null) {
	    cache.hit();
	    return (Storable)cache.get(path);
	} else {
	    cache.miss();
	    try {
		Class[] argtypes={
		    Class.forName("org.w3c.dom.Document"),
		    this.getClass(),
		    Class.forName("java.lang.String")
		 };
		Object[] args={_loadStorable(path),this,path};
		
		return (Storable)c.getConstructor(argtypes).newInstance(args);	    
	    } catch(ClassNotFoundException ex) {
		throw new WebMailException("You don't have org.w3c.dom.Document; Please upgrade your Java installation");
	    } catch(NoSuchMethodException ex) {
		throw new WebMailException("Class "+c.getName()+" does not have an appropriate constructor!");
	    } catch(Exception ex) {
		throw new WebMailException("An error occured",ex);
	    }
	}
    }


    /**
     * Load a Storable from the specified path. Return the Node representing the
     * data of the Storable.
     *
     * @param path the path where to store the data. The implementing subclasses
     * need to take care of properly representing UNIX-like paths.
     */
    public abstract Document _loadStorable(String path) throws WebMailException, IOException;



    /**
     * Remove a certain configuration item from the store.
     */
    public abstract void removeStorable(String path) throws WebMailException, IOException;
}
