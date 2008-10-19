/* CVS ID: $Id$ */

import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;

import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

import java.util.*;

/**
 * ErrorHandler.java
 *
 * Created: Wed Jul 19 2000
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
 * This URLHandler handles error messages.
 *
 *
 * @author Sebastian Schaffert
 * @version
 */

public class ErrorHandler implements Plugin, URLHandler {
        
    public static final String VERSION="1.00";
    public static final String URL="/error";

    WebMailServer parent;

    StorageManager store;

    public ErrorHandler() {
	
    }

    public void register(WebMailServer parent) {
	parent.getURLHandler().registerHandler(URL,this);
	this.parent=parent;
	store=parent.getStorage();
    }

    public String getName() {
	return "ErrorHandler";
    }

    public String getDescription() {
	return "Handle error messages";
    }

    public String getVersion() {
	return VERSION;
    }

    public String getURL() {
	return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
	String theme=WebMailServer.getDefaultTheme();
	Locale locale=WebMailServer.getDefaultLocale();
	if(session instanceof UserSession) {
	    UserSession sess=(UserSession)session;
	    theme=sess.getUser().getTheme();
	    locale=sess.getUser().getPreferredLocale();
	}
	return new XHTMLDocument(session.getModel(),store.getStylesheet("error.xsl",locale,theme));
    }
    
    public String provides() {
	return "about";
    }

    public String requires() {
	return "";
    }
    
} // About
