/* CVS ID: $Id$ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;

import java.util.Enumeration;

/*
 * FolderSetup.java
 *
 * Created: Tue Sep  7 18:45:11 1999
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
 * Show the folder setup form and handle changes (except deletion).
 *
 * provides: folder setup
 * requires: content bar
 *
 * Created: Tue Sep  7 18:45:11 1999
 *
 * @author Sebastian Schaffert
 * @version
 */

public class FolderSetup implements Plugin, URLHandler {
    
	
    public static final String VERSION="1.3";
    public static final String URL="/folder/setup";

    Storage store;

    public FolderSetup() {
	
    }

    public void register(WebMailServer parent) {
	parent.getURLHandler().registerHandler(URL,this);
	store=parent.getStorage();
    }

    public String getName() {
	return "FolderSetup";
    }

    public String getDescription() {
	return "This ContentProvider manages a users folder setup.";
    }	

    public String getVersion() {
	return VERSION;
    }


    public String getURL() {
	return URL;
    }

    public HTMLDocument handleURL(String suburl, HTTPSession sess, HTTPRequestHeader header) throws WebMailException {
	if(sess == null) {
	    throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
	}
	WebMailSession session=(WebMailSession)sess;
	UserData user=session.getUser();
	HTMLDocument content;
	
	/* The user requested to change his *mailbox* settings */
	if(header.isContentSet("method") && header.getContent("method").equals("mailbox")) {
	    if(header.isContentSet("remove")) {
		session.removeMailbox(header.getContent("remove"));
	    } else if(header.isContentSet("add")) {
		
		try {
		    session.addMailbox(header.getContent("mbox_name"),
				       header.getContent("mbox_proto"),
				       header.getContent("mbox_host"),
				       header.getContent("mbox_login"),
				       header.getContent("mbox_password"));
		} catch(Exception ex) {
		    throw new WebMailException(ex);
		}
	    }	    

	    session.refreshFolderInformation(false);

	    content=new XHTMLDocument(session.getModel(),
				      store.getStylesheet("foldersetup-mailbox.xsl",
							  user.getPreferredLocale(),user.getTheme()));

	    /* The user requested to change subfolders in a mailbox */
	} else if(header.isContentSet("method") && header.getContent("method").equals("folder")) {
	    if(header.isContentSet("remove")) {
		try {
		    session.removeFolder(header.getContent("remove"),header.isContentSet("recurse"));
		} catch(Exception ex) {
		    ex.printStackTrace();
		    throw new WebMailException("Error while removing folders");
		}
	    } else if(header.isContentSet("addto")) {
		String type=header.getContent("folder_type");
		boolean holds_folders=false,holds_messages=false;
		if(type.equals("msgs")) {
		    holds_messages=true;
		} else if(type.equals("folder")) {
		    holds_folders=true;
		} else if(type.equals("msgfolder")) {
		    holds_folders=true; holds_messages=true;
		}
		try {
		    session.addFolder(header.getContent("addto"),
				      header.getContent("folder_name"),
				      holds_messages,
				      holds_folders);
		    
		} catch(Exception ex) {
		    ex.printStackTrace();
		    throw new WebMailException("Error while adding folders");
		}
	    } else if(header.isContentSet("hide")) {
		session.unsubscribeFolder(header.getContent("hide"));
	    } else if(header.isContentSet("unhide")) {
		session.subscribeFolder(header.getContent("unhide"));
	    }

	    // We want to see all folders in the folder overview
	    session.refreshFolderInformation(false);

	    content=new XHTMLDocument(session.getModel(),
				      store.getStylesheet("foldersetup-folders.xsl",
							  user.getPreferredLocale(),user.getTheme()));
	    // but we want only to see some in the mailbox overview
	    session.refreshFolderInformation(true);
	} else if(header.isContentSet("method") && header.getContent("method").equals("folderadd")) {
	    session.setAddToFolder(header.getContent("addto"));
	    content=new XHTMLDocument(session.getModel(),
				      store.getStylesheet("foldersetup-folders-add.xsl",
							  user.getPreferredLocale(),user.getTheme()));
	} else {	    

	    content=new XHTMLDocument(session.getModel(),
				      store.getStylesheet("foldersetup.xsl",
							  user.getPreferredLocale(),user.getTheme()));
	}
	return content;
    }
    
    public String provides() {
	return "folder setup";
    }

    public String requires() {
	return "content bar";
    }
} // FolderSetup
