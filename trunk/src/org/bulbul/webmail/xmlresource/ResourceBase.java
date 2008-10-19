/**
 * @(#)ResourceBase.java  1.0 2001/09/02
 *
 * Copyleft 2000 by Steve Excellent Lee
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
package org.bulbul.webmail.xmlresource;

import java.io.IOException;
import java.util.*;


import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;

import net.wastl.webmail.server.WebMailServer;

/**
 * A ResourceBundle implementation that uses a XML file to store the resources.
 * Modified from Sebastian Schaffert's
 * net.wastl.webmail.xml.XMLResourceBundle.java
 *
 * New scheme:
 * We separate locale resource to differenet files instead of putting all
 * different locale resources into single xml file, since the encoding
 * can't vary. (A single xml file can only use one encoding).
 *
 * Subclasses must override <code>getXmlResourceFilename</code> and
 * provide the filename which contains appropriate locale-specific resources.
 *
 * Note:
 * The resource files must resides in the directory that defined by
 * `webmail.template.path' property, hence <code>getXmlResourceFilename</code>
 * must returns only filename without pathname)
 *
 * @author        Steve Excellent Lee
 * @version      1.0 2001
 */
public abstract class ResourceBase extends ResourceBundle {
    protected boolean debug = true;

    protected Document xmlRoot = null;

    protected Element elementBundle = null;     // The <BUNDLE> element of resource xml file
    protected Element elem_common = null;

    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    public ResourceBase() {
    }


    public Enumeration getKeys() {
        Hashtable prop=new Hashtable();

        if(elem_common != null) {
            getKeys(elem_common,prop);
        }
        if(elementBundle != null) {
            getKeys(elementBundle,prop);
        }
        return prop.keys();
    }

    protected Object handleGetObject(String key) throws MissingResourceException {
        String retval=null;

        // Lazily load the XML resource file
        if (xmlRoot == null) {
            loadXmlResourceFile();
        }

        if (elementBundle != null) {
            retval = getResult(elementBundle, key);
        }
        if ((retval == null) && (elem_common != null)) {
            retval = getResult(elem_common,key);
        }

        if (debug)
            System.err.println("XMLResourceBundle: "+key+" = "+retval);

        return retval;
    }

    /**
     * See class description.
     */
    abstract protected String getXmlResourceFilename();

    protected void loadXmlResourceFile() {
        try {
            DocumentBuilder parser=DocumentBuilderFactory.newInstance().newDocumentBuilder();
            System.err.println("file://" +
                               WebMailServer.getServer().getProperty("webmail.template.path") +
                               System.getProperty("file.separator") +
                               getXmlResourceFilename());
            xmlRoot = parser.parse("file://" +
                                   WebMailServer.getServer().getProperty("webmail.template.path") +
                                   System.getProperty("file.separator") +
                                   getXmlResourceFilename());


            NodeList nl = xmlRoot.getElementsByTagName("COMMON");
            if (nl.getLength() > 0) {
                elem_common=(Element)nl.item(0);
            }

            nl = xmlRoot.getElementsByTagName("LOCALE");
            if (nl.getLength() > 0) {
                elementBundle = (Element)nl.item(0);
            }
        }
        catch (IOException e) {
            System.err.println(e);
        }
        catch (SAXException e) {
            System.err.println(e);
        }
        catch (ParserConfigurationException e) {
            System.err.println(e);
        }
    }

    protected void getKeys(Element element, Hashtable hash) {
        NodeList nl = element.getElementsByTagName("RESOURCE");
        for (int i=0; i < nl.getLength(); i++) {
            hash.put(((Element)nl.item(i)).getAttribute("name"), "");
        }
    }

    protected String getResult(Element element, String key) {
        NodeList nl = element.getElementsByTagName("RESOURCE");
        for(int i = 0; i < nl.getLength(); i++) {
            Element e = (Element)nl.item(i);
            if (e.getAttribute("name").equals(key)) {
                String s="";
                NodeList textl = e.getChildNodes();
                for (int j=0; j < textl.getLength(); j++) {
                    if (debug)
                        System.err.println("XMLResourceBundle ("+key+"): Type "+textl.item(j).getNodeName());
                    if (textl.item(j).getNodeName().equals("#text") ||
                        textl.item(j).getNodeName().equals("#cdata-section")) {
                        s += textl.item(j).getNodeValue();
                    }
                }
                return s;
            }
        }
        return null;
    }
}

