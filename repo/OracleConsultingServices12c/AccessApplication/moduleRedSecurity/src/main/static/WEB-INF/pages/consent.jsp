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
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="state"  value="${pageContext.request.parameter('state')}"/>
<c:set var="client" value="${pageContext.request.parameter('client_id')}"/>
<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="oracle.iam.access.bundle.Resource"/>
  <head>
    <meta name="robots"              content="noindex, nofollow">
    <meta name="viewport"            content="width=device-width, initial-scale=1.0">
    <meta http-equiv="expires"       content="0">
    <meta http-equiv="pragma"        content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="description"   content="Page to signin">
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
      <div id="dw__navbar_nav" class="navbar navbar-light navbar-expand-md">
    </div>
    </div>
    <div id="dokuwiki__content" >
      <div>
        <div class="container">
          <div class="row mt-5">
            <div class="col-3">
              <h1 class="h1-responsive font-weight-bold"><fmt:message key="consent.title"/></h1>
              <hr class="hr-dark">
              <h4 class="mb-3"><fmt:message key="consent.state.pre"/></h4>
              <p class="mb-3"><fmt:message key="consent.consent.pre"/></p>
              <p class="mb-3"><fmt:message key="consent.consent.post"/></p>
            </div>
            <div class="col-8">
              <form id="consent-form" name="consent" class="form-canvas needs-validation" role="form" method="post" action="/oauth2/rest/approval" novalidate>
                <div class="text-center mt-4">
                  <button id="consent-deny"  class="btn btn-sm" onclick="deny();"><fmt:message key="consent.deny"/></button>
                  <button id="consent-allow" class="btn btn-sm btn-dark" onclick="allow();"><fmt:message key="consent.allow"/></button>
                </div>
                <!--
                 | Access Manager expects 2 values to be sent back: state and act
                 | The "act" holds the values of consent or deny
                 |   0 : Deny
                 |   1 : Consent
                 -->
                <input id="state" name="state" type="hidden" value="${state}"/>
                <input id="act"   name="act"   type="hidden" value=""/>
              </form>
              <script type="text/javascript">
                function deny() {
                  var frm = document.getElementById('consent-form');
                  var act = document.getElementById('act');
                  act.value = "0";
                  frm.submit();
                }
                function allow() {
                  var frm = document.getElementById('consent-form');
                  var act = document.getElementById('act');
                  act.value = "1";
                  frm.submit();
                  }
              </script>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script type="text/javascript" src="lib/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="lib/redsecurity.min.js"></script>
  </body>
</html>