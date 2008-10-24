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
  <xsl:template match="/">
    <HTML>
      <HEAD>
        <TITLE>WebMail Casilla de correo para <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Title Frame</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META HTTP-EQUIV="REFRESH" CONTENT="5;URL={/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"/>
      </HEAD>
      <BODY bgcolor="#ffffff">
        <H1>
          <CENTER>&#161;Gracias por usar Webmail!</CENTER>
        </H1>
        <H3>
          <CENTER>Preparando salida para <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>.</CENTER>
        </H3>
        <P>
          <CENTER>
            Por favor, aguarda mientras se cierra tu sesi&#243;n y tu configuraci&#243;n
            est&#225; siendo guardada en el disco.<BR/>
            If you don't see the <STRONG>pantalla-de-ingreso</STRONG> in a few seconds, please
            <A HREF="{/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/">haz click aqu&#237;</A>.
          </CENTER>
        </P>
        <P>
          <CENTER>
            <FONT SIZE="-1">
              <EMPH>
                WebMail es (c)1998-2000 de <A HREF="mailto:schaffer@informatik.uni-muenchen.de">Sebastian Schaffert</A>.
                It is distributed under the terms of the GNU General Public License (LGPL).
              </EMPH>
            </FONT>
          </CENTER>
        </P>
      </BODY>
    </HTML>
  </xsl:template>
</xsl:stylesheet>