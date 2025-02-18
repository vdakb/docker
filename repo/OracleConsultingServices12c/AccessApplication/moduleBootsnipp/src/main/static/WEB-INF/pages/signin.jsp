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
<%@ page contentType="text/html;charset=UTF-8"%>
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
    <meta http-equiv="description"   content="Page to sign in">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <title><fmt:message key="signin.title"/></title>
    <link type="text/css" rel="stylesheet" href="css/font-awesome.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootsnipp.min.css">
  </head>
  <body>
    <div class="main">
      <div class="container">
        <center>
          <div class="panel">
            <div id="login">
              <h4><fmt:message key="signin.title"/></h4>
              <h6><fmt:message key="signin.instruction"/></h6>
              <c:if test="${not empty errorCode}">
                <div class="form-group">
                  <div class="col-sm-12">
                    <span class="error"><fmt:message key="${errorCode}"/></span>
                  </div>
                </div>
              </c:if>
              <form name="sigin" role="form" action="/oam/server/auth_cred_submit" method="post">
                <fieldset class="clearfix">
                  <p><span class="fa fa-envelope"></span><input id="fc-username" name="username" type="text" placeholder="<fmt:message key='signin.email.label'/>" required></p>
                  <p><span class="fa fa-lock"></span><input id="fc-password"     name="password" type="password" placeholder="<fmt:message key='signin.password.label'/>" required></p>
                  <div>
                    <span style="width:49%; text-align:left; display:inline-block;">
                      <input id="remember" name="remember" type="checkbox" value=""/><label for="remember">&nbsp;<fmt:message key="signin.remember"/></label>
                    </span>
                    <span style="width:50%; text-align:right; display:inline-block;">
                      <input type="submit" value="<fmt:message key='signin.submit'/>">
                    </span>
                  </div>
                  <input type="hidden" id="requestCtx" name="request_id" value="${requestId}">
                  <input type="hidden" id="accessCtx"  name="OAM_REQ"    value="${accessReq}">
                </fieldset>
                <div style="padding-top: 15px;">
                  <span style="width:49%; text-align:left; display:inline-block;">
                    <a class="small-text" href="signup"><fmt:message key="signin.signup"/></a>
                  </span>
                  <span style="width:50%; text-align:right; display: inline-block;">
                    <a class="small-text" href="forgot-password"><fmt:message key="forgot.password"/></a>
                    <br>
                    <a class="small-text" href="forgot-username"><fmt:message key="forgot.username"/></a>
                  </span>
                </div>
                <div class="clearfix"></div>
              </form>
              <div class="clearfix"></div>
            </div> <!-- end login -->
            <div class="logo">
              <img src="img/favicon-touch.png" width="180" height="180"/>
            </div>
          </div>
        </center>
      </div>
    </div>
    <script type="text/javascript" src="lib/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="lib/bootsnipp.min.js"></script>
  </body>
</html>
