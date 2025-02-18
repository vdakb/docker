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
    <meta name="viewport"            content="width=320; initial-scale=1.0; maximum-scale=1.0; user-scalable=1;" />
    <meta http-equiv="expires"       content="0">
    <meta http-equiv="pragma"        content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="description"   content="Page to signout">
    <meta http-equiv="content-type"  content="text/html; charset=UTF-8"/>
    <title><fmt:message key="signout.title"/></title>
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css">
    <style type="text/css">
      body{
        background-color:      #f5f5f5;
        padding-top:           40px;
        padding-bottom:        40px;
      }
      .form-signout{
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
      .form-signout .form-signout-heading{
        margin-bottom:         10px;
      }
    </style>
  </head>
  <body>
    <%
      String imgBeg="<img border=\"0\" width=\"0\" height=\"0\" src=\"";
      String imgEnd="\"/>";
      Enumeration e = request.getHeaders("OAM_LOGOUT_CALLBACK_URLS");
      while (e.hasMoreElements()) {
      String headerVal = (String)e.nextElement();
      out.write(imgBeg+headerVal+imgEnd);
    }
    %>
    <div class="container">
      <div class="form-signout">
        <h2 class="form-signout-heading"><fmt:message key="signout.title"/></h2>
        <p><fmt:message key="signout.instruction"/></p><label><a href="signout" class="text-primary pull-right"><fmt:message key="signin.action"/></a></label>
      </div>
    </div>
  </body>
</html>