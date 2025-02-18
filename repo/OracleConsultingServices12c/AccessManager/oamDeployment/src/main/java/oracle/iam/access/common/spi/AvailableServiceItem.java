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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AvailableServiceInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AvailableServiceInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

import oracle.hst.deployment.ServiceOperation;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.spi.AbstractInstance;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AvailableServiceItem
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AvailableServiceItem</code> represents a global configuration item in
 ** an Oracle Access Manager infrastructure that might be created, deleted or
 ** modified.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AvailableServiceItem extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String                     value;
  protected Type                       type;

  protected AvailableServiceInstance   root;
  protected AvailableServiceItem       item;
  protected List<AvailableServiceItem> element;

  protected final Set<String>          flatten = new HashSet<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** <code>Type</code> defines common type declarations.
   */
  public enum Type {
      URI("uri")
    , MAP("map")
   ,  LONG("long")
    , CLASS("class")
    , STRING("string")
    , BOOLEAN("boolean")
    , INTEGER("integer")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final String               id;

    ////////////////////////////////////////////////////////////////////////////
  // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AvailableServiceType</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Type(final String id) {
      this.id   = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the id of the property.
     **
     ** @return                  the id of the property.
     */
    public String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper <code>Type</code> from the given
     ** string value.
     **
     ** @param  value              the string value the type should be
     **                            returned for.
     **
     ** @return                    the <code>Type</code>.
     */
    public static Type fromValue(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AvailableServiceItem</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AvailableServiceItem() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // Method:   root
  /**
   ** Set the root of the property.
   **
   ** @param  instance           the {@link AvailableServiceInstance} to set as
   **                            root.
   */
  public void root(final AvailableServiceInstance instance) {
    this.root = instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Called to inject the argument for parameter <code>value</code>.
   **
   ** @param  value              the value to handle in Oracle Access Manager
   **                            configuration.
   */
  public final void value(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the configuration entry in Oracle Weblogic Domain
   ** server entity instance to handle.
   **
   ** @return                    the value context to handle in Oracle Access
   **                            Manager.
   */
  public final String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Called to inject the argument for parameter <code>value</code>.
   **
   ** @param  value              the value to handle in Oracle Access Manager
   **                            configuration.
   */
  public final void type(final Type value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the type of the configuration entry in Oracle Weblogic Domain
   ** server entity instance to handle.
   **
   ** @return                    the value context to handle in Oracle Access
   **                            Manager.
   */
  public final Type type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Evaluates the path to the configuration item from the top root element
   ** until the <code>item</code>.
   **
   ** @return                    path to the configuration item from the top
   **                            root element
   */
  public String path() {
    return (this.item == null) ? String.format("%s/%s", this.root.path(), name()) :  String.format("%s/%s", this.item.path(), name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one.
   ** <p>
   ** The {@code equals} method implements an equivalence relation on non-null
   ** object references:
   ** <ul>
   **   <li>It is <i>reflexive</i>: for any non-null reference value {@code x},
   **       {@code x.equals(x)} should return {@code true}.
   **   <li>It is <i>symmetric</i>: for any non-null reference values {@code x}
   **       and {@code y}, {@code x.equals(y)} should return {@code true} if and
   **       only if {@code y.equals(x)} returns {@code true}.
   **   <li>It is <i>transitive</i>: for any non-null reference values
   **       {@code x}, {@code y}, and {@code z}, if {@code x.equals(y)} returns
   **       {@code true} and {@code y.equals(z)} returns {@code true}, then
   **       {@code x.equals(z)} should return {@code true}.
   **   <li>It is <i>consistent</i>: for any non-null reference values {@code x}
   **       and {@code y}, multiple invocations of {@code x.equals(y)}
   **       consistently return {@code true} or consistently return
   **       {@code false}, provided no information used in {@code equals}
   **       comparisons on the objects is modified.
   **   <li>For any non-null reference value {@code x}, {@code x.equals(null)}
   **       should return {@code false}.
   ** </ul>
   ** <p>
   ** Note that it is generally necessary to override the <code>hashCode</code>
   ** method whenever this method is overridden, so as to maintain the general
   ** contract for the <code>hashCode</code> method, which states that equal
   ** objects must have equal hash codes.
   **
   ** @param  other             the reference object with which to compare.
   **
   ** @return                   <code>true</code> if this object is the same as
   **                           the other argument; <code>false</code>
   **                           otherwise.
   **
   ** @see    #hashCode()
   */
  @Override
  public boolean equals(final Object other) {
    if (other == null || (!(other instanceof AbstractInstance)))
      return false;

    final AvailableServiceItem another = (AvailableServiceItem)other;
    return (equal(getClass(), another.getClass())) && (equal(name(), another.name())&& (equal(value(), another.value())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding an item
   ** instance.
   **
   ** @param  instance           the instance to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link AvailableServiceInstance} with the same
   **                            name.
   */
  public void add(final AvailableServiceItem instance)
    throws BuildException {
    // prevent bogus input
    if (instance == null)
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_MANDATORY, "Item"));

    // prevent bogus state
    if (this.flatten.contains(instance.name()))
      throw new BuildException(FeatureResourceBundle.format(FeatureError.INSTANCE_ONLYONCE, "Item", instance.name()));

    // register the instance name for later validations
    this.flatten.add(instance.name());

    if (this.element == null) {
      this.element = new ArrayList<AvailableServiceItem>();
    }

    this.element.add(instance);
    instance.item = this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate()
    throws BuildException {

    // validate strictly for create to avoid side effects
    validate(ServiceOperation.enable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @param  operation          the {@link ServiceOperation} to validate for
   **                            <ul>
   **                              <li>{@link ServiceOperation#enable}
   **                              <li>{@link ServiceOperation#disable}
   **                              <li>{@link ServiceOperation#modify}
   **                              <li>{@link ServiceOperation#print}
   **                            </ul>
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation) {
    // ensure inheritance
    super.validate();

    if (this.type == null)
      handleAttributeMissing("type");

    // only enable and modify commands requires specific mandatory attributes
    if (operation == ServiceOperation.print)
      return;

    if (StringUtility.isEmpty(this.value))
      handleAttributeMissing("value");
  }
}