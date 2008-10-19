/* CVS ID: $Id$ */
package net.wastl.webmail.storage;

/*
 * Storable.java
 *
 * Created: Oct 2002
 *
 * Copyright (C) 1999-2002 Sebastian Schaffert
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
