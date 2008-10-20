/* CVS ID: $Id$ */
package net.wastl.webmail.xml;

import java.util.*;

import org.w3c.dom.*;


/*
 * XMLMessagePart.java
 *
 * Created: Tue Apr 18 14:08:56 2000
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
 *
 */

/**
 * A message part object for an XML message
 */
public class XMLMessagePart  {
    
    protected Document root;
    protected Element part;

    /**
     * Create a new part for the given root document.
     * Creates the necessary Element.
     */
    public XMLMessagePart(Document root) {
	this.part=root.createElement("PART");
	this.root=root;
    }

    /**
     * Return a new part for a given part element
     */
    public XMLMessagePart(Element part) {
	this.part=part;
	this.root=part.getOwnerDocument();
    }

    public Element getPartElement() {
	return part;
    }

    public void setAttribute(String key, String value) {
	part.setAttribute(key,value);
    }

    public String getAttribute(String key) {
	return part.getAttribute(key);
    }

    public void quoteContent() {
	NodeList nl=part.getChildNodes();
	StringBuffer text=new StringBuffer();
	for(int i=0;i<nl.getLength();i++) {
	    Element elem=(Element)nl.item(i);
	    if(elem.getNodeName().equals("CONTENT")) {
		String value=XMLCommon.getElementTextValue(elem);
		StringTokenizer tok=new StringTokenizer(value,"\n");
		while(tok.hasMoreTokens()) {
		    text.append("> ").append(tok.nextToken()).append("\n");
		}
	    }
	}
	removeAllContent();

	addContent(text.toString(),0);
    }

    /**
     * This method is designed for content that already is in DOM format, like HTML
     * messages.
     */
    public void addContent(Document content) {
	Element content_elem=root.createElement("CONTENT");
	content_elem.setAttribute("quotelevel","0");
	
	/* Find all <BODY> elements and add the child nodes to the content */
	for(int count=0; count < 2; count++) {
	    NodeList nl=content.getDocumentElement().getElementsByTagName(count==0?"BODY":"body");
	    System.err.println("While parsing HTML content: Found "+nl.getLength()+" body elements");
	    for(int i=0; i<nl.getLength();i++) {
		NodeList nl2=nl.item(i).getChildNodes();
		System.err.println("While parsing HTML content: Found "+nl2.getLength()+" child elements");
		for(int j=0;j<nl2.getLength();j++) {
		    System.err.println("Element: "+j);
		    content_elem.appendChild(XMLCommon.importNode(root,nl2.item(j),true));
		}
	    }
	}

	
	part.appendChild(content_elem);

	//XMLCommon.debugXML(root);
    }

    public void addContent(String content, int quotelevel) {
	Element content_elem=root.createElement("CONTENT");
	content_elem.setAttribute("quotelevel",quotelevel+"");
	XMLCommon.setElementTextValue(content_elem,content,true);
	part.appendChild(content_elem);
    }

    public void insertContent(String content, int quotelevel) {
	Element content_elem=root.createElement("CONTENT");
	content_elem.setAttribute("quotelevel",quotelevel+"");
	XMLCommon.setElementTextValue(content_elem,content,true);
	Node first=part.getFirstChild();	
	part.insertBefore(content_elem,first);
    }
	

    public void addJavaScript(String content) {
	Element javascript_elem=root.createElement("JAVASCRIPT");
	XMLCommon.setElementTextValue(javascript_elem,content,true);
	part.appendChild(javascript_elem);
    }

    public void removeAllContent() {
	XMLCommon.genericRemoveAll(part,"CONTENT");
    }

    public XMLMessagePart createPart(String type) {
	XMLMessagePart newpart=new XMLMessagePart(root);
	newpart.setAttribute("type",type);
	appendPart(newpart);
	return newpart;
    }

    public void insertPart(XMLMessagePart childpart) {
	Node first=part.getFirstChild();
	part.insertBefore(childpart.getPartElement(),first);
    }

    public void appendPart(XMLMessagePart childpart) {
	part.appendChild(childpart.getPartElement());
    }

    public Enumeration getParts() {
	// Sucking NodeList needs a Vector to store Elements that will be removed!
	Vector v=new Vector();
	NodeList parts=part.getChildNodes();
	for(int j=0;j<parts.getLength();j++) {
	    Element elem=(Element)parts.item(j);
	    if(elem.getTagName().equals("PART")) {
		v.addElement(new XMLMessagePart(elem));
	    }
	}
	return v.elements();
    }

    public void removePart(XMLMessagePart childpart) {
	part.removeChild(childpart.getPartElement());
    }

    public void removeAllParts() {
	XMLCommon.genericRemoveAll(part,"PART");
    }

} // XMLMessagePart
