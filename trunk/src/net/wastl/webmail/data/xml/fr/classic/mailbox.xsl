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
<!-- This is part of the French translation of JWebMail - Christian SENET - senet@lpm.u-nancy.fr - 2002 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>Boite aux Lettres JWebMail de <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Liste des Boites aux Lettres</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <CENTER><IMG SRC="{$imgbase}/images/welcome.png"/></CENTER>

        <P>
          <H3>Bienvenue dans votre bo�te aux lettres, <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>.</H3>
        </P>
        <P>
          Ceci est la <xsl:apply-templates select="/USERMODEL/USERDATA/INTVAR[@name='login count']"/>�me
          fois que vous vous connectez depuis le
          <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/>.
          Votre <B>dernier login</B> date du
          <B><xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='last login']"/></B>.
        </P>
        <P>
          Votre bo�te aux lettres contient les dossiers suivants:
          (Nombre total de messages en <FONT COLOR="green">vert</FONT>,
          nouveaux messages en <FONT COLOR="red">rouge</FONT>) (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=mailbox">Aide</A>):<BR/>
          <CENTER>
            <P>
              <A HREF="{$base}/mailbox?session-id={$session-id}&amp;force-refresh=1">Forcer rafra�chissement</A> - Cliquer ici pour forcer un rafra�chissement des informations de dossier.
            </P>
            <TABLE WIDTH="80%">
              <xsl:for-each select="/USERMODEL/MAILHOST_MODEL">
                <xsl:apply-templates select="."/>
              </xsl:for-each>
            </TABLE>
          </CENTER>
        </P>
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
    <TR BGCOLOR="#dddddd">
      <TD COLSPAN="{/USERMODEL/STATEDATA/VAR[@name='max folder depth']/@value}" WIDTH="50%">
        <xsl:choose>
          <xsl:when test='@error != ""'>
            <FONT COLOR="red"><xsl:value-of select="@name"/></FONT> (Erreur: <xsl:value-of select="@error"/>)
          </xsl:when>
          <xsl:otherwise>
            <STRONG><FONT COLOR="green"><xsl:value-of select="@name"/></FONT></STRONG>
          </xsl:otherwise>
        </xsl:choose>
      </TD>
      <TD WIDTH="50%">
        <STRONG>H�te</STRONG>: <xsl:value-of select="@url"/>
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
      <TD><IMG SRC="{$imgbase}/images/icon-folder.png"/></TD>
      <xsl:choose>
        <xsl:when test="@holds_messages = 'true'">
          <TD COLSPAN="{/USERMODEL/STATEDATA/VAR[@name='max folder depth']/@value - $level - 1}">
            <A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={@id}&amp;part=1"><STRONG><xsl:value-of select="@name"/></STRONG></A>
          </TD>
          <TD>
            <FONT COLOR="green"><xsl:value-of select="MESSAGELIST/@total"/></FONT>/<FONT COLOR="red"><xsl:value-of select="MESSAGELIST/@new"/></FONT> messages
          </TD>
        </xsl:when>
        <xsl:otherwise>
          <TD COLSPAN="{/USERMODEL/STATEDATA/VAR[@name='max folder depth']/@value - $level - 1}">
            <STRONG><xsl:value-of select="@name"/></STRONG>
          </TD>
          <TD>
            ne peut contenir de messages
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
      <TD></TD>
      <xsl:variable name="levelneu" select="$level - 1"/>
      <xsl:call-template name="recurse-folder">
        <xsl:with-param name="level" select="$levelneu"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
