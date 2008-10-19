/* CVS ID: $Id$ */
package net.wastl.webmail.storage.data;

/*
 * XMLSystemData.java
 *
 *
 * Created: Sat Oct 12 15:02:09 2002
 * Copyright (C) 1999-2002 Sebastian Schaffert
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
 *
 * @author <a href="mailto:wastl@wastl.net">Sebastian Schaffert</a>
 * @version 1.0
 */

import org.w3c.dom.*;

import java.util.*;

import net.wastl.webmail.config.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.storage.Storable;
import net.wastl.webmail.storage.Storage;
import net.wastl.webmail.xml.XMLCommon;

import net.wastl.webmail.logger.Logger;

public class XMLSystemData extends XMLData implements Storable, ConfigStore {

    protected Document root;
    protected Element data;

    ConfigScheme scheme;

    Vector listeners;

    /* Save the time when this document has been loaded. Might be used to reload
       a document with a higher modification time
    */
    protected long loadtime;


    public XMLSystemData (Document d, Storage loader, String name){
        super(d,loader,name);
        listeners = new Vector();
        loadtime=System.currentTimeMillis();
    }


    public void setConfigScheme(ConfigScheme scheme) {
        this.scheme=scheme;
    }
   

    public ConfigScheme getConfigScheme() {
        return scheme;
    }

    public long getLoadTime() {
        return loadtime;
    }

    public void setLoadTime(long time) {
        loadtime=time;
    }

    public Document getRoot() {
        return root;
    }

    public Element getSysData() {
        return data;
    }

    public DocumentFragment getDocumentFragment() {
        DocumentFragment df=root.createDocumentFragment();
        df.appendChild(data);
        return df;
    }

    
    public void addConfigurationListener(String key, ConfigurationListener listener) {
        scheme.addConfigurationListener(key,listener);
    }


    /**
     * Fetch the configuration associated with the specified key.
     * @param key Identifier for the configuration
     */
    public String getConfig(String key) {
        String value = getValueXPath("/SYSDATA/CONFIG[KEY = '"+key.toUpperCase()+"']/VALUE/text()");

        if(value == null) {
            value=(String)scheme.getDefaultValue(key.toUpperCase());
        }
        if(value==null) {
            value="";
        }
        return value;
    }

    public boolean isConfigSet(String key) {
        return getValueXPath("/SYSDATA/CONFIG[KEY = '"+key.toUpperCase()+"']/VALUE/text()") != null;
    }

    public Enumeration getConfigKeys() {
        return scheme.getPossibleKeys();
    }


    public void setConfig(String key, String value) 
        throws IllegalArgumentException {
        setConfig(key,value,true,true);
    }
    
   /**
     * Set a configuration "key" to the specified value.
     * @param key Identifier for the configuration
     * @paran value value to set
     */
    public void setConfig(String key, String value, boolean filter, boolean notify) 
        throws IllegalArgumentException {
        if(!scheme.isValid(key,value)) throw new IllegalArgumentException();
        if(!(isConfigSet(key) && getConfig(key).equals(value))) {

            setConfigRaw(scheme.getConfigParameterGroup(key),
                         key,
                         filter?scheme.filter(key,value):value,
                         scheme.getConfigParameterType(key));
            if(notify) scheme.notifyConfigurationChange(key);
        }
    }


    public void setConfigRaw(String groupname,String key, String value, String type) {  
        String curval=getConfig(key);
        if(curval == null || !curval.equals(value)) {
            /* Find all GROUP elements */
            Element group=(Element)getNodeListXPath("/SYSDATA/GROUP[@name='"+groupname+"']");

            if(group != null) {
                // The group exists

                if(group.getAttribute("name").equals(groupname)) {
                    /* If the group name matches, find all keys */
                    NodeList keyl=group.getElementsByTagName("KEY");
                    int j=0;
                    for(j=0;j<keyl.getLength();j++) {
                        Element keyelem=(Element)keyl.item(j);
                        if(key.equals(XMLCommon.getElementTextValue(keyelem))) {
                            /* If the key already exists, replace it */
                            Element conf=(Element)keyelem.getParentNode();
                            group.replaceChild(createConfigElement(key,value,type),conf);
                            return;
                        }
                    }
                    /* If the key was not found, append it */
                    if(j>=keyl.getLength()) {
                        group.appendChild(createConfigElement(key,value,type));
                        return;
                    }
                }
            } else {
                // The group doesn't exist, create it.

                group=createConfigGroup(groupname);
                group.appendChild(createConfigElement(key,value,type));
            }
        }
    }

