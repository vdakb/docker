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
    Subsystem   :   Foundation Shared Library

    File        :   ValueWrapper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ValueWrapper.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.xmpp;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// final class ValueWrapper
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** A wrapper implementation for cached values, suitable for {@code Map} based
 ** caches where a significant portion of keys matches the corresponding value
 ** exactly.
 **
 ** @param  <V>                  the most general value type this wrapper will
 **                              maintain.
 **                              This is normally {@link Object}.
 */
public class ValueWrapper <V extends Serializable> implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with

  @SuppressWarnings("compatibility:-5704884548903962980")
  private static final long serialVersionUID = 8614827766634188024L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The value that is wrapped. */
  private final V              value;
  /**
   ** Indicates how the key that maps to this value can be used to extract the
   ** value from the cache entry.
   */
  private final Representation representation;
  /**
   ** Describes the issue that caused the value to be denoted as 'Illegal'.
   */
  private final String         exception;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // enum Representation
  // ~~~~ ~~~~~~~~~~~~~~~
  /**
   ** Indication of how the key of this cache entry represents the cache value.
   */
  public enum Representation {
      /**
       ** The key that maps to this {@link ValueWrapper} instance cannot be used
       ** to generate a valid value.
       */
      ILLEGAL
      /**
       ** The generated value based on the key that maps to this
       ** {@link ValueWrapper} would be an exact duplicate of the key.
       ** <br>
       ** conserve memory, this wrapped value instance will not have a value
       ** set. Use the key that points to this wrapper instead.
       */
    , KEY
      /**
       ** The key that maps to this {@link ValueWrapper} can be used to generate
       ** a valid value. The generated value is wrapped in this
       ** {@link ValueWrapper} instance.
       */
    , VALUE
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ValueWrapper</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ValueWrapper() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.value          = null;
    this.exception      = null;
    this.representation = Representation.KEY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ValueWrapper</code> that is used to indicate
   ** that the key that maps to this value cannot be used to generate a valid
   ** value.
   **
   ** @param  exception          describes the invalidity of the key.
   **                            <br>
   **                            Allowed object is {@link Exception}.
   */
  public ValueWrapper(final Exception exception) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.value          = null;
    this.exception      = exception.getMessage();
    this.representation = Representation.ILLEGAL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ValueWrapper</code> for <code>value</code> while using
   ** the <code>VALUE</code> representation.
   **
   ** @param  value              the value that is wrapped.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   */
  public ValueWrapper(final V value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.value          = value;
    this.exception      = null;
    this.representation = Representation.VALUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the wrapped value, or <code>null</code> if the representation
   ** used in this instance is not {@link Representation#VALUE}.
   **
   ** @return                    the wrapped value.
   **                            <br>
   **                            Possible object is <code>V</code>.
   */
  public V value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the {@link Representation} of the wrapped value.
   **
   ** @return                    the {@link Representation} of the wrapped value.
   **                            <br>
   **                            Possible object is {@link Representation}.
   */
  public final Representation representation() {
    return this.representation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Returns the message describing the invalidity of the key that maps to this
   ** instance.
   **
   ** @return                    an exception message , possibly
   **                           <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String exception() {
    return this.exception;
  }
}
