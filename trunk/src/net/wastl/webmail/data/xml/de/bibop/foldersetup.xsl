<?xml version="1.0" encoding="UTF-8"?>
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
  <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Ordnereinstellungen</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">

        <TABLE width="100%" border="0" cellspacing="2" cellpadding="4">
          <TR>
            <TD colspan="2" height="22" class="testoNero">
              <IMG SRC="{$imgbase}/images/icona_folder.gif" align="absmiddle"/>
            JWebMail Ordnereinstellungen für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=folder-setup">Hilfe</A>)
                </TD>
            </TR>
           <TR>
            <TD colspan="4" bgcolor="#697791" height="22" class="testoBianco">
                 Benutzer<xsl:value-of select="normalize-space(/USERMODEL/USERDATA/LOGIN)"/><BR/>
                Kennung existiert seit <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/>
           </TD>
          </TR>
          <TR>
            <TD colspan="2" bgcolor="#A6B1C0" height="22" class="testoGrande">
                Du hast folgende Möglichkeiten:
            </TD>
          </TR>
          <TR>
            <TD height="22" width="23%" class="testoNero" bgcolor="#E2E6F0">
              <A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox">Briefkästen einrichten</A>
            </TD>
            <TD height="22" class="testoNero" width="77%" bgcolor="#E2E6F0">
            Mit WebMail kannst Du mehrere Briefkästen gleichzeitig verwalten, die auf dem gleichen oder auf verschiedenen IMAP Servern liegen. Hier kannst Du neue Briefkästen hinzufügen und bestehende entfernen.
            </TD>
          </TR>
          <TR>
            <TD height="22" width="23%" bgcolor="#D1D7E7" class="testoNero">
              <A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=folder">(Unter-)Ordner einrichten</A>
            </TD>
            <TD height="22" class="testoNero" width="77%" bgcolor="#D1D7E7">
            Du bekommst eine Baumsicht Deiner Briefkästen und kannst Ordner hinzufügen und löschen, verstecken und wieder anzeigen.</TD>
          </TR>
        </TABLE>
      </BODY>

    </HTML>
  </xsl:template>

</xsl:stylesheet>
