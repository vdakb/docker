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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Category.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Category.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

////////////////////////////////////////////////////////////////////////////////
// class Category
// ~~~~~ ~~~~~~~~
/**
 ** <code>Category</code> defines the attribute restriction on values that can
 ** be passed as a nested parameter to deployment export instance.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Category extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the exportable/importable object definitions in alphabetical
  // order
  private static final String[] VALUE = {
    "AccessPolicy"
  , "AdminRole" // new since 11.1.2.3
  , "ApplicationInstance" // new since 11.1.1.0
  , "ApplicationTemplate" // new since 12.2.1.3
  , "ApprovalPolicy" // new since 11.1.1.0
  , "AttestationProcess" // new since 11.1.1.0
  , "AttributeDefinition" // new since 11.1.2.0
  , "CatalogDefinition" // new since 11.1.2.0
  , "CategoryDefinition" // new since 11.1.2.2
  , "CertificationDefinition" // new since 11.1.2.2
  , "CertificationConfiguration" // new since 11.1.2.2
  , "CustomResourceBundle" // new since 11.1.2.0
  , "DataObjectDef"
  , "EmailDef"
  , "EntityAdapter"
  , "EntityPublication" // new since 11.1.2.0
  , "ErrorCode"
  , "EventHandler"
  , "eventhandlers"
  , "GenericConnector"
  , "GTCProvider"
  , "IDAConfiguration" // new since 11.1.2.3
  , "IDARules" // new since 11.1.2.3
  , "IDAScanDefinition" // new since 11.1.2.3
  , "ITResource"
  , "ITResourceDef"
  , "Jar" // new since 11.1.2.0
  , "Job"
  , "Lookup"
  , "LOCALTEMPLATE"
  , "NOTIFICATIONTEMPLATE"
  , "OESPolicy"
  , "Organization"
  , "Org Metadata" // new since 11.1.2.0
  , "PasswordPolicy"
  , "Plugin" // new since 11.1.2.0
  , "Policies" // new since 11.1.2.3
  , "PrepopAdapter"
  , "Process"
  , "Process Form"
  , "RequestDataset"
  , "RequestTemplate"
  , "Resource"
  , "Resource Form"
  , "RiskConfiguration" // new since 11.1.2.2
  , "Role and Orgs UDF" // deprecated since 11.1.2.0
  , "Role Metadata" // new since 11.1.2.0
  , "RoleCategory"
  , "Rule"
  , "Rules" // new since 11.1.2.3
  , "scheduledTask"
  , "SystemProperties"
  , "TaskAdapter"
  , "Trigger"
  , "User Metadata" // new since 11.1.2.0
  , "User UDF" // deprecated since 11.1.2.0
  , "UserGroup"
  , "WorkFlowDefinition"
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String name = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Category</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Category() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Category</code> with the specified name and value.
   **
   ** @param  physicalType       the name of the category aka the physical type
   **                            in Identity Manager.
   ** @param  name               the value for the name.
   */
  public Category(final String physicalType, final String name) {
    // ensure inheritance
    this();

    this.setValue(physicalType);
    this.setName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT kernel to inject the argument for parameter name.
   **
   ** @param  name             the name of the Identity Manager object this
   **                          category wraps.
   */
  public void setName(final String name) {
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the object to be exported or imported by this
   ** category.
   **
   ** @return                    the name of the object to be exported or
   **                            imported by this category.
   */
  public final String name() {
    return this.name;
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
   */
  @Override
  public String[] getValues() {
    return VALUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return this.name.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Category</code> object that
   ** represents the same <code>name</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>Category</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>Category</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof Category))
      return false;

    final Category category = (Category)other;
    return (this.value.equals(category.value) && this.name.equals(category.name));
  }
}