/* CVS ID: $Id$ */

import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;

import org.webengruven.webmail.auth.*;


/*
 * UserSetup.java
 *
 * Created: Wed Sep  8 14:07:36 1999
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
 * Show a form to change user settings and actually perform them.
 *
 * provides: user setup
 * requires: content bar
 *
 * @author Sebastian Schaffert
 * @version
 */
/* 9/24/2000 devink - changed for new challenge/response auth */

public class UserSetup implements Plugin, URLHandler {
    	
    public static final String VERSION="1.3";
    public static final String URL="/setup";

    Storage store;

    public UserSetup() {
		
    }

    public void register(WebMailServer parent) {
	parent.getURLHandler().registerHandler(URL,this);
	store=parent.getStorage();
    }

    public String getName() {
	return "UserSetup";
    }

    public String getDescription() {
	return "Change a users settings.";
    }	

    public String getVersion() {
	return VERSION;
    }

    public String getURL() {
	return URL;
    }

    public HTMLDocument handleURL(String suburl,HTTPSession sess,
     HTTPRequestHeader header) throws WebMailException 
    {  
	if(sess == null) {
	    throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
	}
	WebMailSession session=(WebMailSession)sess;
	UserData user=session.getUser();
	HTMLDocument content;
    AuthDisplayMngr adm=store.getAuthenticator().getAuthDisplayMngr();

    /* 9/24/2000 devink - set up password change stuff */
    adm.setPassChangeVars(user, session.getUserModel());
    session.getUserModel().setStateVar(
        "pass change tmpl", adm.getPassChangeTmpl());

	session.refreshFolderInformation();

	if(suburl.startsWith("/submit")) {
	    try {
		session.changeSetup(header);
		content=new XHTMLDocument(session.getModel(),store.getStylesheet("setup.xsl",user.getPreferredLocale(),user.getTheme()));
	    } catch(InvalidPasswordException e) {
		throw new DocumentNotFoundException("The two passwords did not match");
	    }
	} else {
	    content=new XHTMLDocument(session.getModel(),store.getStylesheet("setup.xsl",user.getPreferredLocale(),user.getTheme()));
	}   
	return content;
    }

    public String provides() {
	return "user setup";
    }

    public String requires() {
	return "content bar";
    }
} // UserSetup
