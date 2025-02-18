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

    File        :   LDAPControl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDAPControl.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

import java.util.Hashtable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.naming.ldap.Control;

import oracle.jdeveloper.connection.iam.Bundle;
import oracle.jdeveloper.connection.iam.service.DirectoryException;
import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class LDAPControl
// ~~~~~ ~~~~~~~~~~~
/**
 ** Represents arbitrary control data that can be used with a a particular LDAP
 ** operation. LDAP controls are part of version 3 of the LDAP protocol.
 ** <p>
 ** LDAP controls allow you to extend the functionality of an LDAP operation.
 ** For example, you can use an LDAP control for the search operation to sort
 ** search results on an LDAP server.
 ** <p>
 ** An LDAP control can be either a <b>server control</b> or a
 ** <b>client control</b>:
 ** <ul>
 **   <li><b>Server controls</b> can be sent to the LDAP server or returned by
 **       the server on any operation.
 **   <li><b>Client controls</b> are intended to affect only the client side of
 **       the operation.
 ** </ul>
 ** <p>
 * An LDAP control consists of the following information:
 ** <ul>
 **   <li>A unique object ID (OID) that identifies the control.
 **   <li>A &quot;criticality&quot; field, which indicates whether or not the
 **       control is critical to the operation. (If the control is critical to
 **       the operation and the server does not support the control, the server
 **       should not execute the operation.)
 **   <li>Data pertaining to the control.
 * </ul>
 ** To determine which server controls are supported by a particular server, you
 ** need to search for the root DSE (DSA-specific entry, where DSA is another
 ** term for &quot;LDAP server&quot;) and find the values of the
 ** <code>supportedControl</code> attribute. This attribute contains the object
 ** IDs (OIDs) of the controls supported by this server.
 ** <p>
 ** The following section of code demonstrates how to get the list of the server
 ** controls supported by an LDAP server.
 ** <pre>
 ** public static void main(String[] args) {
 **   Connection ld = new Connection();
 **   try {
 **     String host = "localhost";
 **     int    port = 389;
 **     ld.connect(host, port);
 **     try {
 **       ld.authenticate(3, "cn=Directory Manager", "Welcome1");
 **     }
 **     catch (LDAPException e) {
 **       System.out.println("LDAP server does not support v3.");
 **       ld.disconnect();
 **       System.exit(1);
 **     }
 **
 **     String filter      = "(objectclass=*)";
 **     String searchbase  = "";
 **     String        returning[] = { "supportedControl" };
 **     SearchResults response    = ld.search( searchbase,
 **     LDAPConnection.SCOPE_BASE, filter, returning, false );
 **
 **     while ( response.hasMoreElements() ) {
 **       SreachResult entry     = (SreachResult)response.nextElement();
 **       Attributes   attribute = entry.getAttributeSet();
 **       Enumeration  enum      = attribute.getAttributes();
 **
 **       while (enum.hasMoreElements()) {
 **         Attribute attr = (Attribute)enum.nextElement();
 **         String name = attr.getID();
 **         System.out.println(name);
 **         Enumeration value = attr.getStringValues();
 **         while (value.hasMoreElements())
 **           System.out.println("\t" + (String)value.nextElement());
 **       }
 **     }
 **   }
 **   catch (LDAPException e) {
 **     System.out.println( "Error: " + e.toString() );
 **   }
 **   try {
 **     ld.disconnect();
 **   }
 **   catch (LDAPException e) {
 **     System.exit(1);
 **   }
 **   System.exit(0);
 ** }
 ** </pre>
 ** If you compile and run this example against an LDAP server that supports v3
 ** of the protocol, you might receive the following results:
 ** <pre>
 **   supportedcontrol
 **   2.16.840.1.113730.3.4.2
 **   2.16.840.1.113730.3.4.3
 **   2.16.840.1.113730.3.4.4
 **   2.16.840.1.113730.3.4.5
 **   1.2.840.113556.1.4.473
 ** </pre>
 ** For more information on LDAP controls, see the Internet-Draft on the LDAP v3
 ** protocol. (Note that this internet draft is still a work in progress.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
 */
