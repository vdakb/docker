package oracle.iam.identity.icf.connector.keycloak.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Permission {
    view("view")
  , manage("manage")
  , impersonate("impersonate")
  , mapRoles("mapRoles")
  , manageGroupMembership("manageGroupMembership")
  ;

  //////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////

  public final String id;

  //////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>Permission</code> with a constraint value.
   **
   ** @param  id             the constraint name (used in REST schemas) of
   **                        the object.
   **                        <br>
   **                        Allowed object is {@link String}.
   */
  Permission(final String id) {
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Factory method to create a proper <code>Permission</code> constraint
   ** from the given string value.
   **
   ** @param  id             the string value the order constraint should be
   **                        returned for.
   **                        <br>
   **                        Allowed object is {@link String}.
   **
   ** @return                the <code>Permission</code> constraint.
   **                        <br>
   **                        Possible object is <code>Permission</code>.
   */
  public static Permission from(final String id) {
    for (Permission cursor : Permission.values()) {
      if (cursor.id.equals(id))
        return cursor;
    }
    throw new IllegalArgumentException(id);
  }

  @JsonCreator
  public static Permission origin(final String id) {
    for (Permission cursor : Permission.values()) {
      if (cursor.id.equals(id))
        return cursor;
    }
    throw new IllegalArgumentException(id);
  }

  @JsonValue
  public String value() {
    return id;
  }
}
