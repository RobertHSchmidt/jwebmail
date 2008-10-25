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
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.wastl.webmail.misc.ExpandableProperties;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Purpose<UL>
 * <LI>Set webapp attribute app.contextroot to the app's unique runtime
 *     Context Root.
 *     This will be unique even for multiple deployments of the same
 *     application distro.
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
 * The property contextRoot or application attribute 'context.root'
 * satisfies the need for application-specific switching.
 * Example config files with webapps.rtconfig.dir set to '/local/configs'<UL>
 *    <LI>/local/configs/appa/meta.properties
 *    <LI>/local/configs/appc/meta.properties
 *    <LI>/local/configs/appd/meta.properties
 * </UL>
 * webapps.rtconfig.dir defaults to <CODE>${user.home}</CODE>.
 * Since the app also has access to the rt.configdir value, you can put any
 * and all kinds of runtime resources alongside the meta.properties file.
 * <P>
 * The variables ${rt.configdir} and ${app.contextroot} will be expanded if they
 * occur inside a meta.properties file.
 * The latter allows for safely specifying other files
 * alongside the meta.properties file without worrying about the vicissitudes
 * of relative paths.
 * </P> <P>
 * One would think that the running app could easily detect its own runtime
 * context root, but alas, that's impossible to do in a portable way
 * (until after requests are being served... and that is too late).
 * </P> <P>
 * DESIGN not decided upon yet for handling Context Root of "/"
 * (the default container context root).
 * </P>
 *
 * @author blaine.simpson@admc.com
 */
public class ExtConfigListener implements ServletContextListener {
    /* It's very difficult to choose between camelBack and dot.delimited
     * keys for attributes.  dot.delimited is much more elegant on the
     * configuration side, in .properties and XML files, but these dots
     * break the ability for JavaBean tools and utilities to dereference
     * (e.g. EL, JSTL, Spring).  Also, can't have a getter or setter
     * with a dot in it.
     *
     * Due to the convenience factor, going with dot-delimited until and
     * if this causes us problems.
     */
    private static Log log = LogFactory.getLog(ExtConfigListener.class);

    /** Corresponds to the context.root setting. */
    protected String contextRoot = null;

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        contextRoot = sc.getInitParameter("default.contextroot");
        try {
            Object o = new InitialContext().lookup(
                    "java:comp/env/app.contextroot");
            contextRoot = (String) o;
            log.debug("app.contextroot set by webapp env property");
        } catch (NameNotFoundException nnfe) {
        } catch (NamingException nnfe) {
            throw new RuntimeException(
                    "Runtime failure when looking up env property", nnfe);
        }
        if (contextRoot == null)
            throw new IllegalStateException(
                    "Required setting 'app.contextroot' is not set as either "
                    + "a app webapp JNDI env param, nor by default context "
                    + "init parameter 'default.contextroot'");
        log.info("Initializing configs for runtime app name '"
                + contextRoot + "'");
        String dirProp = System.getProperty("webapps.rtconfig.dir");
        if (dirProp == null) {
            dirProp = System.getProperty("user.home");
            System.setProperty("webapps.rtconfig.dir", dirProp);
        }
        File rtConfigDir = new File(dirProp, contextRoot);
        File metaFile = new File(rtConfigDir, "meta.properties");
        if ((!rtConfigDir.isDirectory()) || !metaFile.isFile()) try {
            installXmlStorage(rtConfigDir, metaFile);
            log.warn("New XML storage system successfully loaded.  "
                    + "Metadata file '" + metaFile.getAbsolutePath() + "'");
        } catch (IOException e) {
            log.fatal("Failed to set up a new XML storage system", e);
            throw new IllegalStateException(
                    "Failed to set up a new XML storage system", e);
        }
        ExpandableProperties metaProperties = new ExpandableProperties();
        try {
            metaProperties.load(new FileInputStream(metaFile));
        } catch (IOException ioe) {
            throw new IllegalStateException("Failed to read meta props file '"
                    + metaFile.getAbsolutePath() + "'", ioe);
        }
        Properties expandProps = new Properties();
        expandProps.setProperty("rtconfig.dir", rtConfigDir.getAbsolutePath());
        expandProps.setProperty("app.contextroot", contextRoot);
        metaProperties.expand(expandProps); // Expand ${} properties
        String requiredKeysString;
        requiredKeysString = sc.getInitParameter("required.metaprop.keys");
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
        sc.setAttribute("app.contextroot", contextRoot);
        sc.setAttribute("rtconfig.dir", rtConfigDir);
        sc.setAttribute("meta.properties", metaProperties);

