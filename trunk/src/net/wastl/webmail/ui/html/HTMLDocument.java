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


package net.wastl.webmail.ui.html;

import net.wastl.webmail.server.Storage;
import java.util.*;

/*
 * HTMLDocument.java
 *
 * Created: Wed Feb  3 16:13:20 1999
 */

/**
 * WebMail's class for representing HTML Documents.
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */
public class HTMLDocument {

    protected String content;
    protected HTMLHeader header;

    protected Hashtable httpheaders;

    protected int returncode=200;
    protected String returnstatus="OK";

    public HTMLDocument() {
    }

    public HTMLDocument(String title, String content) {
                header=new HTMLHeader(title);
                this.content=content;
    }

    public HTMLDocument(String title, String cont, String basepath) {
                this(title,cont);

//              try {
//                      RE regexp2=new RE("<<BASE>>",RE.REG_ICASE & RE.REG_MULTILINE);
//                      content=regexp2.substituteAll(content,basepath);
//              } catch(Exception e) {
//                      e.printStackTrace();
//              }
    }

//     public HTMLDocument(String title,Storage loader, String docname) {
//              this(title,loader.getTextFile(loader.getStringResource(docname,Locale.getDefault()),Locale.getDefault()));
//     }

//     public HTMLDocument(String title,Storage loader, String docname, String basepath) {
//              this(title,loader.getTextFile(loader.getStringResource(docname,Locale.getDefault()),Locale.getDefault()),basepath);
//     }

    public void addHTTPHeader(String key, String value) {
                if(httpheaders==null) {
                        httpheaders=new Hashtable(5);
                }
                httpheaders.put(key,value);
    }

    public Enumeration getHTTPHeaderKeys() {
                return httpheaders.keys();
    }

    public String getHTTPHeader(String key) {
                return (String)httpheaders.get(key);
    }

    public boolean hasHTTPHeader() {
                return (httpheaders!=null) && !httpheaders.isEmpty();
    }

    public int getReturnCode() {
                return returncode;
    }

    public String getReturnStatus() {
                return returnstatus;
    }

    public void setStatus(int code, String status) {
                returncode=code;
                returnstatus=status;
    }

    public String toString() {
                return header.toString()+"\r\n"+content;
    }

    public int length() {
                return header.toString().length()+1+content.length();
    }
} // HTMLDocument
