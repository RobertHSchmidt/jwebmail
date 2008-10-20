/* CVS ID: $Id$ */
package net.wastl.webmail.server;

import java.io.*;
import java.util.regex.*;
import net.wastl.webmail.exceptions.*;

/**
 * SystemCheck.java
 *
 * Created: Tue Aug 31 15:40:57 1999
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
 *
 *
 *
 * @author Sebastian Schaffert
 * @version
 */

public class SystemCheck  {
    
    public SystemCheck(WebMailServer parent) throws WebMailException {
	System.err.println("- Checking Java Virtual Machine ... ");
	System.err.print("  * Version: "+System.getProperty("java.version")+" ... ");

	/* Test if the Java version might cause trouble */
	if(System.getProperty("java.version").compareTo("1.4")>=0) {
	    System.err.println("ok.");
	} else {
	    System.err.println("warn. At least Java 1.4 is required for WebMail.");
	    //System.exit(1);
	}

	/* Test if the operating system is supported */
	System.err.print("  * Operating System: "+System.getProperty("os.name")+"/"+System.getProperty("os.arch")+" "+System.getProperty("os.version")+" ... ");
	if(System.getProperty("os.name").equals("SunOS") || 
	   System.getProperty("os.name").equals("Solaris") || 
	   System.getProperty("os.name").equals("Linux")) {
	    System.err.println("ok.");
	} else {
	    System.err.println("warning. WebMail was only tested\n   on Solaris, HP-UX and Linux and may cause problems on your platform.");
	}

	/* Check if we are running as root and issue a warning */
	try {
	    System.err.print("  * User name: "+System.getProperty("user.name")+" ... ");
	    if(!System.getProperty("user.name").equals("root")) {
		System.err.println("ok.");
	    } else {
		System.err.println("warning. You are running WebMail as root. This may be a potential security problem.");
	    }
	} catch(Exception ex) {
	    // Security restriction prohibit reading the username, then we do not need to
	    // check for root anyway
	}

	/* Check whether all WebMail system properties are defined */
	System.err.print("  * WebMail System Properties: ");
	//checkPathProperty(parent,"webmail.plugin.path");
	//checkPathProperty(parent,"webmail.auth.path");
	checkPathProperty(parent,"webmail.lib.path");
	checkPathProperty(parent,"webmail.template.path");
	checkPathProperty(parent,"webmail.data.path");
	checkPathProperty(parent,"webmail.xml.path");
	System.err.println("ok!");

	System.err.print("  * Setting DTD-path in webmail.xml ... ");
	File f1=new File(parent.getProperty("webmail.data.path")+System.getProperty("file.separator")+"webmail.xml");
	File f2=new File(parent.getProperty("webmail.data.path")+System.getProperty("file.separator")+"webmail.xml."+Long.toHexString(System.currentTimeMillis()));
	
	try {
	    Pattern regexp=Pattern.compile("<!DOCTYPE SYSDATA SYSTEM \".*\">");
	    BufferedReader file1=new BufferedReader(new FileReader(f1));
	    PrintWriter file2=new PrintWriter(new FileWriter(f2));
	    try {
		String line=file1.readLine();
		while(line != null) {
		    Matcher m = regexp.matcher(line);
		    String s = m.replaceAll("<!DOCTYPE SYSDATA SYSTEM \"file://"+
					    parent.getProperty("webmail.xml.path")+
					    System.getProperty("file.separator")+
					    "sysdata.dtd"+"\">");
// 		    String s=regexp.substituteAll(line,"<!DOCTYPE SYSDATA SYSTEM \"file://"+
// 						  parent.getProperty("webmail.xml.path")+
// 						  System.getProperty("file.separator")+
// 						  "sysdata.dtd"+"\">");
		    //System.err.println(s);
		    file2.println(s);
		    line=file1.readLine();
		}
	    } catch(EOFException ex) {
	    }
	    file2.close();
	    file1.close();
	} catch(Exception ex) {
	    //ex.printStackTrace();
	    //throw new WebMailException("Error parsing webmail.xml!");
	    throw new WebMailException(ex);
	}
	f2.renameTo(f1);
	System.err.println("done!");
    }
    
    protected static void checkPathProperty(WebMailServer parent,String property) throws WebMailException {
	if(parent.getProperty(property) == null ||
	   parent.getProperty(property).equals("")) {
	    System.err.println("fatal error. "+property+" not defined.");
	    throw new WebMailException();
	} else {
	    File f=new File(parent.getProperty(property));
	    parent.setProperty(property,f.getAbsolutePath());
	}
    }

} // SystemCheck
