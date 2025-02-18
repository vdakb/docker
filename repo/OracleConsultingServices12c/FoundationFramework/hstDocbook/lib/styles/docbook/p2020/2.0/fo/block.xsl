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
  == NEW:
  ==
  == Specifies the common document control block properties
  =====================================================================
  -->
  <xsl:attribute-set name="document.control.properties">
    <xsl:attribute name="color">
      <xsl:value-of select="$table.title.color"/>
    </xsl:attribute>
    <xsl:attribute name="background-color">
      <xsl:value-of select="$table.title.background"/>
    </xsl:attribute>
    <xsl:attribute name="height">2.5em</xsl:attribute>
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="display-align">center</xsl:attribute>
    <xsl:attribute name="space-before.minimum">3mm</xsl:attribute>
    <xsl:attribute name="space-before.optimum">3mm</xsl:attribute>
    <xsl:attribute name="space-before.maximum">3mm</xsl:attribute>
    <xsl:attribute name="space-after.optimum">0mm</xsl:attribute>
    <xsl:attribute name="space-after.minimum">0mm</xsl:attribute>
    <xsl:attribute name="space-after.maximum">0mm</xsl:attribute>
    <xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
  </xsl:attribute-set>
 <!--
  =====================================================================
  == NEW:
  ==
  == Specifies the document revision history table properties
  =====================================================================
  -->
  <xsl:attribute-set name="document.table.properties">
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
  </xsl:attribute-set>
  <!--
  =====================================================================
  == NEW:
  ==
  == Specifies the document revision status title properties
  =====================================================================
  -->
  <xsl:attribute-set name="revstatus.title.properties" use-attribute-sets="document.control.properties"/>
  <!--
  =====================================================================
  == OVERRIDE:
  ==
  == Specifies the document revision history title properties
  =====================================================================
  -->
  <xsl:attribute-set name="revhistory.title.properties" use-attribute-sets="document.control.properties"/>
  <!--
  =====================================================================
  == NEW:
  ==
  == Revision status
  =====================================================================
  -->
  <xsl:template match="revstatus">
    <fo:block-container xsl:use-attribute-sets="revstatus.title.properties">
      <fo:block margin="0.5em">
        <xsl:choose>
          <xsl:when test="title|info/title">
            <xsl:apply-templates select="title|info/title" mode="titlepage.mode"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="gentext">
              <xsl:with-param name="key" select="'RevStatus'"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </fo:block>
    </fo:block-container>
    <fo:table table-layout="fixed" xsl:use-attribute-sets="document.table.properties">
      <xsl:call-template name="anchor"/>
      <fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
      <fo:table-column column-number="2" column-width="proportional-column-width(2)"/>
      <fo:table-body start-indent="0pt" end-indent="0pt">
        <fo:table-row display-align="center" keep-with-next.within-column="always" border-after-width="1px" border-after-style="solid">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'program'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block><xsl:value-of select="./program"/></fo:block>
          </fo:table-cell>
        </fo:table-row>
        <fo:table-row display-align="center" keep-with-next.within-column="always" border-after-width="1px" border-after-style="solid">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'programdirector'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block><xsl:value-of select="./programdirector"/></fo:block>
          </fo:table-cell>
        </fo:table-row>
        <fo:table-row display-align="center" keep-with-next.within-column="always" border-after-width="1px" border-after-style="solid">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'projectmanager'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block><xsl:value-of select="./projectmanager"/></fo:block>
          </fo:table-cell>
        </fo:table-row>
        <fo:table-row display-align="center" keep-with-next.within-column="always" border-after-width="1px" border-after-style="solid">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'doctitle'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block><xsl:value-of select="./doctitle"/></fo:block>
          </fo:table-cell>
        </fo:table-row>
        <fo:table-row display-align="center" keep-with-next.within-column="always" border-after-width="1px" border-after-style="solid">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'docversion'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block><xsl:value-of select="./docversion"/></fo:block>
          </fo:table-cell>
        </fo:table-row>
        <fo:table-row display-align="center" keep-with-next.within-column="always" border-after-width="1px" border-after-style="solid">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'createdon'"/>
              </xsl:call-template>
            </fo:block>
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'createdby'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block><xsl:value-of select="./createdon"/></fo:block>
            <fo:block><xsl:value-of select="./createdby"/></fo:block>
          </fo:table-cell>
        </fo:table-row>
        <fo:table-row display-align="center" keep-with-next.within-column="always" border-after-width="1px" border-after-style="solid">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'updatedon'"/>
              </xsl:call-template>
            </fo:block>
            <fo:block font-weight="bold">
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'updatedby'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block><xsl:value-of select="./updatedon"/></fo:block>
            <fo:block><xsl:value-of select="./updatedby"/></fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-body>
    </fo:table>
  </xsl:template>
  <!--
  =====================================================================
  == OVERRIDE:
  ==
  == Revision history & friends
  =====================================================================
  -->
  <xsl:template match="revhistory">
    <fo:block-container xsl:use-attribute-sets="revhistory.title.properties">
      <fo:block margin="0.5em">
        <xsl:choose>
          <xsl:when test="title|info/title">
            <xsl:apply-templates select="title|info/title" mode="titlepage.mode"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="gentext">
              <xsl:with-param name="key" select="'RevHistory'"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </fo:block>
    </fo:block-container>
    <fo:table table-layout="fixed" xsl:use-attribute-sets="document.table.properties">
      <xsl:call-template name="anchor"/>
      <fo:table-column column-number="1" column-width="7em"/>
      <fo:table-column column-number="2" column-width="7em"/>
      <fo:table-column column-number="3" column-width="8em"/>
      <fo:table-column column-number="4" column-width="proportional-column-width(1)"/>
      <fo:table-header start-indent="0pt" end-indent="0pt">
        <fo:table-row font-weight="bold" display-align="center" keep-with-next.within-column="always" border-after-width="1px" border-after-style="solid">
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block>
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'Revision'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block>
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'Date'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block>
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'Author'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
          <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
            <fo:block>
              <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'Reference'"/>
              </xsl:call-template>
            </fo:block>
          </fo:table-cell>
        </fo:table-row>
      </fo:table-header>
      <fo:table-body start-indent="0pt" end-indent="0pt">
        <xsl:apply-templates/>
      </fo:table-body>
    </fo:table>
  </xsl:template>
  <!--
  =====================================================================
  == OVERRIDE:
  ==
  == Revision itself
  =====================================================================
  -->
  <xsl:template match="revhistory/revision">
    <fo:table-row display-align="before" border-after-width="1px" border-after-style="solid">
      <fo:table-cell xsl:use-attribute-sets="table.cell.padding">
        <fo:block>
          <xsl:apply-templates select=".//revnumber"/>
        </fo:block>
      </fo:table-cell>
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
          <xsl:apply-templates select=".//revremark|.//revdescription"/>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
</xsl:stylesheet>