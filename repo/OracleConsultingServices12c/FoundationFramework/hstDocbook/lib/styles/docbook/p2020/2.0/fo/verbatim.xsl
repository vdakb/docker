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
  <xsl:template match="programlisting|synopsis">
    <xsl:param    name="suppress-numbers" select="'0'"/>
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="content">
      <xsl:choose>
        <xsl:when test="$suppress-numbers = '0' and @linenumbering = 'numbered' and $use.extensions != '0' and $linenumbering.extension != '0'">
          <xsl:call-template name="number.rtf.lines">
            <xsl:with-param name="rtf">
              <xsl:apply-templates/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <fo:block id                      ="{$id}"
              xsl:use-attribute-sets  ="monospace.properties.program">
      <fo:block wrap-option           ='no-wrap'
                white-space-collapse  ='false'
                white-space-treatment ='preserve'
                linefeed-treatment    ='preserve'
                xsl:use-attribute-sets="monospace.style.program">
        <xsl:copy-of select="$content"/>
      </fo:block>
    </fo:block>
  </xsl:template>

  <xsl:template match="screen">
    <xsl:param    name="suppress-numbers" select="'0'"/>
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="content">
      <xsl:choose>
        <xsl:when test="$suppress-numbers = '0' and @linenumbering = 'numbered' and $use.extensions != '0' and $linenumbering.extension != '0'">
          <xsl:call-template name="number.rtf.lines">
            <xsl:with-param name="rtf">
              <xsl:apply-templates/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <fo:block id                      ="{$id}"
              xsl:use-attribute-sets  ="monospace.properties.screen">
      <fo:block white-space-collapse  ='false'
                white-space-treatment ='preserve'
                linefeed-treatment    ='preserve'
                xsl:use-attribute-sets="monospace.style.screen">
        <xsl:copy-of select="$content"/>
      </fo:block>
    </fo:block>
  </xsl:template>
  <xsl:template match="literallayout">
    <xsl:param name="suppress-numbers" select="'0'"/>
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="content">
      <xsl:choose>
        <xsl:when test="$suppress-numbers = '0' and @linenumbering = 'numbered' and $use.extensions != '0' and $linenumbering.extension != '0'">
          <xsl:call-template name="number.rtf.lines">
            <xsl:with-param name="rtf">
              <xsl:apply-templates/>
            </xsl:with-param>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="possibly-shaded-content">
      <xsl:choose>
        <xsl:when test="$monospace.verbatim.shade = 0">
          <xsl:copy-of select="$content"/>
        </xsl:when>
        <xsl:otherwise>
          <fo:block xsl:use-attribute-sets="monospace.style.program">
            <xsl:copy-of select="$content"/>
          </fo:block>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <fo:block id                   ="{$id}"
              wrap-option          ='no-wrap'
              white-space-collapse ='false'
              white-space-treatment='preserve'
              linefeed-treatment   ="preserve"
              padding              ="5mm"
              border-left          ="2px solid #cccccc">
      <xsl:choose>
        <xsl:when test="@class = 'monospaced'">
          <fo:block xsl:use-attribute-sets="monospace.properties.program">
            <xsl:copy-of select="$possibly-shaded-content"/>
          </fo:block>
        </xsl:when>
        <xsl:otherwise>
          <fo:block text-align='start' xsl:use-attribute-sets="verbatim.properties">
            <xsl:copy-of select="$possibly-shaded-content"/>
          </fo:block>
        </xsl:otherwise>
      </xsl:choose>
    </fo:block>
  </xsl:template>
</xsl:stylesheet>