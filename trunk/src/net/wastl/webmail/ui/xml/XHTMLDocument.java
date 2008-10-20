/* CVS ID: $Id$ */
package net.wastl.webmail.ui.xml;

import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;


import java.io.*;


/**
 * XHTMLDocument.java
 *
 * Created: Mon Mar 27 16:05:52 2000
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
 * Constructs HTML-Documents using a Stylesheet and a XML Document.
 *
 * @author Sebastian Schaffert
 * @version
 */

public class XHTMLDocument extends net.wastl.webmail.ui.html.HTMLDocument {

    public XHTMLDocument(Document xml, String xsl) throws WebMailException {

        StringWriter writer = new StringWriter();

        long start_t=System.currentTimeMillis();
        try {
            DOMSource msg_xml=new DOMSource((Node)xml);
            StreamSource msg_xsl=new StreamSource("file://"+xsl);
            StreamResult msg_result=new StreamResult(writer);

            TransformerFactory factory = TransformerFactory.newInstance();

            Transformer processor = factory.newTransformer(msg_xsl);

            //processor.setDiagnosticsOutput(System.err);
            processor.transform(msg_xml,msg_result);
        } catch(Exception ex) {
            System.err.println("Error transforming XML with "+xsl+" to XHTML.");
            WebMailServer.getStorage().log(Storage.LOG_INFO,"Error transforming XML with "+xsl+" to XHTML.");
            throw new WebMailException(ex.getMessage());
        }
        long end_t=System.currentTimeMillis();
        //System.err.println("Transformation XML --> XHTML took "+(end_t-start_t)+" ms.");
        WebMailServer.getStorage().log(Storage.LOG_DEBUG,"Transformation XML --> XHTML took "+(end_t-start_t)+" ms.");

        content=writer.toString();
    }

    public XHTMLDocument(Document xml, Templates stylesheet) throws WebMailException {
        StringWriter writer = new StringWriter();

        long start_t=System.currentTimeMillis();
        try {
            DOMSource msg_xml=new DOMSource((Node)xml);
            StreamResult msg_result=new StreamResult(writer);

            Transformer processor = stylesheet.newTransformer();
            processor.transform(msg_xml,msg_result);
        } catch(Exception ex) {
            System.err.println("Error transforming XML to XHTML.");
            ex.printStackTrace();
            throw new WebMailException(ex.toString());
        }
        long end_t=System.currentTimeMillis();
        //System.err.println("Transformation (with precompiled stylesheet) XML --> XHTML took "+(end_t-start_t)+" ms.");
        WebMailServer.getStorage().log(Storage.LOG_DEBUG,"Transformation (with precompiled stylesheet) XML --> XHTML took "+(end_t-start_t)+" ms.");

        content=writer.toString();
    }


    public String toString() {
        return content;
    }

    public int length() {
        return content.length();
    }

} // XHTMLDocument
