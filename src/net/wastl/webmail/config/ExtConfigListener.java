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


package net.wastl.webmail.config;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.util.Properties;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileInputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.wastl.webmail.misc.ExpandableProperties;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

/**
 * Purpose<UL>
 * <LI>Set webapp attribute app.name to a unique application name, even
 *     for multiple deployments of the same application distro.
 * <LI>Set webapp attribute rtconfig.dir to the absolute path of a
 *     runtime config directory.  Value is determined dynamically at
 *     runtime, based on the Runtime environment.
 *     The directory is specific to this deployment instance of this web app.
 *     To keep configs independent of the distributable app., the designated
 *     directory should be external to the application.
 * <LI>Load a runtime Properties object from file "meta.properties" in the
 *     rtconfig.dir directory described above, and write the Properties object
 *     to a webapp attribute so the properties will be available to the app.
 * <LI> In addition to primary purposes, also automatically sets Java System
 *      property 'webapps.rtconfig.dir'.
 * </UL>
 * <P>
 * The System Property SHOULD NOT be application-specific or
 * app-instance-specific if the app is to remain portable, since some
 * app servers share one set of System Properties for all web app instances.
 * </P><P>
 * The property or application attribute 'rtAppName' satisfies the need for
 * application-specific switching.
 * Example config files with webapps.rtconfig.dir set to '/local/configs'<UL>
 *    <LI>/local/configs/appa/meta.properties
 *    <LI>/local/configs/appc/meta.properties
 *    <LI>/local/configs/appd/meta.properties
 * </UL>
 * webapps.rtconfig.dir defaults to <CODE>${user.home}</CODE>.
 * Since the app also has access to the rt.configdir value, you can put any
 * and all kinds of runtime resources alongside the meta.properties file.
 * <P>
 * The variables ${rt.configdir} and ${app.name} will be expanded if they
 * occur inside a meta.properties file.
 * The latter allows for safely specifying other files
 * alongside the meta.properties file without worrying about the vicissitudes
 * of relative paths.
 * </P>
 *
 * @author blaine.simpson@admc.com
 */
public class ExtConfigListener implements ServletContextListener {
    //private static Log log = LogFactory.getLog(ExtConfiggableAppContext.class);

    protected String rtAppName = null;

    /**
     * Use webapp application context attribute instead of this setter
     * to configure multiple apps in a single app server JVM.
     */
    public void setRtAppName(String rtAppName) { this.rtAppName = rtAppName; }

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        rtAppName = sc.getInitParameter("defaultRtAppName");
        try {
            Object o = new InitialContext().lookup("java:comp/env/rtAppName");
            rtAppName = (String) o;
            //log.debug("'rtAppName' set by webapp env property");
            sc.log("'rtAppName' set by webapp env property");
        } catch (NameNotFoundException nnfe) {
        } catch (NamingException nnfe) {
            throw new RuntimeException(
                    "Runtime failure when looking up env property", nnfe);
        }
        if (rtAppName == null)
            throw new IllegalStateException(
                    "Required property 'rtAppName' is not set as either a app "
                    + "context init parameter, nor as a webapp JNDI env param");
        sc.log("Initializing configs for runtime app name '"
                + rtAppName + "'");
        String dirProp = System.getProperty("webapps.rtconfig.dir");
        if (dirProp == null) {
            dirProp = System.getProperty("user.home");
            System.setProperty("webapps.rtconfig.dir", dirProp);
        }
        File rtConfigDir = new File(dirProp, rtAppName);
        if (!rtConfigDir.isDirectory())
            throw new IllegalStateException("Runtime properties directory '"
                    + rtConfigDir.getAbsolutePath() + "' not present");
        File metaFile = new File(rtConfigDir, "meta.properties");
        if (!metaFile.isFile())
            throw new IllegalStateException("Runtime meta properties file '"
                    + metaFile.getAbsolutePath() + "' not present");
        ExpandableProperties metaProperties = new ExpandableProperties();
        try {
            metaProperties.load(new FileInputStream(metaFile));
        } catch (IOException ioe) {
            throw new IllegalStateException("Failed to read meta props file '"
                    + metaFile.getAbsolutePath() + "'", ioe);
        }
        Properties expandProps = new Properties();
        expandProps.setProperty("rtconfig.dir", rtConfigDir.getAbsolutePath());
        expandProps.setProperty("app.name", rtAppName);
        metaProperties.expand(expandProps); // Expand ${} properties
        String requiredKeysString;
        requiredKeysString = sc.getInitParameter("requiredMetaPropKeys");
        if (requiredKeysString != null) {
            Set<String> requiredKeys = new HashSet<String>(
                    Arrays.asList(requiredKeysString.split("\\s*,\\s*", -1)));
            requiredKeys.removeAll(metaProperties.keySet());
            if (requiredKeys.size() > 0)
                throw new IllegalStateException(
                        "Meta properties file '"
                        + metaFile.getAbsolutePath()
                        + "' missing required property(s): " + requiredKeys);
        }
        sc.setAttribute("app.name", rtAppName);
        sc.setAttribute("rtconfig.dir", rtConfigDir);
        sc.setAttribute("meta.properties", metaProperties);

        sc.log("'app.name', 'rtconfig.dir', 'meta.properties' "
                + "successfully published to app");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("App '" + rtAppName + "' shutting down.\n"
                + "All Servlets and Filters have been destroyed");
    }
}
