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
  == No padding in formalpara titles. And if the para starts with a
  == block child, the title is placed in a block (with keep-with-next)
  == instead of an inline
  =====================================================================
  -->
  <xsl:template match="formalpara/title|formalpara/info/title">
    <xsl:variable name="firstParaChild" select="ancestor::formalpara[1]/para/node()[self::text() or self::*][1]"/>
    <xsl:variable name="fpcType"        select="local-name($firstParaChild)"/>
    <xsl:variable name="para-starts-with-block">
      <!-- we can add more block element types if needed -->
      <xsl:if test="$fpcType='blockquote' or $fpcType='literallayout' or $fpcType='programlisting' or $fpcType='screen'">1</xsl:if>
    </xsl:variable>
    <xsl:variable name="element-name">
      <xsl:choose>
        <xsl:when test="$para-starts-with-block = 1">fo:block</xsl:when>
        <xsl:otherwise>fo:inline</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="keep-attr-name">
      <xsl:choose>
        <xsl:when test="$para-starts-with-block = 1">keep-with-next.within-column</xsl:when>
        <xsl:otherwise>keep-with-next.within-line</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:element name="{$element-name}">
      <xsl:attribute name="{$keep-attr-name}">always</xsl:attribute>
      <xsl:if test="$runinhead.bold = 1">
        <xsl:attribute name="font-weight">bold</xsl:attribute>
      </xsl:if>
      <xsl:if test="$runinhead.italic = 1">
        <xsl:attribute name="font-style">italic</xsl:attribute>
      </xsl:if>
      <xsl:call-template name="decorate-formalpara-title"/>
    </xsl:element>
  </xsl:template>
  <!--
  =====================================================================
  == Implement blockquote using a blind table.
  ==
  == Otherwise tables in blockquotes outdent back to the original
  == margins, and nested blockquotes are all on the same indentation
  == level
  =====================================================================
  -->
  <xsl:variable name="blockquote-indent" select="'24pt'"/>
  <xsl:template match="blockquote">
    <fo:table xsl:use-attribute-sets="blockquote.properties" table-layout="fixed" width="100%" start-indent="0pt" end-indent="0pt" margin-left="0in" margin-right="0in">
      <fo:table-column column-number="1" column-width="{$blockquote-indent}"/>
      <fo:table-column column-number="2"/>
      <fo:table-column column-number="3" column-width="{$blockquote-indent}"/>
      <fo:table-body>
        <fo:table-row>
          <fo:table-cell>
            <fo:block/>
          </fo:table-cell>
          <fo:table-cell>
            <xsl:call-template name="anchor"/>
            <fo:block>
              <xsl:if test="title">
                <fo:block xsl:use-attribute-sets="formal.title.properties">
                  <xsl:apply-templates select="." mode="object.title.markup"/>
                </fo:block>
              </xsl:if>
              <xsl:apply-templates select="*[local-name(.) != 'title' and local-name(.) != 'attribution']"/>
            </fo:block>
            <xsl:if test="attribution">
              <fo:block text-align="end">
                <!-- mdash -->
                <xsl:text>&#x2014;</xsl:text>
                <xsl:apply-templates select="attribution"/>
              </fo:block>
            </xsl:if>
          </fo:table-cell>
          <fo:table-cell>
            <fo:block/>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-body>
    </fo:table>
  </xsl:template>
  <!--
  =====================================================================
  == Revision history & friends
  =====================================================================
  -->
  <xsl:template match="revhistory">
    <xsl:variable name="explicit.table.width">
      <xsl:call-template name="dbfo-attribute">
        <xsl:with-param name="pis"       select="processing-instruction('dbfo')"/>
        <xsl:with-param name="attribute" select="'table-width'"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="table.width">
      <xsl:choose>
        <xsl:when test="$explicit.table.width != ''">
          <xsl:value-of select="$explicit.table.width"/>
        </xsl:when>
        <xsl:when test="$default.table.width = ''">
          <xsl:text>100%</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$default.table.width"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <fo:block xsl:use-attribute-sets="book.titlepage.recto.style" font-size="14pt" font-weight="bold" color="{$minor.title.color}" space-before="0.5em">
       <xsl:call-template name="gentext">
         <xsl:with-param name="key" select="'RevHistory'"/>
       </xsl:call-template>
    </fo:block>
    <fo:table table-layout="fixed" width="{$table.width}" border-top="3pt solid black" border-bottom="1pt solid black" table-omit-header-at-break="true" table-omit-footer-at-break="true" space-before="1em" text-align="start">
      <fo:table-column column-number="1" column-width="proportional-column-width(3)"/>
      <fo:table-column column-number="2" column-width="proportional-column-width(4)"/>
      <fo:table-column column-number="3" column-width="proportional-column-width(3)"/>
      <fo:table-column column-number="4" column-width="proportional-column-width(10)"/>
      <fo:table-header>
        <fo:table-row border-bottom="1pt solid black" background-color="{$shadeback.color}">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              Date
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'Author'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'Revision'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'Reference'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-header>
      <fo:table-body>
        <xsl:apply-templates/>
      </fo:table-body>
    </fo:table>
  </xsl:template>
  <!--
  =====================================================================
  == Revision itself
  =====================================================================
  -->
  <xsl:template match="revhistory/revision">
    <fo:table-row>
      <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
        <fo:block>
          <xsl:apply-templates select=".//date"/>
        </fo:block>
      </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
        <fo:block>
          <xsl:apply-templates select=".//author"/>
        </fo:block>
      </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
        <fo:block>
          <xsl:apply-templates select=".//revnumber"/>
        </fo:block>
      </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
        <fo:block>
          <xsl:apply-templates select=".//revremark|.//revdescription"/>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
  <!--
  =====================================================================
  == Revision description
  =====================================================================
  -->
  <xsl:template match="revdescription//para">
    <fo:block>
      <!--
      No normal para spacing, ie no vertical whitespace before
      -->
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
</xsl:stylesheet>