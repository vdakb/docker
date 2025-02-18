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
<html lang="${pageContext.request.locale}">
  <head>
    <meta name="robots"              content="noindex, nofollow">
    <meta name="viewport"            content="width=device-width, initial-scale=1.0">
    <meta http-equiv="expires"       content="0">
    <meta http-equiv="pragma"        content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="description"   content="Page to sign out">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <link href="img/favicon.ico"       type="image/x-icon" rel="icon"/>
    <link href="img/favicon-touch.png" type="image/png"    rel="apple-touch-icon"/>
    <link href="img/favicon-32x32.png" type="image/png"    rel="icon"/>
    <link href="img/favicon-16x16.png" type="image/png"    rel="icon"/>
    <title><fmt:message key="signout.title"/></title>
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
              <h4 style="width:48%; text-align:left; display: inline-block;"><fmt:message key="signout.instruction"/></h4>
              <div>
                <span style="width:48%; text-align:left; display: inline-block;">
                  <a class="small-text" href="signin"><fmt:message key="signout.signin"/></a>
                </span>
              </div>
              <div class="clearfix"></div>
            </div> <!-- end login -->
            <div class="logo">
              <img src="favicon-touch.png" width="180" height="180"/>
              <div class="clearfix"></div>
            </div>
          </div>
        </center>
      </div>
    </div>
    <script type="text/javascript" src="lib/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap.min.js"></script>
  </body>
</html>
