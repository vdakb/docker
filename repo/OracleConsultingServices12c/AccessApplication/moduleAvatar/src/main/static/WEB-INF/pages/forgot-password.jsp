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
<%@ page session="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="oracle.iam.access.bundle.Resource"/>
<%--
translate the message key used by inline expansion and store it in a variable
--%>
<fmt:message key="reset.submit" var="submit"/>
<html lang="${pageContext.request.locale}">
  <head>
    <meta name="robots"              content="noindex, nofollow">
    <meta name="viewport"            content="width=device-width, initial-scale=1.0">
    <meta http-equiv="expires"       content="0">
    <meta http-equiv="pragma"        content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="description"   content="Page to send forgotten password">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <title><fmt:message key="signin.title"/></title>
    <link type="text/css" rel="stylesheet" href="css/font-barlow.min.css">
    <link type="text/css" rel="stylesheet" href="css/font-awesome.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="css/avatar.min.css">
  </head>
  <body class="background-gray">
    <section id="header-bg">
      <div id="header-bg-top">
        <div id="header-bg-wrapper">
          <div class="header-bg-avatar"></div>
          <div class="header-bg-speech">
            <h3><fmt:message key="forgot.password"/></h3>
          </div>
        </div>
      </div>
    </section>
    <div class="container margin-top-15">
      <div class="col-md-12">
        <form class="form-horizontal form-canvas needs-validation" role="form" action="#" method="post" novalidate>
         <div class="form-group">
            <div class="col-md-12">
              <fmt:message key="reset.password"/>
            </div>
          </div>
          <div class="form-group">
            <div class="col-md-12">
              <label for="fc-email" class="control-label"><fmt:message key="signin.email.label"/></label>
              <div class="icon-container">
                <i class="fa fa-envelope"></i>
                <input id="fc-email" name="emailaddress" class="form-control" type="text" required>
                <div id="fb-email" class="invalid-feedback"><fmt:message key='signin.email.invalid'/></div>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div class="col-md-12">
              <input type="submit" value="${submit}" class="btn btn-info">
              <a href="signin" class="text-right pull-right"><fmt:message key="signin.action"/></a>
            </div>
          </div>
        </form>
      </div>
    </div>
    <script type="text/javascript" src="lib/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="lib/avatar.min.js"></script>
  </body>
</html>