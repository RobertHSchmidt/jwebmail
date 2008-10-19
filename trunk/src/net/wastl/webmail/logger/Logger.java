/* CVS ID: $Id$ */
package net.wastl.webmail.logger;


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
public interface Logger {
    
    public static final int LOG_DEBUG=10;
    public static final int LOG_INFO=5;
    public static final int LOG_WARN=3;
    public static final int LOG_ERR=1;
    public static final int LOG_CRIT=0;
   
    public void log(int level, String message);

    public void log(int level, Exception ex);

    public void shutdown();


} // Logger
