package oracle.hst.foundation.resource;

////////////////////////////////////////////////////////////////////////////////
// class LoggingBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class LoggingBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // Logging Level
    { "OFF",     "OFF    " }
  , { "ALL",     "ALL    " }
  , { "FATAL",   "FATAL  " }
  , { "SEVERE",  "ERROR  " }
  , { "WARNING", "WARNING" }
  , { "CONFIG",  "INFO   " }
  , { "INFO",    "INFO   " }
  , { "FINE",    "DEBUG  " }
  , { "FINER",   "DEBUG  " }
  , { "FINEST",  "DEBUG  " }
  };

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  public Object[][] getContents() {
    return CONTENT;
  }
}