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

import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

/**
 * WebMailTitle.java
 *
 * Created: Wed Sep  1 16:34:55 1999
 */
/**
 * Show WebMail title.
 *
 * provides: title
 *
 * @author Sebastian Schaffert
 * @version
 */

public class WebMailTitle implements Plugin, URLHandler {

    public static final String VERSION="1.1";
    public static final String URL="/title";

    StorageManager store;

    public WebMailTitle() {

    }
    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        store=parent.getStorage();
    }

    public String getName() {
        return "WebMailTitle";
    }

    public String getDescription() {
        return "The WebMail title-frame plugin";
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
        //return new HTMLParsedDocument(store,session,"title");
        UserData user=((UserSession)session).getUser();
        return new XHTMLDocument(session.getModel(),store.getStylesheet("title.xsl",user.getPreferredLocale(),user.getTheme()));
    }

    public String provides() {
        return "title";
    }

    public String requires() {
        return "";
    }

} // WebMailTitle
