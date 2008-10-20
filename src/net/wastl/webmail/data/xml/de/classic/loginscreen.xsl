<?xml version="1.0" encoding="UTF-8"?>
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

<!-- This template is part of the German translation -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>

  <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>

  <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Anmeldung</TITLE>
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
                <TABLE CELLSPACING="0" CELLPADDING="20" BORDER="4" bgcolor="#ff0000">
                  <TR>
                    <TD ALIGN="CENTER">
                      <TABLE CELLSPACING="0" CELLPADDING="10" BORDER="0" bgcolor="#ff0000">
                        <TR>
                          <TD COLSPAN="2" ALIGN="CENTER">
                            <IMG SRC="{$imgbase}/images/login_title.png" ALT="WebMail Anmeldung"/></TD>
                        </TR>
                        <TR>
                          <TD WIDTH="50%" ALIGN="RIGHT"><STRONG>Benutzer:</STRONG></TD>
                          <TD WIDTH="50%"><INPUT TYPE="text" NAME="login" SIZE="15"/></TD>
                        </TR>
                        <TR>
                          <TD WIDTH="50%" ALIGN="RIGHT"><STRONG>Passwort:</STRONG></TD>
                          <TD WIDTH="50%"><INPUT TYPE="password" NAME="password" SIZE="15"/></TD>
                        </TR>
                        <TR>
                          <TD WIDTH="50%" ALIGN="RIGHT"><B>Domäne:</B></TD>
                          <TD WIDTH="50%">
                            <SELECT name="vdom">
                              <xsl:for-each select="/GENERICMODEL/SYSDATA/DOMAIN">
                                <OPTION><xsl:apply-templates select="NAME"/></OPTION>
                              </xsl:for-each>
                            </SELECT>
                          </TD>
                        </TR>
                        <TR>
                          <TD ALIGN="CENTER"><INPUT TYPE="submit" value="Anmelden"/></TD>
                          <TD ALIGN="CENTER"><INPUT TYPE="reset" value="Zurücksetzen"/></TD>
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
          <xsl:if test="/GENERICMODEL/STATEDATA/VAR[@name='invalid password']/@value = 'yes'">
            <!-- START invalid pass -->
            <TR>
              <TD COLSPAN="3" ALIGN="CENTER">
                <FONT COLOR="red" SIZE="+1">
                  Anmeldung fehlgeschlagen. Das Passwort war falsch oder Benutzer/Passwortfeld leer. Der Versuch wird protokolliert.
                </FONT>
              </TD>
            </TR>
            <!-- END invalid pass -->
          </xsl:if>
          <TR>
            <TD COLSPAN="3" ALIGN="CENTER">
              <FONT SIZE="-">
                <EM>WebMail ist (c)1999/2000 von <A HREF="mailto:schaffer@informatik.uni-muenchen.de">Sebastian Schaffert</A>. Es kann weiterverbreitet werden unter den Bedingungen der GNU Public License (GPL).</EM>
              </FONT>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="3" ALIGN="CENTER">
              <FONT SIZE="-"><EM><STRONG>Version</STRONG>: WebMail <xsl:value-of select="/GENERICMODEL/STATEDATA/VAR[@name = 'webmail version']/@value"/> on "<xsl:value-of select="/GENERICMODEL/STATEDATA/VAR[@name = 'java virtual machine']/@value"/>", <xsl:value-of select="/GENERICMODEL/STATEDATA/VAR[@name = 'operating system']/@value"/></EM></FONT>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="3" ALIGN="CENTER">
              <FONT SIZE="-"><EM>Die Java-Tasse und "Java" sind Warenzeichen von <A HREF="http://java.sun.com">Sun Microsystems, Inc.</A></EM></FONT>
            </TD>
          </TR>
        </TABLE>
      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>