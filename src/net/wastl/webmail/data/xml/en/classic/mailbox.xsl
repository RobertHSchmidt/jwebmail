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
  <xsl:output method="html" encoding="UTF-8"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Mailbox List</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <CENTER><IMG SRC="{$imgbase}/images/welcome.png"/></CENTER>

        <P>
          <H3>Welcome to your mailbox, <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>.</H3>
        </P>
        <P>
          This is the <xsl:apply-templates select="/USERMODEL/USERDATA/INTVAR[@name='login count']"/>
          time you log in since
          <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/>.
          Your <B>last login</B> was on
          <B><xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='last login']"/></B>.
        </P>
        <P>
          Your Mailbox contains the following folders
          (total messages are in <FONT COLOR="green">green</FONT>,
          new messages in <FONT COLOR="red">red</FONT>) (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=mailbox">Help</A>):<BR/>
          <CENTER>
            <P>
              <A HREF="{$base}/mailbox?session-id={$session-id}&amp;force-refresh=1">Force refresh</A> - Click this to force a refresh of the folder information.
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
            <FONT COLOR="red"><xsl:value-of select="@name"/></FONT> (Error: <xsl:value-of select="@error"/>)
          </xsl:when>
          <xsl:otherwise>
            <STRONG><FONT COLOR="green"><xsl:value-of select="@name"/></FONT></STRONG>
          </xsl:otherwise>
        </xsl:choose>
      </TD>
      <TD WIDTH="50%">
        <STRONG>Host</STRONG>: <xsl:value-of select="@url"/>
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
            cannot contain messages
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
