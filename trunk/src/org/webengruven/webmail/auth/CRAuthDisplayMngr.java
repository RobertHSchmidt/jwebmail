/** CRAuthDisplayMngr is a class that knows how to display various parts of
 * the authentication system.  This is a customized version of
 * AuthDisplaMngr that works with the CRAuthenticator.  It knows how to
 * handle the challenge screen.
 *
 * $Id$
 *
 * @author Devin Kowatch
 * @version $Revision$
 * @see org.webengruven.webmail.auth.AuthDisplayMngr
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

package org.webengruven.webmail.auth;

import net.wastl.webmail.server.*;
import net.wastl.webmail.xml.*;
import net.wastl.webmail.exceptions.*;

public class CRAuthDisplayMngr extends AuthDisplayMngr {

    /** Default C'tor. This c'tor isn't very useful, so don't use it.  */
    public CRAuthDisplayMngr() { }

    /** Construct with a ref to the constructing authenticator.  This class
     * needs to get info from the authenticator which created it, so a
     * refrence to that authenticator is needed.
     * @param a The CRAuthenticator which this is tied to.
     */
    public CRAuthDisplayMngr(CRAuthenticator a) {
        auth = a;
    }

    /** Set up some variables for the challenge screen.
     * @param ud User data for the user trying to authenticate.
     * @param model The model to set vars in.
     */
    public void setChallengeScreenVars(UserData ud, XMLGenericModel model)
        throws WebMailException
    {
        String chal = auth.getChallenge(ud);
        model.setStateVar("challenge", chal);
    }

    /** Get the filename of the challenge screen.
     * @return The filename of the challenge screen
     */
    public String getChallengeScreenFile() {
        return "challenge.xsl";
    }

    /** Tell the loginscreen not to use a password prompt.  */
    public void setLoginScreenVars(XMLGenericModel model)
        throws WebMailException
    {
        model.setStateVar("pass prompt", "0");
        model.setStateVar("action uri", "challenge");
    }

    protected CRAuthenticator auth;

}   // END class CRAuthDisplayMngr
