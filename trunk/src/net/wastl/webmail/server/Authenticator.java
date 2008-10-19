/* CVS ID: $Id$ */
package net.wastl.webmail.server;

import net.wastl.webmail.config.ConfigScheme;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.storage.StorageManager;

import org.webengruven.webmail.auth.AuthDisplayMngr;

/**
 * Authenticator.java
 *
 * Created: Mon Apr 19 11:01:22 1999
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
 * Generic class for user authentication.
 * This class actually doesn't do anything.
 *
 * @author Sebastian Schaffert
 * @version 1.0
 * @see AuthenticatorHandler
 * @see net.wastl.webmail.storage.simple.SimpleStorage
 */
/* 9/24/2000 devink -- changed for challenge/response authentication */
public abstract class Authenticator  {
    protected String key;

    public Authenticator() {

    }

    public String getKey() {
        return key;
    }

    public abstract String getVersion();

    /** Get a displamanager object for this class.
     * @see org.webengruven.webamil.auth.AuthDisplayMngr
     * @return the AuthDisplayMngr apropriate for this class.
     */
    public AuthDisplayMngr getAuthDisplayMngr() {
        return new AuthDisplayMngr();
    }

    /**
     * (Re-)Initialize this authenticator.
     * Needed as we can't use the Constructor properly with the Plugin-style.
     * @param parent Give the Storage to allow the authenticator to check
     *  certain things.
     */
    public abstract void init(StorageManager store);

    /**
     * Register this authenticator with WebMail.
     */
    public abstract void register(ConfigScheme store);

    /**
     * Authentication to be done *before* UserData is available.
     * You may use a Unix login() for example to check whether a user is
     * allowed to use WebMail in general
     * Subclasses should override this.
     * It simply does nothing in this implementation.
     *
     * @param login Login-name for the user
     * @param domain Domain name the user used to log on
     * @param passwd Password to verify
     */
    public void authenticatePreUserData(String login, String domain,
     String passwd) throws InvalidPasswordException
    {
            if(login.equals("") || passwd.equals("")) {
                throw new InvalidPasswordException();
            }
    }


    /**
     * Authentication with available UserData.
     * This usually should just check the password saved by the user, but
     * may also be empty if you trust the pre-authentication (perhaps
     * that was done against the Unix-login(), you can really trust in in that
     * case.
     * Subclasses should override this. It simply does nothing in this
     * implementation.
     *
     * @param udata UserData for this user
     * @param domain Domain name the user used to log on
     * @param passwd Password to verify
     */
    public void authenticatePostUserData(UserData udata,String domain,
     String password) throws InvalidPasswordException
    { }

    /**
     * Tell WebMail whether this authentication method allows users to
     * change their passwords.
     * A Password-change option is then shown in the Options-Dialog.
     */
    public boolean canChangePassword() {
        return true;
    }

    public void changePassword(UserData udata,String newpassword,String verify)      throws InvalidPasswordException
    { }

} // Authenticator
