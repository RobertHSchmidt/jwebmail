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

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import javax.mail.Session;
import javax.mail.Provider;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.misc.ByteStore;

import javax.servlet.*;
import javax.servlet.http.*;

import com.oreilly.servlet.multipart.*;

/*
 * WebMailServer.java
 *
 * Created: Oct 1999
 */

/**
 * This is WebMails main server. From here most parts will be administered.
 * This is the servlet implementation of WebMail (introduced in 0.6.1)
 *
 * Created: Tue Feb  2 12:07:25 1999
 *
 * @author Sebastian Schaffert
 * @version $Revision$
 */

/* devink 7/15/2000 - service() now handles a TwoPassAuthenticationException,
 * as does newSession()
 * devink 9/24/2000 - remove TwoPassAuthenticationException stuff
 */
public class WebMailServlet extends WebMailServer implements Servlet {

    ServletConfig srvlt_config;

    /** Size of the chunks that are sent. Must not be greater than 65536 */
    private static final int chunk_size=8192;

    protected String basepath;
    protected String imgbase;

    public WebMailServlet() {
    }

    public void init(ServletConfig config) throws ServletException {
        System.err.println("Init");
        srvlt_config=config;
        this.config=new Properties();
        Enumeration enum=config.getInitParameterNames();
        while(enum.hasMoreElements()) {
            String s=(String)enum.nextElement();
            this.config.put(s,config.getInitParameter(s));
            System.err.println(s+": "+config.getInitParameter(s));
        }

        /*
         * Issue a warning if webmail.basepath and/or webmail.imagebase are
         * not set.
         */
        if(config.getInitParameter("webmail.basepath")==null) {
            config.getServletContext().log("Warning: webmail.basepath initArg should be set to the WebMail Servlet's base path");
            basepath="";
        } else {
            basepath = config.getInitParameter("webmail.basepath");
        }
        if(config.getInitParameter("webmail.imagebase") == null) {
            config.getServletContext().log("Error: webmail.basepath initArg should be set to the WebMail Servlet's base path");
            imgbase="";
        } else {
            imgbase = config.getInitParameter("webmail.imagebase");
        }

        /*
         * Try to get the pathnames from the URL's if no path was given
         * in the initargs.
         */
        if(config.getInitParameter("webmail.data.path") == null) {
            this.config.put("webmail.data.path",getServletContext().getRealPath("/data"));
        }
        if(config.getInitParameter("webmail.lib.path") == null) {
            this.config.put("webmail.lib.path",getServletContext().getRealPath("/lib"));
        }
        if(config.getInitParameter("webmail.template.path") == null) {
            this.config.put("webmail.template.path",getServletContext().getRealPath("/lib/templates"));
        }
        if(config.getInitParameter("webmail.xml.path") == null) {
            this.config.put("webmail.xml.path",getServletContext().getRealPath("/lib/xml"));
        }
        if(config.getInitParameter("webmail.log.facility") == null) {
            this.config.put("webmail.log.facility","net.wastl.webmail.logger.ServletLogger");
        }

        /*
         * Call the WebMailServer's initialization method
         * and forward all Exceptions to the ServletServer
         */
        try {
            doInit();
        } catch(Exception e) {
            e.printStackTrace();
            throw new ServletException("Could not intialize: "+e.getMessage(),e);
        }

    }

    public void debugOut(String msg, Exception ex) {
        if(getDebug()) {
            srvlt_config.getServletContext().log(msg,ex);
        }
    }


    public ServletConfig getServletConfig() {
        return srvlt_config;
    }

    public ServletContext getServletContext() {
        return srvlt_config.getServletContext();
    }

    public String getServletInfo() {
        return getVersion()+"\n(c)2002 by Sebastian Schaffert\nThis software is distributed under the GNU General Public License (GPL)";
    }

    public void destroy() {
        shutdown();
    }

