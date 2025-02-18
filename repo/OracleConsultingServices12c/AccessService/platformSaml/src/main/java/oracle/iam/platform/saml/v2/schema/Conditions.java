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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Conditions.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Conditions.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

import java.time.ZonedDateTime;

////////////////////////////////////////////////////////////////////////////////
// final class Conditions
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Element, which gives the conditions under which the assertion is to be
 ** considered valid.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Conditions {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** SAML 2.0 Conditions local element name. */
  public static final String  LOCAL  = "Conditions";

  /** NotBefore attribute name. */
  public static final String  BEFORE = "NotBefore";

  /** NotOnOrAfter attribute name. */
  public static final String  AFTER  = "NotOnOrAfter";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Not Before conditions. */
  private final ZonedDateTime before;

    /** Not On Or After conditions. */
  private final ZonedDateTime after;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link Conditions}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Not Before conditions. */
    private ZonedDateTime before;

    /** Not On Or After conditions. */
    private ZonedDateTime after;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Builder</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: before
    /**
     ** Sets the date/time before which the assertion is invalid.
     **
     ** @param  value            the date/time before which the assertion is
     **                          invalid.
     **                          <br>
     **                          Allowed object is {@link ZonedDateTime}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder before(final ZonedDateTime value) {
      this.before = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: after
    /**
     ** Sets the date/time on, or after, which the assertion is invalid.
     **
     ** @param  value            the date/time on, or after, which the assertion
     **                          is invalid.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder after(final ZonedDateTime value) {
      this.after = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link Conditions}.
     **
     ** @return                  the SAML 2.0 {@link Conditions}.
     **                          <br>
     **                          Possible object is {@link Conditions}.
     */
    public Conditions build() {
      return new Conditions(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Conditions</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private Conditions(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.before = builder.before;
    this.after  = builder.after;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
  /**
   ** Returns the date/time before which the assertion is invalid.
   **
   ** @return                    the date/time before which the assertion is
   **                            invalid.
   **                            <br>
   **                            Possible object is {@link ZonedDateTime}.
   */
  public final ZonedDateTime before() {
    return this.before;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   after
  /**
   ** Returns the date/time on, or after, which the assertion is invalid.
   **
   ** @return                    the date/time on, or after, which the assertion
   **                            is invalid.
   **                            <br>
   **                            Possible object is {@link ZonedDateTime}.
   */
  public final ZonedDateTime after() {
    return this.after;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>Conditions</code> {@link Builder}.
   **
   ** @return                    a new <code>Conditions</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>Conditions.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}