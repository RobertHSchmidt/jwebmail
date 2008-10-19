<?xml version="1.0" encoding="ISO-8859-2"?>
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
        <TITLE><xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/> WebMail fiókja: Főoldal</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
        <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
          <TR>
            <TD width="5%" background="{$imgbase}/images/sfondino_grigio.gif">
                <IMG SRC="{$imgbase}/images/logo.gif" alt="Logo BiBop"/>
            </TD>
            <TD width="13%" background="{$imgbase}/images/sfondino_grigio.gif">
                <IMG SRC="{$imgbase}/images/webmail.gif" alt="Logo WebMail BiBop"/>
            </TD>
            <TD width="2%" background="{$imgbase}/images/sfondino_scuro.gif">
                <IMG SRC="{$imgbase}/images/spacer.gif" width="15" height="1"/>
            </TD>
            <TD width="32%" background="{$imgbase}/images/sfondino_scuro.gif" align="center">
              &#160;
            </TD>
            <TD width="23%" valign="top" background="{$imgbase}/images/sfondino_scuro.gif">
                &#160;
            </TD>
            <TD width="16%" valign="top" align="right" bgcolor="#697791" background="{$imgbase}/images/curva_alto.gif" class="mailbox">
              <SPAN class="mailbold"><xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/></SPAN><BR/> Levelesládája
            </TD>
            <TD align="right" width="9%" background="{$imgbase}/images/sfondino_grigio_scuro.gif">
                <IMG SRC="{$imgbase}/images/mailbox_dx.gif"/>
            </TD>
          </TR>
        </TABLE>
      </BODY>


    </HTML>
  </xsl:template>


</xsl:stylesheet>