    /**
     * Handle a request to the WebMail servlet.
     *
     * This is the central method of the WebMailServlet. It first gathers all of the necessary information
     * from the client, then either creates or gets a Session and executes the URL handler for the given
     * path.
     */
    public void service(ServletRequest req1, ServletResponse res1) throws ServletException {
        HttpServletRequest req=(HttpServletRequest)req1;
        HttpServletResponse res=(HttpServletResponse)res1;

        HTTPRequestHeader http_header=new HTTPRequestHeader();

        Enumeration en=req.getHeaderNames();
        while(en.hasMoreElements()) {
            String s=(String)en.nextElement();
            http_header.setHeader(s,req.getHeader(s));
        }

        if(req.getPathInfo()!=null) {
            http_header.setPath(req.getPathInfo());
        } else {
            http_header.setPath("/");
        }

        InetAddress addr;
        try {
            addr=InetAddress.getByName(req.getRemoteHost());
        } catch(UnknownHostException e) {
            try {
                addr=InetAddress.getByName(req.getRemoteAddr());
            } catch(Exception ex) {
                throw new ServletException("Remote host must identify!");
            }
        }

        HTMLDocument content=null;
        int err_code=400;
        HTTPSession sess=null;

        /* Here we try to parse the MIME content that the Client sent in his POST
           since the JServ doesn't do that for us:-(
           At least we can use the functionality provided by the standalone server where we need to parse
           the content ourself anyway.
        */
        try {

            BufferedOutputStream out=new BufferedOutputStream(res.getOutputStream());


            /* First we try to use the Servlet API's methods to parse the parameters.
               Unfortunately, it doesn't know how to handle MIME multipart POSTs, so
               we will have to handle that ourselves */


            /* First get all the parameters and set their values into http_header */
            Enumeration enum2=req.getParameterNames();
            while(enum2.hasMoreElements()) {
                String s=(String)enum2.nextElement();
                http_header.setContent(s,req.getParameter(s));
                //getStorage().log(Storage.LOG_INFO,"Parameter "+s);
            }

            /* Then we set all the headers in http_header */
            enum2=req.getHeaderNames();
            while(enum2.hasMoreElements()) {
                String s=(String)enum2.nextElement();
                http_header.setHeader(s,req.getHeader(s));
            }

            /* In Servlet API 2.2 we might want to fetch the attributes also, but this doesn't work
               in API 2.0, so we leave it commented out  */
//          enum2=req.getAttributeNames();
//          while(enum2.hasMoreElements()) {
//              String s=(String)enum2.nextElement();
//              getStorage().log(Storage.LOG_INFO,"Attribute "+s);
//          }


            /* Now let's try to handle multipart/form-data posts */

            if(req.getContentType() != null &&
               req.getContentType().toUpperCase().startsWith("MULTIPART/FORM-DATA")) {


                int size=Integer.parseInt(WebMailServer.getStorage().getConfig("max attach size"));
                MultipartParser mparser = new MultipartParser(req,size);
                Part p;
                while((p = mparser.readNextPart()) != null) {
                    if(p.isFile()) {
                        ByteStore bs = ByteStore.getBinaryFromIS(((FilePart)p).getInputStream(),
                                                                 size);
                        bs.setName(((FilePart)p).getFileName());
                        bs.setContentType(getStorage().getMimeType(((FilePart)p).getFileName()));
                        http_header.setContent(p.getName(),bs);
                        getStorage().log(Storage.LOG_INFO,"File name "+bs.getName());
                        getStorage().log(Storage.LOG_INFO,"Type      "+bs.getContentType());

                    } else if(p.isParam()) {
                        http_header.setContent(p.getName(),((ParamPart)p).getStringValue());
                    }

                    //getStorage().log(Storage.LOG_INFO,"Parameter "+p.getName());
                }

            }


            try {
                String url=http_header.getPath();

                try {
                    /* Find out about the session id */
                    sess=req.getSession(false)==null?null:(HTTPSession)req.getSession(false).getAttribute("webmail.session");

                    /* If the user was logging on, he doesn't have a session id, so generate one.
                       If he already had one, all the better, we will take the old one */
                    if(sess == null && url.startsWith("/login")) {
                        sess=newSession(req,http_header);
                    } else if(sess == null && url.startsWith("/admin/login")) {
                        http_header.setHeader("LOGIN","Administrator");
                        sess=newAdminSession(req,http_header);
                    }
                    if(sess==null && (!url.equals("/") && !url.startsWith("/passthrough") && !url.startsWith("/admin"))) {
                            content=getURLHandler().handleURL("/logout",sess,http_header);
                    } else {

                    /* Ensure that the session state is up-to-date */
                    if(sess != null) sess.setEnv();

                    /* Let the URLHandler determine the result of the query */
                    content=getURLHandler().handleURL(url,sess,http_header);
                }
                } catch(InvalidPasswordException e) {
                    getStorage().log(Storage.LOG_ERR,"Connection to "+addr.toString()+
                                     ": Authentication failed!");
                    if(url.startsWith("/admin/login")) {
                        content=getURLHandler().handleURL("/admin",null,http_header);
                    } else if(url.startsWith("/login")) {
                        content=getURLHandler().handleURL("/",null,http_header);
                    } else {
                        //content=new HTMLErrorMessage(getStorage(),e.getMessage());
                        throw new ServletException("Invalid URL called!");
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                    content=getURLHandler().handleException(ex,sess,http_header);
                    debugOut("Some strange error while handling request",ex);
                }

                /* Set some HTTP headers: Date is now, the document should expire in 5 minutes,
                   proxies and clients shouldn't cache it and all WebMail documents must be revalidated
                   when they think they don't have to follow the "no-cache". */
                res.setDateHeader("Date:",System.currentTimeMillis());
                res.setDateHeader("Expires",System.currentTimeMillis()+300000);
                res.setHeader("Pragma","no-cache");
                res.setHeader("Cache-Control","must-revalidate");

                synchronized(out) {
                    res.setStatus(content.getReturnCode());

                    if(content.hasHTTPHeader()) {
                        Enumeration enum=content.getHTTPHeaderKeys();
                        while(enum.hasMoreElements()) {
                            String s=(String)enum.nextElement();
                            res.setHeader(s,content.getHTTPHeader(s));
                        }
                    }

                    /* What we will send is an image or some other sort of binary */
                    if(content instanceof HTMLImage) {
                        HTMLImage img=(HTMLImage)content;
                        /* the HTMLImage class provides us with most of the necessary information
                           that we want to send */
                        res.setHeader("Content-Type",img.getContentType());
                        res.setHeader("Content-Transfer-Encoding",img.getContentEncoding());
                        res.setHeader("Content-Length",""+img.size());
                        res.setHeader("Connection","Keep-Alive");

                        /* Send 8k junks */
                        int offset=0;
                        while(offset + chunk_size < img.size()) {
                            out.write(img.toBinary(),offset,chunk_size);
                            offset+=chunk_size;
                        }
                        out.write(img.toBinary(),offset,img.size()-offset);
                        out.flush();

                        out.close();
                    } else {
                        byte[] encoded_content=content.toString().getBytes("UTF-8");

                        /* We are sending HTML text. Set the encoding to UTF-8 for Unicode messages */
                        res.setHeader("Content-Length",""+(encoded_content.length+2));
                        res.setHeader("Connection","Keep-Alive");
                        res.setHeader("Content-Type","text/html; charset=\"UTF-8\"");

                        out.write(encoded_content);
                        out.write("\r\n".getBytes());

                        out.flush();

                        out.close();
                    }
                }
            } catch(DocumentNotFoundException e) {
                getStorage().log(Storage.LOG_INFO,"Connection to "+addr.toString()+
                                 ": Could not handle request ("+err_code+") (Reason: "+e.getMessage()+")");
                throw new ServletException("Error: "+e.getMessage(),e);
//              res.setStatus(err_code);
//              res.setHeader("Content-type","text/html");
//              res.setHeader("Connection","close");

//              content=new HTMLErrorMessage(getStorage(),e.getMessage());
//              out.write((content+"\r\n").getBytes("UTF-8"));
//              out.write("</HTML>\r\n".getBytes());
//              out.flush();
//              out.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
            getStorage().log(Storage.LOG_INFO,"Connection to "+addr.toString()+" closed");
            throw new ServletException("Error: "+e.getMessage(),e);
        }
    }

    /**
     * Init possible servers of this main class
     */
    protected void initServers() {
    }

    protected void shutdownServers() {
    }

    public String getBasePath() {
        return basepath;
    }

    public String getImageBasePath() {
        return imgbase;
    }

    public WebMailSession newSession(HttpServletRequest req,HTTPRequestHeader h)
     throws UserDataException, InvalidPasswordException, WebMailException {
        HttpSession sess=req.getSession(true);

        if(sess.getAttribute("webmail.session") == null) {
            WebMailSession n=new WebMailSession(this,req,h);
            timer.addTimeableConnection(n);
            n.login();
            sess.setAttribute("webmail.session",n);
            sessions.put(sess.getId(),n);
            debugOut("Created new Session: "+sess.getId());
            return n;
        } else {
            Object tmp=sess.getAttribute("webmail.session");
            if(tmp instanceof WebMailSession) {
                WebMailSession n=(WebMailSession)tmp;
                n.login();
                debugOut("Using old Session: "+sess.getId());
                return n;
            } else {
                /* If we have an admin session, get rid of it and create a new session */
                sess.setAttribute("webmail.session",null);
                debugOut("Reusing old AdminSession: "+sess.getId());
                return newSession(req,h);
            }
        }
    }

    public AdminSession newAdminSession(HttpServletRequest req, HTTPRequestHeader h) throws InvalidPasswordException, WebMailException {
        HttpSession sess=req.getSession(true);

        if(sess.getAttribute("webmail.session") == null) {
            AdminSession n=new AdminSession(this,req,h);
            timer.addTimeableConnection(n);
            n.login(h);
            sess.setAttribute("webmail.session",n);
            sessions.put(sess.getId(),n);
            debugOut("Created new Session: "+sess.getId());
            return n;
        } else {
            Object tmp=sess.getAttribute("webmail.session");
            if(tmp instanceof AdminSession) {
                AdminSession n=(AdminSession)tmp;
                n.login(h);
                debugOut("Using old Session: "+sess.getId());
                return n;
            } else {
                sess.setAttribute("webmail.session",null);
                debugOut("Reusing old UserSession: "+sess.getId());
                return newAdminSession(req,h);
            }
        }
    }


    /** Overwrite the old session handling methods */

    public WebMailSession newSession(InetAddress a,HTTPRequestHeader h) throws InvalidPasswordException {
        System.err.println("newSession invalid call");
        return null;
    }

    public AdminSession newAdminSession(InetAddress a,HTTPRequestHeader h) throws InvalidPasswordException {
        System.err.println("newAdminSession invalid call");
        return null;
    }

    public HTTPSession getSession(String sessionid, InetAddress a,HTTPRequestHeader h) throws InvalidPasswordException {
        System.err.println("getSession invalid call");
        return null;
    }

    public Enumeration getServers() {
        return new Enumeration() {
                public boolean hasMoreElements() {
                    return false;
                }
                public Object nextElement() {
                    return null;
                }
            };
    }

    public String toString() {
        String s="";
        s+="Server: "+srvlt_config.getServletContext().getServerInfo()+"\n";
        s+="Mount Point: "+getBasePath()+"\n";
        s+=getServletInfo();
        return s;
    }

    public Object getServer(String ID) {
        return null;
    }

    public void reinitServer(String ID) {
    }

    public static String getVersion() {
        return "WebMail/Java Servlet v"+VERSION;
    }

} // WebMailServer
