/** OTPAuthenticatorIface is the interface for the OTPAuthenticator class.
 * It is used by the OTPAuthDisplayMngr class as a means of accessing the
 * OTPAuthenticator methods that are not also part of the CRAuthenticator
 * base class.  It primarilly exists because OTPAuthenticator is not a
 * member of any package (due to being a plugin) and as such,
 * OTPAuthDisplayMngr has no access to it, or it's specific methods.
 *
 * @version $Revision$
 * @author Devin Kowatch
 * @see OTPAuthenticator
 * @see OTPAuthDisplayMngr
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
import net.wastl.webmail.exceptions.*;

public abstract class OTPAuthenticatorIface extends CRAuthenticator {
    public OTPAuthenticatorIface() { super(); }

    abstract public String getNewChallenge(UserData ud) throws WebMailException;
};
