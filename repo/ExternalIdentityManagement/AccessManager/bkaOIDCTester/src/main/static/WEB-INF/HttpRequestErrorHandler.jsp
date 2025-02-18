<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="de.itk.oidcdemo.demowebanwendung.OidcDemoConst,
		de.itk.oidcdemo.demowebanwendung.OidcDemoUtil,
		de.itk.oidcdemo.demowebanwendung.OidcDemoHtmlUtil,
		de.itk.auth.client.demoutil.DemoUtil,
		de.itk.auth.client.demoutil.HttpRequestException,
		de.itk.auth.client.GetAppVersion" %>
<!DOCTYPE html>
<html lang="de">
<head>
	<title>OIDC Demo-Webanwendung</title>
	<link rel="stylesheet" href="res/styles.css">
	<link rel="stylesheet" href="res/common.css">
</head>
<body class="nav-fixed main-body">
	<!-- Top app bar navigation menu-->
	<nav class="top-app-bar-app navbar navbar-expand" >
	    <div class="container-fluid px-2">
	        <div class="p20-logoapp">
	        	<a href="<%= DemoUtil.getBaseUrl( request ) %>">
	            	<span class="logo-text-headline2">PG IAM &ndash; OIDC Demo-Webanwendung <span class="version">(v<%=GetAppVersion.get()%>)</span></span>
	            </a>
	        </div>
	    </div>
	</nav>

	<!-- Layout wrapper-->
	<div id="layoutDrawer">
		<!-- Layout content-->
		<div id="layoutDrawer_dashboard">
      		<!-- Main dashboard content-->
			<div class="container-xl p-5">

				<div class="card card-code card-compact card-documentation mb-3">
					<div class="card-header">
						<h4 class="error">HTTP-Request fehlgeschlagen</h4>
					</div>
					<div class="card-body">
						<p>Aktion: <%=((HttpRequestException)request.getAttribute( "javax.servlet.error.exception" )).getAction()%></p>
						<p>Fehlermeldung: <code><%=((HttpRequestException)request.getAttribute( "javax.servlet.error.exception" )).getException().toString()%></code></p>
						<%=((HttpRequestException)request.getAttribute( "javax.servlet.error.exception" )).getRequestAsHtml()%>
						<p>Zurück zur Hauptseite: <a class="button" href="<%=DemoUtil.getBaseUrl( request )%>">Klick</a></p>
						<p>Siehe Server-Logdateien für weitere Details.</p>
					</div>

				</div>
			</div>

	        <!-- Footer-->
	        <!-- Min-height is set inline to match the height of the drawer footer-->
	        <footer class="border-top  d-flex justify-content-center d-none d-sm-flex" style="min-height: 65px">

	            <div class="p-20-logo-bottom-zusatztext" style="font-size:0.75rem">
	                <div style="transform:translate(0,29px)">Dieses Projekt ist<br> Bestandteil von</div>
	            </div>
	            <div class="p20-logo-bottom">
	                <img src="res/P20_Logo_Klein_Blau-Dunkelblau_Claim.svg" class="bottom-logo" style="height:100px;" alt="">
	            </div>

	        </footer>
	        <footer class="border-top  justify-content-center  d-flex d-sm-none" style="min-height: 30px">

	            <div class="p-20-logo-bottom-zusatztext" style="font-size:0.75rem">
	                <div style="transform:translate(0,6px)">Dieses Projekt ist Bestandteil von</div>
	            </div>
	            <div class="p20-logo-bottom">
	                <img src="res/P20_Logo_Klein_Blau-Dunkelblau_Signet.svg" class="bottom-logo" style="height:30px;" alt="">
	            </div>

	        </footer>
		</div>
	</div>
</body>
</html>