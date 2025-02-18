package de.itk.auth.client;

import java.io.IOException;

/** Wird verwendet, wenn im Rahmen der Initialisierung ein Fehler auftritt - in der Regel wegen ungültiger Konfiguration.
 * @author Patrik Stellmann
 *
 */
public class InitException extends IOException {

  private static final long serialVersionUID = 430499922517647153L;

  private final String    action;
  private final Exception exception;

  public InitException(String action, Exception exception) {
    super("Aktion: " + action + ", Fehler: " + exception.getMessage());

    this.action = action;
    this.exception = exception;
  }

  public String getAction() {
    return action;
  }

  public Exception getException() {
    return exception;
  }

}
