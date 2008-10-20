/* CVS ID $Id$ */
package net.wastl.webmail.xml;

import net.wastl.webmail.config.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.exceptions.*;

import java.util.*;
import java.text.*;


import org.w3c.dom.*;


/**
 * XMLUserData.java
 *
 * Created: Fri Mar 10 16:17:28 2000
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
 *
 * @author Sebastian Schaffert
 * @version
 */
/* 9/25/2000 devink -- modified for challenge/response authentication */

public class XMLUserData extends XMLData implements UserData {
    

    protected boolean debug;

    protected long login_time;
    protected boolean logged_in;

    public XMLUserData(Document d) {
	super(d);
	debug=WebMailServer.getDebug();
	if(data==null) {
	    System.err.println("Data was null ???");
	    data=root.createElement("USERDATA");
	    root.appendChild(data);
	}
    }

    public void init(String user, String domain, String password) {
	setUserName(user);
	setDomain(domain);
	setFullName(user);
	if(user.indexOf("@")!=-1) { 
	  // This is a special case when the user already contains the domain
	  // e.g. QMail
	  setEmail(user);
	} else {
	  setEmail(user+"@"+domain);
	}
	try {
	    setPassword(password,password);
	} catch(InvalidPasswordException ex) {}

	setPreferredLocale(WebMailServer.getDefaultLocale().toString());
	setTheme(WebMailServer.getDefaultTheme());
	setIntVar("first login",System.currentTimeMillis());
	setIntVar("last login", System.currentTimeMillis());
	setIntVar("login count",0);
	setIntVar("max show messages",20);
	setIntVar("icon size",48);
	setBoolVar("break lines",true);
	setIntVar("max line length",79);
    }

    public String getProperty(String xpath) {
	return getValueXPath(xpath);
    }

    public Document getRoot() {
	return root;
    }

    public Document getDocumentInstance() {
	return root;
    }

    public Element getUserData() {
	return data;
    }
    
    public DocumentFragment getDocumentFragment() {
	DocumentFragment df=root.createDocumentFragment();
	df.appendChild(data);
	return df;
    }


    protected void ensureElement(String tag, String attribute, String att_value) {
	String xp_query="//"+tag;
	if(attribute != null && att_value != null) {
	    xp_query += "[@"+attribute+"='"+att_value+"']";
	} else if(attribute != null) {
	    xp_query += "[@"+attribute+"]";
	}

	Node node=getNodeXPath(xp_query);

// 	NodeList nl=data.getElementsByTagName(tag);
// 	boolean flag=false;
// 	for(int i=0;nl != null && i<nl.getLength();i++) {
// 	    Element e=(Element)nl.item(i);
// 	    if(attribute == null) {
// 		// No attribute required
// 		flag=true;
// 		break;
// 	    } else if(att_value == null) {
// 		if(e.getAttributeNode(attribute) != null) {
// 		    // Attribute exists, value is not requested
// 		    flag=true;
// 		    break;
// 		}
// 	    } else if(e.getAttribute(attribute).equals(att_value)) {
// 		flag=true;
// 		break;
// 	    }
// 	}
	if(node == null) {
	    Element elem=root.createElement(tag);
	    if(attribute != null) {
		elem.setAttribute(attribute,att_value==null?"":att_value);
	    }
	    if(tag.equalsIgnoreCase("BOOLVAR")) {
		elem.setAttribute("value", "no");
	    }
	    data.appendChild(elem);
	    invalidateCache();
	}
    }

    public void login() {
	// Increase login count and last login pointer
	//setIntVar("last login",System.currentTimeMillis());
	if(!logged_in) {
	    setIntVar("login count",getIntVar("login count")+1);
	    login_time=System.currentTimeMillis();
	    logged_in=true;
	} else {
	    System.err.println("Err: Trying to log in a second time for user "+getLogin());
	}
    }

    public void logout() {
	if(logged_in) {
	    setIntVar("last login",login_time);
	    // Modified by exce, start
	    logged_in = false;
	    // Modified by exce, end
	} else {
	    System.err.println("Err: Logging out a user that wasn't logged in.");
	}
    }

