/* CVS ID: $Id$ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.misc.ByteStore;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.session.user.UserSession;
import net.wastl.webmail.storage.StorageManager;

/*
 * ShowMIME.java
 *
 * Created: Thu Sep  2 18:52:40 1999
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
 * Show a MIME part of a message.
 *
 * provides: message mime
 * requires: message show
 *
 * Created: Thu Sep  2 18:52:40 1999
 *
 * @author Sebastian Schaffert
 * @version
 */

public class ShowMIME implements Plugin, URLHandler {

    public static final String VERSION="1.1";
    public static final String URL="/showmime";

    StorageManager store;

    public ShowMIME() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        this.store=parent.getStorage();
    }

    public String getName() {
        return "ShowMIME";
    }

    public String getDescription() {
        return "Show a MIME part";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }


    public HTMLDocument handleURL(String suburl, HTTPSession sess, HTTPRequestHeader header) throws WebMailException {
        if(sess == null) {
            throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
        }
        UserSession session=(UserSession)sess;
        //System.err.println("Fetching MIME part: "+suburl);
        // Modified by exce, start
        /**
         * Refer to UserSession.java line #903, since hrefFileName is transcoded
         * from UTF-8 to ISO8859_1 then URL encoded, here we have to transcode URL
         * request string from ISO8859_1 to UTF-8 string to get actual URL string.
         */
        try {
                suburl = new String(suburl.getBytes("ISO8859_1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
        }
        // Modified by exce, end
        ByteStore b=session.getMIMEPart(header.getContent("msgid"),suburl.substring(1));
        int count=0;
        while(b == null && count <= 10) {
            //System.err.print(count+" ");
            try {
                Thread.sleep(250);
            } catch(InterruptedException e) {}
            b=session.getMIMEPart(header.getContent("msgid"),suburl.substring(1));
            count++;
        }
        if(count != 0) { System.err.println(); }
        HTMLImage content=new HTMLImage(b);
        //System.err.println(content.size());
        return content;
    }

    public String provides() {
        return "message mime";
    }

    public String requires() {
        return "message show";
    }
} // ShowMIME
