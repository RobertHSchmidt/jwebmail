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
  <xsl:output method="html" indent="no"/>

  <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>
  <xsl:variable name="session-id" select="/USERMODEL/STATEDATA/VAR[@name='session id']/@value"/>

  <xsl:variable name="work" select="/USERMODEL/WORK/MESSAGE[position()=1]"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>Boite aux Lettres WebMail de <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Affichage des messages <xsl:value-of select="/USERMODEL/CURRENT[@type='message']/@id"/></TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#B5C1CF" topmargin="5" leftmargin="0" marginwidth="0" marginheight="5">
        <FORM ACTION="{$base}/compose/attach?session-id={$session-id}" METHOD="POST" ENCTYPE="multipart/form-data">

          <TABLE width="100%" border="0" cellspacing="2" cellpadding="4">
            <TR>
              <TD colspan="2" height="22" class="testoNero">
                <IMG SRC="{$imgbase}/images/icona_composer.gif" BORDER="0" align="absmiddle"/>Attacher fichiers... (<A HREF="{$base}/help?session-id={$session-id}&amp;helptopic=compose-attach">Aide</A>)
              </TD>
            </TR>
            <TR>
              <TD colspan="2" bgcolor="#697791" height="22" class="testoBianco">
                Date: <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='date']/@value"/>
              </TD>
            </TR>
            <TR>
              <TD rowspan="3" bgcolor="#E2E6F0" class="testoNero">
                <SELECT NAME="ATTACHMENTS" SIZE="10" multiple="multiple" WIDTH="40%" class="testoNero">
                  <xsl:for-each select="$work/PART[position()=1]/PART[@type='binary']">
                    <OPTION VALUE="{@filename}"><xsl:apply-templates select="."/></OPTION>
                  </xsl:for-each>
                </SELECT>
              </TD>
              <TD bgcolor="#D1D7E7" class="testoNero">
                <INPUT TYPE="FILE" NAME="FILE" SIZE="20" class="testoNero"/>
              </TD>
            </TR>
            <TR>
              <TD bgcolor="#E2E6F0" class="testoNero">
                <SPAN class="testoGrande">Description:</SPAN><BR/>
                <TEXTAREA NAME="DESCRIPTION" ROWS="4" COLS="20" class="testoNero"></TEXTAREA>
              </TD>
            </TR>
            <TR>
              <TD class="testoNero" bgcolor="#D1D7E7">
                <INPUT TYPE="SUBMIT" NAME="ADD" VALUE="Ajouter nouveau fichier" class="testoNero"/>&#160;
                <INPUT TYPE="SUBMIT" NAME="DELETE" VALUE="Effacer fichier(s) selectionne(s)" class="testoNero"/>
              </TD>
            </TR>
            <TR>
              <TD colspan="2" bgcolor="#A6B1C0" align="center" height="22" class="testoGrande">
                <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='current attach size']/@value"/> sur <xsl:value-of select="/USERMODEL/STATEDATA/VAR[@name='max attach size']/@value"/> octets
              </TD>
            </TR>
            <TR>
              <TD COLSPAN="2" bgcolor="#697791" height="22">
                <TABLE border="0" cellpadding="0" cellspacing="0"><TR><TD>
                <A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><IMG SRC="{$imgbase}/images/back.gif" BORDER="0"/></A></TD><TD><A HREF="{$base}/compose?session-id={$session-id}&amp;continue=1"><SPAN class="testoBianco"> Retour à la composition ...</SPAN></A></TD></TR></TABLE>
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
      <xsl:value-of select="@filename"/> (<xsl:value-of select="@size"/> octets)
    </xsl:if>
  </xsl:template>


  <!-- Content of a message should be displayed plain -->
  <xsl:template match="CONTENT">
    <xsl:value-of select="."/>
  </xsl:template>
</xsl:stylesheet>