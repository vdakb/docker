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
  <xsl:param name="footer.column.widths" select="'2 1 2'"/>
  <!--
  =====================================================================
  == If non-zero, a rule will be drawn above the page footers and thats
  == excatly what we dont't want.
  =====================================================================
  -->
  <xsl:param name="footer.rule" select="0"/>
  <!--
  =====================================================================
  == If non-zero, footers will be placed on blank pages
  =====================================================================
  -->
  <xsl:param name="footers.on.blank.pages">0</xsl:param>
  <!--
  =====================================================================
  == Properties of page footer content.
  =====================================================================
  -->
  <xsl:attribute-set name="footer.content.properties">
    <xsl:attribute name="font-family">
      <xsl:value-of select="$body.font.family"/>
    </xsl:attribute>
    <xsl:attribute name="margin-left">0mm</xsl:attribute>
    <xsl:attribute name="margin-right">0mm</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Custom of page footer content.
  =====================================================================
  -->
  <xsl:template name="footer.content">
  </xsl:template>
</xsl:stylesheet>