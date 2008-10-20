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

  <xsl:variable name="work" select="/USERMODEL/WORK/MESSAGE[position()=1]"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Ergebnis der Nachrichtenzustellung</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="WebMail 0.7 XSL"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">
        <TABLE width="100%" border="0" cellspacing="2" cellpadding="4">
          <TR>
            <TD height="22" class="testoNero">
              <IMG SRC="{$imgbase}/images/icona_composer.gif" BORDER="0" align="absmiddle"/>Status der Nachrichtenzustellung
            </TD>
          </TR>
          <TR>
            <TD bgcolor="#697791" height="22" class="testoBianco">Date: <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/></TD>
          </TR>
          <TR>
            <TD class="testoGrande" bgcolor="#A6B1C0">Sende Nachricht<BR/>
              Betreff: <SPAN class="testoNero"><xsl:value-of select="$work/HEADER/SUBJECT"/></SPAN><BR/>
              An: <SPAN class="testoNero"><xsl:value-of select="$work/HEADER/TO"/></SPAN><BR/>
              Datum: <SPAN class="testoNero"><xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/></SPAN>
            </TD>
          </TR>
          <TR>
            <TD class="testoGrande" bgcolor="#D1D7E7">Zustellungsstatus:
                <SPAN class="testoNero"><xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='send status']/@value"/>
                </SPAN>
            </TD>
        </TR>
        <TR>
         <TD class="testoGrande" bgcolor="#E2E6F0">
          <!-- Only show the section for valid addresses if there actually were any -->
          <xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid sent addresses']/@value != ''">
               Ausgeliefert an Adressen: <SPAN class="testoNero">gültig&#160;&#160;<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='valid sent addresses']/@value"/></SPAN>
          </xsl:if>
         </TD>
        </TR>
        <TR>
          <TD class="testoGrande" bgcolor="#E2E6F0">
          <!-- Only show the section for invalid addresses if there actually were any -->
          <xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value != '' or /USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value != ''">
            Nicht ausgeliefert an Adressen:
              <SPAN class="testoNero">
                <xsl:if test="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value != ''">gültig&#160;&#160;
<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='valid unsent addresses']/@value"/>
                </xsl:if></SPAN>
            <SPAN class="testoNero"><xsl:if test="/USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value != ''">
              ungültig&#160;&#160;<xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='invalid unsent addresses']/@value"/>
            </xsl:if></SPAN>
          </xsl:if>
          </TD>
        </TR>
        <TR>
          <TD bgcolor="#697791">
        <TABLE border="0" cellpadding="0" cellspacing="0"><TR><TD>
        <A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><IMG SRC="{$imgbase}/images/back.gif" BORDER="0"/></A></TD><TD><A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><SPAN class="testoBianco">Zurück zum Dialog "Nachricht bearbeiten" ...</SPAN></A></TD></TR></TABLE>
          </TD>
        </TR>
      </TABLE>


      </BODY>
    </HTML>
  </xsl:template>



</xsl:stylesheet>