    public void addMailHost(String name, String host, String login, String password) {
	// First, check whether a mailhost with this name already exists.
	// Delete, if yes.
	try {
	    //System.err.println("Adding mailhost "+name);
	    if(getMailHost(name) != null) {
		removeMailHost(name);
	    }	
	    Element mailhost=root.createElement("MAILHOST");
	    mailhost.setAttribute("name",name);
	    mailhost.setAttribute("id",Long.toHexString(Math.abs(name.hashCode()))+Long.toHexString(System.currentTimeMillis()));
	    
	    Element mh_login=root.createElement("MH_LOGIN");
	    XMLCommon.setElementTextValue(mh_login,login);
	    mailhost.appendChild(mh_login);
	    
	    Element mh_pass=root.createElement("MH_PASSWORD");
	    XMLCommon.setElementTextValue(mh_pass,Helper.encryptTEA(password));
	    mailhost.appendChild(mh_pass);
	    
	    Element mh_uri=root.createElement("MH_URI");
	    XMLCommon.setElementTextValue(mh_uri,host);
	    mailhost.appendChild(mh_uri);
	    
	    data.appendChild(mailhost);
	    //System.err.println("Done mailhost "+name);
	    //XMLCommon.writeXML(root,System.err,"");
	    invalidateCache();
	} catch(Exception ex) {
	    ex.printStackTrace();
	}
    }

    public void removeMailHost(String id) {
	Element n=(Element)getNodeXPath("/USERDATA/MAILHOST[@id='"+id+"']");
	if(n!=null) {
	    data.removeChild(n);
	    invalidateCache();
	}
    }

    public MailHostData getMailHost(String id) {
	//final Element mailhost=XMLCommon.getElementByAttribute(data,"MAILHOST","id",id);
	final Element mailhost=(Element)getNodeXPath("/USERDATA/MAILHOST[@id='"+id+"']");
	return new MailHostData() {

		public String getPassword() {
		    return Helper.decryptTEA(XMLCommon.getValueXPath(mailhost,"MH_PASSWORD/text()"));
		}

		public void setPassword(String s) {
		    XMLCommon.setValueXPath(mailhost,"MH_PASSWORD/text()",Helper.encryptTEA(s));
		}

		public String getLogin() {
		    return XMLCommon.getValueXPath(mailhost,"MH_LOGIN/text()");
		}

 		public String getName() { 
		    return mailhost.getAttribute("name"); 
		}

		public void setLogin(String s) {
		    XMLCommon.setValueXPath(mailhost,"MH_LOGIN/text()",s);
		}

 		public void setName(String s) { 
		    mailhost.setAttribute("name",s);
		}

		public String getHostURL() {
		    return XMLCommon.getValueXPath(mailhost,"MH_URI/text()");
		}

		public void setHostURL(String s) {
		    XMLCommon.setValueXPath(mailhost,"MH_URI/text()",s);
		}

		public String getID() {
		    return mailhost.getAttribute("id");
		}
	    };
    }

    public Enumeration mailHosts() { 
	final NodeList nl=getNodeListXPath("//MAILHOST");
	return new Enumeration() {
		int i=0;
		public boolean hasMoreElements() {
		    return i<nl.getLength();
		}

		public Object nextElement() {
		    Element e=(Element)nl.item(i++);
		    return e.getAttribute("id");
		}
	    };
    }
 
    public int getMaxShowMessages() { 
	int retval=(int)getIntVarWrapper("max show messages");
	return retval==0?20:retval;
    }

    public void setMaxShowMessages(int i) { 
	setIntVarWrapper("max show messages",i);
    }

    /**
     * As of WebMail 0.7.0 this is different from the username, because it
     * consists of the username and the domain.
     * @see getUserName()
     */
    public String getLogin() { 
	return getUserName()+"@"+getDomain(); 
    }

    public String getFullName() {
	return getValueXPath("/USERDATA/FULL_NAME/text()");
    }
    public void setFullName(String s) { 
	setValueXPath("/USERDATA/FULL_NAME/text()",s);
    }

    public String getSignature() { 
	return XMLCommon.getTagValue(data,"SIGNATURE");
    }
    public void setSignature(String s) { 
	XMLCommon.setTagValue(data,"SIGNATURE",s,true);
    }

    public String getEmail() { 
	return getValueXPath("/USERDATA/EMAIL/ADDY/text()");
    }
    public void setEmail(String s) { 
	ensureElement("/USERDATA/EMAIL/ADDY", "default", "yes");
	setValueXPath("/USERDATA/EMAIL/ADDY/text()",s);
    }

