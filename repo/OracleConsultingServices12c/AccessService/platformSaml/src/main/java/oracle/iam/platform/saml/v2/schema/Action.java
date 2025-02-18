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

    File        :   Action.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Action.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2.schema;

////////////////////////////////////////////////////////////////////////////////
// final class Action
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** SAML 2.0 Core Action.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Action {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Element local name. */
  public static final String LOCAL            = "Action";

  /** Name of the Namespace attribute. */
  public static final String NAMEPSACE        = "Namespace";

  /** Read/Write/Execute/Delete/Control action namespace. */
  public static final String RWEDC            = "urn:oasis:names:tc:SAML:1.0:action:rwedc";

  /** Read/Write/Execute/Delete/Control negation action namespace. */
  public static final String RWEDC_NEGATION   = "urn:oasis:names:tc:SAML:1.0:action:rwedc-negation";

  /** Get/Head/Put/Post action namespace. */
  public static final String GHPP             = "urn:oasis:names:tc:SAML:1.0:action:ghpp";

  /** UNIX file permission action namespace. */
  public static final String UNIX             = "urn:oasis:names:tc:SAML:1.0:action:unix";

  /** Read action. */
  public static final String READ             = "Read";

  /** Write action. */
  public static final String WRITE            = "Write";

  /** Execute action. */
  public static final String EXECUTE          = "Execute";

  /** Delete action. */
  public static final String DELETE           = "Delete";

  /** Control action. */
  public static final String CONTROL          = "Control";

  /** Negated Read action. */
  public static final String READ_NEGATION    = "~Read";

  /** Negated Write action. */
  public static final String WRITE_NEGATION   = "~Write";

  /** Negated Execute action. */
  public static final String EXECUTE_NEGATION = "~Execute";

  /** Negated Delete action. */
  public static final String DELETE_NEGATION  = "~Delete";

  /** Negated Control action. */
  public static final String CONTROL_NEGATION = "~Control";

  /** HTTP GET action. */
  public static final String HTTP_GET         = "GET";

  /** HTTP HEAD action. */
  public static final String HTTP_HEAD        = "HEAD";

  /** HTTP PUT action. */
  public static final String HTTP_PUT         = "PUT";

  /** HTTP POST action. */
  public static final String HTTP_POST        = "POST";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** URI of the Namespace of the Action. */
  private final String       namespace;

  /** Action value. */
  private final String       value;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Builder</code> used for building the {@link Action}.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** URI of the Namespace of the Action. */
    private String namespace;

    /** Action value. */
    private String value;

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
    // Method: namespace
    /**
     ** Sets the namespace scope of the specified action.
     **
     ** @param  value            the namespace scope of the specified action.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder namespace(final String value) {
      this.namespace = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the URI of the action to be performed.
     **
     ** @param  value            the URI of the action to be performed.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose with configured <code>id</code>
     **                          parameter.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder value(final String value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a SAML 2.0 {@link Action}.
     **
     ** @return                  the SAML 2.0 {@link Action}.
     **                          <br>
     **                          Possible object is {@link Action}.
     */
    public Action build() {
      return new Action(this);
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
  private Action(final Builder builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.value     = builder.value;
    this.namespace = builder.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the namespace scope of the specified action value.
   **
   ** @return                    the namespace scope of the specified action
   **                            value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String namespace() {
    return this.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the URI of the action to be performed.
   **
   ** @return                   the URI of the action to be performed.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>Action</code> {@link Builder}.
   **
   ** @return                    a new <code>Action</code> {@link Builder}.
   **                            <br>
   **                            Possible object is <code>Action.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}