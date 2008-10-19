/* CVS ID: $Id$ */
package net.wastl.webmail.server;

import java.util.*;
/**
 * MailHostData.java
 *
 *
 * Created: Sun Feb  7 15:56:39 1999
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
 * @version $Revision$
 */
public interface MailHostData {	
	
    /**
     * The password for this mailbox
     * @return Value of password.
     */
    public String getPassword();
    
    /**
     * Set the value of password.
     * @param v  Value to assign to password.
     */
    public void setPassword(String  v);
    
    /**
     * The name of this mailbox
     * @return Value of name.
     */
    public String getName();
    
    /**
     * Set the value of name.
     * @param v  Value to assign to name.
     */
    public void setName(String  v);
    
    /**
     * The login for this mailbox
     */
    public String getLogin();

    public void setLogin(String s);

    /**
     * The Hostname for this mailbox
     * @return Value of host.
     */
    public String getHostURL();

    /**
     * Set the value of host.
     * @param v  Value to assign to host.
     */
    public void setHostURL(String  v); 

    /**
     * The unique ID of this mailbox
     */
    public String getID();
}
