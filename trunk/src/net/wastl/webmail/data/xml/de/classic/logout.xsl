<?xml version="1.0" encoding="UTF-8"?>
<!--
 * Copyright (C) 2000 Sebastian Schaffert
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
-->

<!-- This template is part of the German translation -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Abmeldung</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META HTTP-EQUIV="REFRESH" CONTENT="5;URL={/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <H1><CENTER>Danke dass Du WebMail benutzt!</CENTER></H1>
        <H3><CENTER>Bereite Abmeldung vor für <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>.</CENTER></H3>
        <P>
          <CENTER>
            Die Benutzereinstellungen wurden gespeichert.
        <BR/>
            Wenn Du den <STRONG>Anmeldeschirm</STRONG> nicht in ein paar Sekunden siehst,
            <A HREF="{/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/">hier klicken</A>.
          </CENTER>
        </P>
        <P>
          <CENTER>
            <FONT SIZE="-">
              <EMPH>
                WebMail ist (c)1998-2000 von <A HREF="mailto:schaffer@informatik.uni-muenchen.de">Sebastian Schaffert</A>.
                Es steht unter den Bedingungen der GNU General Public License (GPL).
              </EMPH>
            </FONT>
          </CENTER>
        </P>
      </BODY>

    </HTML>
  </xsl:template>


</xsl:stylesheet>