/* CVS ID: $Id$ */
package net.wastl.webmail.server;

/**
 * StatusServer.java
 *
 * A Server object that can return a status message.
 *
 * Created: Sun Dec 31 16:07:04 2000
 *
 * @author Sebastian Schaffert
 * @version
 */

public interface StatusServer  {
    
    /**
     * Return a status message.
     */
    public String getStatus();
    
} // StatusServer
