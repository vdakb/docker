package de.itk.auth.client.oidc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Antwort eines STS auf ein {@link de.itk.auth.client.oidc.UserinfoRequest UserInfoRequest}
 *
 * @author Patrik Stellmann
 */
public class UserinfoResponse {

  private static final Logger logger = LogManager.getLogger();

  private boolean    success = false;
  private String     errorMessage = null;
  private String     responseStr = null;
  private ObjectNode responseJson = null;

  /**
   * Konstruktor im Falle einer erfolgreichen Anfrage
   *
   * @param httpResponse Antwort des STS
   */
  public UserinfoResponse(HttpResponse httpResponse) {

    try {
      responseStr = EntityUtils.toString(httpResponse.getEntity());
      logger.debug("response entity: {}", responseStr);
    }
    catch (ParseException | IOException e) {
      logger.error(e, e);
      errorMessage = e.getMessage();
    }

    if (responseStr != null) {
      parseResponseStr();
    }
  }

  private void parseResponseStr() {
    try {
      final JsonNode json = (new ObjectMapper()).readTree(responseStr);
      if (json instanceof ObjectNode) {
        responseJson = (ObjectNode)json;
        if (responseJson.has(OidcConsts.JN_ERROR)) {
          errorMessage = responseJson.get(OidcConsts.JN_ERROR).textValue();
          if (responseJson.has(OidcConsts.JN_ERROR_DESCRIPTION)) {
            errorMessage += " /" + responseJson.get(OidcConsts.JN_ERROR_DESCRIPTION).textValue();
          }
        }
        else if (responseJson.has(OidcConsts.JN_SUB)) {
          success = true;
        }
        else {
          errorMessage = "Zurückgegebenes JSON-Objekt enthält kein sub-Claim.";
        }
      }
      else {
        errorMessage = "Antwort ist kein gültiges JSON-Objekt.";
      }
    }
    catch (JsonProcessingException e) {
      logger.error(e, e);
      errorMessage = e.getMessage();
    }
  }

  /**
   * Konstruktor im Falle einer Exception bei der Anfrage
   *
   * @param e Exception
   */
  public UserinfoResponse(Exception e) {
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
   * Die Antwort des STS als String, sofern es eine Antwort gab.
   *
   * @return Antwort des STS
   */
  public String getResponseString() {
    return responseStr;
  }
}
