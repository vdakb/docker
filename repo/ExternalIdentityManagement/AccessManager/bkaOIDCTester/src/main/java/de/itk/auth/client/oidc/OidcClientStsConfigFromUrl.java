package de.itk.auth.client.oidc;

import java.io.IOException;

import java.net.MalformedURLException;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.SigningKeyNotFoundException;
import com.auth0.jwk.UrlJwkProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.itk.auth.client.AuthClientUtil;
import de.itk.auth.client.InitException;

/**
 * Fragt die STS-Konfiguration für einen Client direkt beim STS über einen entspr. Endpunkt ab, der gemäß OIDC-Standard von jedem
 * OIDC-STS bereitgestellt werden muss.
 *
 * @author Patrik Stellmann
 */
public abstract class OidcClientStsConfigFromUrl
  implements OidcClientStsConfig {

  private static final Logger logger = LogManager.getLogger();

  protected final String         stsUrl;
  protected final String         proxyIp;
  protected final Integer        proxyPort;
  protected final String         domain;
  protected final ObjectNode     oidcConfig;
  protected final UrlJwkProvider jwkProvider;

  /**
   * Konstruktor
   *
   * @param stsUrl URL des STS, an die "/.well-known/openid-configuration" angehängt wird, um die Konfiguration in Form eine
   * JSON-Struktur auszulesen.
   * @throws ServletException Wird geworfen, wenn es beim Auslesen der Konfiguration ein Problem gab.
   */
  protected OidcClientStsConfigFromUrl(String proxyIp, Integer proxyPort, String stsUrl, String domain)
    throws InitException {

    this.proxyIp = proxyIp;
    this.proxyPort = proxyPort;
    this.stsUrl = stsUrl;
    this.domain = domain;

    final String        configUrl = stsUrl + "/.well-known/openid-configuration";
    final HttpGet       configRequest = new HttpGet(configUrl);
    CloseableHttpClient httpClient = null;
    String              oidcConfigStr;
    logger.info("config url: {}", configUrl);
    try {
      if ((proxyIp != null) && (!proxyIp.isEmpty())) {
        final HttpRoutePlanner  routePlanner = new DefaultProxyRoutePlanner(new HttpHost(proxyIp, proxyPort));
        final HttpClientBuilder clientBuilder = HttpClients.custom().setRoutePlanner(routePlanner);
        httpClient = clientBuilder.build();
      }
      else {
        httpClient = HttpClients.createDefault();
      }
      oidcConfigStr = EntityUtils.toString(httpClient.execute(configRequest).getEntity());
    }
    catch (Exception e) {
      logger.error(e, e);
      throw new InitException("Abfrage der OpenID Connect Configuration des STS: " + configRequest.getURI().toString(), e);
    }
    finally {
      if (httpClient != null) {
        try {
          httpClient.close();
        }
        catch (IOException e) {
          // <empty>
        }
      }
    }
    logger.debug("Antwort vom STS: {}", oidcConfigStr);

    try {
      oidcConfig = (ObjectNode)(new ObjectMapper()).readTree(oidcConfigStr);
    }
    catch (JsonProcessingException e) {
      logger.error(e, e);
      throw new InitException("Parsen der OpenID Connect Configuration des STS fehlgeschlagen: " + oidcConfigStr, e);
    }

    final JsonNode errorNode = oidcConfig.get("error");
    if (errorNode != null) {
      final String errorText = errorNode.asText();
      logger.error("Fehler bei Abfrage der STS-Konfiguration von {}: {}", configUrl, errorText);
      throw new InitException("Abfrage der STS-Konfiguration von " + configUrl, new Exception(errorText));
    }

    final String jwksUri = oidcConfig.get("jwks_uri").asText();
    try {
      jwkProvider = AuthClientUtil.createUrlJwkProvider(jwksUri, proxyIp, proxyPort, domain);
    }
    catch (MalformedURLException | SigningKeyNotFoundException e) {
      logger.error(e, e);
      throw new InitException("Abfrage der JWKS des STS: " + jwksUri, e);
    }
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getAuthorizationEndpoint()
   */
  public String getAuthorizationEndpoint() {
    return oidcConfig.get("authorization_endpoint").asText("");
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getTokenEndpoint()
   */
  public String getTokenEndpoint() {
    return oidcConfig.get("token_endpoint").asText("");
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getTokenEndpoint()
   */
  public String getTokenExchangeEndpoint() {
    return oidcConfig.get("token_endpoint").asText("");
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getUserinfoEndpoint()
   */
  public String getUserinfoEndpoint() {
    return oidcConfig.get("userinfo_endpoint").asText("");
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getEndSessionEndpoint()
   */
  public String getEndSessionEndpoint() {
    return oidcConfig.get("end_session_endpoint").asText("");
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getJwkProvider()
   */
  public JwkProvider getJwkProvider() {
    return jwkProvider;
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getDomain()
   */
  @Override
  public String getDomain() {
    return domain;
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#hasProxy()
   */
  @Override
  public boolean hasProxy() {
    return ((proxyIp != null) && (!proxyIp.isEmpty()));
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getProxyIp()
   */
  @Override
  public String getProxyIp() {
    return proxyIp;
  }

  /**
   * (non-Javadoc)
   *
   * @see de.itk.auth.client.oidc.OidcClientStsConfig#getProxyPort()
   */
  @Override
  public Integer getProxyPort() {
    return proxyPort;
  }
}
