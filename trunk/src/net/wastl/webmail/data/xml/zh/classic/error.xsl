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

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Error Reporting</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <TABLE WIDTH="100%">
          <TR BGCOLOR="red">
            <TD COLSPAN="2" align="center"><H1>An error ocurred</H1></TD>
          </TR>
          <TR>
            <TD><STRONG>Error message</STRONG></TD>
            <TD>
              <xsl:apply-templates select="//STATEDATA/EXCEPTION/EX_MESSAGE"/>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="2" ALIGN="center">
              <TABLE BGCOLOR="yellow" WIDTH="80%" BORDER="1">
                <TR>
                  <TD><STRONG>Stack Trace</STRONG><BR/>
                    <PRE>
                      <xsl:apply-templates select="//STATEDATA/EXCEPTION/EX_STACKTRACE"/>
                    </PRE>
                  </TD>
                </TR>
              </TABLE>
            </TD>
          </TR>
          <TR BGCOLOR="red">
            <TD COLSPAN="2" align="center">!</TD>
          </TR>
        </TABLE>
      </BODY>
    </HTML>
  </xsl:template>


</xsl:stylesheet>
