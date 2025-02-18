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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   IdentityServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityServer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.model;

import java.util.Map;
import java.util.LinkedHashMap;

import oracle.iam.platform.OIMClient;

import oracle.jdeveloper.connection.iam.model.Endpoint;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServer
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties stored in the <code>IdentityServer</code> model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityServer extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the type of
   ** the Identity Manager Managed Server where this IT Resource will be working
   ** on.
   */
  public static final String  SERVER_TYPE            = "serverType";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the property
   ** value to set.
   */
  public static final String  SERVER_TYPE_PROPERTY   = "serverTypeProperty";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify if you plan to configure SSL to secure communication between
   ** Identity Manager and the target system.
   */
  public static final String  SECURE_SOCKET          = "secureSocket";

  private static final String DEFAULT_SERVER_TYPE    = IdentityServerConstant.SERVER_TYPE_WEBLOGIC;
  private static final int    DEFAULT_SERVER_PORT    = 8005;
  private static final String DEFAULT_PRINCIPAL_NAME = "xelsysadm";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////
  
  public enum Type {

  /** the type name of a WebLogic Managed Server */
  WEBLOGIC(
      IdentityServerConstant.SERVER_TYPE_WEBLOGIC
    , IdentityServerConstant.SYSTEM_PROPERTY_WEBLOGIC
    , OIMClient.WLS_CONTEXT_FACTORY
    , IdentityServerConstant.PROTOCOL_WEBLOGIC_DEFAULT
    , IdentityServerConstant.PROTOCOL_WEBLOGIC_SECURE
    ),

    /** the type name of a WebSphere Server */
  WEBSPHERE(
      IdentityServerConstant.SERVER_TYPE_WEBSPHERE
    , IdentityServerConstant.SYSTEM_PROPERTY_WEBSPHERE
    , OIMClient.WAS_CONTEXT_FACTORY
    , IdentityServerConstant.PROTOCOL_WEBSPHERE_DEFAULT
    , IdentityServerConstant.PROTOCOL_WEBSPHERE_SECURE
    );

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;
    private final String property;
    private final String factory;
    private final String nonssl;
    private final String ssl;

    private final Map<String, String> old = new LinkedHashMap<String, String>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ServerType</code> that allows use as a JavaBean.
     **
     ** @param  property           the name of the system property value to
     **                            identify the server type.
     ** @param  factory            the full qualified class name of the JNDI
     **                            context factory.
     ** @param  nonssl             the non-ssl protocol prefix.
     ** @param  ssl                the ssl protocol prefix.
     */
    Type(final String value, final String property, final String factory, final String nonssl, final String ssl) {
      // initialize instance attributes
      this.value    = value;
      this.property = property;
      this.factory  = factory;
      this.nonssl   = nonssl;
      this.ssl      = ssl;
    }
 
    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   property
    /**
     ** Returns the name of the system property value to identify the server
     ** type.
     **
     ** @return                  possible object is {@link String}.
     */
    public String property() {
      return this.property;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   factory
    /**
     ** Returns the value of the vendor specific context factory.
     **
     ** @return                  possible object is {@link String}.
     */
    public String factory() {
      return this.factory;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   fromValue
    /**
     ** Factory method to create a proper server type from the given string
     ** value.
     **
     ** @param  value            the string value the server type should be
     **                          returned for.
     **
     ** @return                  the server type.
     */
    public static Type fromValue(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   pushSystemProperties
    /**
     ** Manage system properties that needs to exists et the time a connection
     ** is established to the specific server type.
     */
    public void saveSystemProperties() {
      this.old.put(IdentityServerConstant.TIMEOUT_WEBLOGIC_CONNECT, System.getProperty(IdentityServerConstant.TIMEOUT_WEBLOGIC_CONNECT));
      this.old.put(IdentityServerConstant.JAVA_SECURITY_CONFIG,     System.getProperty(IdentityServerConstant.JAVA_SECURITY_CONFIG));
      this.old.put(this.property, System.getProperty(this.property));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   restoreSystemProperties
    /**
     ** Restore system properties that might be overriden by
     ** {@link #saveSystemProperties()} et the time a connection was established
     ** to the specific server type.
     */
    public void restoreSystemProperties() {
      // reset the system properties to the origin
      for (Map.Entry<String, String> property : old.entrySet()) {
        final String name = property.getKey();
        final String value = property.getValue();
        if (value == null)
          System.getProperties().remove(name);
        else
          System.setProperty(name, value);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityServer</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityServer() {
    // ensure inheritance
    super(IdentityServer.class, IdentityServerFactory.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort (overridden)
  /**
   ** Returns the listener port of the server the target system is deployed on
   ** and this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PORT}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the listener port of the server the target
   **                            system is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   */
  @Override
  public int serverPort() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return integerValue(SERVER_PORT, DEFAULT_SERVER_PORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverType
  /**
   ** Sets the type of the Manager Server used to connect to.
   ** <p>
   ** If {@link IdentityServer#SERVER_TYPE} is not mapped this method
   ** returns {@link IdentityServerConstant#SERVER_TYPE_WEBLOGIC}.
   **
   ** @param  type               the type of the Manager Server used to
   **                            connect to.
   */
  public final void serverType(final String type) {
    property(SERVER_TYPE, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverType
  /**
   ** Returns the type of the Manager Server used to connect to.
   ** <p>
   ** If {@link IdentityServer#SERVER_TYPE} is not mapped this method returns
   ** {@link IdentityServerConstant#SERVER_TYPE_WEBLOGIC}.
   **
   ** @return                    the type of the Manager Server used to
   **                            connect to.
   */
  public final String serverType() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return stringValue(SERVER_TYPE, DEFAULT_SERVER_TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverTypeProperty
  /**
   ** Returns the type of the server where the Identity Manager is running and
   ** this IT Resource is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_TYPE_PROPERTY}.
   **
   ** @return                    the property value to set for the server type
   **                            Identity Manager is deployed on
   */
  public final String serverTypeProperty() {
    return stringValue(SERVER_TYPE_PROPERTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Sets the <code>true</code> if the server is using a secure protocol.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @param  value              <code>true</code> if the server is using
   **                            a secure protocol; otherwise <code>false</code>.
   */
  public void secureSocket(final boolean value) {
    property(SECURE_SOCKET, String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns the <code>true</code> if the server is using a secure protocol.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURE_SOCKET}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    <code>true</code> if the server is using
   **                            a secure protocol; otherwise
   **                            <code>false</code>.
   */
  public boolean secureSocket() {
    return booleanValue(SECURE_SOCKET, Boolean.FALSE.booleanValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName (overridden)
  /**
   ** Returns the name of the principal of a target system to establish a
   ** connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PRINCIPAL_NAME}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the name of the principal to establish a
   **                            connection.
   */
  public final String principalName() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return stringValue(PRINCIPAL_NAME, DEFAULT_PRINCIPAL_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverContext (overridden)
  /**
   ** Returns the name of the server context in a target system where this
   ** <code>IT Resource</code> will be working on
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_CONTEXT}.
   **
   ** @return                    the name of the server context in a target
   **                            system.
   */
  @Override
  public final String serverContext() {
    return stringValue(SERVER_CONTEXT, "oim");
  }
}