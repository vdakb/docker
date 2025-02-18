package de.itk.oidcdemo.demowebanwendung;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.itk.auth.client.demoutil.DemoHtmlUtil;
import de.itk.auth.client.demoutil.DemoUtil;
import de.itk.auth.client.demoutil.HttpRequestException;

import javax.ws.rs.core.HttpHeaders;

/**
 * Servlet implementation class CallWebservice
 */
@WebServlet("/call-webservice")
public class CallWebservice extends HttpServlet {

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
    final HttpGet        wsRequest = new HttpGet(config.getWebserviceUrl());

    if (accessToken != null) {
      wsRequest.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }

    final String wsResponse = doCall(session, wsRequest, "Aufruf des Webservices");
    session.setAttribute(OidcDemoConst.SA_WS_RESPONSE, wsResponse);

    if (isInsightMode) {
      request.setAttribute(OidcDemoConst.ATTR_REDIRECT_URI,        DemoUtil.getBaseUrl(request) + "?" + OidcDemoConst.QP_INSIGHT_MODE + "=1&" + OidcDemoConst.ATTR_INIT_TAB_BTN + "=" + OidcDemoConst.TAB_WEBSERVICE);
      request.setAttribute(OidcDemoConst.ATTR_WEBSERVICE_REQUEST,  DemoHtmlUtil.getRequestToHtml(wsRequest));
      request.setAttribute(OidcDemoConst.ATTR_WEBSERVICE_RESPONSE, DemoHtmlUtil.jsonToHtml(wsResponse));
      request.getRequestDispatcher("/WEB-INF/webservice-insight-mode.jsp").forward(request, response);
    }
    else {
      request.setAttribute(OidcDemoConst.ATTR_INIT_TAB_BTN, OidcDemoConst.TAB_WEBSERVICE);
      request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

  }

  protected String doCall(final HttpSession session, HttpGet wsRequest, String action)
    throws HttpRequestException, IOException {
    CloseableHttpClient httpClient = null;
    String              wsResponse = null;

    try {
      httpClient = HttpClients.createDefault();
      final HttpResponse httpResponse = httpClient.execute(wsRequest);
      if (httpResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
        wsResponse = EntityUtils.toString(httpResponse.getEntity());
        DemoUtil.addToSessionLog(session, action + " - erfolgreich");
      }
      else {
        wsResponse = httpResponse.getStatusLine().toString();
        DemoUtil.addToSessionLog(session, action + " - fehlgeschlagen");
      }
    }
    catch (Exception e) {
      DemoUtil.addToSessionLog(session, action + " - fehlgeschlagen");
      throw new HttpRequestException(action, e, wsRequest);
    }
    finally {
      if (httpClient != null) {
        httpClient.close();
      }
    }
    return wsResponse;
  }

}
