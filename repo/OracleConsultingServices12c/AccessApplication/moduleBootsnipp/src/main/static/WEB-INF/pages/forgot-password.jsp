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
    <meta http-equiv="description"   content="Page to send forgotten password">
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
              <h4><fmt:message key="forgot.password"/></h4>
              <h6><fmt:message key="reset.password"/></h6>
              <form action="#" method="post">
                <fieldset class="clearfix">
                  <p><span class="fa fa-envelope"></span><input type="text" placeholder="<fmt:message key='signin.email.label'/>" required></p>
                  <div>
                    <span style="width:49%; text-align:left;  display:inline-block;">
                      <a class="small-text" href="signin"><fmt:message key="signin.action"/></a>
                    </span>
                    <span style="width:50%; text-align:right; display:inline-block;">
                      <input type="submit" value="<fmt:message key='reset.submit'/>">
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