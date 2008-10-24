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

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import javax.mail.Session;
import javax.mail.Provider;
import javax.servlet.UnavailableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.wastl.webmail.debug.ErrorHandler;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.config.ConfigScheme;
import net.wastl.webmail.misc.Helper;
import net.wastl.webmail.exceptions.*;

/**
 * This is WebMails main server. From here most parts will be administered.
 *
 * @author Sebastian Schaffert
 */
public abstract class WebMailServer  {
    private static Log log = LogFactory.getLog(WebMailServer.class);

    protected ConnectionTimer timer;

    protected AuthenticatorHandler ahandler;
    protected PluginHandler phandler;
    protected ToplevelURLHandler uhandler;

    protected Hashtable sessions;

    protected static boolean debug=@debug@;

    public static final String VERSION="@version@";

    protected static Provider[] possible_providers;
    protected static Provider[] store_providers;
    protected static Provider[] transport_providers;

    private long start_time;

    protected static Storage storage;
    protected ConfigScheme config_scheme;

    protected static WebMailServer server;

    protected Properties config;

    /**
     * Webmail default locale setting.
     */
    protected static Locale defaultLocale = null;

    protected static String defaultTheme = null;

    public WebMailServer() {
    }

    /**
     * If debugging is enabled, send the given message to STDERR.
     *
     * @param msg The message
     */
    public static void debugOut(String msg) {
        if(getDebug()) {
            System.err.println("DBG: "+msg);
        }
    }

    /**
     * If debugging is enabled, send the given exception together with an explanatory message
     * to STDERR.
     *
     * @param msg The explanatory message
     * @param ex The exception
     */
    public void debugOut(String msg, Exception ex) {
        if(getDebug()) {
            System.err.println("DBG: "+msg);
            ex.printStackTrace();
        }
    }

    public static boolean getDebug() {
        return debug;
    }

    public static void setDebug(boolean b) {
        debug=b;
    }

    protected void doInit() throws UnavailableException, WebMailException {
        server=this;
        System.err.println("\n\nWebMail/Java Server v"+VERSION+" going up...");
        System.err.println("=========================================\n");
        System.err.println("Initalizing...");

        new SystemCheck(this);

        initConfig();
        /**
         * Initialize the default locale for webmail.
         */
        if ((config.getProperty("webmail.default.locale.language") == null) ||
            (config.getProperty("webmail.default.locale.country") == null))
            defaultLocale = Locale.getDefault();
        else
            defaultLocale = new Locale(
                                       config.getProperty("webmail.default.locale.language"),
                                       config.getProperty("webmail.default.locale.country")
                                       );
        System.err.println("- Default Locale: " + defaultLocale.getDisplayName());

        /*
         * Set the default theme to the parameter given in webmail.default.theme
         * or to "bibop" if unset.
         */
        if(config.getProperty("webmail.default.theme")==null) {
            defaultTheme="bibop";
        } else {
            defaultTheme=config.getProperty("webmail.default.theme");
        }
        System.err.println("- Default Theme: " + defaultTheme);

        ahandler=new AuthenticatorHandler(this);

        System.err.println("- Storage API ("+System.getProperty("webmail.storage")+
                           ") and Configuration ... ");

        initStorage();
        log.fatal("=============================== cut ===============================");
        log.fatal("Storage initialized.");

        timer=new ConnectionTimer();
        sessions=new Hashtable();

        System.err.println("  done!");

        uhandler=new ToplevelURLHandler(this);

        log.fatal("URLHandler initialized.");

        phandler=new PluginHandler(this);

        log.fatal("Plugins initialized.");

        initProviders();

        initServers();

        storage.initConfigKeys();

        log.fatal("=============================== cut ===============================");
        log.fatal("WebMail/Java Server "+VERSION+" initialization completed.");
        System.err.println("Initalization complete.");
        start_time=System.currentTimeMillis();
    }

