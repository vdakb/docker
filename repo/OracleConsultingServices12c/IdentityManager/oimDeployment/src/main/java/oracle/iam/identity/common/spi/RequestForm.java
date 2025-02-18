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

    File        :   RequestForm.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RequestForm.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class RequestForm
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>RequestForm</code> represents a set of <code>Attribute</code> of an
 ** request data set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RequestForm extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ENTITLEMENT = "%d~%s";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the attributes mapping */
  private List<Attribute> attribute;

  /** the attributes mapping specific for subordinated forms */
  private Map<String, List<List<Attribute>>> dataSet;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Reference
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Reference</code> represents an <code>Attribute</code> reference of
   ** a <code>RequestDataSet</code>.
   */
  public static class Reference {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String           name      = null;
    private String           type      = null;
    private ServiceOperation operation = ServiceOperation.assign;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Reference</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Reference() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Reference</code> with the specified name and type.
     **
     ** @param  name             the name of the <code>Attribute</code> to
     **                          handle.
     ** @param  type             the type of the <code>Attribute</code> to
     **                          handle.
     */
    public Reference(final String name, final String type) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.name = name;
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Called to inject the argument for parameter <code>name</code>.
     **
     ** @param  name             the name of the <code>Reference</code> to
     **                          handle.
     */
    public final void name(final String name) {
      this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the internal name of the entity instance.
     **
     ** @return                  the name of the <code>Reference</code> to
     **                          handle.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Called to inject the argument for parameter <code>type</code>.
     **
     ** @param  type             the type of the <code>Reference</code> to
     **                          handle.
     */
    public final void type(final String type) {
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the internal type of the entity instance.
     **
     ** @return                  the type of the <code>Reference</code> to
     **                          handle.
     */
    public final String type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: operation
    /**
     ** Called to inject the argument for parameter <code>operation</code>.
     **
     ** @param  operation          the operation to execute against Identity
     **                            Manager entity instance.
     */
    public void operation(final ServiceOperation operation) {
      if (operation == null)
        handleAttributeMissing("operation");

      this.operation = operation;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: operation
    /**
     ** Returns the operation to execute against Identity Manager entity
     ** instance.
     **
     ** @return                  the operation to execute against Identity
     **                          Manager entity instance.
     */
    public final ServiceOperation operation() {
      return this.operation;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public int hashCode() {
      long   hc = getClass().hashCode();
      String id = this.name;
      if (id != null) {
        hc += id.hashCode();
      }
      return (int)hc;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one.
     ** <p>
     ** The {@code equals} method implements an equivalence relation on non-null
     ** object references:
     ** <ul>
     **   <li>It is <i>reflexive</i>: for any non-null reference value
     **       {@code x}, {@code x.equals(x)} should return {@code true}.
     **   <li>It is <i>symmetric</i>: for any non-null reference values
     **       {@code x} and {@code y}, {@code x.equals(y)} should return
     **       {@code true} if and only if {@code y.equals(x)} returns
     **       {@code true}.
     **   <li>It is <i>transitive</i>: for any non-null reference values
     **       {@code x}, {@code y}, and {@code z}, if {@code x.equals(y)}
     **       returns {@code true} and {@code y.equals(z)} returns {@code true},
     **       then {@code x.equals(z)} should return {@code true}.
     **   <li>It is <i>consistent</i>: for any non-null reference values
     **       {@code x} and {@code y}, multiple invocations of
     **       {@code x.equals(y)} consistently return {@code true} or
     **       consistently return {@code false}, provided no information used in
     **       {@code equals} comparisons on the objects is modified.
     **   <li>For any non-null reference value {@code x}, {@code x.equals(null)}
     **       should return {@code false}.
     ** </ul>
     ** <p>
     ** Note that it is generally necessary to override the {@code hashCode}
     ** method whenever this method is overridden, so as to maintain the general
     ** contract for the {@code hashCode} method, which states that equal objects
     ** must have equal hash codes.
     **
     ** @param  other            the reference object with which to compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the other argument; <code>false</code>
     **                          otherwise.
     **
     ** @see    #hashCode()
     */
    @Override
    public boolean equals(final Object other) {
      if (other == null || (!(other instanceof Reference)))
        return false;

      final Reference another = (Reference)other;
      return (equal(getClass(), another.getClass())) && (equal(this.name, another.name));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Attribute</code> represents an <code>Attribute</code> value of
   ** a <code>RequestDataSet</code>.
   */
  public static class Attribute extends Reference {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** determines that the attribute value needs to be prefixed with the
     ** internal system identifier of the <code>IT Resource</code> associated
     ** with the corresponding <code>Application Instance</code>
     */
    private boolean prefix = false;

    /**
     ** specifies the pattern to be applied if <code>prefix</code> specifies
     ** <code>true</code>
     ** if the instance attribute is <code>null</code> the default pattern
     ** "%d~%s" will be used
     */
    private String pattern = null;

    /** the value of the attribute */
    private String value   = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Attribute() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Reference</code> with the specified name and type.
     **
     ** @param  name             the name of the <code>Attribute</code> to
     **                          handle.
     ** @param  type             the type of the <code>Attribute</code> to
     **                          handle.
     ** @param  value            the value of the <code>Attribute</code> to
     **                          handle.
     */
    public Attribute(final String name, final String type, final String value) {
      // ensure inheritance
      super(name, type);

      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix
    /**
     ** Called to inject the argument for parameter <code>prefix</code>.
     **
     ** @param  prefix           the prefix of the <code>Attribute</code> to
     **                          handle.
     */
    public final void prefix(final boolean prefix) {
      this.prefix = prefix;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix
    /**
     ** Returns the prefix of the <code>Attribute</code> instance.
     **
     ** @return                  the prefix of the <code>Attribute</code> to
     **                          handle.
     */
    public final boolean prefix() {
      return this.prefix;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: pattern
    /**
     ** Called to inject the argument for parameter <code>pattern</code>.
     **
     ** @param  pattern          the pattern of the <code>Attribute</code> to
     **                          handle.
     */
    public final void pattern(final String pattern) {
      this.pattern = pattern;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: pattern
    /**
     ** Returns the pattern of the <code>Attribute</code> instance.
     **
     ** @return                  the pattern of the <code>Attribute</code> to
     **                          handle.
     */
    public final String pattern() {
      return this.pattern == null ? ENTITLEMENT : this.pattern;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Called to inject the argument for parameter <code>value</code>.
     **
     ** @param  value            the value of the <code>Attribute</code> to
     **                          handle.
     */
    public final void value(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Attribute</code> instance.
     **
     ** @return                  the value of the <code>Attribute</code> to
     **                          handle.
     */
    public final String value() {
      return this.value;
    }
    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException     in case the instance does not meet the
     **                            requirements.
     */
    public void validate()
      throws BuildException {

      if (this.prefix && !this.type().equals("String"))
        throw new BuildException("Only attributes of type String can be prefixed");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RequestForm</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RequestForm() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RequestForm</code> with the specified name.
   **
   ** @param  name               the name of the <code>RequestDataSet</code> to
   **                            handle.
   */
  public RequestForm(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the attribute mapping to be applied during a request.
   **
   ** @return                    the attribute mapping to be applied during a
   **                            request.
   */
  public final List<Attribute> attribute() {
    return this.attribute;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the attributes mapping specific for subordinated forms to be
   ** applied during a request.
   **
   ** @return                    the attributes mapping specific for
   **                            subordinated forms to be applied during a
   **                            request.
   */
  public final Map<String, List<List<Attribute>>> dataSet() {
    return this.dataSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Add the specified {@link Attribute} that has to be applied during a
   ** request operation.
   **
   ** @param  attribute          the {@link Attribute}  to add.
   */
  public void addAttribute(final Attribute attribute) {
    if (this.attribute == null)
      this.attribute = new ArrayList<Attribute>();
    else if (this.attribute.contains(attribute))
      throw new BuildException();

    this.attribute.add(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDataSet
  /**
   ** Add the specified {@link RequestForm} that has to be applied during a
   ** request operation.
   **
   ** @param  dataSet            the {@link RequestForm}  to add.
   */
  public void addDataSet(final RequestForm dataSet) {
    if (this.dataSet == null)
      this.dataSet = new HashMap<String, List<List<Attribute>>>();

    List<List<Attribute>> mapping = this.dataSet.get(dataSet.name());
    if (mapping == null) {
      mapping = new ArrayList<List<Attribute>>();
      this.dataSet.put(dataSet.name(), mapping);
    }
    mapping.add(dataSet.attribute());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of an user profile like type. If it's
   ** <code>false</code> only the login name of the user profile has to be
   ** present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    // ensure inheritance
    super.validate();

    if (this.attribute != null) {
      for (Attribute cursor : this.attribute) {
        cursor.validate();
      }
    }
    if (this.dataSet != null) {
      for (String cursor : this.dataSet.keySet()) {
        for (List<Attribute> tupel : this.dataSet.get(cursor)) {
          for (Attribute attribute : tupel) {
            attribute.validate();
          }
        }
      }
    }
  }
}