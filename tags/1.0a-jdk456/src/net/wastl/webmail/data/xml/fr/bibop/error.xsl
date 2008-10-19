<?xml version="1.0" encoding="ISO-8859-1"?>
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
<!-- This is part of the French translation of WebMail - Christian SENET - senet@lpm.u-nancy.fr - 2002 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

    <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
    <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>Rapport d Erreur WebMail</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META CONTENT="GENERATOR" VALUE="JWebMail 0.7 XSL"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">
        <TABLE WIDTH="100%">
          <TR bgcolor="#A6B1C0">
            <TD COLSPAN="2" align="center" class="testoGrande">Une erreur a eu lieu</TD>
          </TR>
          <TR>
            <TD class="testoNero"><SPAN class="testoGrande">Message d'Erreur</SPAN></TD>
            <TD class="testoNero">
              <P class="testoMesg"><xsl:apply-templates select="//STATEDATA/EXCEPTION/EX_MESSAGE"/></P>
            </TD>
          </TR>
          <TR>
            <TD COLSPAN="2" ALIGN="center" class="testoNero">
              <TABLE bgcolor="#E2E6F0" WIDTH="80%" BORDER="1">
                <TR>
                  <TD><SPAN class="testoGrande">Trace de la Pile</SPAN><BR/>
                    <PRE>
                      <P class="testoMesg"><xsl:apply-templates select="//STATEDATA/EXCEPTION/EX_STACKTRACE"/></P>
                    </PRE>
                  </TD>
                </TR>
              </TABLE>
            </TD>
          </TR>
          <TR bgcolor="#A6B1C0">
            <TD COLSPAN="2" align="center" class="testoGrande">!</TD>
          </TR>
        </TABLE>
      </BODY>
    </HTML>
  </xsl:template>


</xsl:stylesheet>
