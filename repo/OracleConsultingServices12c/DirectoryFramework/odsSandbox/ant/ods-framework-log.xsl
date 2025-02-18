<?xml version="1.0" encoding="US-ASCII"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fnc="http://www.w3.org/2005/xpath-functions"
                version  ="2.0">
  <xsl:output method="html" version="4.0" encoding="utf-8" indent="yes"/>
  <xsl:template match="/changes">
    <html>
      <head>
        <title>Oracle<xsl:text disable-output-escaping="yes">&amp;</xsl:text>reg; Directory Service Framework Change Log</title>
        <meta http-equiv="Content-Type"        content="text/html; charset=us-ascii"/>
        <meta http-equiv="Content-Style-Type"  content="text/css"/>
        <meta http-equiv="Content-Script-Type" content="text/javascript"/>
        <meta name="robots"                    content="noarchive"/>
        <style type="text/css">
          body {
            font-family: Tahoma, sans-serif;
            color: black;
            background-color: white;
            font-size: small;
          }
          * html body {
            /* http://www.info.com.ph/~etan/w3pantheon/style/modifiedsbmh.html */
            font-size: x-small; /* for IE5.x/win */
            font-size: small;   /* for other IE versions */
          }
          h1 {
            font-size: 165%;
            font-weight: bold;
            border-bottom: 1px solid #ddd;
            width: 100%;
          }
          h2 {
            font-size: 152%;
            font-weight: bold;
          }
          h3 {
            font-size: 139%;
            font-weight: bold;
          }
          h4 {
            font-size: 126%;
            font-weight: bold;
          }
          h5 {
            font-size: 113%;
            font-weight: bold;
            display: inline;
          }
          h6 {
            font-size: 100%;
            font-weight: bold;
            font-style: italic;
            display: inline;
          }
          a:link {
            color: #039;
            background: inherit;
          }
          a:visited {
            color: #72007C;
            background: inherit;
          }
          a:hover {
            text-decoration: underline;
          }
          a img, img[usemap] {
            border-style: none;
          }
          code, pre, samp, tt {
            font-family: monospace;
            font-size: 110%;
          }
          caption {
            text-align: center;
            font-weight: bold;
            width: auto;
          }
          dt {
            font-weight: bold;
          }
          table {
            font-size: small; /* for ICEBrowser */
          }
          td {
            vertical-align: top;
          }
          td p {
            margin-top: 0;
            margin-bottom: 0;
          }
          th {
            font-weight: bold;
            text-align: left;
            vertical-align: bottom;
          }
          ol ol {
            list-style-type: lower-alpha;
          }
          ol ol ol {
            list-style-type: lower-roman;
          }
          td p:first-child, td pre:first-child {
            margin-top: 0px;
            margin-bottom: 0px;
          }
          table.table-border {
            border-collapse: collapse;
            border-top: 1px solid #ccc;
            border-left: 1px solid #ccc;
          }
          table.table-border th {
            padding: 0.5ex 0.25em;
            color: black;
            background-color: #f7f7ea;
            border-right: 1px solid #ccc;
            border-bottom: 1px solid #ccc;
          }
          table.table-border td {
            padding: 0.5ex 0.25em;
            border-right: 1px solid #ccc;
            border-bottom: 1px solid #ccc;
          }
          span.gui-object, span.gui-object-action {
            font-weight: bold;
          }
          span.gui-object-title {
          }
          p.horizontal-rule {
            width: 100%;
            border: solid #cc9;
            border-width: 0px 0px 1px 0px;
            margin-bottom: 4ex;
          }
          div.zz-skip-header {
            display: none;
          }
          td.zz-nav-header-cell {
            text-align: left;
            font-size: 95%;
            width: 99%;
            color: black;
            background: inherit;
            font-weight: normal;
            vertical-align: top;
            margin-top: 0ex;
            padding-top: 0ex;
          }
          a.zz-nav-header-link {
            font-size: 95%;
          }
          td.zz-nav-button-cell {
            white-space: nowrap;
            text-align: center;
            width: 1%;
            vertical-align: top;
            padding-left: 4px;
            padding-right: 4px;
            margin-top: 0ex;
            padding-top: 0ex;
          }
          a.zz-nav-button-link {
            font-size: 90%;
          }
          div.zz-nav-footer-menu {
            width: 100%;
            text-align: center;
            margin-top: 2ex;
            margin-bottom: 4ex;
          }
          p.zz-legal-notice, a.zz-legal-notice-link {
            font-size: 85%;
          }
        </style>
      </head>
      <body>
        <div>
          <a id="top" name="top"/>
          <div class="zz-skip-header"><a href="#BEGIN">Skip Headers</a></div>
          <table summary="" cellspacing="0" cellpadding="0" width="100%">
          <tr>
            <td align="left" valign="top"><b>Oracle<xsl:text disable-output-escaping="yes">&amp;</xsl:text>reg; Directory Service Framework Change Log</b><br/></td>
          </tr>
          </table>
          <hr/>
          <a name="BEGIN" id="BEGIN"></a>
          <div>
            <h1>Oracle<xsl:text disable-output-escaping="yes">&amp;</xsl:text>reg; Directory Service Framework</h1>
            <p>This document describes changes on top of Release 1.0.0.0 of Oracle Directory Service Framework. It contains the following sections:</p>
            <p><xsl:text disable-output-escaping="yes">July 2014</xsl:text></p>
          </div>
        </div>
        <ol>
          <xsl:for-each select="release">
            <xsl:variable name="release"><xsl:value-of select="@id"/></xsl:variable>
            <xsl:for-each select="build">
            <h3>
              <xsl:text>Release </xsl:text><xsl:value-of select="$release"/>-<xsl:value-of select="@id"><xsl:text>text()</xsl:text></xsl:value-of>
            </h3>
            <h5>Functionality</h5>
            <div>
              <table width="700px">
              <xsl:for-each select="function">
              <tr>
                <td width="200px"><xsl:value-of select="@id"><xsl:text>text()</xsl:text></xsl:value-of></td>
                <td width="500px"><xsl:value-of select="current()"><xsl:text>text()</xsl:text></xsl:value-of></td>
              </tr>
              </xsl:for-each>
              </table>
            </div>
            <h5>Resolved Issues</h5>
            <div>
              <table width="700px">
              <xsl:for-each select="defect">
              <tr>
                <td width="200px"><xsl:value-of select="@id"><xsl:text>text()</xsl:text></xsl:value-of></td>
                <td width="500px"><xsl:value-of select="current()"><xsl:text>text()</xsl:text></xsl:value-of></td>
              </tr>
              </xsl:for-each>
              </table>
            </div>
            </xsl:for-each>
          </xsl:for-each>
        </ol>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
