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

<!-- This template is part of the German translation -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>JWebMail Mailbox für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Zeige Nachricht <xsl:value-of select="/USERMODEL/CURRENT[@type='message']/@id"/></TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <xsl:variable name="current_msg" select="/USERMODEL/CURRENT[@type='message']/@id"/>
        <xsl:variable name="current_folder" select="/USERMODEL/CURRENT[@type='folder']/@id"/>
        <xsl:apply-templates select="/USERMODEL/MAILHOST_MODEL//FOLDER[@id=$current_folder]//MESSAGE[@msgid=$current_msg]"/>
      </BODY>
    </HTML>
  </xsl:template>

  <xsl:template match="MESSAGE">

    <xsl:call-template name="navigation"/>

    <TABLE WIDTH="100%" BGCOLOR="#dddddd" CELLSPACING="0" BORDER="0">
      <TR>
        <TD COLSPAN="3">
            <xsl:apply-templates select="HEADER/SUBJECT"/>
        </TD>
        <TD ALIGN="right">(<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=messagelist">Hilfe</A>)</TD>
      </TR>
      <TR>
        <TD WIDTH="10%" VALIGN="top">
          <STRONG>Von:</STRONG>
        </TD>
        <TD WIDTH="40%" VALIGN="top">
          <xsl:apply-templates select="HEADER/FROM"/>
        </TD>
        <TD WIDTH="10%" VALIGN="top">
          <STRONG>An:</STRONG>
        </TD>
        <TD WIDTH="40%" VALIGN="top">
          <xsl:for-each select="HEADER/TO">
            <xsl:apply-templates select="."/><BR/>
          </xsl:for-each>
        </TD>
      </TR>
      <TR>
        <TD VALGIN="top">
          <STRONG>Antwort An:</STRONG>
        </TD>
        <TD VALIGN="top">
          <xsl:apply-templates select="HEADER/REPLY_TO"/>
          <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        </TD>
        <TD VALIGN="top">
          <STRONG>Kopie:</STRONG>
        </TD>
        <TD VALIGN="top">
          <xsl:for-each select="HEADER/CC">
            <xsl:apply-templates select="."/><BR/>
          </xsl:for-each>
          <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
        </TD>
      </TR>
      <TR>
        <TD>
          <STRONG>Datum:</STRONG>
        </TD>
        <TD COLSPAN="3">
          <xsl:apply-templates select="HEADER/DATE"/>
        </TD>
      </TR>
      <TR>
        <TD COLSPAN="4" ALIGN="right">
          <A HREF="{$base}/compose?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;message-nr={@msgnr}&amp;reply=1">auf diese Nachricht antworten...</A> -
          <A HREF="{$base}/compose?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;message-nr={@msgnr}&amp;forward=1">diese Nachricht weiterleiten...</A>
        </TD>
      </TR>
    </TABLE>

    <xsl:for-each select="PART">
      <xsl:apply-templates select="."/>
    </xsl:for-each>

    <TABLE WIDTH="100%" BGCOLOR="#dddddd" CELLSPACING="0" BORDER="0">
      <TR>
        <FONT SIZE="-">
          <FORM ACTION="{$base}/folder/list?flag=1&amp;session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}" METHOD="POST">
            <TD>
              <INPUT TYPE="HIDDEN" NAME="CH%{@msgnr}" VALUE="on"/>
              <SELECT NAME="MARK">
                <OPTION VALUE="MARK">setze</OPTION>
                <OPTION VALUE="UNMARK">entferne</OPTION>
              </SELECT>
              <STRONG>Markierung</STRONG>
              <SELECT NAME="MESSAGE FLAG">
                <OPTION VALUE="DELETED">gelöscht</OPTION>
                <OPTION VALUE="SEEN">gelesen</OPTION>
                <OPTION VALUE="ANSWERED">beantwortet</OPTION>
                <OPTION VALUE="RECENT">aktuell</OPTION>
                <OPTION VALUE="DRAFT">entwurf</OPTION>
              </SELECT>
              <INPUT TYPE="SUBMIT" NAME="flagmsgs" VALUE="Ausführen!"/>
            </TD>
          </FORM>
          <FORM ACTION="{$base}/folder/showmsg?flag=1&amp;session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}" METHOD="POST">
            <TD>
              <INPUT TYPE="HIDDEN" NAME="CH%{@msgnr}" VALUE="on"/>
              <SELECT NAME="COPYMOVE">
                <OPTION VALUE="COPY">kopiere</OPTION>
                <OPTION VALUE="MOVE">verschiebe</OPTION>
              </SELECT>
              <STRONG>Nachricht in Ordner</STRONG>
              <SELECT NAME="TO">
                <xsl:for-each select="/USERMODEL/MAILHOST_MODEL//FOLDER">
                  <OPTION value="{@id}"><xsl:value-of select="@name"/></OPTION>
                </xsl:for-each>
              </SELECT>
              <INPUT TYPE="SUBMIT" NAME="copymovemsgs" VALUE="Ausführen!"/>
            </TD>
          </FORM>
        </FONT>
      </TR>
    </TABLE>


    <xsl:call-template name="navigation"/>
  </xsl:template>

  <xsl:template match="PART">
    <xsl:choose>
      <xsl:when test="@type='text' and @hidden='true'" />
      <xsl:when test="@type='text' and not(@hidden='true')">
        <PRE>
          <xsl:for-each select="*">
            <xsl:apply-templates select="."/>
          </xsl:for-each>
        </PRE>
      </xsl:when>
      <xsl:when test="@type='html'">
        <xsl:for-each select="*">
          <xsl:apply-templates select="."/>
        </xsl:for-each>
      </xsl:when>
      <xsl:when test="@type='multi'">
        <HR/>
        <xsl:for-each select="PART">
          <CENTER><EM>MIME Abschnitt</EM></CENTER><BR/>
          <xsl:apply-templates select="."/>
          <HR/>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <HR/>
        <TABLE WIDTH="100%" BGCOLOR="#f7f3a8" CELLPADDING="0" CELLSPACING="0" BORDER="0">
          <TR>
            <TD>
              <STRONG>Angehängte Datei</STRONG>
            </TD>
            <TD>
              <A HREF="{$base}/showmime/{@filename}?session-id={$session-id}&amp;msgid={ancestor::MESSAGE/@msgid}"><xsl:value-of select="@filename"/><xsl:value-of select="@name"/></A>
            </TD>
          </TR>
          <xsl:if test="@description != ''">
            <TR>
              <TD><STRONG>Beschreibung:</STRONG></TD>
              <TD>
                <xsl:value-of select="@description"/>
                <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
              </TD>
            </TR>
          </xsl:if>
          <TR>
            <TD><STRONG>Typ:</STRONG></TD>
            <TD><xsl:value-of select="@content-type"/></TD>
          </TR>
          <TR>
            <TD><STRONG>Größe:</STRONG></TD>
            <TD><xsl:value-of select="@size"/> bytes</TD>
          </TR>
          <xsl:if test="@type='image' and /USERMODEL/USERDATA/BOOLVAR[@name='show images']/@value = 'yes'">
            <TR>
              <TD COLSPAN="2">
                <IMG SRC="{$base}/showmime/{@filename}?session-id={$session-id}&amp;msgid={ancestor::MESSAGE/@msgid}"/>
              </TD>
            </TR>
          </xsl:if>
        </TABLE>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="CONTENT">
    <xsl:choose>
      <xsl:when test="../@type = 'html'">
        <xsl:apply-templates select="*"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:choose>
          <xsl:when test="@quotelevel > 2">
            <FONT COLOR="orange">
              <EM>
                <xsl:value-of disable-output-escaping="yes" select="."/>
              </EM>
            </FONT>
          </xsl:when>
          <xsl:when test="@quotelevel = 2">
            <FONT COLOR="green">
              <EM>
                <xsl:value-of disable-output-escaping="yes" select="."/>
              </EM>
            </FONT>
          </xsl:when>
          <xsl:when test="@quotelevel = 1">
            <FONT COLOR="blue">
              <EM>
                <xsl:value-of disable-output-escaping="yes" select="."/>
              </EM>
            </FONT>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of disable-output-escaping="yes" select="."/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="navigation">
    <P>
      <TABLE WIDTH="100%">
        <TR>
          <TD ALIGN="left" VALIGN="center">
            <EM>
              <xsl:choose>
                <xsl:when test="@msgnr > 1">
                  <A HREF="{$base}/folder/showmsg?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;message-nr={@msgnr - 1}"><IMG SRC="{$imgbase}/images/arrow-left.png" BORDER="0"/> Vorherige Nachricht</A>
                </xsl:when>
                <xsl:otherwise>
                  <IMG SRC="{$imgbase}/images/arrow-left-disabled.png" BORDER="0"/> Vorherige Nachricht
                </xsl:otherwise>
              </xsl:choose>
            </EM>
          </TD>
          <TD ALIGN="center" VALIGN="center">
            <EM>
              <A HREF="{$base}/folder/list?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;part={/USERMODEL/CURRENT[@type='folder']/@list_part}"><IMG SRC="{$imgbase}/images/arrow-up.png" BORDER="0"/>Nachrichtenliste</A>
            </EM>
          </TD>
          <TD ALIGN="right" VALIGN="center">
            <xsl:variable name="current_folder" select="/USERMODEL/CURRENT[@type='folder']/@id"/>
            <EM>
              <xsl:choose>
                <xsl:when test="@msgnr &lt; /USERMODEL/MAILHOST_MODEL//FOLDER[@id=$current_folder]/MESSAGELIST/@total">
                  <A HREF="{$base}/folder/showmsg?session-id={$session-id}&amp;folder-id={/USERMODEL/CURRENT[@type='folder']/@id}&amp;message-nr={@msgnr + 1}">Nächste Nachricht <IMG SRC="{$imgbase}/images/arrow-right.png" BORDER="0"/></A>
                </xsl:when>
                <xsl:otherwise>
                   Nächste Nachricht <IMG SRC="{$imgbase}/images/arrow-right-disabled.png" BORDER="0"/>
                </xsl:otherwise>
              </xsl:choose>
            </EM>
          </TD>
        </TR>
      </TABLE>
    </P>
  </xsl:template>

  <xsl:template match="DATE">
    <xsl:value-of select="."/>
  </xsl:template>

  <xsl:template match="SUBJECT">
    <FONT SIZE="+1" COLOR="darkgreen">
      <STRONG>
        <xsl:value-of select="."/>
      </STRONG>
    </FONT>
  </xsl:template>

  <xsl:template match="TO">
    <EM><xsl:value-of select="."/></EM>
  </xsl:template>

  <xsl:template match="CC">
    <EM><xsl:value-of select="."/></EM>
  </xsl:template>

  <xsl:template match="REPLY-TO">
    <EM><xsl:value-of select="."/></EM>
  </xsl:template>

  <xsl:template match="FROM">
    <xsl:choose>
      <xsl:when test="contains(.,'&lt;')">
        <EM><xsl:value-of select="substring-before(.,'&lt;')"/></EM>,
        <xsl:value-of select="substring-before(substring-after(.,'&lt;'),'>')"/>
       </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="."/>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <!-- The following three sections deal with displaying HTML code from HTML attachments.
  Elements marked "malicious" will get a special treatment -->
  <xsl:template match="@*|node()">
    <xsl:choose>
      <xsl:when test="not(@malicious)">
        <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
      </xsl:when>
      <xsl:otherwise>
        <TABLE  BORDER="1" WIDTH="100%" CELLSPACING="0" CELLPADDING="0">
          <TR>
            <TD BGCOLOR="#FF9933">
              Untrusted HTML element removed: <xsl:value-of select="@malicious"/>
            </TD>
          </TR>
          <TR>
            <TD bgcolor="#3399FF" >
              <xsl:apply-templates select="." mode="quote"/>
            </TD>
          </TR>
        </TABLE>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="node()" mode="quote">
    <xsl:choose>
      <xsl:when test="name(.)">
        &lt;<xsl:value-of select="name(.)"/>
        <xsl:text> </xsl:text><xsl:apply-templates select="@*" mode="quote"/>
        <xsl:if test="./node()">
          &gt;
          <xsl:apply-templates select="node()" mode="quote"/>
          &lt;/<xsl:value-of select="name(.)"/>
        </xsl:if>
        &gt;
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="."/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="@*" mode="quote">
    <xsl:if test="name(.) != 'malicious'">
      <xsl:text> </xsl:text><xsl:value-of select="name(.)"/>="<xsl:value-of select="."/>"
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
