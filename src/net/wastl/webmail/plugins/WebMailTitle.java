/* CVS ID: $Id$ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;

/**
 * WebMailTitle.java
 *
 * Created: Wed Sep  1 16:34:55 1999
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
 * Show WebMail title.
 *
 * provides: title
 *
 * @author Sebastian Schaffert
 * @version
 */

public class WebMailTitle implements Plugin, URLHandler {
    
    public static final String VERSION="1.1";
    public static final String URL="/title";

    Storage store;

    public WebMailTitle() {
	
    }
    public void register(WebMailServer parent) {
	parent.getURLHandler().registerHandler(URL,this);
	store=parent.getStorage();
    }

    public String getName() {
	return "WebMailTitle";
    }

    public String getDescription() {
	return "The WebMail title-frame plugin";
    }

    public String getVersion() {
	return VERSION;
    }

    public String getURL() {
	return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
	if(session == null) {
	    throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
	}
	//return new HTMLParsedDocument(store,session,"title");
	UserData user=((WebMailSession)session).getUser();
	return new XHTMLDocument(session.getModel(),store.getStylesheet("title.xsl",user.getPreferredLocale(),user.getTheme()));
    }

    public String provides() {
	return "title";
    }

    public String requires() {
	return "";
    }
    
} // WebMailTitle
