/* $Id$ */
package net.wastl.webmail.config;

import java.util.*;

/*
 * ConfigScheme.java
 *
 * Created: 31.08.99
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
 * This class contains a scheme for WebMail configuration data.
 *
 * It is mainly a container for the ConfigParameter objects and a wrapper to access
 * the main functions in them to ease access to the scheme.
 *
 * Created: 31.08.99
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */
public class ConfigScheme {

    protected Hashtable config_scheme;

    public ConfigScheme() {
        System.err.print("- Configuration Scheme Handler ... ");
        config_scheme=new Hashtable();
        System.err.println("done!");
    }

    /**
     * Check whether a key/value pair is valid in this configuration scheme
     * @param key Name of the parameter
     * @param value value to check for
     */
    public boolean isValid(String key, Object value) {
        ConfigParameter scheme=getConfigParameter(key);
        if(scheme==null) {
            return false;
        } else {
            return scheme.isPossibleValue(value);
        }
    }

    public String filter(String key, String value) {
        ConfigParameter c=(ConfigParameter)config_scheme.get(key);
        if(c!=null) {
            return c.filter(value);
        } else {
            return value;
        }
    }

    /**
     * Register a configuration key that can take String values
     * @param key Name of the configuration key
     * @param default Default value for this key
     * @param desc Description for this key
     */
    public void configRegisterStringKey(String key, String def, String desc) {
        StringConfigParameter parm=new StringConfigParameter(key,def,desc);
        registerConfig(parm);
    }

    public void configRegisterStringKey(ConfigurationListener l,String key, String def, String desc) {
        StringConfigParameter parm=new StringConfigParameter(key,def,desc);
        registerConfig(parm);
        parm.addConfigurationListener(l);
    }

    /**
     * Register a configuration key that can take String values
     * @param key Name of the configuration key
     * @param default Default value for this key
     * @param desc Description for this key
     */
    public void configRegisterIntegerKey(String key, String def, String desc) {
        IntegerConfigParameter parm=new IntegerConfigParameter(key,def,desc);
        registerConfig(parm);
    }

    public void configRegisterIntegerKey(ConfigurationListener l,String key, String def, String desc) {
        IntegerConfigParameter parm=new IntegerConfigParameter(key,def,desc);
        registerConfig(parm);
        parm.addConfigurationListener(l);
    }

    /**
     * Register a configuration key that can take String values and crypts them
     * @param key Name of the configuration key
     * @param default Default value for this key
     * @param desc Description for this key
     */
    public void configRegisterCryptedStringKey(String key, String def, String desc) {
        CryptedStringConfigParameter parm=new CryptedStringConfigParameter(key,def,desc);
        registerConfig(parm);
    }

    public void configRegisterCryptedStringKey(ConfigurationListener l,String key, String def, String desc) {
        CryptedStringConfigParameter parm=new CryptedStringConfigParameter(key,def,desc);
        registerConfig(parm);
        parm.addConfigurationListener(l);
    }

    /**
     * Register a configuration key that can take one of a choice of possible values
     * @param key Name of the configuration key
     * @param desc Description for this key
     * @see configAddChoice
     */
    public void configRegisterChoiceKey(String key, String desc) {
        ChoiceConfigParameter parm=new ChoiceConfigParameter(key,desc);
        registerConfig(parm);
    }

    public void configRegisterChoiceKey(ConfigurationListener l,String key, String desc) {
        ChoiceConfigParameter parm=new ChoiceConfigParameter(key,desc);
        registerConfig(parm);
        parm.addConfigurationListener(l);
    }

    public void configRegisterYesNoKey(String key, String desc) {
        ChoiceConfigParameter parm=new ConfigYesNoParameter(key,desc);
        registerConfig(parm);
    }

    public void configRegisterYesNoKey(ConfigurationListener l,String key, String desc) {
        ChoiceConfigParameter parm=new ConfigYesNoParameter(key,desc);
        registerConfig(parm);
        parm.addConfigurationListener(l);
    }

    /**
     * Add a choice to an already-existing Choice-key
     * @param key Name of the configuration key where a choice is to be added
     * @param choice Name of the new choice
     * @param desc Description for this choice
     */
    public void configAddChoice(String key, String choice, String desc) {
        if(config_scheme!=null) {
            ConfigParameter parm=(ConfigParameter)config_scheme.get(key);
            if(parm instanceof ChoiceConfigParameter) {
                ((ChoiceConfigParameter)parm).addChoice(choice,desc);
            }
        }
    }

    /**
     * Add a configuration listener for a key.
     * There may be any amount of Listeners for a parameter.
     */
    public void addConfigurationListener(String key,ConfigurationListener l) {
        ConfigParameter parm=getConfigParameter(key);
        parm.addConfigurationListener(l);
    }

    public ConfigParameter getConfigParameter(String key) {
        return (ConfigParameter)config_scheme.get(key);
    }

    public String getConfigParameterType(String key) {
        return getConfigParameter(key).getType();
    }

    public String getConfigParameterGroup(String key) {
        return getConfigParameter(key).getGroup();
    }

    public Object getDefaultValue(String key) {
        ConfigParameter cp=(ConfigParameter)config_scheme.get(key);
        if(cp!=null) {
            return cp.getDefault();
        } else {
            return null;
        }
    }

    public void setDefaultValue(String key, Object default_value) {
        ConfigParameter cp=(ConfigParameter)config_scheme.get(key);
        if(cp!=null) {
            cp.setDefault(default_value);
        }
    }


    public String getDescription(String key) {
        ConfigParameter cp=(ConfigParameter)config_scheme.get(key);
        if(cp!=null) {
            return cp.getDescription();
        } else {
            return null;
        }
    }

    public Enumeration getPossibleKeys() {
        return config_scheme.keys();
    }

    public void notifyConfigurationChange(String key) {
//      System.err.println("NOTIFY: "+key);
        ConfigParameter parm=getConfigParameter(key);
        if(parm!=null) {
            Enumeration enum=parm.getConfigurationListeners();

            while(enum.hasMoreElements()) {
                ConfigurationListener l=(ConfigurationListener)enum.nextElement();
//              System.err.println(l);
                try {
                    l.notifyConfigurationChange(key);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void registerConfig(ConfigParameter parm) {
        if(config_scheme == null) {
            config_scheme=new Hashtable();
        }
        Enumeration e=config_scheme.keys();
        boolean flag=false;
        while(e.hasMoreElements()) {
            if(e.nextElement().equals(parm.getKey())) {
                flag=true;
                break;
            }
        }
        if(!flag) {
            config_scheme.put(parm.getKey(),parm);
        }
    }
}