    protected Element createConfigGroup(String groupname) {
        Element group=root.createElement("GROUP");
        group.setAttribute("name",groupname);
        data.appendChild(group);
        return group;
    }

    protected void deleteConfigGroup(String groupname) {
        NodeList nl=getNodeListXPath("/SYSDATA/GROUP[@name='"+groupname+"']");
        for(int i=0;i<nl.getLength();i++) {
            data.removeChild(nl.item(i));
        }
    }

    protected Element getConfigElementByKey(String key) {
        return (Element)getNodeXPath("/SYSDATA/CONFIG[KEY = '"+key.toUpperCase()+"']");
    }

    public void initChoices() {
        Enumeration enum=getConfigKeys();
        while(enum.hasMoreElements()) {
            initChoices((String)enum.nextElement());
        }
    }

    public void initChoices(String key) {
        Element config=getConfigElementByKey(key);

        XMLCommon.genericRemoveAll(config,"CHOICE");


        ConfigParameter param=scheme.getConfigParameter(key);
        if(param instanceof ChoiceConfigParameter) {
            Enumeration enum=((ChoiceConfigParameter)param).choices();
            while(enum.hasMoreElements()) {
                Element choice=root.createElement("CHOICE");
                choice.appendChild(root.createTextNode((String)enum.nextElement()));
                config.appendChild(choice);
            }
        }
    }

    protected Element createConfigElement(String key, String value, String type) {                     
        Element config=root.createElement("CONFIG");
        Element keyelem=root.createElement("KEY");
        Element desc=root.createElement("DESCRIPTION");
        Element valueelem=root.createElement("VALUE");
        keyelem.appendChild(root.createTextNode(key));
        desc.appendChild(root.createTextNode(scheme.getDescription(key)));
        valueelem.appendChild(root.createTextNode(value));
        config.appendChild(keyelem);
        config.appendChild(desc);
        config.appendChild(valueelem);
        config.setAttribute("type",type);
        ConfigParameter param=scheme.getConfigParameter(key);
        if(param instanceof ChoiceConfigParameter) {
            Enumeration enum=((ChoiceConfigParameter)param).choices();
            while(enum.hasMoreElements()) {
                Element choice=root.createElement("CHOICE");
                choice.appendChild(root.createTextNode((String)enum.nextElement()));
                config.appendChild(choice);
            }
        }
        return config;
    }
                


    public Enumeration getVirtualDomains() {
        final NodeList nl=getNodeListXPath("/SYSDATA/DOMAIN");
        return new Enumeration() {
                int i=0;
                
                public boolean hasMoreElements() {
                    return i<nl.getLength();
                }

                public Object nextElement() {
                    Element elem=(Element)nl.item(i++);
                    String value=XMLCommon.getTagValue(elem,"NAME");
                    return value==null?"unknown"+(i-1):value;
                }
            };
    }

