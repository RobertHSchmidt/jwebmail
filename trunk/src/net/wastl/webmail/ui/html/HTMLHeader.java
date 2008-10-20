/* CVS ID: $Id$ */
package net.wastl.webmail.ui.html;


/*
 * HTMLHeader.java
 *
 * Created: Wed Feb  3 12:29:44 1999
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
 * A HTML header used by HTMLDocument.
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */
public class HTMLHeader  {

    private String title;

    public HTMLHeader(String title) {
        this.title=title;
    }

    public String toString() {
        String s="<!-- HTML-Header created by webmail.ui.html package -->\n";
        s += "<HTML>\n";
        s += "  <HEAD>\n";
        s += "    <TITLE>"+title+"</TITLE>\n";
        s += "  </HEAD>\n";
        return s;
    }
} // HTMLHeader
