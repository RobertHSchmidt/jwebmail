/* CVS ID: $Id$ */
package net.wastl.webmail.misc;

import org.w3c.dom.*;

/*
 * JavaScriptCleaner.java
 *
 * Created: Mon Jan  1 15:20:54 2001
 *
 * Copyright (C) 1999-2001 Sebastian Schaffert
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
 * JavaScriptCleaner.java
 *
 * This class removes hopefully all of the possible malicious code from HTML messages
 * like <SCRIPT> tags, javascript: hrefs and onMouseOver, ...;
 *
 * Furthermore, we should consider removing all IMG tags as they might be used to call CGIs
 *
 * Created: Mon Jan  1 15:20:54 2001
 *
 * @author Sebastian Schaffert
 * @version
 */

public class JavaScriptCleaner  {

    Document d;

    public JavaScriptCleaner(Document d) {
        this.d=d;
        walkTree(d.getDocumentElement());
    }

    protected void walkTree(Node node) {



        /* First we check for element types that shouldn't be sent to the user.
           For that, we add an attribute "malicious" that can be handled by the XSLT
           stylesheets that display the message.
         */
        if(node instanceof Element && ((Element)node).getTagName().toUpperCase().equals("SCRIPT")) {

            ((Element)node).setAttribute("malicious","Marked malicious because of potential JavaScript abuse");
        }

        if(node instanceof Element && ((Element)node).getTagName().toUpperCase().equals("IMG")) {

            ((Element)node).setAttribute("malicious","Marked malicious because of potential Image/CGI abuse");
        }

        /* What we also really don't like in HTML messages are FORMs! */

        if(node instanceof Element && ((Element)node).getTagName().toUpperCase().equals("FORM")) {
            ((Element)node).setAttribute("malicious","Marked malicious because of potential JavaScript abuse");
        }

        /* Now we search the attribute list for attributes that may potentially be used maliciously.
           These will be:
           - href: check for a String containing "javascript"
           - onXXX events: if they exist, the link will be marked "malicious".
         */
        String javascript_href="javascript";
        NamedNodeMap map=node.getAttributes();
        for(int i=0;i<map.getLength();i++) {
            Attr a=(Attr)map.item(i);
            /* First case: look for hrefs containing "javascript" */
            if(a.getName().toUpperCase().equals("HREF")) {
                for(int j=0;j<a.getValue().length()-javascript_href.length() ;j++) {
                    if(a.getValue().regionMatches(true,j,javascript_href,0,javascript_href.length())) {
                        ((Element)node).setAttribute("malicious","Marked malicious because of potential JavaScript abuse (HREF attribute contains javascript code)");
                        break;
                    }
                }
                /* All elements containing "onXXX" tags get the malicious attribute immediately */
            } else if(a.getName().toUpperCase().startsWith("ON")) {
                        ((Element)node).setAttribute("malicious","Marked malicious because of potential JavaScript abuse (element contains script events)");
            }
        }

        /* Do that recursively */
        if(node.hasChildNodes()) {
            NodeList nl=node.getChildNodes();
            for(int i=0;i<nl.getLength();i++) {
                walkTree(nl.item(i));
            }
        }
    }

} // JavaScriptCleaner
