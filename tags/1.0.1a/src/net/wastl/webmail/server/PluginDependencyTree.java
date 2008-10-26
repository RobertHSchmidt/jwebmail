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


package net.wastl.webmail.server;

import java.util.*;

/*
 * PluginDependencyTree.java
 *
 * Created: Sat Sep 11 14:52:22 1999
 */
/**
 *
 * @author Sebastian Schaffert
 * @version
 */

public class PluginDependencyTree {

    protected Plugin node;
    protected String meprovides;

    protected Vector children;

    public PluginDependencyTree(Plugin p) {
        this.node=p;
        this.meprovides=p.provides();
        children=new Vector();
    }

    public PluginDependencyTree(String s) {
        this.node=null;
        this.meprovides=s;
        children=new Vector();
    }

    public boolean provides(String s) {
        return s.equals(meprovides);
    }

    public String provides() {
        String s=meprovides;
        Enumeration e=children.elements();
        while(e.hasMoreElements()) {
            PluginDependencyTree p=(PluginDependencyTree)e.nextElement();
            s+=","+p.provides();
        }
        return s;
    }


    public boolean addPlugin(Plugin p) {
        if(p.requires().equals(meprovides)) {
            children.addElement(new PluginDependencyTree(p));
            return true;
        } else {
            boolean flag=false;
            Enumeration e=children.elements();
            while(e.hasMoreElements()) {
                PluginDependencyTree pt=(PluginDependencyTree)e.nextElement();
                flag = flag || pt.addPlugin(p);
            }
            return flag;
        }
    }



    public void register(WebMailServer parent) {
        if(node!=null) {
            //System.err.print(node.getName()+" ");
            //System.err.flush();
            node.register(parent);
        }

        /* Perform depth-first registraion. Breadth-first would be better, but
           it will work anyway */
        Enumeration e=children.elements();
        while(e.hasMoreElements()) {
            PluginDependencyTree p=(PluginDependencyTree)e.nextElement();
            p.register(parent);
        }
    }

} // PluginDependencyTree