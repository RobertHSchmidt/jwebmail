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
        <FORM ACTION="{$base}/send?session-id={$session-id}" METHOD="POST">
          <TABLE BGCOLOR="#dddddd" CELLSPACING="0" CELLPADDING="5" BORDER="0">
            <TR>
              <TD COLSPAN="3" VALIGN="CENTER"><IMG SRC="{$imgbase}/images/btn-compose.png" BORDER="0"/></TD>
              <TD VALIGN="CENTER">
                <FONT SIZE="+2"><STRONG>Composing a message...</STRONG></FONT> (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=compose">Help</A>)<BR/>
                <EM>Date: <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/></EM>
              </TD>
            </TR>
            <TR>
             <TD><STRONG>From:</STRONG></TD>
             <TD>
               <select name="FROM" size="1">
                 <xsl:for-each select="/USERMODEL/USERDATA/EMAIL/ADDY">
                <xsl:choose>
                  <xsl:when test="$work/HEADER/FROM!=''">
                   <xsl:choose>
                         <xsl:when test="$work/HEADER/FROM=current()">
                           <option selected="selected">
                                  <xsl:value-of select="."/>
                            </option>
                          </xsl:when>
                          <xsl:otherwise>
                           <option>
                                <xsl:value-of select="."/>
                                  </option>
                          </xsl:otherwise>
                        </xsl:choose>
                  </xsl:when>
                <xsl:otherwise>
                   <xsl:choose>
                     <xsl:when test="@default='yes'">
                       <option selected="selected">
                              <xsl:value-of select="."/>
                            </option>
                          </xsl:when>
                          <xsl:otherwise>
                       <option>
                            <xsl:value-of select="."/>
                          </option>
                          </xsl:otherwise>
                        </xsl:choose>
              </xsl:otherwise>
            </xsl:choose>
             </xsl:for-each>
           </select>
           </TD>
           </TR>
           <TR>
              <TD><STRONG>To:</STRONG></TD>
              <TD><INPUT TYPE="TEXT" NAME="TO" SIZE="40" VALUE="{$work/HEADER/TO}"/></TD>
              <TD><STRONG>CarbonCopy:</STRONG></TD>
              <TD><INPUT TYPE="TEXT" NAME="CC" SIZE="40" VALUE="{$work/HEADER/CC}"/></TD>
            </TR>
            <TR>
              <TD><STRONG>Reply-To:</STRONG></TD>
              <TD><INPUT TYPE="TEXT" NAME="REPLY-TO" SIZE="40" VALUE="{$work/HEADER/REPLY_TO}"/></TD>
              <TD><STRONG>Blind CC:</STRONG></TD>
              <TD><INPUT TYPE="TEXT" NAME="BCC" SIZE="40" VALUE="{$work/HEADER/BCC}"/></TD>
            </TR>
            <TR>
              <TD><STRONG>Subject:</STRONG></TD>
              <TD COLSPAN="3"><INPUT TYPE="TEXT" NAME="SUBJECT" SIZE="80" VALUE="{$work/HEADER/SUBJECT}"/></TD>
            </TR>
            <TR>
              <TD><STRONG>Attachments:</STRONG></TD>
              <TD COLSPAN="3">
                <xsl:for-each select="$work/PART[position()=1]//PART[@type='binary']">
                  <xsl:apply-templates select="."/>
                </xsl:for-each>
                <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
              </TD>
            </TR>
            <TR>
              <TD COLSPAN="2" ALIGN="LEFT"><INPUT TYPE="SUBMIT" VALUE="Send message..." NAME="SEND"/></TD>
              <TD COLSPAN="2" ALIGN="RIGHT"><INPUT TYPE="SUBMIT" VALUE="Attach files..." NAME="ATTACH"/></TD>
            </TR>
            <TR>
              <TD COLSPAN="4" BGCOLOR="#ffffff" ALIGN="CENTER">
                <TEXTAREA NAME="BODY" COLS="79" ROWS="50">
                  <xsl:for-each select="$work/PART[position()=1]/PART[position()=1]/CONTENT">
                    <xsl:apply-templates select="."/>
                  </xsl:for-each>
                </TEXTAREA>
              </TD>
            </TR>
            <TR>
              <TD COLSPAN="2" ALIGN="LEFT"><INPUT TYPE="SUBMIT" VALUE="Send message..." NAME="SEND"/></TD>
              <TD COLSPAN="2" ALIGN="RIGHT"><INPUT TYPE="SUBMIT" VALUE="Attach files..." NAME="ATTACH"/></TD>
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
      <xsl:value-of select="@filename"/> (<xsl:value-of select="@size"/> bytes),
    </xsl:if>
  </xsl:template>


  <!-- Content of a message should be displayed plain -->
  <xsl:template match="CONTENT">
    <xsl:value-of select="."/>
  </xsl:template>
</xsl:stylesheet>
