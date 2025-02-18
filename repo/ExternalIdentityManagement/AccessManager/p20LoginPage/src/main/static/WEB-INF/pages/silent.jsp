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
<%@ page import="java.net.URLDecoder"%>
<%@ page session="false"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="bka.iam.access.bundle.Resource"/>
<c:set var="user"   value="${pageContext.request.getHeader('OAM_REMOTE_USER')}"/>
<c:set var="state"  value="${pageContext.request.getParameter('state')}"/>
<c:set var="client" value="${pageContext.request.getParameter('client_id')}"/>
<c:set var="scope"  value="${pageContext.request.getParameter('scopes')}"/>
<c:set var="claim"  value="${pageContext.request.getParameter('claims')}"/>
<c:if test = "${user == null}">
  <c:set var="user" value="${pageContext.request.getHeader('REMOTE_USER')}"/>
</c:if>
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
    <title><fmt:message key="consent.title"/></title>
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
    <style id="clickJack" type="text/css">body { display: none !important; }</style>
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
                      <c:choose>
                        <c:when test="${user == null}">
                          <!-- show 403 -->
                          <h1 class="mb-0" style="font-size: 28px !important;font-weight:700 !important;"><span class="code"><fmt:message key="consent.403.code"/></span> - <span class="text"><fmt:message key="consent.403.text"/></span></h1>
                          <div class="mt-0 ruler"></div>
                          <p class="message"><fmt:message key="consent.403.message"/></p>
                          <p><fmt:message key="consent.403.activity"/></p>
                        </c:when>    
                        <c:when test="${state == null or state == '' or client == null or client == '' or scope == null or scope == ''}">
                          <!-- show 400 -->
                          <h1 class="mb-0" style="font-size: 28px !important;font-weight:700 !important;"><span class="code"><fmt:message key="consent.400.code"/></span> - <span class="text"><fmt:message key="consent.400.text"/></span></h1>
                          <div class="mt-0 ruler"></div>
                          <p class="message"><fmt:message key="consent.400.message"/></p>
                          <p><fmt:message key="consent.400.activity"/></p>
                        </c:when>
                        <c:otherwise>
                          <!-- normal operation -->
                          <p><fmt:message key="consent.app.name"/> <i>${client}</i> <fmt:message key="consent.app.request"/></p>
                          <p>
                          <c:forTokens items="${scope}" delims=" " var="item">
                            <b><c:out value="${item}"/></b>
                          </c:forTokens>
                          </p>
                          <p class="jumbo-body-title"><fmt:message key="silent.approval"/></p>
                          <p class="jumbo-body-title"><fmt:message key="silent.instruction"/></p>
                          <!-- form -->
                          <form id="f01" name="consent" method="post" action="/oauth2/rest/approval">
                            <input type="hidden" id="state" name="state" value="${state}">
                            <input type="hidden" id="act"   name="act"   value="">
                          </form>
                        </c:otherwise>
                      </c:choose>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <script type="text/javascript">
      document.addEventListener("DOMContentLoaded",
        function(event) {
			    var act = document.getElementById('act');
  			  act.value = "1";
			    var frm = document.getElementById('f01');
  			  frm.submit();
        }
      );
    </script>
  </body>
</html>