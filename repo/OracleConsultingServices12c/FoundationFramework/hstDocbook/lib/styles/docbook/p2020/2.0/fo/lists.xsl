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
 ! This stylesheet is used for the fo (Formatting Objects) generation.
 ! It imports the shipped "official" stylesheet, supplies parameters, and
 ! overrides stuff.
 !
-->
<xsl:stylesheet xmlns:xsl              ="http://www.w3.org/1999/XSL/Transform"
                xmlns:src              ="http://nwalsh.com/xmlns/litprog/fragment"
                xmlns:fo               ="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="src"
                version                ="1.0">
  <!--
  =====================================================================
  == ITEMIZEDLIST and ORDEREDLIST
  ==
  == FIXME: Must be able to select an attribute set as a variable.
  ==        But how?
  ==        Now we get lots of duplicated blocks :-(
  =====================================================================
  -->
  <xsl:template match="itemizedlist|orderedlist">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="effspacing">
      <xsl:value-of select="ancestor-or-self::*[@spacing][1]/@spacing"/>
    </xsl:variable>
    <xsl:variable name="label-width">
      <xsl:call-template name="dbfo-attribute">
        <xsl:with-param name="pis"       select="processing-instruction('dbfo')"/>
        <xsl:with-param name="attribute" select="'label-width'"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="labelcol-width">
      <xsl:choose>
        <xsl:when test="$label-width != ''">
          <xsl:value-of select="$label-width"/>
        </xsl:when>
        <xsl:when test="name()='orderedlist'">
          <xsl:choose>
            <!--
            Could refine the following further: how many inheritnum levels?
            -->
            <xsl:when test="@inheritnum='inherit' and ancestor::listitem[parent::orderedlist]">
              <xsl:value-of select="'3em'"/>
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="'2em'"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="'1.2em'"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!--
    Preserve order of PIs and comments
    -->
    <xsl:apply-templates select="*[not(self::listitem or self::title or self::titleabbrev)] | comment()[not(preceding-sibling::listitem)] | processing-instruction()[not(preceding-sibling::listitem)]"/>
    <xsl:choose>
      <xsl:when test="$effspacing = 'compact' and ancestor::*[@spacing][1]/@spacing = 'compact'">
        <fo:table id="{$id}" table-layout="fixed" width="100%" start-indent="{$list.start.indent}" end-indent="0pt" table-omit-header-at-break="true" table-omit-footer-at-break="true">
          <!--
          actually, that sucks (just like in the shipped template):
          preamble should not come BEFORE the block that has the id, but in table-header!
          -->
          <fo:table-column column-number="1" column-width="{$labelcol-width}"/>
          <fo:table-column column-number="2"/>
          <!--
          maybe a "spacer column" of around 0.2em in between?
          -->
          <xsl:if test="title">
            <fo:table-header>
              <fo:table-row>
                <fo:table-cell number-columns-spanned="2">
                  <fo:block font-weight="bold">
                    <xsl:apply-templates select="." mode="object.title.markup">
                      <xsl:with-param name="allow-anchors" select="1"/>
                    </xsl:apply-templates>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-header>
          </xsl:if>
          <fo:table-body>
            <xsl:apply-templates select="listitem | comment()[preceding-sibling::listitem] | processing-instruction()[preceding-sibling::listitem]"/>
            <!--
            can you use "with-param" on apply-templates?
            if so, use: <xsl:with-param name="numcols" select="2"/>

            listitems will generate their own rows. but what happens with the rest?
            Couldn't find non-modal templates matching them! We'll see...
            -->
          </fo:table-body>
        </fo:table>
      </xsl:when>
      <xsl:otherwise>
        <fo:table id="{$id}" table-layout="fixed" width="100%" start-indent="{$list.start.indent}" end-indent="0pt" table-omit-header-at-break="true" table-omit-footer-at-break="true" xsl:use-attribute-sets="list.block.spacing">
          <fo:table-column column-number="1" column-width="{$labelcol-width}"/>
          <fo:table-column column-number="2"/>
          <xsl:if test="title">
            <fo:table-header>
              <fo:table-row>
                <fo:table-cell number-columns-spanned="2">
                  <fo:block font-weight="bold">
                    <!--
                    no space-after for compact lists, even if not nested in another compact list
                    -->
                    <xsl:if test="not($effspacing='compact')">
                      <xsl:attribute name="space-after.optimum">1em</xsl:attribute>
                    </xsl:if>
                    <xsl:apply-templates select="." mode="object.title.markup">
                      <xsl:with-param name="allow-anchors" select="1"/>
                    </xsl:apply-templates>
                  </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-header>
          </xsl:if>
          <fo:table-body>
            <xsl:apply-templates select="listitem | comment()[preceding-sibling::listitem] | processing-instruction()[preceding-sibling::listitem]"/>
          </fo:table-body>
        </fo:table>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!--
  =====================================================================
  == (ITEMIZEDLIST | ORDEREDLIST) / LISTITEM
  ==
  == FIXME: Must be able to select an attribute set as a variable.
  ==        But how?
  ==        Now we get lots of duplicated blocks :-(
  =====================================================================
  -->
  <xsl:template match="itemizedlist/listitem|orderedlist/listitem">
    <xsl:param name="numcols" select="2"/>
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="effspacing">
      <!--
      <xsl:value-of select="ancestor::*[@spacing][1]/@spacing"/>
      -->
      <xsl:value-of select="'bla'"/>
    </xsl:variable>
    <xsl:variable name="itemlabel">
      <xsl:choose>
        <xsl:when test="name(..)='orderedlist'">
          <xsl:apply-templates select="." mode="item-number"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="itemsymbol">
            <xsl:call-template name="list.itemsymbol">
              <xsl:with-param name="node" select="parent::itemizedlist"/>
            </xsl:call-template>
          </xsl:variable>
          <xsl:choose>
            <xsl:when test="$itemsymbol='dash'">&#x002d;</xsl:when>
            <xsl:when test="$itemsymbol='circle'">&#x25cc;</xsl:when>
            <xsl:when test="$itemsymbol='square'">&#x25a1;</xsl:when>
            <xsl:when test="$itemsymbol='triangle'">&#x00bb;</xsl:when>
            <xsl:otherwise>&#x2022;</xsl:otherwise>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!--
    No vertical space before first listitem. Otherwise, insert a spacer row
    -->
    <xsl:if test="position() > 1">
      <fo:table-row>
        <fo:table-cell number-columns-spanned="{$numcols}">
          <xsl:choose>
            <xsl:when test="$effspacing = 'compact'">
              <fo:block xsl:use-attribute-sets="compact.list.item.spacing"/>
            </xsl:when>
            <xsl:otherwise>
              <fo:block xsl:use-attribute-sets="list.item.spacing"/>
            </xsl:otherwise>
          </xsl:choose>
        </fo:table-cell>
      </fo:table-row>
    </xsl:if>
    <fo:table-row id="{$id}">
      <fo:table-cell>
        <fo:block>
          <xsl:value-of select="$itemlabel"/>
        </fo:block>
      </fo:table-cell>
      <fo:table-cell>
        <fo:block>
          <xsl:apply-templates/>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
  </xsl:template>
</xsl:stylesheet>
