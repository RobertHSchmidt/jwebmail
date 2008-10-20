import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;

import java.util.Locale;

/*
 * ImageHandler.java
 *
 * Created: Mon Mar 27 23:15:41 2000
 *
 * Copyright (C) 2000 Sebastian Schaffert
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
 * This plugin passes through data without doing any processing. It can be used for images
 * or other binary/text data by calling http://yourhost/mountpoint/webmail/passthrough/<file>
 *
 * @author Sebastian Schaffert
 * @version
 */

public class PassThroughPlugin implements Plugin, URLHandler  {

    public static final String VERSION="1.0";
    public static final String URL="/passthrough";

    Storage store;
    WebMailServer parent;

    public PassThroughPlugin() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        store=parent.getStorage();
        this.parent=parent;
    }

    public String getName() {
        return "PassThroughPlugin";
    }

    public String getDescription() {
        return "Pass though any kind of data to the browser";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        //System.err.println(header);
        if(session == null || session instanceof AdminSession) {
            //throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
            //System.err.println("Sending "+suburl.substring(1)+" to unknown user ");
            // Modified by exce, start
            /**
             * If we just use JVM's default locale, no matter what user's locale
             * is, we always send webmail.css of JVM's default locale.
             *
             * By using WebMailServer's default locale instead of JVM's,
             * when responsing the login screen, we are able to pass through
             * webmail.css depending on user's locale.
             */
            // return new HTMLImage(store,suburl.substring(1),Locale.getDefault(),"default");
            return new HTMLImage(store, suburl.substring(1), WebMailServer.getDefaultLocale(), parent.getProperty("webmail.default.theme"));
            // Modified by exce, end
        } else {
            UserData data=((WebMailSession)session).getUser();
            //System.err.println("Sending "+suburl.substring(1)+" to user "+data.getLogin());
            return new HTMLImage(store,suburl.substring(1),data.getPreferredLocale(),data.getTheme());
        }
    }

    public String provides() {
        return "passthrough";
    }

    public String requires() {
        return "";
    }

} // ImageHandler
