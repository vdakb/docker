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

package oracle.iam.identity.common.type.cache;

import org.apache.tools.ant.types.EnumeratedAttribute;

////////////////////////////////////////////////////////////////////////////////
// class Category
// ~~~~~ ~~~~~~~~
/**
 ** <code>Category</code> defines the attribute restriction on values that can
 ** be passed as a nested parameter to purge cache instance.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Category extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the caches in alphabetical order
  public static final String[] registry = {
    "AccessPolicyDefinition"
  , "AdapterInformation"
  , "API"
  , "CallbackConfiguration"
  , "Catalog"                    // new since 11.1.2.0
  , "ColumnMap"
  , "ColumnMetaData"
  , "ConnectorResourceBundle"
  , "CustomDefaultBundle"
  , "CustomResourceBundle"
  , "DataObjectEventHandlers"
  , "EmailDefinition"
  , "FormDefinition"
  , "GCProviders"                // deprecated since 11.1.2.0
  , "GenericConnector"
  , "GenericConnectorProviders" // new since 11.1.2.0
  , "ITResourceKey"
  , "LinguisticSort"
  , "LocaleCodeLanguageMapping" // new since 11.1.2.0
                                            , "LocalizedResource" // new since 11.1.2.0
  , "LookupDefinition"
  , "LookupValues"
  , "MetaData"
  , "NoNeedToFlush"
  , "ObjectDefinition"
  , "OESDefinition"
  , "OrgnizationName"
  , "PluginFramework"
  , "ProcessDefinition"
  , "RecordExists"
  , "RoleContainerToDescrMap"   // new since 11.1.2.0
  , "RuleDefinition"
  , "Reconciliation"
  , "SchedulerTaskDefinition"
  , "ServerProperties"
  , "SystemProperties"
  , "StoredProcAPI"
  , "TenantRegistry"            // new since 11.1.2.0
  , "User"
  , "UserDefinedColumns"
  , "UserConfig"
  , "UserGroups"
  , "UserStatus"
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Category</code> task that allows use as a JavaBean.
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
   ** Constructs a <code>Category</code> with the specified value.
   ** <br>
   ** Convinience Constructor.
   **
   ** @param  value              the name of the category to be flushed by this
   **                            category.
   */
  public Category(final String value) {
    // ensure inheritance
    super();

    // initialize instance
    super.setValue(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the name of the category to be exported or imported by this
   ** category.
   **
   ** @return                    the name of the category to be flushed by this
   **                            category.
   */
  public final String value() {
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
   */
  @Override
  public String[] getValues() {
    return registry;
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
   **
   ** @see    Object#equals(Object)
   ** @see    Object#hashCode()
   ** @see    #equals(Object)
   */
  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Category</code> object that
   ** represents the same <code>value</code> as this object.
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

    return (this.value.equals(((Category)other).value));
  }
}