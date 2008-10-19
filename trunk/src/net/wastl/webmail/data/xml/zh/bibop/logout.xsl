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
  <xsl:output method="html" indent="yes"/>

   <xsl:variable name="imgbase" select="/USERMODEL/STATEDATA/VAR[@name='img base uri']/@value"/>
    <xsl:variable name="base" select="/USERMODEL/STATEDATA/VAR[@name='base uri']/@value"/>

  
    <xsl:template match="/">

    <HTML>
      <HEAD>
        <TITLE> <xsl:value-of select="/USERMODEL/USERDATA/FULL_NAME"/> 的 WebMail 信箱: 標題框</TITLE>
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
                    <BR/><SPAN class="testoGrande">感謝您使用
                    WebMail!</SPAN><BR/><BR/> 準備為 <xsl:value-of
                    select="/USERMODEL/USERDATA/FULL_NAME"/> 登出系統.<BR/><BR/>
                  </TD>
                </TR>
                <TR>
                  <TD colspan="2" align="center" height="50" bgcolor="#7B889F" width="400" class="testoBianco">
                        請稍候，正在將您的連線關閉並將組態寫回磁碟中。
                    如果幾秒鐘內您還沒見到 <SPAN class="bold">登入視窗</SPAN> 出現, 請 
                    <A HREF="{/USERMODEL/STATEDATA/VAR[@name='base uri']/@value}/"><SPAN class="testoScuro">按這裡</SPAN></A>.
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