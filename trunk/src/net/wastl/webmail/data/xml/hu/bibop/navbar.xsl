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
    <xsl:variable name="iconsize" select="/USERMODEL/USERDATA/INTVAR[@name='icon size']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Title Frame</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#818A9E" background="{$imgbase}/images/sfondo_sx.gif" leftmargin="5" topmargin="5" marginwidth="5" marginheight="5">
<TABLE width="50" border="0" cellspacing="0" cellpadding="0">
  <TR>
    <TD align="center">
        <A HREF="{$base}/mailbox?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider shows a list of all folders and links to the FolderList URLHandler.';"><IMG SRC="{$imgbase}/images/mailbox.gif" BORDER="0" ALT="MailboxList" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/mailbox?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider shows a list of all folders and links to the FolderList URLHandler.';">
          <P class="testoScuroSx">Postal�da<BR/>List</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/compose?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider handles the composition of messages.';"><IMG SRC="{$imgbase}/images/composer.gif" BORDER="0" ALT="Composer" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/compose?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider handles the composition of messages.';">
          <P class="testoScuroSx">�zenet<BR/>
        Szerkeszt�</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/folder/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider manages a users folder setup.';"><IMG SRC="{$imgbase}/images/folder_setup.gif" BORDER="0" ALT="FolderSetup" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/folder/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider manages a users folder setup.';">
          <P class="testoScuroSx">Folder<BR/>Setup</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='Change a users settings.';"><IMG SRC="{$imgbase}/images/other_setup.gif" BORDER="0" ALT="UserSetup" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='Change a users settings.';">
          <P class="testoScuroSx">Felhaszn�l�<BR/>
        be�ll�t�sok</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/help?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This is the WebMail help content-provider.';"><IMG SRC="{$imgbase}/images/help.gif" BORDER="0" ALT="WebMailHelp" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
<A HREF="{$base}/help?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This is the WebMail help content-provider.';">
          <P class="testoScuroSx">WebMail<BR/>
        S�g�</P>
        </A>
    </TD>
  </TR>
  <TR>
    <TD align="center">
        <A HREF="{$base}/logout?session-id={$session-id}" TARGET="_top" onMouseOver="self.status='ContentProvider plugin that closes an active WebMail session.';"><IMG SRC="{$imgbase}/images/logout.gif" BORDER="0" ALT="LogoutSession" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/></A>
    </TD>
  </TR>
  <TR>
    <TD height="30" align="center" valign="top">
        <A HREF="{$base}/logout?session-id={$session-id}" TARGET="_top" onMouseOver="self.status='ContentProvider plugin that closes an active WebMail session.';">
          <P class="testoScuroSx">Kil�p�s<BR/>
        Session</P>
        </A>
    </TD>
  </TR>
</TABLE>
        </BODY>


    </HTML>
  </xsl:template>


</xsl:stylesheet>