    public Locale getPreferredLocale() { 
	String loc=getValueXPath("/USERDATA/LOCALE/text()");
	StringTokenizer t=new StringTokenizer(loc,"_");
	String language=t.nextToken().toLowerCase();
	String country="";
	if(t.hasMoreTokens()) {
	    country=t.nextToken().toUpperCase();
	}
	return new Locale(language,country);
    }

    public void setPreferredLocale(String newloc) { 
	setValueXPath("/USERDATA/LOCALE/text()",newloc);
    }



    public void addEmail(String s) {
	Element email=(Element)getNodeXPath("/USERDATA/EMAIL");
	Element addy=root.createElement("ADDY");
	XMLCommon.setElementTextValue(addy,s);
	email.appendChild(addy);
    }

    public void removeEmail(String s)
    	throws WebMailException {
    	NodeList nl=getNodeListXPath("/USERDATA/EMAIL/ADDY");
	if(nl.getLength()==1) {
		throw new WebMailException("Can not delete last email address!");
	}
	for(int i=0;i<nl.getLength();i++) {
	    Element addy=(Element) nl.item(i);
	    if(XMLCommon.getElementTextValue(addy).equals(s)) {
		Element email=(Element)addy.getParentNode();
		if(addy.getAttribute("default").equals("yes")) {
		    // Need to set a new default
		    email.removeChild(addy);
		    addy=(Element) nl.item(0);
		    addy.setAttribute("default","yes");
		} else {
		    // Just remove it
		    email.removeChild(addy);
		}
		break;
	    }
	}
	invalidateCache();
    }



    public void setDefaultEmail(String s) {
	NodeList nl=getNodeListXPath("/USERDATA/EMAIL/ADDY");
	for(int i=0;i<nl.getLength();i++) {
	    Element email=(Element) nl.item(i);
	    // Be sure there is no other 'default'
	    email.removeAttribute("default");
	}

	Element email=(Element)getNodeXPath("/USERDATA/EMAIL/ADDY[./text() = '"+s+"']");
	email.setAttribute("default","yes");
	invalidateCache();
    }

    public String getDefaultEmail() 
	    throws WebMailException {
	// XXX Still buggy, check out why
	String v=getValueXPath("/USERDATA/EMAIL/ADDY[@default='yes']/@value");
	if(v == null) {
	    // It should not happen, but take care...
	    throw new WebMailException("There is no default email address!");
	} else {
	    return v;
	}
    }




    public String getTheme() {
	String retval=getValueXPath("/USERDATA/THEME/text()");
	if(retval.equals("")) {
	    return WebMailServer.getDefaultTheme();
	} else {
	    return retval;
	}
    }

    public void setTheme(String theme) {
	setValueXPath("/USERDATA/THEME/text()",theme);
    }

