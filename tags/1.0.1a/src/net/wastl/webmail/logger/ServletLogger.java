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


package net.wastl.webmail.logger;


import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.config.*;

/**
 * ServletLogger.java
 */
/**
 * This logger implementation logs all WebMail logfile entries using the Servlet Container's
 * log facility.
 *
 * @author Sebastian Schaffert
 * @version
 */
public class ServletLogger implements Logger, ConfigurationListener {

    protected WebMailServlet parent;
    protected Storage store;
    protected int loglevel;

    public ServletLogger(WebMailServer parent, Storage st) throws WebMailException {
        this.store=st;

        if(! (parent instanceof WebMailServlet)) {
            throw new WebMailException("ServletLogger can only be used by a Servlet!");
        } else {
            this.parent=(WebMailServlet)parent;
        }
        parent.getConfigScheme().configRegisterIntegerKey(this,"LOGLEVEL","5",
                                                          "How much debug output will be written in the logfile");

        initLog();
    }

    protected void initLog() {
        try {
            loglevel=Integer.parseInt(store.getConfig("LOGLEVEL"));
        } catch(NumberFormatException e) {
            loglevel = 5;
        }
    }

    public void notifyConfigurationChange(String key) {
        initLog();
    }


    public void log(int level, String message) {
        if(level <= loglevel) {
            String s="LEVEL "+level;
            switch(level) {
            case Storage.LOG_DEBUG: s="DEBUG   "; break;
            case Storage.LOG_INFO: s="INFO    "; break;
            case Storage.LOG_WARN: s="WARNING "; break;
            case Storage.LOG_ERR: s="ERROR   "; break;
            case Storage.LOG_CRIT: s="CRITICAL"; break;
            }
            parent.getServletContext().log(s+" - "+message);
        }
    }

    public void log(int level, Exception ex) {
        if(level <= loglevel) {
            String s="LEVEL "+level;
            switch(level) {
            case Storage.LOG_DEBUG: s="DEBUG   "; break;
            case Storage.LOG_INFO: s="INFO    "; break;
            case Storage.LOG_WARN: s="WARNING "; break;
            case Storage.LOG_ERR: s="ERROR   "; break;
            case Storage.LOG_CRIT: s="CRITICAL"; break;
            }
            parent.getServletContext().log(s+" - An exception occured", ex);
        }
    }

    public void shutdown() {}


}
