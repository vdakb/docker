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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Minimalistic JSON Parser

    File        :   JsonLiteral.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonLiteral.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.json.simple;

////////////////////////////////////////////////////////////////////////////////
// abstract class JsonLiteral
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** Represents a JSON literal.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class JsonLiteral extends JsonValue {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Represents the JSON literal <code>null</code>. */
  public static final JsonValue NULL             = new Null();

  /** Represents the JSON literal <code>true</code>. */
  public static final JsonValue TRUE             = new True();

  /** Represents the JSON literal <code>false</code>. */
  public static final JsonValue FALSE            = new False();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5769895901885797664")
  private static final long     serialVersionUID = -6008503621858452738L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Null
  // ~~~~ ~~~~~
  /**
   ** The JSON literal value <code>null</code>.
   */
  static class Null extends JsonLiteral {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-6123007352137599768")
    private static final long serialVersionUID = 7782682493555939529L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>JsonValue</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Null() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type (JsonValue)
    /**
     ** Returns the value type of this JSON value.
     **
     ** @return                  the JSON value type.
     */
    public final Type type() {
      return Type.NULL;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: toString (JsonValue)
    /**
     ** Returns the JSON string for this literal in its minimal form, without any
     ** additional whitespace.
     ** <br>
     ** The result is guaranteed to be a valid input for the method
     ** {@link #readFrom(String)} and to create a value that is <em>equal</em> to
     ** this object.
     **
     ** @return                  a JSON string that represents this literal.
     */
    @Override
    public String toString() {
      return "null";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class True
  // ~~~~ ~~~~~
  /**
   ** The JSON literal value <code>null</code>.
   */
  static class True extends JsonLiteral {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4145718402001088997")
    private static final long serialVersionUID = -6597529027463390867L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>JsonValue</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    True() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type (JsonValue)
    /**
     ** Returns the value type of this JSON value.
     **
     ** @return                  the JSON value type.
     */
    public final Type type() {
      return Type.TRUE;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: toString (JsonValue)
    /**
     ** Returns the JSON string for this literal in its minimal form, without any
     ** additional whitespace.
     ** <br>
     ** The result is guaranteed to be a valid input for the method
     ** {@link #readFrom(String)} and to create a value that is <em>equal</em> to
     ** this object.
     **
     ** @return                  a JSON string that represents this literal.
     */
    @Override
    public String toString() {
      return "true";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class False
  // ~~~~ ~~~~~~
  /**
   ** The JSON literal value <code>null</code>.
   */
  static class False extends JsonLiteral {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4656995932006668313")
    private static final long serialVersionUID = -6719712066194160528L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>JsonValue</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    False() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type (JsonValue)
    /**
     ** Returns the value type of this JSON value.
     **
     ** @return                  the JSON value type.
     */
    public final Type type() {
      return Type.FALSE;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: toString (JsonValue)
    /**
     ** Returns the JSON string for this literal in its minimal form, without any
     ** additional whitespace.
     ** <br>
     ** The result is guaranteed to be a valid input for the method
     ** {@link #readFrom(String)} and to create a value that is <em>equal</em> to
     ** this object.
     **
     ** @return                  a JSON string that represents this literal.
     */
    @Override
    public String toString() {
      return "false";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JsonValue</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  JsonLiteral() {
    // ensure inheritance
    super();
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
    return this.type().hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>JsonLiteral</code>s are considered equal if and only if they
   ** represent the same JSON text. As a consequence, two given
   ** <code>JsonLiteral</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other == null)
      return false;

    if (this == other)
      return true;

    if (getClass() != other.getClass())
      return false;

    final JsonLiteral that = (JsonLiteral)other;
    return this.type() == that.type();
  }
}