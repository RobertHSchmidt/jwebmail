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
  <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
  <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
  <xsl:variable name="iconsize" select="/USERMODEL/USERDATA/INTVAR[@name='icon size']/@value"/>
  <xsl:template match="/">
    <HTML>
      <HEAD>
        <TITLE>Buz&243;n de WebMail para <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Ventana principal</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>
      <BODY bgcolor="#dddddd">
        <A HREF="{$base}/mailbox?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider shows a list of all folders and links to the FolderList URLHandler.';">
          <IMG SRC="{$imgbase}/images/btn-mailbox.png" BORDER="0" ALT="MailboxList" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/>
        </A>
        <BR/>
        <A HREF="{$base}/compose?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider handles the composition of messages.';">
          <IMG SRC="{$imgbase}/images/btn-compose.png" BORDER="0" ALT="Composer" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/>
        </A>
        <BR/>
        <A HREF="{$base}/folder/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This ContentProvider manages a users folder setup.';">
          <IMG SRC="{$imgbase}/images/btn-folders.png" BORDER="0" ALT="FolderSetup" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/>
        </A>
        <BR/>
        <A HREF="{$base}/setup?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='Change a users settings.';">
          <IMG SRC="{$imgbase}/images/btn-setup.png" BORDER="0" ALT="UserSetup" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/>
        </A>
        <BR/>
        <A HREF="{$base}/help?session-id={$session-id}" TARGET="Main" onMouseOver="self.status='This is the WebMail help content-provider.';">
          <IMG SRC="{$imgbase}/images/btn-help.png" BORDER="0" ALT="WebMailHelp" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/>
        </A>
        <BR/>
        <A HREF="{$base}/logout?session-id={$session-id}" TARGET="_top" onMouseOver="self.status='ContentProvider plugin that closes an active WebMail session.';">
          <IMG SRC="{$imgbase}/images/btn-logout.png" BORDER="0" ALT="LogoutSession" WIDTH="{$iconsize}" HEIGTH="{$iconsize}"/>
        </A>
        <BR/>
      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>
