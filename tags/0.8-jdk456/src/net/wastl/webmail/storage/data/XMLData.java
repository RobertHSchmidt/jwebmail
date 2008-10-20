/*
 * @(#)$Id$
 *
 * Copyright 2008 by the JWebMail Development Team and Sebastian Schaffert.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package net.wastl.webmail.storage.data;

/**
 * XMLData.java
 *
 *
 * Created: Sat Oct 12 15:07:58 2002
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
