/* CVS ID: $Id$ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
// import net.wastl.webmail.ui.ContentProvider;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

import org.w3c.dom.*;

import javax.xml.parsers.*;

/*
 * WebMailHelp.java
 *
 * Created: Wed Sep  1 16:23:14 1999
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

    StorageManager store;

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
        UserData user=((UserSession)session).getUser();

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

        if(header.isContentSet("helptopic") && session instanceof UserSession) {
            ((UserSession)session).getUserModel().setStateVar("helptopic",header.getContent("helptopic"));
        }


        HTMLDocument retdoc=new XHTMLDocument(session.getModel(),store.getStylesheet("help.xsl",user.getPreferredLocale(),user.getTheme()));

        /* Here we remove the help document from the model */
        session.getModel().getDocumentElement().removeChild(n);
        /* Remove the indicator for a specific help topic */
        if(header.isContentSet("helptopic") && session instanceof UserSession) {
            ((UserSession)session).getUserModel().removeAllStateVars("helptopic");
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
