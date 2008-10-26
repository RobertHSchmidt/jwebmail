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


package net.wastl.webmail.exceptions;

/*
 * WebMailException.java
 *
 * Created: Thu Feb  4 16:55:06 1999
 */
/**
 * An error occured while creating the userdata for a user.
 *
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */

public class CreateUserDataException extends UserDataException {

    public CreateUserDataException() {
        super();
    }

    public CreateUserDataException(String s, String user, String domain) {
        super(s,user,domain);
    }

} // InvalidPasswordException