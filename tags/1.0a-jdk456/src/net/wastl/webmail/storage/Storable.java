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


package net.wastl.webmail.storage;

/*
 * Storable.java
 *
 * Created: Oct 2002
 */
import org.w3c.dom.*;

import net.wastl.webmail.exceptions.WebMailException;

/**
 * This interface provides an abstraction of storable configuration data.
 *
 * Storables can be used to store any kind of persistant data with implementations
 * of Storage.
 * @see Storage
 */
public interface Storable {

    /**
     * The following constructor must be provided:
     */
    // public Storable (Document d, Storage loader, String name);


    /**
     * Return a DOM representation of this Storable.
     */
    public Document getDocumentInstance();

    /**
     * A document fragment representation of the data contained in this
     * storable.
     */
    public DocumentFragment getDocumentFragment();

    /**
     * Get the value of the node pointed to by xpath, beginning at the root
     * node.
     */
    public String getProperty(String xpath);

    /**
     * Save the storable using the Storage that was used to load it.
     */
    public void save() throws WebMailException;


    /**
     * Save the storable using the Storage provided as parameter.
     */
    public void saveWith(Storage loader) throws WebMailException;
}
