/* $Id$ */
/** OTPCacheNode is a class that exists exclusivly for OTPAuthenticator.
 * It was placed in this package because including it in either in the
 * unamed package (as OTPAuthenticator is) or as a private memeber class of
 * OTPAuthenticator means that it would need to be placed in the webmail
 * authenticators directory, and that just creates problems.
 *
 * @author Devin Kowatch
 * @version $Revision$
 * @see OTPAuthenticator
 *
 * Copyright (C) 2000 Devin Kowatch
 * all rights reserved.
 */
/* This program is free software; you can redistribute it and/or
 * modify it under the terms of the Lesser GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 * USA.
 */
package org.webengruven.webmail.auth;
import org.webengruven.javaOTP.OTPState;

public class OTPCacheNode {
    public OTPState active_st;
    public OTPState new_st;

    public OTPCacheNode() {
        active_st = new_st = null;
    }

    public OTPCacheNode(OTPState act) {
        active_st = act;
        new_st = null;
    }

    public OTPCacheNode(OTPState act, OTPState nw) {
        active_st = act;
        new_st = nw;
    }
};


