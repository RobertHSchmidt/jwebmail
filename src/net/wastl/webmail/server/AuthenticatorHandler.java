/*
 * @(#)$Id$
 *
 * Copyright 2008 by the JWebMail Development Team and Sebastian Schaffert.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package net.wastl.webmail.server;

import net.wastl.webmail.config.*;
import net.wastl.webmail.exceptions.WebMailException;
import java.io.*;
import java.util.*;

/**
 *
 * @author Sebastian Schaffert
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
}
