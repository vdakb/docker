<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" version='1.0'>
  <!-- ********************************************************************
     $Id: toc.xsl 9822 2013-10-19 00:45:49Z stilor $
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://docbook.sf.net/release/xsl/current/ for
     copyright and other information.

     ******************************************************************** -->
  <!-- ==================================================================== -->
  <!-- only set, book and part puts toc in its own page sequence -->
  <xsl:template match="set/toc | book/toc | part/toc">
    <xsl:variable name="toc.params">
      <xsl:call-template name="find.path.params">
        <xsl:with-param name="node" select="parent::*"/>
        <xsl:with-param name="table" select="normalize-space($generate.toc)"/>
      </xsl:call-template>
    </xsl:variable>
    <!-- Do not output the toc element if one is already generated
       by the use of $generate.toc parameter, or if
       generating a source toc is turned off -->
    <xsl:if test="not(contains($toc.params, 'toc')) and
                ($process.source.toc != 0 or $process.empty.source.toc != 0)">
      <!-- Don't generate a page sequence unless there is content -->
      <xsl:variable name="content">
        <xsl:choose>
          <xsl:when test="* and $process.source.toc != 0">
            <xsl:apply-templates/>
          </xsl:when>
          <xsl:when test="count(*) = 0 and $process.empty.source.toc != 0">
            <!-- trick to switch context node to parent element -->
            <xsl:for-each select="parent::*">
              <xsl:choose>
                <xsl:when test="self::set">
                  <xsl:call-template name="set.toc">
                    <xsl:with-param name="toc.title.p" select="contains($toc.params, 'title')"/>
                  </xsl:call-template>
                </xsl:when>
                <xsl:when test="self::book">
                  <xsl:call-template name="division.toc">
                    <xsl:with-param name="toc.title.p" select="contains($toc.params, 'title')"/>
                  </xsl:call-template>
                </xsl:when>
                <xsl:when test="self::part">
                  <xsl:call-template name="division.toc">
                    <xsl:with-param name="toc.title.p" select="contains($toc.params, 'title')"/>
                  </xsl:call-template>
                </xsl:when>
              </xsl:choose>
            </xsl:for-each>
          </xsl:when>
        </xsl:choose>
      </xsl:variable>
      <xsl:if test="string-length(normalize-space($content)) != 0">
        <xsl:variable name="lot-master-reference">
          <xsl:call-template name="select.pagemaster">
            <xsl:with-param name="pageclass" select="'lot'"/>
          </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="page.sequence">
          <xsl:with-param name="master-reference" select="$lot-master-reference"/>
          <xsl:with-param name="element" select="'toc'"/>
          <xsl:with-param name="gentext-key" select="'TableofContents'"/>
          <xsl:with-param name="content" select="$content"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  <xsl:template match="chapter/toc | appendix/toc | preface/toc | article/toc">
    <xsl:variable name="toc.params">
      <xsl:call-template name="find.path.params">
        <xsl:with-param name="node" select="parent::*"/>
        <xsl:with-param name="table" select="normalize-space($generate.toc)"/>
      </xsl:call-template>
    </xsl:variable>
    <!-- Do not output the toc element if one is already generated
       by the use of $generate.toc parameter, or if
       generating a source toc is turned off -->
    <xsl:if test="not(contains($toc.params, 'toc')) and
                ($process.source.toc != 0 or $process.empty.source.toc != 0)">
      <xsl:choose>
        <xsl:when test="* and $process.source.toc != 0">
          <fo:block>
            <xsl:apply-templates/>
          </fo:block>
        </xsl:when>
        <xsl:when test="count(*) = 0 and $process.empty.source.toc != 0">
          <!-- trick to switch context node to section element -->
          <xsl:for-each select="parent::*">
            <xsl:call-template name="component.toc">
              <xsl:with-param name="toc.title.p" select="contains($toc.params, 'title')"/>
            </xsl:call-template>
          </xsl:for-each>
        </xsl:when>
      </xsl:choose>
      <xsl:call-template name="component.toc.separator"/>
    </xsl:if>
  </xsl:template>
  <xsl:template match="section/toc
                    |sect1/toc
                    |sect2/toc
                    |sect3/toc
                    |sect4/toc
                    |sect5/toc">
    <xsl:variable name="toc.params">
      <xsl:call-template name="find.path.params">
        <xsl:with-param name="node" select="parent::*"/>
        <xsl:with-param name="table" select="normalize-space($generate.toc)"/>
      </xsl:call-template>
    </xsl:variable>
    <!-- Do not output the toc element if one is already generated
       by the use of $generate.toc parameter, or if
       generating a source toc is turned off -->
    <xsl:if test="not(contains($toc.params, 'toc')) and
                ($process.source.toc != 0 or $process.empty.source.toc != 0)">
      <xsl:choose>
        <xsl:when test="* and $process.source.toc != 0">
          <fo:block>
            <xsl:apply-templates/>
          </fo:block>
        </xsl:when>
        <xsl:when test="count(*) = 0 and $process.empty.source.toc != 0">
          <!-- trick to switch context node to section element -->
          <xsl:for-each select="parent::*">
            <xsl:call-template name="section.toc">
              <xsl:with-param name="toc.title.p" select="contains($toc.params, 'title')"/>
            </xsl:call-template>
          </xsl:for-each>
        </xsl:when>
      </xsl:choose>
      <xsl:call-template name="section.toc.separator"/>
    </xsl:if>
  </xsl:template>
  <!-- ==================================================================== -->
  <xsl:template match="tocpart|tocchap
                     |toclevel1|toclevel2|toclevel3|toclevel4|toclevel5">
    <xsl:apply-templates select="tocentry"/>
    <xsl:if test="tocchap|toclevel1|toclevel2|toclevel3|toclevel4|toclevel5">
      <fo:block start-indent="{count(ancestor::*)*2}pc">
        <xsl:apply-templates select="tocchap|toclevel1|toclevel2|toclevel3|toclevel4|toclevel5"/>
      </fo:block>
    </xsl:if>
  </xsl:template>
  <xsl:template match="tocentry|lotentry|tocdiv|tocfront|tocback">
    <fo:block end-indent="2pc" last-line-end-indent="-2pc">
      <xsl:if test="@linkend or @pagenum">
        <xsl:attribute name="text-align-last">justify</xsl:attribute>
      </xsl:if>
      <fo:inline keep-with-next.within-line="always">
        <xsl:choose>
          <xsl:when test="@linkend">
            <fo:basic-link internal-destination="{@linkend}">
              <xsl:apply-templates/>
            </fo:basic-link>
          </xsl:when>
          <xsl:otherwise>
            <xsl:apply-templates/>
          </xsl:otherwise>
        </xsl:choose>
      </fo:inline>
      <xsl:choose>
        <xsl:when test="@linkend">
          <fo:inline keep-together.within-line="always">
            <fo:leader xsl:use-attribute-sets="toc.leader.properties"/>
            <fo:basic-link internal-destination="{@linkend}">
              <xsl:choose>
                <xsl:when test="@pagenum">
                  <xsl:value-of select="@pagenum"/>
                </xsl:when>
                <xsl:otherwise>
                  <fo:page-number-citation ref-id="{@linkend}"/>
                </xsl:otherwise>
              </xsl:choose>
            </fo:basic-link>
          </fo:inline>
        </xsl:when>
        <xsl:when test="@pagenum">
          <fo:inline keep-together.within-line="always">
            <fo:leader xsl:use-attribute-sets="toc.leader.properties"/>
            <xsl:value-of select="@pagenum"/>
          </fo:inline>
        </xsl:when>
      </xsl:choose>
    </fo:block>
  </xsl:template>
  <xsl:template match="toc/title | tocdiv/title">
    <fo:block font-weight="bold">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template match="toc/subtitle | tocdiv/subtitle">
    <fo:block font-weight="bold">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template match="toc/titleabbrev |tocdiv/titleabbrev">
