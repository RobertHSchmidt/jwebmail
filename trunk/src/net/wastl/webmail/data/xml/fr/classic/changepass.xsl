<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
 * changepass.xsl is a set of named templates that can be used to display
 * all the prompts needed to change a password in a larger setup page.
 * Each of these templates assumes that it is in a table.  Furthermore, the
 * template that should be called directly is "changepass", and it should
 * get a parameter named "cptmpl" which is the name of the changepass
 * template to use.  It will then call the appropriate template.
 *
 * This file should be included in an xml which uses this functionality via
 * the <xsl:inculde> directive.
 *
 * I don't really like the way this works right now, however, it does work.
 * It feels like there should be a better way to do it.
--> 
<!--
 * Copyright (C) 2000 Devin Kowatch
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

  <xsl:template name="changepass_header">
    <xsl:param name="cptmpl">normchangepass</xsl:param> 
    <xsl:param name="verbose">yes</xsl:param>
    <xsl:param name="sd"/>

    <xsl:choose>
      
      <!-- a normal password change -->
      <xsl:when test="$cptmpl = 'normchangepass'">
      <!-- do nothing special -->
      </xsl:when>

      <!-- an OTP password change -->
      <xsl:when test="$cptmpl = 'otpchangepass'">
        <xsl:if test="$verbose = 'yes'">
          Entrez un nouveau challenge et votre mot de passe dans votre générateur OTP.
          Ensuite, entrez le résultat obtenu dans le Champ 'Mot de passe' ci-dessous. <BR/>
        </xsl:if>
        Nouveau Challenge: 
        <xsl:value-of select='$sd[@name="new challenge"]/@value'/>
      </xsl:when>

    </xsl:choose>
  </xsl:template>

  <xsl:template name="changepass_input">
    <xsl:param name="cptmpl">normchangepass</xsl:param>
    <xsl:param name="tag">password</xsl:param>
    <xsl:param name="sd"/>
    <xsl:variable name="plen" select="$sd[@name='pass len']/@value"/>

    <xsl:choose>
      <!-- a normal password change box -->
      <xsl:when test="$cptmpl = 'normchangepass'">
        <INPUT TYPE="PASSWORD" NAME="{$tag}" SIZE="{$plen}"/>
      </xsl:when>

      <!-- an OTP password change box -->
      <xsl:when test="$cptmpl = 'otpchangepass'">
        <INPUT TYPE="TEXT" NAME="{$tag}" SIZE="{$plen}"/> 
      </xsl:when>

    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
