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
  <xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE> <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/> 的 WebMail 信箱：主框/求救</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">
          <TABLE width="100%" border="0" cellspacing="0" cellpadding="4">
            <TR>
                <TD colspan="2" height="22" class="testoNero"><img src="images/icona_help.gif" align="absmiddle"/>WebMail 求救手冊
                </TD>
            </TR>
            <TR>
                <TD colspan="2" height="22" class="testoBianco" bgcolor="#697791">&#160;                </TD>
            </TR>

            <!-- Test whether the user has choosen a specific help topic or wants to display the whole
                 help file -->
            <xsl:choose>
              <xsl:when test="/USERMODEL/STATEDATA/VAR[@name='helptopic']">
                <xsl:apply-templates select="/USERMODEL/help/helptopic[@id = /USERMODEL/STATEDATA/VAR[@name='helptopic']/@value]"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="/USERMODEL/help/helptopic"/>
              </xsl:otherwise>
            </xsl:choose>

            <TR>
                <TD height="22" width="11%" bgcolor="#A6B1C0">&#160;
                </TD>
              <TD height="22" class="testo" width="89%" bgcolor="#E2E6F0">
                本系統使用 <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='java virtual machine']"/> 於 <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='operating system']"/><BR/>
                WebMail 系統 <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='webmail version']"/> (c)1999-2001 by Sebastian Schaffert, schaffer@informatik.uni-muenchen.de
              </TD>
            </TR>
          </TABLE>
      </BODY>

    </HTML>
  </xsl:template>

  <xsl:template match="/USERMODEL/STATEDATA/VAR">
    <xsl:value-of select="@value"/>
  </xsl:template>

   <xsl:template match="helptopic">
    <TR>
      <A NAME="{@id}"/>
      <TD rowspan="2" width="11%" valign="top" bgcolor="#A6B1C0">
        <xsl:if test="icon">
          <IMG SRC="{$imgbase}/images/{icon/@href}" BORDER="0"/>
        </xsl:if>&#160;
      </TD>
      <TD class="testo" width="89%" bgcolor="#E2E6F0" valign="top">
        <SPAN class="testoGrande">
          <xsl:value-of select="@title"/>
        </SPAN>
      </TD>
     </TR>
     <TR>
       <TD class="testo" width="89%" bgcolor="#D1D7E7" valign="top">
        <xsl:apply-templates select="helpdata"/>
<P class="testo" align="justify">
          <SPAN class="bold">進一步參考：</SPAN> <xsl:apply-templates select="ref"/>
        </P>
      </TD>
    </TR>
  </xsl:template>


  <xsl:template match="helpdata">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="p">
    <P align="justify" class="testo">
      <xsl:apply-templates/>
    </P>
  </xsl:template>

  <xsl:template match="br">
    <BR/>
  </xsl:template>

  <xsl:template match="ul">
    <UL>
      <xsl:apply-templates/>
    </UL>
  </xsl:template>

  <xsl:template match="bold">
    <SPAN class="bold">
      <xsl:apply-templates/>
    </SPAN>
  </xsl:template>

  <xsl:template match="li">
    <LI><xsl:apply-templates/></LI>
  </xsl:template>

  <xsl:template match="note">
    <TABLE width="100%" border="0" cellspacing="0" cellpadding="4">
      <TR>
        <TD width="6%" class="testo" valign="top">
          <SPAN class="bold">Note: </SPAN>
        </TD>
        <TD width="94%" class="testo" bgcolor="#3399FF" valign="top">
          <xsl:apply-templates/>
        </TD>
      </TR>
    </TABLE>
  </xsl:template>

  <xsl:template match="warning">
    <TABLE width="100%" border="0" cellspacing="0" cellpadding="4">
      <TR>
        <TD width="6%" class="testo" valign="top">
          <SPAN class="bold">Warning: </SPAN>
        </TD>
        <TD width="94%" class="testo" bgcolor="#FF9933" valign="top">
          <xsl:apply-templates/>
        </TD>
      </TR>
    </TABLE>
  </xsl:template>

  <xsl:template match="ref">
    <xsl:variable name="href" select="@ref-id"/>
    <xsl:choose>
      <xsl:when test="/USERMODEL/STATEDATA/VAR[@name='helptopic']">
        <A HREF="{$base}/help?session-id={$session-id}&amp;helptopic={$href}"><xsl:value-of select="/USERMODEL/help/helptopic[@id = $href]/@title"/></A>,
      </xsl:when>
      <xsl:otherwise>
        <A HREF="#{$href}"><xsl:value-of select="/USERMODEL/help/helptopic[@id = $href]/@title"/></A>,
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
