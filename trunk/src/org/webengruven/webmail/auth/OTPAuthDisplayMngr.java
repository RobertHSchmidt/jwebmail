/** OTPAuthDisplayMngr provides info/setup for user interaction with the
 * OTPAuthenticator class.  There are a few special details to using
 * RFC1938 OTPs when it comes to changing them.  Namely, rather than having
 * the server compute the OTP, the client computes it and gives it to the
 * serevr.  Thus there needs to be a challenge used in changing the
 * password.
 *
 *
 * $Id$
 *
 * @author Devin Kowatch
 * @version $Revision$
 * @see org.webengruven.auth.CRAuthenticator
 * @see OTPAuthenticator
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

import org.webengruven.webmail.auth.*;

import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.xml.*;

public class OTPAuthDisplayMngr extends CRAuthDisplayMngr {
    /* the length of the passwords */
    private int     PASS_LEN = 30;

    /** Default C'tor */
    public OTPAuthDisplayMngr() { super(null);}

    /** Construct with a ref to the Authenticator using this object.  It
     * will be used later.
     */
    public OTPAuthDisplayMngr(OTPAuthenticatorIface a) {
        auth = a;
    }

    /** Setup state vars for the password change prompt.
     * @param ud UserData for the user who will have their password changed
     * @param model The model to set state vars in
     */
    public void setPassChangeVars(UserData ud, XMLGenericModel model)
        throws WebMailException
    {
        try {
            OTPAuthenticatorIface otp_auth = (OTPAuthenticatorIface)auth;

            model.setStateVar("new challenge", otp_auth.getNewChallenge(ud));
            model.setStateVar("pass len", String.valueOf(PASS_LEN));
        }
        catch (ClassCastException e) {
            e.printStackTrace();
            throw new WebMailException( "tried to use OTPAuthDisplayMngr"
             + " with an Authenticator other than OTPAuthenticator");
        }
    }

    /** Get the name of the template that can display an OTP password
     * change screen.
     * @return The name of the template
     */
    public String getPassChangeTmpl() {
        return "otpchangepass";
    }
}
