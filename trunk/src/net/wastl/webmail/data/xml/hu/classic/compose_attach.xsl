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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>
  
  <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
  <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>
  
  <xsl:variable name="work" select="/USERMODEL/WORK/MESSAGE[position()=1]"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Showing message <xsl:value-of select="/USERMODEL/CURRENT[@type='message']/@id"/></TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>
      
      <BODY bgcolor="#ffffff">
        <FORM ACTION="{$base}/compose/attach?session-id={$session-id}" METHOD="POST" ENCTYPE="multipart/form-data">

          <TABLE BGCOLOR="#dddddd" CELLSPACING="0" CELLPADDING="5" BORDER="0">
            <TR>
              <TD VALIGN="CENTER"><IMG SRC="{$imgbase}/images/btn-compose.png" BORDER="0"/></TD>
              <TD VALIGN="CENTER">
                <FONT SIZE="+2"><STRONG>Attaching files...</STRONG></FONT> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=compose-attach">Help</A>)<BR/>
                <EM>Date: <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/></EM>
              </TD>
            </TR>
            <TR>
              <TD>
                <SELECT NAME="ATTACHMENTS" SIZE="10" multiple="multiple" WIDTH="40%">
                  <xsl:for-each select="$work/PART[position()=1]/PART[@type='binary']">
                    <OPTION VALUE="{@filename}"><xsl:apply-templates select="."/></OPTION>
                  </xsl:for-each>
                </SELECT>
              </TD>           
              <TD>
                <INPUT TYPE="FILE" NAME="FILE"/><BR/>
                <STRONG>Description:</STRONG><BR/>
                <TEXTAREA NAME="DESCRIPTION" ROWS="4" COLS="79"></TEXTAREA><BR/>
                <INPUT TYPE="SUBMIT" NAME="ADD" VALUE="Add new file"/>
                <INPUT TYPE="SUBMIT" NAME="DELETE" VALUE="Delete selected file(s)"/>
              </TD>
            </TR>
            <TR>
              <TD COLSPAN="4" ALIGN="CENTER">
                <STRONG><xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='current attach size']/@value"/> of <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='max attach size']/@value"/> bytes</STRONG>
              </TD>
            </TR>
            <TR>
              <TD COLSPAN="4" VALIGN="center">
                <A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><IMG SRC="{$imgbase}/images/arrow-left.png" BORDER="0"/> Back to compose dialog ...</A>
              </TD>
            </TR>
          </TABLE>
        </FORM> 
      </BODY>
    </HTML>
  </xsl:template>
  
  <!-- All parts that are attachments (= have a file name) should be displayed with their name and
       size only -->
  <xsl:template match="PART">
    <xsl:if test="@filename != ''">
      <xsl:value-of select="@filename"/> (<xsl:value-of select="@size"/> bytes)
    </xsl:if>
  </xsl:template>


  <!-- Content of a message should be displayed plain -->
  <xsl:template match="CONTENT">
    <xsl:value-of select="."/>
  </xsl:template>
</xsl:stylesheet>