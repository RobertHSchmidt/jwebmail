/* CVS ID: $Id$ */
package net.wastl.webmail.server;

/*
 * Plugin.java
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
 *
 *
 * This provides a generic interface for WebMail Plugins
 *
 * @author Sebastian Schaffert
 * @version $Revision$
*/
public interface Plugin {

    /**
     * Register this plugin with a WebMailServer
     * The plugin thus has access to most WebMail objects.
     */
    public void register(WebMailServer parent);

    /**
     * Return the name for this plugin.
     */
    public String getName();

    /**
     * Return a short description for this plugin to be shown in the
     * plugin list and perhaps in configuration
     */
    public String getDescription();

    /**
     * Get a version information for this plugin.
     * This is used for informational purposes only.
     */
    public String getVersion();

    /**
     * Return a stringlist (comma seperated) of features this plugin provides.
     * @see requires
     */
    public String provides();

    /**
     * Return a stringlist (comma seperated) of features this plugin requires.
     * @see provides
     */
    public String requires();

}
