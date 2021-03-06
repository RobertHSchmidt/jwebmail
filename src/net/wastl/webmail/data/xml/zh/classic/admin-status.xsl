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

    <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/GENERICMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>JWebMail Administration Interface: System Status</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>

      <BODY BGCOLOR="white">
        <TABLE WIDTH="100%" CELLSPACING="0" CELLPADDING="0">
          <TR bgcolor="#dddddd">
            <TD COLSPAN="3" ALIGN="center"><FONT SIZE="+1"><STRONG>Active Server Threads</STRONG></FONT></TD>
          </TR>
          <xsl:if test='/GENERICMODEL/STATEDATA/VAR[@name="http server status"]/@value != ""'>
            <TR>
              <TD><STRONG>HTTP Server</STRONG></TD>
              <TD COLSPAN="2"><PRE><xsl:value-of select='/GENERICMODEL/STATEDATA/VAR[@name="http server status"]/@value'/></PRE></TD>
            </TR>
          </xsl:if>
          <xsl:if test='/GENERICMODEL/STATEDATA/VAR[@name="ssl server status"]/@value != ""'>
            <TR>
              <TD><STRONG>SSL Server</STRONG></TD>
              <TD COLSPAN="2"><PRE><xsl:value-of select='/GENERICMODEL/STATEDATA/VAR[@name="ssl server status"]/@value'/></PRE></TD>
            </TR>
          </xsl:if>
          <xsl:if test='/GENERICMODEL/STATEDATA/VAR[@name="servlet status"]/@value != ""'>
            <TR>
              <TD><STRONG>Servlet</STRONG></TD>
              <TD COLSPAN="2"><PRE><xsl:value-of select='/GENERICMODEL/STATEDATA/VAR[@name="servlet status"]/@value'/></PRE></TD>
            </TR>
          </xsl:if>
          <xsl:if test='/GENERICMODEL/STATEDATA/VAR[@name="storage status"]/@value != ""'>
            <TR>
              <TD><STRONG>Storage</STRONG></TD>
              <TD COLSPAN="2"><PRE><xsl:value-of select='/GENERICMODEL/STATEDATA/VAR[@name="storage status"]/@value'/></PRE></TD>
            </TR>
          </xsl:if>

          <TR bgcolor="#dddddd">
            <TD COLSPAN="3" ALIGN="center"><FONT SIZE="+1"><STRONG>Active Sessions</STRONG></FONT></TD>
          </TR>

          <xsl:for-each select="/GENERICMODEL/STATEDATA/SESSION">
            <xsl:sort select="@type" order="ascending"/>
              <xsl:choose>
                <xsl:when test="position() mod 2 = 1">
                  <TR bgcolor="#f7f3a8">
                    <xsl:apply-templates select="."/>
                  </TR>
                </xsl:when>
                <xsl:otherwise>
                  <TR>
                    <xsl:apply-templates select="."/>
                  </TR>
                </xsl:otherwise>
              </xsl:choose>
          </xsl:for-each>

          <TR bgcolor="#dddddd">
            <TD COLSPAN="3" ALIGN="center"><FONT SIZE="+1"><STRONG>Shutdown/Restart</STRONG></FONT></TD>
          </TR>
          <TR>
            <FORM ACTION="{$base}/admin/control?session-id={$session-id}" METHOD="POST">
              <TD><STRONG>Shutdown/Restart system</STRONG></TD>
              <TD>after <INPUT TYPE="text" SIZE="4" NAME="SHUTDOWN SECONDS" VALUE="0"/> seconds</TD>
              <TD><INPUT TYPE="submit" name="REBOOT" value="Restart"/><INPUT TYPE="submit" name="SHUTDOWN" value="Shutdown"/></TD>
            </FORM>
          </TR>
        </TABLE>
      </BODY>
    </HTML>
  </xsl:template>

  <xsl:template match="SESSION">
    <FORM ACTION="{$base}/admin/control/kill?session-id={$session-id}&amp;kill={SESS_CODE}" METHOD="POST">
      <TD>
        <xsl:choose>
          <xsl:when test="@type = 'admin'">
            <STRONG>Admin Session</STRONG>
          </xsl:when>
          <xsl:otherwise>
            <STRONG>User Session </STRONG> (User <xsl:value-of select="SESS_USER"/>)
          </xsl:otherwise>
        </xsl:choose>
      </TD>
      <TD>
        <STRONG>remote address: </STRONG> <xsl:value-of select="SESS_ADDRESS"/>, <STRONG>idletime: </STRONG> <xsl:value-of select="VAR[@name='idle time']/@value"/><BR/>
        <xsl:if test="count(SESS_CONN) > 0">
          <STRONG>active mail connections:</STRONG><BR/>
          <UL>
            <xsl:for-each select="SESS_CONN">
              <LI><EM><xsl:value-of select="."/></EM></LI>
            </xsl:for-each>
          </UL>
        </xsl:if>
      </TD>
      <TD>
        <INPUT TYPE="submit" NAME="submit" VALUE="Kill"/>
      </TD>
    </FORM>
  </xsl:template>
</xsl:stylesheet>
