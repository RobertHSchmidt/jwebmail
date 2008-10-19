/* CVS ID: $Id$ */
package net.wastl.webmail.storage;


/*
 * StorageManager.java
 *
 * Created: Feb 2002
 *
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
 */
import org.w3c.dom.*;

import java.io.*;
import java.util.*;
import java.text.Collator;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import net.wastl.webmail.logger.*;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.storage.data.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.xml.*;
import net.wastl.webmail.config.*;

/**
 * This class provides an interface to all storable data in JWebMail. Depending 
 * on the configuration, different means of storage are used for different data.
 *
 * 
 * @author Sebastian Schaffert
 * @version $Revision$
 */
public class StorageManager implements ConfigurationListener {

    protected Logger logger;
    protected XMLSystemData sysdata = null;

    protected AttributedExpireableCache cache;
    protected AttributedExpireableCache user_cache;

    protected WebMailServlet parent;

    protected Hashtable caches, mime_types, resources;

    protected Authenticator auth;


    public StorageManager(WebMailServlet parent) throws WebMailException {
	this.parent = parent;
	initConfig();
	initLog();
	initCache();
	initAuth();
    }

    /* ------------------------- Shutdown methods ------------------- */

    public void shutdown() {
	try {
	    sysdata.save();
	} catch(WebMailException ex) {
	    ex.printStackTrace();
	}
    }



    /* ------------------------- Initialisation methods ------------------- */

    protected void initConfig() throws WebMailException {
	sysdata = getSystemData();
	sysdata.setConfigScheme(parent.getConfigScheme());
    }


    public void initConfigKeys() {
	// Initialize the configuration file with the default or set parameters
	// needed to complete the XML file
	Enumeration enum=parent.getConfigScheme().getPossibleKeys();
	while(enum.hasMoreElements()) {
	    String key=(String)enum.nextElement();
	    if(!sysdata.isConfigSet(key)) {
		// We must use the raw method so the input doesn't get filtered.
		sysdata.setConfig(key,(String)parent.getConfigScheme().getDefaultValue(key),false,false);
	    }
	}
	try {
	    sysdata.save();
	} catch(WebMailException ex) {
	    getLogger().log(Logger.LOG_ERR,"Could not save system configuration");
	}
    }


    protected void initCache() throws WebMailException {
	caches = new Hashtable();

	NodeList cachelist = getSystemData().getNodeListXPath("/SYSDATA/cache");
	for(int i=0;i<cachelist.getLength();i++) {
	    Element entry = (Element)cachelist.item(i);
	    String name = entry.getAttribute("name");
	    System.err.print("  * "+getSystemData().getProperty("/SYSDATA/cache[@name='"+name+"']/description/text()")+" ... ");
	    
	    int size = 40;
	    float factor = (float)0.9;
	    try {
		size = Integer.parseInt(this.getSystemData().getProperty("/SYSDATA/cache[@name='"+name+"']/size/text()"));
	    } catch(NumberFormatException ex) {}
	    try {
		factor = Float.parseFloat(this.getSystemData().getProperty("/SYSDATA/cache[@name='"+name+"']/threshold/text()"));
	    } catch(NumberFormatException ex) {}
	    caches.put(name,new AttributedExpireableCache(size,factor));
	    System.err.println(size+" entries, threshold "+factor+".");
	}
	
	cache = (AttributedExpireableCache)caches.get("stylesheet");
	user_cache = (AttributedExpireableCache)caches.get("user");
    }



    protected void initLog() {
	System.err.print("  * Logging ... ");
	if(true) {
	    try {
		Class logger_class=Class.forName(parent.getProperty("webmail.log.facility"));
		Class[] argtypes={Class.forName("net.wastl.webmail.server.WebMailServer"),
				  Class.forName("net.wastl.webmail.server.Storage")};
		Object[] args={parent,this};
		logger=(Logger)logger_class.getConstructor(argtypes).newInstance(args);
		System.err.println("ok. Using "+logger.getClass().getName()+" to log messages.");
	    } catch(Exception ex) {
		// Print a warning!
		logger=new FileLogger(parent,this);
		System.err.println("Could not initalise "+parent.getProperty("webmail.log.facility")+". Using "+logger.getClass().getName()+" to log messages.");
	    }
	} else {
	    try {
		logger=new ServletLogger(parent,this);
	    } catch(Exception ex) {
		// Print a warning!
		logger=new FileLogger(parent,this);
	    }

	}
    }
	
