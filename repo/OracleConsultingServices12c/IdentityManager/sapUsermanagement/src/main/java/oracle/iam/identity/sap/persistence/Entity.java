package oracle.iam.identity.sap.persistence;

public class Entity {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** query the specified table in the R/3 system and return the results in an
   ** unformatted table.
   */
  private final String   function;
  private final String   object;
  private final String   segment;
  private final String[] details;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Entity</code> with the specified properties.
   **
   ** @param  function           the name to query the specified table in the
   **                            R/3 system and return the results in an
   **                            unformatted table.
   ** @param  object             the name of the object to access.
   */
  public Entity(final String function, final String object, final String segment, final String[] details) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.function = function;
    this.object   = object;
    this.segment  = segment;
    this.details  = details;
  }
}
