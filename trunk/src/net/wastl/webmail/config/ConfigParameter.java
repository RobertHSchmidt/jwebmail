/* $Id$ */
package net.wastl.webmail.config;

import java.util.*;

/*
 * ConfigParameter.java
 *
 * Created: Sep 1999
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
 * An abstraction for a configuration parameter.
 * Subclasses must implement a method that checks whether a specific value is correct for this
 * parameter.
 *
 * ConfigParameters may have ConfigurationListeners that work much like the Listeners in the
 * Java AWT. All listeners get informed if the value of the parameter has changed.
 *
 * Each ConfigParameter has a corresponding (unique) key, a default value (if not yet changed
 * by the user) and a short description for the administrator about what the parameter means.
 *
 * This is a scheme only, however, ConfigParameters just describe the behaviour of certain
 * keys in the WebMail configuration, they don't actually store the value itself.
 */
public abstract class ConfigParameter {
    protected String key;
    protected Object def_value;
    protected String desc;
    protected Vector listeners;

    protected String group;

    /**
     * Create a new parameter.
     * @param name Unique key of this parameter
     * @param def Default value for this parameter
     * @param desc Description for this parameter
     */
    public ConfigParameter(String name, Object def, String desc) {
        key=name;
        this.def_value=def;
        this.desc=desc;
        group="default";
        listeners=new Vector();
    }

    public void setGroup(String g) {
        group=g;
    }

    /**
     * Return the key of this parameter.
     */
    public String getKey() {
        return key;
    }

    /**
     * Return the default value of this parameter.
     */
    public Object getDefault() {
        return def_value;
    }

    public void setDefault(Object value) {
        def_value=value;
    }

    /**
     * Return the description for this parameter.
     */
    public String getDescription() {
        return desc;
    }

    /**
     * Add a ConfigurationListener for this object that will be informed if the parameter's
     * value changes.
     */
    public void addConfigurationListener(ConfigurationListener l) {
        listeners.addElement(l);
    }

    /**
     * Get a list of all configuration listeners.
     */
    public Enumeration getConfigurationListeners() {
        return listeners.elements();
    }

    /**
     * Put through some sort of filter.
     * This method is called when a String value for this parameter is set.
     * Subclasses should implement this, if they want to change the behaviour
     * @see CryptedStringConfigParameter
     */
    public String filter(String s) {
        return s;
    }

    /**
     * Check whether the value that is passed as the parameter is a valid value for this
     * ConfigParameter
     * @see ChoiceConfigParameter
     */
    public abstract boolean isPossibleValue(Object value);

    public String getType() {
        return "undefined";
    }

    public String getGroup() {
        return group;
    }
}

