package de.itk.auth.client.oidc;

import javax.ws.rs.core.UriBuilder;

/**
 * Erzeugt eine URL zum Ausloggen beim STS.
 *
 * @author Patrik Stellmann
 */
public class LogoutRequestUrl {

  private String endSessionEndpoint = null;
  private String redirectUri = null;

  /**
   * Konstruktor
   *
   * @param endSessionEndpoint URL des STS zum Ausloggen
   */
  public LogoutRequestUrl(String endSessionEndpoint) {
    this.endSessionEndpoint = endSessionEndpoint;
  }

  /**
   * Setzt die Redirect-URI, auf die der STS nach dem Logout verweisen soll.
   *
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public LogoutRequestUrl setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
    return this;
  }

  /**
   * Erzeugt die URI.
   *
   * @return die erzeugte URI
   */
  public String build() {
    final UriBuilder ub = UriBuilder.fromUri(endSessionEndpoint);

    ub.queryParam(OidcConsts.QP_REDIRECT_URI, (redirectUri == null) ? "" : redirectUri);

    return ub.build().toString();
  }
}
