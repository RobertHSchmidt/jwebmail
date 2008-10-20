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

import java.util.*;
import net.wastl.webmail.debug.ErrorHandler;
import net.wastl.webmail.exceptions.*;

/*
 * ConnectionTimer.java
 *
 * Created: Tue Feb  2 12:27:43 1999
 */
/**
 *
 *
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */
public class ConnectionTimer extends Thread {

    private Vector connections;
    private static final long sleep_interval=1000;

    public ConnectionTimer() {
        connections=new Vector();
        this.start();
    }

    public void printStatus() {
        System.err.println(" Vulture: "+connections.size()+" connections in queue");
    }

    public void addTimeableConnection(TimeableConnection c) {
        synchronized(connections) {
            connections.addElement(c);
        }
    }

    public void removeTimeableConnection(TimeableConnection c) {
        synchronized(connections) {
            connections.removeElement(c);
        }
    }

    public void removeAll() {
        Enumeration e;
        synchronized(connections) {
            e=connections.elements();
        }
        while(e.hasMoreElements()) {
            TimeableConnection t=(TimeableConnection)e.nextElement();
            t.timeoutOccured();
        }
    }

    public void run() {
        Enumeration e;
        while(true) {
            synchronized(connections) {
                e=connections.elements();
            }
            while(e.hasMoreElements()) {
                TimeableConnection t=(TimeableConnection)e.nextElement();
                if(System.currentTimeMillis() - t.getLastAccess() > t.getTimeout()) {
                    t.timeoutOccured();
                }
            }
            try { this.sleep(sleep_interval); } catch(InterruptedException ex) { new ErrorHandler(ex); }
        }
    }
} // ConnectionTimer
