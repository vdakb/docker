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
<%@ page import="java.util.*" %>
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

<c:set var="contextPath" value="${pageContext.request.getContextPath()}"/>
<c:set var="OTPEmails"   value="${pageContext.request.getParameter('OTPEmails')}"/>
<%--
error code sent by OAM in case of failure
--%>
<c:set var="errorCode" value="${pageContext.request.getParameter('error_code')}"/>
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
    <link type="image/ico" rel="shortcut icon" href="${contextPath}/img/favicon.png"/>
    <!-- ===============================================-->
    <!-- Custom Fonts                                   -->
    <!-- ===============================================-->
    <link type="text/css"  rel="stylesheet"    href="${contextPath}/css/zondicons.min.css">
    <!-- ===============================================-->
    <!-- Stylesheets                                    -->
    <!-- ===============================================-->
    <link type="text/css"  rel="stylesheet"    href="${contextPath}/css/bootstrap.min.css">
    <link type="text/css"  rel="stylesheet"    href="${contextPath}/css/mdb.min.css">
    <link type="text/css"  rel="stylesheet"    href="${contextPath}/css/main.min.css">
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
    <div id="header-bg" class="navbar navbar-light navbar-expand-md navbar-fixed-top">
      <img class="navbar-brand" src="${contextPath}/img/branding.png"/>
      <img class="ml-auto"      src="${contextPath}/img/provider.png"/>
    </div>
    <div id="header-bg-top">
      <div id="header-bg-tagline">
        <h3><fmt:message key="portal.title"/></h3>
      </div>
    </div>
    <div class="container margin-top">
      <div class="col-md-auto col-sm-auto">
        <form id="signin-form" name="signin" class="form-canvas needs-validation" method="post" action="/oam/server/auth_cred_submit" novalidate>
	 <div class="d-flex justify-content-between">
            <h3 class="mt-auto text-dark"><fmt:message key="otp.title"/></h3>
          </div>
          <hr class="hr-dark mb-3 mt-4"/>
          <c:choose>
              <c:when test = "${OTPEmails != null }">
                <p><fmt:message key="otp.select.email"/><p>
                 <c:forTokens var="OTPEmail" items="${OTPEmails}" delims="," varStatus="stat">
                    <div class="form-check">
                        <i class="zir zi-envelope prefix text-dark active"></i>
                        <input type="radio" class="text-dark" name="OTPEmailIndex" value="${stat.index}" id="${stat.index}" ${stat.index == 0 ? 'checked="true"':''}/>
                        <label for="${stat.index}" class="form-check-label"><c:out value="${OTPEmail}"/></label>
                    </div>
                  </c:forTokens>
                  <div class="d-flex justify-content-between">
                    <div>
                      <div id="signin-feedback" class="invalid-feedback is-invalid">
                        <c:if test = "${errorCode != null }">
                          <p><fmt:message key="${errorCode}"/></p>
                        </c:if>
                      </div> 
                    </div>
                    <div>
                        <button type="submit" name="SubmitEmail"  class="btn btn-info"><fmt:message key="otp.action.submit"/></button>                
                    </div>
                  </div>            
                  <input id='OTPAction'  type='hidden' name='OTPAction'  value='EMAIL'>
              </c:when>
              <c:when test = "${errorCode != 'OAM-OTP-000' }">
                  <p><fmt:message key="signin.instruction"/><p>
                  <div class="md-form">
                    <i class="zir zi-lock-closed prefix text-dark active"></i>
                    <input id="OTPin" name="OTPin" type="text" class="form-control text-dark" required/>
                    <label for="OTPin" class="active"><fmt:message key="otp.pin"/></label>
                    <div id="password-feedback" class="invalid-feedback"><fmt:message key="otp.password.invalid"/></div>
                  </div>
                  <div class="d-flex justify-content-between">
                    <div>
                      <div id="signin-feedback" class="invalid-feedback is-invalid">
                        <c:if test = "${errorCode != null }">
                          <p><fmt:message key="${errorCode}"/></p>
                        </c:if>
                      </div> 
                    </div>
                    <div>
                        <button type="submit" name="Re-Send" class="btn btn-info" ${(errorCode == 'OAM-OTP-002' || errorCode == 'OAM-OTP-004') ? 'disabled' : ''} onclick="this.disabled=true;document.body.style.cursor = 'wait'; document.getElementById('OTPAction').value='RESEND';form.submit();return false;"><fmt:message key="otp.action.resend"/></button>
                        <button type="submit" name="Submit"  class="btn btn-info" ${(errorCode == 'OAM-OTP-002' || errorCode == 'OAM-OTP-003' || errorCode == 'OAM-OTP-004') ? 'disabled' : ''} ><fmt:message key="otp.action.submit"/></button>                
                    </div>
                  </div>
                  <input id='OTPAction'  type='hidden' name='OTPAction'  value='SUBMIT'>
              </c:when> 
              <c:otherwise>
                <p><fmt:message key="otp.missing.email.instructions"/><p>
              </c:otherwise>
          </c:choose>
          
          <input id="requestCtx" type="hidden" name="request_id" value="${requestId}">
          <input id="accessCtx"  type="hidden" name="OAM_REQ"    value="${accessReq}">
          
        </form>
      </div>
    </div>
    <script type="text/javascript" src="${contextPath}/lib/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/popper.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/mdb.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/main.js"></script>
    <script type="text/javascript">
      var signinFeedback = document.getElementById('signin-feedback');
      if (signinFeedback.firstChild === null || signinFeedback.firstChild.nodeValue === null || signinFeedback.firstChild.nodeValue === '' ) {
        signinFeedback.style.display = 'none';
      }
      else {
        signinFeedback.style.display = 'unset';
      }
    </script>
  </body>
</html>