    protected void initAuth() {
	System.err.print("  * Authenticator ... ");
	Authenticator a=parent.getAuthenticatorHandler().getAuthenticator(getConfig("AUTH"));
	if(a!=null) {
	    // IMAP level authentication
	    auth=a;
	    auth.init(this);
	    System.err.println("ok. Using "+auth.getClass().getName()+" (v"+auth.getVersion()+") for authentication.");
	} else {
	    System.err.println("error. Could not initalize any authenticator. Users will not be able to log on.");
	    auth=null;
	}
    }
    
	
    protected void initMIME() {
	System.err.print("  * MIME types ... ");
	if(getConfig("mime types") != null) {
	    try {
		File f=new File(getConfig("mime types"));
		if(f.exists() && f.canRead()) {
		    mime_types=new Hashtable();
		    BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		    String line=in.readLine();
		    while(line != null) {
			if(!line.startsWith("#")) {
			    StringTokenizer tok=new StringTokenizer(line);
			    if(tok.hasMoreTokens()) {
				String type=tok.nextToken();
				while(tok.hasMoreTokens()) {
				    String key=tok.nextToken();
				    mime_types.put(key,type);
				    //System.err.println(key+" -> "+type);
				}
			    }
			}
			line=in.readLine();
		    }
		    in.close();
		    System.err.println(" loaded from "+getConfig("mime types")+".");
		} else {
		    System.err.println(" could not find "+getConfig("mime types")+". Will use standard MIME types.");
		}
	    } catch(IOException ex) {
		System.err.println(" could not find "+getConfig("mime types")+". Will use standard MIME types.");
	    }
	} else {
	    System.err.println(" not configured. Will use standard MIME types.");
	}
    }
    
    protected void initLanguages() throws WebMailException {
	System.err.print("  * Available languages ... ");
	File f=new File(parent.getProperty("webmail.template.path")+System.getProperty("file.separator"));
	String[] flist=f.list(new FilenameFilter() {
		public boolean accept(File myf, String s) {
		    if(myf.isDirectory() && s.equals(s.toLowerCase()) && (s.length()==2 || s.equals("default"))) {
			return true;
		    } else {
			return false;
		    }
		}
	    });

	File cached=new File(parent.getProperty("webmail.data.path")+System.getProperty("file.separator")+"locales.cache");
	Locale[] available1=null;

	/* Now we try to cache the Locale list since it takes really long to gather it! */
	boolean exists=cached.exists();
	if(exists) {
	    try {
		ObjectInputStream in=new ObjectInputStream(new FileInputStream(cached));
		available1=(Locale[])in.readObject();
		in.close();
		System.err.print(" using disk cache ... ");
	    } catch(Exception ex) {
		exists=false;
	    }
	}
	if(!exists) {
	    // We should cache this on disk since it is so slow!
	    available1=Collator.getAvailableLocales();
	    try {
		ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(cached));
		os.writeObject(available1);
		os.close();
	    } catch(Exception ex) {
		ex.printStackTrace();
	    }
	}

