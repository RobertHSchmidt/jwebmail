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
        <TITLE>WebMail Mailbox für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Mailbox Übersicht</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">
        <P class="testoChiaro">
          <SPAN class="testoScuro">Willkommen zu Deinem Briefkasten, <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>.</SPAN><BR/>
          Dies ist das <SPAN class="testoScuro"><xsl:apply-templates select="/USERMODEL/USERDATA/INTVAR[@name='login count']"/>
          Mal</SPAN> daß Du Dich anmeldest seit dem
          <SPAN class="testoScuro"><xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/></SPAN>.
          Deine <SPAN class="bold">letzte Anmeldung</SPAN> war am
          <SPAN class="testoScuro"><xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='last login']"/></SPAN>.<BR/>
          Dein Briefkasten enthält die folgenden Ordner:
          (Gesamtzahl der Nachrichten in <SPAN class="testoVerde">grün</SPAN>,
          neue Nachrichten in <SPAN class="testoRosso">rot</SPAN>) (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=mailbox">Hilfe</A>):<BR/><BR/>
              <A HREF="{$base}/mailbox?session-id={$session-id}&amp;force-refresh=1">Neuaufbau erzwingen</A> - Hier klicken um die Ordnerübersicht neu aufzubauen.
        </P>
            <TABLE width="100%" border="0" cellspacing="0" cellpadding="3">
              <xsl:for-each select="/USERMODEL/MAILHOST_MODEL">
                <xsl:apply-templates select="."/>
              </xsl:for-each>
            </TABLE>
      </BODY>


    </HTML>
  </xsl:template>

  <xsl:template match="/USERMODEL/USERDATA/INTVAR">
    <xsl:value-of select="@value"/>
  </xsl:template>

  <xsl:template match="/USERMODEL/STATEDATA/VAR">
    <xsl:value-of select="@value"/>
  </xsl:template>

  <xsl:template match="/USERMODEL/MAILHOST_MODEL">
    <TR>
      <TD COLSPAN="{/USERMODEL/STATEDATA/VAR[@name='max folder depth']/@value}" WIDTH="50%" bgcolor="#A6B1C0" class="testoGrande">
        <xsl:choose>
          <xsl:when test='@error != ""'>
            <SPAN class="testoRosso"><xsl:value-of select="@name"/></SPAN> (Fehler: <xsl:value-of select="@error"/>)
          </xsl:when>
          <xsl:otherwise>
            <SPAN class="testoVerde"><xsl:value-of select="@name"/></SPAN>
          </xsl:otherwise>
        </xsl:choose>
      </TD>
      <TD bgcolor="#909CAF" width="48%" class="testoScuro">
        Rechnung: <xsl:value-of select="@url"/>
      </TD>
      <TD bgcolor="#909CAF" width="2%" class="testoScuro">
        &#160;
      </TD>
    </TR>
    <xsl:for-each select="FOLDER[@subscribed='true']">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="FOLDER">
    <xsl:variable name="level" select="count(ancestor::FOLDER)"/>
    <TR>
      <xsl:call-template name="recurse-folder">
         <xsl:with-param name="level" select="$level"/>
      </xsl:call-template>
      <TD bgcolor="#E2E6F0" align="right"><IMG SRC="{$imgbase}/images/folder.gif"/></TD>
      <xsl:choose>
        <xsl:when test="@holds_messages = 'true'">
          <TD bgcolor="#E2E6F0" COLSPAN="{/USERMODEL/STATEDATA/VAR[@name='max folder depth']/@value - $level - 1}" valign="middle" class="testoGrande">
            <A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={@id}&amp;part=1"><xsl:value-of select="@name"/></A>
          </TD>
          <TD WIDTH="48%" bgcolor="#D3D8DE" valign="middle" class="testoNero">
            <SPAN class="testoVerde"><xsl:value-of select="MESSAGELIST/@total"/></SPAN>/<SPAN class="testoRosso"><xsl:value-of select="MESSAGELIST/@new"/></SPAN> Nachrichten
          </TD>
     <TD width="2%" class="testoScuro">
        &#160;
      </TD>
        </xsl:when>
        <xsl:otherwise>
          <TD bgcolor="#E2E6F0" COLSPAN="{/USERMODEL/STATEDATA/VAR[@name='max folder depth']/@value - $level - 1}" valign="middle" class="testoGrande">
            <xsl:value-of select="@name"/>
          </TD>
          <TD WIDTH="48%" bgcolor="#D3D8DE" valign="middle" class="testoNero">
            kann keine Nachrichten enthalten
          </TD>
     <TD width="2%" class="testoScuro">
        &#160;
      </TD>
        </xsl:otherwise>
      </xsl:choose>
    </TR>


    <xsl:for-each select="FOLDER">
      <xsl:apply-templates select="."/>
    </xsl:for-each>
  </xsl:template>


  <!-- Create an appropriate number of <TD></TD> before a folder, depending on the level -->
  <xsl:template name="recurse-folder">
    <xsl:param name="level"/>
    <xsl:if test="$level>0">
      <TD bgcolor="#E2E6F0" class="testoNero">&#160;</TD>
      <xsl:variable name="levelneu" select="$level - 1"/>
      <xsl:call-template name="recurse-folder">
        <xsl:with-param name="level" select="$levelneu"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
