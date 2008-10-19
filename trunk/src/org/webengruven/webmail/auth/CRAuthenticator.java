/* $Id$ */
package org.webengruven.webmail.auth;

import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;

/**
 * CRAuthenticator.java
 *
 * This is the base class for all Authenticators which implement challenge
 * response authentication.  As a general rule most parts of Webmail won't
 * care if if an authenticator is a child of the class Authenticator, or a
 * child of CRAuthenticator, however, there are a few extra methods needed
 * for challenge response.
 *
 * Also worth noting, this used to be the name of concrete authenticator.  I
 * decided that it was okay to reuse the name here because:
 *  1) it was only a test authenticator
 *  2) I really couldn't think of a better name.
 *
 * Created: Mon Jul 15 20:25
 * Recreated: Sun Sep 24 2000
 *
 * 08/11/2000 Sebastian Schaffert: Modified to fit into WebMail 0.7.2
 * - added import net.wastl.webmail.exceptions.*;
 *
 *
 * @author Devin Kowatch
 * @version $Revision$
 * @see webmail.server.UserData
 *
 * Copyright (C) 2000 Devin Kowatch
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

public abstract class CRAuthenticator extends Authenticator {

    /* dummy c'tor */
    public CRAuthenticator() { }

    /** Return an AuthDisplayMngr to use for display*/
    public AuthDisplayMngr getAuthDisplayMngr() {
        return new CRAuthDisplayMngr(this);
    }

    /** Get the challenge for this authentication.  This will get passed some
     * user data and should return the approriate challenge string for that
     * user.
     */
    public abstract String getChallenge(UserData ud) throws WebMailException;
} // CRAuthenticator
