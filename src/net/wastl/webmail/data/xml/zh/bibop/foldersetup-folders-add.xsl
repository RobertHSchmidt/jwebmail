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
        <TITLE> <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/> 的 WebMail 信箱：收信匣設定</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
      </HEAD>

      <BODY bgcolor="#ffffff">

        <TABLE BGCOLOR="#dddddd" CELLSPACING="0" BORDER="0" WIDTH="100%">
          <TR>
            <TD VALIGN="CENTER">
              <IMG SRC="{$imgbase}/images/btn-folders.png"/>
            </TD>
            <TD VALIGN="CENTER" COLSPAN="2">
              <FONT SIZE="+2"><STRONG><xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/> 的 WebMail 收信匣設定</STRONG></FONT><BR/>
              <EM>登入帳號 <xsl:value-of select="normalize-space(/USERMODEL/USERDATA/LOGIN)"/></EM><BR/>
              <EM>此帳號從 <xsl:apply-templates select="/USERMODEL/STATEDATA/VAR[@name='first login']"/> 開始使用</EM>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="3" bgcolor="#aaaaaa"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></TD>
          </TR>
        </TABLE>
        <FORM ACTION="{$base}/folder/setup?session-id={$session-id}&amp;method=folder&amp;addto={/USERMODEL/STATEDATA/VAR[@name='add to folder']/@value}" METHOD="POST">
          <TABLE WIDTH="100%">
            <TR>
              <TD><STRONG>收信匣名稱</STRONG></TD>
              <TD><INPUT TYPE="text" NAME="folder_name" SIZE="20"/></TD>
              <TD><STRONG>收信匣類型</STRONG></TD>
              <TD>
                <SELECT NAME="folder_type">
                  <OPTION value="msgs">只包含信件</OPTION>
                  <OPTION value="folder">只包含收信匣</OPTION>
                  <OPTION value="msgfolder">可包含信件與收信匣</OPTION>
                </SELECT>
              </TD>
            </TR>
            <TR>
              <TD COLSPAN="4"><INPUT TYPE="submit" name="submit" value="新增收信匣"/></TD>
            </TR>
          </TABLE>
        </FORM>
      </BODY>

    </HTML>
  </xsl:template>

  <xsl:template match="/USERMODEL/STATEDATA/VAR">
    <xsl:value-of select="@value"/>
  </xsl:template>

</xsl:stylesheet>
