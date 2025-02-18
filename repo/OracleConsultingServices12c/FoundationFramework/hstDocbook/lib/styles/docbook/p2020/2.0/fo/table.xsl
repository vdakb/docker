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
  == OVERRIDE:
  ==
  == Table frame
  =====================================================================
  -->
  <xsl:template name="table.frame">
    <xsl:attribute name="border-top-style">
      <xsl:value-of select="$table.frame.top.style"/>
    </xsl:attribute>
    <xsl:attribute name="border-bottom-style">
      <xsl:value-of select="$table.frame.bottom.style"/>
    </xsl:attribute>
    <xsl:attribute name="border-top-width">
      <xsl:value-of select="$table.frame.top.thickness"/>
    </xsl:attribute>
    <xsl:attribute name="border-bottom-width">
      <xsl:value-of select="$table.frame.bottom.thickness"/>
    </xsl:attribute>
    <xsl:attribute name="border-top-color">
      <xsl:value-of select="$table.frame.top.color"/>
    </xsl:attribute>
    <xsl:attribute name="border-bottom-color">
      <xsl:value-of select="$table.frame.bottom.color"/>
    </xsl:attribute>
  </xsl:template>
  <!--
  =====================================================================
  == OVERRIDE:
  ==
  == Table header
  =====================================================================
  -->
  <xsl:template name="table.row.properties">
   <xsl:if test="ancestor::thead">
     <xsl:attribute name="color">
       <xsl:value-of select="$table.title.color"/>
     </xsl:attribute>
     <xsl:attribute name="background-color">
       <xsl:value-of select="$table.title.background"/>
     </xsl:attribute>
     <xsl:attribute name="font-weight">bold</xsl:attribute>
     <xsl:attribute name="display-align">center</xsl:attribute>
     <xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
   </xsl:if>
  </xsl:template>
  <!--
  =====================================================================
  == OVERRIDE:
  ==
  == Table Block (avoid caption)
  =====================================================================
  -->
  <xsl:template name="table.block">
    <xsl:param name="table.layout" select="NOTANODE"/>
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="keep-together">
      <xsl:call-template name="dbfo-attribute">
        <xsl:with-param name="pis"       select="processing-instruction('dbfo')"/>
        <xsl:with-param name="attribute" select="'keep-together'"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="param.placement" select="substring-after(normalize-space($formal.title.placement), concat(local-name(.), ' '))"/>
    <xsl:variable name="placement">
      <xsl:choose>
        <xsl:when test="contains($param.placement, ' ')">
          <xsl:value-of select="substring-before($param.placement, ' ')"/>
        </xsl:when>
        <xsl:when test="$param.placement = ''">before</xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$param.placement"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="self::table">
        <fo:block id="{$id}" xsl:use-attribute-sets="table.properties">
          <!--
          table.properties by default uses formal.object.properties,
          which contains keep-together.within-column = "always"
          -->
          <xsl:if test="$keep-together != ''">
            <xsl:attribute name="keep-together.within-column">
              <xsl:value-of select="$keep-together"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:copy-of select="$table.layout"/>
          <xsl:call-template name="table.footnote.block"/>
        </fo:block>
      </xsl:when>
      <xsl:otherwise>
        <fo:block id="{$id}" xsl:use-attribute-sets="informaltable.properties">
          <xsl:if test="$keep-together != ''">
            <xsl:attribute name="keep-together.within-column">
              <xsl:value-of select="$keep-together"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:copy-of select="$table.layout"/>
          <xsl:call-template name="table.footnote.block"/>
        </fo:block>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>