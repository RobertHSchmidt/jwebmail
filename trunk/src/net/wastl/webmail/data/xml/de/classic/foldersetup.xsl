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
  
    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Ordner einrichten</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
      </HEAD>
      
      <BODY bgcolor="#ffffff">

        <TABLE BGCOLOR="#dddddd" CELLSPACING="0" BORDER="0">
          <TR>
            <TD VALIGN="CENTER">
              <IMG SRC="{$imgbase}/images/btn-folders.png"/>
            </TD>
            <TD VALIGN="CENTER">
              <FONT SIZE="+2"><STRONG>JWebMail Ordnereinstellungen für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></STRONG></FONT> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=folder-setup">Hilfe</A>) <BR/>
              <EM>Benutzer <xsl:value-of select="normalize-space(/USERMODEL/USERDATA/LOGIN)"/></EM><BR/>
              <EM>Kennung existiert seit <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/></EM>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="2" BGCOLOR="#aaaaaa"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></TD>
          </TR>
          <TR>
            <TD COLSPAN="2"><STRONG>Du hast folgende Möglichkeiten:</STRONG></TD>
          </TR>
          <TR>
            <TD>              
              <A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox">Briefkästen einrichten</A>
            </TD>
            <TD>
              Mit WebMail kannst Du mehrere Briefkästen gleichzeitig verwalten, die auf dem gleichen oder auf verschiedenen IMAP Servern liegen. Hier kannst Du neue Briefkästen hinzufügen und bestehende entfernen.
            </TD>
          </TR>
          <TR>
            <TD>              
              <A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=folder">(Unter)Ordner einrichten</A>
            </TD>
            <TD>
              Du bekommst eine Baumsicht Deiner Briefkästen und kannst Ordner hinzufügen und löschen, verstecken und
              wieder anzeigen.
            </TD>
          </TR>
        </TABLE>
      </BODY>

    </HTML>
  </xsl:template>  

</xsl:stylesheet>
