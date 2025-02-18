package de.itk.auth.client.oidc;

import java.io.IOException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Erzeugt eine Anfrage für Benutzerinformationen beim STS
 *
 * @author Patrik Stellmann
 */
public class UserinfoRequest extends HttpStsRequest {

  private static final Logger logger = LogManager.getLogger();

  private final HttpGet httpGetRequest;

  /**
   * Konstruktor
   *
   * @param userinfoEndpoint URL des STS für die Token-Anfrage.
   * @param accessToken An den STS zu übergebendes Access-Token (Muss von genau diesem ausgestellt worden sein.)
   */
  public UserinfoRequest(OidcClientStsConfig config, String accessToken) {
    super(config);
    this.httpGetRequest = new HttpGet(config.getUserinfoEndpoint());

    if (accessToken != null) {
      httpGetRequest.setHeader(HttpHeaders.AUTHORIZATION, OidcConsts.HEADER_PARAMETER_BEARER + " " + accessToken);
    }
  }

  /**
   * Führt die Anfrage beim STS aus.
   *
   * @return Antwort des STS
   */
  public UserinfoResponse execute() {
    logger.debug("request: {}", config.getUserinfoEndpoint());

    final CloseableHttpClient httpClient = createHttpClient();
    try {
      return new UserinfoResponse(httpClient.execute(httpGetRequest));
    }
    catch (Exception e) {
      logger.error(e, e);
      return new UserinfoResponse(e);
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
   * @return Aufruf als {@link org.apache.http.client.methods.HttpGet HttpGet}-Objekt
   */
  public HttpGet getHttpGetRequest() {
    return httpGetRequest;
  }
}
