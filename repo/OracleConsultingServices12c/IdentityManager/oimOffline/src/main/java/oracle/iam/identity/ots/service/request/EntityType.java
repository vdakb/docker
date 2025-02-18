package oracle.iam.identity.ots.service.request;

////////////////////////////////////////////////////////////////////////////////
// enum EntityType
// ~~~~ ~~~~~~~~~~
/**
 ** The <code>EntityType</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Adapter Factory which
 ** handles data delivered to an Offline Target System.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public enum EntityType {

  APPLICATION("Application")
, ENTITLEMENT("Entitlement");

  private final String name;

  private EntityType(final String name) {
      this.name = name;
  }
}