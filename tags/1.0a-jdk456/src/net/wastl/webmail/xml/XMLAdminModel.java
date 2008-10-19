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


package net.wastl.webmail.xml;

import java.util.*;

import org.w3c.dom.*;

import net.wastl.webmail.server.*;

import javax.xml.parsers.ParserConfigurationException;

/*
 * XMLAdminModel.java
 *
 * Created: Thu May 18 14:48:21 2000
 *
 */

/**
 * Used to represent an Admin's state model
 *
 * @author Sebastian Schaffert
 * @version
 */

public class XMLAdminModel extends XMLGenericModel {

    public XMLAdminModel(WebMailServer parent, Element rsysdata) throws ParserConfigurationException {
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
