/* CVS ID: $Id$ */
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.exceptions.*;
import java.util.*;
import java.text.*;
import java.io.*;
import javax.mail.*;
import net.wastl.webmail.misc.*;

/**
 * FileAttacher.java
 *
 * Created: Tue Sep  7 16:45:21 1999
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
 * This plugin shows the Form for attaching files to a message as well as does
 * the actual attaching to a WebMailSession
 *
 * provides: attach
 * requires: composer
 *
 * @author Sebastian Schaffert
 * @version
 */

public class FileAttacher implements URLHandler, Plugin {

    public static final String VERSION="1.00";
    public static final String URL="/compose/attach";

    Storage store;

    public FileAttacher() {

    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        this.store=parent.getStorage();
        parent.getConfigScheme().configRegisterStringKey("MAX ATTACH SIZE","1000000","Maximum size of attachments in bytes");
    }

    public String getName() {
        return "FileAttacher";
    }

    public String getDescription() {
        return "This URL-Handler handles file attachments for the Composer.";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }

    public HTMLDocument handleURL(String suburl, HTTPSession sess, HTTPRequestHeader head) throws WebMailException {
        if(sess == null) {
            throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
        }
        WebMailSession session=(WebMailSession)sess;
        UserData user=session.getUser();
        if(head.isContentSet("ADD")) {
            try {
                /* Read the file from the HTTP Header and store it in the user's session */
                ByteStore bs=(ByteStore)head.getObjContent("FILE");
                String description="";
                if(head.isContentSet("DESCRIPTION")) {
                    description=new String(((ByteStore)head.getObjContent("DESCRIPTION")).getBytes());
                }
                //System.err.println("Description: "+description);
                // Modified by exce, start
                /**
                 * It seems that IE will use its browser encoding setting to
                 * encode the file name that sent to us. Hence we have to
                 * transcode this carefully.
                 *
                 * Since we set browser's encoding to UTF-8, the attachment
                 * fliename, ie, p.getFileName(), should be UTF-8 encoded.
                 * However the filename retrived from JavaMail MimeBodyPart
                 * is ISO8859_1 encoded, we have to decode its raw bytes and
                 * construct a new string with UTF-8 encoding.
                 *
                 * But where should we write this code?
                 * WebMailSession.java, FileAttacher.java, or HTTPRequestHeader.java?
                 *
                 * I guess the transocde operation should be done here. We retain the
                 * original bytes in HTTP header processing classes.
                 *
                 * Note that the after we called bs.setName() to set name to fileName,
                 * the client browser will display the file name correctly. However,
                 * to safely transfer mail, the file name must be encoded -- eg, by
                 * MimeUtility.encodeText() which is also adopted by M$-OutLook.
                 * We delay such operation until the mail is being sent, that is,
                 * SendMessage.java line #390.
                 */
                String fileName = bs.getName();

                // Transcode file name
                if (!((fileName == null) || fileName.equals(""))) {
                        int offset = fileName.lastIndexOf("\\");                // This is no effect. It seems that MimeBodyPart.getFileName() filters '\' character.
                        fileName = fileName.substring(offset + 1);
                        fileName = new String(fileName.getBytes("ISO8859_1"), "UTF-8");
                        bs.setName(fileName);
                }

                // Transcode decription
                if ((description != null) && (!description.equals("")))
                        description = new String(description.getBytes("ISO8859_1"), "UTF-8");
                // Modified by exce, end
                if(bs!=null && bs.getSize() >0 ) {
                    session.addWorkAttachment(bs.getName(),bs,description);
                }
            } catch(Exception e) {
                e.printStackTrace();
                throw new DocumentNotFoundException("Could not attach file. (Reason: "+e.getMessage()+")");
            }
        } else if(head.isContentSet("DELETE") && head.isContentSet("ATTACHMENTS")) {
                try {
                        // Modified by exce, Start
                        /**
                         * Since attachmentName comes from HTTPRequestHeader, we have to
                         * transcode it.
                         */
                    // System.err.println("Removing "+head.getContent("ATTACHMENTS"));
                    // session.removeWorkAttachment(head.getContent("ATTACHMENTS"));
                        String attachmentName = head.getContent("ATTACHMENTS");
                        attachmentName = new String(attachmentName.getBytes("ISO8859_1"), "UTF-8");
                System.err.println("Removing " + attachmentName);
                    session.removeWorkAttachment(attachmentName);
                        // Modified by exce, end
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new DocumentNotFoundException("Could not remove attachment. (Reason: "+e.getMessage()+")");
                }
        }

        return new XHTMLDocument(session.getModel(),
                                 store.getStylesheet("compose_attach.xsl",
                                                     user.getPreferredLocale(),user.getTheme()));
    }

    public String provides() {
        return "attach";
    }

    public String requires() {
        return "composer";
    }
} // FileAttacher