public class LDAPControl implements Cloneable
                         ,           Control {

  /////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  // use a Hashtable to synchronize the access
  static private Hashtable<String, Class<Control>>  controlClassHash = null;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5015921564374008890")
  private static final long serialVersionUID = -3655886045939941327L;

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String            oid;
  protected boolean         critical         = Control.NONCRITICAL;
  protected byte[]          value            = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default constructor for the <code>LDAPControl</code> class.
   */
  public LDAPControl() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>LDAPControl</code> object using the specified
   ** object ID (OID), &quot;criticality&quot; field, and data to be used by the
   ** control.
   **
   ** @param  oid                the object ID (OID) identifying the control
   ** @param  critical           <code>true</code> if the LDAP operation should
   **                            be cancelled when the server does not support
   **                            this control (in other words, this control is
   **                            critical to the LDAP operation)
   ** @param  value              control-specific data
   */
  public LDAPControl(final String oid, final boolean critical, final byte[] value) {
    this.oid      = oid;
    this.critical = critical;
    this.value    = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Gets the data in the control.
   **
   ** @return                    the data in the control as a byte array.
   */
  public byte[] value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone (Cloneable)
  /**
   ** Creates a copy of the control
   **
   ** @return                    copy of the control
   */
  public Object clone() {
    byte[] vals = null;
    if (this.value != null) {
      vals = new byte[this.value.length];
      for (int i = 0; i < this.value.length; i++)
        vals[i] = this.value[i];
    }
    return new LDAPControl(this.oid, this.critical, vals);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getID (Control)
  /**
   ** Returns the object ID (OID) of the control.
   **
   ** @return                    object ID (OID) of the control.
   */
  @Override
  public String getID() {
    return this.oid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCritical (Control)
  /**
   ** Specifies whether or not the control is critical to the LDAP operation.
   **
   ** @return                    <code>true</code> if the LDAP operation should
   **                            be cancelled when the server does not support
   **                            this control.
   */
  @Override
  public boolean isCritical() {
    return this.critical;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEncodedValue
  /**
   ** Gets the data in the control.
   **
   ** @return                    the data in the control as a byte array.
   */
  @Override
  public byte[] getEncodedValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Return a string representation of the control for debugging
   **
   ** @return                    a string representation of the control.
   */
  @Override
  public String toString() {
    String s = getID() + StringUtility.BLANK + Boolean.toString(isCritical());
    if (this.value != null)
      s += StringUtility.BLANK + this.value.toString();

    return "LDAPControl {" + s + '}';
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Associates a class with an oid. This class must be an extension of
   ** <code>DirectoryControl</code>, and should implement the
   ** <code>DirectoryControl(String oid, boolean critical, byte[] value)</code>
   ** constructor to instantiate the control.
   **
   ** @param  oid                the string representation of the oid
   ** @param  controlClass       the class that instantatiates the control
   **                            associated with oid
   **
   ** @throws DirectoryException if the class parameter is not implementing
   **                            <code>Control</code> or the class parameter
   **                            does not provide a
   **                            <code>LDIFControl(String oid, boolean critical, byte[] value)</code>
   **                            constructor.
   */
  public static void register(final String oid, final Class<Control> controlClass)
    throws DirectoryException {

    if (controlClass == null)
      return;

    // 1. make sure controlClass implements Control
    if (!Control.class.isAssignableFrom(controlClass)) {
      String[] arguments = { controlClass.getName(), Control.class.getName()};
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_UNHANDLED, arguments));
    }

    // 2. make sure controlClass has the proper constructor
    Class[] parameter = { String.class, boolean.class, byte[].class };
    try {
      controlClass.getConstructor(parameter);
    }
    catch (NoSuchMethodException e) {
      String[] arguments = { controlClass.getName(), "String.class, boolean.class, byte[].class"};
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_UNHANDLED, arguments), e);
    }

    // 3. check if the hash table exists
    if (controlClassHash == null)
      controlClassHash = new Hashtable<String, Class<Control>>();

    // 4. add the controlClass
    controlClassHash.put(oid, controlClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controlClass
  /**
   ** Returns the <code>Class</code> that has been registered to oid.
   **
   ** @param  oid                a String that associates the control class to a
   **                            control.
   **
   ** @return                    a <code>Class</code> that can instantiate a
   **                            control of the type specified by oid.
   */
  protected static Class<Control> controlClass(String oid) {
    if (controlClassHash == null)
      return null;

    return controlClassHash.get(oid);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createControl
  /**
   ** Returns a <code>LDIFControl</code> object instantiated by the Class
   ** associated by <code>LDIFControl.register</code> to the oid. If no Class is
   ** found for the given control, or an exception occurs when attempting to
   ** instantiate the control, a basic <code>LDIFControl</code> is instantiated
   ** using the parameters.
   **
   ** @param  oid                the oid of the control to instantiate
   ** @param  critical           <code>true</code> if this is a critical control
   ** @param  value              the byte value for the control
   **
   ** @return                    a newly instantiated <code>LDIFControl</code>.
   **
   ** @see    LDAPControl#register
   */
  protected static Control createControl(final String oid, final boolean critical, final byte[] value) {
    final Class<Control> controlClass = controlClass(oid);
    if (controlClass == null)
      return new LDAPControl(oid, critical, value);

    final Class[]        parameter   = { String.class, boolean.class, byte[].class };
    Constructor<Control> constructor = null;
    try {
      constructor = controlClass.getConstructor(parameter);
    }
    catch (NoSuchMethodException e) {
      // shouldn't happen, but...
      System.err.println("Caught java.lang.NoSuchMethodException while attempting to instantiate a control of type " + oid);
      return new LDAPControl(oid, critical, value);
    }

    final Object[] argument = { oid, String.valueOf(critical), value };
    Control  control  = null;
    try {
      control = constructor.newInstance(argument);
    }
    catch (Exception e) {
      String reason = null;
      if (e instanceof InvocationTargetException) {
        reason = ((InvocationTargetException)e).getTargetException().toString();
      }
      else {
        reason = e.toString();
      }
      System.err.println("Caught " + reason + " while attempting to instantiate a control of type " + oid);
      control = new LDAPControl(oid, critical, value);
    }

    return control;
  }
}