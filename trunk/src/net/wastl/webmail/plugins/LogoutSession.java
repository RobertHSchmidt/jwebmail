/* CVS ID: $Id$ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import java.util.Locale;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

/*
 * LogoutSession.java
 *
 * Created: Wed Sep  1 16:46:34 1999
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
 * Log out a user.
 *
 * provides: logout
 * requires: content bar
 *
 * @author Sebastian Schaffert
 * @version
 */

public class LogoutSession implements Plugin, URLHandler {
    
    public static final String VERSION="1.3";
    public static final String URL="/logout";

    StorageManager store;

    public LogoutSession() {
        
    }
 
    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        //parent.getContentBar().registerContentItem(this);
        store=parent.getStorage();
    }

    public String getName() {
        return "LogoutSession";
    }

    public String getDescription() {
        return "ContentProvider plugin that closes an active WebMail session.";
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
        UserData user=((UserSession)session).getUser();
        HTMLDocument content=new XHTMLDocument(session.getModel(),store.getStylesheet("logout.xsl",user.getPreferredLocale(),user.getTheme()));
        if(!session.isLoggedOut()) {
            session.logout();
        }
        return content;
    }

    public String provides() {
        return "logout";
    }

    public String requires() {
        return "content bar";
    }
} // LogoutSession
