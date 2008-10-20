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
  <xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
    <xsl:variable name="iconsize" select="/USERMODEL/USERDATA/INTVAR[@name='icon size']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>Boite aux Lettres WebMail de <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Page de Titre</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#818A9E" background="{$imgbase}/images/sfondo_sx.gif" leftmargin="5" topmargin="5" marginwidth="5" marginheight="5">
<TABLE width="50" border="0" cellspacing="0" cellpadding="0">
  <TR>
    <TD align="center">
        <A HREF="{$base}/mailbox?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='affiche la liste de tous les dossiers et liens.';"><IMG SRC="{$imgbase}/images/mailbox.gif" BORDER="0" ALT="Liste des Boites" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/mailbox?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='affiche la liste de tous les dossiers et liens.';">
          <P class="testoScuroSx">Liste<BR/>des Boîtes</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/compose?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='gere la composition des messages.';"><IMG SRC="{$imgbase}/images/composer.gif" BORDER="0" ALT="Composer Message" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/compose?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='gere la composition des messages.';">
          <P class="testoScuroSx">Composer<BR/>
        Message</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/folder/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='gere le parametrage dossier de l utilisateur.';"><IMG SRC="{$imgbase}/images/folder_setup.gif" BORDER="0" ALT="Setup Dossier" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/folder/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='gere le parametrage dossier de l utilisateur.';">
          <P class="testoScuroSx">Setup<BR/>Dossier</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='change les parametres utilisateurs.';"><IMG SRC="{$imgbase}/images/other_setup.gif" BORDER="0" ALT="Setup Utilisateur" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='change les parametres utilisateurs.';">
          <P class="testoScuroSx">Setup<BR/>
        Utilisateur</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/help?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='fournit l Aide WebMail.';"><IMG SRC="{$imgbase}/images/help.gif" BORDER="0" ALT="Aide WebMail" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
<A HREF="{$base}/help?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='fournit l Aide WebMail.';">
          <P class="testoScuroSx">Aide<BR/>
        WebMail</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/logout?session-id={$session-id}" TARGET="_top" onMouseOver="self.status='ferme une session WebMail active.';"><IMG SRC="{$imgbase}/images/logout.gif" BORDER="0" ALT="Logout Session" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/logout?session-id={$session-id}" TARGET="_top" onMouseOver="self.status='ferme une session WebMail active.';">
          <P class="testoScuroSx">Logout<BR/>
        Session</P>
        </A>
    </TD>
  </TR>
</TABLE>
        </BODY>


    </HTML>
  </xsl:template>


</xsl:stylesheet>
