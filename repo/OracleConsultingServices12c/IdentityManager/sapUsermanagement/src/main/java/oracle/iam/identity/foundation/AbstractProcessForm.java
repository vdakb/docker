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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AbstractAttribute.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Set;
import java.util.Map;
import java.util.List;

import oracle.hst.foundation.utility.TypeUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class AbstractProcessForm
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractProcessForm</code> implements the base functionality
 ** of a Oracle Identity Manager Process Form.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AbstractProcessForm {

  /**
   ** the data mapping where each entry use the form attribute name as the key.
   */
  private final Map<String, String> nativly;

  /**
   ** the data mapping where each entry use the form attribute label as the key.
   */
  private final Map<String, String> labeled;
  private final Map<String, ?>      child;
  private final Set<Schema>         schema;

  /**
   ** Value object for holding the information about Form Field, currently there
   ** is only name and label, but can be more like type etc.
   */
  public static class Schema {
    private final String name;
    private final String label;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Option</code> from the specified properties.
     **
     ** @param  name               the name of a form attribute.
     ** @param  label              the label of a form attribute.
     */
    public Schema(final String name, final String label) {
      // ensure inheritance
      super();

      this.name  = name;
      this.label = label;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the attribute name where this schema attribute belongs to.
     **
     ** @return                  the attribute name where this schema
     **                          attribute belongs to.
     */
    public String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: label
    /**
     ** Returns the attribute label where this schema attribute belongs to.
     **
     ** @return                  the attribute label where this schema
     **                          attribute belongs to.
     */
    public String label() {
      return this.label;
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
      return this.name.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Schema</code>s are considered equal if and only if
     ** they represent the name. As a consequence, two given
     ** <code>Schema</code>s may be different even though they contain the same
     ** label.
     **
     ** @param  other            the reference object with which to compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;
      if (other == null || getClass() != other.getClass())
        return false;

      final Schema schema = (Schema)other;
      return this.name.equals(schema.name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     */
    @Override
    public String toString() {
    return "Schema{"
      +  StringUtility.formatValuePair("name",  this.name)
      +  StringUtility.formatValuePair("label", this.label)
      + '}'
    ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SchemaAttribute</code> from the specified properties.
   **
   ** @param  schema             the access attibute definition of the form.
   ** @param  nativly            the data of the form where each entry use the
   **                            form attribute name as the key.
   ** @param  labeled            the data of the form where each entry use the
   **                            form attribute label as the key.
   */
  public AbstractProcessForm(final Set<Schema> schema, final Map<String, String> nativly, final Map<String, String> labeled, final Map<String, List<Map<String, String>>> child) {
    super();

    this.nativly = nativly;
    this.labeled = labeled;
    this.child   = child;
    this.schema  = schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Returns the {@link Schema} of the process form where this descriptor
   ** belongs to.
   **
   ** @return                    the {@link Schema} of the process form where
   **                            this descriptor belongs to.
   */
  public Set<Schema> schema() {
    // be carefull, the schema Set is modifiable
    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValueForName
  /**
   ** Returns the value of a process form attribute by its name.
   **
   ** @param  name               the name of the attribute the value has to be
   **                            returned for.
   **
   ** @return                    the desired value as String.
   */
  public String stringValueForName(final String name) {
    return CollectionUtility.value(this.nativly, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValueForLabel
  /**
   ** Returns the value of a process form attribute by its label.
   **
   ** @param  label              the label of the attribute the value has to be
   **                            returned for.
   **
   ** @return                    the desired value as String.
   */
  public String stringValueForLabel(final String label) {
    return CollectionUtility.value(this.labeled, label);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValueForLabelRequired
  /**
   ** Returns the required value of a process form attribute by its label.
   **
   ** @param  label              the label of the attribute the value has to be
   **                            returned for.
   **
   ** @return                    the required value as String.
   */
  public String stringValueForLabelRequired(final String label) {
    return CollectionUtility.valueRequired(this.labeled, label);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueForLabel
  /**
   ** Returns the value of a process form attribute by its label.
   **
   ** @param  label              the label of the attribute the value has to be
   **                            returned for.
   **
   ** @return                    the desired value as String.
   */
  public <T> T valueForLabel(final String label, final Class<T> type) {
    return TypeUtility.convert(stringValueForLabel(label), type);
  }
}