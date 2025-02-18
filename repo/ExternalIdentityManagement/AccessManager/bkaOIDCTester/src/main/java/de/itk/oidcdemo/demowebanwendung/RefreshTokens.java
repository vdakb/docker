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
import de.itk.auth.client.oidc.RefreshTokenTokenRequest;
import de.itk.auth.client.oidc.TokenResponse;

/**
 * Servlet implementation class RefreshTokens
 */
@WebServlet("/refresh-tokens") public class RefreshTokens extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    final HttpSession    session = request.getSession(true);
    final OidcDemoConfig config = OidcDemoConfig.getInstance(request.getServletContext());
    final boolean        isInsightMode = "1".equals(request.getParameter(OidcDemoConst.QP_INSIGHT_MODE));

    final RefreshTokenTokenRequest tokenRequest = new RefreshTokenTokenRequest(config, (String)session.getAttribute(OidcDemoConst.SA_REFRESH_TOKEN));
    tokenRequest.setClientId(config.getClientId());
    tokenRequest.setClientSecret(config.getClientSecret());

    final TokenResponse tokenResponse = tokenRequest.execute();
    if (tokenResponse.isSuccess()) {

      DemoUtil.addToSessionLog(session, "Tokenanfrage mit Refresh-Token beim STS - erfolgreich");
      session.setAttribute(OidcDemoConst.SA_ACCESS_TOKEN, tokenResponse.getAccessToken());
      if (tokenResponse.getIdToken() != null) {
        // Nur austauschen, wenn ein neuer übermittelt wurde, sonst alten behalten.
        session.setAttribute(OidcDemoConst.SA_ID_TOKEN, tokenResponse.getIdToken());
      }
      if (tokenResponse.getRefreshToken() != null) {
        // Nur austauschen, wenn ein neuer übermittelt wurde, sonst alten behalten.
        session.setAttribute(OidcDemoConst.SA_REFRESH_TOKEN, tokenResponse.getRefreshToken());
      }

      if (isInsightMode) {
        request.setAttribute(OidcDemoConst.ATTR_REQUEST_URI, DemoHtmlUtil.uriToHtml(request.getRequestURI() + "?" + request.getQueryString()));
        request.setAttribute(OidcDemoConst.ATTR_TOKEN_REQUEST, OidcDemoHtmlUtil.tokenRequestToHtml(tokenRequest));
        request.setAttribute(OidcDemoConst.ATTR_TOKEN_RESPONSE, DemoHtmlUtil.jsonToHtml(tokenResponse.getResponseJson()));
        request.setAttribute(OidcDemoConst.ATTR_REDIRECT_URI, DemoUtil.getBaseUrl(request).replace("&", "&amp;") + "?" + OidcDemoConst.QP_INSIGHT_MODE + "=1");
        request.getRequestDispatcher("/WEB-INF/refresh-tokens-insight-mode.jsp").forward(request, response);
      }
      else {
        response.sendRedirect(DemoUtil.getBaseUrl(request));
      }

    }
    else {
      session.setAttribute(OidcDemoConst.SA_ACCESS_TOKEN, null);
      session.setAttribute(OidcDemoConst.SA_ID_TOKEN, null);
      session.setAttribute(OidcDemoConst.SA_REFRESH_TOKEN, null);

      if (tokenResponse.getErrorMessage() != null) {
        DemoUtil.addToSessionLog(session, "Tokenanfrage mit Auth-Code beim STS liefert Fehlermeldung: " + tokenResponse.getErrorMessage());
      }
      else {
        DemoUtil.addToSessionLog(session, "Tokenanfrage mit Auth-Code beim STS fehlgeschlagen.");
      }
    }

  }

}
