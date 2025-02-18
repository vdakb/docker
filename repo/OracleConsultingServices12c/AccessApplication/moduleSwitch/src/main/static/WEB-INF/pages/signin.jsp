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
    <link type="text/css" rel="stylesheet" href="css/switch.min.css">
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
    <h1 class="title-agile text-center"><fmt:message key="signin.title"/></h1>
    <div class="content-w3ls">
      <div class="content-top-agile">
        <h4><fmt:message key="signin.instruction"/></h4>
      </div>
      <div class="content-bottom">
        <form name="sigin" role="form" method="post" action="/oam/server/auth_cred_submit">
          <div class="field-group">
            <span class="fa fa-user" aria-hidden="true"></span>
            <div class="wthree-field">
              <input id="fc-username" name="username" type="text" value="" placeholder="<fmt:message key='signin.username.label'/>" required>
            </div>
          </div>
          <div class="field-group">
            <span class="fa fa-lock" aria-hidden="true"></span>
            <div class="wthree-field">
              <input id="fc-password" name="password" type="password" placeholder="<fmt:message key='signin.password.label'/>" required>
            </div>
          </div>
          <div class="wthree-field">
            <input type="submit" id="submit" name="submit" value="<fmt:message key='signin.submit'/>"/>
          </div>
          <ul class="list-login">
            <li class="switch-agileits">
              <label class="switch"><input type="checkbox"><span class="slider round"></span><fmt:message key="signin.remember"/></label>
            </li>
            <li>
              <a href="forgot-password" class="text-right"><fmt:message key="forgot.password"/></a>
              <a href="forgot-username" class="text-right"><fmt:message key="forgot.username"/></a>
            </li>
            <li class="clearfix"></li>
          </ul>
          <input type="hidden" id="requestCtx" name="request_id" value="${requestId}">
          <input type="hidden" id="accessCtx"  name="OAM_REQ"    value="${accessReq}">
        </form>
      </div>
    </div>
  </body>
</html>