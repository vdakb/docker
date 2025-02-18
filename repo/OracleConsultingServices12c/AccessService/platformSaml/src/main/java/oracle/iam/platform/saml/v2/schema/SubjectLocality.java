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

    File        :   SubjectLocality.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SubjectLocality.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

////////////////////////////////////////////////////////////////////////////////
// final class Subject
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** SAML 2.0 Core SubjectLocality.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class SubjectLocality {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** SAML 2.0 Subject local element name. */
  public static final String LOCAL   = "SubjectLocality";

  /** Address attribute name. */
  public static final String ADDRESS = "Address";

  /** DNSName attribute name. */
  public static final String DNS     = "DNSName";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The Address of the assertion. */
  private final String       address;

  /** The DNS Name of the assertion. */
  private final String       dns;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link SubjectLocality}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The Address of the assertion. */
    private String address;

    /** The DNS Name of the assertion. */
    private String dns;

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
    // Method: address
    /**
     ** Sets the IP address of the system from which the subject was
     ** authenticated.
     **
     ** @param  value            the IP address of the system from which the
     **                          subject was authenticated.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder address(final String value) {
      this.address = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dns
    /**
     ** Sets he DNSName of the system from which the subject was authenticated.
     **
     ** @param  value            he DNSName of the system from which the subject
     **                          was authenticated.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder dns(final String value) {
      this.dns = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 Core {@link SubjectLocality}.
     **
     ** @return                  the SAML 2.0 Core {@link SubjectLocality}.
     **                          <br>
     **                          Possible object is {@link SubjectLocality}.
     */
    public SubjectLocality build() {
      return new SubjectLocality(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SubjectLocality</code> with the specified
   ** {@link Builder}.
   **
   ** @param  builder           the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private SubjectLocality(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.dns     = builder.dns;
    this.address = builder.address;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   address
  /**
   ** Returns the IP address of the system from which the subject was
   ** authenticated.
   **
   ** @return                    the IP address of the system from which the
   **                            subject was authenticated.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String address() {
    return this.address;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dns
  /**
   ** Returns the DNSName of the system from which the subject was
   ** authenticated.
   **
   ** @return                    the DNSName of the system from which the
   **                            subject was authenticated.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String dns() {
    return this.dns;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>SubjectLocality</code> {@link Builder}.
   **
   ** @return                    a new <code>SubjectLocality</code>
   **                            {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>SubjectLocality.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}