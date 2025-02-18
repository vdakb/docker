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
    <meta name="viewport"            content="width=320; initial-scale=1.0; maximum-scale=1.0; user-scalable=1;" />
    <meta http-equiv="expires"       content="0">
    <meta http-equiv="pragma"        content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="description"   content="Page to signin">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <title><fmt:message key="signin.title"/></title>
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <style type="text/css">
      body{
        background-color:      #f5f5f5;
        padding-top:           40px;
        padding-bottom:        40px;
      }
      .form-signin{
        background-color:      #fff;
        max-width:             300px;
        padding:               19px 29px 29px;
        margin:                0 auto 20px;
        border:                1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow:    0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow:    0 1px 2px rgba(0,0,0,.05);
                box-shadow:    0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading, .form-signin .checkbox{
        margin-bottom:         10px;
      }
      .form-signin input[type="text"], .form-signin input[type="password"]{
        font-size:             16px;
        height:                auto;
        margin-bottom:         15px;
        padding:               7px 9px;
      }
    </style>
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
    <div class="container">
      <form name="signin" class="form-signin needs-validation" role="form" action="/oam/server/auth_cred_submit" method="post" novalidate>
        <h2 class="form-signin-heading"><fmt:message key="signin.title"/></h2>
        <c:if test="${not empty errorCode}">
          <div class="form-group">
            <div class="col-sm-12">
              <span class="error"><fmt:message key="${errorCode}"/></span>
            </div>
          </div>
        </c:if>
        <input type="text"     class="input-block-level" placeholder="<fmt:message key='signin.username.label'/>">
        <input type="password" class="input-block-level" placeholder="<fmt:message key='signin.password.label'/>">
        <label class="checkbox">
          <input type="checkbox" value="remember-me"> <fmt:message key="signin.remember"/>
        </label>
        <button class="btn btn-large btn-primary" type="submit"><fmt:message key="signin.action"/></button>
        <input type="hidden" id="requestCtx" name="request_id" value="${requestId}">
        <input type="hidden" id="accessCtx"  name="OAM_REQ"    value="${accessReq}">
      </form>
    </div>
  </body>
</html>