    public WebMailVirtualDomain getVirtualDomain(String domname) {
        Element elem=(Element)getNodeXPath("/SYSDATA/DOMAIN[NAME/text() = '"+domname+"']");

        if(elem != null) {
            final Element domain=elem;
            return new WebMailVirtualDomain() {

                    public String getDomainName() {
                        String value=XMLCommon.getValueXPath(domain,"NAME/text()");
                        return value==null?"unknown":value;
                    }
                    public void setDomainName(String name) throws Exception {
                        XMLCommon.setValueXPath(domain,"NAME/text()",name);
                    }

                    public String getDefaultServer() {
                        String value=XMLCommon.getValueXPath(domain,"DEFAULT_HOST/text()");
                        return value==null?"unknown":value;
                    }
                        
                    public void setDefaultServer(String name) {
                        XMLCommon.setValueXPath(domain,"DEFAULT_HOST/text()",name);
                    }

                    public String getAuthenticationHost() {
                        String value=XMLCommon.getValueXPath(domain,"AUTHENTICATION_HOST/text()");
                        return value==null?"unknown":value;
                    }

                    public void setAuthenticationHost(String name) {
                        XMLCommon.setValueXPath(domain,"AUTHENTICATION_HOST/text()",name);
                    }

                    public boolean isAllowedHost(String host) {
                        if(getHostsRestricted()) {
                            Vector v=new Vector();
                            v.addElement(getDefaultServer());
                            Enumeration e=getAllowedHosts();
                            while(e.hasMoreElements()) {
                                v.addElement(e.nextElement());
                            }
                            Enumeration enum=v.elements();
                            while(enum.hasMoreElements()) {
                                String next=(String)enum.nextElement();
                                if(host.toUpperCase().endsWith(next.toUpperCase())) {
                                    return true;
                                }
                            }
                            return false;
                        } else {
                            return true;
                        }
                    }

                    public void setAllowedHosts(String hosts) {
                        NodeList nl=XMLCommon.getNodeListXPath(domain,"ALLOWED_HOST/text()");
                        for(int i=0;i<nl.getLength();i++) {
                            domain.removeChild(nl.item(i));
                        }
                        StringTokenizer tok=new StringTokenizer(hosts,", ");
                        while(tok.hasMoreElements()) {
                            Element ahost=root.createElement("ALLOWED_HOST");
                            XMLCommon.setElementTextValue(ahost,tok.nextToken());
                            domain.appendChild(ahost);
                        }
                    }
                            
                    public Enumeration getAllowedHosts() {
                        final NodeList nl=XMLCommon.getNodeListXPath(domain,"ALLOWED_HOST");                    
                        return new Enumeration() {
                                int i=0;
                                public boolean hasMoreElements() {
                                    return i<nl.getLength();
                                }
                                
                                public Object nextElement() {
                                    String value=XMLCommon.getElementTextValue((Element)nl.item(i++));
                                    return value==null?"error":value;
                                }
                            };
                    }

                    public void setHostsRestricted(boolean b) {
                        NodeList nl=XMLCommon.getNodeListXPath(domain,"RESTRICTED");
                        for(int i=0;i<nl.getLength();i++) {
                            domain.removeChild(nl.item(i));
                        }
                        if(b) {
                            domain.appendChild(root.createElement("RESTRICTED"));
                        }                           
                    }

                    public boolean getHostsRestricted() {
                        Node n=XMLCommon.getNodeXPath(domain,"RESTRICTED");
                        return n != null;
                    }
                };                  
        } else {
            return null;
        }
    }

    public void deleteVirtualDomain(String name) {
        NodeList nl=getNodeListXPath("/SYSDATA/DOMAIN[NAME/text()='"+name+"']");
        for(int i=0;i<nl.getLength();i++) {
            data.removeChild(nl.item(i).getParentNode());
        }
        WebMailServer.getLogger().log(Logger.LOG_INFO,"XMLSystemData: Deleted WebMail virtual domain "+name);
    }

    public void createVirtualDomain(String name) throws Exception {
        WebMailVirtualDomain dom=getVirtualDomain(name);
        if(dom!=null) {
            throw new Exception("Domain names must be unique!");
        }
        Element domain=root.createElement("DOMAIN");
        data.appendChild(domain);
        domain.appendChild(root.createElement("NAME"));
        domain.appendChild(root.createElement("DEFAULT_HOST"));
        domain.appendChild(root.createElement("AUTHENTICATION_HOST"));
        domain.appendChild(root.createElement("ALLOWED_HOST"));
        XMLCommon.setTagValue(domain,"NAME",name);
        XMLCommon.setTagValue(domain,"DEFAULT_HOST","localhost");
        XMLCommon.setTagValue(domain,"AUTHENTICATION_HOST","localhost");
        XMLCommon.setTagValue(domain,"ALLOWED_HOST","localhost");
        WebMailServer.getLogger().log(Logger.LOG_INFO,"XMLSystemData: Created WebMail virtual domain "+name);
    }

}// XMLSystemData
