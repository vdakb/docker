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
  <xsl:template match="filename//text()">
    <xsl:call-template name="hyphenate-special">
      <xsl:with-param name="str"         select="."/>
      <xsl:with-param name="before"      select="$filename-hyph.before"/>
      <xsl:with-param name="after"       select="$filename-hyph.after"/>
      <xsl:with-param name="not-before"  select="$filename-hyph.not-before"/>
      <xsl:with-param name="not-after"   select="$filename-hyph.not-after"/>
      <xsl:with-param name="not-between" select="$filename-hyph.not-between"/>
      <xsl:with-param name="hyph-char"   select="$filename-hyph.char"/>
      <xsl:with-param name="min-before"  select="$filename-hyph.min-before"/>
      <xsl:with-param name="min-after"   select="$filename-hyph.min-after"/>
    </xsl:call-template>
  </xsl:template>
  <!-- Renders the current node as versioninfo: -->
  <xsl:template name="make-vi">
    <xsl:param name="break" select="''"/>
    <xsl:variable name="content">
      <xsl:call-template name="inline.boldseq"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$break='after'">
        <fo:block keep-with-next.within-column="always">
          <xsl:copy-of select="$content"/>
        </fo:block>
      </xsl:when>
      <xsl:when test="$break='before'">
        <fo:block keep-with-previous.within-column="always">
          <xsl:copy-of select="$content"/>
        </fo:block>
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy-of select="$content"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!--
  In the above template, the wrapping of the content in a fo:block forces a line
  break both before and after the content. This will probably never become a
  problem in practice, but if it does, this is an (untested!) alternative for
  break='after':

    <xsl:inline><xsl:copy-of select="$content"/></xsl:inline>
    <fo:block keep-with-previous.within-column="always" keep-with-next.within-column="always"/>

  The idea here is that the empty block will glue the vi content to the text
  that follows. Question is if this works between block and inline areas!
  We may also try keep-with-next on the fo:inline.
  -->
</xsl:stylesheet>