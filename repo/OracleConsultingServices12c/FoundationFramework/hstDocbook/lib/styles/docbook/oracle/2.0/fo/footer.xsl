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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo ="http://www.w3.org/1999/XSL/Format"
                version  ="1.0">
  <!--
  =====================================================================
  == If non-zero, a rule will be drawn above the page footers and thats
  == excatly what we dont't want.
  =====================================================================
  -->
  <xsl:param name="footer.rule" select="0"/>
  <xsl:template name="footer.content">
    <xsl:param name="pageclass"   select="''"/>
    <xsl:param name="sequence"    select="''"/>
    <xsl:param name="position"    select="''"/>
    <xsl:param name="gentext-key" select="''"/>
    <fo:block>
      <!--
      pageclass can be front, body, back
      sequence can be odd, even, first, blank
      position can be left, center, right
      -->
      <xsl:choose>
        <xsl:when test="$pageclass = 'titlepage'">
          <!-- nop; no footer on title pages -->
        </xsl:when>
        <xsl:when test="$double.sided != 0 and $sequence = 'even' and $position='left'">
          <fo:page-number/>
        </xsl:when>
        <xsl:when test="$double.sided != 0 and ($sequence = 'odd' or $sequence = 'first') and $position='right'">
          <fo:page-number/>
        </xsl:when>
        <xsl:when test="$double.sided = 0 and $position='left'">
          <xsl:apply-templates select="/*[1]" mode="title.markup"/>
        </xsl:when>
        <xsl:when test="$double.sided = 0 and $position = 'right'">
          <xsl:apply-templates select="." mode="title.markup"/>
          <xsl:text>      </xsl:text>
          <fo:page-number/>
        </xsl:when>
        <xsl:when test="$sequence='blank'">
          <xsl:choose>
            <xsl:when test="$double.sided != 0 and $position = 'left'">
              <fo:page-number/>
            </xsl:when>
            <xsl:when test="$double.sided = 0 and $position = 'center'">
              <fo:page-number/>
            </xsl:when>
            <xsl:otherwise>
              <!-- nop -->
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <!-- nop -->
        </xsl:otherwise>
      </xsl:choose>
    </fo:block>
  </xsl:template>
</xsl:stylesheet> 