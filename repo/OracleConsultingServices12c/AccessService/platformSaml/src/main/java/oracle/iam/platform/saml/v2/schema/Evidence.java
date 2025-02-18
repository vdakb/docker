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

    File        :   Evidence.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Evidence.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

import java.util.List;

////////////////////////////////////////////////////////////////////////////////
// final class Evidence
// ~~~~~ ~~~~~ ~~~~~~~~
/**
 ** SAML 2.0 Core Evidence.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Evidence {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Element local name. */
  public static final String LOCAL            = "Evidence";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Assertion of the Evidence. */
  private final List<Evidentiary> evidence;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Evidentiary
  // ~~~~~~~~~ ~~~~~~~~~~~
  /**
   * Marker interface for element types that can constitute evidence within a
   * {@link Evidence} object.
   */
  public interface Evidentiary {

  }

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link Evidence}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Assertion of the Evidence. */
    private List<Evidentiary> evidence;

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
    // Method: evidence
    /**
     ** Sets the list of all elements used as evidence.
     **
     ** @param  value            the list of all elements used as evidence.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Evidentiary}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder evidence(final List<Evidentiary> value) {
      this.evidence = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link Evidence}.
     **
     ** @return                  the SAML 2.0 {@link Evidence}.
     **                          <br>
     **                          Possible object is {@link Evidence}.
     */
    public Evidence build() {
      return new Evidence(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Action</code> with the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private Evidence(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.evidence = builder.evidence;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evidence
  /**
   ** Returns the list of all elements used as evidence.
   **
   ** @return                    the list of all elements used as evidence.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Evidentiary}.
   */
  public final List<Evidentiary> evidence() {
    return this.evidence;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>Evidence</code> {@link Builder}.
   **
   ** @return                    a new <code>Evidence</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>Evidence.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}