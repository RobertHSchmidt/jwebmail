/* CVS ID: $Id$ */
package net.wastl.webmail.server;


import net.wastl.webmail.server.http.*;
import java.net.*;
import java.util.*;
import net.wastl.webmail.exceptions.*;

import org.w3c.dom.Document;

/*
 * HTTPSession.java
 *
 *
 * Created: Thu Sep  9 17:20:37 1999
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
 *
 * @author Sebastian Schaffert
 * @version
 */

public interface HTTPSession extends TimeableConnection {
    
    public void login(HTTPRequestHeader h) throws InvalidPasswordException;

    public void login();

    public void logout();

    public String getSessionCode();

    public Locale getLocale();

    public long getLastAccess();

    public void setLastAccess();

    public String getEnv(String key);

    public void setEnv(String key, String value);

    public void setEnv();

    public InetAddress getRemoteAddress();

    public void saveData();

    public Document getModel();

    public boolean isLoggedOut();

    public void setException(Exception ex);
   
} // HTTPSession
