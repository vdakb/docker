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
                xmlns:fox="http://xmlgraphics.apache.org/fop/extensions"
                version  ="1.0">
  <xsl:template match="restapi">
    <xsl:variable name="label">
      <xsl:choose>
        <xsl:when test="@role='method'">METHOD</xsl:when>
        <xsl:when test="@role='search'">GET</xsl:when>
        <xsl:when test="@role='create'">POST</xsl:when>
        <xsl:when test="@role='update'">PUT</xsl:when>
        <xsl:when test="@role='modify'">PATCH</xsl:when>
        <xsl:when test="@role='delete'">DELETE</xsl:when>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="light">
      <xsl:choose>
        <xsl:when test="@role='method'">#c2c2c2</xsl:when>
        <xsl:when test="@role='search'">#effff4</xsl:when>
        <xsl:when test="@role='create'">#eff6fe</xsl:when>
        <xsl:when test="@role='update'">#fefbf3</xsl:when>
        <xsl:when test="@role='modify'">#fefbf3</xsl:when>
        <xsl:when test="@role='delete'">#fef5f6</xsl:when>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="dark">
      <xsl:choose>
        <xsl:when test="@role='method'">#2c2c2c</xsl:when>
        <xsl:when test="@role='search'">#218041</xsl:when>
        <xsl:when test="@role='create'">#1d5aab</xsl:when>
        <xsl:when test="@role='update'">#d6a042</xsl:when>
        <xsl:when test="@role='modify'">#d6a042</xsl:when>
        <xsl:when test="@role='delete'">#d34349</xsl:when>
      </xsl:choose>
    </xsl:variable>
    <fo:block background-color="{$light}" border="1px {$dark} solid" keep-together.within-page="1">
      <fo:table width="100%" margin="1mm" border="0">
        <fo:table-column column-number="1" column-width="10%" background-color="{$dark}"/>
        <fo:table-column column-number="2" column-width="80%" background-color="{$light}"/>
        <fo:table-body>
           <fo:table-row line-height="2em">
             <fo:table-cell column-number="1">
               <fo:block color="{$light}" font-weight="bold" text-align="center"><xsl:value-of select='$label'/></fo:block>
             </fo:table-cell>
             <fo:table-cell column-number="2">
               <fo:block>
                 <fo:inline color="#000000">
                  <xsl:call-template name="inline.charseq"/>
                 </fo:inline>
               </fo:block>
             </fo:table-cell>
           </fo:table-row>
        </fo:table-body>
      </fo:table>
    </fo:block>
  </xsl:template>
</xsl:stylesheet>