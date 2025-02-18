<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.Date,
		java.util.Map,
		java.text.SimpleDateFormat,
		com.fasterxml.jackson.databind.node.ObjectNode,
		de.itk.auth.client.demoutil.DemoUtil,
		de.itk.auth.client.demoutil.DemoHtmlUtil,
		de.itk.oidcdemo.demowebanwendung.OidcDemoConfig,
		de.itk.oidcdemo.demowebanwendung.OidcDemoConst,
		de.itk.oidcdemo.demowebanwendung.Token,
		de.itk.oidcdemo.demowebanwendung.OidcDemoUtil,
		de.itk.oidcdemo.demowebanwendung.OidcDemoHtmlUtil,
		de.itk.auth.client.GetAppVersion" %>
<!DOCTYPE html>
<html lang="de">
<head>
	<title>OIDC Demo-Webanwendung</title>
	<link rel="stylesheet" href="res/styles.css">
	<link rel="stylesheet" href="res/common.css">
	<link rel="stylesheet" href="res/log.css">
	<link rel="stylesheet" href="res/json.css">
	<script src="res/countdown.js"></script>
	<script src="res/tabs.js"></script>
	<script src="res/scroll.js"></script>
	<script>
		function updateCountdowns() {
			setCountdown(<%=OidcDemoUtil.getExpireTimeForToken(session, OidcDemoConst.SA_ACCESS_TOKEN)%>, "countdownAc");
			setCountdown(<%=OidcDemoUtil.getExpireTimeForToken(session, OidcDemoConst.SA_ID_TOKEN)%>, "countdownId");
			setCountdown(<%=OidcDemoUtil.getExpireTimeForToken(session, OidcDemoConst.SA_REFRESH_TOKEN)%>, "countdownRf");
		}
		setInterval(updateCountdowns, 1000);
	</script>
