<?xml version="1.0" encoding="US-ASCII"?>
<!--
  ! This software is the confidential and proprietary information of
  ! Oracle Corporation. ("Confidential Information").  You shall not
  ! disclose such Confidential Information and shall use it only in
  ! accordance with the terms of the license agreement you entered
  ! into with Oracle.
  !
  ! ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
  ! SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
  ! IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
  ! PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
  ! SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
  ! THIS SOFTWARE OR ITS DERIVATIVES.
  !
  ! Copyright (c) 2015. All Rights reserved
  !
-->
<xsl:stylesheet xmlns:xsl              ="http://www.w3.org/1999/XSL/Transform"
                xmlns:src              ="http://nwalsh.com/xmlns/litprog/fragment"
                xmlns:fo               ="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="src"
                version                ="1.0">
  <!--
  =====================================================================
  == Set relative space in header for left, center and right cells
  =====================================================================
  -->
  <xsl:param name="header.column.widths" select="'1 1 1'"/>
  <!--
  =====================================================================
  == If non-zero, a rule will be drawn below the page headers and thats
  == excatly what we dont't want.
  =====================================================================
  -->
  <xsl:param name="header.rule" select="0"/>
  <!--
  =====================================================================
  == If non-zero, headers will be placed on blank pages
  =====================================================================
  -->
  <xsl:param name="headers.on.blank.pages">0</xsl:param>
  <!--
  =====================================================================
  == Properties of page header content.
  =====================================================================
  -->
  <xsl:attribute-set name="header.content.properties">
    <xsl:attribute name="font-family">
      <xsl:value-of select="$body.font.family"/>
    </xsl:attribute>
    <xsl:attribute name="margin-left">0</xsl:attribute>
    <xsl:attribute name="margin-right">0</xsl:attribute>
  </xsl:attribute-set>
</xsl:stylesheet>