    private String formatDate(long date) {
	TimeZone tz=TimeZone.getDefault();
	DateFormat df=DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT, getPreferredLocale());
	df.setTimeZone(tz);
	String now=df.format(new Date(date));
	return now;
    }	

    public String getFirstLogin() {
	long date=getIntVarWrapper("first login");
	return formatDate(date);
    }

    public String getLastLogin() { 
	long date=getIntVarWrapper("last login");
	return formatDate(date);
    }

    public String getLoginCount() { 
	return getIntVarWrapper("login count")+"";
    }

    public boolean checkPassword(String s) { 
	String password=getValueXPath("/USERDATA/PASSWORD/text()");
	if(password.startsWith(">")) {
	    password=password.substring(1);
	}
	return password.equals(Helper.crypt(password,s));
    }

    public void setPassword(String newpasswd, String verify) throws InvalidPasswordException {
	if(newpasswd.equals(verify)) {
	    Random r=new Random();
	    // Generate the crypted password; avoid problems with XML parsing
	    String crypted=">";
	    while(crypted.lastIndexOf(">") >= 0 || crypted.lastIndexOf("<") >= 0) {
		// This has to be some integer between 46 and 127 for the Helper
		// class
		String seed=(char)(r.nextInt(80)+46) + "" + (char)(r.nextInt(80)+46);
		System.err.println("Seed: "+seed);
		crypted=Helper.crypt(seed,newpasswd);
	    }
	    setValueXPath("/USERDATA/PASSWORD/text()",crypted);
	} else {
	    throw new InvalidPasswordException("The passwords did not match!");
	}
    }

    public void setPasswordData(String data) {
        setValueXPath("/USERDATA/PASSUSERDATA/text()", data);
    }

    public String getPasswordData() {
        return getValueXPath("/USERDATA/PASSUSERDATA/text()");
    }

    public int getMaxLineLength() {
	int retval=(int)getIntVarWrapper("max line length");
	return retval==0?79:retval;
    }

    public void setMaxLineLength(int i) {
	setIntVarWrapper("max line length",i);
    }

    public boolean wantsBreakLines() {
	return getBoolVarWrapper("break lines");
    }

    public void setBreakLines(boolean b) {
	setBoolVarWrapper("break lines",b);
    }

    public boolean wantsShowImages() {
	return getBoolVarWrapper("show images");
    }

    public void setShowImages(boolean b) {
	setBoolVarWrapper("show images",b);
    }

    public boolean wantsShowFancy() { 
	return getBoolVarWrapper("show fancy");
    }
    public void setShowFancy(boolean b) {
	setBoolVarWrapper("show fancy",b);
    }

    public boolean wantsSetFlags() { 
	return getBoolVarWrapper("set message flags");
    }
    public void setSetFlags(boolean b) {
	setBoolVarWrapper("set message flags",b);
    }

    public void setSaveSent(boolean b) {
	setBoolVarWrapper("save sent messages",b);
    }
    public boolean wantsSaveSent() {
	return getBoolVarWrapper("save sent messages");
    }
    public String getSentFolder() { 
	return getValueXPath("/USERDATA/SENT_FOLDER/text()");
    }
    public void setSentFolder(String s) { 
	setValueXPath("/USERDATA/SENT_FOLDER/text()",s);
    }

    public String getDomain() {
	return getValueXPath("/USERDATA/USER_DOMAIN/text()");
    }
    public void setDomain(String s) {
	setValueXPath("/USERDATA/USER_DOMAIN/text()",s);
    }

    /**
     * Return the username without the domain (in contrast to getLogin()).
     * @see getLogin()
     */
    public String getUserName() {
	return getValueXPath("/USERDATA/LOGIN/text()"); 
    }

    public void setUserName(String s) { 
	setValueXPath("/USERDATA/LOGIN/text()",s);
    }

    public void setIntVar(String var, long value) {
	setIntVarWrapper(var,value);
    }

    public long getIntVar(String var) {
	return getIntVarWrapper(var);
    }

    public void setBoolVar(String var, boolean value) {
	setBoolVarWrapper(var,value);
    }

    public boolean getBoolVar(String var) {
	return getBoolVarWrapper(var);
    }

    /**
     * Wrapper method for setting all bool vars
     */
    protected void setIntVarWrapper(String var, long value) {
	if(getIntVarWrapper(var) != value) {
	    ensureElement("INTVAR","name",var);
	    Element e=(Element)getNodeXPath("/USERDATA/INTVAR[@name='"+var+"']");
	    e.setAttribute("value",value+"");
	    invalidateCache();
	}
    }
	
    protected long getIntVarWrapper(String var) {
	ensureElement("INTVAR","name",var);
	long r=0;
	try {
	    //r=Long.parseLong(e.getAttribute("value"));
	    r = Long.parseLong(getValueXPath("/USERDATA/INTVAR[@name='"+var+"']/@value"));
	} catch(NumberFormatException ex) {
	    System.err.println("Warning: Not a valid number in '"+var+"' for user "+
			       getUserName()+"@"+getDomain());
	}
	return r;
    }

    /**
     * Wrapper method for setting all bool vars
     */
    protected void setBoolVarWrapper(String var, boolean value) {
	if(getBoolVarWrapper(var) != value) {
	    ensureElement("BOOLVAR","name",var);
	    Element e=(Element)getNodeXPath("/USERDATA/BOOLVAR[@name='"+var+"']");
	    e.setAttribute("value",value?"yes":"no");
	    invalidateCache();
	}

    }
	
    protected boolean getBoolVarWrapper(String var) {
	ensureElement("BOOLVAR","name",var);
	String value = getValueXPath("/USERDATA/BOOLVAR[@name='"+var+"']/@value");
	return (value.toUpperCase().equals("YES") || value.toUpperCase().equals("TRUE"));
    }

    /**
     * Set all boolvars to "false".
     *
     */
    public void resetBoolVars() {
	NodeList nl=getNodeListXPath("/USERDATA/BOOLVAR");
	for(int i=0;i<nl.getLength();i++) {
	    Element elem=(Element)nl.item(i);
	    elem.setAttribute("value","no");
	}
	invalidateCache();
    }
    
} // XMLUserData
