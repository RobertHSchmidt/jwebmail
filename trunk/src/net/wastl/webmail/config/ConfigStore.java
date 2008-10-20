/* CVS ID: $Id$ */
package net.wastl.webmail.config;

import java.util.*;

/*
 * ConfigStore.java
 *
 * Created: Tue Oct 19 11:54:12 1999
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
 * This class is a generic storage for configuration parameters.
 * Subclasses must implement setConfigRaw and getConfigRaw.
 *
 * @author Sebastian Schaffert
 * @version
 */

public abstract class ConfigStore  {

    protected ConfigScheme scheme;

    public ConfigStore(ConfigScheme cs) {
        scheme=cs;
    }

    public ConfigStore() {
        this(new ConfigScheme());
    }

    public ConfigScheme getConfigScheme() {
        return scheme;
    }

    /**
     * Fetch all keys of the current configuration.
     */
    public Enumeration getConfigKeys() {
        return scheme.getPossibleKeys();
    }

    /**
     * Fetch the configuration associated with the specified key.
     * @param key Identifier for the configuration
     */
    public String getConfig(String key) {
        String value=getConfigRaw(key.toUpperCase());
        if(value==null || value.equals("")) {
            value=(String)scheme.getDefaultValue(key.toUpperCase());
        }
        if(value==null) {
            value="";
        }
        return value;
    }

    /**
     * Access a configuration on a low level, e.g. access a file, make a SQL query, ...
     * Will be called by getConfig.
     * return null if undefined
     */
    protected abstract String getConfigRaw(String key);

    public boolean isConfigSet(String key) {
        return getConfigRaw(key) != null;
    }

    public void setConfig(String key, String value) throws IllegalArgumentException {
        setConfig(key,value,true,true);
    }
    /**
     * Set a configuration "key" to the specified value.
     * @param key Identifier for the configuration
     * @paran value value to set
     */
    public void setConfig(String key, String value, boolean filter, boolean notify) throws IllegalArgumentException {
        if(!scheme.isValid(key,value)) throw new IllegalArgumentException();
        if(!(isConfigSet(key) && getConfigRaw(key).equals(value))) {
//          System.err.println("Key: "+key);
//          System.err.println("Value old: |"+getConfigRaw(key)+"|");
//          System.err.println("Value new: |"+value+"|");

            setConfigRaw(scheme.getConfigParameterGroup(key),
                         key,
                         filter?scheme.filter(key,value):value,
                         scheme.getConfigParameterType(key));
            if(notify) scheme.notifyConfigurationChange(key);
        }
    }

    /**
     * Access a configuration on a low level, e.g. access a file, make a SQL query, ...
     * Will be called by setConfig.
     * return null if undefined
     */
    public abstract void setConfigRaw(String group,String key, String value, String type);


    public void addConfigurationListener(String key, ConfigurationListener l) {
        scheme.addConfigurationListener(key,l);
    }


} // ConfigStore
