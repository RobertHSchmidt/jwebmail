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


package net.wastl.webmail.xml;

import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import net.wastl.webmail.server.WebMailServer;

/**
 * A ResourceBundle implementation that uses a XML file to store the resources.
 *
 * @author Sebastian Schaffert
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
}
