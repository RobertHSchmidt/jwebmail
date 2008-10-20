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


package net.wastl.webmail.exceptions;

import java.io.*;
/*
 * WebMailException.java
 *
 * Created: Thu Feb  4 16:55:06 1999
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