        log.debug("'app.contextroot', 'rtconfig.dir', 'meta.properties' "
                + "successfully published to app");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        log.info("App '" + contextRoot + "' shutting down.\n"
                + "All Servlets and Filters have been destroyed");
    }

    /**
     * @param baseDir  Parent directory of metaFile
     * @param metaFile Properties file to be created.  IT CAN NOT EXIST YET!
     * @throws IOException if fail to create new XML Storage system
     */
    protected void installXmlStorage(File baseDir, File metaFile)
            throws IOException {
        File dataDir = new File(baseDir, "data");
        if (dataDir.exists())
            throw new IOException("Target data root dir already exists: "
                    + dataDir.getAbsolutePath());
        if (!baseDir.isDirectory()) {
            File parentDir = baseDir.getParentFile();
            if (!parentDir.canWrite())
                throw new IOException("Cannot create base RT directory '"
                        + baseDir.getAbsolutePath() + "'");
            if (!baseDir.mkdir())
                throw new IOException("Failed to create base RT directory '"
                        + baseDir.getAbsolutePath() + "'");
            log.debug("Created base RT dir '"
                    + baseDir.getAbsolutePath() + "'");
        }
        if (!baseDir.canWrite())
            throw new IOException(
                    "Do not have privilegest to create meta file '"
                    + metaFile.getAbsolutePath() + "'");
        if (!dataDir.mkdir())
            throw new IOException("Failed to create data directory '"
                    + dataDir.getAbsolutePath() + "'");
        log.debug("Created data dir '" + dataDir.getAbsolutePath() + "'");
        // In my experience, you can't trust the return values of the
        // File.mkdir() method.  But the file creations or extractions
        // wild fail below in that case, so that's no problem.

        // Could create a Properties object and save it, but why?
        PrintWriter pw = new PrintWriter(new FileWriter(metaFile));
        pw.println("webmail.data.path: ${rtconfig.dir}/data");
        pw.flush();
        pw.close();

        InputStream zipFileStream = getClass().getResourceAsStream(
                "/data.zip");
        if (zipFileStream == null)
            throw new IOException(
                    "Zip file 'data.zip' missing from web application");
        ZipEntry entry;
        File newNode;
        FileOutputStream fileStream;
        long fileSize, bytesRead;
        int i;
        byte[] buffer = new byte[10240];
        ZipInputStream zipStream = new ZipInputStream(zipFileStream);
        try { while ((entry = zipStream.getNextEntry()) != null) {
            newNode = new File(dataDir, entry.getName());
            if (entry.isDirectory()) {
                if (!newNode.mkdir())
                    throw new IOException("Failed to extract dir '"
                            + entry.getName() + "' from 'data.zip' file");
                log.debug("Extracted dir '" + entry.getName() + "' to '"
                        + newNode.getAbsolutePath() + "'");
                zipStream.closeEntry();
                continue;
            }
            fileSize = entry.getSize();
            fileStream = new FileOutputStream(newNode);
            try {
                bytesRead = 0;
                while ((i = zipStream.read(buffer)) > 0) {
                    fileStream.write(buffer, 0, i);
                    bytesRead += i;
                }
                fileStream.flush();
            } finally {
                fileStream.close();
            }
            zipStream.closeEntry();
            if (bytesRead != fileSize)
                throw new IOException("Expected " + fileSize
                        + " bytes for '" + entry.getName()
                        + ", but extracted " + bytesRead
                        + " bytes to '" + newNode.getAbsolutePath() + "'");
            log.debug("Extracted file '" + entry.getName() + "' to '"
                    + newNode.getAbsolutePath() + "'");
        } } finally {
            zipStream.close();
        }
    }
}
