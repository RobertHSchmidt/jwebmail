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

  <xsl:variable name="imgbase" select="/GENERICMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
  <xsl:variable name="base" select="/GENERICMODEL/STATEDATA/VAR[@name='base uri']/@value"/>

  <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Login Screen</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <P align="center">&#160;</P>
        <P align="center">&#160;</P>
            <CENTER>
              <FORM ACTION="{$base}/login" METHOD="POST" NAME="loginForm">
                <TABLE width="402" border="0" cellspacing="0" cellpadding="1" bgcolor="#000000" height="252" align="center">
                  <TR>
                    <TD align="center" bgcolor="#000000" valign="middle">
                      <TABLE width="400" border="0" cellspacing="0" cellpadding="0" height="250">
                        <TR>
                          <TD colspan="4" width="400" height="50" bgcolor="#7B889F">&#160;
                          </TD>
                        </TR>
                        <TR>
                          <TD rowspan="4" width="85" align="center" bgcolor="#D3D8DE" height="110">
                            <IMG SRC="{$imgbase}/images/logobibop.gif" ALT="Logo BiBop"/>
                          </TD>
                          <TD colspan="2" align="center" height="40" bgcolor="#FFFFFF" width="290">
                            <IMG SRC="{$imgbase}/images/webmailtitle.gif"/>
                          </TD>
                          <TD rowspan="4" width="25" height="115" bgcolor="#FFFFFF">
                            &#160;
                          </TD>
                        </TR>
                        <TR>
                          <TD class="testo" align="right" height="25" bgcolor="#FFFFFF" width="100">
                            Login&#160;&#160;
                          </TD>
                          <TD height="25" bgcolor="#FFFFFF" width="190" class="testo">
                            <INPUT TYPE="text" NAME="login" SIZE="15" class="testo"/>
                          </TD>
                        </TR>
                        <TR>
                          <TD class="testo" align="right" height="25" bgcolor="#FFFFFF" width="100">
                            Password&#160;&#160;
                          </TD>
                          <TD height="25" bgcolor="#FFFFFF" width="190" class="testo">
                           <INPUT TYPE="password" NAME="password" SIZE="15" class="testo"/>
                          </TD>
                        </TR>
                        <TR>
                          <TD class="testo" align="right" height="25" bgcolor="#FFFFFF" width="100">
                            Domain&#160;&#160;
                          </TD>
                          <TD height="25" bgcolor="#FFFFFF" width="190" class="testo">
                            <SELECT name="vdom" class="testo">
                              <xsl:for-each select="/GENERICMODEL/SYSDATA/DOMAIN">
                                <OPTION><xsl:apply-templates select="NAME"/></OPTION>
                              </xsl:for-each>
                            </SELECT>
                          </TD>
                        </TR>
                        <TR>
                          <TD width="85" bgcolor="#7B889F" height="50" class="testo">&#160;</TD>
                          <TD bgcolor="#7B889F" height="50" width="100" class="testo">&#160;</TD>
                          <TD bgcolor="#7B889F" height="50" width="190" class="testo">
                            <INPUT TYPE="submit" value="Login" class="testo"/>
                            <INPUT TYPE="reset" value="Reset" class="testo"/>
                          </TD>
                          <TD width="25" bgcolor="#7B889F" height="50" class="testo">&#160;</TD>
                        </TR>
                        <TR>
                          <TD colspan="4" width="400" class="testoBianco" bgcolor="#394864" height="35" align="center"><SPAN class="bold">BiBop
              WebMail </SPAN>is based on<BR/>
              WebMail is &#169; 1999-2002 by Sebastian Schaffert</TD>
                        </TR>
                      </TABLE>
                    </TD>
                  </TR>
                </TABLE>
              </FORM>
            </CENTER>
          <xsl:if test="/GENERICMODEL/STATEDATA/VAR[@name='invalid password']/@value = 'yes'">
            <!-- START invalid pass -->
            <P align="center" class="testo"><SPAN class="bold">Login incorrect</SPAN>. The passwords did not match or the name/password field was empty! Attempt will be logged.</P>
            <P align="center">&#160;</P>
            <P align="center">&#160;</P>
            <!-- END invalid pass -->
          </xsl:if>

      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>
