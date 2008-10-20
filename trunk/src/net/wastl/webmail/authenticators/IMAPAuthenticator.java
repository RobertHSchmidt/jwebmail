/* $Id$ */

import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import javax.mail.*;
import net.wastl.webmail.config.*;

import java.util.*;

/**
 * IMAPAuthenticator.java
 *
 *
 * Created: Mon Apr 19 12:03:53 1999
 *
 * @author Sebastian Schaffert
 * @version
 */

public class IMAPAuthenticator extends net.wastl.webmail.server.Authenticator {

    public final String VERSION="1.2";

    private Store st;

    private Storage storage;

    public IMAPAuthenticator() {
	super();
    }

    public String getVersion() {
	return VERSION;
    }

    public void init(Storage store) {
	storage=store;
	Session session=Session.getDefaultInstance(System.getProperties(),null);
	try {
	    st=session.getStore("imap");
	} catch(NoSuchProviderException e) {
	    e.printStackTrace();
	}
    }
	
    public void register(ConfigScheme store) {
	key="IMAP";
	store.configAddChoice("AUTH",key,"Authenticate against an IMAP server on the net. Does not allow password change.");
    }
    
    public void authenticatePreUserData(String user,String domain,String passwd)
     throws InvalidPasswordException { 
	super.authenticatePreUserData(user,domain,passwd);

	WebMailVirtualDomain vd=storage.getVirtualDomain(domain);
	String authhost=vd.getAuthenticationHost();

	try {
	    st.connect(authhost,user,passwd);
	    st.close();
	    storage.log(Storage.LOG_INFO,"IMAPAuthentication: user "+user+
			" authenticated successfully (imap host: "+authhost+").");
	} catch(MessagingException e) {
	    storage.log(Storage.LOG_WARN,"IMAPAuthentication: user "+user+
			" authentication failed (imap host: "+authhost+").");
	    //e.printStackTrace();
	    throw new InvalidPasswordException("IMAP authentication failed!");
	}
    }

    public boolean canChangePassword() {
	return false;
    }

} // IMAPAuthenticator
