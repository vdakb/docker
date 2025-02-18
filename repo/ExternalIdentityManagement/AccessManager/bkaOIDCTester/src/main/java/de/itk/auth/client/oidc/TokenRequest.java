package de.itk.auth.client.oidc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Basisklasse für Token-Requests bei einem STS.
 *
 * @author Patrik Stellmann
 */
public abstract class TokenRequest extends HttpStsRequest {

  private static final Logger logger = LogManager.getLogger();

  private final String grantType;

  private String clientId = null;
  private String clientSecret = null;
  private String redirectUri = null;

  /**
   * Konstruktor
   *
   * @param config Konfiguration des STS, über den die Token-Anfrage gestellt werden muss.
   * @param grantType der zu verwendende Grant-Type
   */
  protected TokenRequest(OidcClientStsConfig config, String grantType) {
    super(config);
    this.grantType = grantType;
  }

  /**
   * Setzt die Client-ID, mit der die Anwendung beim STS registriert ist.
   *
   * @param clientId
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public TokenRequest setClientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  /**
   * Setzt das Client-Secret, mit der die Anwendung beim STS registriert ist.
   *
   * @param clientSecret
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public TokenRequest setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
    return this;
  }

  /**
   * Setzt die Redirect-URI, auf die der STS bei seiner Antwort verweisen soll.
   *
   * @param redirectUri
   * @return Die Instanz selbst, um kompakt mehrere set-Methoden nacheinander aufrufen zu können.
   */
  public TokenRequest setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
    return this;
  }

  /**
   * Führt die Anfrage beim STS aus.
   *
   * @return Antwort des STS
   */
  public TokenResponse execute() {
    final HttpPost            tokenRequest = new HttpPost(getUrl());
    final List<NameValuePair> parameters = getParameters();
    final List<NameValuePair> headerParameters = getHeaderParameters();

    logger.debug("TokenRequest: {}", getUrl());
    for (NameValuePair entry : headerParameters) {
      logger.debug("  Header: {} = {}", entry.getName(), entry.getValue());
      tokenRequest.setHeader(entry.getName(), entry.getValue());
    }
    for (NameValuePair entry : parameters) {
      logger.debug("  {} = {}", entry.getName(), entry.getValue());
    }

    try {
      tokenRequest.setEntity(new UrlEncodedFormEntity(parameters));
    }
    catch (UnsupportedEncodingException e) {
      logger.error(e, e);
      return new TokenResponse(e);
    }

    return executeRequest(tokenRequest);
  }

  /**
   * Liefert die Liste der Parameter zurück.
   *
   * @return Query-Parameter als Liste
   */
  public List<NameValuePair> getParameters() {
    final List<NameValuePair> parameters = new ArrayList<>();

    addParameter(parameters, OidcConsts.QP_GRANT_TYPE, grantType);
    addParameter(parameters, OidcConsts.QP_CLIENT_ID, clientId);
    addParameter(parameters, OidcConsts.QP_CLIENT_SECRET, clientSecret);
    addParameter(parameters, OidcConsts.QP_REDIRECT_URI, redirectUri);

    return parameters;
  }

  /**
   * Liefert die Liste der HeaderParameter zurück.
   *
   * @return Query-Parameter als Liste
   */
  public List<NameValuePair> getHeaderParameters() {
    final List<NameValuePair> parameters = new ArrayList<>();

    if ((config.getDomain() != null) && (!config.getDomain().isEmpty())) {
      addParameter(parameters, OidcConsts.HEADER_PARAMETER_DOMAIN, config.getDomain());
    }

    return parameters;
  }

  /**
   * Gibt die URL des STS zurück.
   *
   * @return
   */
  public String getUrl() {
    return config.getTokenEndpoint();
  }

  /**
   * Führt den übergebenen HTTP-Post-Request aus und erneugt aus der Antwort ein
   * {@link de.itk.auth.client.oidc.TokenResponse TokenResponse}-Objekt.
   *
   * @param tokenRequest Request an STS
   * @return Antwort des STS
   */
  protected TokenResponse executeRequest(HttpPost tokenRequest) {
    final CloseableHttpClient httpClient = createHttpClient();
    try {
      final HttpResponse response = httpClient.execute(tokenRequest);
      logger.debug("response: {}", response);
      final HttpEntity entity = response.getEntity();
      logger.debug("response entity: {}", entity);
      final String string = EntityUtils.toString(entity);
      logger.debug("response content: {}", string);
      return new TokenResponse(string);
    }
    catch (IOException e) {
      logger.error(e, e);
      return new TokenResponse(e);
    }
    finally {
      try {
        httpClient.close();
      }
      catch (IOException e) {
        // <empty>
      }
    }
  }

  /**
   * Utility-Funktion zum Hinzufügen eines Parameters zu einer Liste.
   *
   * @param parameters Liste
   * @param name Name des Parameters
   * @param value Wert des Parameters
   */
  protected static void addParameter(List<NameValuePair> parameters, String name, String value) {
    if (value != null) {
      parameters.add(new BasicNameValuePair(name, value));
    }
  }
}
