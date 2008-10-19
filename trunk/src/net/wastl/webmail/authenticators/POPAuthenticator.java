/* $Id$ */

import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import javax.mail.*;
import net.wastl.webmail.config.*;
import net.wastl.webmail.storage.StorageManager;
import net.wastl.webmail.logger.Logger;

import java.util.*;

/**
 * POPAuthenticator.java
 *
 *
 * Created: Mon Apr 19 12:03:53 1999
 *
 * @author Sebastian Schaffert
 * @version
 * devink 7/15/2000 - changed some leftover "imap" strings to "pop"
 */

public class POPAuthenticator extends net.wastl.webmail.server.Authenticator {

    public final String VERSION="1.2";

    private Store st;

    private StorageManager storage;

    public POPAuthenticator() {
	super();
    }

    public String getVersion() {
	return VERSION;
    }

    public void init(StorageManager store) {
	storage=store;
	Session session=Session.getDefaultInstance(System.getProperties(),null);
	try {
	    st=session.getStore("pop3");
	} catch(NoSuchProviderException e) {
	    e.printStackTrace();
	}
    }
	
    public void register(ConfigScheme store) {
	key="POP3";
	store.configAddChoice("AUTH",key,"Authenticate against an POP3 server on the net. Does not allow password change.");
    }
    
    public void authenticatePreUserData(String user,String domain,String passwd)
     throws InvalidPasswordException {
	super.authenticatePreUserData(user,domain,passwd);

	WebMailVirtualDomain vd=storage.getVirtualDomain(domain);
	String authhost=vd.getAuthenticationHost();

	try {
	    st.connect(authhost,user,passwd);
	    st.close();
	    storage.getLogger().log(Logger.LOG_INFO,"POPAuthentication: user "+user+
			" authenticated successfully (pop host: "+authhost+").");
	} catch(MessagingException e) {
	    storage.getLogger().log(Logger.LOG_WARN,"POPAuthentication: user "+user+
			" authentication failed (pop host: "+authhost+").");
	    //e.printStackTrace();
	    throw new InvalidPasswordException("POP authentication failed!");
	}
    }

    public boolean canChangePassword() {
	return false;
    }

} // POPAuthenticator
