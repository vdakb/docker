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

    File        :   SubjectConfirmationData.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SubjectConfirmationData.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

import java.time.ZonedDateTime;

////////////////////////////////////////////////////////////////////////////////
// final class SubjectConfirmationData
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** SAML 2.0 Core SubjectConfirmationData.
 ** <p>
 ** The <code>SubjectConfirmationData</code> property refers to an extensible
 ** piece of data that can be customized for a specific authentication protocol.
 ** <p>
 ** The <code>SubjectConfirmationData</code> property corresponds to the
 ** <code>&lt;saml:SubjectConfirmationData&gt;</code> element that is defined in
 ** the Assertions and Protocol for the OASIS Security Assertion Markup Language
 ** (SAML) specification.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SubjectConfirmationData {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Element local name. */
  public static final String  LOCAL       = "SubjectConfirmationData";

  /** NotBefore attribute name. */
  public static final String  BEFORE      = "NotBefore";

  /** NotOnOrAfter attribute name. */
  public static final String  AFTER       = "NotOnOrAfter";

  /** Recipient attribute name. */
  public static final String  RECIPIENT   = "Recipient";

  /** InResponseTo attribute name. */
  public static final String  RESPONSE_TO = "InResponseTo";

  /** Address attribute name. */
  public static final String  ADDRESS     = "Address";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** NotBefore of the Confirmation Data. */
  private final ZonedDateTime before;

  /** NotOnOrAfter of the Confirmation Data. */
  private final ZonedDateTime after;

  /** Recipient of the Confirmation Data. */
  private final String        recipient;

  /** InResponseTo of the Confirmation Data. */
  private final String        responseTo;

  /** Address of the Confirmation Data. */
  private final String        address;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the
   ** {@link SubjectConfirmationData}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** NotBefore of the Confirmation Data. */
    private ZonedDateTime before;

    /** NotOnOrAfter of the Confirmation Data. */
    private ZonedDateTime after;

    /** Recipient of the Confirmation Data. */
    private String        recipient;

    /** InResponseTo of the Confirmation Data. */
    private String        responseTo;

    /** Address of the Confirmation Data. */
    private String        address;

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
     ** Sets the time before which the subject is not valid.
     **
     ** @param  value            the time before which the subject is not
     **                          valid.
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
     ** Sets the time at, or after, which the subject is not valid.
     **
     ** @param  value            the time at, or after, which the subject is
     **                          not valid.
     **                          <br>
     **                          Allowed object is {@link ZonedDateTime}.
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
    // Method: recipient
    /**
     ** Sets the recipient of the subject.
     **
     ** @param  value            the recipient of the subject.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder recipient(final String value) {
      this.recipient = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: responseTo
    /**
     ** Sets the message ID this is in response to.
     **
     ** @param  value            the message ID this is in response to.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder responseTo(final String value) {
      this.responseTo = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: address
    /**
     ** Sets the IP address to which this information may be pressented.
     **
     ** @param  value            the IP address to which this information may be
     **                          pressented.
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
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 Core
     ** {@link SubjectConfirmationData}.
     **
     ** @return                  the SAML 2.0 Core
     **                          {@link SubjectConfirmationData}.
     **                          <br>
     **                          Possible object is
     **                          {@link SubjectConfirmationData}.
     */
    public SubjectConfirmationData build() {
      return new SubjectConfirmationData(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SubjectConfirmationData</code> with the specified
   ** {@link Builder}.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            configure.
   **                            <br>
   **                            If some parameters is not set, the default
   **                            values are used.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private SubjectConfirmationData(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.after      = builder.after;
    this.before     = builder.before;
    this.address    = builder.address;
    this.recipient  = builder.recipient;
    this.responseTo = builder.responseTo;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
  /**
   ** Returns the time before which this subject is not valid.
   **
   ** @return                    the message ID this is in response to.
   **                            <br>
   **                            Possible object is {@link ZonedDateTime}.
   */
  public final ZonedDateTime before() {
    return this.before;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   after
  /**
   ** Returns the time at, or after, which this subject is not valid.
   **
   ** @return                    the time at, or after, which this subject is
   **                            not valid.
   **                            <br>
   **                            Possible object is {@link ZonedDateTime}.
   */
  public final ZonedDateTime after() {
    return this.after;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseTo
  /**
   ** Returns the message ID this is in response to.
   **
   ** @return                    the message ID this is in response to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String responseTo() {
    return this.responseTo;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   recipient
  /**
   ** Returns the recipient of this subject.
   **
   ** @return                    the recipient of this subject.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String recipient() {
    return this.recipient;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   address
  /**
   ** Returns the IP address to which this information may be pressented.
   **
   ** @return                    the IP address to which this information may
   **                            be pressented.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String address() {
    return this.address;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>SubjectConfirmationData</code>
   ** {@link Builder}.
   **
   ** @return                    a new <code>SubjectConfirmationData</code>
   **                            {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>SubjectConfirmationData.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}