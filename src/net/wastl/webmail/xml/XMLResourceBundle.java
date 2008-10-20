/* CVS ID: $Id$ */
package net.wastl.webmail.xml;

import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import net.wastl.webmail.server.WebMailServer;

/*
 * XMLResourceBundle.java
 *
 * Created: Sun Mar  5 17:59:33 2000
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
 * A ResourceBundle implementation that uses a XML file to store the resources.
 *
 * @author Sebastian Schaffert
 * @version
 */

public class XMLResourceBundle extends ResourceBundle {

    protected boolean debug=false;

    protected Document root;
    protected String language;

    protected Element elem_locale;
    protected Element elem_common;
    protected Element elem_default;

    public XMLResourceBundle(String resourcefile, String lang) throws Exception {
	DocumentBuilder parser=DocumentBuilderFactory.newInstance().newDocumentBuilder();
	root = parser.parse("file://"+resourcefile);
	language=lang;
	NodeList nl=root.getElementsByTagName("COMMON");
	if(nl.getLength()>0) {
	    elem_common=(Element)nl.item(0);
	} else {
	    elem_common=null;
	}


	elem_locale=null;
	elem_default=null;
	/* Now the locale specific stuff; fallback to default if not possbile */
	String default_lang=root.getDocumentElement().getAttribute("default");
	if(debug) System.err.println("XMLResourceBundle ("+resourcefile+"): Default language '"+default_lang+"'.");
	nl=root.getElementsByTagName("LOCALE");
	for(int i=0;i<nl.getLength();i++) {
	    Element e=(Element)nl.item(i);
	    if(e.getAttribute("lang").equals(lang)) {
		elem_locale=e;
	    }
	    if(e.getAttribute("lang").equals(default_lang)) {
		elem_default=e;
	    }
	}
    }

    protected String getResult(Element element, String key) {
	NodeList nl=element.getElementsByTagName("RESOURCE");
	for(int i=0;i<nl.getLength();i++) {
	    Element e=(Element)nl.item(i);
	    if(e.getAttribute("name").equals(key)) {
		String s="";
		NodeList textl=e.getChildNodes();
		for(int j=0;j<textl.getLength();j++) {
		    if(debug) System.err.println("XMLResourceBundle ("+key+"): Type "+textl.item(j).getNodeName());
		    if(textl.item(j).getNodeName().equals("#text") ||
		       textl.item(j).getNodeName().equals("#cdata-section")) {
			s+=textl.item(j).getNodeValue();
		    }
		}
		return s;
	    }
	}
	return null;
    }
    
    public Object handleGetObject(String key) {
	String retval=null;
	if(elem_locale != null) {
	    retval=getResult(elem_locale,key);
	}
	if(retval == null && elem_default != null) {
	    retval=getResult(elem_default,key);
	}
	if(retval == null && elem_common != null) {
	    retval=getResult(elem_common,key);
	}
	if(debug) System.err.println("XMLResourceBundle: "+key+" = "+retval);
	return retval;
    }

    protected void getKeys(Element element, Hashtable hash) {
	NodeList nl=element.getElementsByTagName("RESOURCE");
	for(int i=0;i<nl.getLength();i++) {
	    hash.put(((Element)nl.item(i)).getAttribute("name"),"");
	}
    }
    
    public Enumeration getKeys() {
	
	Hashtable prop=new Hashtable();
	
	if(elem_common != null) {
	    getKeys(elem_common,prop);
	}
	if(elem_default != null) {
	    getKeys(elem_default,prop);
	}
	if(elem_locale != null) {
	    getKeys(elem_locale,prop);
	}
	return prop.keys();
    }
	
    public static synchronized ResourceBundle getBundle(String name, Locale locale, ClassLoader cl) throws MissingResourceException {
	String lang=locale.getLanguage();

	ResourceBundle ret=null;

	try {

	    ret=new XMLResourceBundle(WebMailServer.getServer().getProperty("webmail.template.path")+
				      System.getProperty("file.separator")+name+".xml",lang);
	} catch(Exception ex) {
	    ex.printStackTrace();
	    throw new MissingResourceException("Resource not found",name,"");
	}
	
	return ret;
    }

} // XMLResourceBundle
