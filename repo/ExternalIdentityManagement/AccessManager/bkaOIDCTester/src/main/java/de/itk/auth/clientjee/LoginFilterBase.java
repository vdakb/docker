package de.itk.auth.clientjee;

import java.io.IOException;

import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Basisklasse zum Implementieren eines Login-Filters, der vor jedem Zugriff die Authentifizierung
 * sicherstellt.
 *
 * @author Patrik Stellmann
 */
public abstract class LoginFilterBase
  implements Filter {

  /**
   * @see Filter#destroy()
   */
  public void destroy() {
    // <empty>
  }

  /**
   * Muss eine Liste von URI-Suffixen zum identifizieren von Aufrufen liefern zurückgeben, die nicht durch den
   * Filter abgefangen werden sollen. Dies sind insbesondere Ressourcen wie *.js und *.css.
   *
   * @return Liste nicht zu filternder URI-Suffixe
   */
  protected abstract List<String> getExcludeUriSuffix();

  /**
   * Diese Methode wird aufgerufen, nachdem ein Logout erfolgt ist. die Implementierung sollte eine entsprechende
   * Seite anzeigen, die den Nutzer über den erfolgreichen Logout informiert.
   *
   * @param httpRequest
   * @param httpResponse
   * @throws ServletException
   * @throws IOException
   */
  protected abstract void handleLogoutDone(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
    throws ServletException, IOException;

  /**
   * Prüft, ob der übergebene Aufruf durch den Filter gemäß Methode {@link #getExcludeUriSuffix()} zu
   * ignorieren ist.
   *
   * @param httpRequest Aufruf, der zu prüfen ist.
   * @return true, sofern der Aufruf nicht zu prüfen ist.
   */
  protected boolean isUrlExcluded(HttpServletRequest httpRequest) {
    final List<String> excludeUriSuffix = getExcludeUriSuffix();
    if (excludeUriSuffix != null) {
      final String suffix = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

      boolean          excluded = false;
      Iterator<String> it = excludeUriSuffix.iterator();
      while ((!excluded) && it.hasNext()) {
        excluded = suffix.matches(it.next());
      }
      return excluded;
    }
    else {
      return false;
    }
  }

  /**
   * Wird aufgrufen, wenn der Servlet-Path für den Logout aufgerufen wurde. Sofern eine Session vorliegt, wird
   * diese terminiert. In jedem Fall wird die Methode
   * {@link #handleLogoutDone(HttpServletRequest, HttpServletResponse)} aufgerufen.
   *
   * @param httpRequest
   * @param httpResponse
   * @throws ServletException
   * @throws IOException
   */
  protected void handleLogoutRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
    throws ServletException, IOException {
    final HttpSession session = httpRequest.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    handleLogoutDone(httpRequest, httpResponse);
  }

  /**
   * Wird aufgrufen, wenn der Servlet-Path für den Logout-STS aufgerufen wurde.
   *
   * @param httpRequest
   * @param httpResponse
   * @throws IOException
   */
  protected abstract void handleLogoutStsRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
    throws IOException;

}
