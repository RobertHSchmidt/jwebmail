/* $Id$ */
package net.wastl.webmail.server;

import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.server.http.*;

import javax.servlet.ServletException;


/**
 * URLHandler.java
 *
 * Created: Aug 1999
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
 * Classes that want to register for handling URLs must implement this.
 * 
 * @author Sebastian Schaffert
 * @versin $Revision$
*/
public interface URLHandler extends URLHandlerTreeNode {

    public String getName();
    
    public String getDescription();

    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException, ServletException;

}
