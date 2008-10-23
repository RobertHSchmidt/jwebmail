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

import java.io.*;
import java.util.*;
import java.text.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.config.*;

/**
 * Logger.java
 *
 * Created: Sun Sep 19 18:58:28 1999
 */
/**
 * This is an asynchronous Logger thread that accepts log messages to a Queue and writes
 * them to the logfile from time to time (all 5 seconds).
 *
 * @author Sebastian Schaffert
 * @version
 */
public class FileLogger extends Thread implements ConfigurationListener, Logger {

    private DateFormat df=null;

    protected PrintWriter logout;
    protected int loglevel;

    protected net.wastl.webmail.misc.Queue queue;
    protected net.wastl.webmail.misc.Queue time_queue;

    protected boolean do_shutdown=false;

    protected WebMailServer parent;
    protected Storage store;

    protected int interval;

    public FileLogger(WebMailServer parent, Storage st) {
        super("FileLogger Thread");
        this.parent=parent;
        this.store=st;
        parent.getConfigScheme().configRegisterIntegerKey(this,"LOGLEVEL","5",
                                                          "How much debug output will be written in the logfile");
        parent.getConfigScheme().configRegisterStringKey(this,"LOGFILE",
                                                         parent.getProperty("webmail.data.path")+
                                                         System.getProperty("file.separator")+
                                                         "webmail.log",
                                                         "WebMail logfile");
        parent.getConfigScheme().configRegisterIntegerKey(this,"LOG INTERVAL","5",
                                                          "Interval used for flushing the log buffer"+
                                                          " in seconds. Log messages of level CRIT or"+
                                                          " ERR will be written immediately in any way.");
        initLog();
        queue=new net.wastl.webmail.misc.Queue();
        time_queue=new net.wastl.webmail.misc.Queue();
        this.start();
    }

    protected void initLog() {
        System.err.print("  * Logfile ... ");
        try {
            loglevel=Integer.parseInt(store.getConfig("LOGLEVEL"));
        } catch(NumberFormatException e) {}
        try {
            interval=Integer.parseInt(store.getConfig("LOG INTERVAL"));
        } catch(NumberFormatException e) {}
        try {
            String filename=store.getConfig("LOGFILE");
            logout=new PrintWriter(new FileOutputStream(filename,true));
            System.err.println("initalization complete: "+filename+", Level "+loglevel);
        } catch(IOException ex) {
            ex.printStackTrace();
            logout=new PrintWriter(System.out);
            System.err.println("initalization complete: Sending to STDOUT, Level "+loglevel);
        }
    }

    protected String formatDate(long date) {
        if(df==null) {
            TimeZone tz=TimeZone.getDefault();
            df=DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.DEFAULT, Locale.getDefault());
            df.setTimeZone(tz);
        }
        String now=df.format(new Date(date));
        return now;
    }

    protected void writeMessage(long time, String message) {
        logout.println("["+formatDate(time)+"] - "+message);
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
            queue(System.currentTimeMillis(),s+" - "+message);
            if(level <= Storage.LOG_ERR) {
                flush();
            }
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
            StringWriter message=new StringWriter();
            ex.printStackTrace(new PrintWriter(message));
            queue(System.currentTimeMillis(),s+" - "+message.toString());
            if(level <= Storage.LOG_ERR) {
                flush();
            }
        }
    }


    protected void flush() {
        while(!queue.isEmpty()) {
            Long l=(Long)time_queue.next();
            String s=(String)queue.next();
            writeMessage(l.longValue(),s);
        }
        logout.flush();
    }

    public void queue(long time, String message) {
        queue.queue(message);
        time_queue.queue(new Long(time));
    }

    public void notifyConfigurationChange(String key) {
        initLog();
    }

    public void shutdown() {
        flush();
        do_shutdown=true;
    }

    public void run() {
        while(!do_shutdown) {
            flush();
            try {
                sleep(interval*1000);
            } catch(InterruptedException e) {}
        }
    }
} // FileLogger
