/* CVS ID: $Id$ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
// import net.wastl.webmail.ui.ContentProvider;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;

/*
 * MailboxList.java
 *
 * Created: Thu Sep  2 12:00:38 1999
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
 * Show a list of user mailboxes.
 *
 * provides: mailbox list
 * requires: content bar
 *
 * Created: Thu Sep  2 12:00:38 1999
 *
 * @author Sebastian Schaffert
 * @version
 */

public class MailboxList implements Plugin, URLHandler {

    public static final String VERSION="1.3";
    public static final String URL="/mailbox";

    Storage store;

    public MailboxList() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        //parent.getContentBar().registerContentItem(this);
        store=parent.getStorage();
    }

    public String getName() {
        return "MailboxList";
    }

    public String getDescription() {
        return "This ContentProvider shows a list of all folders and links to the FolderList URLHandler.";
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
        /* If the user requests the folder overview, try to fetch new information */
        /* Do so only, if this is forced, to save the time! */
        if(header.isContentSet("force-refresh")) {
            session.refreshFolderInformation(true);
        }
        content=new XHTMLDocument(session.getModel(),store.getStylesheet("mailbox.xsl",user.getPreferredLocale(),user.getTheme()));
        return content;
    }

    public String provides() {
        return "mailbox list";
    }

    public String requires() {
        return "content bar";
    }
} // MailboxList
