/* CVS ID: $Id$ */
import java.util.*;

import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.HTTPRequestHeader;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.exceptions.*;

/*
 * HTMLContentBar.java
 *
 * Created: Wed Sep  1 13:08:11 1999
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
 * The content bar on the left.
 *
 * provides: content bar
 * requires:
 *
 * @author Sebastian Schaffert
 * @version
 */

public class NavBar implements Plugin, URLHandler {

    public static final String VERSION="2.0";
    public static final String URL="/content";

    String template;
    String bar;
    Storage store;

    public NavBar() {
    }


    public void register(WebMailServer parent) {
        this.store=parent.getStorage();
        parent.getURLHandler().registerHandler(URL,this);
    }

    public String getVersion() {
        return VERSION;
    }

    public String getName() {
        return "ContentBar";
    }

    public String getDescription() {
        return "This is the content-bar on the left frame in the mailbox window. "+
            "ContentProviders register with this content-bar to have a link and an icon added.";
    }

    public String getURL() {
        return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        if(session == null) {
            throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
        }
        WebMailSession sess=(WebMailSession)session;
        UserData user=sess.getUser();
        return new XHTMLDocument(session.getModel(),store.getStylesheet("navbar.xsl",user.getPreferredLocale(),user.getTheme()));
    }

    public String provides() {
        return "content bar";
    }

    public String requires() {
        return "";
    }

} // HMTLContentBar
