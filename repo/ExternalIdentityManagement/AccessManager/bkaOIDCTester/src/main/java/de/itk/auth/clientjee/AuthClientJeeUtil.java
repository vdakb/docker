package de.itk.auth.clientjee;

import javax.servlet.http.HttpServletRequest;

public class AuthClientJeeUtil {

  private AuthClientJeeUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Utility-Methode zum Ermitteln der Basis-URL aus dem Request, also ohne Servlet-Pfad.
   *
   * @param request
   * @return Basis-URL
   */
  public static String getBaseUrl(HttpServletRequest request) {
    String scheme = request.getScheme() + "://";
    String serverName = request.getServerName();
    String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
    String contextPath = request.getContextPath();
    return scheme + serverName + serverPort + contextPath;
  }

  /**
   * Utility-Methode zum Ermitteln der gesamten Aufruf-URL inkl. Query-Parameter aus dem Request.
   *
   * @param request
   * @return Aufruf-URL
   */
  public static String getFullRequestUrl(HttpServletRequest request) {
    final String requestUrl = request.getRequestURL().toString();
    final String queryString = request.getQueryString();

    if (queryString == null) {
      return requestUrl;
    }
    else {
      return requestUrl + "?" + queryString;
    }
  }

}
