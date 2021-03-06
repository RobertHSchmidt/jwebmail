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

import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.server.http.*;
import javax.servlet.ServletException;


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
