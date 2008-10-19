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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>
  
    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Folder Setup</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
      </HEAD>
      
      <BODY bgcolor="#ffffff">

        <TABLE BGCOLOR="#dddddd" CELLSPACING="0" BORDER="0" WIDTH="100%">
          <TR>
            <TD VALIGN="CENTER">
              <IMG SRC="{$imgbase}/images/btn-folders.png"/>
            </TD>
            <TD VALIGN="CENTER" COLSPAN="2">
              <FONT SIZE="+2"><STRONG>JWebMail Folder Setup for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></STRONG></FONT> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=folder-setup-mailboxes">Help</A>)<BR/>
              <EM>Login name <xsl:value-of select="normalize-space(/USERMODEL/USERDATA/LOGIN)"/></EM><BR/>
              <EM>Account exists since <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/></EM>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="3" BGCOLOR="#aaaaaa" ALIGN="CENTER"><STRONG>Remove Mailboxes</STRONG></TD>
          </TR>
          <xsl:for-each select="/USERMODEL/USERDATA/MAILHOST">      
            <TR>
              <TD><STRONG><xsl:value-of select="@name"/></STRONG></TD>
              <TD><EM><xsl:apply-templates select="MH_URI"/></EM></TD>
              <TD><A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox&amp;remove={@id}">Remove</A></TD>           
            </TR>
          </xsl:for-each>
          <TR>
            <TD COLSPAN="3"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></TD>
          </TR>
          <TR>
            <TD COLSPAN="3" BGCOLOR="#aaaaaa" ALIGN="CENTER"><STRONG>Add Mailbox</STRONG></TD>
          </TR>
          <TR>
            <TD COLSPAN="3">
              <FORM ACTION="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox&amp;add=1" METHOD="POST">
                <TABLE WIDTH="100%">
                  <TR>
                    <TD><STRONG>Mailbox Name:</STRONG></TD>
                    <TD COLSPAN="3"><INPUT TYPE="text" SIZE="30" NAME="mbox_name"/></TD>
                  </TR>
                  <TR>
                    <TD><STRONG>Hostname:</STRONG></TD>
                    <TD><INPUT TYPE="text" SIZE="20" NAME="mbox_host"/></TD>
                    <TD><STRONG>Protocol:</STRONG></TD>
                    <TD>
                      <SELECT NAME="mbox_proto">
                        <xsl:for-each select="/USERMODEL/STATEDATA/VAR[@name='protocol']">
                          <xsl:choose>
                            <xsl:when test="@value = /USERMODEL/SYSDATA//CONFIG[KEY='DEFAULT PROTOCOL']/VALUE">
                              <OPTION selected="selected"><xsl:value-of select="@value"/></OPTION>
                            </xsl:when>
                            <xsl:otherwise>
                              <OPTION><xsl:value-of select="@value"/></OPTION>
                            </xsl:otherwise>
                          </xsl:choose>
                        </xsl:for-each>         
                      </SELECT>
                    </TD>
                  </TR>
                  <TR>
                    <TD><STRONG>Login:</STRONG></TD>
                    <TD><INPUT TYPE="text" SIZE="10" NAME="mbox_login"/></TD>
                    <TD><STRONG>Password:</STRONG></TD>
                    <TD><INPUT TYPE="PASSWORD" SIZE="10" NAME="mbox_password"/></TD>
                  </TR>
                  <TR>
                    <TD COLSPAN="4" ALIGN="CENTER"><INPUT TYPE="submit" NAME="submit" VALUE="Add Mailbox"/></TD>
                  </TR>
                </TABLE>
              </FORM>
            </TD>
          </TR>
        </TABLE>
      </BODY>

    </HTML>
  </xsl:template>  

  <xsl:template match="/USERMODEL/STATEDATA/VAR">
    <xsl:value-of select="@value"/>
  </xsl:template>                   


</xsl:stylesheet>
