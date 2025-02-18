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
import de.itk.auth.client.oidc.AuthorizationCodeTokenRequest;
import de.itk.auth.client.oidc.TokenResponse;

/**
 * Servlet implementation class Redirect
 */
@WebServlet("/redirect") public class Redirect extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    final HttpSession session = request.getSession(true);
    final boolean     isInsightMode = OidcDemoConst.QP_INSIGHT_MODE.equals(request.getParameter(OidcDemoConst.QP_STATE));
    final String      error = request.getParameter(OidcDemoConst.QP_ERROR);
    final String      errorDesc = request.getParameter(OidcDemoConst.QP_ERROR_DESC);

    if (error != null) {
      DemoUtil.addToSessionLog(session, "Redirect vom STS mit Fehlermeldung: " + error + ((errorDesc != null) ? ": " + errorDesc : ""));

      if (isInsightMode) {
        request.setAttribute(OidcDemoConst.ATTR_REQUEST_URI, DemoHtmlUtil.uriToHtml(request.getRequestURI() + "?" + request.getQueryString()));
        request.setAttribute(OidcDemoConst.ATTR_REDIRECT_URI, DemoUtil.getBaseUrl(request).replace("&", "&amp;") + "?" + OidcDemoConst.QP_INSIGHT_MODE + "=1");
        request.getRequestDispatcher("/WEB-INF/redirect-insight-mode.jsp").forward(request, response);
      }
      else {
        response.sendRedirect(DemoUtil.getBaseUrl(request));
      }

    }
    else {
      DemoUtil.addToSessionLog(session, "Redirect vom STS - erfolgreich");

      requestTokens(request, response, session, isInsightMode);
    }
  }

  private void requestTokens(HttpServletRequest request, HttpServletResponse response, final HttpSession session, final boolean isInsightMode)
    throws ServletException, IOException {
    final OidcDemoConfig config = OidcDemoConfig.getInstance(request.getServletContext());
    final String         authorizationCode = request.getParameter(OidcDemoConst.QP_AUTHORIZATION_CODE);
    final String         codeVerifier = (String)session.getAttribute(OidcDemoConst.SA_CODE_VERIFIER);

    session.setAttribute(OidcDemoConst.SA_CODE_VERIFIER, null); // Sessionattribut löschen

    final AuthorizationCodeTokenRequest tokenRequest = new AuthorizationCodeTokenRequest(config, authorizationCode);
    tokenRequest.setClientId(config.getClientId());
    tokenRequest.setClientSecret(config.getClientSecret());
    tokenRequest.setRedirectUri(DemoUtil.getBaseUrl(request) + "/redirect");
    tokenRequest.setCodeVerifier(codeVerifier);

    final TokenResponse tokenResponse = tokenRequest.execute();
    if (tokenResponse.isSuccess()) {
      DemoUtil.addToSessionLog(session, "Tokenanfrage mit Auth-Code beim STS - erfolgreich");

      session.setAttribute(OidcDemoConst.SA_ACCESS_TOKEN, tokenResponse.getAccessToken());
      session.setAttribute(OidcDemoConst.SA_ID_TOKEN, tokenResponse.getIdToken());
      session.setAttribute(OidcDemoConst.SA_REFRESH_TOKEN, tokenResponse.getRefreshToken());

    }
    else if (tokenResponse.getErrorMessage() != null) {
      DemoUtil.addToSessionLog(session, "Tokenanfrage mit Auth-Code beim STS liefert Fehlermeldung: " + tokenResponse.getErrorMessage());
    }
    else {
      DemoUtil.addToSessionLog(session, "Tokenanfrage mit Auth-Code beim STS fehlgeschlagen.");
    }

    if (isInsightMode) {
      request.setAttribute(OidcDemoConst.ATTR_REQUEST_URI, DemoHtmlUtil.uriToHtml(request.getRequestURI() + "?" + request.getQueryString()));
      request.setAttribute(OidcDemoConst.ATTR_TOKEN_REQUEST, OidcDemoHtmlUtil.tokenRequestToHtml(tokenRequest));
      request.setAttribute(OidcDemoConst.ATTR_TOKEN_RESPONSE, DemoHtmlUtil.jsonToHtml(tokenResponse.getResponseJson()));
      request.setAttribute(OidcDemoConst.ATTR_REDIRECT_URI, DemoUtil.getBaseUrl(request).replace("&", "&amp;") + "?" + OidcDemoConst.QP_INSIGHT_MODE + "=1");
      request.getRequestDispatcher("/WEB-INF/redirect-insight-mode.jsp").forward(request, response);
    }
    else {
      response.sendRedirect(DemoUtil.getBaseUrl(request));
    }
  }

}
