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


package net.wastl.webmail.config;

import java.util.*;

/**
 * This class is a generic storage for configuration parameters.
 * Subclasses must implement setConfigRaw and getConfigRaw.
 *
 * @author Sebastian Schaffert
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
}
