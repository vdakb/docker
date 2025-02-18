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
    <meta http-equiv="description"   content="Page to signin">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <title><fmt:message key="signup.title"/></title>
    <link type="text/css" rel="stylesheet" href="css/font-awesome.min.css">
    <link type="text/css" rel="stylesheet" href="css/material.min.css">
  </head>
  <body>
    <form name="sigin" role="form" action="#" method="post">
      <div class="container lg">
        <div class="box">
          <div class="title"><fmt:message key="signup.title"/></div>
          <div id="row">
            <div class="input">
              <label for="fc-email"><fmt:message key="signup.email.label"/></label>
              <input id="fc-emial" name="emailaddress" type="email" required>
              <span class="spin"></span>
            </div>
          </div>
          <div id="row">
            <div class="float left">
              <div class="input">
                <label for="fc-password"><fmt:message key="signup.password.label"/></label>
                <input id="fc-password" name="password" type="password" required>
                <span class="spin"></span>
              </div>
            </div>
            <div class="float right">
              <div class="input">
                <label for="fc-confirm"><fmt:message key="signup.confirm.label"/></label>
                <input id="fc-confirm" name="confirmation" type="password" required>
                <span class="spin"></span>
              </div>
            </div>
          </div>
          <div id="row" class="clearfix">
            <div class="input">
              <label for="fc-username"><fmt:message key="signup.username.label"/></label>
              <input id="fc-username" name="username" type="text" required>
              <span class="spin"></span>
            </div>
          </div>
          <div id="row">
            <div class="float left">
              <div class="input">
                <label for="fc-firstname"><fmt:message key="signup.firstname.label"/></label>
                <input id="fc-firstname" name="firstname" type="text" required>
                <span class="spin"></span>
              </div>
            </div>
            <div class="float right">
              <div class="input">
                <label for="fc-lastname"><fmt:message key="signup.lastname.label"/></label>
                <input id="fc-lastname" name="lastname" type="text" required>
                <span class="spin"></span>
              </div>
            </div>
          </div>
          <div class="clearfix">
            <br>
            <p><fmt:message key="signup.agree.pre"/>&nbsp;<a class="text-primary" href="javascript:;" data-toggle="modal" data-target="#terms"><fmt:message key="signup.agree.terms"/></a>&nbsp;<fmt:message key="signup.agree.and"/>&nbsp;<a class="text-primary" href="javascript:;" data-toggle="modal" data-target="#privacy"><fmt:message key="signup.agree.privacy"/></a><fmt:message key="signup.agree.post"/></p>
          </div>
          <div class="button signup">
            <button><span><fmt:message key="signup.submit"/></span></button>
          </div>
          <a href="signin" class="forgot"><fmt:message key="signin.action"/></a>
        </div>
      </div>
    </form>
    <script type="text/javascript" src="lib/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="lib/material.min.js"></script>
  </body>
</html>