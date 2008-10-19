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


package net.wastl.webmail.server;

import java.io.*;
import java.util.regex.*;
import net.wastl.webmail.exceptions.*;

/**
 * SystemCheck.java
 *
 * Created: Tue Aug 31 15:40:57 1999
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
//                  String s=regexp.substituteAll(line,"<!DOCTYPE SYSDATA SYSTEM \"file://"+
//                                                parent.getProperty("webmail.xml.path")+
//                                                System.getProperty("file.separator")+
//                                                "sysdata.dtd"+"\">");
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
            try {
                File f=new File(parent.getProperty(property));
                parent.setProperty(property,f.getCanonicalPath());
            } catch(IOException ex) {
                throw new WebMailException(ex.getMessage());
            }
        }
    }

} // SystemCheck
