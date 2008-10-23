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
public interface Logger {


    public void log(int level, String message);

    public void log(int level, Exception ex);

    public void shutdown();


} // Logger
