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
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>  
<fmt:setLocale value="${pageContext.request.locale}"/>
<fmt:setBundle basename="bka.iam.access.bundle.Resource"/>
<!-- entityID of the requesting SP -->
<c:set var="entityID"    value="${pageContext.request.getParameter('entityID')}"/>
<c:set var="requestIp"   value="${applicationScope.storage.requestIP(pageContext.request)}"/>
<c:set var="provider"    value="${applicationScope.storage.provider(requestIp)}"/>

<!-- idp_name of the requesting SP -->
<c:set var="resource_url"    value="${pageContext.request.getParameter('resource_url')}"/>
<c:set var="idp_name_id" value="${applicationScope.storage.findProviderId(resource_url)}"/>

<c:set var="request_id"  value="${pageContext.request.getParameter('request_id')}"/>
<c:set var="oam_req"     value="${pageContext.request.getHeader('OAM_REQ')}"/>

<!--
the query parameter that will contain the IdP's ProviderID/Issuer when
redirecting the user back to the SP
-->
<c:set var="returnPRM" value="providerid"/>
<!--the URL where the user should be redirected at the SP -->
<c:set var="returnURL" value="/oam/server/auth_cred_submit"/>

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
    <title><fmt:message key="provider.title"/></title>
    <!-- ===============================================-->
    <!-- Favicons                                       -->
    <!-- ===============================================-->
    <link type="image/ico" rel="shortcut icon" href="img/favicon.png"/>
    <!-- ===============================================-->
    <!-- Stylesheets                                    -->
    <!-- ===============================================-->
    <link type="text/css"  rel="stylesheet"    href="css/bootstrap.min.css">
    <link type="text/css"  rel="stylesheet"    href="css/bootstrap-cust.min.css">
    <link type="text/css"  rel="stylesheet"    href="css/mdb.min.css">
    <link type="text/css"  rel="stylesheet"    href="css/main.min.css">
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
    </script>
    <!-- Choose automatically partner by idp_name or Ip Range -->
    <script type="text/javascript">
		  window.onload = function() {
			  if ('${idp_name_id}' != '#') { 
				  document.getElementById('${idp_name_id}').click(); 
				}
		    if ('${provider.partner}' != '#') { 
				  document.getElementById('${provider.id}').click(); 
				}
			}
      
		</script>
  </head>
  <body class="background-gray">
    <div id="header-bg" class="navbar navbar-light navbar-expand-md navbar-fixed-top">
      <img class="navbar-brand" src="img/branding.png"/>
      <img class="ml-auto"      src="img/provider.png"/>
    </div>
    <div id="header-bg-top">
      <div id="header-bg-tagline">
        <h3><fmt:message key="portal.title"/></h3>
      </div>
    </div>
   <div class="container margin-top">
      <div class="col-md-auto col-sm-auto">
        <form id="landing-form" name="landing" class="form-canvas" method="post" action="/submit.jsp">
          <div class="d-flex justify-content-between">
            <h3 class="mt-auto text-dark"><fmt:message key="provider.title"/></h3>
            <img class="align-baseline image-avatar" src="img/discovery.png"/>
          </div>
          <hr class="hr-dark mb-3 mt-4"/>
          <p id="idp"><fmt:message key="provider.instruction"/></p>
          <div class="container">
            <div class="row">
              <c:forEach var="provider" items="${applicationScope.storage.provider}">
                <div class="card">
                  <div class="card-body">
                    <img class="card-img-top rounded mx-auto d-block" src="${provider.symbol}" alt="${provider.name}" style="height: 5em;width: auto;">
                  </div>
                  <div class="card-footer text-center">
                    <a id="${provider.id}" class="card-title stretched-link" href="#" onclick="submitProvider('${provider.partner}');"><small>${provider.name}</small></a>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </form>
      </div>
    </div>
    <!-- Remove OAM LB domain from returnURL-->
    <form id="fed-discovery-data" name="fed-discovery-data" method="post" action="/oam/${fn:substringAfter(returnURL,"/oam/")}">
      <input type="hidden" id="${returnPRM}" name="${returnPRM}" value="">
      <input type="hidden" id="request_id"   name="request_id"   value="${request_id}"
      <input type="hidden" id="OAM_REQ"      name="OAM_REQ"      value="${oam_req}">
    </form>
    <script type="text/javascript" src="lib/jquery.min.js"></script>
    <script type="text/javascript" src="lib/popper.min.js"></script>
    <script type="text/javascript" src="lib/bootstrap.min.js"></script>
    <script type="text/javascript" src="lib/mdb.min.js"></script>
    <script type="text/javascript" >
      function submitProvider(id) {
        document.getElementById('${returnPRM}').value = id;
        document.getElementById('fed-discovery-data').submit();
      }
    </script>
  </body>
</html>