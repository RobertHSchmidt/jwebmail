/* $Id$ */
package net.wastl.webmail.server;

import net.wastl.webmail.config.*;
import net.wastl.webmail.exceptions.WebMailException;
import java.io.*;
import java.util.*;

/**
 * AuthenticatorHandler.java
 *
 * Created: Wed Sep  1 15:04:04 1999
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
 *
 * @author Sebastian Schaffert
 * @version
 */

public class AuthenticatorHandler  {    
    
    WebMailServer parent;

    Hashtable authenticators;

    String authenticator_list;

    public AuthenticatorHandler(WebMailServer parent) throws WebMailException {
        this.parent=parent;

        authenticator_list=parent.getProperty("webmail.authenticators");
        if(authenticator_list == null) {
            throw new WebMailException("No Authenticators defined (parameter: webmail.authenticators)");
        }

        parent.getConfigScheme().configRegisterChoiceKey("AUTH","Authentication method to use.");
        //parent.getConfigScheme().configRegisterStringKey("AUTHHOST","localhost","Host used for remote authentication (e.g. for IMAP,POP3)");
        registerAuthenticators();
        parent.getConfigScheme().setDefaultValue("AUTH","IMAP");
    }


    /**
     * Initialize and register WebMail Authenticators.
     */
    public void registerAuthenticators() {
        System.err.println("- Initializing WebMail Authenticator Plugins ...");

        StringTokenizer tok=new StringTokenizer(authenticator_list,":;, ");

        authenticators=new Hashtable();
        while(tok.hasMoreTokens()) {
            String name=(String)tok.nextToken();
            try {
                Class c=Class.forName(name);
                Authenticator a=(Authenticator) c.newInstance();
                a.register(parent.getConfigScheme());
                authenticators.put(a.getKey(),a);
                System.err.println("  * registered authenticator plugin \""+c.getName()+"\"");
            } catch(Exception ex) {
                System.err.println("  * Error: could not register \""+name+"\" ("+ex.getMessage()+")!");
                //ex.printStackTrace();
            }
        }
        System.err.println("  done!");
    }

    public Authenticator getAuthenticator(String key) {
        return (Authenticator)authenticators.get(key);
    }
   
    
} // AuthenticatorHandler
