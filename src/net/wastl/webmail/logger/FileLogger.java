/* CVS ID: $Id$ */
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

    protected Queue queue;
    protected Queue time_queue;

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
        queue=new Queue();
        time_queue=new Queue();
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
