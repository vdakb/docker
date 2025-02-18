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
  #####################################################################
  ## Overridden Stylesheet Parameter
  #####################################################################
  -->
  <!--
  =====================================================================
  == By default, stylesheets use the generate.toc parameter to
  == determine which elements have a TOC generated at the beginning of
  == the element in the output.
  == For print output or non-chunked HTML output, a single TOC at the
  == beginning may suffice.
  == But when you are generating chunked HTML files, you may want
  == certain sublevels to provide TOCs to help orient the reader.
  ==
  == Default: ??=
  =====================================================================
  -->
  <xsl:param name="generate.toc">
    appendix  nop
    article/appendix  nop
    article   toc,title
    book      toc,title
    chapter   nop
    part      nop
    preface   nop
    qandadiv  nop
    qandaset  nop
    reference nop
    sect1     nop
    sect2     nop
    sect3     nop
    sect4     nop
    sect5     nop
    section   nop
    set       toc,title
  </xsl:param>
  <!--
  #####################################################################
  ## Stylesheet Parameter introduced by us
  #####################################################################
  -->
  <xsl:param name="ocs-home.url"             select="'https://www.oracle.com/de/consulting/'"/>
  <xsl:param name="ocs-home.title"           select="'Oracle Deutschland / Oracle Consulting'"/>

  <xsl:param name="ocs-docindex.url"         select="'https://stbeehive.oracle.com/teamcollab/wiki/iam-germany/en/documentation/'"/>
  <xsl:param name="ocs-docindex.title"       select="'Identity and Access Management Documentation Index'"/>
  <!--
  =====================================================================
  == Highlighting text colors
  =====================================================================
  -->
  <xsl:param name="pagetitle.color"          select="'#ffffff'"/>
  <xsl:param name="highlight.color"          select="'#0000ff'"/>
</xsl:stylesheet>