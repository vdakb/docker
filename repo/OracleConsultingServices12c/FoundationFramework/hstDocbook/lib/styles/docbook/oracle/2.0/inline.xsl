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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <!--
  =====================================================================
  == Templates to render version info.
  == The "make-vi" templates called from here are defined per target
  == (HTML, PDF...)
  ==
  == Render as versioninfo, with possible line break before or after:
  =====================================================================
  -->
  <xsl:template match="emphasis[@role='vi']">
    <xsl:variable name="at_start" select="not(preceding-sibling::* or preceding-sibling::text())"/>
    <xsl:variable name="at_end" select="not(following-sibling::* or following-sibling::text())"/>
    <xsl:call-template name="make-vi">
      <xsl:with-param name="break">
        <xsl:choose>
          <xsl:when test="$at_start and not($at_end)">after</xsl:when>
          <xsl:when test="$at_end and not($at_start)">before</xsl:when>
        </xsl:choose>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  <!--
  =====================================================================
  == This mode is active in ToCs, page headers and footers, etc.
  == We want no breaks in such cases:
  =====================================================================
  -->
  <xsl:template match="emphasis[@role='vi']" mode="no.anchor.mode">
    <xsl:call-template name="make-vi"/>
  </xsl:template>
  <!--
  TODO - but no hurry at all:
  Give the make-vi templates an extra param 'no-anchors' causing IDs, links, HTML names etc. to
  be suppressed. Call make-vi with this param when in no.anchor.mode. This prevents duplicate ids,
  names, etc.
  In practice though this will only pose a problem if someone gives the <emphasis role="vi"> element
  an id, or includes links or xrefs in it, i.e. probably never.
  -->
</xsl:stylesheet>
