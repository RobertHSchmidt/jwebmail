/* CVS ID: $Id$ */
import net.wastl.webmail.config.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.server.http.*;
import net.wastl.webmail.ui.html.*;
import net.wastl.webmail.ui.xml.*;
import net.wastl.webmail.xml.*;
import net.wastl.webmail.exceptions.*;
import net.wastl.webmail.storage.data.*;

import net.wastl.webmail.logger.Logger;
import net.wastl.webmail.session.admin.AdminSession;

import java.util.*;

/**
 * AdminPlugin.java
 *
 * Created: Thu Sep  9 16:52:36 1999
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

public class AdminPlugin implements Plugin, URLHandler {

    public static final String VERSION="1.3";
    public static final String URL="/admin";

    protected Vector sessions;

    WebMailServer parent;

    public AdminPlugin() {
        sessions=new Vector();
    }

    public void register(WebMailServer parent) {
        parent.getURLHandler().registerHandler(URL,this);
        this.parent=parent;
    }

    public String getName() {
        return "Administrator";
    }

    public String getDescription() {
        return "Change WebMail settings";
    }

    public String getVersion() {
        return VERSION;
    }

    public String getURL() {
        return URL;
    }

    protected void reqShutdown(int time, boolean reboot) {
        new ShutdownThread(time,reboot,parent);
    }

    public HTMLDocument handleURL(String suburl, HTTPSession session, HTTPRequestHeader header) throws WebMailException {
        HTMLDocument content=null;
        if(session != null) {
            session.setLastAccess();
            session.setEnv();
        }
        if(session == null && !(suburl.equals("/") || suburl.equals(""))) {
            throw new DocumentNotFoundException("Could not continue as there was no session id submitted");
        } else if(suburl.startsWith("/login")) {
            System.err.println("Admin login ... ");
            content=new XHTMLDocument(session.getModel(),
                                      parent.getStorage().getStylesheet("admin-frame.xsl",
                                                                        parent.getDefaultLocale(),
                                                                        parent.getProperty("webmail.default.theme")));

        } else if(suburl.startsWith("/system")) {
            if(suburl.startsWith("/system/set")) {
                XMLSystemData sysdata=parent.getStorage().getSystemData();
                Enumeration enum=sysdata.getConfigKeys();
                while(enum.hasMoreElements()) {
                    String ckey=(String)enum.nextElement();
                    if(header.isContentSet(ckey)) {
//                      System.err.println(ckey+" = "+header.getContent(ckey));
                        sysdata.setConfig(ckey,header.getContent(ckey));
                    }
                }
                parent.getStorage().getSystemData().save();
                session.setEnv();
            }
            content=new XHTMLDocument(session.getModel(),
                                      parent.getStorage().getStylesheet("admin-system.xsl",
                                                                        parent.getDefaultLocale(),
                                                                        parent.getProperty("webmail.default.theme")));

        } else if(suburl.startsWith("/navigation")) {
            content=new XHTMLDocument(session.getModel(),
                                      parent.getStorage().getStylesheet("admin-navigation.xsl",
                                                                        parent.getDefaultLocale(),
                                                                        parent.getProperty("webmail.default.theme")));
            // Modified by exce, end
        } else if(suburl.startsWith("/control")) {
            if(suburl.startsWith("/control/kill")) {
                String sid=header.getContent("kill");

                parent.getStorage().getLogger().log(Logger.LOG_INFO,"Session "+sid+": removing on administrator request.");

                HTTPSession sess2=parent.getSession(sid);
                if(sess2 != null) {
                    parent.removeSession(sess2);
                }
                session.setEnv();
            }
            if(header.isContentSet("SHUTDOWN")) {
                int time=0;
                try {
                    time=Integer.parseInt(header.getContent("SHUTDOWN SECONDS"));
                } catch(NumberFormatException e) {}
                reqShutdown(time,false);
            } else if(header.isContentSet("REBOOT")) {
                int time=0;
                try {
                    time=Integer.parseInt(header.getContent("SHUTDOWN SECONDS"));
                } catch(NumberFormatException e) {}
                reqShutdown(time,true);
            }

            content=new XHTMLDocument(session.getModel(),
                                      parent.getStorage().getStylesheet("admin-status.xsl",
                                                                        parent.getDefaultLocale(),
                                                                        parent.getProperty("webmail.default.theme")));

        } else if(suburl.startsWith("/domain")) {
            if(suburl.startsWith("/domain/set")) {

                try {
                    Enumeration enum=parent.getStorage().getVirtualDomains();
                    while(enum.hasMoreElements()) {
                        String s1=(String)enum.nextElement();
                        if(header.getContent("CHANGE "+s1) != null && !header.getContent("CHANGE "+s1).equals("")) {
                            WebMailVirtualDomain vd=parent.getStorage().getVirtualDomain(s1);
                            if(!vd.getDomainName().equals(header.getContent(s1+" DOMAIN"))) {
                                vd.setDomainName(header.getContent(s1+" DOMAIN"));
                            }
                            vd.setDefaultServer(header.getContent(s1+" DEFAULT HOST"));
                            vd.setAuthenticationHost(header.getContent(s1+" AUTH HOST"));
                            vd.setHostsRestricted(header.getContent(s1+" HOST RESTRICTION")!=null);
                            vd.setAllowedHosts(header.getContent(s1+" ALLOWED HOSTS"));
                            //parent.getStorage().setVirtualDomain(s1,vd);
                        } else if(header.getContent("DELETE "+s1) != null && !header.getContent("DELETE "+s1).equals("")) {
                            parent.getStorage().deleteVirtualDomain(s1);
                        }
                    }
                    if(header.getContent("ADD NEW") != null && !header.getContent("ADD NEW").equals("")) {
                        WebMailVirtualDomain vd=parent.getStorage().createVirtualDomain(header.getContent("NEW DOMAIN"));
                        vd.setDomainName(header.getContent("NEW DOMAIN"));
                        vd.setDefaultServer(header.getContent("NEW DEFAULT HOST"));
                        vd.setAuthenticationHost(header.getContent("NEW AUTH HOST"));
                        vd.setHostsRestricted(header.getContent("NEW HOST RESTRICTION")!=null);
                        vd.setAllowedHosts(header.getContent("NEW ALLOWED HOSTS"));
                        //parent.getStorage().setVirtualDomain(header.getContent("NEW DOMAIN"),vd);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                parent.getStorage().getSystemData().save();
                session.setEnv();
            }

            content=new XHTMLDocument(session.getModel(),
                                      parent.getStorage().getStylesheet("admin-domains.xsl",
                                                                        parent.getDefaultLocale(),
                                                                        parent.getProperty("webmail.default.theme")));

        } else if(suburl.startsWith("/user")) {
            if(header.isContentSet("domain")) {
                ((AdminSession)session).selectDomain(header.getContent("domain"));
            }

            if(suburl.startsWith("/user/edit") &&
               (header.isContentSet("edit") || header.isContentSet("change"))) {
                if(header.isContentSet("user")) {
                    ((AdminSession)session).selectUser(header.getContent("user"));
            /* 10/22/2000 devink -- setup new password changing stuff */
            ((AdminSession)session).setupUserEdit();

                    if(header.isContentSet("change")) {
                        ((AdminSession)session).changeUser(header);
                    }

                } else {
                    ((AdminSession)session).clearUser();
                }

                content=new XHTMLDocument(session.getModel(),
                                        parent.getStorage().getStylesheet("admin-edituser.xsl",
                                                                        parent.getDefaultLocale(),
                                                                        parent.getProperty("webmail.default.theme")));

            } else {
                if(header.isContentSet("user") && header.isContentSet("delete")) {
                    ((AdminSession)session).deleteUser(header.getContent("user"));
                }
                content=new XHTMLDocument(session.getModel(),
                                        parent.getStorage().getStylesheet("admin-users.xsl",
                                                                        parent.getDefaultLocale(),
                                                                        parent.getProperty("webmail.default.theme")));
            }
        } else {
            if(suburl.startsWith("/logout")) {
                session.logout();
            }
            //content=new HTMLDocument("WebMail Administrator Login",parent.getStorage(),"adminlogin",parent.getBasePath());
            XMLGenericModel model = parent.getStorage().createXMLGenericModel();
            if(header.isContentSet("login")) {
                model.setStateVar("invalid password","yes");
            }
            content=new XHTMLDocument(model.getRoot(),parent.getStorage().getStylesheet("admin-login.xsl",parent.getDefaultLocale(),parent.getProperty("webmail.default.theme")));
        }
        return content;
    }

    public String provides() {
        return "admin";
    }

    public String requires() {
        return "";
    }


    protected class ShutdownThread extends Thread {

        protected WebMailServer parent;

        protected int time;
        protected boolean reboot;

        ShutdownThread(int time, boolean restart, WebMailServer parent) {
            this.parent=parent;
            this.time=time;
            this.reboot=restart;
            this.start();
        }

        public void run() {
            String action=reboot?"reboot":"shutdown";
            if(time >=0) {
                System.err.println("\n*** WebMail "+action+" in "+time+" seconds! ***\n");
                parent.getStorage().getLogger().log(Logger.LOG_CRIT,"*** WebMail "+action+" in "+time+" seconds! ***");
                try {
                    Thread.sleep(time*1000);
                } catch(InterruptedException ex) {}
                System.err.println("\n*** WebMail "+action+" NOW! ***\n");
                if(reboot) {
                    parent.restart();
                } else {
                    parent.shutdown();
                }
            }
        }
    }

} // AdminPlugin
