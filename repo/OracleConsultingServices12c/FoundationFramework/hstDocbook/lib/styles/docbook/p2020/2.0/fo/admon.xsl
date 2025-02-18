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
  == Given an admonition node, returns the name of the graphic that
  == should be used for that admonition.
  ==
  == we use our own conventions to pick up the graphic and not follow
  == the recommended naming conventions
  =====================================================================
  -->
  <xsl:template name="admon.graphic">
    <xsl:param name="node" select="."/>
    <xsl:variable name="filename">
      <xsl:value-of select="$admon.graphics.path"/>
      <xsl:choose>
        <xsl:when test="local-name($node)='note'">note</xsl:when>
        <xsl:when test="local-name($node)='warning'">warning</xsl:when>
        <xsl:when test="local-name($node)='caution'">caution</xsl:when>
        <xsl:when test="local-name($node)='tip'">tip</xsl:when>
        <xsl:when test="local-name($node)='important'">important</xsl:when>
        <xsl:otherwise>note</xsl:otherwise>
      </xsl:choose>
      <xsl:value-of select="$admon.graphics.extension"/>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$fop.extensions != 0 or $arbortext.extensions != 0">
        <xsl:value-of select="$filename"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>url(</xsl:text>
        <xsl:value-of select="$filename"/>
        <xsl:text>)</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!--
  =====================================================================
  == Colored background for graphical admonitions, based on a
  == solution by Carlos Guzman Alvarez.
  =====================================================================
  -->
  <xsl:template name="graphical.admonition">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="background">
      <xsl:choose>
        <xsl:when test="name()='note'">#eff6fe</xsl:when>
        <xsl:when test="name()='warning'">#fefbf3</xsl:when>
        <xsl:when test="name()='caution'">#fef5f6</xsl:when>
        <xsl:when test="name()='tip'">#effff4</xsl:when>
        <xsl:when test="name()='important'">#fdf5ff</xsl:when>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="bordercolor">
      <xsl:choose>
        <xsl:when test="name()='note'">#1d5aab</xsl:when>
        <xsl:when test="name()='warning'">#d6a042</xsl:when>
        <xsl:when test="name()='caution'">#d34349</xsl:when>
        <xsl:when test="name()='tip'">#218041</xsl:when>
        <xsl:when test="name()='important'">#7c2482</xsl:when>
      </xsl:choose>
    </xsl:variable>
    <fo:block id="{$id}" xsl:use-attribute-sets="graphical.admonition.properties" background-color="{$background}" padding="2mm" border-left="4px {$bordercolor} solid">
      <fo:list-block provisional-distance-between-starts="{$admon.graphic.width} + 8pt" provisional-label-separation="4pt">
        <fo:list-item>
          <fo:list-item-label end-indent="label-end()">
            <fo:block>
              <fo:external-graphic width="auto" height="auto" content-width="{$admon.graphic.width}">
                <xsl:attribute name="src">
                  <xsl:call-template name="admon.graphic"/>
                </xsl:attribute>
              </fo:external-graphic>
            </fo:block>
          </fo:list-item-label>
          <fo:list-item-body start-indent="body-start()">
            <xsl:if test="$admon.textlabel != 0 or title or info/title">
              <fo:block xsl:use-attribute-sets="admonition.title.properties" color="{$bordercolor}">
                <xsl:apply-templates select="." mode="object.title.markup">
                  <xsl:with-param name="allow-anchors" select="1"/>
                </xsl:apply-templates>
              </fo:block>
            </xsl:if>
            <fo:block xsl:use-attribute-sets="admonition.properties">
              <xsl:apply-templates/>
            </fo:block>
          </fo:list-item-body>
        </fo:list-item>
      </fo:list-block>
    </fo:block>
  </xsl:template>
  <!--
  =====================================================================
  == Colored background for non-graphical admonitions, based on a
  == solution by Carlos Guzman Alvarez.
  =====================================================================
  -->
  <xsl:template name="nongraphical.admonition">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="background">
      <xsl:choose>
        <xsl:when test="name()='note'">#eff6fe</xsl:when>
        <xsl:when test="name()='warning'">#fefbf3</xsl:when>
        <xsl:when test="name()='caution'">#fef5f6</xsl:when>
        <xsl:when test="name()='tip'">#effff4</xsl:when>
        <xsl:when test="name()='important'">#fdf5ff</xsl:when>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="bordercolor">
      <xsl:choose>
        <xsl:when test="name()='note'">#1d5aab</xsl:when>
        <xsl:when test="name()='warning'">#d6a042</xsl:when>
        <xsl:when test="name()='caution'">#d34349</xsl:when>
        <xsl:when test="name()='tip'">#218041</xsl:when>
        <xsl:when test="name()='important'">#7c2482</xsl:when>
      </xsl:choose>
    </xsl:variable>
<!--
    <fo:block id="{$id}" background-color="{$background}" padding="3mm" margin-top="3mm" margin-bottom="3mm" border-left="4px solid {$bordercolor}">
-->
    <fo:block id="{$id}" background-color="{$background}" border-left="4px solid {$bordercolor}" xsl:use-attribute-sets="nongraphical.admonition.style">
      <xsl:if test="$admon.textlabel != 0 or title">
        <fo:block keep-with-next='always' xsl:use-attribute-sets="admonition.title.properties" color="{$bordercolor}">
          <xsl:apply-templates select="." mode="object.title.markup"/>
        </fo:block>
      </xsl:if>
      <fo:block xsl:use-attribute-sets="admonition.properties">
        <xsl:apply-templates/>
      </fo:block>
    </fo:block>
  </xsl:template>
</xsl:stylesheet>
