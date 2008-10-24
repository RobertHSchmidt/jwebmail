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
import net.wastl.webmail.misc.*;
import net.wastl.webmail.exceptions.WebMailException;
import java.io.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * PluginHandler.java
 *
 * Handle WebMail Plugins
 *
 * Created: Tue Aug 31 15:28:45 1999
 */
/**
 *
 *
 *
 * @author Sebastian Schaffert
 * @version
 */

public class PluginHandler  {
    private static Log log = LogFactory.getLog(PluginHandler.class);

    WebMailServer parent;
    String plugin_list;
    Vector plugins;

    public PluginHandler(WebMailServer parent) throws WebMailException {
        this.parent=parent;
        this.plugin_list=parent.getProperty("webmail.plugins");
        if(plugin_list == null) {
            throw new WebMailException("Error: No Plugins defined (Property webmail.plugins).");
        }
        plugins=new Vector();
        registerPlugins();
    }


     /**
     * Initialize and register WebMail Plugins.
     */
    public void registerPlugins() {
        log.info("Initializing WebMail Plugins ...");
        //      System.setProperty("java.class.path",System.getProperty("java.class.path")+System.getProperty("path.separator")+pluginpath);



        StringTokenizer tok=new StringTokenizer(plugin_list,":;, ");

        Class plugin_class=null;
        try {
            plugin_class=Class.forName("net.wastl.webmail.server.Plugin");
        } catch(ClassNotFoundException ex) {
            log.fatal("===> Could not find interface 'Plugin'!!");
            System.exit(1);
        }

        PluginDependencyTree pt=new PluginDependencyTree("");
        net.wastl.webmail.misc.Queue q=new net.wastl.webmail.misc.Queue();

        int count=0;

        while(tok.hasMoreTokens()) {
            String name=(String)tok.nextToken();
            try {
                Class c=Class.forName(name);
                if(plugin_class.isAssignableFrom(c)) {
                    Plugin p=(Plugin) c.newInstance();
                    q.queue(p);
                    plugins.addElement(p);
                    //System.err.print(p.getName()+" ");
                    //System.err.flush();
                    count++;
                }
            } catch(Exception ex) {
                log.error("could not register plugin \""+name+"\"!");
                ex.printStackTrace();
            }
        }

        log.info(count+" plugins loaded correctly.");


        count=0;
        while(!q.isEmpty()) {
            Plugin p=(Plugin)q.next();
            if(!pt.addPlugin(p)) {
                q.queue(p);
            }
        }
        pt.register(parent);
        log.info(count+" plugins initialized.");
    };

    public Enumeration getPlugins() {
        return plugins.elements();
    }

    /**
     * A filter to find WebMail Plugins.
     */
    class FFilter implements FilenameFilter {
        FFilter() {
        }

        public boolean accept(File f, String s) {
            if(s.endsWith(".class")) {
                return true;
            } else {
                return false;
            }
        }
    }

} // PluginHandler
