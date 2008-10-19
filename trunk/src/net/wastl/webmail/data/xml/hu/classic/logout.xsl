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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>

    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>WebMail Mailbox for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Title Frame</TITLE>
        <META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
        <META HTTP-EQUIV="REFRESH" CONTENT="5;URL={/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"/>
      </HEAD>

      <BODY bgcolor="#ffffff">
        <H1><CENTER>Thanks for using WebMail!</CENTER></H1>
        <H3><CENTER>Preparing logout for <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>.</CENTER></H3>
        <P>
          <CENTER>
            Please stand by while your session is being closed and your configuration
            is being written to disk.<BR/>
            If you don't see the <STRONG>login-screen</STRONG> in a few seconds, please
            <A HREF="{/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/">click here</A>.
          </CENTER>
        </P>
        <P>
          <CENTER>
            <FONT SIZE="-1">
              <EMPH>
                WebMail is (c)1998-2000 by <A HREF="mailto:schaffer@informatik.uni-muenchen.de">Sebastian Schaffert</A>.
                It is distributed under the terms of the GNU General Public License (LGPL).
              </EMPH>
            </FONT>
          </CENTER>
        </P>
      </BODY>

    </HTML>
  </xsl:template>


</xsl:stylesheet>