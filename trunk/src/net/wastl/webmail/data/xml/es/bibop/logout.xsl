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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="yes"/>

   <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>


    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Title Frame</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META HTTP-EQUIV="REFRESH" CONTENT="5;URL={/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"/>
        <link rel="stylesheet" href="{$base}/passthrough/webmail.css"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <P align="center">&#160;</P>
        <P align="center">&#160;</P>
        <CENTER>
        <TABLE width="402" border="0" cellspacing="0" cellpadding="1" bgcolor="#000000" height="252" align="center">
          <TR>
            <TD align="center" bgcolor="#000000" valign="middle">
              <TABLE width="400" border="0" cellspacing="0" cellpadding="0" height="250">
                <TR>
                  <TD colspan="2" width="400" height="50" bgcolor="#7B889F">&#160;
                  </TD>
                </TR>
                <TR>
                  <TD rowspan="2" width="85" align="center" bgcolor="#D3D8DE" height="110">
                    <IMG SRC="{$imgbase}/images/logobibop.gif" ALT="Logo BiBop"/>
                  </TD>
                  <TD align="center" height="40" bgcolor="#FFFFFF" width="315">
                    <IMG SRC="{$imgbase}/images/webmailtitle.gif"/>
                  </TD>
                </TR>
                <TR>
                  <TD align="center" bgcolor="#FFFFFF" width="315" class="testo">
                    <BR/><SPAN class="testoGrande">Thanks for using WebMail!</SPAN><BR/><BR/>
                    Preparing logout for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>.<BR/><BR/>
                  </TD>
                </TR>
                <TR>
                  <TD colspan="2" align="center" height="50" bgcolor="#7B889F" width="400" class="testoBianco">
                    Please stand by while your session is being closed and your configuration
                    is being written to disk.
                    If you don't see the <SPAN class="bold">login-screen</SPAN> in a few seconds, please
                    <A HREF="{/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"><SPAN class="testoScuro">click here</SPAN></A>.
                  </TD>
                </TR>
                <TR>
                  <TD colspan="2" width="400" class="testoBianco" bgcolor="#394864" height="35" align="center">
                    <SPAN class="bold">BiBop WebMail </SPAN>is based on<BR/>
                    WebMail is &#169; 1999/2000 by Sebastian Schaffert<BR/>
                  </TD>
                </TR>
               </TABLE>
             </TD>
           </TR>
         </TABLE>
         </CENTER>
      </BODY>

    </HTML>
  </xsl:template>


</xsl:stylesheet>