<?xml version="1.0" encoding="ISO-8859-1"?>

<!-- CVS ID: $Id$ -->

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
  <xsl:output method="html" indent="yes"/>
  
  <xsl:template match="README">

    <HTML>
      <HEAD>
        <TITLE><xsl:apply-templates select="TITLE"/></TITLE>
        <META CONTENT="AUTHOR" VALUE="{AUTHOR}"/>
      </HEAD>
      <BODY BGCOLOR="#ffffff">
        <CENTER><H1>README for <xsl:value-of select="TITLE"/></H1></CENTER>
        <xsl:apply-templates select="SECTION"/>
      </BODY>
    </HTML>
  </xsl:template>

  <xsl:template match="SECTION">
    <A NAME="{@id}"/>
    <xsl:variable name="cur_section" select="position()"/>
     <TABLE WIDTH="100%" BGCOLOR="#a0d895" CELLSPACING="0" BORDER="0">
        <TR>
          <TD>
            <H1><CENTER><xsl:number value="position()" format="I"/>. <xsl:value-of select="@name"/></CENTER></H1>
          </TD>
        </TR>
     </TABLE>
    <xsl:apply-templates select="*[self::NOTE or self::P or self::CONTENTS]"/>
    <xsl:apply-templates select="SUBSECTION">
      <xsl:with-param name="cur_section" select="$cur_section"/>
    </xsl:apply-templates>
     <A HREF="#{@id}">Top of section "<xsl:value-of select="@name"/>"</A>
   </xsl:template>

  <xsl:template match="SUBSECTION">
    <A NAME="{@id}"/>
    <xsl:variable name="cur_subsection" select="position()"/>
    <H2>
      <xsl:number value="$cur_section"/>.<xsl:number value="position()"/>. <xsl:value-of select="@name"/>
    </H2>
    <xsl:apply-templates select="*[self::NOTE or self::P]"/>
    <xsl:apply-templates select="SUBSUBSECTION">
      <xsl:with-param name="cur_section" select="$cur_section"/>
      <xsl:with-param name="cur_subsection" select="$cur_subsection"/>
    </xsl:apply-templates>
    <A HREF="#{@id}">Top of section "<xsl:value-of select="@name"/>"</A>
    <HR/>
  </xsl:template>
  
  <xsl:template match="SUBSUBSECTION">
    <A NAME="{@id}"/>
    <H3><xsl:number value="$cur_section"/>.<xsl:number value="$cur_subsection"/>.<xsl:number value="position()"/>. <xsl:value-of select="@name"/></H3>
    <xsl:apply-templates/>
    <A HREF="#{@id}">Top of section "<xsl:value-of select="@name"/>"</A>
  </xsl:template>

   <xsl:template match="CONTENTS">
     <OL>
       <xsl:for-each select="../../SECTION">
         <LI><A HREF="#{@id}"><xsl:value-of select="@name"/></A></LI>
       </xsl:for-each>
     </OL>
   </xsl:template>

   <xsl:template match="P">
     <P>
       <xsl:apply-templates/>
     </P>
   </xsl:template>

   <xsl:template match="STRONG">
     <STRONG>
       <xsl:apply-templates/> 
     </STRONG>
   </xsl:template>

   <xsl:template match="EM">
     <I>
       <xsl:apply-templates/>
     </I>
   </xsl:template>

   <xsl:template match="CODE">
     <CODE>
       <xsl:apply-templates/> 
     </CODE>
   </xsl:template>

   <xsl:template match="NOTE">
     <CENTER>
       <TABLE WIDTH="80%" BGCOLOR="#eae723" BORDER="1">
         <TR>
           <TD><B>Note:</B> <xsl:apply-templates/></TD>
         </TR>
       </TABLE>
     </CENTER>
   </xsl:template>

   <xsl:template match="LIST">
     <UL>
       <xsl:apply-templates/>
     </UL>
   </xsl:template>

   <xsl:template match="NLIST">
     <OL>
       <xsl:apply-templates/>
     </OL>
   </xsl:template>

   <xsl:template match="ITEM">
     <LI><xsl:apply-templates/></LI>
   </xsl:template>

   <xsl:template match="A">
     <A HREF="{@href}"><xsl:apply-templates/></A>
   </xsl:template>

   <xsl:template match="BR">
     <BR/>
   </xsl:template>

   <xsl:template match="TABLE">
     <TABLE>
       <xsl:apply-templates/>
     </TABLE>
   </xsl:template>

   <xsl:template match="TR">
     <TR>
       <xsl:apply-templates/>
     </TR>
   </xsl:template>

   <xsl:template match="TD">
     <TD>
       <xsl:apply-templates/>
     </TD>
   </xsl:template>

</xsl:stylesheet>