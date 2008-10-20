/* CVS ID: $Id$ */
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
 *
 * @author Sebastian Schaffert
 * @version
 */

public class ImageHandler implements Plugin, URLHandler  {

    public static final String VERSION="1.0";
    public static final String URL="/images";

    Storage store;
    WebMailServer parent;

    public ImageHandler() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        store=parent.getStorage();
        this.parent=parent;
    }

    public String getName() {
        return "ImageHandler";
    }

    public String getDescription() {
        return "Return WebMail images";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        if(session == null || session instanceof AdminSession) {
            //throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
            return new HTMLImage(store,"images"+suburl,Locale.getDefault(),parent.getProperty("webmail.default.theme"));
        } else {
            UserData data=((WebMailSession)session).getUser();
            return new HTMLImage(store,"images"+suburl,data.getPreferredLocale(),data.getTheme());
        }
    }

    public String provides() {
        return "imagehandler";
    }

    public String requires() {
        return "";
    }

} // ImageHandler
