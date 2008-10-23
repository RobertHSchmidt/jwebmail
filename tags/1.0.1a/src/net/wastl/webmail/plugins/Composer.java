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


import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.ui.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.exceptions.*;

/*
 * Composer.java
 *
 * Created: Tue Sep  7 12:46:08 1999
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

    Storage store;

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

        WebMailSession session=(WebMailSession)sess;
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
            mode += WebMailSession.GETMESSAGE_MODE_REPLY;
        }
        if(header.isContentSet("forward")) {
            mode += WebMailSession.GETMESSAGE_MODE_FORWARD;
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
