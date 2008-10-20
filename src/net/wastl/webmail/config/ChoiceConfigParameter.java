/* $Id$ */
package net.wastl.webmail.config;

import java.util.*;

/*
 * ChoiceConfigParameter.java
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
 * Scheme of a parameter that can take one of several choices as value
 */
public class ChoiceConfigParameter extends ConfigParameter {

    Hashtable possible_values;

    public ChoiceConfigParameter(String name, String desc) {
        super(name, null, desc);
        possible_values=new Hashtable();
    }

    public void addChoice(Object choice, String desc) {
        /* First is default */
        if(possible_values.isEmpty()) {
            def_value=choice;
        }
        possible_values.put(choice,desc);
    }

    public void removeChoice(Object choice) {
        possible_values.remove(choice);
    }

    public Enumeration choices() {
        return possible_values.keys();
    }

    public String getDescription(String choice) {
        return (String)possible_values.get(choice);
    }

    public boolean isPossibleValue(Object value) {
        Enumeration e=possible_values.keys();
        boolean flag=false;
        while(e.hasMoreElements()) {
            Object o=e.nextElement();
            if(value.equals(o)) {
                flag=true;
                break;
            }
            //System.err.println((String)value + " <> " + (String)o);
        }
        return flag;
    }

    public String getType() {
        return "choice";
    }
}
