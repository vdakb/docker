package de.itk.auth.client.oidc;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Antwort eines STS auf eine Token-Anfrage (siehe {@link de.itk.auth.client.oidc.TokenRequest TokenRequest})
 *
 * @author Patrik Stellmann
 */
public class TokenResponse {

  private static final Logger logger = LogManager.getLogger();

  private boolean    success = false;
  private ObjectNode responseJson = null;
  private String     accessToken = null;
  private String     idToken = null;
  private String     refreshToken = null;
  private Date       expiresAt = null;
  private String     errorMessage = null;

  /**
   * Konstruktor im Falle einer erfolgreichen Anfrage
   *
   * @param responseStr Antwort des STS
   */
  public TokenResponse(String responseStr) {
    if (responseStr != null) {
      try {
        responseJson = (ObjectNode)(new ObjectMapper().readTree(responseStr));

        final String error = stringFromJsonNode(responseJson.get(OidcConsts.JN_ERROR));
        final String errorDesc = stringFromJsonNode(responseJson.get(OidcConsts.JN_ERROR_DESCRIPTION));

        if (error != null) {
          errorMessage = error + ((errorDesc != null) ? ": " + errorDesc : "");
          logger.error("Tokenanfrage beim STS liefert Fehlermeldung: {}", errorMessage);
        }
        else {
          final Date now = new Date();
          accessToken = stringFromJsonNode(responseJson.get(OidcConsts.JN_ACCESS_TOKEN));
          idToken = stringFromJsonNode(responseJson.get(OidcConsts.JN_ID_TOKEN));
          refreshToken = stringFromJsonNode(responseJson.get(OidcConsts.JN_REFRESH_TOKEN));

          final long expiresIn = intFromJsonNode(responseJson.get(OidcConsts.JN_EXPIRES_IN));
          expiresAt = new Date(now.getTime() + expiresIn * 1000);
          logger.debug("expiresAt: {}", expiresAt);

          success = true;
        }
      }
      catch (JsonProcessingException e) {
        logger.error(e, e);
        errorMessage = e.getMessage();
      }
    }
    else {
      errorMessage = "Keine Antwort empfangen";
    }
  }

  /**
   * Konstruktor im Falle einer Exception bei der Anfrage
   *
   * @param e Exception
   */
  public TokenResponse(Exception e) {
    success = false;
    errorMessage = e.getMessage();
  }

  /**
   * @return true, sofern die Anfrage beim STS erfolgreich war
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * Liefert das Access-Token aus der Antwort zurück.
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Liefert das ID-Token aus der Antwort zurück.
   */
  public String getIdToken() {
    return idToken;
  }

  /**
   * Liefert das Refresh-Token aus der Antwort zurück.
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Liefert den Zeitpunkt, an dem das Access-Token ablaufen wird. Diese Information stammt direkt aus der Antwort und nicht dem
   * Access-Token selbst.
   *
   * @return Ablaufzeitpunkt
   */
  public Date getExpiresAt() {
    return expiresAt;
  }

  /**
   * Liefert im Fehlerfall ({@link #isSuccess()} liefert false) einen Fehlertext
   *
   * @return Fehlertext
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Die Antwort des STS als JSON-Objekt, sogern es eine Antwort gab und diese geparst werden konnte.
   *
   * @return Antwort des STS
   */
  public ObjectNode getResponseJson() {
    return responseJson;
  }

  /**
   * Utility-Methode zum Konvertieren eines {@link com.fasterxml.jackson.databind.JsonNode JsonNode}-Objektes in einen String.
   *
   * @param jsonNode zu konvertierender Knoten
   * @return String oder null, , falls eine Konvertierung nicht möglich war.
   */
  protected static String stringFromJsonNode(JsonNode jsonNode) {
    if ((jsonNode != null) && (jsonNode.isTextual())) {
      return jsonNode.asText();
    }
    else {
      return null;
    }
  }

  /**
   * Utility-Methode zum Konvertieren eines {@link com.fasterxml.jackson.databind.JsonNode JsonNode}-Objektes in einen int-Wert.
   *
   * @param jsonNode zu konvertierender Knoten
   * @return int-Wert oder 0, falls eine Konvertierung nicht möglich war.
   */
  protected static int intFromJsonNode(JsonNode jsonNode) {
    if ((jsonNode != null) && (jsonNode.isIntegralNumber())) {
      return jsonNode.asInt();
    }
    else {
      return 0;
    }
  }
}
