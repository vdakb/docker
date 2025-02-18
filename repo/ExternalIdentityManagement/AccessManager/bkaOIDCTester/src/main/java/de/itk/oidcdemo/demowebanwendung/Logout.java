package de.itk.oidcdemo.demowebanwendung;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.itk.auth.client.demoutil.DemoUtil;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/logout") public class Logout extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    final HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    final boolean isInsightMode = "1".equals(request.getParameter(OidcDemoConst.QP_INSIGHT_MODE));
    if (isInsightMode) {
      request.getRequestDispatcher("/WEB-INF/logout.jsp").forward(request, response);
    }
    else {
      response.sendRedirect(DemoUtil.getBaseUrl(request));
    }
  }

}
