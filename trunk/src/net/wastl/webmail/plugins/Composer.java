/* CVS ID: $Id$ */
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.ui.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.exceptions.*;

import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

/*
 * Composer.java
 *
 * Created: Tue Sep  7 12:46:08 1999
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
 * Compose a message. This plugin will show the compose form and fill in
 * the necessary fields if this is a continued message
 *
 * @see FileAttacher
 *
 * provides: composer
 * requires: content bar
 *
 * Created: Tue Sep  7 12:46:08 1999
 *
 * @author Sebastian Schaffert
 * @version
 */

public class Composer implements Plugin, URLHandler {

    public static final String VERSION="1.3";
    public static final String URL="/compose";

    StorageManager store;

    public Composer() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        store=parent.getStorage();
    }

    public String getName() {
        return "Composer";
    }

    public String getDescription() {
        return "This plugin handles the composition of a message.";
    }

    public String getVersion() {
        return VERSION;
    }


    public String getURL() {
        return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession sess, HTTPRequestHeader header) throws WebMailException {
        HTMLDocument content;

        UserSession session=(UserSession)sess;
        UserData user=session.getUser();

        /* We were not continuing to edit a message, so we should delete the current draft! */
        if(!header.isContentSet("continue")) {

            session.clearAttachments();
            session.clearWork();

            session.prepareCompose();

            session.setEnv();
        }

        int mode=0;

        if(header.isContentSet("reply")) {
            mode += UserSession.GETMESSAGE_MODE_REPLY;
        }
        if(header.isContentSet("forward")) {
            mode += UserSession.GETMESSAGE_MODE_FORWARD;
        }
        if(mode>0) {
            if(!header.isContentSet("folder-id") || !header.isContentSet("message-nr")) {
                /// XXX error handler TBD here!
                System.err.println("Error: no folder-id or message-nr in request for reply or forward!");
            } else {
                String folderhash=header.getContent("folder-id");
                int msgnr=0;
                try {
                    msgnr=Integer.parseInt(header.getContent("message-nr"));
                } catch(NumberFormatException ex) {
                    /// XXX error handler TBD here!
                    System.err.println("MSGNR wrong in forward/reply!");
                }

                try {
                    session.getMessage(folderhash,msgnr,mode);
                } catch(NoSuchFolderException ex) {
                    /// XXX error handler TBD here!
                }
            }
        }

        return new XHTMLDocument(session.getModel(),
                                 store.getStylesheet("compose.xsl",
                                                     user.getPreferredLocale(),user.getTheme()));
    }


    public String provides() {
        return "composer";
    }

    public String requires() {
        return "content bar";
    }
} // Composer
