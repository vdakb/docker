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
<fmt:setBundle basename="bka.iam.access.bundle.Resource"/>
<%--
request OAM_REQ parameter from Header OAM Version 12.1.2
--%>
<c:set var="accessReq" value="${pageContext.request.getHeader('OAM_REQ')}"/>
<c:set var="requestId" value="${pageContext.request.getParameter('request_id')}"/>
<c:set var="requestIp" value="${applicationScope.storage.requestIP(pageContext.request)}"/>
<c:set var="symbol"    value="${applicationScope.storage.symbol(requestIp)}"/>
<%--
error code sent by OAM in case of failure
--%>
<c:set var="errorCode" value="${pageContext.request.getParameter('p_error_code')}"/>
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
    <title><fmt:message key="signin.title"/></title>
    <!-- ===============================================-->
    <!-- Favicons                                       -->
    <!-- ===============================================-->
    <link media="screen" rel="icon"             type="image/png" sizes="32x32"   href="ico/p20-32x32.png"/>
    <link media="screen" rel="icon"             type="image/png" sizes="16x16"   href="ico/p20-16x16.png"/>
    <link media="screen" rel="apple-touch-icon" type="image/png" sizes="180x180" href="ico/p20-touch.png"/>
    <link media="screen" rel="shortcut icon"    type="image/x-icon" href="ico/p20-icon.ico"/>
    <!-- ===============================================-->
    <!-- Custom Fonts                                   -->
    <!-- ===============================================-->
    <link type="text/css"  rel="stylesheet"    href="css/zondicons.min.css">
    <!-- ===============================================-->
    <!-- Stylesheets                                    -->
    <!-- ===============================================-->
    <link type="text/css"  rel="stylesheet"    href="css/bootstrap.min.css">
    <link type="text/css"  rel="stylesheet"    href="css/bootstrap.p20.css">
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
  <body class="container-fluid vh-100 fixed-navbar">
    <div class="page-wrapper">
      <div class="content-wrapper">
        <div class="content-header"/>
        <div class="fade-in-op">
          <div class="container mt-4">
            <div class="card jumbo">
              <div class="card-body jumbo-body">
                <div class="row h-100 no-gutters">
                  <div class="col-md-6">
                    <img class="ml-4 mt-4" src="img/p20-signin-md.png" alt="signin"/>
                  </div>
                  <div class="col-md-6">
                    <div class="h-100">
                      <div>
                        <img class="logo" src="img/p20-logo-lg.png" width="177" height="100" alt="logo"/>
                      </div>
                      <p class="jumbo-body-title"><fmt:message key="signin.instruction"/></p>
                      <form id="f01" name="signin" method="post" action="/oam/server/auth_cred_submit">
                        <div class="form-row mt-2">
                          <div class="form-group">
                            <label for="username" class="active"><fmt:message key="signin.username"/></label>
                            <div class="input-group">
                              <div class="input-group-prepend">
                                <div class="input-group-text"><i class="zir zi-user prefix text-dark active"></i></i></div>
                              </div>
                              <input id="username" name="username" type="text" class="form-control text-dark" required/>
                            </div>
                            <small class="form-text text-muted"><fmt:message key="signin.username.invalid"/></small>
                          </div>
                        </div>
                        <div class="form-row">
                          <div class="form-group">
                            <label for="password" class="active"><fmt:message key="signin.password"/></label>
                            <div class="input-group">
                              <div class="input-group-prepend">
                                <div class="input-group-text"><i class="zir zi-lock-closed prefix text-dark active"></i></div>
                              </div>
                              <input id="password" name="password" type="password" class="form-control text-dark" required/>
                            </div>
                            <small class="form-text text-muted"><fmt:message key="signin.password.invalid"/></small>
                          </div>
                        </div>
                        <div class="form-row">
                          <button type="submit" class="btn-submit"><fmt:message key="signin.action"/></button>
                        </div>
                        <div id="form-feedback" class="invalid-feedback is-invalid">
                          <c:if test = "${errorCode != null}">
                            <p><fmt:message key="${errorCode}"/></p>
                          </c:if>
                        </div>
                        <input id="requestCtx" type="hidden" name="request_id" value="${requestId}">
                        <input id="accessCtx"  type="hidden" name="OAM_REQ"    value="${accessReq}">
                      </form>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script type="text/javascript" src="lib/jquery.min.js"></script>
    <!--
    <script type="text/javascript" src="lib/p2020.js"/>
    -->
    <script type="text/javascript">
      var feedback = document.getElementById('form-feedback');
      if (feedback.firstChild === null || feedback.firstChild.nodeValue === null || feedback.firstChild.nodeValue === '' ) {
        feedback.style.display = 'none';
      }
      else {
        feedback.style.display = 'unset';
      }
    </script>
  </body>
</html>