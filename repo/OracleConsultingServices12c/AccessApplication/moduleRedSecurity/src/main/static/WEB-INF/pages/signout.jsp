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
<%@ page session="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="oracle.iam.access.bundle.Resource"/>
<html lang="${pageContext.request.locale}">
  <head>
    <meta name="robots"              content="noindex, nofollow">
    <meta name="viewport"            content="width=device-width, initial-scale=1.0">
    <meta http-equiv="expires"       content="0">
    <meta http-equiv="pragma"        content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="description"   content="Page to signout">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <!-- ===============================================-->
    <!-- Document Title                                 -->
    <!-- ===============================================-->
    <title>RedSecurity</title>
    <!-- ===============================================-->
    <!-- Favicons                                       -->
    <!-- ===============================================-->
    <link rel="icon"             type="image/png" sizes="32x32"   href="img/redsecurity-icon-32x32.png">
    <link rel="icon"             type="image/png" sizes="16x16"   href="img/redsecurity-icon-16x16.png">
    <link rel="apple-touch-icon" type="image/png" sizes="180x180" href="img/redsecurity-icon-touch.png">
    <link rel="shortcut icon"    type="image/x-icon" href="img/redsecurity-icon.ico">
    <!-- ===============================================-->
    <!-- Custom Fonts                                   -->
    <!-- ===============================================-->
    <link rel="stylesheet" type="text/css" href="css/zondicons.min.css">
    <!-- ===============================================-->
    <!-- Stylesheets                                    -->
    <!-- ===============================================-->
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="css/mdb.min.css">
    <link type="text/css" rel="stylesheet" href="css/redsecurity.min.css">
    <!-- Start Disable frame hijacking Script-->
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
  <body>
    <div id="dw__navbar" class="navbar navbar-light navbar-fixed-top navbar-expand-md">
      <a class="navbar-brand" href="">
        <img src="img/redsecurity-logo.png" width="48" height="48" alt="RedSecurity">
        <span>RedSecurity</span>
      </a>
      <button type="button" class="navbar-toggler navbar-toggler-right" data-toggle="collapse" data-target="#global-menu"><span class="zir zi-menu"></span></button>
      <div class="collapse navbar-collapse" id="global-menu">
        <ul class="navbar-nav ml-auto">
          <li class="nav-item" href="contact.html">
            <a class="nav-link" href="#" data-toggle="modal" data-target="#contactPane">
              <i class="zir zi-envelope"></i>
              <span>Contact</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
    <div id="dw__navbar_nav" class="navbar navbar-light navbar-expand-md">
    </div>
    <div id="dokuwiki__content" >
      <div>
        <div class="container">
          <div class="row mt-5">
            <div class="col-3">
              <h1 class="h1-responsive font-weight-bold"><fmt:message key="signout.title"/></h1>
              <hr class="hr-dark">
            </div>
            <div class="col-8">
              <div class="card">
                <div class="card-body">
                  <div class="text-center">
                    <h3 class="text-dark"><i class="zir zi-user text-dark"></i> <fmt:message key="signout.instruction"/></h3>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>