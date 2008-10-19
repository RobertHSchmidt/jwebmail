/* $Id$ */
import net.wastl.webmail.server.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.config.ConfigScheme;
import net.wastl.webmail.storage.StorageManager;

/**
 * SimpleAuthenticator.java
 *
 * Does simple authentication just based on the UserData checkPasswd() 
 *
 * Created: Mon Apr 19 11:17:03 1999
 *
 * @author Sebastian Schaffert
 * @version 1.0
 * @see webmail.server.UserData
 */
public class SimpleAuthenticator extends Authenticator {
    
    public final String VERSION="1.0";

    public SimpleAuthenticator() {
	super();
    }

    public String getVersion() {
	return VERSION;
    }

    
    public void init(StorageManager store) {
    }

    public void register(ConfigScheme store) {
	key="SIMPLE";
	store.configAddChoice("AUTH",key,"Very simple style authentication. First login sets password. Password may be changed.");
    }	

    public void authenticatePostUserData(UserData udata, String domain,String password) throws InvalidPasswordException {
	if(!udata.checkPassword(password) || password.equals("")) {
	    throw new InvalidPasswordException();
	}
    }

    public void changePassword(UserData udata, String passwd, String verify) throws InvalidPasswordException {
	udata.setPassword(passwd,verify);
    }
} // SimpleAuthenticator
