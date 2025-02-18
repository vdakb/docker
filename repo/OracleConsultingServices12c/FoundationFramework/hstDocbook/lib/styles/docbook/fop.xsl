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
  =====================================================================
  == Import default DocBook stylesheet for fo generation
  =====================================================================
  -->
  <xsl:import href="@docbook.base@/xsl/1.0/fo/docbook.xsl"/>
  <!--
  =====================================================================
  == Include our own customizations for fo generation
  =====================================================================
  -->
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/param.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/titles.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/gentext.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/inline.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/hyph.xsl"/>

  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/param.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/header.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/footer.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/page.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/verbatim.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/inline.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/lists.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/formal.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/block.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/table.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/sections.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/titlepage.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/admon.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/xref.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/autotoc.xsl"/>
  <xsl:import href="@docbook.base@/lib/styles/docbook/@docbook.style@/fo/component.xsl"/>
</xsl:stylesheet>