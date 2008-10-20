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


import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;
import java.util.*;
import java.text.*;

import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

/*
 * FolderList.java
 *
 * Created: Thu Sep  2 12:59:16 1999
 */
/**
 * List the messages in a folder.
 *
 * provides: message list
 * requires: mailbox list
 *
 * @author Sebastian Schaffert
 * @version
 */

public class FolderList implements Plugin, URLHandler {

    public static final String VERSION="1.5";
    public static final String URL="/folder/list";


    StorageManager store;
    WebMailServer parent;

    public FolderList() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        this.store=parent.getStorage();
        this.parent=parent;
    }

    public String getName() {
        return "FolderList";
    }

    public String getDescription() {
        return "List the contents of a folder";
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
        String hashcode=header.getContent("folder-id");

        if(header.isContentSet("flag")) {
            try {
                session.setFlags(hashcode,header);
            } catch(Exception ex) {
                if(WebMailServer.getDebug()) ex.printStackTrace();
                throw new WebMailException(ex.getMessage());
            }
        }

        int nr=1;
        try {
            nr=Integer.parseInt(header.getContent("part"));
        } catch(Exception e) {}
        try {
            session.createMessageList(hashcode,nr);
        } catch(NoSuchFolderException e) {
            throw new DocumentNotFoundException("Could not find folder "+hashcode+"!");
        }
        return new XHTMLDocument(session.getModel(),
                                 store.getStylesheet("messagelist.xsl",
                                                     user.getPreferredLocale(),user.getTheme()));
    }

    public String provides() {
        return "message list";
    }

    public String requires() {
        return "mailbox list";
    }
} // FolderList
