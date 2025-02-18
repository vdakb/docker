package de.itk.oidcdemo.demowebanwendung;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.itk.auth.client.demoutil.DemoUtil;
import de.itk.auth.client.oidc.AuthorizationCodeRequestUrl;

/**
 * Servlet implementation class Authorize
 */
@WebServlet("/authorize") public class Authorize extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    final HttpSession    session = request.getSession(true);
    final OidcDemoConfig config = OidcDemoConfig.getInstance(getServletContext());
    final String         scope = request.getParameter(OidcDemoConst.QP_SCOPE) == null ? config.getDefaultScope() : request.getParameter(OidcDemoConst.QP_SCOPE);
    final String         idp = request.getParameter(OidcDemoConst.QP_IDP);
    final boolean        isInsightMode = "1".equals(request.getParameter(OidcDemoConst.QP_INSIGHT_MODE));

    final AuthorizationCodeRequestUrl redirectUrl = new AuthorizationCodeRequestUrl(config);
    redirectUrl.setScope(scope);
    redirectUrl.setClientId(config.getClientId());
    redirectUrl.setRedirectUri(DemoUtil.getBaseUrl(request) + "/redirect");
    redirectUrl.setState(isInsightMode ? OidcDemoConst.QP_INSIGHT_MODE : null);
    if ((idp != null) && (!idp.isEmpty())) {
      if (idp.equals(OidcDemoConst.IDP_FIAM)) {
        redirectUrl.setAuthorizationEndpoint(config.getStdAuthorizationEndpoint());
      }
      else {
        redirectUrl.addParameter(OidcDemoConst.QP_IDP_NAME, idp);
      }
    }
    final String redirectUri = redirectUrl.build();

    session.setAttribute(OidcDemoConst.SA_CODE_VERIFIER, redirectUrl.getCodeVerifier());

    DemoUtil.addToSessionLog(session, "Login initiiert");

    if (isInsightMode) {
      request.setAttribute(OidcDemoConst.ATTR_REDIRECT_URI, redirectUri);
      request.getRequestDispatcher("/WEB-INF/authorize-insight-mode.jsp").forward(request, response);
    }
    else {
      response.sendRedirect(redirectUri);
    }
  }

}
