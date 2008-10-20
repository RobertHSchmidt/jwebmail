<?xml version="1.0" encoding="ISO-8859-2"?>
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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Mappa beállítás</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">

        <TABLE width="100%" border="0" cellspacing="2" cellpadding="4">
         <FORM ACTION="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox&amp;add=1" METHOD="POST">
          <TR>
            <TD colspan="4" height="22" class="testoNero">
              <IMG SRC="{$imgbase}/images/icona_folder.gif" align="absmiddle"/>
               WebMail mappa beállítások <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/> számára (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=folder-setup-mailboxes">Segítség</A>)
            </TD>
           </TR>
           <TR>
            <TD colspan="4" bgcolor="#697791" height="22" class="testoBianco">
                Bejelentkezési név <xsl:value-of select="normalize-space(/USERMODEL/USERDATA/LOGIN)"/><BR/>
                Az azonosító <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/> óta létezik
           </TD>
          </TR>
          <TR>
            <TD bgcolor="#A6B1C0" colspan="4" height="22" align="center" class="testoGrande">Levélfiók eltávolítása</TD>
          </TR>
          <xsl:for-each select="/USERMODEL/USERDATA/MAILHOST">
            <TR>
              <TD width="25%" height="35" class="testoNero" bgcolor="#E2E6F0">
                <SPAN class="bold">
                  <xsl:value-of select="@name"/>
                </SPAN>
              </TD>
              <TD width="50%" colspan="2" bgcolor="#D1D7E7" class="testoNero">
                <xsl:apply-templates select="MH_URI"/>
              </TD>
              <TD width="25%" class="testoNero" bgcolor="#E2E6F0"><A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox&amp;remove={@id}">Eltávolít</A></TD>
            </TR>
          </xsl:for-each>
          <TR>
            <TD bgcolor="#A6B1C0" colspan="4" height="22" align="center" class="testoGrande">Levélfiók hozzáadása</TD>
          </TR>
          <TR>
            <TD width="25%" bgcolor="#E2E6F0" class="testoNero">Levélfiók neve:</TD>
            <TD width="75%" colspan="3" class="testoNero" bgcolor="#D1D7E7">
              <INPUT TYPE="text" SIZE="25" NAME="mbox_name" class="testoNero"/>
            </TD>
          </TR>
          <TR>
            <TD width="25%" class="testoNero" bgcolor="#E2E6F0">Gazdanév:</TD>
            <TD width="25%" class="testoNero" bgcolor="#D1D7E7"><INPUT TYPE="text" SIZE="10" NAME="mbox_host" class="testoNero"/>
            </TD>
            <TD width="25%" class="testoNero" bgcolor="#E2E6F0">Protokoll:</TD>
            <TD width="25%" class="testoNero" bgcolor="#D1D7E7">
              <SELECT NAME="mbox_proto" class="testoNero">
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
            <TD width="25%" class="testoNero" bgcolor="#E2E6F0">Login:
            </TD>
            <TD width="25%" class="testoNero" bgcolor="#D1D7E7">
              <INPUT TYPE="text" SIZE="10" NAME="mbox_login" class="testoNero"/>
            </TD>
            <TD width="25%" class="testoNero" bgcolor="#E2E6F0">Jelszó:
            </TD>
            <TD width="25%" class="testoNero" bgcolor="#D1D7E7">
              <INPUT TYPE="PASSWORD" SIZE="10" NAME="mbox_password" class="testoNero"/>
            </TD>
          </TR>
          <TR>
            <TD colspan="4" align="center" class="testoNero" bgcolor="#A6B1C0">
              <INPUT TYPE="submit" NAME="submit" VALUE="Fiók hozzáadása" class="testoNero"/>
            </TD>
          </TR>
        </FORM>
      </TABLE>
    </BODY>

    </HTML>
  </xsl:template>

  <xsl:template match="/USERMODEL/STATEDATA/VAR">
    <xsl:value-of select="@value"/>
  </xsl:template>


</xsl:stylesheet>
