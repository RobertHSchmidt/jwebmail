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


package net.wastl.webmail.storage.xml;

import org.w3c.dom.*;

import java.net.*;
import java.io.*;
import java.util.Properties;

import javax.xml.parsers.*;
import org.xml.sax.InputSource;

import net.wastl.webmail.storage.*;
import net.wastl.webmail.exceptions.WebMailException;
import net.wastl.webmail.xml.XMLCommon;

/*
 * XMLStorage.java
 *
 * Created: Oct 2002
 */

/**
 * An implementation of the abstract Storage class storing the data in XML files.
 *
 * Requires the following properties: "webmail.data.url", "webmail.xml.url"
 */
public class XMLStorage extends Storage {


    public XMLStorage(Properties options) {
        super(options);
    }

    /**
     * Save the Storable that is provided as argument by the means of this
     * storage implementation.
     *
     * @param path the path where to store the data. The implementing subclasses
     * need to take care of properly representing UNIX-like paths.
     * @param data the data to store.
     */
    public void _saveStorable(String path, Document data) throws WebMailException, IOException {
        File file = new File(getProperty("webmail.data.path")+File.separator+path);
        if(!file.canRead() || !file.canWrite()) {
            throw new WebMailException("Cannot read file "+path+". Please check the permissions.");
        }

        OutputStream out = new FileOutputStream(file);
        XMLCommon.writeXML(data,out,null);  // instead of "null" should state the DTD
        out.flush();
        out.close();
    }


    /**
     * Load a Storable from the specified path. Return the Node representing the
     * data of the Storable.
     *
     * @param path the path where to store the data. The implementing subclasses
     * need to take care of properly representing UNIX-like paths.
     */
    public Document _loadStorable(String path) throws WebMailException, IOException {
        File file = new File(getProperty("webmail.data.path")+File.separator+path);
        if(!file.canRead() || !file.canWrite()) {
            throw new WebMailException("Cannot read file "+path+". Please check the permissions.");
        }

        InputStream in = new FileInputStream(file);


        try {
            DocumentBuilderFactory dfactory=DocumentBuilderFactory.newInstance();
            dfactory.setNamespaceAware(true);
            DocumentBuilder parser = dfactory.newDocumentBuilder();
            Document root = parser.parse(new InputSource(new InputStreamReader(in, "UTF-8")));

            return root;
        } catch(Exception ex) {
            throw new WebMailException("Could not load "+path,ex);
        }

    }


    /**
     * Remove a certain configuration item from the store.
     */
    public void removeStorable(String path) throws WebMailException, IOException {
        File file = new File(getProperty("webmail.data.path")+File.separator+path);
        if(!file.canRead() || !file.canWrite()) {
            throw new WebMailException("Cannot read file "+path+". Please check the permissions.");
        }
        if(!file.exists()) {
            throw new WebMailException("Cannot delete file "+path+". It does not exist.");
        }
        file.delete();
    }

}

