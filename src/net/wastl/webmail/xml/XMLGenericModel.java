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
import java.io.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import net.wastl.webmail.server.*;

/*
 * XMLGenericModel.java
 *
 * Created: Thu May  4 15:53:12 2000
 *
 */

/**
 * A generic representation of WebMail data. Contains mainly state information
 * and the system configuration
 *
 * @author Sebastian Schaffert
 * @version
 */

public class XMLGenericModel  {

    protected Document root;

    protected Element sysdata;

    protected Element statedata;

    protected WebMailServer parent;

    protected long current_id=0;

    protected DocumentBuilder parser;

    public XMLGenericModel(WebMailServer parent, Element rsysdata)
        throws ParserConfigurationException {

        this.parent=parent;

        parser=DocumentBuilderFactory.newInstance().newDocumentBuilder();

        initRoot();

        statedata=root.createElement("STATEDATA");

        NodeList nl=root.getDocumentElement().getElementsByTagName("STATEDATA");
        if(nl.getLength() > 0) {
            System.err.println("*** Warning: Webmail usermodel template contained a STATEDATA tag, although this should only be created at runtime!");
            root.getDocumentElement().replaceChild(statedata,nl.item(0));
        } else {
            root.getDocumentElement().appendChild(statedata);
        }

        this.sysdata=rsysdata;

    }

    protected void initRoot() {
        // Create a new usermodel from the template file
        try {
            root = parser.parse("file://"+parent.getProperty("webmail.xml.path")+
                                System.getProperty("file.separator")+"generic_template.xml");
        } catch(Exception ex) {
            System.err.println("Error parsing WebMail UserModel template "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Document getRoot() {
        return root;
    }

    public Element getStateData() {
        return statedata;
    }

    public void init() {
        setStateVar("base uri",parent.getBasePath());
        setStateVar("img base uri",parent.getImageBasePath()+
                    "/"+parent.getDefaultLocale().getLanguage()+
                    "/"+parent.getDefaultTheme());
        setStateVar("webmail version",parent.getVersion());
        setStateVar("operating system",System.getProperty("os.name")+" "+
                    System.getProperty("os.version")+"/"+System.getProperty("os.arch"));
        setStateVar("java virtual machine",System.getProperty("java.vendor")+" "+
                    System.getProperty("java.vm.name")+" "+System.getProperty("java.version"));
    }

    public void update() {
        // Insert the sysdata and userdata objects into the usermodel tree
        try {
            NodeList nl=root.getElementsByTagName("SYSDATA");
            root.getDocumentElement().replaceChild(root.importNode(sysdata,true),nl.item(0));
        } catch(ArrayIndexOutOfBoundsException ex) {
            System.err.println("The WebMail GenericModel template file didn't contain a SYSDATA tag.");
        } catch(DOMException ex) {
            System.err.println("Something went wrong with the XML generic model.");
        }
    }

    /**
     * Create a unique ID.
     * Important: This returns a new Unique ID within this session.
     * It should be used to generate IDs for Folders and messages so that they can be easily referenced
     */
    public String getNextID() {
        return Long.toHexString(++current_id).toUpperCase();
    }

    public synchronized void setException(Exception ex) {
        Element exception=root.createElement("EXCEPTION");
        Element ex_message=root.createElement("EX_MESSAGE");
        Element ex_stacktrace=root.createElement("EX_STACKTRACE");
        exception.appendChild(ex_message);
        exception.appendChild(ex_stacktrace);

        Text msg=root.createTextNode(ex.getMessage());
        ex_message.appendChild(msg);

        String my_stack="";
        CharArrayWriter cstream=new CharArrayWriter();
        ex.printStackTrace(new PrintWriter(cstream));
        my_stack=cstream.toString();
        CDATASection stack=root.createCDATASection(my_stack);
        ex_stacktrace.appendChild(stack);

        NodeList nl=statedata.getElementsByTagName("EXCEPTION");
        if(nl.getLength() > 0) {
            statedata.replaceChild(exception,nl.item(0));
        } else {
            statedata.appendChild(exception);
        }

        //XMLCommon.debugXML(root);
    }

    /**
     * We need to synchronize that to avoid problems, but this should be fast anyway
     */
    public synchronized void setStateVar(String name, String value) {
        Element var=XMLCommon.getElementByAttribute(statedata,"VAR","name",name);
        if(var == null) {
            var=root.createElement("VAR");
            var.setAttribute("name",name);
            statedata.appendChild(var);
        }
        var.setAttribute("value",value);
    }

    public Element createStateVar(String name, String value) {
        Element var=root.createElement("VAR");
        var.setAttribute("name",name);
        var.setAttribute("value",value);
        return var;
    }

    public void addStateVar(String name, String value) {
        Element var=root.createElement("VAR");
        var.setAttribute("name",name);
        statedata.appendChild(var);
        var.setAttribute("value",value);
    }

    /**
     * We need to synchronize that because it can cause problems with multiple threads
     */
    public synchronized void removeAllStateVars(String name) {
        NodeList nl=statedata.getElementsByTagName("VAR");

        /* This suxx: NodeList Object is changed when removing children !!!
           I will store all nodes that should be deleted in a Vector and delete them afterwards */
        int length=nl.getLength();
        Vector v=new Vector(nl.getLength());
        for(int i=0;i<length;i++) {
            if(((Element)nl.item(i)).getAttribute("name").equals(name)) {
                v.addElement(nl.item(i));
            }
        }
        Enumeration enumVar=v.elements();
        while(enumVar.hasMoreElements()) {
            Node n=(Node)enumVar.nextElement();
            statedata.removeChild(n);
        }
    }


    public String getStateVar(String name) {
        Element var=XMLCommon.getElementByAttribute(statedata,"VAR","name",name);
        if(var == null) {
            return "";
        } else {
            return var.getAttribute("value");
        }
    }


} // XMLGenericModel
