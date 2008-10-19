/* CVS ID: $Id$ */
package net.wastl.webmail.storage.xml;

import org.w3c.dom.*;

import java.net.*;
import java.io.*;
import java.util.Properties;

import javax.xml.parsers.*;
import org.xml.sax.InputSource;

import net.wastl.webmail.storage.*;
import net.wastl.webmail.exceptions.WebMailException;
import net.wastl.webmail.xml.XMLCommon;

/*
 * XMLStorage.java
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
 * An implementation of the abstract Storage class storing the data in XML files.
 *
 * Requires the following properties: "webmail.data.url", "webmail.xml.url"
 */
public class XMLStorage extends Storage {
    

    public XMLStorage(Properties options) {
	super(options);
    }

    /**
     * Save the Storable that is provided as argument by the means of this
     * storage implementation.
     *
     * @param path the path where to store the data. The implementing subclasses
     * need to take care of properly representing UNIX-like paths.
     * @param data the data to store.
     */
    public void _saveStorable(String path, Document data) throws WebMailException, IOException {
	File file = new File(getProperty("webmail.data.path")+File.separator+path);
	if(!file.canRead() || !file.canWrite()) {
	    throw new WebMailException("Cannot read file "+path+". Please check the permissions.");
	}

	OutputStream out = new FileOutputStream(file);
	XMLCommon.writeXML(data,out,null);  // instead of "null" should state the DTD
	out.flush();
	out.close();
    }


    /**
     * Load a Storable from the specified path. Return the Node representing the
     * data of the Storable.
     *
     * @param path the path where to store the data. The implementing subclasses
     * need to take care of properly representing UNIX-like paths.
     */
    public Document _loadStorable(String path) throws WebMailException, IOException {
	File file = new File(getProperty("webmail.data.path")+File.separator+path);
	if(!file.canRead() || !file.canWrite()) {
	    throw new WebMailException("Cannot read file "+path+". Please check the permissions.");
	}

	InputStream in = new FileInputStream(file);
	

	try {
	    DocumentBuilderFactory dfactory=DocumentBuilderFactory.newInstance();
	    dfactory.setNamespaceAware(true);
	    DocumentBuilder parser = dfactory.newDocumentBuilder();
	    Document root = parser.parse(new InputSource(new InputStreamReader(in, "UTF-8")));

	    return root;
	} catch(Exception ex) {
	    throw new WebMailException("Could not load "+path,ex);
	}

    }


    /**
     * Remove a certain configuration item from the store.
     */
    public void removeStorable(String path) throws WebMailException, IOException {
	File file = new File(getProperty("webmail.data.path")+File.separator+path);
	if(!file.canRead() || !file.canWrite()) {
	    throw new WebMailException("Cannot read file "+path+". Please check the permissions.");
	}
	if(!file.exists()) {
	    throw new WebMailException("Cannot delete file "+path+". It does not exist.");
	}
	file.delete();
    }
    
}

