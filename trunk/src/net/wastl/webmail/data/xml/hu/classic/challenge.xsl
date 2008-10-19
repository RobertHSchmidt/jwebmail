<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
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
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

  <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
  <xsl:variable name="login" select="/GENERICMODEL/STATEDATA/VAR[@name='login']/@value"/>
  <xsl:variable name="challenge" select="/GENERICMODEL/STATEDATA/VAR[@name='challenge']/@value"/>
  <xsl:variable name="challenged" select="/GENERICMODEL/STATEDATA/VAR[@name='challenged']/@value"/>
  <xsl:variable name="vdom" select="/GENERICMODEL/STATEDATA/VAR[@name='vdom']/@value"/>

  <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Login Screen</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <TABLE WIDTH="100%">
          <TR>
            <TD COLSPAN="3" ALIGN="CENTER" HEIGHT="70">
            </TD>
          </TR>
          <TR>
            <TD WIDTH="20%" VALIGN="TOP"><IMG SRC="{$imgbase}/images/java_powered.png" ALT="Java powered"/></TD>
            <TD WIDTH="60%" ALIGN="CENTER">
              <FORM ACTION="{$base}/login" METHOD="POST" NAME="loginForm">
           <!-- Make sure we get all the needed login values -->
           <INPUT TYPE="hidden" NAME="login" VALUE="{$login}"/>
           <INPUT TYPE="hidden" NAME="challenged" VALUE="{$challenged}"/>
           <INPUT TYPE="hidden" NAME="vdom" VALUE="{$vdom}"/>
                <TABLE CELLSPACING="0" CELLPADDING="20" BORDER="4" bgcolor="#ff0000">
                  <TR>
                    <TD ALIGN="CENTER">
                      <TABLE CELLSPACING="0" CELLPADDING="10" BORDER="0" bgcolor="#ff0000">
                        <TR>
                          <TD COLSPAN="2" ALIGN="CENTER">
                            <IMG SRC="{$imgbase}/images/login_title.png" ALT="WebMail login"/></TD>
                        </TR>
            <TR>
              <TD WIDTH="50%" ALIGN="RIGHT"><STRONG>Login:</STRONG></TD>
              <TD WIDTH="50%">
               <xsl:value-of select="$login"/>
              </TD>
            </TR>
                        <TR>
                          <TD WIDTH="50%" ALIGN="RIGHT"><STRONG>Challenge:</STRONG></TD>
              <TD WIDTH="50%">
               <xsl:value-of select="$challenge"/>
              </TD>
                        </TR>
                        <TR>
                          <TD WIDTH="50%" ALIGN="RIGHT"><STRONG>Response:</STRONG></TD>
                          <TD WIDTH="50%"><INPUT TYPE="text" NAME="password" SIZE="35"/></TD>
                        </TR>
                        <TR>
                          <TD ALIGN="CENTER"><INPUT TYPE="submit" value="Login"/></TD>
                          <TD ALIGN="CENTER"><INPUT TYPE="reset" value="Reset"/></TD>
                        </TR>
                      </TABLE>
                    </TD>
                  </TR>
                </TABLE>
              </FORM>
            </TD>
            <TD WIDTH="20%">
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="3" ALIGN="CENTER">
              <FONT SIZE="-1">
                <EM>WebMail is (c)1999/2000 by <A HREF="mailto:schaffer@informatik.uni-muenchen.de">Sebastian Schaffert</A>. It is distributed under the terms of the GNU Public License (GPL).</EM>
              </FONT>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="3" ALIGN="CENTER">
              <FONT SIZE="-1"><EM><STRONG>Version</STRONG>: WebMail <xsl:value-of select="/GENERICMODEL/STATEDATA/VAR[@name = 'webmail version']/@value"/> on "<xsl:value-of select="/GENERICMODEL/STATEDATA/VAR[@name = 'java virtual machine']/@value"/>", <xsl:value-of select="/GENERICMODEL/STATEDATA/VAR[@name = 'operating system']/@value"/></EM></FONT>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="3" ALIGN="CENTER">
              <FONT SIZE="-1"><EM>The Java Coffee Cup and "Java" are trademarks of <A HREF="http://java.sun.com">Sun Microsystems, Inc.</A></EM></FONT>
            </TD>
          </TR>
        </TABLE>
      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>
