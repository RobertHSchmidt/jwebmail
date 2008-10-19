/* CVS ID: $Id$ */
package net.wastl.webmail.config;

/*
 * UnchangeableStringConfigParameter.java
 *
 * Created: Tue Oct 19 16:16:18 1999
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
 * This is a parameter that cannot be changed.
 *
 * Created: Tue Oct 19 16:16:18 1999
 *
 * @author Sebastian Schaffert
 * @version
 */

public class UnchangeableStringConfigParameter extends StringConfigParameter {

    public UnchangeableStringConfigParameter(String name, String def, String desc) {
        super(name,def,desc);
    }

    public boolean isPossibleValue(Object value) {
        return false;
    }

    public String getType() {
        return "unchangeable";
    }
} // UnchangeableStringConfigParameter
