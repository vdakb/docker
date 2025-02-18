package de.itk.auth.client.oidc;

import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Erzeugt eine URL für eine Authentifizierungsanfrage bei einem STS für den Authorization Code Flow, ggf. mit PKCE. Die erzeugte
 * URL muss in einem Redirect an den Browser übergeben werden.
 *
 * @author Patrik Stellmann
 */
public class AuthorizationCodeRequestUrl {

  private static final Logger logger = LogManager.getLogger();

  private final OidcClientStsConfig config;

  private String authorizationEndpoint = null;
  private String scope = null;
  private String clientId = null;
  private String redirectUri = null;
  private String state = null;
  private String codeVerifier = null;

  private Map<String, String> customParameterList = new HashMap<>();

  /**
   * Konstruktor<br> Erzeugt intern einen zufälligen Code-Verifier, der über die Methode {@link #getCodeVerifier()} abgefragt werden
   * kann.
   *
   * @param authorizationEndpoint
   */
  public AuthorizationCodeRequestUrl(OidcClientStsConfig config) {
    this.config = config;
    authorizationEndpoint = config.getAuthorizationEndpoint();
    codeVerifier = generateCodeVerifier();
  }

  /**
   * Setzt explizit den Authoization-Endpoint. Per Default wird der aus der Konfiguration des Konstruktors übernommen.
   *
   * @param URL des Authoization-Endpoints
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public AuthorizationCodeRequestUrl setAuthorizationEndpoint(String authorizationEndpoint) {
    this.authorizationEndpoint = authorizationEndpoint;
    return this;
  }

  /**
   * Setzt den Scope für die Anfrage.
   *
   * @param scope
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public AuthorizationCodeRequestUrl setScope(String scope) {
    this.scope = scope;
    return this;
  }

  /**
   * Setzt die Client-ID, mit der die Anwendung beim STS registriert ist.
   *
   * @param clientId
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public AuthorizationCodeRequestUrl setClientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  /**
   * Setzt die Redirect-URI, auf die der STS bei der Antwort verweisen soll.
   *
   * @param redirectUri
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public AuthorizationCodeRequestUrl setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
    return this;
  }

  /**
   * Setzt den Status, der beim Redirect vom STS zurückgegeben werden soll.
   *
   * @param state
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public AuthorizationCodeRequestUrl setState(String state) {
    this.state = state;
    return this;
  }

  /**
   * Erzeugt die URI.
   *
   * @return die erzeugte URI
   */
  public String build() {
    final UriBuilder ub = UriBuilder.fromUri(authorizationEndpoint);

    ub.queryParam(OidcConsts.QP_SCOPE, scope);
    ub.queryParam(OidcConsts.QP_RESPONSE_TYPE, OidcConsts.RESPONSE_TYPE_CODE);
    ub.queryParam(OidcConsts.QP_CLIENT_ID, (clientId == null) ? "" : clientId);
    ub.queryParam(OidcConsts.QP_REDIRECT_URI, (redirectUri == null) ? "" : redirectUri);
    ub.queryParam(OidcConsts.QP_CODE_CHALLENGE_METHOD, OidcConsts.CODE_CHALLENGE_METHOD_S256);
    ub.queryParam(OidcConsts.QP_CODE_CHALLENGE, generateCodeChallange(codeVerifier));
    ub.queryParam(OidcConsts.QP_STATE, (state == null) ? "" : state);
    if (config.getDomain() != null) {
      ub.queryParam(OidcConsts.QP_DOMAIN, config.getDomain());
    }
    for (Entry<String, String> entry : customParameterList.entrySet()) {
      ub.queryParam(entry.getKey(), entry.getValue());
    }

    final String uri = ub.build().toString();
    logger.info("redirect-URI: {}", uri);

    return uri;
  }

  /**
   * Ermittelt den zufällig generierten Code-Verifier, der dem STS bei der nachgelagerten Token-Anfrage übergeben werden muss.
   *
   * @return Code-Verifier
   */
  public String getCodeVerifier() {
    return codeVerifier;
  }

  /**
   * Erzeugt eine zufällige Zeichenfolge, die als Code-Verifier genutzt werden kann.
   *
   * @return
   */
  public static String generateCodeVerifier() {
    final SecureRandom secureRandom = new SecureRandom();
    final byte[]       codeVerifier = new byte[32];
    secureRandom.nextBytes(codeVerifier);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
  }

  /**
   * Ermittelt für einen übergebenen Code-Verifier eine Code-Challenge, indem der Hash-Wert daraus gebildet wird.
   *
   * @param codeVerifier Code-Verifier, aus dem die Code-Challenge erzeugt werden soll
   * @return Code-Challenge
   */
  public static String generateCodeChallange(String codeVerifier) {
    final byte[]  bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    }
    catch (NoSuchAlgorithmException e) {
      logger.error(e, e); // sollte niemals auftreten.
      return null;
    }
    messageDigest.update(bytes, 0, bytes.length);
    final byte[] digest = messageDigest.digest();
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }

  public void addParameter(String name, String value) {
    customParameterList.put(name, value);
  }
}
