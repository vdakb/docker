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
<%--
translate the message key used by inline expansion and store it in a variable
--%>
<fmt:message key="signin.submit" var="submit"/>
<%--
request OAM_REQ parameter from Header OAM Version 12.1.2
--%>
<c:set var="accessReq" value="${pageContext.request.getHeader('OAM_REQ')}"/>
<c:set var="requestId" value="${pageContext.request.getParameter('request_id')}"/>
<%--
error code sent by OAM in case of failure
--%>
<c:set var="errorCode" value="${pageContext.request.getHeader('p_error_code')}"/>
<html lang="${pageContext.request.locale}">
  <head>
    <meta name="robots"              content="noindex, nofollow">
    <meta name="viewport"            content="width=device-width, initial-scale=1.0">
    <meta http-equiv="expires"       content="0">
    <meta http-equiv="pragma"        content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="description"   content="Page to signin">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <title><fmt:message key="signin.title"/></title>
    <link type="text/css" rel="stylesheet" href="css/font-barlow.min.css">
    <link type="text/css" rel="stylesheet" href="css/font-awesome.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="css/avatar.min.css">
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
  <body class="background-gray">
    <div id="header-bg">
      <div id="header-bg-top">
        <div id="header-bg-wrapper">
          <div class="header-bg-avatar"></div>
          <div class="header-bg-speech">
            <h3><fmt:message key="signin.title"/></h3>
          </div>
        </div>
      </div>
    </div>
    <div class="container margin-top-15">
      <div class="col-md-12">
        <form name="sigin" role="form" method="post" action="/oam/server/auth_cred_submit" class="form-horizontal form-canvas needs-validation" novalidate>
          <c:if test="${not empty errorCode}">
            <div class="form-group">
              <div class="col-sm-12">
                <span class="error"><fmt:message key="${errorCode}"/></span>
              </div>
            </div>
          </c:if>
          <div class="form-group">
            <div class="col-sm-12">
              <label for="username" class="control-label"><fmt:message key="signin.username.label"/></label>
              <div class="icon-container">
                <i class="fas fa-user"></i>
                <input id="fc-username" name="username" class="form-control" type="text" required>
                <div id="fb-username" class="invalid-feedback"><fmt:message key="signin.username.invalid"/></div>
              </div>
            </div>
         </div>
         <div class="form-group">
            <div class="col-sm-12">
              <label for="password" class="control-label"><fmt:message key="signin.password.label"/></label>
              <div class="icon-container">
                <i class="fas fa-lock"></i>
                <input id="fc-password" name="password" class="form-control" type="password" required>
                <div id="fb-password" class="invalid-feedback"><fmt:message key="signin.password.invalid"/></div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-sm-12">
              <div class="checkbox">
                <button type="submit" class="btn btn-info"><fmt:message key="signin.submit"/></button>
                &nbsp;
                <label><input type="checkbox">&nbsp;<fmt:message key="signin.remember"/></label>
              </div>
              <br>
              <div>
                <a href="forgot-username" class="text-left  pull-left"><fmt:message key="forgot.username"/>&nbsp;</a>
                <a href="forgot-password" class="text-left  pull-left"><fmt:message key="forgot.password"/>&nbsp;</a>
                <a href="signup"          class="text-right pull-right"><fmt:message key="signin.signup"/></a>
              </div>
            </div>
          </div>
          <input id="requestCtx" type="hidden" name="request_id" value="${requestId}">
          <input id="accessCtx"  type="hidden" name="OAM_REQ"    value="${accessReq}">
        </form>
      </div>
    </div>
    <script type="text/javascript" src="lib/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="lib/avatar.min.js"></script>
  </body>
</html>