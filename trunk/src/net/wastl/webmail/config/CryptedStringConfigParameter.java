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

import net.wastl.webmail.misc.Helper;

/*
 * CryptedStringConfigParameter.java
 *
 * Created: Mon Sep 13 12:49:42 1999
 */
/**
 *
 * @author Sebastian Schaffert
 * @version
 */

public class CryptedStringConfigParameter extends StringConfigParameter {

    public CryptedStringConfigParameter(String name, String def, String desc) {
        super(name,Helper.crypt("AA",def),desc);
    }


    public String filter(String s) {
        return Helper.crypt("AA",s);
    }

    public String getType() {
        return "crypted";
    }
} // CryptedStringConfigParameter