	// Do this manually, as it is not JDK 1.1 compatible ...
	//Vector available=new Vector(Arrays.asList(available1));
	Vector available=new Vector(available1.length);
	for(int i=0; i<available1.length; i++) {
	    available.addElement(available1[i]);
	}
	String s="";
	int count=0;       
	for(int i=0;i<flist.length;i++) {
	    String cur_lang=flist[i];
	    Locale loc=new Locale(cur_lang,"","");
	    Enumeration enum=available.elements();
	    boolean added=false;
	    while(enum.hasMoreElements()) {
		Locale l=(Locale)enum.nextElement();
		if(l.getLanguage().equals(loc.getLanguage())) {
		    s+=l.toString()+" ";
		    count++;
		    added=true;
		}
	    }
	    if(!added) {
		s+=loc.toString()+" ";
		count++;
	    }
	}
	System.err.println(count+" languages initialized.");
	parent.getConfigScheme().configRegisterStringKey(this,"LANGUAGES",s,"Languages available in WebMail");
	getSystemData().setConfig("LANGUAGES",s);
	
	/*
	  Setup list of themes for each language
	*/
	for(int j=0;j<flist.length;j++) {
	    File themes=new File(parent.getProperty("webmail.template.path")+System.getProperty("file.separator")
				 +flist[j]+System.getProperty("file.separator"));
	    String[] themelist=themes.list(new FilenameFilter() {
		    public boolean accept(File myf, String s3) {
			if(myf.isDirectory() && !s3.equals("CVS")) {
			    return true;
			} else {
			    return false;
			}
		    }
		});
	    String s2="";
	    for(int k=0;k<themelist.length;k++) {
		s2+=themelist[k]+" ";
	    }
	    parent.getConfigScheme().configRegisterStringKey(this,"THEMES_"+flist[j].toUpperCase(),s2,"Themes for language "+flist[j]);
	    getSystemData().setConfig("THEMES_"+flist[j].toUpperCase(),s2);
	}
    }


    /* ---------------------------- Member Methods ------------------------- */
    

    /**
     * Get the system configuration with the given key.
     * deprecated use xpath queries instead
     */
    public String getConfig(String key) {
	try {
	    return getSystemData().getConfig(key);
	} catch(Exception ex) {
	    ex.printStackTrace();
	    return null;
	}
    }

