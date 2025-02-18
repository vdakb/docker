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
<%@ page import="java.util.Enumeration"%>
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
    <meta http-equiv="description"   content="Page to signout">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <title><fmt:message key="signout.title"/></title>
    <link type="text/css" rel="stylesheet" href="css/material.min.css">
  </head>
  <body>
    <div class="container sm">
      <div class="box">
        <div class="title"><fmt:message key="signout.title"/></div>
        <%
          String imgBeg="<img border=\"0\" width=\"0\" height=\"0\" src=\"";
          String imgEnd="\"/>";
          Enumeration e = request.getHeaders("OAM_LOGOUT_CALLBACK_URLS");
          while (e.hasMoreElements()) {
          String headerVal = (String)e.nextElement();
          out.write(imgBeg+headerVal+imgEnd);
        }
        %>
        <div class="input">
          <p><fmt:message key="signout.instruction"/></p>
        </div>
        <a href="signin" class="forgot"><fmt:message key="signin.action"/></a>
      </div>
    </div>
  </body>
</html>