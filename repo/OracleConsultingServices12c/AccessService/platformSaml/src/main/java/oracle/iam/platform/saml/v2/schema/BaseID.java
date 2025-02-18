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

    File        :   BaseID.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    BaseID.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

////////////////////////////////////////////////////////////////////////////////
// final class BaseID
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** SAML 2.0 Core Base ID.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class BaseID {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** SAML 2.0 Base ID local element name. */
  public static final String LOCAL        = "BaseID";

  /** NameQualifier attribute name. */
  public static final String QUALIFIER    = "NameQualifier";

  /** SPNameQualifier attribute name. */
  public static final String SP_QUALIFIER = "SPNameQualifier";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Name Qualifier of the Base ID. */
  private final String       nameQualifier;

  /** SP Name Qualifier of the Base ID. */
  private final String       spNameQualifier;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link BaseID}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Name Qualifier of the Base ID. */
    String nameQualifier;

    /** SP Name Qualifier of the Base ID. */
    String spNameQualifier;

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
    protected Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: nameQualifier
    /**
     ** Sets the NameQualifier the BaseID.
     **
     ** @param  value            the NameQualifier the BaseID.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>nameQualifier</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder nameQualifier(final String value) {
      this.nameQualifier = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: spNameQualifier
    /**
     ** Sets the SPNameQualifier the BaseID.
     **
     ** @param  value            the SPNameQualifier the BaseID.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured
     **                          <code>spNameQualifier</code> parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder spNameQualifier(final String value) {
      this.spNameQualifier = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link BaseID}.
     **
     ** @return                  the SAML 2.0 {@link BaseID}.
     **                          <br>
     **                          Possible object is {@link BaseID}.
     */
    public BaseID build() {
      return new BaseID(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BaseID</code> with the specified {@link Builder}.
   **
   ** @param builder the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private BaseID(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.nameQualifier   = builder.nameQualifier;
    this.spNameQualifier = builder.spNameQualifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameQualifier
  /**
   ** Returns the NameQualifier value this <code>BaseID</code>.
   **
   ** @return                    the NameQualifier value this
   **                            <code>BaseID</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String nameQualifier() {
    return this.nameQualifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   spNameQualifier
  /**
   ** Returns the SPNameQualifier value this <code>BaseID</code>.
   **
   ** @return                    the SPNameQualifier value this
   **                            <code>BaseID</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
   public final String spNameQualifier() {
     return this.spNameQualifier;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>BaseID</code> {@link Builder}.
   **
   ** @return                    a new <code>BaseID</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>BaseID.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}