</head>
<body class="nav-fixed main-body">
	<!-- Top app bar navigation menu-->
	<nav class="top-app-bar-app navbar navbar-expand" >
	    <div class="container-fluid px-2">
	        <div class="p20-logoapp">
	            <a  href="<%= DemoUtil.getBaseUrl( request ) %>">
	            	<span class="logo-text-headline2">PG IAM &ndash; OIDC Demo-Webanwendung <span class="version">(v<%=GetAppVersion.get()%>)</span></span>
	            </a>
	        </div>
	    </div>
	</nav>

	<% final OidcDemoConfig demoConfig = OidcDemoConfig.getInstance( request.getServletContext() ); %>


	<!-- Layout wrapper-->
	<div id="layoutDrawer">
		<!-- Layout content-->
		<div id="layoutDrawer_dashboard">
			<!-- Main dashboard content-->
			<div class="container-xl p-5">

				<div class="card card-code card-compact card-documentation mb-3">
					<div class="card-header">
						<h4>Aktionen</h4>
					</div>
					<div class="card-body">
						<form method="get">
							<div class="mb-4">
								<button
									class="btn btn-primary"
									type="submit"
									formaction="<%=DemoUtil.getBaseUrl( request )%>/authorize"
									title="Login über STS per OIDC durchführen"
									<%=OidcDemoUtil.isTokenExpired( session, OidcDemoConst.SA_ACCESS_TOKEN ) ? "" : "disabled"%>
									>Login</button>
								<button
									class="btn btn-primary"
									type="submit"
									formaction="<%=DemoUtil.getBaseUrl( request )%>/refresh-tokens"
									title="Mit dem Refresh-Token alle Tokens erneuern"
									>Tokens Erneuern</button>
								<button
									class="btn btn-primary"
									type="submit"
									formaction="<%=DemoUtil.getBaseUrl( request )%>/userinfo"
									title="Mit aktuellem Access-Token Userinfo-Endpoint des STS abfragen"
									<%=OidcDemoUtil.isTokenExpired( session, OidcDemoConst.SA_ACCESS_TOKEN ) ? "disabled" : ""%>
									>Benutzerinformationen abfragen</button>
								<button
									class="btn btn-primary"
									type="submit"
									formaction="<%=DemoUtil.getBaseUrl( request )%>/logout"
									title="Aktuelle Session der Anwendung beenden"
									<%=OidcDemoUtil.isTokenExpired( session, OidcDemoConst.SA_ACCESS_TOKEN ) ? "disabled" : ""%>
									>Ausloggen - Anwendung</button>
								<button
									class="btn btn-primary"
									type="submit"
									formaction="<%=DemoUtil.getBaseUrl( request )%>/logout-sts"
									title="Aktuelle Session der Anwendung sowie des STS beenden"
									<%=OidcDemoUtil.isTokenExpired( session, OidcDemoConst.SA_ACCESS_TOKEN ) ? "disabled" : ""%>
									>Ausloggen - Komplett</button>
							</div>
							<div class="settings mb-4">
								<label for="scope">Scope: </label>
								<input
									id="scope"
									name="<%= OidcDemoConst.QP_SCOPE %>"
									value="<%=OidcDemoUtil.getScope( session )%>"
									<%=OidcDemoUtil.isTokenExpired( session, OidcDemoConst.SA_ACCESS_TOKEN ) ? "" : "disabled"%>/>
								<label for="idp">IdP: </label>
								<select
									id="idp"
									name="<%= OidcDemoConst.QP_IDP %>">
									<option value="<%= OidcDemoConst.IDP_FIAM %>">F-IAM</option>
									<option value="">&lt;Auswahl beim Login&gt;</option>
									<% for (String entry : demoConfig.getTnIdps()) { %>
									<option value="<%= entry %>"><%= entry %></option>
									<% } %>
								</select>
								<label for="insight-mode">Insight-Mode:</label>
								<div><input
									id="insight-mode"
									type="checkbox"
									name="<%= OidcDemoConst.QP_INSIGHT_MODE %>"
									value="1"
									<%=("1".equals( request.getParameter( OidcDemoConst.QP_INSIGHT_MODE ) ) ) ? "checked" : ""%>
									title="Insight-Modus aktivieren/deaktivieren. Im Insight-Modus werden von der Webanwendung anstelle der regulären Redirects eigene Seiten angezeigt, so dass auch die Zwischenschritte nachvollzogen werden können."/></div>
							</div>
							<%
							    if ( (!OidcDemoConfig.getInstance( request.getServletContext() ).getWebappLinks().isEmpty())
									|| (OidcDemoConfig.getInstance( request.getServletContext() ).getWebserviceUrl() != null) ) {
							%>
							<div class="">
								<h5>Fremdaufrufe</h5>
								<%
								    if (OidcDemoConfig.getInstance( request.getServletContext() ).getWebserviceUrl() != null) {
								%>
								<button
									class="btn btn-secondary"
									type="submit"
									formaction="<%=DemoUtil.getBaseUrl( request )%>/call-webservice"
									title="Webservice unter Verwendung des Access-Tokens der Webanwendung aufrufen"
									>Webservice</button>
								<%
								    }
								%>
								<button
									class="btn btn-secondary"
									type="submit"
									formaction="<%=DemoUtil.getBaseUrl( request )%>/call-webservice-dedicated"
									title="Webservice unter Verwendung eines speziell dafür angefragten Access-Tokens aufrufen"
									>Webservice - Dediziertes Token</button>
								<%
									for (Map.Entry<String,String> link : demoConfig.getWebappLinks() ) {
								%>
								<a
									class="btn btn-secondary"
									href="<%= link.getKey() %>"
									title="<%= link.getValue() %> in separatem Tab aufrufen"
									target="_blank"
									rel="noopener"
									><%= link.getValue() %></a>
								<%
									}
								 %>
							</div>
							<%
							    }
							%>
						</form>
					</div>
				</div>

				<div class="card card-code card-compact card-documentation mb-3">
					<div class="card-header">
						<h4>Log</h4>
					</div>
					<div class="card-body">
						<div id="log">
							<%=DemoHtmlUtil.logToHtml( (String)session.getAttribute( OidcDemoConst.SA_LOG ) )%>
						</div>
					</div>
				</div>

				<div class="card card-code card-compact card-documentation mb-3">
					<div class="card-header">
						<h4 class="mb-4">Inhalte</h4>
					</div>
					<div class="">
						<ul class="nav nav-tabs nav-tabs-mini-underlined" role="tablist">
						    <li class="nav-item" role="presentation">
						        <a id="<%=OidcDemoConst.TAB_ACCESS_TOKEN%>" class="nav-link" type="button" onclick="openTab(event, 'AcToken')">Access Token <span class="countdown">(<span id="countdownAc"></span>)</span></a>
						    </li>
						    <li id="IdTokenBn" class="nav-item" role="presentation">
						        <a class="nav-link" type="button" onclick="openTab(event, 'IdToken')">ID Token <span class="countdown">(<span id="countdownId"></span>)</span></a>
						    </li>
						    <li class="nav-item" role="presentation">
						        <a id="RfTokenBn" class="nav-link" type="button" onclick="openTab(event, 'RfToken')">Refresh Token <span class="countdown">(<span id="countdownRf"></span>)</span></a>
						    </li>
						    <li class="nav-item" role="presentation">
						        <a id="<%=OidcDemoConst.TAB_USERINFO%>" class="nav-link" type="button" onclick="openTab(event, 'Userinfo')">Userinfo</a>
						    </li>
						    <li class="nav-item" role="presentation">
						        <a id="<%=OidcDemoConst.TAB_WEBSERVICE%>" class="nav-link" type="button" onclick="openTab(event, 'WsAntwort')">Webservice Antwort</a>
						    </li>
						</ul>

						<div class="tab-content">
							<div id="AcToken" class="tab-pane" role="tabpanel">
								<div class="p-3">
									<h6 class="mb-2">Rohdaten</h6>
									<input class="token mb-3" type="text" readonly value="<%=(session.getAttribute(OidcDemoConst.SA_ACCESS_TOKEN) == null) ? "" : (String)session.getAttribute(OidcDemoConst.SA_ACCESS_TOKEN)%>" onClick="this.select()">
									<%=OidcDemoHtmlUtil.tokenToHtml( session, OidcDemoConst.SA_ACCESS_TOKEN )%>
								</div>
							</div>

							<div id="IdToken" class="tab-pane" role="tabpanel">
								<div class="p-3">
									<h6 class="mb-2">Rohdaten</h6>
									<input class="token mb-3" type="text" readonly value="<%=(session.getAttribute(OidcDemoConst.SA_ID_TOKEN) == null) ? "" : (String)session.getAttribute(OidcDemoConst.SA_ID_TOKEN)%>" onClick="this.select()">
									<%=OidcDemoHtmlUtil.tokenToHtml( session, OidcDemoConst.SA_ID_TOKEN )%>
								</div>
							</div>

							<div id="RfToken" class="tab-pane" role="tabpanel">
								<div class="p-3">
									<h6 class="mb-2">Rohdaten</h6>
									<input class="token mb-3" type="text" readonly value="<%=(session.getAttribute(OidcDemoConst.SA_REFRESH_TOKEN) == null) ? "" : (String)session.getAttribute(OidcDemoConst.SA_REFRESH_TOKEN)%>" onClick="this.select()">
									<%=OidcDemoHtmlUtil.tokenToHtml( session, OidcDemoConst.SA_REFRESH_TOKEN )%>
									<p><em>Hinweis: </em> Refresh-Tokens müssen nicht durch die Anwendung verifiziert werden, da sie nur gegenüber dem STS zur Authentifizierung dienen, der sie dann selbst verifiziert.</p>
								</div>
							</div>

							<div id="Userinfo" class="tab-pane" role="tabpanel">
								<div class="p-3">
									<p>Zeitpunkt der Abfrage: <%=(session.getAttribute(OidcDemoConst.SA_USERINFO_TIME) == null) ? "-" : new SimpleDateFormat( OidcDemoConst.DATE_FORMAT ).format( (Date)session.getAttribute(OidcDemoConst.SA_USERINFO_TIME) )%> </p>
									<%=DemoHtmlUtil.stringToHtml( (String)session.getAttribute( OidcDemoConst.SA_USERINFO ) )%>
								</div>
							</div>

							<div id="WsAntwort" class="tab-pane" role="tabpanel">
								<div class="p-3">
									<h6 class="mb-2">URL</h6>
									<p class="code"><%=OidcDemoConfig.getInstance( request.getServletContext() ).getWebserviceUrl()%></p>
									<h6>Antwort</h6>
									<%=DemoHtmlUtil.stringToHtml( (String)session.getAttribute( OidcDemoConst.SA_WS_RESPONSE ) )%>
								</div>
							</div>
						</div>
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
	<script>
		updateCountdowns();
		document.getElementById("<%=(request.getAttribute( OidcDemoConst.ATTR_INIT_TAB_BTN ) != null) ?
		    	request.getAttribute( OidcDemoConst.ATTR_INIT_TAB_BTN ) :
		    	(request.getParameter( OidcDemoConst.ATTR_INIT_TAB_BTN ) != null) ?
		    	    request.getParameter( OidcDemoConst.ATTR_INIT_TAB_BTN ) :
		    	        OidcDemoConst.TAB_ACCESS_TOKEN%>").click();
		scrollDown("log");
	</script>
</body>
</html>


