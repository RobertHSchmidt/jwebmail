<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- $Id$ -->

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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xalan="http://xml.apache.org/xslt" version="1.0">
  <xsl:output method="html" indent="yes" xalan:content-handler="org.apache.xalan.serialize.SerializerToHTML"/>

  <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
  <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

  <xsl:variable name="work" select="/USERMODEL/WORK/MESSAGE[position()=1]"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>Buz&#243;n de WebMail para <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Resultado de enviar mensaje</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="WebMail 0.7 XSL"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">
        <TABLE width="100%" border="0" cellspacing="2" cellpadding="4">
          <TR>
            <TD height="22" class="testoNero">
              <IMG SRC="{$imgbase}/images/icona_composer.gif" BORDER="0" align="absmiddle"/>Resultado de enviar mensaje
            </TD>
          </TR>
          <TR>
            <TD bgcolor="#697791" height="22" class="testoBianco">Fecha: <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/></TD>
          </TR>
          <TR>
            <TD class="testoGrande" bgcolor="#A6B1C0">Enviar mensaje<BR/>
             De: <SPAN class="testoNero"><xsl:value-of select="$work/HEADER/FROM"/></SPAN><BR/>
              Asunto: <SPAN class="testoNero"><xsl:value-of select="$work/HEADER/SUBJECT"/></SPAN><BR/>
              Para: <SPAN class="testoNero"><xsl:value-of select="$work/HEADER/TO"/></SPAN><BR/>
              Fecha: <SPAN class="testoNero"><xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/></SPAN>
            </TD>
          </TR>
          <TR>
            <TD class="testoGrande" bgcolor="#D1D7E7">Estado del env&#237;o:
                <SPAN class="testoNero"><xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='send status']/@value"/>
                </SPAN>
            </TD>
        </TR>
        <TR>
         <TD class="testoGrande" bgcolor="#E2E6F0">
          <!-- Only show the section for valid addresses if there actually were any -->
          <xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid sent addresses']/@value != ''">
               Entregado a las direcciones: <SPAN class="testoNero">v&#225;lidas&#160;&#160;<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='valid sent addresses']/@value"/></SPAN>
          </xsl:if>
         </TD>
        </TR>
        <TR>
          <TD class="testoGrande" bgcolor="#E2E6F0">
          <!-- Only show the section for invalid addresses if there actually were any -->
          <xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value != '' or /USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value != ''">
            No entregado a las direcciones:
              <SPAN class="testoNero">
                <xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value != ''">v&#225;lidas&#160;&#160;
<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value"/>
                </xsl:if></SPAN>
            <SPAN class="testoNero"><xsl:if test="/USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value != ''">
              inv&#225;lidas&#160;&#160;<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value"/>
            </xsl:if></SPAN>
          </xsl:if>
          </TD>
        </TR>
        <TR>
          <TD bgcolor="#697791">
        <TABLE border="0" cellpadding="0" cellspacing="0"><TR><TD>
        <A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><IMG SRC="{$imgbase}/images/back.gif" BORDER="0"/></A></TD><TD><A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><SPAN class="testoBianco"> Volver a la ventana de redactar ...</SPAN></A></TD></TR></TABLE>
          </TD>
        </TR>
      </TABLE>


      </BODY>
    </HTML>
  </xsl:template>



</xsl:stylesheet>
