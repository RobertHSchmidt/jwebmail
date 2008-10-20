/* CVS ID $Id$ */
package net.wastl.webmail.xml;

import net.wastl.webmail.config.*;
import net.wastl.webmail.server.*;

import java.util.*;


import org.w3c.dom.*;

/*
 * XMLSystemData.java
 *
 * Created: Sat Mar  4 16:07:30 2000
 *
 * Copyright (C) 2000 Sebastian Schaffert
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
 * This class represents methods for accessing WebMail's system configuration in a
 * XML tree.
 *
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */

public class XMLSystemData extends ConfigStore {
    
    protected Document root;

    protected Element sysdata;

    /* Save the time when this document has been loaded. Might be used to reload
       a document with a higher modification time
    */
    protected long loadtime;

    public XMLSystemData(Document d, ConfigScheme cs) {
	super(cs);
	root=d;
	sysdata=root.getDocumentElement();
	if(sysdata==null) {
	    sysdata=root.createElement("SYSDATA");
	    root.appendChild(sysdata);
	}
	loadtime=System.currentTimeMillis();
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
	return sysdata;
    }

    public DocumentFragment getDocumentFragment() {
	DocumentFragment df=root.createDocumentFragment();
	df.appendChild(sysdata);
	return df;
    }

    protected String getConfigRaw(String key) {
	NodeList nl=sysdata.getElementsByTagName("KEY");
	for(int i=0;i<nl.getLength();i++) {
	    Element e=(Element)nl.item(i);
	    if(XMLCommon.getElementTextValue(e).equals(key)) {
		Element p=(Element)e.getParentNode();
		NodeList valuel=p.getElementsByTagName("VALUE");
		if(valuel.getLength()>=0) {
		    return XMLCommon.getElementTextValue((Element)valuel.item(0));
		}
	    }
	}
	return null;
    }
    
    public void setConfigRaw(String groupname,String key, String value, String type) {	
	String curval=getConfigRaw(key);
	if(curval == null || !curval.equals(value)) {
// 	    System.err.println("XMLSystemData: "+groupname+"/"+key+" = "+value);
	    /* Find all GROUP elements */
	    NodeList groupl=sysdata.getElementsByTagName("GROUP");
	    int i=0;
	    for(i=0; i<groupl.getLength();i++) {
		Element group=(Element)groupl.item(i);
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
	    }
	    if(i>=groupl.getLength()) {
		Element group=createConfigGroup(groupname);
		group.appendChild(createConfigElement(key,value,type));
	    }
	}
    }

    protected Element createConfigGroup(String groupname) {
	Element group=root.createElement("GROUP");
	group.setAttribute("name",groupname);
	sysdata.appendChild(group);
	return group;
    }

    protected void deleteConfigGroup(String groupname) {
	NodeList nl=sysdata.getElementsByTagName("GROUP");
	for(int i=0;i<nl.getLength();i++) {
	    if(((Element)nl.item(i)).getAttribute("name").equals(groupname)) {
		sysdata.removeChild(nl.item(i));
	    }
	}
    }

