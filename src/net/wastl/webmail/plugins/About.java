import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.exceptions.*;
import java.util.Enumeration;

/**
 * About.java
 *
 * Created: Wed Sep  1 17:16:06 1999
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

public class About implements Plugin, URLHandler {
        
    public static final String VERSION="1.00";
    public static final String URL="/about";

    WebMailServer parent;

    public About() {
	
    }

    public void register(WebMailServer parent) {
	parent.getURLHandler().registerHandler(URL,this);
	this.parent=parent;
    }

    public String getName() {
	return "About";
    }

    public String getDescription() {
	return "About WebMail";
    }

    public String getVersion() {
	return VERSION;
    }

    public String getURL() {
	return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws DocumentNotFoundException {
	String content="<BODY BGCOLOR=WHITE><CENTER><H1>About WebMail</H1></CENTER><BR>";
	content+="<H3>Copyright</H3><BR>WebMail is (c)1999 by Sebastian Schaffert, wastl@wastl.net "
	    +"and is distributed under the terms of the <A HREF=\"<<BASE>>/license/gnu\">GNU Lesser General Public License</A> "
	    +".<BR><P><HR><P>"
	    +"<H3>Registered Plugins</H3><BR><UL>";
	Enumeration e=parent.getPluginHandler().getPlugins();
	while(e.hasMoreElements()) {
	    Plugin p=(Plugin)e.nextElement();
	    content+="<LI><B>"+p.getName()+"</B> (v"+p.getVersion()+"): "+p.getDescription()+"</LI>";
	}
	//System.gc();
	content+="</UL><P><HR><P><H3>System Information</H3><BR><UL><LI><B>Operating System:</B> "+System.getProperty("os.name")
	    +"/"+System.getProperty("os.arch")+" "+System.getProperty("os.version")+"</LI><LI><B>Java Virtual Machine:</B> "
	    +System.getProperty("java.vm.name")+" version "+System.getProperty("java.version")+" from "
	    +System.getProperty("java.vendor")+"</LI><LI><B>Free memory for this JVM:</B> "
	    +Runtime.getRuntime().freeMemory()+" bytes</LI></UL></BODY>";
	return new HTMLDocument("About WebMail",content);
    }
    
    public String provides() {
	return "about";
    }

    public String requires() {
	return "";
    }
    
} // About
