package de.itk.oidcdemo.demowebanwendung;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;

import de.itk.auth.client.demoutil.DemoHtmlUtil;
import de.itk.auth.client.demoutil.DemoUtil;
import de.itk.auth.client.oidc.ExchangeTokenRequest;
import de.itk.auth.client.oidc.TokenResponse;

/**
 * Servlet implementation class CallWebservice
 */
@WebServlet("/call-webservice-dedicated") public class CallWebserviceDedicated extends CallWebservice {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    final HttpSession    session = request.getSession(true);
    final boolean        isInsightMode = "1".equals(request.getParameter(OidcDemoConst.QP_INSIGHT_MODE));
    final OidcDemoConfig config = OidcDemoConfig.getInstance(request.getServletContext());
    final String         accessToken = (String)session.getAttribute(OidcDemoConst.SA_ACCESS_TOKEN);

    final ExchangeTokenRequest tokenRequest = new ExchangeTokenRequest(config, config.getWebserviceScope(), accessToken);
    tokenRequest.setClientId(config.getClientId());
    tokenRequest.setClientSecret(config.getClientSecret());

    final TokenResponse tokenResponse = tokenRequest.execute();
    final HttpGet       wsRequest = new HttpGet(config.getWebserviceUrl());

    if (tokenResponse.isSuccess()) {
      DemoUtil.addToSessionLog(session, "Token-Exchange - erfolgreich");
      wsRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken());
    }
    else {
      DemoUtil.addToSessionLog(session, "Token-Exchange - fehlgeschlagen");
    }

    final String wsResponse = doCall(session, wsRequest, "Aufruf des Webservices mit dediziertem Token");
    session.setAttribute(OidcDemoConst.SA_WS_RESPONSE, wsResponse);

    if (isInsightMode) {
      request.setAttribute(OidcDemoConst.ATTR_TOKEN_REQUEST, OidcDemoHtmlUtil.tokenRequestToHtml(tokenRequest));
      request.setAttribute(OidcDemoConst.ATTR_TOKEN_RESPONSE, DemoHtmlUtil.jsonToHtml(tokenResponse.getResponseJson()));
      request.setAttribute(OidcDemoConst.ATTR_REDIRECT_URI, DemoUtil.getBaseUrl(request) + "?" + OidcDemoConst.QP_INSIGHT_MODE + "=1&" + OidcDemoConst.ATTR_INIT_TAB_BTN + "=" + OidcDemoConst.TAB_WEBSERVICE);
      request.setAttribute(OidcDemoConst.ATTR_WEBSERVICE_REQUEST, DemoHtmlUtil.getRequestToHtml(wsRequest));
      request.setAttribute(OidcDemoConst.ATTR_WEBSERVICE_RESPONSE, DemoHtmlUtil.jsonToHtml(wsResponse));
      request.getRequestDispatcher("/WEB-INF/webservice-dedicated-insight-mode.jsp").forward(request, response);
    }
    else {
      request.setAttribute(OidcDemoConst.ATTR_INIT_TAB_BTN, OidcDemoConst.TAB_WEBSERVICE);
      request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

  }

}
