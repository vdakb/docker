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

<%
  // Check if this page is protected by OAM policy
	String user = (String)request.getHeader("OAM_REMOTE_USER");
  if (user == null) {
    user = (String)request.getHeader("REMOTE_USER");
  }

  if(user == null || user.isEmpty()){
    response.sendError(403, "Contact Administrator for access to this resource!");
  }
	
	// Check for required input parameters provided by OAM Server
	String formState  = (String) request.getParameter("state");
	String nonceState = (String) request.getParameter("nonce");
  String clientName = (String) request.getParameter("client_id");
  String scope      = (String) request.getParameter("scopes");   
	
  if (formState == null || formState.isEmpty() || clientName == null || clientName.isEmpty() || scope == null || scope.isEmpty()) {
    response.sendError(400, "Invalid parameters");
  }
%>

<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="bka.iam.access.bundle.Resource"/>

<c:set var="contextPath" value="${pageContext.request.getContextPath()}"/>
<c:set var="errorCode"   value="${pageContext.request.getParameter('error_code')}"/>

<c:set var="scopes"      value="${pageContext.request.getParameter('scopes')}"/>
<c:set var="clientName"  value="${pageContext.request.getParameter('client_id')}"/>
<c:set var="state"       value="${pageContext.request.getParameter('state')}"/>

<html dir="ltr" lang="${pageContext.request.locale}">

<% if("true".equalsIgnoreCase(getServletConfig().getInitParameter("AUTO_APPROVAL"))){%>
		<head>
			<meta http-equiv="expires"         content="0">
			<meta http-equiv="pragma"          content="no-cache">
			<meta http-equiv="cache-control"   content="no-cache">
			<meta http-equiv="description"     content="Page to confirm consent">
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
			<link type="image/ico" rel="shortcut icon" href="${contextPath}/img/favicon.png"/>
		</head>
		<body>
			<form id="consent_frm" name="consent_frm" class="form-canvas needs-validation" method="post" action="/oauth2/rest/approval" novalidate>
				<input id="act"   type="hidden" name="act"   value="1">
				<input id="state" type="hidden" name="state" value="<c:out value="${state}"/>"/>
				<input id="nonce" type="hidden" name="nonce" value="<c:out value="${nonce}"/>"/>
			 </form>
			<script type="text/javascript">
				document.addEventListener("DOMContentLoaded", () => { 
				  var frm = document.getElementById('consent_frm');
				  frm.submit();
				});
			</script>
		</body>
		
	<%}else{%>

  <head>
    <meta http-equiv="expires"         content="0">
    <meta http-equiv="pragma"          content="no-cache">
    <meta http-equiv="cache-control"   content="no-cache">
    <meta http-equiv="description"     content="Page to confirm consent">
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
		
		function deny() {
			var frm = document.getElementById('consent_frm');
			var act = document.getElementById('act');
			act.value = "0";
			frm.submit();
		}

		function allow() {
			var frm = document.getElementById('consent_frm');
			var act = document.getElementById('act');
			act.value = "1";
			frm.submit();
		}
		
    </script>
  </head>
  <body class="background-gray">
    <script type="text/javascript" src="${contextPath}/lib/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/popper.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/mdb.min.js"></script>
    <script type="text/javascript" src="${contextPath}/lib/main.js"></script>
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
        <form id="consent_frm" name="consent_frm" class="form-canvas needs-validation" method="post" action="/oauth2/rest/approval" novalidate>
				  <div class="d-flex justify-content-between">
            <h3 class="mt-auto text-dark"><fmt:message key="consent.title"/></h3>
          </div>
          <hr class="hr-dark mb-3 mt-4"/>
					<p><fmt:message key="consent.disclaimer.start"/> <span class="text-primary"><c:out value="${clientName}"/></span> <fmt:message key="consent.disclaimer.end"/><p>
					 <c:forTokens var="scope" items="${scopes}" delims=" ">
							<div class="form-check">
									<i class="zir zi-shield prefix text-dark active"></i>
									<c:out value="${scope}"/>
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
									<button type="submit" name="Reject"  class="btn btn-info" onclick="deny();"><fmt:message key="consent.action.reject"/></button>                
									<button type="submit" name="Approve" class="btn btn-info" onclick="allow();"><fmt:message key="consent.action.approve"/></button>                
							</div>
						</div>            
          <input id="act"   type="hidden" name="act"   value="">
					<input id="state" type="hidden" name="state" value="<c:out value="${state}"/>"/>
        </form>
      </div>
    </div>
	</body>
<%}%>

</html>