    protected Element getConfigElementByKey(String key) {
	NodeList nl=sysdata.getElementsByTagName("KEY");
	
	Element config=null;
	for(int i=0;i<nl.getLength();i++) {
	    Element keyelem=(Element)nl.item(i);
	    Element parent=(Element)keyelem.getParentNode();
	    if(XMLCommon.getElementTextValue(keyelem).equals(key) &&
	       parent.getTagName().equals("CONFIG")) {
		config=parent;
		break;
	    }
	}
	return config;
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

    public boolean getVirtuals() {
	    final NodeList nl=sysdata.getElementsByTagName("VIRTUALS");
	    Element virtuals=(Element) nl.item(0);
	    if(virtuals != null &&
	       virtuals.getAttribute("enabled")!=null &&
	       virtuals.getAttribute("enabled").equals("true")) {
		    return true;
	    } else {
		    return false;
	    }               
    }
    public void setVirtuals(boolean state) {
	    NodeList nl=sysdata.getElementsByTagName("VIRTUALS");
	    Element virtuals=(Element) nl.item(0);
	    if(state==false) {
		    virtuals.setAttribute("enabled","false");
	    } else {
		    virtuals.setAttribute("enabled","true");
	    }
    }

    public Enumeration getVirtualDomains() {
	final NodeList nl=sysdata.getElementsByTagName("DOMAIN");
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
	// Check if virtual domains are disabled
	if(getVirtuals()==false) {
	    // No, default to localhost
	    return new WebMailVirtualDomain() {
		    public String getDomainName() {
			return "localhost";
		    }
		    public void setDomainName(String name) throws Exception {
		    }

		    public String getDefaultServer() {
			return "localhost";
		    }
			
		    public void setDefaultServer(String name) {
		    }

		    public String getAuthenticationHost() {
			return "localhost";
		    }

		    public void setAuthenticationHost(String name) {
		    }

		    public boolean isAllowedHost(String host) {
			    return true;
		    }

		    public void setAllowedHosts(String hosts) {
		    }
			    
		    public Enumeration getAllowedHosts() {
			return new Enumeration() {
				int i=0;
				public boolean hasMoreElements() {
				    return i<1;
				}
				
				public Object nextElement() {
				    i++;
				    return "localhost";
				}
			};
		    }

		    public void setHostsRestricted(boolean b) {
		    }

		    public boolean getHostsRestricted() {
			return false;
		    }
	    };
	}
	// Virtual domains are allowed, get that wanted one
	NodeList nodel=sysdata.getElementsByTagName("DOMAIN");
	Element elem=null;
	int j;
	for(j=0;j<nodel.getLength();j++) {
	    elem=(Element)nodel.item(j);
	    elem.normalize();
	    NodeList namel=elem.getElementsByTagName("NAME");
	    if(namel.getLength()>0) {
		if(XMLCommon.getElementTextValue((Element)namel.item(0)).equals(domname)) {
		    break;
		}
	    }
	}
	if(j<nodel.getLength() && elem != null) {
	    final Element domain=elem;
	    return new WebMailVirtualDomain() {

		    public String getDomainName() {
			String value=XMLCommon.getTagValue(domain,"NAME");
			return value==null?"unknown":value;
		    }
		    public void setDomainName(String name) throws Exception {
			XMLCommon.setTagValue(domain,"NAME",name,true,"Virtual Domain names must be unique!");
		    }

		    public String getDefaultServer() {
			String value=XMLCommon.getTagValue(domain,"DEFAULT_HOST");
			return value==null?"unknown":value;
		    }
			
		    public void setDefaultServer(String name) {
			XMLCommon.setTagValue(domain,"DEFAULT_HOST",name);
		    }

		    public String getAuthenticationHost() {
			String value=XMLCommon.getTagValue(domain,"AUTHENTICATION_HOST");
			return value==null?"unknown":value;
		    }

		    public void setAuthenticationHost(String name) {
			XMLCommon.setTagValue(domain,"AUTHENTICATION_HOST",name);
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
			NodeList nl=domain.getElementsByTagName("ALLOWED_HOST");
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
			final NodeList nl=domain.getElementsByTagName("ALLOWED_HOST");			
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
			NodeList nl=domain.getElementsByTagName("RESTRICTED");
			for(int i=0;i<nl.getLength();i++) {
			    domain.removeChild(nl.item(i));
			}
			if(b) {
			    domain.appendChild(root.createElement("RESTRICTED"));
			} 			    
		    }

		    public boolean getHostsRestricted() {
			NodeList nl=domain.getElementsByTagName("RESTRICTED");
			return nl.getLength()>0;
		    }
		};		    
	} else {
	    return null;
	}
    }

    /**
     * This is just completely useless, since you can change virtual domains directly.
     * It should be removed ASAP
     */
    public void setVirtualDomain(String name,WebMailVirtualDomain domain) {
	System.err.println("Called useless net.wastl.webmail.xml.XMLSystemData::setVirtualDomain/2");
    }

    public void deleteVirtualDomain(String name) {
	NodeList nl=sysdata.getElementsByTagName("NAME");
	for(int i=0;i<nl.getLength();i++) {
	    if(nl.item(i).getParentNode().getNodeName().equals("DOMAIN") && 
	       XMLCommon.getElementTextValue((Element)nl.item(i)).equals(name)) {
		sysdata.removeChild(nl.item(i).getParentNode());
	    }
	}
	WebMailServer.getStorage().log(Storage.LOG_INFO,"XMLSystemData: Deleted WebMail virtual domain "+name);
    }

    public void createVirtualDomain(String name) throws Exception {
	WebMailVirtualDomain dom=getVirtualDomain(name);
	if(dom!=null) {
	    throw new Exception("Domain names must be unique!");
	}
	Element domain=root.createElement("DOMAIN");
	sysdata.appendChild(domain);
	domain.appendChild(root.createElement("NAME"));
	domain.appendChild(root.createElement("DEFAULT_HOST"));
	domain.appendChild(root.createElement("AUTHENTICATION_HOST"));
	domain.appendChild(root.createElement("ALLOWED_HOST"));
	XMLCommon.setTagValue(domain,"NAME",name);
	XMLCommon.setTagValue(domain,"DEFAULT_HOST","localhost");
	XMLCommon.setTagValue(domain,"AUTHENTICATION_HOST","localhost");
	XMLCommon.setTagValue(domain,"ALLOWED_HOST","localhost");
	WebMailServer.getStorage().log(Storage.LOG_INFO,"XMLSystemData: Created WebMail virtual domain "+name);
    }

} // XMLSystemData
