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
  ! Driver stylesheet for the chunked (= multi-file) HTML generation.
  !
  ! IMPORTANT:
  ! - Files containing templates specific to the chunked html builds
  !   must be xsl:included here, AFTER the inclusion of the original
  !   chunk-code.xsl
  !
  ! - Files overriding other stuff (element presentation etc.) that
  !   is common to chunked and monolith html must be xsl:included in
  !   htmlbase.xsl, AFTER the import of the original docbook.xsl
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>
  <!--
  =====================================================================
  == Import default DocBook stylesheet for html generation
  =====================================================================
  -->
  <xsl:import href="@docbook.base@/xsl/1.0/html/docbook.xsl"/>
  <!--
  =====================================================================
  == Include our own customizations for html generation
  ==
  ==   EXCEPT THOSE THAT CONTROL CHUNKING BEHAVIOUR OR ARE OTHERWISE
  ==   SPECIFIC TO EITHER CHUNKED OR MONOLITH HTML
  =====================================================================
  -->
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/param.xsl"/>

  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/html/para.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/html/base.xsl"/>

  <xsl:output method="html" encoding="ISO-8859-1" indent="yes"/>
</xsl:stylesheet>