/* CVS ID: $Id$ */
package net.wastl.webmail.exceptions;

import java.io.*;
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
 * This is a generic WebMail Exception.
 *
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */

public class WebMailException extends Exception {

    Exception nested;

    public WebMailException() {
        super();
    }

    public WebMailException(String s) {
        super(s);
    }

    public WebMailException(Exception ex) {
        super(ex.getMessage());
        nested=ex;
    }

    public WebMailException(String s,Exception ex) {
        super(s);
        nested=ex;
    }

    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if(nested!=null) {
            try {
                ps.println("==> Nested exception: ");
            } catch(Exception ex) {}
            nested.printStackTrace(ps);
        }
    }

    public void printStackTrace(PrintWriter ps) {
        super.printStackTrace(ps);
        if(nested!=null) {
            try {
                ps.println("==> Nested exception: ");
            } catch(Exception ex) {}
            nested.printStackTrace(ps);
        }
    }

} // InvalidPasswordException
