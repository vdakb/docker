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
  == We have to change the 1.71.1 table|informaltable template because
  == it messed up our own (superior) HTML-style table handling
  ==
  == Unified handling of CALS and HTML tables, formal and not creates a
  == hierarchy of nested containers:
  == - Outer container does a float.
  == - Nested container does block-container for rotation
  == - Nested block contains title, layout table and footnotes
  == - Nested layout table placeholder template supports extensions.
  == - fo:table is innermost.
  == Created from the innermost and working out.
  == Not all layers apply to every table.
  =====================================================================
  -->
  <xsl:template match="table|informaltable">
    <!--
    introduced table-style var to avoid duplicate tests later
    -->
    <xsl:variable name="table-style">
      <xsl:choose>
        <xsl:when test="tgroup|mediaobject|graphic">cals</xsl:when>
        <xsl:otherwise>html</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="($table-style = 'cals') and .//tr">
        <xsl:message terminate="yes">
          <xsl:text>Broken table: tr descendent of CALS Table.</xsl:text>
          <xsl:text>The text in the first tr is:&#10;</xsl:text>
          <xsl:value-of select=".//tr[1]"/>
        </xsl:message>
      </xsl:when>
      <xsl:when test="($table-style = 'html') and .//row">
        <xsl:message terminate="yes">
          <xsl:text>Broken table: row descendent of HTML table.</xsl:text>
          <xsl:text>The text in the first row is:&#10;</xsl:text>
          <xsl:value-of select=".//row[1]"/>
        </xsl:message>
      </xsl:when>
    </xsl:choose>
    <!--
    So far, soo good. But this is where we leave if it concerns an HTML
    table, otherwise table.block adds a top title and a duplicate id
    -->
    <xsl:choose>
      <xsl:when test="$table-style = 'html'">
        <xsl:apply-templates select="." mode="htmlTable"/>
      </xsl:when>
      <xsl:otherwise>
        <!--
        make CALS table
        Contains fo:table, not title or footnotes
        -->
        <xsl:variable name="table.content">
          <xsl:call-template name="make.table.content"/>
        </xsl:variable>
        <!--
        Optional layout table template for extensions
        -->
        <xsl:variable name="table.layout">
          <xsl:call-template name="table.layout">
            <xsl:with-param name="table.content" select="$table.content"/>
          </xsl:call-template>
        </xsl:variable>
        <!--
        fo:block contains title, layout table, and footnotes
        <xsl:variable name="table.block">
          <xsl:call-template name="table.block">
            <xsl:with-param name="table.layout" select="$table.layout"/>
          </xsl:call-template>
        </xsl:variable>
        -->
        <!--
        pgwide or orient container
        -->
        <xsl:variable name="table.container">
          <xsl:call-template name="table.container">
            <!--
            <xsl:with-param name="table.block" select="$table.block"/>
            -->
            <xsl:with-param name="table.block" select="$table.layout"/>
          </xsl:call-template>
        </xsl:variable>
        <!--
        float or not
        -->
        <xsl:variable name="floatstyle">
          <xsl:call-template name="floatstyle"/>
        </xsl:variable>
        <xsl:choose>
          <xsl:when test="$floatstyle != ''">
            <xsl:call-template name="floater">
              <xsl:with-param name="position" select="$floatstyle"/>
              <xsl:with-param name="content"  select="$table.container"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            <xsl:copy-of select="$table.container"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>