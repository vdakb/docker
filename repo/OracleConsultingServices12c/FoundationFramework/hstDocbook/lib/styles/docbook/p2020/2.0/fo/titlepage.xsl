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
<xsl:stylesheet xmlns:xsl              ="http://www.w3.org/1999/XSL/Transform"
                xmlns:exsl             ="http://exslt.org/common"
                xmlns:fo               ="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="exsl"
                version                ="1.0">
  <!--
  =====================================================================
  == New template to display logo and inlets on titlepage
  =====================================================================
  -->
  <!--xsl:template name="front.cover"-->
  <xsl:template name="titlepage.cover">
    <fo:block-container absolute-position="fixed" top="0mm" left="0mm" width="210mm" height="297mm" background-image="file:////Users/dsteding/Project/OracleConsultingServices12c/FoundationFramework/hstDocbook/lib/styles/docbook/p2020/img/background.png">
      <fo:block-container background-color="white" absolute-position="absolute" top="5mm" left="5mm" width="200mm" height="106mm" display-align="center" border-color="white" border-style="solid">
        <fo:block/>
      </fo:block-container>
      <fo:block-container absolute-position="absolute" top="14mm" left="140mm">
        <fo:block line-height="0">
          <fo:external-graphic width="55mm" content-width="scale-down-to-fit" src="file:///Users/dsteding/Project/OracleConsultingServices12c/FoundationFramework/hstDocbook/lib/styles/docbook/p2020/img/logo.png"/>
        </fo:block>
      </fo:block-container>
      <fo:block-container absolute-position="absolute" top="277mm" left="25mm">
        <fo:block line-height="0">
          <fo:external-graphic height="10mm" content-height="scale-down-to-fit" src="file:///Users/dsteding/Project/OracleConsultingServices12c/FoundationFramework/hstDocbook/lib/styles/docbook/p2020/img/footer.png"/>
        </fo:block>
      </fo:block-container>
    </fo:block-container>
  </xsl:template>
  <!--
  <xsl:template name="back.cover">
    <fo:block-container absolute-position="fixed" top="0mm" left="0mm" width="210mm" height="297mm" background-image="file:////Users/dsteding/Project/OracleConsultingServices12c/FoundationFramework/hstDocbook/lib/styles/docbook/p2020/img/background.png">
      <fo:block-container background-color="white" absolute-position="absolute" top="5mm" left="5mm" width="200mm" height="106mm" display-align="center" border-color="white" border-style="solid">
        <fo:block/>
      </fo:block-container>
      <fo:block-container absolute-position="absolute" top="14mm" left="140mm">
        <fo:block line-height="0">
          <fo:external-graphic width="55mm" content-width="scale-down-to-fit" src="file:///Users/dsteding/Project/OracleConsultingServices12c/FoundationFramework/hstDocbook/lib/styles/docbook/p2020/img/logo.png"/>
        </fo:block>
      </fo:block-container>
    </fo:block-container>
  </xsl:template>
  -->
  <!--
  =====================================================================
  == OVERRIDE:
  ==
  == Not everything in bold (finetuning in titlepage-templates.xml)
  =====================================================================
  -->
  <xsl:attribute-set name="book.titlepage.recto.style">
    <xsl:attribute name="font-family">
      <xsl:value-of select="$title.fontset"/>
    </xsl:attribute>
    <xsl:attribute name="font-weight">normal</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == OVERRIDE:
  ==
  == subtitle template doesn't have to be called from here anymore
  =====================================================================
  -->
  <xsl:template name="book.verso.title">
    <fo:block>
      <xsl:apply-templates mode="titlepage.mode"/>
    </fo:block>
  </xsl:template>
  <!--
  =====================================================================
  == Recto (Right) Title Page
  ==
  == This is the main page and the content appears in the following
  == order:
  ==   the book title, from /book/title or /book/info/title
  ==   the book subtitle, from /book/subtitle or /book/info/subtitle
  ==   the book's author, from /book/info/author
  ==   the edition, from /book/info/edition
  =====================================================================
  -->
  <xsl:template name="book.titlepage.before.recto">
    <xsl:call-template name="titlepage.cover"/>
  </xsl:template>
  <xsl:template name="book.titlepage.recto">
    <xsl:choose>
      <xsl:when test="bookinfo/title">
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/title"/>
      </xsl:when>
      <xsl:when test="title">
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="title"/>
      </xsl:when>
    </xsl:choose>
    <xsl:choose>
      <xsl:when test="bookinfo/subtitle">
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/subtitle"/>
      </xsl:when>
      <xsl:when test="info/subtitle">
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/subtitle"/>
      </xsl:when>
      <xsl:when test="subtitle">
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="subtitle"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <!--
  =====================================================================
  == Verso (Left) Title Page
  ==
  == This usually holds the imprint and the content appears in the
  == following order:
  ==   the book title, from /book/title or /book/info/title
  ==   the book subtitle, from /book/subtitle or /book/info/subtitle
  ==   the book's author, from /book/info/author
  ==   the edition, from /book/info/edition
  =====================================================================
  -->
  <xsl:template name="book.titlepage.before.verso">
    <fo:block break-after="page"/>
  </xsl:template>
  <xsl:template name="book.titlepage.verso">
    <xsl:choose>
      <xsl:when test="bookinfo/title">
        <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/title"/>
      </xsl:when>
      <xsl:when test="title">
        <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="title"/>
      </xsl:when>
    </xsl:choose>
    <xsl:choose>
    <!--
      <xsl:when test="bookinfo/subtitle">
        <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/subtitle"/>
      </xsl:when>
    -->
      <xsl:when test="info/subtitle">
        <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/subtitle"/>
      </xsl:when>
      <xsl:when test="subtitle">
        <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="subtitle"/>
      </xsl:when>
    </xsl:choose>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/edition"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/edition"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/corpauthor"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/corpauthor"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/copyright"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/copyright"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/pubdate"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/pubdate"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/authorgroup"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/authorgroup"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/author"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/author"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/othercredit"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/othercredit"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/abstract"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/abstract"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/legalnotice"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/legalnotice"/>
    <!--
    Extension start
    -->
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/revstatus"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="bookinfo/revhistory"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/revstatus"/>
    <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="info/revhistory"/>
    <!--
    Extension end
    -->
  </xsl:template>
  <xsl:template name="book.titlepage.separator">
    <fo:block break-after="page"/>
  </xsl:template>
  <!--
  =====================================================================
  == Title Page Controller
  =====================================================================
  -->
  <xsl:template name="book.titlepage">
    <fo:block>
      <xsl:variable name="recto.content">
        <xsl:call-template name="book.titlepage.before.recto"/>
        <xsl:call-template name="book.titlepage.recto"/>
      </xsl:variable>
      <xsl:variable name="recto.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($recto.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($recto.content) != '') or ($recto.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$recto.content"/>
        </fo:block>
      </xsl:if>
      <xsl:variable name="verso.content">
        <xsl:call-template name="book.titlepage.before.verso"/>
        <xsl:call-template name="book.titlepage.verso"/>
      </xsl:variable>
      <xsl:variable name="verso.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($verso.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($verso.content) != '') or ($verso.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$verso.content"/>
        </fo:block>
      </xsl:if>
      <xsl:call-template name="book.titlepage.separator"/>
    </fo:block>
  </xsl:template>
  <!--
  =====================================================================
  == Recto (Right) Title Page Generator
  =====================================================================
  -->
  <xsl:template match="*" mode="book.titlepage.recto.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="*" mode="book.titlepage.verso.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="title" mode="book.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.recto.style" font-size="32pt" color="{$major.title.color}" space-before="40pt">
      <xsl:call-template name="division.title">
        <xsl:with-param name="node" select="ancestor-or-self::book[1]"/>
      </xsl:call-template>
    </fo:block>
  </xsl:template>
  <xsl:template match="subtitle" mode="book.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.recto.style" font-size="24pt" color="{$minor.title.color}" space-before="20pt">
      <xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <!--
  <xsl:template match="edition" mode="book.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.recto.style" font-style="italic" color="{$minor.title.color}" space-before="0.5em">
      <xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  -->
  <!--
  =====================================================================
  == Verso (Left) Title Page Generator
  =====================================================================
  -->
  <xsl:template match="title" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" font-size="24pt" font-family="{$title.fontset}">
      <xsl:call-template name="book.verso.title"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="subtitle" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" font-size="32pt" color="{$minor.title.color}" space-before="40pt">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="edition" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="0.5em">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="corpauthor" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="0.5em">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="authorgroup" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="0.5em">
      <xsl:call-template name="verso.authorgroup"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="author" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="0.5em">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="othercredit" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="0.5em">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="pubdate" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="0.5em">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="copyright" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="1.0em">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="abstract" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="0.5em">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="legalnotice" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style">
      <xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="revstatus" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style">
      <xsl:apply-templates select="."/>
    </fo:block>
  </xsl:template>
  <xsl:template match="revhistory" mode="book.titlepage.verso.auto.mode">
    <fo:block xsl:use-attribute-sets="book.titlepage.verso.style">
      <xsl:apply-templates select="."/>
    </fo:block>
  </xsl:template>

  <xsl:template name="part.titlepage.recto">
    <xsl:choose>
      <xsl:when test="partinfo/title">
        <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/title"/>
      </xsl:when>
      <xsl:when test="docinfo/title">
        <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="docinfo/title"/>
      </xsl:when>
      <xsl:when test="info/title">
        <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="info/title"/>
      </xsl:when>
      <xsl:when test="title">
        <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="title"/>
      </xsl:when>
    </xsl:choose>
    <xsl:choose>
      <xsl:when test="partinfo/subtitle">
        <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/subtitle"/>
      </xsl:when>
      <xsl:when test="docinfo/subtitle">
        <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="docinfo/subtitle"/>
      </xsl:when>
      <xsl:when test="info/subtitle">
        <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="info/subtitle"/>
      </xsl:when>
      <xsl:when test="subtitle">
        <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="subtitle"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="part.titlepage.verso"/>

  <xsl:template name="part.titlepage.separator"/>
  <xsl:template name="part.titlepage.before.recto"/>
  <xsl:template name="part.titlepage.before.verso"/>

  <xsl:template name="part.titlepage">
    <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <xsl:variable name="recto.content">
        <xsl:call-template name="part.titlepage.before.recto"/>
        <xsl:call-template name="part.titlepage.recto"/>
      </xsl:variable>
      <xsl:variable name="recto.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($recto.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($recto.content) != '') or ($recto.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$recto.content"/>
        </fo:block>
      </xsl:if>
      <xsl:variable name="verso.content">
        <xsl:call-template name="part.titlepage.before.verso"/>
        <xsl:call-template name="part.titlepage.verso"/>
      </xsl:variable>
      <xsl:variable name="verso.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($verso.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($verso.content) != '') or ($verso.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$verso.content"/>
        </fo:block>
      </xsl:if>
      <xsl:call-template name="part.titlepage.separator"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="*" mode="part.titlepage.recto.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="*" mode="part.titlepage.verso.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="title" mode="part.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="part.titlepage.recto.style" text-align="center" font-size="14pt" font-family="{$title.fontset}" color="{$minor.title.color}">
      <xsl:call-template name="division.title">
        <xsl:with-param name="node" select="ancestor-or-self::part[1]"/>
      </xsl:call-template>
    </fo:block>
  </xsl:template>
  <xsl:template match="subtitle" mode="part.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="part.titlepage.recto.style" text-align="center" font-size="14pt" font-family="{$title.fontset}">
      <xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <!--
  =====================================================================
  == Recto (Right) Preface Page
  =====================================================================
  -->
  <xsl:template name="preface.titlepage.recto">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style" margin-left="{$title.margin.left}" font-size="24pt" font-family="{$title.fontset}" font-weight="bold" color="{$minor.title.color}">
      <xsl:call-template name="component.title">
        <xsl:with-param name="node" select="ancestor-or-self::preface[1]"/>
      </xsl:call-template>
    </fo:block>
    <xsl:choose>
      <xsl:when test="prefaceinfo/subtitle">
        <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/subtitle"/>
      </xsl:when>
      <xsl:when test="docinfo/subtitle">
        <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/subtitle"/>
      </xsl:when>
      <xsl:when test="info/subtitle">
        <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/subtitle"/>
      </xsl:when>
      <xsl:when test="subtitle">
        <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="subtitle"/>
      </xsl:when>
    </xsl:choose>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/corpauthor"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/corpauthor"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/corpauthor"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/authorgroup"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/authorgroup"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/authorgroup"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/author"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/author"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/author"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/othercredit"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/othercredit"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/othercredit"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/releaseinfo"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/releaseinfo"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/releaseinfo"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/copyright"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/copyright"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/copyright"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/legalnotice"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/legalnotice"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/legalnotice"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/pubdate"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/pubdate"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/pubdate"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/revision"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/revision"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/revision"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/revhistory"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/revhistory"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/revhistory"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/abstract"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="docinfo/abstract"/>
    <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="info/abstract"/>
  </xsl:template>

  <xsl:template name="preface.titlepage.verso"/>
  <xsl:template name="preface.titlepage.separator"/>
  <xsl:template name="preface.titlepage.before.recto"/>
  <xsl:template name="preface.titlepage.before.verso"/>
  <!--
  =====================================================================
  == Preface Title Page Controller
  =====================================================================
  -->
  <xsl:template name="preface.titlepage">
    <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <xsl:variable name="recto.content">
        <xsl:call-template name="preface.titlepage.before.recto"/>
        <xsl:call-template name="preface.titlepage.recto"/>
      </xsl:variable>
      <xsl:variable name="recto.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($recto.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($recto.content) != '') or ($recto.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$recto.content"/>
        </fo:block>
      </xsl:if>
      <xsl:variable name="verso.content">
        <xsl:call-template name="preface.titlepage.before.verso"/>
        <xsl:call-template name="preface.titlepage.verso"/>
      </xsl:variable>
      <xsl:variable name="verso.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($verso.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($verso.content) != '') or ($verso.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$verso.content"/>
        </fo:block>
      </xsl:if>
      <xsl:call-template name="preface.titlepage.separator"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="*" mode="preface.titlepage.recto.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="*" mode="preface.titlepage.verso.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="subtitle" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style" font-family="{$title.fontset}">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="corpauthor" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="authorgroup" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="author" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="othercredit" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="releaseinfo" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="copyright" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="legalnotice" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="pubdate" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="revision" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="revhistory" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="abstract" mode="preface.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="preface.titlepage.recto.style">
      <xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <!--
  =====================================================================
  == Recto (Right) Chapter Page
  =====================================================================
  -->
  <xsl:template name="chapter.titlepage.recto">
    <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="title"/>
  </xsl:template>
  <!--
  =====================================================================
  == Verso (Left) Chapter Page
  =====================================================================
  -->
  <xsl:template name="chapter.titlepage.verso"/>
  <xsl:template name="chapter.titlepage.separator"/>
  <xsl:template name="chapter.titlepage.before.recto"/>
  <xsl:template name="chapter.titlepage.before.verso"/>


  <xsl:template name="chapter.titlepage">
    <fo:block font-family="{$title.fontset}">
      <xsl:variable name="recto.content">
        <xsl:call-template name="chapter.titlepage.before.recto"/>
        <xsl:call-template name="chapter.titlepage.recto"/>
      </xsl:variable>
      <xsl:variable name="recto.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($recto.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($recto.content) != '') or ($recto.elements.count &gt; 0)">
        <fo:block margin-left="{$title.margin.left}">
          <xsl:copy-of select="$recto.content"/>
        </fo:block>
      </xsl:if>
      <xsl:variable name="verso.content">
        <xsl:call-template name="chapter.titlepage.before.verso"/>
        <xsl:call-template name="chapter.titlepage.verso"/>
      </xsl:variable>
      <xsl:variable name="verso.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($verso.content)/*)"/>
          </xsl:when>
         <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($verso.content) != '') or ($verso.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$verso.content"/>
        </fo:block>
      </xsl:if>
      <xsl:call-template name="chapter.titlepage.separator"/>
    </fo:block>
  </xsl:template>
  <!--
  =====================================================================
  == Recto (Right) Chapter Title Generator
  =====================================================================
  -->
  <xsl:template match="*" mode="chapter.titlepage.recto.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="*" mode="chapter.titlepage.verso.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>

  <xsl:template match="title" mode="chapter.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="chapter.titlepage.recto.style">
      <xsl:call-template name="component.title">
        <xsl:with-param name="node" select="ancestor-or-self::chapter[1]"/>
      </xsl:call-template>
    </fo:block>
  </xsl:template>

  <xsl:template name="section.titlepage.recto">
    <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="title"/>
  </xsl:template>

  <xsl:template name="section.titlepage.verso"/>
  <xsl:template name="section.titlepage.separator"/>
  <xsl:template name="section.titlepage.before.recto"/>
  <xsl:template name="section.titlepage.before.verso"/>

  <xsl:template name="section.titlepage">
    <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <xsl:variable name="recto.content">
        <xsl:call-template name="section.titlepage.before.recto"/>
        <xsl:call-template name="section.titlepage.recto"/>
      </xsl:variable>
      <xsl:variable name="recto.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($recto.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($recto.content) != '') or ($recto.elements.count &gt; 0)">
        <fo:block keep-with-next.within-page="always">
          <xsl:copy-of select="$recto.content"/>
        </fo:block>
      </xsl:if>
      <xsl:variable name="verso.content">
        <xsl:call-template name="section.titlepage.before.verso"/>
        <xsl:call-template name="section.titlepage.verso"/>
      </xsl:variable>
      <xsl:variable name="verso.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($verso.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($verso.content) != '') or ($verso.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$verso.content"/>
        </fo:block>
      </xsl:if>
      <xsl:call-template name="section.titlepage.separator"/>
    </fo:block>
  </xsl:template>

  <xsl:template match="*" mode="section.titlepage.recto.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="*" mode="section.titlepage.verso.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="title" mode="section.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="section.titlepage.recto.style" margin-left="{$title.margin.left}">
     <xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>

  <xsl:template name="appendix.titlepage.recto">
    <xsl:choose>
      <xsl:when test="appendixinfo/title">
        <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/title"/>
      </xsl:when>
      <xsl:when test="docinfo/title">
        <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/title"/>
      </xsl:when>
      <xsl:when test="info/title">
        <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/title"/>
      </xsl:when>
      <xsl:when test="title">
        <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="title"/>
      </xsl:when>
    </xsl:choose>
    <xsl:choose>
      <xsl:when test="appendixinfo/subtitle">
        <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/subtitle"/>
      </xsl:when>
      <xsl:when test="docinfo/subtitle">
        <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/subtitle"/>
      </xsl:when>
      <xsl:when test="info/subtitle">
        <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/subtitle"/>
      </xsl:when>
      <xsl:when test="subtitle">
        <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="subtitle"/>
      </xsl:when>
    </xsl:choose>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/corpauthor"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/corpauthor"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/corpauthor"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/authorgroup"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/authorgroup"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/authorgroup"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/author"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/author"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/author"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/othercredit"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/othercredit"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/othercredit"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/releaseinfo"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/releaseinfo"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/releaseinfo"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/copyright"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/copyright"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/copyright"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/legalnotice"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/legalnotice"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/legalnotice"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/pubdate"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/pubdate"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/pubdate"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/revision"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/revision"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/revision"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/revhistory"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/revhistory"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/revhistory"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/abstract"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="docinfo/abstract"/>
    <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="info/abstract"/>
  </xsl:template>

  <xsl:template name="appendix.titlepage.verso"/>
  <xsl:template name="appendix.titlepage.separator"/>
  <xsl:template name="appendix.titlepage.before.recto"/>
  <xsl:template name="appendix.titlepage.before.verso"/>

  <xsl:template name="appendix.titlepage">
    <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <xsl:variable name="recto.content">
        <xsl:call-template name="appendix.titlepage.before.recto"/>
        <xsl:call-template name="appendix.titlepage.recto"/>
      </xsl:variable>
      <xsl:variable name="recto.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
             <xsl:value-of select="count(exsl:node-set($recto.content)/*)"/>
           </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($recto.content) != '') or ($recto.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$recto.content"/>
        </fo:block>
      </xsl:if>
      <xsl:variable name="verso.content">
        <xsl:call-template name="appendix.titlepage.before.verso"/>
        <xsl:call-template name="appendix.titlepage.verso"/>
      </xsl:variable>
      <xsl:variable name="verso.elements.count">
        <xsl:choose>
          <xsl:when test="function-available('exsl:node-set')">
            <xsl:value-of select="count(exsl:node-set($verso.content)/*)"/>
          </xsl:when>
          <xsl:otherwise>1</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="(normalize-space($verso.content) != '') or ($verso.elements.count &gt; 0)">
        <fo:block>
          <xsl:copy-of select="$verso.content"/>
        </fo:block>
      </xsl:if>
      <xsl:call-template name="appendix.titlepage.separator"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="*" mode="appendix.titlepage.recto.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="*" mode="appendix.titlepage.verso.mode">
    <!-- if an element isn't found in this mode, try the generic titlepage.mode -->
    <xsl:apply-templates select="." mode="titlepage.mode"/>
  </xsl:template>
  <xsl:template match="title" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style" margin-left="{$title.margin.left}" font-size="24pt" font-weight="bold" font-family="{$title.fontset}" color="{$minor.title.color}">
      <xsl:call-template name="component.title">
         <xsl:with-param name="node" select="ancestor-or-self::appendix[1]"/>
      </xsl:call-template>
    </fo:block>
  </xsl:template>
  <xsl:template match="subtitle" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style" font-family="{$title.fontset}">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="corpauthor" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="authorgroup" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="author" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="othercredit" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="releaseinfo" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="copyright" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
     <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="legalnotice" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="pubdate" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="revision" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="revhistory" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
  <xsl:template match="abstract" mode="appendix.titlepage.recto.auto.mode">
    <fo:block xsl:use-attribute-sets="appendix.titlepage.recto.style">
      <xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
    </fo:block>
  </xsl:template>
</xsl:stylesheet>