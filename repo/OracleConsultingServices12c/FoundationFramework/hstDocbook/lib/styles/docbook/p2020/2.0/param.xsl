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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
 <!--
  #####################################################################
  ## Overridden Stylesheet Parameter
  #####################################################################
  -->
  <!--
  =============================================================================
  == These two have been moved here because they should be the same for each
  == build target (HTML, PDF etc.)
  =============================================================================
  -->
  <xsl:param name="runinhead.title.end.punct"         select="'.!?:-'"/>
  <xsl:param name="runinhead.default.title.end.punct" select="':'"/>
  <!--
  #####################################################################
  ## Stylesheet Parameter introduced by us
  #####################################################################
  -->
  <xsl:param name="runinhead.bold"                    select="1"/>
  <xsl:param name="runinhead.italic"                  select="0"/>
  <!--
  =============================================================================
  == A convenience parameter
  =============================================================================
  -->
  <xsl:param name="digits"                            select="'0123456789'"/>
  <!--
  =============================================================================
  == &#x200B; (zero-width space) WORKS - (and no more "stretching" as of FOP
  ==          0.93)
  == &#x00AD; (soft hyphen) does NOT work - FOP 0.20.5 treats it as a normal hyphen,
  ==          always displaying it!
  =============================================================================
  -->
  <xsl:param name="special-hyph.char"                 select="'&#x200B;'"/>
  <xsl:param name="special-hyph.min-before"           select="3"/>
  <xsl:param name="special-hyph.min-after"            select="2"/>
  <!--
  =============================================================================
  == Params for URL breaking (works only in ulinks, and then only if the text
  == content is (almost) the same as the @url attvalue
  ==
  == see xref.xsl
  =============================================================================
  -->
  <xsl:param name="url-hyph.char"                     select="$special-hyph.char"/>
  <xsl:param name="url-hyph.before"                   select="concat($digits, '?&amp;')"/>
  <xsl:param name="url-hyph.after"                    select="concat($digits, '/.,-=:;_@')"/>
  <xsl:param name="url-hyph.not-before"               select="'/'"/>
  <xsl:param name="url-hyph.not-after"                select="''"/>
  <xsl:param name="url-hyph.not-between"              select="'./'"/>
  <xsl:param name="url-hyph.min-before"               select="$special-hyph.min-before"/>
  <xsl:param name="url-hyph.min-after"                select="$special-hyph.min-after"/>
  <!--
  =============================================================================
  == Params for filename breaking
  =============================================================================
  -->
  <xsl:param name="filename-hyph.char"                select="$special-hyph.char"/>
  <xsl:param name="filename-hyph.before"              select="concat($digits, '?&amp;')"/>
  <xsl:param name="filename-hyph.after"               select="concat($digits, '\/.,-+=:;_')"/>
  <xsl:param name="filename-hyph.not-before"          select="'\/'"/>
  <xsl:param name="filename-hyph.not-after"           select="''"/>
  <xsl:param name="filename-hyph.not-between"         select="'./\'"/>
  <xsl:param name="filename-hyph.min-before"          select="$special-hyph.min-before"/>
  <xsl:param name="filename-hyph.min-after"           select="$special-hyph.min-after"/>
</xsl:stylesheet>