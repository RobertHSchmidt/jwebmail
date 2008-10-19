/* CVS ID: $Id$ */
package net.wastl.webmail.config;

import java.util.*;

/*
 * ConfigStore.java
 *
 * Created: Tue Oct 19 11:54:12 1999
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
/**
 * This class is a generic storage for configuration parameters.
 * Subclasses must implement setConfigRaw and getConfigRaw.
 *
 * @author Sebastian Schaffert
 * @version
 */

public interface ConfigStore  {


    public ConfigScheme getConfigScheme();

    /**
     * Fetch all keys of the current configuration.
     */
    public Enumeration getConfigKeys();

    /**
     * Fetch the configuration associated with the specified key.
     * @param key Identifier for the configuration
     */
    public String getConfig(String key);


    public boolean isConfigSet(String key);

    public void setConfig(String key, String value)
        throws IllegalArgumentException;

    /**
     * Set a configuration "key" to the specified value.
     * @param key Identifier for the configuration
     * @paran value value to set
     */
    public void setConfig(String key, String value, boolean filter, boolean notify)
        throws IllegalArgumentException;


    public void addConfigurationListener(String key, ConfigurationListener l);


} // ConfigStore
