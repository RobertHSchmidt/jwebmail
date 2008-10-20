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

import java.util.*;

/**
 * ErrorHandler.java
 *
 * Created: Wed Jul 19 2000
 */
/**
 * This URLHandler handles error messages.
 *
 *
 * @author Sebastian Schaffert
 * @version
 */

public class ErrorHandler implements Plugin, URLHandler {

    public static final String VERSION="1.00";
    public static final String URL="/error";

    WebMailServer parent;

    StorageManager store;

    public ErrorHandler() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        this.parent=parent;
        store=parent.getStorage();
    }

    public String getName() {
        return "ErrorHandler";
    }

    public String getDescription() {
        return "Handle error messages";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        String theme=WebMailServer.getDefaultTheme();
        Locale locale=WebMailServer.getDefaultLocale();
        if(session instanceof UserSession) {
            UserSession sess=(UserSession)session;
            theme=sess.getUser().getTheme();
            locale=sess.getUser().getPreferredLocale();
        }
        return new XHTMLDocument(session.getModel(),store.getStylesheet("error.xsl",locale,theme));
    }

    public String provides() {
        return "about";
    }

    public String requires() {
        return "";
    }

} // About
