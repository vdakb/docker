/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities

    File        :   ServerType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerType.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

////////////////////////////////////////////////////////////////////////////////
// class ServerType
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>ServerType</code> defines the attribute restriction on values that can
 ** be passed to a {@link ServerContext}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerType extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String JBOSS       = "jboss";
  private static final String OC4J        = "oc4j";
  private static final String HTTP        = "http";
  private static final String WEBLOGIC    = "weblogic";
  private static final String WEBSPEHERE  = "websphere";
  private static final String GLASSFISH   = "glassfish";

  // the names of the allowed server types in alphabetical order
  private static final String[] registry = {
    JBOSS
  , OC4J
  , HTTP
  , WEBLOGIC
  , WEBSPEHERE
  , GLASSFISH
  };

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class JBoss
  // ~~~~~ ~~~~~
  /**
   ** <code>JBoss</code> defines the attribute restriction to {@link #JBOSS}.
   */
  public static final class JBoss extends ServerType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>JBoss</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public JBoss(){
      // ensure inheritance
      super(JBOSS);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class OC4J
  // ~~~~~ ~~~~
  /**
   ** <code>OC4J</code> defines the attribute restriction to {@link #OC4J}.
   */
  public static final class OC4J extends ServerType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>OC4J</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public OC4J(){
      // ensure inheritance
      super(OC4J);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class HTTP
  // ~~~~~ ~~~~
  /**
   ** <code>HTTP</code> defines the attribute restriction to
   ** {@link #HTTP}.
   */
  public static final class HTTP extends ServerType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>HTTP</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public HTTP(){
      // ensure inheritance
      super(HTTP);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class WebLogic
  // ~~~~~ ~~~~~~~~
  /**
   ** <code>WebLogic</code> defines the attribute restriction to
   ** {@link #WEBLOGIC}.
   */
  public static final class WebLogic extends ServerType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>WebLogic</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public WebLogic(){
      // ensure inheritance
      super(WEBLOGIC);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class WebSphere
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>WebSphere</code> defines the attribute restriction to
   ** {@link #WEBSPEHERE}.
   */
  public static final class WebSphere extends ServerType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>WebSphere</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public WebSphere(){
      // ensure inheritance
      super(WEBSPEHERE);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Glassfish
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Glassfish</code> defines the attribute restriction to
   ** {@link #GLASSFISH}.
   */
  public static final class Glassfish extends ServerType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Glassfish</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Glassfish(){
      // ensure inheritance
      super(GLASSFISH);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerType</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServerType(){
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerType</code> type with the specified value
   ** property.
   **
   ** @param  type               the type property value to set.
   **                            Allowed object is {@link String}.
   */
  protected ServerType(final String type){
    // ensure inheritance
    super();

    // initialize instance
    setValue(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the server where a task will connecting to.
   **
   ** @return                    the type of the server where a task will
   **                            connecting to.
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return super.getValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   **                            Possible object is array of {@link String}s.
   */
  @Override
  public String[] getValues() {
    return registry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWebLogic
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Oracle WebLogic Server.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Oracle WebLogic Server.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isWebLogic() {
    return WEBLOGIC.equals(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWebsphere
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** IBM WebSphere Server.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is IBM WebSphere Server.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isWebsphere() {
    return WEBSPEHERE.equals(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isJboss
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Red Hat JBoss Application Server.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Red Hat JBoss Application Server.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isJboss() {
    return JBOSS.equals(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isGlassfish
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Oracle Glassfish Server.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Oracle Glassfish Server.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isGlassfish() {
    return GLASSFISH.equals(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isOC4J
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Oracle Application Server.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Oracle Application Server.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isOC4J() {
    return OC4J.equals(this.value);
  }
}