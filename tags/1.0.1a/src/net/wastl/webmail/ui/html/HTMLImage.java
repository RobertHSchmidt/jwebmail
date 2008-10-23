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


package net.wastl.webmail.ui.html;

import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.misc.ByteStore;
import java.util.Locale;
/*
 * HTMLImage.java
 *
 * Created: Wed Feb  3 18:23:28 1999
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

    public HTMLImage(Storage store,String name, Locale locale, String theme) throws WebMailException {
        cont=new ByteStore(store.getBinaryFile(name,locale,theme));
        cont.setContentType(store.getMimeType(name));
        cont.setContentEncoding("BINARY");
    }

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
