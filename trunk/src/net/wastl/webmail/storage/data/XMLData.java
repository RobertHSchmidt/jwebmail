/* CVS ID: $Id$ */
package net.wastl.webmail.storage.data;

/**
 * XMLData.java
 *
 *
 * Created: Sat Oct 12 15:07:58 2002
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
 *
 * @author <a href="mailto:wastl@wastl.net">Sebastian Schaffert</a>
 * @version 1.0
 */
import org.w3c.dom.*;

import org.apache.xpath.XPathAPI;
import javax.xml.transform.TransformerException;

import net.wastl.webmail.storage.Storage;
import net.wastl.webmail.storage.Storable;

import net.wastl.webmail.exceptions.WebMailException;

/**
 * A generic class for holding some XML data. This class provides means to
 * retrieve data based on XPath expressions.
 */
public class XMLData implements Storable {

    protected Document root;
    protected Element data;

    protected Storage loader;
    protected String name;

    public XMLData (Document d, Storage loader, String name){
        this.root = d;
        this.data = d.getDocumentElement();
        this.loader = loader;
        this.name = name;
    }

    /**
     * Return a DOM representation of this Storable.
     */
    public Document getDocumentInstance() {
        return root;
    }

    public DocumentFragment getDocumentFragment() {
        DocumentFragment df=root.createDocumentFragment();
        df.appendChild(data);
        return df;
    }


    /**
     * Get the value of the node pointed to by xpath, beginning at the root
     * node.
     */
    public String getProperty(String xpath) {
        return getValueXPath(xpath);
    }



    public void save() throws WebMailException {
        loader.saveStorable(name,this);
    }

    public void saveWith(Storage storage) throws WebMailException {
        storage.saveStorable(name,this);
    }

    /* -------------------------- Access Members ------------------------- */

    protected String getParentXPath(String str) {
        int last_slash = str.lastIndexOf("/");
        if(last_slash == -1) {
            return ".";
        } else {
            return str.substring(0,last_slash);
        }
    }


    /**
     * Return the node value of a single node selected by the given xpath
     * expression.
     */
    public String getValueXPath(String path) {
        data.normalize();
        try {
            Node n = XPathAPI.selectSingleNode(data,path);
            if(n != null) {
                return n.getNodeValue();
            } else {
                return null;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Set the node value of the node selected by the given xpath expression.
     */
    public void setValueXPath(String path, String value) {
        data.normalize();
        try {
            Node n = XPathAPI.selectSingleNode(data,path);
            if(n != null) {
                n.setNodeValue(value);
            } else {
                addNodeXPath(getParentXPath(path),root.createTextNode(value));
            }
        } catch(TransformerException ex) {
            addNodeXPath(getParentXPath(path),root.createTextNode(value));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    public Node getNodeXPath(String path) {
        try {
            Node n = XPathAPI.selectSingleNode(data,path);
            return n;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Add a node as child to the node selected by the given xpath expression.
     */
    public void addNodeXPath(String path, Node child) {
        try {
            Node n = XPathAPI.selectSingleNode(data,path);
            n.appendChild(child);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    public NodeList getNodeListXPath(String path) {
        try {
            NodeList n = XPathAPI.selectNodeList(data,path);
            return n;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}// XMLData
