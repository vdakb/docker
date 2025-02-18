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
translate the message key used by inline expansion and store it in a variable
--%>
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
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="css/bootsnipp.min.css">
  </head>
  <body>
    <div class="main">
      <div class="container">
        <center>
          <div class="panel">
            <div id="login">
              <h4 style="color:#93272c;"><fmt:message key="signup.title"/></h4>
              <form action="#" method="post">
                <fieldset class="clearfix">
                  <p><span class="fa fa-user"></span><input id="username"         name="username"     type="text"     placeholder="<fmt:message key='signup.username.label'/>" required></p>
                  <p><span class="fa fa-lock"></span><input id="password"         name="password"     type="password" placeholder="<fmt:message key='signup.password.label'/>" required></p>
                  <p><span class="fa fa-lock"></span><input id="confirmation"     name="confirmation" type="password" placeholder="<fmt:message key='signup.confirm.label'/>"  required></p>
                  <p><span class="fa fa-envelope"></span><input id="emailaddress" name="emailaddress" type="text"     placeholder="<fmt:message key='signup.email.label'/>"     required></p>
                  <p><span class="fa"></span><input id="firstname" name="firstname" type="text"     placeholder="<fmt:message key='signup.firstname.label'/>" required></p>
                  <p><span class="fa"></span><input id="lastname" name="lastname" type="text"       placeholder="<fmt:message key='signup.lastname.label'/>" required></p>
                  <div>
                    <span style="width:49%; text-align:left;  display:inline-block;">
                      <input type="submit" value="<fmt:message key='signup.submit'/>">
                    </span>
                    <span style="width:50%; text-align:right; display:inline-block;">
                      <a class="small-text" href="signin"><fmt:message key="signup.return"/></a>
                    </span>
                  </div>
                </fieldset>
                <div class="clearfix"></div>
              </form>
              <div class="clearfix"></div>
            </div> <!-- end login -->
            <div class="logo">
              <img src="img/favicon-touch.png" width="180" height="180"/>
              <div class="clearfix"></div>
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