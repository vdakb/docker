<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--
  This software is the confidential and proprietary information of
  Oracle Corporation. ("Confidential Information").  You shall not
  disclose such Confidential Information and shall use it only in
  accordance with the terms of the license agreement you entered
  into with Oracle.

  ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
  SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
  IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
  PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
  SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
  THIS SOFTWARE OR ITS DERIVATIVES.

  Copyright (c) 2011. All Rights reserved
-->
<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Enumeration"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="bka.iam.access.bundle.Resource"/>
<c:set var="requestIp" value="${applicationScope.storage.requestIP(pageContext.request)}"/>
<c:set var="symbol"    value="${applicationScope.storage.symbol(requestIp)}"/>
<html dir="ltr" lang="${pageContext.request.locale}">
  <head>
    <meta http-equiv="expires"         content="0">
    <meta http-equiv="pragma"          content="no-cache">
    <meta http-equiv="cache-control"   content="no-cache">
    <meta http-equiv="description"     content="Page to signin">
    <meta http-equiv="content-type"    content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="robots"                content="noindex, nofollow">
    <meta name="viewport"              content="width=device-width, initial-scale=1.0">
    <!-- ===============================================-->
    <!-- Document Title                                 -->
    <!-- ===============================================-->
    <title><fmt:message key="signout.title"/></title>
    <!-- ===============================================-->
    <!-- Favicons                                       -->
    <!-- ===============================================-->
    <link type="image/ico" rel="shortcut icon" href="ico/favicon.png"/>
    <!-- ===============================================-->
    <!-- Stylesheets                                    -->
    <!-- ===============================================-->
    <link type="text/css"  rel="stylesheet"    href="css/bootstrap.min.css">
    <link type="text/css"  rel="stylesheet"    href="css/mdb.min.css">
    <link type="text/css"  rel="stylesheet"    href="css/main.min.css">
    <style id="clickJack">body { display: none !important; }</style>
    <script type="text/javascript">
    // disable frame hijacking
    if (self === top) {
      var clickJack = document.getElementById("clickJack");
      clickJack.parentNode.removeChild(clickJack);
    }
    else {
      top.location = self.location;
    }
    </script>
  </head>
  <body class="background-gray">
    <div id="header-bg" class="navbar navbar-light navbar-expand-md navbar-fixed-top" role="navigation">
      <img class="navbar-brand" src="img/branding.png"/>
      <img class="ml-auto"      src="img/provider.png"/>
    </div>
    <div id="header-bg-top">
      <div id="header-bg-tagline">
        <h3><fmt:message key="portal.title"/></h3>
      </div>
    </div>
    <%
      String imgBeg="<img border=\"0\" width=\"0\" height=\"0\" src=\"";
      String imgEnd="\"/>";
      Enumeration e = request.getHeaders("OAM_LOGOUT_CALLBACK_URLS");
      while (e.hasMoreElements()) {
        String headerVal = (String)e.nextElement();
        out.write(imgBeg + headerVal + imgEnd);
      }
    %>
    <div class="container margin-top">
      <div class="form-canvas">
        <div class="col-md-auto col-sm-auto">
          <div class="d-flex justify-content-between">
            <h3 class="mt-auto text-dark"><fmt:message key="signout.title"/></h3>
            <img class="align-baseline image-avatar" src="${symbol}" alt="${requestIp}" data-toggle="tooltip" data-placement="top" title="${requestIp}"/>
          </div>
          <hr class="hr-dark mb-3 mt-4"/>
          <div class="md-form">
            <p><fmt:message key="signout.instruction"/></p>
          </div>
          <div class="d-flex justify-content-end">
            <a href="/ecc" class="btn btn-info" role="button"><fmt:message key="signin.action"/></a>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>