</xsl:template>
  <!-- ==================================================================== -->
  <!-- A lot element must have content, because there is no attribute
     to select what kind of list should be generated -->
  <xsl:template match="book/lot | part/lot">
    <!-- Don't generate a page sequence unless there is content -->
    <xsl:variable name="content">
      <xsl:choose>
        <xsl:when test="* and $process.source.toc != 0">
          <xsl:apply-templates/>
        </xsl:when>
        <xsl:when test="not(child::*) and $process.empty.source.toc != 0">
          <xsl:call-template name="process.empty.lot"/>
        </xsl:when>
      </xsl:choose>
    </xsl:variable>
    <xsl:if test="string-length(normalize-space($content)) != 0">
      <xsl:variable name="lot-master-reference">
        <xsl:call-template name="select.pagemaster">
          <xsl:with-param name="pageclass" select="'lot'"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:call-template name="page.sequence">
        <xsl:with-param name="master-reference" select="$lot-master-reference"/>
        <xsl:with-param name="element" select="'toc'"/>
        <xsl:with-param name="content" select="$content"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  <xsl:template match="chapter/lot | appendix/lot | preface/lot | article/lot">
    <xsl:choose>
      <xsl:when test="* and $process.source.toc != 0">
        <fo:block>
          <xsl:apply-templates/>
        </fo:block>
        <xsl:call-template name="component.toc.separator"/>
      </xsl:when>
      <xsl:when test="not(child::*) and $process.empty.source.toc != 0">
        <xsl:call-template name="process.empty.lot"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="section/lot
                    |sect1/lot
                    |sect2/lot
                    |sect3/lot
                    |sect4/lot
                    |sect5/lot">
    <xsl:choose>
      <xsl:when test="* and $process.source.toc != 0">
        <fo:block>
          <xsl:apply-templates/>
        </fo:block>
        <xsl:call-template name="section.toc.separator"/>
      </xsl:when>
      <xsl:when test="not(child::*) and $process.empty.source.toc != 0">
        <xsl:call-template name="process.empty.lot"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="process.empty.lot">
    <!-- An empty lot element does not provide any information to indicate
       what should be included in it.  You can customize this
       template to generate a lot based on @role or something -->
    <xsl:message>
      <xsl:text>Warning: don't know what to generate for </xsl:text>
      <xsl:text>lot that has no children.</xsl:text>
    </xsl:message>
  </xsl:template>
  <xsl:template match="lot/title">
    <fo:block font-weight="bold">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template match="lot/subtitle">
    <fo:block font-weight="bold">
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>
  <xsl:template match="lot/titleabbrev">
</xsl:template>
</xsl:stylesheet>
