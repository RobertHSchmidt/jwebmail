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

import org.webengruven.webmail.auth.*;


/*
 * UserSetup.java
 *
 * Created: Wed Sep  8 14:07:36 1999
 */
/**
 * Show a form to change user settings and actually perform them.
 *
 * provides: user setup
 * requires: content bar
 *
 * @author Sebastian Schaffert
 * @version
 */
/* 9/24/2000 devink - changed for new challenge/response auth */

public class UserSetup implements Plugin, URLHandler {

    public static final String VERSION="1.3";
    public static final String URL="/setup";

    StorageManager store;

    public UserSetup() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        store=parent.getStorage();
    }

    public String getName() {
        return "UserSetup";
    }

    public String getDescription() {
        return "Change a users settings.";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }

    public HTMLDocument handleURL(String suburl,HTTPSession sess,
     HTTPRequestHeader header) throws WebMailException
    {
        if(sess == null) {
            throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
        }
        UserSession session=(UserSession)sess;
        UserData user=session.getUser();
        HTMLDocument content;
    AuthDisplayMngr adm=store.getAuthenticator().getAuthDisplayMngr();

    /* 9/24/2000 devink - set up password change stuff */
    adm.setPassChangeVars(user, session.getUserModel());
    session.getUserModel().setStateVar(
        "pass change tmpl", adm.getPassChangeTmpl());

        session.refreshFolderInformation();

        if(suburl.startsWith("/submit")) {
            try {
                session.changeSetup(header);
                content=new XHTMLDocument(session.getModel(),store.getStylesheet("setup.xsl",user.getPreferredLocale(),user.getTheme()));
            } catch(InvalidPasswordException e) {
                throw new DocumentNotFoundException("The two passwords did not match");
            }
        } else {
            content=new XHTMLDocument(session.getModel(),store.getStylesheet("setup.xsl",user.getPreferredLocale(),user.getTheme()));
        }
        return content;
    }

    public String provides() {
        return "user setup";
    }

    public String requires() {
        return "content bar";
    }
} // UserSetup
