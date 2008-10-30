<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
$Id$

Copyright 2008 by the JWebMail Development Team and Sebastian Schaffert.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->
<!-- This is part of the French translation of WebMail - Christian SENET - senet@lpm.u-nancy.fr - 2002 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>Boite aux Lettres WebMail de <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Setup Dossier</TITLE>
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
              <FONT SIZE="+2"><STRONG>Setup Dossier WebMail de <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></STRONG></FONT> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=folder-setup-mailboxes">Aide</A>)<BR/>
              <EM>Nom de login <xsl:value-of select="normalize-space(/USERMODEL/USERDATA/LOGIN)"/></EM><BR/>
              <EM>Compte existant depuis le <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/></EM>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="3" BGCOLOR="#aaaaaa" ALIGN="CENTER"><STRONG>Supprimer Bo�tes aux Lettres</STRONG></TD>
          </TR>
          <xsl:for-each select="/USERMODEL/USERDATA/MAILHOST">
            <TR>
              <TD><STRONG><xsl:value-of select="@name"/></STRONG></TD>
              <TD><EM><xsl:apply-templates select="MH_URI"/></EM></TD>
              <TD><A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox&amp;remove={@id}">Supprimer</A></TD>
            </TR>
          </xsl:for-each>
          <TR>
            <TD COLSPAN="3"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></TD>
          </TR>
          <TR>
            <TD COLSPAN="3" BGCOLOR="#aaaaaa" ALIGN="CENTER"><STRONG>Ajouter Boite aux Lettres</STRONG></TD>
          </TR>
          <TR>
            <TD COLSPAN="3">
              <FORM ACTION="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox&amp;add=1" METHOD="POST">
                <TABLE WIDTH="100%">
                  <TR>
                    <TD><STRONG>Nom Bo�te aux lettres:</STRONG></TD>
                    <TD COLSPAN="3"><INPUT TYPE="text" SIZE="30" NAME="mbox_name"/></TD>
                  </TR>
                  <TR>
                    <TD><STRONG>H�te serveur de Messagerie:</STRONG></TD>
                    <TD><INPUT TYPE="text" SIZE="20" NAME="mbox_host"/></TD>
                    <TD><STRONG>Protocole:</STRONG></TD>
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
                    <TD><STRONG>Mot de passe:</STRONG></TD>
                    <TD><INPUT TYPE="PASSWORD" SIZE="10" NAME="mbox_password"/></TD>
                  </TR>
                  <TR>
                    <TD COLSPAN="4" ALIGN="CENTER"><INPUT TYPE="submit" NAME="submit" VALUE="Ajouter Boite aux Lettres"/></TD>
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
