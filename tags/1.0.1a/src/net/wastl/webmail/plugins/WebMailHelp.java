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
// import net.wastl.webmail.ui.ContentProvider;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.exceptions.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

/*
 * WebMailHelp.java
 *
 * Created: Wed Sep  1 16:23:14 1999
 */
/**
 * Show WebMail help file
 *
 * provides: help
 * requires: content bar
 *
 * @author Sebastian Schaffert
 * @version
 */

public class WebMailHelp implements Plugin, URLHandler {

    public static final String VERSION="2.0";
    public static final String URL="/help";

    ExpireableCache cache;

    Storage store;

    public WebMailHelp() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        cache=new ExpireableCache(20,(float).9);
        store=parent.getStorage();
    }

    public String getName() {
        return "WebMailHelp";
    }

    public String getDescription() {
        return "This is the WebMail help content-provider.";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }

    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        UserData user=((WebMailSession)session).getUser();

        Document helpdoc=(Document)cache.get(user.getPreferredLocale().getLanguage()+"/"+user.getTheme());

        if(helpdoc == null) {
            String helpdocpath="file://"+store.getBasePath(user.getPreferredLocale(),user.getTheme())+"help.xml";

            try {
                DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                helpdoc=parser.parse(helpdocpath);
            } catch(Exception ex) {
                ex.printStackTrace();
                throw new WebMailException("Could not parse "+helpdocpath);
            }

            cache.put(user.getPreferredLocale().getLanguage()+"/"+user.getTheme(),helpdoc);
        }

        /* Unfortunately we can't use two input documents, so we will temporarily insert the help document
           into the user's model */
        Node n=session.getModel().importNode(helpdoc.getDocumentElement(),true);
        session.getModel().getDocumentElement().appendChild(n);

        if(header.isContentSet("helptopic") && session instanceof WebMailSession) {
            ((WebMailSession)session).getUserModel().setStateVar("helptopic",header.getContent("helptopic"));
        }


        HTMLDocument retdoc=new XHTMLDocument(session.getModel(),store.getStylesheet("help.xsl",user.getPreferredLocale(),user.getTheme()));

        /* Here we remove the help document from the model */
        session.getModel().getDocumentElement().removeChild(n);
        /* Remove the indicator for a specific help topic */
        if(header.isContentSet("helptopic") && session instanceof WebMailSession) {
            ((WebMailSession)session).getUserModel().removeAllStateVars("helptopic");
        }


        return retdoc;
    }

    public String provides() {
        return "help";
    }

    public String requires() {
        return "content bar";
    }
} // WebMailHelp
