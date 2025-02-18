package de.itk.auth.clientjee;

import java.io.IOException;

import java.security.interfaces.RSAPublicKey;

import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.logging.Logger;

/**
 * Servlet Filter implementation class AuthFilter
 */
public abstract class JwtAuthFilter
  implements Filter {

  private static final String THIS = JwtAuthFilter.class.getName();

  private static final Logger LOGGER = Logger.getLogger(THIS);

  public static final String ATTR_SUB = "sub";
  public static final String ATTR_TOKEN = "token";

  private static final String BEARER = "bearer ";

  /**
   * Default constructor.
   */
  protected JwtAuthFilter() {
    // <empty>
  }

  /**
   * @see Filter#destroy()
   */
  public void destroy() {
    // <empty>
  }

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {

    boolean isAuthorized = false;

    final HttpServletRequest  httpRequest = (HttpServletRequest)request;
    final HttpServletResponse httpResponse = (HttpServletResponse)response;
    final String              authHeader = httpRequest.getHeader("authorization");

    if ((getRequiredScope() == null) || (getJwkProvider() == null) || (getScopeClaim() == null)) {
      LOGGER.severe("Initialisierungsfehler!");
      httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Initialisierungsfehler");
    }
    else if (authHeader == null) {
      LOGGER.info("Aufruf ohne authorization header!");
    }
    else if (!authHeader.toLowerCase().startsWith(BEARER)) {
      LOGGER.info(String.format("Aufruf ohne bearer token im authorization header!: '%s'", authHeader));
    }
    else {
      final String token = authHeader.substring(BEARER.length());

      isAuthorized = isTokenValid(request, token);

    }
    if (isAuthorized) {
      chain.doFilter(request, response);
    }
    else {
      httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  protected abstract String getScopeClaim();

  protected abstract JwkProvider getJwkProvider();

  protected abstract String getRequiredScope();

  /**
   * Checks if the token is valid.
   * @param request the request
   * @param token the JWT in encoded format
   * @return true if the token is valid.
   */
  private boolean isTokenValid(ServletRequest request, final String token) {
    boolean isAuthorized = false;
    try {
      final String      scopeClaim = getScopeClaim();
      final String      requiredScope = getRequiredScope();
      final DecodedJWT  decoded = JWT.decode(token);
      final Jwk         jwk = getJwkProvider().get(decoded.getKeyId());
      final Algorithm   algorithm = Algorithm.RSA256((RSAPublicKey)jwk.getPublicKey(), null);
      final JWTVerifier verifier = JWT.require(algorithm).build();
      verifier.verify(decoded);

      final String       scopeStr = decoded.getClaim(scopeClaim).asString();
      final List<String> scopeList = decoded.getClaim(scopeClaim).asList(String.class);
      if (decoded.getClaim(scopeClaim).isNull()) {
        LOGGER.severe(String.format("Kein Scope (%s) im Token enthalten.", scopeClaim));
      }
      else if ((scopeStr != null) && (!scopeStr.matches("(^|.*\\s)" + requiredScope + "(\\s.*|$)"))) {
        LOGGER.severe(String.format("Scope (%s) im Token ('%s') enthält nicht den benötigten Scope : '%s'", scopeClaim, scopeStr, requiredScope));
      }
      else if ((scopeList != null) && (!scopeList.contains(requiredScope))) {
        LOGGER.severe(String.format("Scope (%s) im Token ('%s') enthält nicht den benötigten Scope : '%s'", scopeClaim, scopeList, requiredScope));
      }
      else if ((decoded.getSubject() == null) || (decoded.getSubject().isEmpty())) {
        LOGGER.severe("Kein oder ein leerer sub-Claim im Token.");
      }
      else {
        LOGGER.fine(String.format("Sub im Token: '%s'", decoded.getSubject()));
        isAuthorized = true;
        request.setAttribute(ATTR_TOKEN, token);
        request.setAttribute(ATTR_SUB, decoded.getSubject());
      }

    }
    catch (JwkException | JWTVerificationException e) {
      LOGGER.throwing(THIS, "isTokenValid", e);
    }
    return isAuthorized;
  }
}