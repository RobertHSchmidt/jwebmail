/* CVS ID: $Id$ */
package net.wastl.webmail.xml;

import java.util.*;

import org.w3c.dom.*;


/*
 * XMLMessage.java
 *
 * Created: Tue Apr 18 14:02:30 2000
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
 * Represents an XML message object (part of the XMLUserModel)
 * @see XMLUserModel
 */
public class XMLMessage extends XMLMessagePart {

    protected Element message;

    public XMLMessage(Element message) {
        super(message);
        this.message=message;
    }

    public Element getMessageElement() {
        return message;
    }


    public boolean messageCompletelyCached() {
        NodeList nl=message.getElementsByTagName("PART");
        return nl.getLength()>0;
    }


    public void prepareReply(String prefix_subject, String postfix_subject,
                             String prefix_message, String postfix_message) {

        String subject=getHeader("SUBJECT");
        // Test whether this is already a reply (prefixed with RE or AW)
        if(!isReply(subject)) {
            subject=prefix_subject+" "
                +getHeader("SUBJECT")+" "
                +postfix_subject;
        }
        setHeader("SUBJECT",subject);

        XMLMessagePart firstpart=new XMLMessagePart(getFirstMessageTextPart(message));

        firstpart.quoteContent();

        firstpart.insertContent(prefix_message+"\n",0);
        firstpart.addContent(postfix_message,0);

        removeAllParts();

        XMLMessagePart newmainpart=createPart("multi");

        newmainpart.appendPart(firstpart);
    }

    public void prepareForward(String prefix_subject, String postfix_subject,
                               String prefix_message, String postfix_message) {

        String subject=getHeader("SUBJECT");
        subject=prefix_subject+" "
            +getHeader("SUBJECT")+" "
            +postfix_subject;
        setHeader("SUBJECT",subject);

        XMLMessagePart mainpart=null;
        NodeList nl=message.getChildNodes();
        for(int i=0;i<nl.getLength();i++) {
            Element elem=(Element)nl.item(i);
            if(elem.getTagName().equals("PART")) {
                mainpart=new XMLMessagePart(elem);
                break;
            }
        }
        XMLMessagePart firstpart=new XMLMessagePart(getFirstMessageTextPart(message));

        firstpart.insertContent(prefix_message+"\n",0);
        firstpart.addContent(postfix_message,0);

        removeAllParts();

        XMLMessagePart newmainpart=createPart("multi");

        newmainpart.appendPart(firstpart);

        if(mainpart != null) {
            Enumeration enum=mainpart.getParts();
            while(enum.hasMoreElements()) {
                newmainpart.appendPart((XMLMessagePart)enum.nextElement());
            }
        }
    }


    public Element getHeader() {
        NodeList nl=message.getElementsByTagName("HEADER");
        Element header=nl.getLength()==0?null:(Element)nl.item(0);
        if(header == null) {
            header=root.createElement("HEADER");
            message.appendChild(header);
        }
        return header;
    }

    public String getHeader(String header) {
        Element xml_header=getHeader();
        NodeList nl=xml_header.getElementsByTagName(header);
        Element element;
        if(nl.getLength() == 0) {
            element=root.createElement(header);
            xml_header.appendChild(element);
        } else {
            element=(Element)nl.item(0);
        }
        return XMLCommon.getElementTextValue(element);
    }


    public void setHeader(String header, String value) {
        Element xml_header=getHeader();
        NodeList nl=xml_header.getElementsByTagName(header);
        Element element;
        if(nl.getLength() == 0) {
            element=root.createElement(header);
            xml_header.appendChild(element);
        } else {
            element=(Element)nl.item(0);
        }
        XMLCommon.setElementTextValue(element,value);
    }


    protected boolean isReply(String subject) {
        String s=subject.toUpperCase();
        return s.startsWith("RE") || s.startsWith("AW");
    }

    protected Element getFirstMessageTextPart(Element parent) {
        NodeList nl=parent.getChildNodes();
        // Modified by exce, start
        /**
         * Maybe here we should modify the algorithm:
         * If no appropriate text/plain is found, try to search for text/html.
         */
        // Modified by exce, end
        for(int i=0;i<nl.getLength();i++) {
            Element elem=(Element)nl.item(i);
            if(elem.getTagName().equals("PART")) {
                if(elem.getAttribute("type").equals("multi")) {
                    Element retval=getFirstMessageTextPart(elem);
                    if(retval!=null) {
                        return retval;
                    }
                } else if(elem.getAttribute("type").equals("text")) {
                    return elem;
                }
            }
        }
        // Modified by exce, start
        for(int i=0;i<nl.getLength();i++) {
            Element elem=(Element)nl.item(i);
            if(elem.getTagName().equals("PART")) {
                if(elem.getAttribute("type").equals("multi")) {
                    Element retval=getFirstMessageTextPart(elem);
                    if(retval!=null) {
                        return retval;
                    }
                } else if(elem.getAttribute("type").equals("html")) {
                        return elem;
                }
            }
        }
        // Modified by exce, end
        return null;
    }

    public XMLMessagePart getFirstMessageTextPart() {
        return new XMLMessagePart(getFirstMessageTextPart(message));
    }

    public XMLMessagePart getFirstMessageMultiPart() {
        NodeList nl=message.getChildNodes();
        for(int i=0;i<nl.getLength();i++) {
            Element elem=(Element)nl.item(i);
            if(elem.getTagName().equals("PART")) {
                if(elem.getAttribute("type").equals("multi")) {
                    return new XMLMessagePart(elem);
                }
            }
        }
        return null;
    }


} // XMLMessage
