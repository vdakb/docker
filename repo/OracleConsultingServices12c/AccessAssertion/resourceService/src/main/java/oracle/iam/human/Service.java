package oracle.iam.human;

import javax.ws.rs.ApplicationPath;

import javax.ws.rs.core.Application;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** Defines the components of a JAX-RS application and supplies additional
 ** metadata. 
 */
// It's good practice to include a version number in the path to REST web
// services so you can have multiple versions deployed at once.
// That way consumers don't need to upgrade right away if things are working for
// them.
@ApplicationPath("api/v1")
public class Service extends Application {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Service() {
    // ensure inheritance
    super();
  }
}