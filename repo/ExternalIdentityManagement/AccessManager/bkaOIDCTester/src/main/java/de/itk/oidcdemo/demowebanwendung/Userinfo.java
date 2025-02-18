package de.itk.oidcdemo.demowebanwendung;

import java.io.IOException;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.itk.auth.client.demoutil.DemoHtmlUtil;
import de.itk.auth.client.demoutil.DemoUtil;
import de.itk.auth.client.oidc.UserinfoRequest;
import de.itk.auth.client.oidc.UserinfoResponse;

/**
 * Servlet implementation class Userinfo
 */
@WebServlet("/userinfo") public class Userinfo extends HttpServlet {

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

    final UserinfoRequest  userInfoRequest = new UserinfoRequest(config, (String)session.getAttribute(OidcDemoConst.SA_ACCESS_TOKEN));
    final UserinfoResponse userInfoResponse = userInfoRequest.execute();
    if (userInfoResponse.isSuccess()) {
      DemoUtil.addToSessionLog(session, "Abfrage von Benutzerdaten beim STS erfolgreich");
    }
    else if (userInfoResponse.getErrorMessage() != null) {
      DemoUtil.addToSessionLog(session, "Abfrage von Benutzerdaten beim STS liefert Fehlermeldung: " + userInfoResponse.getErrorMessage());
    }
    else {
      DemoUtil.addToSessionLog(session, "Abfrage von Benutzerdaten beim STS fehlgeschlagen.");
    }

    session.setAttribute(OidcDemoConst.SA_USERINFO_TIME, new Date());
    session.setAttribute(OidcDemoConst.SA_USERINFO, userInfoResponse.isSuccess() ? userInfoResponse.getResponseString() : userInfoResponse.getErrorMessage());

    if (isInsightMode) {
      request.setAttribute(OidcDemoConst.ATTR_REDIRECT_URI, DemoUtil.getBaseUrl(request) + "?" + OidcDemoConst.QP_INSIGHT_MODE + "=1&" + OidcDemoConst.ATTR_INIT_TAB_BTN + "=" + OidcDemoConst.TAB_USERINFO);
      request.setAttribute(OidcDemoConst.ATTR_USERINFO_REQUEST, DemoHtmlUtil.getRequestToHtml(userInfoRequest.getHttpGetRequest()));
      request.setAttribute(OidcDemoConst.ATTR_USERINFO_RESPONSE, DemoHtmlUtil.jsonToHtml(userInfoResponse.getResponseJson()));
      request.getRequestDispatcher("/WEB-INF/userinfo-insight-mode.jsp").forward(request, response);
    }
    else {
      request.setAttribute(OidcDemoConst.ATTR_INIT_TAB_BTN, OidcDemoConst.TAB_USERINFO);
      request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
  }

}
