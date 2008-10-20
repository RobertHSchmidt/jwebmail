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


package net.wastl.webmail.server;

import java.util.*;
import net.wastl.webmail.logger.Logger;
import net.wastl.webmail.xml.*;
import net.wastl.webmail.ui.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.session.user.UserSession;

import org.webengruven.webmail.auth.AuthDisplayMngr;

import javax.servlet.ServletException;


/*
 * ToplevelURLHandler.java
 *
 * Created: Tue Aug 31 17:20:29 1999
 */
/**
 * Handle URLs. Give them to the appropriate Plugins/Program parts
 *
 * Created: Tue Aug 31 17:20:29 1999
 *
 * @author Sebastian Schaffert
 * @version
 */
/* 9/24/2000 devink -- changed for challenge/response authentication */

public class ToplevelURLHandler implements URLHandler {

    WebMailServer parent;
    //Hashtable urlhandlers;
    URLHandlerTree urlhandlers;

    public ToplevelURLHandler(WebMailServer parent) {
        System.err.println("- Initializing WebMail URL Handler ... done.");
        urlhandlers=new URLHandlerTree("/");
        urlhandlers.addHandler("/",this);
        this.parent=parent;
    }

    public void registerHandler(String url, URLHandler handler) {
        //urlhandlers.put(url,handler);
        urlhandlers.addHandler(url,handler);
        //System.err.println("Tree changed: "+urlhandlers.toString());
    }

    public String getURL() {
        return "/";
    }

    public String getName() {
        return "TopLevelURLHandler";
    }

    public String getDescription() {
        return "";
    }

    public HTMLDocument handleException(Exception ex, HTTPSession session, HTTPRequestHeader header) throws ServletException {
        try {
            session.setException(ex);
            String theme=parent.getDefaultTheme();
            Locale locale=Locale.getDefault();
            if(session instanceof UserSession) {
                UserSession sess=(UserSession)session;
                theme=sess.getUser().getTheme();
                locale=sess.getUser().getPreferredLocale();
            }
            return new XHTMLDocument(session.getModel(),parent.getStorage().getStylesheet("error.xsl",locale,theme));
        } catch(Exception myex) {
            parent.getLogger().log(Logger.LOG_ERR,"Error while handling exception:");
            parent.getLogger().log(Logger.LOG_ERR,myex);
            parent.getLogger().log(Logger.LOG_ERR,"The handled exception was:");
            parent.getLogger().log(Logger.LOG_ERR,ex);
            throw new ServletException(myex);
        }
    }

    public HTMLDocument handleURL(String url, HTTPSession session, HTTPRequestHeader header) throws WebMailException, ServletException {

        HTMLDocument content;

        if(url.equals("/")) {
            //content=new HTMLLoginScreen(parent,parent.getStorage(),false);
            XMLGenericModel model=parent.getStorage().createXMLGenericModel();

            AuthDisplayMngr adm = parent.getStorage().getAuthenticator().getAuthDisplayMngr();

            if(header.isContentSet("login")) {
                model.setStateVar("invalid password","yes");
            }

            // Let the authenticator setup the loginscreen
            adm.setLoginScreenVars(model);

                // Modified by exce, start.
                /**
                 * Show login screen depending on WebMailServer's default locale.
                 */
                /*
            content = new XHTMLDocument(model.getRoot(),
                                        parent.getStorage().getStylesheet(adm.getLoginScreenFile(),
                                                                          Locale.getDefault(),"default"));
                */
            content = new XHTMLDocument(model.getRoot(),
                                        parent.getStorage().getStylesheet(adm.getLoginScreenFile(),
                                                                                parent.getDefaultLocale(),parent.getProperty("webmail.default.theme")));
                // Modified by exce, end.
        } else if(url.equals("/login")) {

            UserSession sess=(UserSession)session;
            UserData user=sess.getUser();
            content=new XHTMLDocument(session.getModel(),parent.getStorage().getStylesheet("login.xsl",user.getPreferredLocale(),user.getTheme()));
        } else {
            /* Let the plugins handle it */

            URLHandler uh=urlhandlers.getHandler(url);

            if(uh != null && uh != this) {
                // System.err.println("Handler: "+uh.getName()+" ("+uh.getURL()+")");
                String suburl=url.substring(uh.getURL().length(),url.length());
                content=uh.handleURL(suburl,session,header);
            } else {
                throw new DocumentNotFoundException(url + " was not found on this server");
            }
        }
        return content;
    }

} // URLHandler
