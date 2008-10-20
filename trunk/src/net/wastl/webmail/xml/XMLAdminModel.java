/* CVS ID: $Id$ */
package net.wastl.webmail.xml;

import java.io.IOException;
import java.util.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import net.wastl.webmail.server.*;

import javax.xml.parsers.ParserConfigurationException;

/*
 * XMLAdminModel.java
 *
 * Created: Thu May 18 14:48:21 2000
 *
 * Copyright (C) 2000 Sebastian Schaffert
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
 *
 */

/**
 * Used to represent an Admin's state model
 *
 * @author Sebastian Schaffert
 * @version
 */

public class XMLAdminModel extends XMLGenericModel {

    public XMLAdminModel(WebMailServer parent, Element rsysdata) throws ParserConfigurationException, SAXException, IOException {
        super(parent,rsysdata);
    }

    public synchronized Element addStateElement(String tag) {
        Element elem=root.createElement(tag);
        statedata.appendChild(elem);
        return elem;
    }

    public synchronized Element createElement(String tag) {
        return root.createElement(tag);
    }

    public synchronized Element createTextElement(String tag, String value) {
        Element elem=root.createElement(tag);
        XMLCommon.setElementTextValue(elem,value);
        return elem;
    }

    public synchronized void importUserData(Element userdata) {
        XMLCommon.genericRemoveAll(statedata,"USERDATA");
        statedata.appendChild(root.importNode(userdata,true));
    }

    public synchronized void clearUserData() {
        XMLCommon.genericRemoveAll(statedata,"USERDATA");
    }

} // XMLAdminModel
