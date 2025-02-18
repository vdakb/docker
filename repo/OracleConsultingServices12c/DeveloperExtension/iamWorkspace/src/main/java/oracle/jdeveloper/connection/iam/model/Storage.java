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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   Storage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Storage.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.model;

import java.util.Map;
import java.util.HashMap;

import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.naming.NamingException;

import oracle.adf.share.jndi.SecureRefAddr;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Storage
// ~~~~~ ~~~~~~~
/**
 ** The model to support the Connection dialog for creating or modifiying the
 ** connection properties.
 ** <p>
 ** This data might also been used to display a specific node in the Resource
 ** Catalog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.104
 ** @since   12.2.1.3.42.60.104
 */
public class Storage implements Referenceable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the user name of the target system account to be used to establish
   ** a connection.
   */
  public static final String  PRINCIPAL_NAME     = "principal-name";

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the password of the target system account specified by the
   ** #PRINCIPAL_NAME parameter.
   */
  public static final String  PRINCIPAL_PASSWORD = "principal-password";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Class<? extends Referenceable>   type;
  private final Class<? extends EndpointFactory> factory;
  private final Map<String, String>              properties = new HashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Endpoint</code> that belongs to the specified
   ** connection type and factory.
   **
   ** @param  type               the connection type.
   ** @param  factory            the connection factory.
   */
  protected Storage(final Class<? extends Referenceable> type, final Class<? extends EndpointFactory> factory) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type    = type;
    this.factory = factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Sets the name of the principal of a target system to establish a
   ** connection.
   **
   ** @param  username           the name of the principal to establish a
   **                            connection.
   */
  public void principalName(final String username) {
    property(PRINCIPAL_NAME, username);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
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
  public String principalName() {
    return stringValue(PRINCIPAL_NAME);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Sets the password for the principal of a target system to establish
   ** a connection.
   **
   ** @param  password           the password for the principal of a target
   **                            system to establish a connection.
   */
  public void principalPassword(final char[] password) {
    principalPassword(String.copyValueOf(password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Sets the password for the principal of a target system to establish
   ** a connection.
   **
   ** @param  password           the password for the principal of a target
   **                            system to establish a connection.
   */
  public void principalPassword(final String password) {
    property(PRINCIPAL_PASSWORD, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password for the principal of a target system to establish
   ** a connection.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PRINCIPAL_PASSWORD}.
   ** <p>
   ** Not all <code>IT Resource</code>s that are managed by this code following
   ** the agreed naming conventions hence it may be necessary to overload this
   ** method so we cannot make it final.
   **
   ** @return                    the password for the principal of a target
   **                            system to establish a connection.
   */
  public String principalPassword() {
    return this.stringValue(PRINCIPAL_PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Add the specified value pairs to the properties that has to be applied.
   **
   ** @param  name               the name of the property to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>name</code> to set on this
   **                            instance.
   */
  public final void property(final String name, final String value) {
    // prevent bogus input
    if (name == null) {
      return;
    }

    this.properties.put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns the <code>boolean</code> value that is bound at the specified
   ** name.
   ** <p>
   ** If <code>null</code> is bound or there is no bound value, the value of the
   ** defaultValue parameter is returned, and the defaultValue string is put
   ** into the {@link Map} as a <code>placeholder</code>.
   **
   ** @param  name               the key for the desired string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is bound at the specified key.
   **
   ** @return                    the <code>String</code> value that is bound at
   **                            the specified name.
   */
  public final boolean booleanValue(final String name, final boolean defaultValue) {
    final String value = stringValue(name);
    return StringUtility.empty(value) ? defaultValue : Boolean.valueOf(value).booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns the <code>int</code> value that is bound at the specified name.
   ** <p>
   ** If <code>null</code> is bound or there is no bound value, the value of the
   ** defaultValue parameter is returned, and the defaultValue string is put
   ** into the {@link Map} as a <code>placeholder</code>.
   **
   ** @param  name               the key for the desired string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is bound at the specified key.
   **
   ** @return                    the <code>String</code> value that is bound at
   **                            the specified name.
   */
  public final int integerValue(final String name, final int defaultValue) {
    final String value = stringValue(name);
    return StringUtility.empty(value) ? defaultValue : Integer.valueOf(value).intValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns the <code>String</code> value that is bound at the specified key.
   **
   ** @param  name               the key for the desired attribute string.
   **
   ** @return                    the <code>String</code> for the given key.
   */
  public final String stringValue(final String name) {
    if (name == null) {
      return null;
    }

    final String value = this.properties.get(name);
    return (value == null) ? StringUtility.EMPTY : value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns the <code>String</code> value that is bound at the specified name.
   ** <p>
   ** If <code>null</code> is bound or there is no bound value, the value of the
   ** defaultValue parameter is returned, and the defaultValue string is put
   ** into the {@link Map} as a <code>placeholder</code>.
   **
   ** @param  name               the key for the desired string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            is bound at the specified key.
   **
   ** @return                    the <code>String</code> value that is bound at
   **                            the specified name.
   */
  public final String stringValue(final String name, final String defaultValue) {
    if (name == null) {
      return null;
    }

    final String value = this.properties.get(name);
    // the second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return (value == null) ? defaultValue : value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Returns the property mapping of the Server Instance to handle.
   **
   ** @return                    the property mapping of the Server Instance to
   **                            handle.
   */
  public final Map<String, String> properties() {
    return this.properties;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getReference (Referenceable)
  /**
   ** Retrieves the {@link Reference} of this object.
   **
   ** @return                    the non-<code>null</code> {@link Reference} of
   **                            this object.
   **
   ** @throws NamingException    if a naming exception was encountered while
   **                            retrieving the reference.
   */
  @Override
  public Reference getReference()
    throws NamingException {

    final Reference reference = new Reference(this.type.getName(), this.factory.getName(), null);
    for (Map.Entry<String, String> entry : this.properties.entrySet()) {
      final String name  = entry.getKey();
      final String value = entry.getValue();
      reference.add((name.equals(PRINCIPAL_PASSWORD)) ? new SecureRefAddr(name, value) : new StringRefAddr(name, value));
    }
    return reference;
  }
}