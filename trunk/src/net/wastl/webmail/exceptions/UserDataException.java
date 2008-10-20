/* CVS ID: $Id$ */
package net.wastl.webmail.exceptions;

/*
 * WebMailException.java
 *
 * Created: Thu Feb  4 16:55:06 1999
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
 * An error corresponding to some user's data
 *
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */

public class UserDataException extends WebMailException {
    
    String user;
    String domain;

    public UserDataException() {
	super();
    }

    public UserDataException(String s, String user, String domain) {
	super(s);
	this.user=user;
	this.domain=domain;
    }
    
    public String getUser() {
	return user;
    }

    public String getDomain() {
	return domain;
    }

} // InvalidPasswordException
