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
  <xsl:template match="/">
    <HTML>
      <HEAD>
        <TITLE>WebMail Casilla de correo para <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Folder Setup</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
      </HEAD>
      <BODY bgcolor="#ffffff">
        <TABLE BGCOLOR="#dddddd" CELLSPACING="0" BORDER="0">
          <TR>
            <TD VALIGN="CENTER">
              <IMG SRC="{$imgbase}/images/btn-folders.png"/>
            </TD>
            <TD VALIGN="CENTER"><FONT SIZE="+2"><STRONG>JWebMail Configuraci&#243;n de carpeta para <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></STRONG></FONT> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=folder-setup">Ayuda</A>) <BR/><EM>Nombre de usuario<xsl:value-of select="normalize-space(/USERMODEL/USERDATA/LOGIN)"/></EM><BR/><EM>La cuenta existe desde <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/></EM></TD>
          </TR>
          <TR>
            <TD COLSPAN="2" BGCOLOR="#aaaaaa">
              <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="2">
              <STRONG>Tienes las siguientes opciones:</STRONG>
            </TD>
          </TR>
          <TR>
            <TD>
              <A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=mailbox">Agregar/Borrar Casilla de correo </A>
            </TD>
            <TD>
	      WebMail permite tener varias conexiones a servidores IMAP y POP. Puedes agregar o quitar esas conexiones aqu&#237;.
	    </TD>
          </TR>
          <TR>
            <TD>
              <A HREF="{$base}/folder/setup?session-id={$session-id}&amp;method=folder">Agregar/Borrar subcarpetas</A>
            </TD>
            <TD>
	      WebMail te mostrar&#225; un &#225;rbol de carpetas para cada casilla donde puedes elegir entre agregar o
	      borrar subcarpetas individuales.
	    </TD>
          </TR>
        </TABLE>
      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>
