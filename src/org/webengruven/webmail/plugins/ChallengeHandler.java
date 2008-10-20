/** This is the URLHandler for "/challenge" It shows the user a challenge and
 *  gets their new password.
 *
 * 08/11/2000 Sebastian Schaffert: Modified to fit into WebMail 0.7.2
 * - added import net.wastl.webmail.exceptions.*;
 *
 * @author Devin Kowatch
 * @version $Revision$
 * @see net.wastl.webmail.URLHandler
 *
 * Copyright (C) 2000, Devin Kowatch
 */
/* This program is free software; you can redistribute it and/or
 * modify it under the terms of the Lesser GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 * USA.
 */


import java.util.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.xml.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;

import org.webengruven.webmail.auth.*;

public class ChallengeHandler implements Plugin, URLHandler {
    public final String VERSION="2.0";
    public final String URL="/challenge";

    private Storage store;

        public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL, this);
        storage = parent.getStorage();
        }

        public String getVersion() {
        return VERSION;
        }

        public String provides() {
        return "Authentication Challenge";
        }

    /* XXX Not sure what, if anything, this should return */
        public String requires() {
        return "";
        }

        public String getURL() {
        return URL;
        }

        public String getName() {
                return "ChallengeHandler";
        }

        public String getDescription() {
                return "This URLHandler will show the user a challenge and allow them"
         + "to respond to it";
        }

        public HTMLDocument handleURL(String subURL, HTTPSession sess,
        HTTPRequestHeader h) throws WebMailException
        {
        XMLGenericModel model = storage.createXMLGenericModel();
        HTMLDocument content;
        XMLUserData ud;
        CRAuthDisplayMngr adm;
        String chal_file;

        ud = storage.getUserData(h.getContent("login"), h.getContent("vdom"),"",false);

        try {
            adm=(CRAuthDisplayMngr)storage.getAuthenticator().getAuthDisplayMngr();
            adm.setChallengeScreenVars(ud, model);
            chal_file = adm.getChallengeScreenFile();
        } catch (ClassCastException e) {
            throw new WebMailException(
             "Trying to handle /challenge for a non CRAuthenticator");
        } catch (Exception e) {
            throw new WebMailException(e.toString());
        }

        model.setStateVar("login", h.getContent("login"));
        model.setStateVar("vdom", h.getContent("vdom"));

        content = new XHTMLDocument(model.getRoot(), storage.getStylesheet(
         chal_file, Locale.getDefault(), "default"));

        return content;
    }

    private Storage storage;


} /* END class ChallengeHandler */


