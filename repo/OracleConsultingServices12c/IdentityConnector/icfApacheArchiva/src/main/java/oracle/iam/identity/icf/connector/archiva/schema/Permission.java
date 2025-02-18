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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Apache Archiva Connector

    File        :   Permission.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Permission.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Permission
// ~~~~~ ~~~~~~~~~~
/**
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Permission {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute
  @JsonProperty
  private String    name;

  @Attribute
  @JsonProperty
  private Operation operation;

  @Attribute
  @JsonProperty
  private Resource  resource;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class Operation
  // ~~~~~ ~~~~~~~~~~
  /**
   **
   */
  public static class Operation {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @Attribute
    @JsonProperty
    private String  name;

    @Attribute
    @JsonProperty
    private String  description;

    @Attribute
    @JsonProperty
    private Boolean permanent;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Operation</code> REST Resource that allows use
     ** as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Operation() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the identifier of the <code>Operation</code>.
     **
     ** @param  value            the identifier of the <code>Operation</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Operation</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Operation</code>.
     */
    public final Operation name(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the identifier of the <code>Operation</code>.
     **
     ** @return                  the identifier of the <code>Operation</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Sets the description of the <code>Operation</code>.
     **
     ** @param  value            the description of the <code>Operation</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Operation</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Operation</code>.
     */
    public final Operation description(final String value) {
      this.description = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Returns the description of the <code>Operation</code>.
     **
     ** @return                  the description of the <code>Operation</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String description() {
      return this.description;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: permanent
    /**
     ** Sets the permanent behavior of the <code>Operation</code>.
     **
     ** @param  value            the permanent behavior of the
     **                          <code>Operation</code>.
     **                          <br>
     **                          Allowed object is {@link Boolean}.
     **
     ** @return                  the <code>Operation</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Operation</code>.
     */
    public final Operation permanent(final Boolean value) {
      this.permanent = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: permanent
    /**
     ** Returns the permanent behavior of the <code>Operation</code>.
     **
     ** @return                  the permanent behavior of the
     **                          <code>Operation</code>.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     */
    public final Boolean permanent() {
      return this.permanent;
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
     ** @return                    a hash code value for this object.
     **                            <br>
     **                            Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = this.name != null ? this.name.hashCode() : 0;
      result = 31 * result + (this.description != null ? this.description.hashCode() : 0);
      result = 31 * result + (this.permanent   != null ? this.permanent.hashCode()   : 0);
      return result;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Resource
  // ~~~~~ ~~~~~~~~
  /**
   **
   */
  public static class Resource {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @Attribute
    @JsonProperty
    private String  identifier;

    @Attribute
    @JsonProperty
    private Boolean pattern;

    @Attribute
    @JsonProperty
    private Boolean permanent;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Resource</code> REST Resource that allows use
     ** as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Resource() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: identifier
    /**
     ** Sets the identifier of the <code>Operation</code>.
     **
     ** @param  value            the identifier of the <code>Operation</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Resource</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Resource</code>.
     */
    public final Resource identifier(final String value) {
      this.identifier = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: identifier
    /**
     ** Returns the identifier of the <code>Resource</code>.
     **
     ** @return                  the identifier of the <code>Resource</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String identifier() {
      return this.identifier;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: pattern
    /**
     ** Sets the pattern behavior of the <code>Resource</code>.
     **
     ** @param  value            the pattern behavior of the
     **                          <code>Operation</code>.
     **                          <br>
     **                          Allowed object is {@link Boolean}.
     **
     ** @return                  the <code>Resource</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Resource</code>.
     */
    public final Resource pattern(final Boolean value) {
      this.pattern = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: pattern
    /**
     ** Returns the pattern behavior of the <code>Resource</code>.
     **
     ** @return                  the pattern behavior of the
     **                          <code>Operation</code>.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     */
    public final Boolean pattern() {
      return this.pattern;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: permanent
    /**
     ** Sets the permanent behavior of the <code>Resource</code>.
     **
     ** @param  value            the permanent behavior of the
     **                          <code>Operation</code>.
     **                          <br>
     **                          Allowed object is {@link Boolean}.
     **
     ** @return                  the <code>Resource</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Resource</code>.
     */
    public final Resource permanent(final Boolean value) {
      this.permanent = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: permanent
    /**
     ** Returns the permanent behavior of the <code>Resource</code>.
     **
     ** @return                  the permanent behavior of the
     **                          <code>Operation</code>.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     */
    public final Boolean permanent() {
      return this.permanent;
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
     ** @return                    a hash code value for this object.
     **                            <br>
     **                            Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = this.identifier != null ? this.identifier.hashCode() : 0;
      result = 31 * result + (this.pattern   != null ? this.pattern.hashCode()   : 0);
      result = 31 * result + (this.permanent != null ? this.permanent.hashCode() : 0);
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Permission</code> REST Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Permission() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the identifier of the <code>Permission</code>.
   **
   ** @param  value              the identifier of the <code>Permission</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Permission</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Permission</code>.
   */
  public final Permission name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the identifier of the <code>Permission</code>.
   **
   ** @return                    the identifier of the <code>Permission</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Sets the operation permitted by the <code>Permission</code>.
   **
   ** @param  value              the operation permitted by the
   **                            <code>Permission</code>.
   **                            <br>
   **                            Allowed object is {@link Operation}.
   **
   ** @return                    the <code>Permission</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Permission</code>.
   */
  public final Permission operation(final Operation value) {
    this.operation = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Returns the operation permitted by the <code>Permission</code>.
   **
   ** @return                    the operation permitted by the
   **                            <code>Permission</code>.
   **                            <br>
   **                            Possible object is {@link Operation}.
   */
  public final Operation operation() {
    return this.operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Sets the resource pattern permitted by the <code>Permission</code>.
   **
   ** @param  value              the resource pattern permitted by the
   **                            <code>Permission</code>.
   **                            <br>
   **                            Allowed object is {@link Resource}.
   **
   ** @return                    the <code>Permission</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Permission</code>.
   */
  public final Permission resource(final Resource value) {
    this.resource = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns the resource pattern permitted by the <code>Permission</code>.
   **
   ** @return                    the resource pattern permitted by the
   **                            <code>Permission</code>.
   **                            <br>
   **                            Possible object is {@link Resource}.
   */
  public final Resource resource() {
    return this.resource;
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
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.name != null ? this.name.hashCode() : 0;
    result = 31 * result + (this.operation != null ? this.operation.hashCode() : 0);
    result = 31 * result + (this.resource  != null ? this.resource.hashCode()  : 0);
    return result;
  }
}