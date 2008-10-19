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

/*
 * ConfigStore.java
 *
 * Created: Tue Oct 19 11:54:12 1999
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
