/* $Id$ */
package net.wastl.webmail.debug;

/**
 * ErrorHandler.java
 *
 *
 * Created: Tue Feb  2 12:24:40 1999
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */
public class ErrorHandler  {

    public ErrorHandler(Exception ex) {
        //System.err.println(ex.getMessage());;
        ex.printStackTrace();
    }

} // ErrorHandler