//     /**
//      * Set/add a WebMail virtual domain
//      */
//     public void setVirtualDomain(String name,WebMailVirtualDomain v) {
// 	sysdata.setVirtualDomain(name,v);
// 	try {
// 	    sysdata.save();
// 	} catch(WebMailException ex) {
// 	    getLogger().log(Logger.LOG_ERR,"Could not save system configuration");
// 	    getLogger().log(Logger.LOG_ERR,ex);
// 	}
//     }

    /**
     * Get a WebMail virtual domain
     */
    public WebMailVirtualDomain getVirtualDomain(String name) {
	return sysdata.getVirtualDomain(name);
    }

    public WebMailVirtualDomain createVirtualDomain(String name) throws Exception {
	sysdata.createVirtualDomain(name);
	return sysdata.getVirtualDomain(name);
    }

    /**
     * Delete a WebMail virtual domain
     */
    public void deleteVirtualDomain(String name) {
	sysdata.deleteVirtualDomain(name);
    }

    /**
     * Return a list of virtual domains
     */
    public Enumeration getVirtualDomains() {
	return sysdata.getVirtualDomains();
    }


    public Logger getLogger() {
	return logger;
    }


    public Authenticator getAuthenticator() {
	return auth;
    }


    public XMLUserModel createXMLUserModel(XMLUserData data) throws WebMailException {
	try {
	    return new XMLUserModel(parent,sysdata.getSysData(),data.getUserData());
	} catch(ParserConfigurationException ex) {
	    throw new WebMailException("Creating the generic XML model failed. Reason: "+
				       ex.getMessage());
	}
    }

    /**
     * Return a XML model that contains state and system information for administrator use   
     */
    public XMLAdminModel createXMLAdminModel() throws WebMailException {
	try {
	    XMLAdminModel model=new XMLAdminModel(parent,sysdata.getSysData());
	    model.init();
	    model.update();
	return model;
	} catch(ParserConfigurationException ex) {
	    throw new WebMailException("Creating the generic XML model failed. Reason: "+
				       ex.getMessage());
	}
    }

    /**
     * Return a generic XML model that only contains some state and system information.
     * This cannot be changed by a single session.
     */
    public XMLGenericModel createXMLGenericModel() throws WebMailException {
	try {
	    XMLGenericModel model=new XMLGenericModel(parent,sysdata.getSysData());
	    model.init();
	    model.update();
	    return model;
	} catch(ParserConfigurationException ex) {
	    throw new WebMailException("Creating the generic XML model failed. Reason: "+
				       ex.getMessage());
	}
    }


    public XMLUserData createUserData(String user, String domain, String password) 
	throws CreateUserDataException {
	XMLUserData data;
	String template="templates"+System.getProperty("file.separator")+"userdata.xml";

	File f=new File(template);
	if(!f.exists()) {
	    getLogger().log(Logger.LOG_WARN,"SimpleStorage: User configuration template ("+template+") doesn't exist!");
	    throw new CreateUserDataException("User configuration template ("+template+") doesn't exist!",user,domain);
	} else if(!f.canRead()) {
	    getLogger().log(Logger.LOG_WARN,"SimpleStorage: User configuration template ("+template+") is not readable!");
	    throw new CreateUserDataException("User configuration template ("+template+") is not readable!",user,domain);
	}
	
	try {
	    Properties options = new Properties();
	    options.put("webmail.cache.size","40");
	    options.put("webmail.cache.expire",".9");
	    options.put("webmail.data.path",
			"file://"+parent.getProperty("webmail.data.path")+System.getProperty("file.separator"));
	    Storage st = Storage.getInstance(parent.getProperty("webmail.storage.user"),options);
	    try {
		data = (XMLUserData)st.loadStorable(template,
						    Class.forName("net.wastl.webmail.xml.XMLUserData"));
	    } catch(Exception ex) {
		throw new WebMailException("A fatal error occured.",ex);
	    }


	    data.init(user,domain,password);
	    if(getConfig("SHOW ADVERTISEMENTS").toUpperCase().equals("YES")) {
	        if(domain.equals("")) {
		  data.setSignature(user+"\n\n"+
		  		    getConfig("ADVERTISEMENT MESSAGE"));
		} else {
		  data.setSignature(user+"@"+domain+"\n\n"+
				    getConfig("ADVERTISEMENT MESSAGE"));
		}
	    } else {
	        if(domain.equals("")) {
		  data.setSignature(user);
		} else {
		  data.setSignature(user+"@"+domain);
		}
	    }
	    data.setTheme(parent.getDefaultTheme());
	    WebMailVirtualDomain vdom=getVirtualDomain(domain);
	    data.addMailHost("Default",getConfig("DEFAULT PROTOCOL")+"://"+
			     vdom.getDefaultServer(),user,password);
	    	    
	} catch(Exception ex) {
	    getLogger().log(Logger.LOG_WARN,"SimpleStorage: User configuration template ("+template+") exists but could not be parsed");
	    throw new CreateUserDataException("User configuration template ("+template+") exists but could not be parsed",user,domain);
	}
	return data;
    }



    /**
     * Return userlist for a given domain.
     */
    public Enumeration getUsers(String domain) {
	throw new NoSuchMethodError("getUsers() not yet implemented!");
    }

    /**
     * @deprecated Use getUsers(String domain) instead
     */
    public Enumeration getUsers() {
	final Enumeration domains=getVirtualDomains();
	return new Enumeration() {
		Enumeration enum=null;
		public boolean hasMoreElements() {
		    return (domains.hasMoreElements() || (enum != null && enum.hasMoreElements()));
		}
		public Object nextElement() {
		    if(enum == null || !enum.hasMoreElements()) {
			if(domains.hasMoreElements()) {
			    enum=getUsers((String)domains.nextElement());
			} else {
			    return null;
			}
		    }
		    return enum.nextElement();
		}
	    };
    }


    /**
     * Get the String for key and the specified locale.
     * @param key Identifier for the String
     * @param locale locale of the String to fetch
     */
    public String getStringResource(String key, Locale locale) {
	if(resources.get(locale.getLanguage()) != null) {
		String s = ((ResourceBundle)resources.get(locale.getLanguage())).getString(key);
	    return ((ResourceBundle)resources.get(locale.getLanguage())).getString(key);
	} else {
	    try {
	    // Modified by exce, start.
		// ResourceBundle rc=XMLResourceBundle.getBundle("resources",locale,null);
		System.err.println("Loading locale");
		ResourceBundle rc = ResourceBundle.getBundle("org.bulbul.webmail.xmlresource.Resources", locale);
		// Modified by exce, end.
		resources.put(locale.getLanguage(),rc);
		return rc.getString(key);
	    } catch(Exception e) {
		e.printStackTrace();
		return "";
	    }
	}
    }


    /** 
     * Get a xsl stylesheet for the specified locale and theme.
     * @param key Identifier for the String
     * @param locale locale of the String to fetch
     * @param theme theme where to look for the file
     */
    public Templates getStylesheet(String name, Locale locale, String theme) 
	throws WebMailException {

	String key = locale.getLanguage()+"/"+theme+"/"+name;

	Templates stylesheet=null;
	    
	String basepath=getBasePath(locale,theme);

	File f=new File(basepath+name);
	if(!f.exists()) {
	    throw new StylesheetNotFoundException("The requested stylesheet "+name+" could not be found (path tried: "+basepath+".");
	}

	if(cache != null && cache.get(key) != null && 
	   ((Long)cache.getAttributes(key)).longValue() >= f.lastModified()) {
	    // Keep statistics :-)
	    cache.hit();
	    return (Templates)cache.get(key);
	} else {
	    try {
		StreamSource msg_xsl=new StreamSource("file://"+basepath+name);
		TransformerFactory factory=TransformerFactory.newInstance();
		stylesheet=factory.newTemplates(msg_xsl);
		if(cache != null) {
		    cache.put(key,stylesheet, new Long(f.lastModified()));
		    cache.miss();
		}
	    } catch(Exception ex) { 
		throw new WebMailException("Error while compiling stylesheet "+name+", language="+locale.getLanguage()+", theme="+theme+":\n"+ex.toString()); 
	    }
	    return stylesheet;
	}

    }

    
    public XMLSystemData getSystemData() throws WebMailException {
	if(sysdata == null) {
	    System.err.print("  * System Configuration ... ");
	    Properties options = new Properties();
	    options.put("webmail.cache.size","40");
	    options.put("webmail.cache.expire",".9");
	    options.put("webmail.data.path",
			"file://"+parent.getProperty("webmail.data.path")+System.getProperty("file.separator"));
	    Storage loader = Storage.getInstance(parent.getProperty("webmail.storage.system"),options);
	    try {
		sysdata = (XMLSystemData)loader.loadStorable("webmail.xml",
							     Class.forName("net.wastl.webmail.xml.XMLSystemData"));
	    } catch(ClassNotFoundException ex) {
		ex.printStackTrace();
		throw new WebMailException("A fatal error occured.",ex);
	    }
	    System.err.println("ok.");
	}	
	return sysdata;
    }


    /**
     * Get the userdata for the specified user.
     *
     * @param user Name of the user
     * @param domain Virtual Domain name of the user
     * @param passwd Password that the user provided
     *
     */
    public XMLUserData getUserData(String user, String domain, String password, 
				   boolean authenticate) 
	 throws WebMailException
    {
	if(authenticate) {
	    auth.authenticatePreUserData(user,domain,password);
	}
		
	if(user.equals("")) {
	    return null;
	}

	String key = user+"/"+domain;

 	XMLUserData data = null;
	if(user_cache != null) 
	    data=(XMLUserData)user_cache.get(key);
	else
	    throw new WebMailException("Error. User cache was not initialised! Check system configuration!");

	if(data == null) {
	    user_cache.miss();
	    String filename=System.getProperty("file.separator")+domain+
		System.getProperty("file.separator")+user+".xml";
	    Properties options = new Properties();
	    options.put("webmail.cache.size","40");
	    options.put("webmail.cache.expire",".9");
	    options.put("webmail.data.path",
			"file://"+parent.getProperty("webmail.data.path")+System.getProperty("file.separator"));
	    Storage st = Storage.getInstance(parent.getProperty("webmail.storage.user"),options);
	    try {
		data = (XMLUserData)st.loadStorable(filename,
						    Class.forName("net.wastl.webmail.xml.XMLUserData"));
	    } catch(WebMailException ex) {
		logger.log(logger.LOG_WARN,ex);
		data=createUserData(user,domain,password);
	    } catch(ClassNotFoundException ex) {
		throw new WebMailException("A fatal error occured.",ex);
	    }
	    if(user_cache != null)
		user_cache.put(key,data);
	} else {	    
	    user_cache.hit();
	}


	if(authenticate) {
	    auth.authenticatePostUserData(data,domain,password);
	}
	return data;
    }

    public void saveUserData(String user, String domain) throws WebMailException {
	String key = user+"/"+domain;
 	XMLUserData data = null;
	if(user_cache != null) 
	    data=(XMLUserData)user_cache.get(key);
	else
	    throw new WebMailException("Error. User cache was not initialised! Check system configuration!");
	data.save();
    }


    public void deleteUserData(String user, String domain) {
	throw new NoSuchMethodError("deleteUserData not implemented!");
    }


    /* ------------------------------ Helper Methods ----------------------------- */

    /**
     * Calculate the document base path for a given locale and theme
     */
    public String getBasePath(Locale locale, String theme) {
	String language_path=(parent.getProperty("webmail.template.path")+
			      System.getProperty("file.separator")+locale.getLanguage());
	File f=new File(language_path);
	if(!f.exists()) {
	    language_path=(parent.getProperty("webmail.template.path")+
			   System.getProperty("file.separator")+parent.getDefaultLocale().getLanguage());
	    f=new File(language_path);
	    if(!f.exists()) {
		System.err.println("Storage::getBasePath: Default Language templates not found \n(tried path: "+language_path+")");
	    }
	}
	String theme_path=language_path+System.getProperty("file.separator")+theme;
	f=new File(theme_path);
	if(!f.exists()) {
	    if(parent.getProperty("webmail.default.theme") != null) {
		theme_path=language_path+System.getProperty("file.separator")+
		    parent.getProperty("webmail.default.theme");
	    } else {
		theme_path=language_path+System.getProperty("file.separator")+
		    "bibop";
	    }
	    f=new File(theme_path);
	    if(!f.exists()) {
		System.err.println("Storage::getBasePath: Theme could not be found; probably a problem with your\n installation. Please check the lib/templates/bibop directory and the \nwebmail.default.theme property");
	    }
	}
	String basepath=theme_path+System.getProperty("file.separator");
	return basepath;
    }	

    public String getMimeType(String name) {
	String content_type;
	if(name != null && (name.toLowerCase().endsWith("jpg") || name.toLowerCase().endsWith("jpeg"))) {
	    content_type="IMAGE/JPEG";
	} else if(name != null && name.toLowerCase().endsWith("gif")) {
	    content_type="IMAGE/GIF";
	} else if(name != null && name.toLowerCase().endsWith("png")) {
	    content_type="IMAGE/PNG";
	} else if(name != null && name.toLowerCase().endsWith("txt")) {
	    content_type="TEXT/PLAIN";
	} else if(name != null && (name.toLowerCase().endsWith("htm") || name.toLowerCase().endsWith("html"))) {
	    content_type="TEXT/HTML";
	} else {
	    content_type="APPLICATION/OCTET-STREAM";
	}
	return content_type;
    }


    public void notifyConfigurationChange(String key) {
	getLogger().log(Logger.LOG_DEBUG,"StorageManager: Configuration change notify for key "+key+".");
	if(key.toUpperCase().startsWith("AUTH")) {
	    initAuth();
	} else if(key.toUpperCase().startsWith("MIME")) {
	    initMIME();
	}
    }

}
