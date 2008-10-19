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


package net.wastl.webmail.misc;

import net.wastl.webmail.server.*;
import java.io.*;

/**
 * StreamConnector.java
 *
 * Created: Tue Sep  7 14:47:10 1999
 */
/**
 * Used to write to a OutputStream in a separate Thread to avoid blocking.
 *
 * @author Sebastian Schaffert
 * @version
 */
public class StreamConnector extends Thread {

    InputStream in;
    ByteStore b;
    int size;
    boolean ready=false;

    public StreamConnector(InputStream sin, int size) {
        super();
        in=sin;
        this.size=size;
        b=null;
        this.start();
    }

    public void run() {
        b=ByteStore.getBinaryFromIS(in,size);
        ready=true;
    }

    public ByteStore getResult() {
        while(!ready) {
            try {
                sleep(500);
                System.err.print(".");
            } catch(InterruptedException ex) {
            }
        }
        System.err.println();
        return b;
    }

} // StreamConnector
