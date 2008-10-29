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


package net.wastl.webmail.authenticators;

import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import javax.mail.*;
import net.wastl.webmail.config.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author blaine.simpson@admc.com
 */
public class IMAPSAuthenticator extends IMAPAuthenticator {
    private static Log log = LogFactory.getLog(IMAPSAuthenticator.class);

    public void init(Storage store) {
        storage=store;
        Session session=Session.getDefaultInstance(System.getProperties(),null);
        try {
            st=session.getStore("imaps");
        } catch(NoSuchProviderException e) {
            log.error("Initialization for 'imaps' failed", e);
        }
    }

    public void register(ConfigScheme store) {
        key="IMAPS";
        store.configAddChoice("AUTH",key,"Authenticate against an SSL IMAP server on the net. Does not allow password change.");
    }
}
