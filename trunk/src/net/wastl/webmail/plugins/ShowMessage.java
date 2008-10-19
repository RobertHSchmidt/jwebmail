/* CVS ID: $Id$ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.exceptions.*;
import java.util.*;
import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

/*
 * ShowMessage.java
 *
 * Created: Thu Sep  2 16:57:54 1999
 *
 * Copyright (C) 2000 Sebastian Schaffert
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
 * Show a message.
 *
 * provides: message show
 * requires: message list
 *
 * @author Sebastian Schaffert
 * @version
 */

public class ShowMessage implements Plugin, URLHandler {

    public static final String VERSION="1.3";
    public static final String URL="/folder/showmsg";

    StorageManager store;

    public ShowMessage() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        this.store=parent.getStorage();
    }

    public String getName() {
        return "ShowMessage";
    }

    public String getDescription() {
        return "Display a message";
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
        UserSession session=(UserSession)sess;
        UserData user=session.getUser();
        String folderhash=header.getContent("folder-id");

        if(header.isContentSet("flag")) {
            try {
                session.setFlags(folderhash,header);
            } catch(Exception ex) {
                if(WebMailServer.getDebug()) ex.printStackTrace();
                throw new WebMailException(ex.getMessage());
            }
        }

        int nr=1;
        try {
            nr=Integer.parseInt(header.getContent("message-nr"));
        } catch(Exception e) {}
        try {
            session.getMessage(folderhash,nr);
        } catch(NoSuchFolderException e) {
            throw new DocumentNotFoundException("Could not find folder "+folderhash+"!");
        }
        return new XHTMLDocument(session.getModel(),
                                 store.getStylesheet("showmessage.xsl",
                                                     user.getPreferredLocale(),user.getTheme()));
        //return new HTMLParsedDocument(store,session,"showmessage");
    }

    public String provides() {
        return "message show";
    }

    public String requires() {
        return "message list";
    }
} // ShowMessage
