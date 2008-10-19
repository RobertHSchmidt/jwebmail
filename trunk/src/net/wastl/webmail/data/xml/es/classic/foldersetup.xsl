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
