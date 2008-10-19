/* $Id$ */
package net.wastl.webmail.server.http;

import java.io.*;
import java.util.*;
import java.text.*;
import net.wastl.webmail.server.WebMailServer;
import net.wastl.webmail.debug.ErrorHandler;
import net.wastl.webmail.exceptions.*;

/**
 * HTTPHeader.java
 *
 *
 * Created: Tue Feb  2 15:25:48 1999
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */

public class HTTPResponseHeader  {

    private String status;
    private int response_code;
    private String http_version="HTTP/1.1";

    private String response_line;

    private Hashtable headers;

    public HTTPResponseHeader(int response_code,String status) {
	headers=new Hashtable();
	this.response_code=response_code;
	this.status=status;

	response_line=http_version+" "+response_code+" "+status;

	putHeader("Server","JWebMail/"+WebMailServer.VERSION);
	SimpleTimeZone tz=new SimpleTimeZone(0,"GMT");
	SimpleDateFormat df=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", new Locale("en","UK"));
	df.setTimeZone(tz);
	String now=df.format(new Date());
	String now_plus_5=df.format(new Date(System.currentTimeMillis()+300000));
	putHeader("Date",now);
	putHeader("Expires",now_plus_5);
	putHeader("Pragma","no-cache");
	putHeader("Cache-Control","must-revalidate");
	putHeader("Allow","GET, POST");
    }

    public String getResponseLine() {
	return response_line;
    }

    public void putHeader(String key,String value) {
	headers.remove(key);
	headers.put(key,value);
    }

    public String getHeader(String t) {
	return (String)headers.get(t);
    }

    public void removeHeader(String key) {
	headers.remove(key);
    }

    public Enumeration getHeaderKeys() {
	return headers.keys();
    }

    public String toString() {
	String s=response_line+"\r\n";
	Enumeration e=headers.keys();
	while(e.hasMoreElements()) {
	    String h=(String)e.nextElement();
	    s+=h+": "+headers.get(h)+"\r\n";
	}
	s+="\r\n";
	return s;
    }
    
} // HTTPHeader
