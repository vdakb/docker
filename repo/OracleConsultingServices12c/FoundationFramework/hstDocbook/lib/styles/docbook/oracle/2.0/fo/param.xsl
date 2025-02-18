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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <!--
  #####################################################################
  ## Introtuced Stylesheet Parameter
  #####################################################################
  -->
  <!--
  =====================================================================
  == Specifies level header font size ratio
  ==
  == The size ratio should be a number between 1.1 and 1.7 and is
  == pre calculated on level 5 in the form of:
  == level(n) = level(n - 1) * ${titel.font.ratio}
  =====================================================================
  -->
  <xsl:param name="titel.font.ratio">1.1</xsl:param>
  <xsl:param name="chapter.font.ratio">1.4641</xsl:param>  
  <xsl:param name="level1.font.ratio">1.3310</xsl:param>  
  <xsl:param name="level2.font.ratio">1.2100</xsl:param>  
  <xsl:param name="level3.font.ratio">1.1000</xsl:param>  
  <xsl:param name="level4.font.ratio">1.0000</xsl:param>  
  <xsl:param name="level5.font.ratio">0.9090</xsl:param>  
  <xsl:param name="level6.font.ratio">0.8264</xsl:param>  
  <!--
  =====================================================================
  == This parameter provides the means of indenting the text block
  == relative to section titles.
  ==
  == For left-to-right text direction, it indents the left side. For
  == right-to-left text direction, it indents the right side.
  =====================================================================
  -->
  <xsl:param name="block.start.indent">
    <xsl:choose>
      <xsl:when test="$fop.extensions != 0">44.4mm</xsl:when>
      <xsl:when test="$passivetex.extensions != 0">44.4mm</xsl:when>
      <xsl:otherwise>44.4mm</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  <!--
  #####################################################################
  ## Overridden Stylesheet Parameter
  #####################################################################
  -->
  <!--
  =====================================================================
  == Specifies the default point size for body text
  ==
  == The body font size is specified in two parameters (body.font.master
  == and body.font.size) so that math can be performed on the font size
  == by XSLT.
  =====================================================================
  -->
  <xsl:param name="body.font.master">10</xsl:param>
  <!--
  =====================================================================
  == The body font family is the default font used for text in the page
  == body.
  ==
  == If more than one font is required, enter the font names, separated
  == by a comma.
  =====================================================================
  -->
  <xsl:param name="body.font.family"     select="'Helvetica'"/>
  <!--
  =====================================================================
  == The title font family is used for titles (chapter, section,
  == figure, etc.)
  ==
  == If more than one font is required, enter the font names, separated
  == by a comma.
  =====================================================================
  -->
  <xsl:param name="title.font.family"    select="'Helvetica'"/>
  <!--
  =====================================================================
  == Enable extensions for FOP version 0.90 and later
  =====================================================================
  -->
  <xsl:param name="fop1.extensions"       select="1"/>
  <!--
  =====================================================================
  == The paper type is a convenient way to specify the paper size.
  ==
  == Default: USLetter
  =====================================================================
  -->
  <xsl:param name="paper.type"            select="'A4'"/>
  <!--
  =====================================================================
  == This parameter is useful when printing a document on both sides of
  == the paper.
  =====================================================================
  -->
  <xsl:param name="double.sided"          select="0"/>
  <!--
  =====================================================================
  == The inner page margin is the distance from bound edge of the page
  == to the first column of text.
  ==
  == Default: 1in
  =====================================================================
  -->
  <xsl:param name="page.margin.inner">
    <xsl:choose>
      <xsl:when test="$double.sided != 0">25.4mm</xsl:when>
      <xsl:otherwise>19.8mm</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  <!--
  =====================================================================
  == The outer page margin is the distance from non-bound edge of the
  == page to the outer edge of the last column of text.
  ==
  == Default: 1in
  =====================================================================
  -->
  <xsl:param name="page.margin.outer">
    <xsl:choose>
      <xsl:when test="$double.sided != 0">12.7mm</xsl:when>
      <xsl:otherwise>12.7mm</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  <!--
  =====================================================================
  == The top page margin is the distance from the physical top of the
  == page to the top of the region-before.
  ==
  == Default: 0.5in
  =====================================================================
  -->
  <xsl:param name="page.margin.top"       select="'10mm'"/>
  <!--
  =====================================================================
  == The bottom page margin is the distance from the bottom of the
  == region-after to the physical bottom of the page.
  ==
  == Default: 0.5in
  =====================================================================
  -->
  <xsl:param name="page.margin.bottom"    select="'10mm'"/>
  <!--
  =====================================================================
  == The region before extent is the height of the area where headers
  == are printed.
  ==
  == Default: 0.4in
  =====================================================================
  -->
  <xsl:param name="region.before.extent"  select="'12.7mm'"/>
  <!--
  =====================================================================
  == The region after extent is the height of the area where footers
  == are printed.
  ==
  == Default: 0.4in
  =====================================================================
  -->
  <xsl:param name="region.after.extent"  select="'12.7mm'"/>
  <!--
  =====================================================================
  == The body top margin is the distance from the top of the
  == region-before to the first line of text in the page body.
  ==
  == Default: 0.5in
  =====================================================================
  -->
  <xsl:param name="body.margin.top"       select="'12.7mm'"/>
  <!--
  =====================================================================
  == The body bottom margin is the distance from the last line of text
  == in the page body to the bottom of the region-after.
  ==
  == Default: 0.5in
  =====================================================================
  -->
  <xsl:param name="body.margin.bottom"    select="'25.4mm'"/>
  <!--
  =====================================================================
  == This parameter provides the means of indenting the body text
  == relative to section titles.
  ==
  == For left-to-right text direction, it indents the left side. For
  == right-to-left text direction, it indents the right side. It is
  == used in place of the title.margin.left for all XSL-FO processors
  == except FOP 0.25. It enables support for side floats to appear in
  == the indented margin area.
  ==
  == If this parameter is used, section titles should have a
  == start-indent value of 0pt if they are to be outdented relative to
  == the body text.
  =====================================================================
  -->
  <xsl:param name="body.start.indent">
    <xsl:choose>
      <xsl:when test="$fop.extensions != 0">0pt</xsl:when>
      <xsl:when test="$passivetex.extensions != 0">0pt</xsl:when>
      <xsl:otherwise>44.4mm</xsl:otherwise>
    </xsl:choose>
  </xsl:param>
  <!--
  =====================================================================
  == The URI of the image to be used for draft watermarks
  == This connects to that URL everytime it is mentioned in the draft
  == pagemasters so we change it
  ==
  == Default: http://docbook.sourceforge.net/release/images/draft.png
  =====================================================================
  -->
  <xsl:param name="draft.watermark.image" select="''"/>
  <!--
  =====================================================================
  == Let's get rid of that draft pagemaster FO bloat anyhow
  ==
  == Default: maybe
  =====================================================================
  -->
  <xsl:param name="draft.mode"            select="'no'"/>
  <!--
  =====================================================================
  == Without this, we get incorrect image file paths in the .fo,
  == resulting in missing images in the PDF if the source document is
  == in a subdir and xi:included in the docset
  ==
  == Default: 0
  =====================================================================
  -->
  <xsl:param name="keep.relative.image.uris" select="1"/>
  <!--
  =====================================================================
  == By default, stylesheets use the generate.toc parameter to
  == determine which elements have a TOC generated at the beginning of
  == the element in the output.
  == For print output or non-chunked HTML output, a single TOC at the
  == beginning may suffice.
  == But when you are generating chunked HTML files, you may want
  == certain sublevels to provide TOCs to help orient the reader.
  ==
  == Default: ??=
  =====================================================================
  -->
  <xsl:param name="generate.toc">
    appendix  nop
    article/appendix  nop
    article   toc,title
    book      toc,title
    chapter   nop
    part      nop
    preface   nop
    qandadiv  nop
    qandaset  nop
    reference nop
    sect1     nop
    sect2     nop
    sect3     nop
    sect4     nop
    sect5     nop
    section   nop
    set       toc,title
  </xsl:param>
  <!--
  =====================================================================
  == Display URLs after ulinks?
  ==
  == If non-zero, the URL of each ulink will appear after the text of
  == the link. If the text of the link and the URL are identical, the
  == URL is suppressed.
  ==
  == Default: 1
  =====================================================================
  -->
  <xsl:param name="ulink.show"               select="0"/>
  <!--
  =====================================================================
  == Specify the spacing required between normal paragraphs
  ==
  == Default: space-before.optimum 1em
  ==          space-before.minimum 0.8em
  ==          space-before.maximum 1.2em
  =====================================================================
  -->
  <xsl:attribute-set name="normal.para.spacing">
    <xsl:attribute name="space-before.optimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.minimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">6pt</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == By default, when you process an admonition element such as note,
  == the output displays the label Note in the appropriate language,
  == followed by the text of the note.
  == You can add a distinctive admonition graphic before the label by
  == setting the admon.graphics parameter to non-zero.
  ==
  == Default: 0
  =====================================================================
  -->
  <xsl:param name="admon.graphics"           select="1"/>
  <!--
  =====================================================================
  == Given an admonition node, returns the width of the graphic that
  == will be used for that admonition.
  ==
  == Default: 36pt
  =====================================================================
  -->
  <xsl:param name="admon.graphic.width">16pt</xsl:param>
  <!--
  =====================================================================
  == Sets the filename extension to use on admonition graphics.
  ==
  == Default: .png
  =====================================================================
  -->
  <xsl:param name="admon.graphics.extension">.png</xsl:param>
  <!--
  =====================================================================
  == Sets the path to the directory containing the admonition graphics
  == (caution.png, important.png etc).
  == This location is normally relative to the output html directory.
  ==
  == Default: images/
  =====================================================================
  -->
  <xsl:param name="admon.graphics.path">file:///Users/dsteding/Project/OracleConsultingServices12c/FoundationFramework/hstDocbook/lib/styles/docbook/oracle/ico/</xsl:param>
  <!--
  =====================================================================
  == Sets the style for admonitions titles.
  =====================================================================
  -->
  <xsl:attribute-set name="admonition.title.properties">
    <xsl:attribute name="font-size">12pt</xsl:attribute>
    <xsl:attribute name="hyphenate">false</xsl:attribute>
    <xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == These properties are added to the outer block containing the
  == entire graphical admonition, including its title.
  == It is used when the parameter admon.graphics is set to nonzero.
  == Use this attribute-set to set the space above and below, and any
  == indent for the whole admonition.
  =====================================================================
  -->
  <xsl:attribute-set name="graphical.admonition.properties">
    <xsl:attribute name="space-before.optimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.minimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">6pt</xsl:attribute>
    <xsl:attribute name="margin-{$direction.align.start}">0pt</xsl:attribute>
    <xsl:attribute name="margin-{$direction.align.end}">0pt</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == These properties are added to the outer block containing the
  == entire nongraphical admonition, including its title.
  == It is used when the parameter admon.graphics is set to zero.
  == Use this attribute-set to set the space above and below, and any
  == indent for the whole admonition.
  =====================================================================
  -->
  <xsl:attribute-set name="nongraphical.admonition.properties">
    <xsl:attribute name="space-before.minimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.optimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">6pt</xsl:attribute>
    <xsl:attribute name="margin-{$direction.align.start}">0pt</xsl:attribute>
    <xsl:attribute name="margin-{$direction.align.end}">0pt</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Sets the style for Section Level.
  =====================================================================
  -->
  <xsl:attribute-set name="section.title.properties">
    <xsl:attribute name="color">
      <xsl:value-of select="$minor.title.color"/>
    </xsl:attribute>
    <xsl:attribute name="start-indent">
      <xsl:value-of select="$body.start.indent"/>
    </xsl:attribute>
    <xsl:attribute name="font-family">
      <xsl:value-of select="$title.fontset"/>
    </xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
    <xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
    <xsl:attribute name="hyphenate">false</xsl:attribute>
    <xsl:attribute name="text-align">start</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Sets the style for Section Level 1 (Word Template Header 3).
  =====================================================================
  -->
  <xsl:attribute-set name="section.title.level1.properties">
    <xsl:attribute name="start-indent">
      <xsl:value-of select="0"/>
    </xsl:attribute>
    <!--
     | dsteding:
     | font size is calculated dynamically by section.heading template
    -->
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * $level1.font.ratio"/>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="space-before.minimum">6pt</xsl:attribute>
    <xsl:attribute name="space-before.optimum">6pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">9pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">9pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">9pt</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Sets the style for Section Level 2 (Word Template Header 4).
  =====================================================================
  -->
  <xsl:attribute-set name="section.title.level2.properties">
    <!--
     | dsteding:
     | font size is calculated dynamically by section.heading template
    -->
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * $level2.font.ratio"/>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="space-before.minimum">12pt</xsl:attribute>
    <xsl:attribute name="space-before.optimum">12pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">12pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">9pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">9pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">9pt</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Sets the style for Section Level 3 (Word Template Header 5).
  =====================================================================
  -->
  <xsl:attribute-set name="section.title.level3.properties">
    <!--
     | dsteding:
     | font size is calculated dynamically by section.heading template
    -->
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * $level3.font.ratio"/>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="font-style">italic</xsl:attribute>
    <xsl:attribute name="space-before.minimum">12pt</xsl:attribute>
    <xsl:attribute name="space-before.optimum">12pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">12pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">9pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">9pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">9pt</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Sets the style for Section Level 4 (Word Template Header 6).
  =====================================================================
  -->
  <xsl:attribute-set name="section.title.level4.properties">
    <!--
     | dsteding:
     | font size is calculated dynamically by section.heading template
    -->
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * $level4.font.ratio"/>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="font-weight">normal</xsl:attribute>
    <xsl:attribute name="font-style">italic</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Sets the style for Section Level 5
  =====================================================================
  -->
  <xsl:attribute-set name="section.title.level5.properties">
    <!--
     | dsteding:
     | font size is calculated dynamically by section.heading template
    -->
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * $level5.font.ratio"/>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Sets the style for Section Level 6
  =====================================================================
  -->
  <xsl:attribute-set name="section.title.level6.properties">
    <!--
     | dsteding:
     | font size is calculated dynamically by section.heading template
    -->
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * $level6.font.ratio"/>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == This attribute set is used to set properties on cross reference
  == text.
  =====================================================================
  -->
  <xsl:attribute-set name="xref.properties">
    <xsl:attribute name="color">
      <xsl:value-of select="$hyperlink.color"/>
    </xsl:attribute>
    <xsl:attribute name="text-decoration">underline</xsl:attribute>
  </xsl:attribute-set>
  <!--
  #####################################################################
  ## Stylesheet Parameter introduced by us
  #####################################################################
  -->
  <!--
  =====================================================================
  == Default params for special word-breaking (e.g. in urls, filenames)
  ==
  == &#x200B; (zero-width space) WORKS - (and no more "stretching" as
  ==          of FOP 0.93)
  == &#x00AD; (soft hyphen) does NOT work - FOP 0.20.5 treats it as a
  ==          normal hyphen, always displaying it!
  =====================================================================
  -->
  <xsl:param name="special-hyph.char"       select="'&#x200B;'"/>
  <xsl:param name="special-hyph.min-before" select="3"/>
  <xsl:param name="special-hyph.min-after"  select="2"/>
  <!--
  =====================================================================
  == Just in case some other (future-version?) template refers to
  == ulink.hyphenate, which is used in the original DocBook stylesheets
  =====================================================================
  -->
  <xsl:param name="ulink.hyphenate"         select="$special-hyph.char"/>
  <!--
  =====================================================================
  == Highlighting text colors
  =====================================================================
  -->
  <xsl:param name="pagetitle.color"          select="'#ff0000'"/>
  <xsl:param name="highlight.color"          select="'#0000ff'"/>
  <!--
  <xsl:param name="shadeback.color"          select="'#e0e0e0'"/>
  <xsl:param name="shadeframe.color"         select="'#000000'"/>
  -->
  <xsl:param name="shadeback.color"          select="'#f2f2f2'"/>
  <xsl:param name="shadeframe.color"         select="'#7f7f7f'"/>
  <xsl:param name="shadeback.color.screen"   select="'#fafad2'"/>
  <xsl:param name="shadeframe.color.screen"  select="'#ffd700'"/>
  <xsl:param name="hyperlink.color"          select="$highlight.color"/>
  <!--
  =====================================================================
  == Highlighted title color
  =====================================================================
  -->
  <xsl:param name="major.title.color"       select="$pagetitle.color"/>
  <xsl:param name="minor.title.color"       select="'#000000'"/>
  <!--
  =============================================================================
  == By default, the generated title is placed before the object itself.
  ==
  == You can move the title to appear after the object by altering the
  == formal.title.placement parameter.
  ==
  == Default: figure before
  ==          example before
  ==          equation before
  ==          table before
  ==          procedure before
  =====================================================================
  -->
  <xsl:param name="formal.title.placement">
    figure after
    example after
    equation after
    table after
    procedure after
  </xsl:param>
  <!--
  =====================================================================
  == Specify how the title should be styled. Specify the font size and
  == weight of the title of the formal object.
  =====================================================================
  -->
  <xsl:attribute-set name="formal.title.properties" use-attribute-sets="normal.para.spacing">
    <xsl:attribute name="text-align">center</xsl:attribute>
    <xsl:attribute name="font-weight">normal</xsl:attribute>
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * 0.5"></xsl:value-of>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="hyphenate">false</xsl:attribute>
    <xsl:attribute name="space-after.minimum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">3pt</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == The properties associated with a formal object such as a figure,
  == or other component that has a title.
  ==
  == Specify the spacing before and after the object.
  =====================================================================
  -->
  <xsl:attribute-set name="formal.object.properties">
    <xsl:attribute name="space-before.minimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.optimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">3pt</xsl:attribute>
    <xsl:attribute name="keep-together.within-column">always</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == What indention do you want for a lists on each level? 
  =====================================================================
  -->
  <xsl:param name="list.start.indent"       select="'25.4mm'"/>
  <!--
  =====================================================================
  == What spacing do you want before and after lists? 
  ==
  == Specify the spacing required before and after a list.
  == It is necessary to specify the space after a list block because
  == lists can come inside of paras.
  ==
  == This attribute set is not applied to nested lists, so that only
  == the list.item.spacing attribute-set is used, which provides
  == consistent spacing.
  =====================================================================
  -->
  <xsl:attribute-set name="list.block.spacing">
    <xsl:attribute name="space-before.optimum">0.5em</xsl:attribute>
    <xsl:attribute name="space-before.minimum">0.5em</xsl:attribute>
    <xsl:attribute name="space-before.maximum">0.5em</xsl:attribute>
    <xsl:attribute name="space-after.optimum">0.5em</xsl:attribute>
    <xsl:attribute name="space-after.minimum">0.5em</xsl:attribute>
    <xsl:attribute name="space-after.maximum">0.5em</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Specify what spacing you want before (and optionally after) each
  == list item.
  ==
  == The setting for relative-align ensures the text aligns vertically
  == with the list mark even if an inline image is in the text.
  ==
  == See also list.block.spacing, which sets the spacing before and
  == after an entire list.
  =====================================================================
  -->
  <xsl:attribute-set name="list.item.spacing">
    <xsl:attribute name="space-before.optimum">1.5em</xsl:attribute>
    <xsl:attribute name="space-before.minimum">1em</xsl:attribute>
    <xsl:attribute name="space-before.maximum">2em</xsl:attribute>
    <xsl:attribute name="space-after.optimum">1.5em</xsl:attribute>
    <xsl:attribute name="space-after.minimum">1em</xsl:attribute>
    <xsl:attribute name="space-after.maximum">2em</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="compact.list.item.spacing">
    <xsl:attribute name="space-before.optimum">0em</xsl:attribute>
    <xsl:attribute name="space-before.minimum">0em</xsl:attribute>
    <xsl:attribute name="space-before.maximum">0em</xsl:attribute>
    <xsl:attribute name="space-after.optimum">0em</xsl:attribute>
    <xsl:attribute name="space-after.minimum">0em</xsl:attribute>
    <xsl:attribute name="space-after.maximum">0em</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == OVERRIDE:
  ==
  == Not everything in bold (finetuning in titlepage-templates.xml)
  =====================================================================
  -->
  <xsl:attribute-set name="chapter.title.properties">
    <xsl:attribute name="color">
      <xsl:value-of select="$minor.title.color"/>
    </xsl:attribute>
    <xsl:attribute name="font-family">
      <xsl:value-of select="$title.fontset"/>
    </xsl:attribute>
    <!-- font size is calculated dynamically by section.heading template -->
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * $chapter.font.ratio"/>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">start</xsl:attribute>
    <xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == in default: uses verbatim.properties and monospace.properties, text-align=start
  =====================================================================
  -->
  <xsl:param name="monospace.verbatim.shade" select="1"/>
  <xsl:attribute-set name="monospace.properties.program">
    <xsl:attribute name="padding">2pt</xsl:attribute>
    <xsl:attribute name="border-left">
      <xsl:text>4px </xsl:text>
      <xsl:value-of select="$shadeframe.color"/>
      <xsl:text> solid</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="background-color">
      <xsl:value-of select="$shadeback.color"/>
    </xsl:attribute>
    <xsl:attribute name="space-before.minimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.optimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">6pt</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="monospace.style.program">
    <xsl:attribute name="margin-top">1em</xsl:attribute>
    <xsl:attribute name="margin-bottom">1em</xsl:attribute>
    <xsl:attribute name="margin-{$direction.align.start}">0.5em</xsl:attribute>
    <xsl:attribute name="margin-{$direction.align.end}">0.5em</xsl:attribute>
    <xsl:attribute name="font-family">monospace</xsl:attribute>
    <xsl:attribute name="font-size">8pt</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == in default: uses verbatim.properties and monospace.properties, text-align=start
  =====================================================================
  -->
  <xsl:attribute-set name="monospace.properties.screen">
    <xsl:attribute name="padding">0pt</xsl:attribute>
    <xsl:attribute name="border-left">
      <xsl:text>4px </xsl:text>
      <xsl:value-of select="$shadeframe.color.screen"/>
      <xsl:text> solid</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="background-color">
      <xsl:value-of select="$shadeback.color.screen"/>
    </xsl:attribute>
    <xsl:attribute name="space-before.minimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.optimum">3pt</xsl:attribute>
    <xsl:attribute name="space-before.maximum">3pt</xsl:attribute>
    <xsl:attribute name="space-after.optimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.minimum">6pt</xsl:attribute>
    <xsl:attribute name="space-after.maximum">6pt</xsl:attribute>
  </xsl:attribute-set>
  <xsl:attribute-set name="monospace.style.screen">
    <xsl:attribute name="margin-top">1em</xsl:attribute>
    <xsl:attribute name="margin-bottom">1em</xsl:attribute>
    <xsl:attribute name="margin-{$direction.align.start}">0.5em</xsl:attribute>
    <xsl:attribute name="margin-{$direction.align.end}">0.5em</xsl:attribute>
    <xsl:attribute name="font-family">monospace</xsl:attribute>
    <xsl:attribute name="font-size">8pt</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
  </xsl:attribute-set>
  <!--
  =====================================================================
  == Specifies the border style of table frames.
  =====================================================================
  -->
  <xsl:param name="table.frame.top.style"         select="'solid'"/>
  <xsl:param name="table.frame.bottom.style"      select="'solid'"/>
  <!--
  =====================================================================
  == Specifies the thickness of the frame border
  =====================================================================
  -->
  <xsl:param name="table.frame.top.thickness"     select="'3.5pt'"/>
  <xsl:param name="table.frame.bottom.thickness"  select="'1.5pt'"/>
  <!--
  =====================================================================
  == Specifies the border color of table frames.
  =====================================================================
  -->
  <xsl:param name="table.frame.top.color"         select="'black'"/>
  <xsl:param name="table.frame.bottom.color"      select="'black'"/>
  <!--
  =====================================================================
  == Specifies the thickness of the frame border
  =====================================================================
  -->
  <xsl:param name="table.header.top.thickness"    select="'0pt'"/>
  <xsl:param name="table.header.bottom.thickness" select="'1.5pt'"/>
</xsl:stylesheet>
