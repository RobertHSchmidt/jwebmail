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

    <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/GENERICMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail 管理介面：導覽選單</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
      </HEAD>

      <BODY BGCOLOR="lightblue">
        <TABLE WIDTH="100%">
          <TR>
            <TD>
              <A HREF="{$base}/admin/system?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red">系統設定</FONT></A>
            </TD>
          </TR>
          <TR>
            <TD>
              <A HREF="{$base}/admin/control?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red">系統控制</FONT></A>
            </TD>
          </TR>
          <TR>
            <TD>
              <A HREF="{$base}/admin/domain?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red">虛擬網域設定</FONT></A>
            </TD>
          </TR>
          <TR>
            <TD>
              <A HREF="{$base}/admin/user?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red">使用者設定</FONT></A>
            </TD>
          </TR>
          <TR>
            <TD>
<!--          <A HREF="{$base}/admin/help?session-id={$session-id}" TARGET="Main"><FONT SIZE="-1" COLOR="red">求救</FONT></A>-->
              <FONT SIZE="-1" COLOR="red">求救（此功能尚未完成）</FONT>
            </TD>
          </TR>
          <TR>
            <TD>
              <A HREF="{$base}/admin/logout?session-id={$session-id}" TARGET="_top"><FONT SIZE="-1" COLOR="red">登出</FONT></A>
            </TD>
          </TR>
        </TABLE>
      </BODY>

    </HTML>

  </xsl:template>
</xsl:stylesheet>