    protected void initStorage()
        throws UnavailableException {
        /* Storage API */
        try {
            Class storage_api=Class.forName(config.getProperty("webmail.storage"));

            Class[] tmp=new Class[1];
            tmp[0]=Class.forName("net.wastl.webmail.server.WebMailServer");
            Constructor cons=storage_api.getConstructor(tmp);

            Object[] sargs=new Object[1];
            sargs[0]=this;

            storage=(Storage)cons.newInstance(sargs);

        } catch(InvocationTargetException e) {
            Throwable t=e.getTargetException();
            System.err.println("Nested exception: ");
            t.printStackTrace();
            System.err.println("Fatal error. Could not initialize. Exiting now!");
            throw new UnavailableException(e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            System.err.println("Fatal error. Could not initialize. Exiting now!");
            throw new UnavailableException(e.getMessage());
        }
    }

    protected void initConfig() {
        config_scheme=new ConfigScheme();

        config_scheme.configRegisterIntegerKey("SESSION TIMEOUT","3600000",
                                               "Timeout in milliseconds after which a WebMailSession is closed automatically.");
        config_scheme.configRegisterCryptedStringKey("ADMIN PASSWORD","Secret",
                                                     "Password for administrator connections. Shown encrypted, but enter"+
                                                     " plain password to change.");
    }


    protected void initProviders() {
        possible_providers=Session.getDefaultInstance(System.getProperties(),null).getProviders();
        System.err.println("- Mail providers:");
        config_scheme.configRegisterChoiceKey("DEFAULT PROTOCOL","Protocol to be used as default");
        int p_transport=0;
        int p_store=0;
        for(int i=0; i<possible_providers.length;i++) {
            System.err.println("  * "+possible_providers[i].getProtocol()+" from "+possible_providers[i].getVendor());
            if(possible_providers[i].getType() == Provider.Type.STORE) {
                p_store++;
                config_scheme.configAddChoice("DEFAULT PROTOCOL",possible_providers[i].getProtocol(),"Use "+
                                              possible_providers[i].getProtocol()+" from "+possible_providers[i].getVendor());
                config_scheme.configRegisterYesNoKey("ENABLE "+possible_providers[i].getProtocol().toUpperCase(),"Enable "+
                                              possible_providers[i].getProtocol()+" from "+possible_providers[i].getVendor());
            } else {
                p_transport++;
            }
        }
        store_providers=new Provider[p_store];
        transport_providers=new Provider[p_transport];
        p_store=0;
        p_transport=0;
        for(int i=0; i<possible_providers.length;i++) {
            if(possible_providers[i].getType() == Provider.Type.STORE) {
                store_providers[p_store]=possible_providers[i];
                p_store++;
            } else {
                transport_providers[p_transport]=possible_providers[i];
                p_transport++;
            }
        }
        /* We want to use IMAP as default, since this is the most useful protocol for WebMail */
        config_scheme.setDefaultValue("DEFAULT PROTOCOL","imap");
    }


    /**
     * Init possible servers of this main class
     */
    protected abstract void initServers();

    protected abstract void shutdownServers();

    public abstract Object getServer(String ID);

    public abstract Enumeration getServers();

    public String getBasePath() {
        return "";
    }

    public String getImageBasePath() {
        return "";
    }


    public abstract void reinitServer(String ID);


    public String getBaseURI(HTTPRequestHeader header) {
        String host=header.getHeader("Host");
        StringTokenizer tok=new StringTokenizer(host,":");
        String hostname=tok.nextToken();
        int port=80;
        if(tok.hasMoreElements()) {
            try {
                port=Integer.parseInt(tok.nextToken());
            } catch(NumberFormatException e) {}
        }
        int ssl_port=443;
        try {
            ssl_port=Integer.parseInt(storage.getConfig("ssl port"));
        } catch(NumberFormatException e) {}
        int http_port=80;
        try {
            http_port=Integer.parseInt(storage.getConfig("http port"));
        } catch(NumberFormatException e) {}
        String protocol="http";
        if(port==ssl_port) protocol="https"; else
            if(port==http_port) protocol="http";
        return protocol+"://"+host;
    }

    public Provider[] getStoreProviders() {
        Vector v=new Vector();
        for(int i=0;i<store_providers.length;i++) {
            if(storage.getConfig("ENABLE "+store_providers[i].getProtocol().toUpperCase()).equals("YES")) {
                v.addElement(store_providers[i]);
            }
        }
        Provider[] retval=new Provider[v.size()];
        v.copyInto(retval);
        return retval;
    }

    public Provider[] getTransportProviders() {
        return transport_providers;
    }

    public ConnectionTimer getConnectionTimer() {
        return timer;
    }


    public static Storage getStorage() {
        return storage;
    }

    public PluginHandler getPluginHandler() {
        return phandler;
    }

    public AuthenticatorHandler getAuthenticatorHandler() {
        return ahandler;
    }

    public ToplevelURLHandler getURLHandler() {
        return uhandler;
    }

    public ConfigScheme getConfigScheme() {
        return config_scheme;
    }



    public String getProperty(String name) {
        return config.getProperty(name);
    }

    public static String getDefaultTheme() {
        return defaultTheme;
    }

    /**
     * Return default locale.
     *
     * Related code:
     * 1. login screen:
     *    server/TopLevelHandler.java line #110.
     * 2. webmail.css:
     *    plugins/PassThroughPlugin.java line #77.
     * 3. user's default locale setting:
     *    xml/XMLUserData.java line #82.
     *
     * @return default locale.
     */
    public static Locale getDefaultLocale() {
        return defaultLocale;
    }

    public void setProperty(String name, String value) {
        config.put(name,value);
    }

    /**
       @deprecated Use StorageAPI instead
    */
    public static String getConfig(String key) {
        return storage.getConfig(key);
    }

    public void restart()
        throws UnavailableException {
        System.err.println("Initiating shutdown for child processes:");
        Enumeration e=sessions.keys();
        System.err.print("- Removing active WebMail sessions ... ");
        while(e.hasMoreElements()) {
            HTTPSession w=(HTTPSession)sessions.get(e.nextElement());
            removeSession(w);
        }
        System.err.println("done!");
        shutdownServers();
        try {
            Thread.sleep(5000);
        } catch(Exception ex) {}
        log.fatal("Shutdown completed successfully. Restarting.");
        storage.shutdown();
        System.err.println("Garbage collecting ...");
        System.gc();
        try {
            doInit();
        } catch(WebMailException ex) {
            ex.printStackTrace();
        }
    }

    public void shutdown() {
        System.err.println("Initiating shutdown for child processes:");
        Enumeration e=sessions.keys();
        System.err.print("- Removing active WebMail sessions ... ");
        while(e.hasMoreElements()) {
            HTTPSession w=(HTTPSession)sessions.get(e.nextElement());
            removeSession(w);
        }
        System.err.println("done!");
        shutdownServers();
        log.fatal("Shutdown completed successfully. Terminating.");
        storage.shutdown();
        System.err.println("Shutdown complete! Will return to console now.");
        System.exit(0);
    }

    public long getUptime() {
        return System.currentTimeMillis()-start_time;
    }

    public static String getVersion() {
        return "WebMail/Java v"+VERSION+", built with JDK @java-version@";
    }

    public static String getCopyright() {
        return "(c)1999-@year@ Sebastian Schaffert and others";
    }

    public static WebMailServer getServer() {
        return server;
    }

    public static String generateMessageID(String user) {
        long time=System.currentTimeMillis();
        String msgid=Long.toHexString(time)+".JavaWebMail."+VERSION+"."+user;
        try {
            msgid+="@"+InetAddress.getLocalHost().getHostName();
        } catch(Exception ex){}
        return msgid;
    }

    public void removeSession(HTTPSession w) {
        log.info("Removing session: "+w.getSessionCode());
        timer.removeTimeableConnection(w);
        sessions.remove(w.getSessionCode());
        if(!w.isLoggedOut()) {
            w.logout();
        }
    }

    public HTTPSession getSession(String key) {
        return (HTTPSession)sessions.get(key);
    }


    public Enumeration getSessions() {
        return sessions.keys();
    }
}
