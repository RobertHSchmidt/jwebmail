/* $Id$ */
package net.wastl.webmail.ui.html;

import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.misc.ByteStore;
import java.util.Locale;
/*
 * HTMLImage.java
 *
 * Created: Wed Feb  3 18:23:28 1999
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
 * A HTML Document that is actually an image.:-)
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */

public class HTMLImage extends HTMLDocument {

    public ByteStore cont;


    public HTMLImage(ByteStore content) {
        this.cont=content;
    }

//     public HTMLImage(StorageManager store,String name, Locale locale, String theme) throws WebMailException {
//      cont=new ByteStore(store.getBinaryFile(name,locale,theme));
//      cont.setContentType(store.getMimeType(name));
//      cont.setContentEncoding("BINARY");
//     }

    public int size() {
        if(cont == null) {
            return 0;
        } else {
            return cont.getSize();
        }
    }

    public String getContentEncoding() {
        return cont.getContentEncoding();
    }

    public String getContentType() {
        return cont.getContentType();
    }

    public byte[] toBinary() {
        return cont.getBytes();
    }
    
} // HTMLImage
