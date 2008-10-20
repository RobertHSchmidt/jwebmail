/** AuthDisplayMngr is a class that knows how to display various parts of
 * the authentication system.  It's currently used to generate the content
 * of the login screen and setup the password changing fields.
 *
 * Each Authenticator will have an object that is either an instace of, or
 * an instance of a child of AuthDisplayMngr.  URLHandlers can use this
 * objects of this class to setup displays that may be partially
 * Authenticator specific.  The purpose of putting all the information here
 * rather than in the actual Authenticator is that many Authenticators may
 * use the exact same display setup. And only the odd ones will be left
 * out.
 *
 * This class will show a display that is suitable for normal
 * password authentication.  To accomadate other types of authentication
 * this class can be inherited from.  When inherited form, each child class
 * should assure it's self access to any additional information that might
 * be needed.  This may mean including a pointer to the Authenticator which
 * is using that AuthDisplayMngr, or it may include access to other
 * classes.
 *
 * $Id$
 *
 * @author Devin Kowatch
 * @version $Revision$
 * @see net.wastl.webmail.server.Authenticator
 * @see net.wastl.webmail.server.AuthenticatorHandler
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

import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.xml.*;

public class AuthDisplayMngr {
    
    /** Default C'tor */
    public AuthDisplayMngr() { }

    /** Setup state vars for the login screen.  If the login screen
     * requires any special state variables, this function should set them
     * up.
     * @param model The model to set vars in.
     */
    public void setLoginScreenVars(XMLGenericModel model) 
        throws WebMailException
    { 
        model.setStateVar("action uri", "login");
        model.setStateVar("pass prompt", "1");
    }

    /** Get the filename of the loginscreen. 
     * @return The filename of the login screen .xsl template
     */
    public String getLoginScreenFile() {
        return "loginscreen.xsl";
    }

    /** Setup state vars for the password change prompt.  
     * @param ud UserData for the user who will have their password changed
     * @param model The model to set state vars in
     */
    public void setPassChangeVars(UserData ud, XMLGenericModel model)
        throws WebMailException
    { 
        model.setStateVar("pass len", "15");
    }

    /** Get the name of the template that can display the password change
     * screen.
     * @return The name of the template.
     */
    public String getPassChangeTmpl() {
        return "normchangepass";
    }

}
