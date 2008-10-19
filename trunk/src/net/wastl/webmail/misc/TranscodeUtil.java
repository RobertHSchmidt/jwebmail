/**
 * @(#)TranscodeUtil.java       1.0 2001/09/25
 *
 * Copyleft 2001 by Steve Excellent Lee,
 */
package org.bulbul.webmail.util;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import javax.mail.internet.MimeUtility;


/**
 * TranscodeUtil.
 *
 * Provides transcoding utilities.
 *
 * @author              Steve Excellent Lee.
 * @version             1.0 2000
 */
public class TranscodeUtil {
    /**
     * Why we need 
     * org.bulbul.util.TranscodeUtil.transcodeThenEncodeByLocale()?
     *
         * Because we specify client browser's encoding to UTF-8, IE seems
     * to send all data encoded in UTF-8. That means the byte sequences 
     * we received are all UTF-8 bytes. However, strings read from HTTP
     * is ISO8859_1 encoded, that's we need to transcode them (usually
     * from ISO8859_1 to UTF-8.
     * Next we encode those strings using MimeUtility.encodeText() depending
     * on user's locale. Since MimeUtility.encodeText() is used to convert 
     * the strings into its transmission format, finally we can use the 
     * strings in the outgoing e-mail, then receiver's email agent is 
     * responsible for decoding the strings.
     *
     * As described in JavaMail document, MimeUtility.encodeText() conforms
     * to RFC2047 and as a result, we'll get strings like "=?Big5?B......".
     * @param   sourceString                    String to be encoded
     * @param   sourceStringEncoding    The encoding to decode `sourceString' 
     *                                                                  string. If `sourceStringEncoding'
     *                                                                  is null, use JVM's default enconding.
     * @param   Locale                                  prefered locale
     *
     * @return  empty string(prevent NullPointerException) if sourceString 
     *                  is null or empty(""); 
     *                  otherwise RFC2047 conformed string, eg, "=?Iso8859-1?Q....."
     */
    public static String transcodeThenEncodeByLocale(
                                                     String sourceString, 
                                                     String sourceStringEncoding,
                                                     Locale locale) 
        throws java.io.UnsupportedEncodingException {
        String str;
        
        if ((sourceString == null) || (sourceString.equals("")))
            return "";
        
        // Transcode to UTF-8
        if ((sourceStringEncoding == null) || 
            (sourceStringEncoding.equals("")))
            str = new String(sourceString.getBytes(), "UTF-8");
        else
            str = new String(sourceString.getBytes(sourceStringEncoding),"UTF-8");
        
        // Encode text
        if (locale.getLanguage().equals("zh") && locale.getCountry().equals("TW")) {
            return MimeUtility.encodeText(str, "Big5", null);
        } else {
            return MimeUtility.encodeText(str);
        }
    }
}
