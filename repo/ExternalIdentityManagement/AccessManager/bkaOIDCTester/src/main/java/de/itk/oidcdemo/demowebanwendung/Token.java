package de.itk.oidcdemo.demowebanwendung;

import java.security.interfaces.RSAPublicKey;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import java.util.logging.Logger;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Token {

  public static final String CLAIM_AUTHORIZED_PARTY = "azp";

  private static final String THIS = Token.class.getName();

  private static final Logger LOGGER = Logger.getLogger(THIS);

  private final String     raw;
  private final DecodedJWT decoded;

  public Token(String raw) {
    this.raw = raw;
    decoded = decodeJwt(raw);
  }

  private static DecodedJWT decodeJwt(String raw) {
    try {
      return JWT.decode(raw);
    }
    catch (JWTDecodeException e) {
      return null;
    }
  }

  public boolean isJwt() {
    return (decoded != null);
  }

  public ObjectNode getHeaderAsJson() {
    if ((decoded != null) && (decoded.getHeader() != null)) {
      try {
        final String       header = new String(Base64.getUrlDecoder().decode(decoded.getHeader()));
        final ObjectMapper objectMapper = new ObjectMapper();
        return (ObjectNode)objectMapper.readTree(header);
      }
      catch (JsonProcessingException e) {
        LOGGER.severe(e.getLocalizedMessage());
        return null;
      }
    }
    else {
      return null;
    }
  }

  public ObjectNode getPayloadAsJson() {
    if ((decoded != null) && (decoded.getPayload() != null)) {
      try {
        final String       payload = new String(Base64.getUrlDecoder().decode(decoded.getPayload()));
        final ObjectMapper objectMapper = new ObjectMapper();
        return (ObjectNode)objectMapper.readTree(payload);
      }
      catch (Exception e) {
        LOGGER.severe(String.format("Error converting payload (%s) to JSON: %s%s", decoded.getPayload(), e.getMessage(), e.getStackTrace()));
        return null;
      }
    }
    else {
      return null;
    }
  }

  public Date getExpires() {
    if (decoded != null) {
      return decoded.getExpiresAt();
    }
    else {
      return new Date(0);
    }
  }

  public String getRaw() {
    return raw;
  }

  public DecodedJWT getJwt() {
    return decoded;
  }

  public static Token createFromJsonNode(JsonNode jsonNode) {
    if ((jsonNode != null) && (jsonNode.isTextual())) {
      return new Token(jsonNode.asText());
    }
    else {
      return null;
    }
  }

  public boolean isExpired() {
    if (decoded != null) {
      final Date expires = getExpires();
      final Date now = new Date();
      return ((expires == null) || (expires.before(now)));
    }
    else {
      return true;
    }
  }

  public boolean isValidAlgorithm() {
    if ((decoded != null) && (decoded.getAlgorithm() != null)) {
      return decoded.getAlgorithm().equals("RS256");
    }
    else {
      return false;
    }
  }

  public boolean isAzp(String clientId) {
    if ((decoded != null) && (decoded.getClaim(CLAIM_AUTHORIZED_PARTY) != null)) {
      return decoded.getClaim(CLAIM_AUTHORIZED_PARTY).asString().equals(clientId);
    }
    else {
      return false;
    }
  }

  public boolean containsScope(String scopeClaim, String requiredScope) {
    if ((decoded != null) && (decoded.getClaim(scopeClaim) != null) && (!decoded.getClaim(scopeClaim).isNull())) {
      final String scopeStr = decoded.getClaim(scopeClaim).asString();
      if (scopeStr != null) {
        final String regex = "(^|.*\\s)" + requiredScope + "(\\s.*|$)";
        return scopeStr.matches(regex);
      }
      else {
        final List<String> scopeList = decoded.getClaim(scopeClaim).asList(String.class);
        if (scopeList != null) {
          return scopeList.contains(requiredScope);
        }
        else {
          return false;
        }
      }
    }
    else {
      return false;
    }
  }

  public boolean isSignatureValid(JwkProvider jwkProvider) {
    if ((decoded != null) && (jwkProvider != null)) {
      try {
        final Jwk         jwk = jwkProvider.get(decoded.getKeyId());
        final Algorithm   algorithm = Algorithm.RSA256((RSAPublicKey)jwk.getPublicKey(), null);
        final JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(decoded);
        return true;
      }
      catch (JWTVerificationException | JwkException e) {
        LOGGER.severe(e.getLocalizedMessage());
        return false;
      }
    }
    else {
      return false;
    }
  }
}
