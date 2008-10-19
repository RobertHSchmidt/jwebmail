<?xml version="1.0" encoding="ISO-8859-1"?>
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
<!-- This is part of the French translation of WebMail - Christian SENET - senet@lpm.u-nancy.fr - 2002 -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" encoding="UTF-8"/>
  
    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE>Boite aux Lettres de <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>: Page de Titre</TITLE>
	<META CONTENT="AUTHOR" VALUE="Sebastian Schaffert"/>
	<META HTTP-EQUIV="REFRESH" CONTENT="5;URL={/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"/> 
      </HEAD>

      <BODY bgcolor="#ffffff">
	<H1><CENTER>Merci d'utiliser WebMail!</CENTER></H1>
	<H3><CENTER>Préparation du logout de <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/>.</CENTER></H3>
	<P>
	  <CENTER>
	    Merci de patienter le temps que votre session soit fermée et que votre configuration 
	    soit écrite sur disque.<BR/>
	    Si vous ne voyez pas la <STRONG>fenêtre de login</STRONG> dans quelques secondes, alors 
	    <A HREF="{/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/">cliquez ici</A>.
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