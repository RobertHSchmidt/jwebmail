/* $Id$ */
package net.wastl.webmail.ui.html;

import java.util.*;

/*
 * HTMLDocument.java
 *
 * Created: Wed Feb  3 16:13:20 1999
 *
 * Copyright (C) 1999-2000 Sebastian Schaffert
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

    }


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
