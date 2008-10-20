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
import java.util.Locale;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

/*
 * LogoutSession.java
 *
 * Created: Wed Sep  1 16:46:34 1999
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
