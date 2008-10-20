/* CVS ID: $Id$ */
package net.wastl.webmail.server;

import java.util.*;

/*
 * WebMailVirtualDomain.java
 *
 * Created: Sat Jan 15 14:08:30 2000
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
 * Represents a virtual domain in WebMail.
 * A virtual domain in WebMail allows the following things
 * - users can belong to a certain domain
 * - each domain has it's own default host, authentication host, and default email suffix
 * - each domain can have specific security features, i.e. IMAP/POP hosts users of that domain
 *   are allowed to connect to.
 *
 * @author Sebastian Schaffert
 * @version
 */

public interface WebMailVirtualDomain  {

    /**
     * Return the name of this domain. This will be appended to a new users email address
     * and will be used in the login screen
     */
    public String getDomainName();

    public void setDomainName(String name) throws Exception;

    /**
     * This returns the name of the default server that will be used.
     * The default server is where a user gets his first folder (the one named "Default").
     */
    public String getDefaultServer();

    public void setDefaultServer(String name);

    /**
     * If the authentication type for this domain is IMAP or POP, this host will be used
     * to authenticate users.
     */
    public String getAuthenticationHost();

    public void setAuthenticationHost(String name);

    /**
     * Check if a hostname a user tried to connect to is within the allowed range of
     * hosts. Depending on implementation, this could simply check the name or do an
     * DNS lookup to check for IP ranges.
     * The default behaviour should be to only allow connections to the default host and
     * reject all others. This behaviour should be configurable by the administrator, however.
     */
    public boolean isAllowedHost(String host);

    /**
     * Set the hosts a user may connect to if host restriction is enabled.
     * Excpects a comma-separated list of hostnames.
     * The default host will be added to this list in any case
     */
    public void setAllowedHosts(String hosts);

    public Enumeration getAllowedHosts();

    /**
     * Enable/Disable restriction on the hosts that a user may connect to.
     * If "disabled", a user may connect to any host on the internet
     * If "enabled", a user may only connect to hosts in the configured list
     * @see isAllowedHost
     */
    public void setHostsRestricted(boolean b);

    public boolean getHostsRestricted();

} // WebMailVirtualDomain
