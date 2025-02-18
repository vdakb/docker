/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Credential.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Credential.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// final class Credential
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The base REST user credential entity representation.
 ** <br>
 ** This object contains all of the attributes required of user authentication.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema("__CREDENTIAL__")
@JsonIgnoreProperties({"createdDate", "credentialData", "secretData"})
public final class Credential extends Entity<Credential> {

  ////////////////////////////////////////////////////////////////////////////
  // instance attributes
  ////////////////////////////////////////////////////////////////////////////

  /**
   ** The type of the credential.
   */
  @Attribute
  @JsonProperty
  private CredentialType type;
  /**
   ** A Boolean value indicating the the <code>Credential</code> is temporary
   ** used only.
   */
  @JsonProperty
  @Attribute(required=true)
  private Boolean        temporary;
  /**
   ** The human readable name of the credential.
   */
  @JsonProperty("userLabel")
  @Attribute(required=true)
  private String         label;
  /**
   ** The value a credential.
   */
  @Attribute
  @JsonProperty
  private String         value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Credential</code> REST representation that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Credential() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>Credential</code> REST representation whth the
   ** specified type and value.
   **
   ** @param  type               the type of the <code>Credential</code>.
   **                            <br>
   **                            Allowed object is {@link CredentialType}.
   ** @param  value              the value of the <code>Credential</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Credential(final CredentialType type, final String value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type  = type;
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: type
  /**
   ** Sets the type of a <code>Credential</code>.
   **
   ** @param  value              the type of a <code>Credential</code>.
   **                            <br>
   **                            Allowed object is {@link CredentialType}.
   **
   ** @return                    the <code>Credential</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Credential</code>.
   */
  public final Credential type(final CredentialType value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of a <code>Credential</code>.
   **
   ** @return                  the type of a <code>Credential</code>.
   **                          <br>
   **                          Possible object is {@link CredentialType}.
   */
  public final CredentialType type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   temporary
  /**
   ** Wheter the <code>Credential</code> is temporary used only.
   **
   ** @param  value              <code>true</code> if the
   **                            <code>Credential</code> is temporary used only.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Credential</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Credential</code>.
   */
  public final Credential temporary(final Boolean value) {
    this.temporary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: temporary
  /**
   ** Returns <code>true</code> <code>Credential</code> is temporary used
   ** only.
   **
   ** @return                    <code>true</code> if the
   **                            <code>Credential</code> is temporary used only;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean temporary() {
    return this.temporary == null ? false : this.temporary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Sets the human readable label of a <code>Credential</code>.
   **
   ** @param  value              the human readable label of a
   **                            <code>Credential</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Credential</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Credential</code>.
   */
  public final Credential label(final String value) {
    this.label = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the human readable label of a <code>Credential</code>.
   **
   ** @return                    the human readable label of a
   **                            <code>Credential</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String label() {
    return this.label;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the algorithm of a password <code>Credential</code>.
   **
   ** @param  value              the algorithm of a password
   **                            <code>Credential</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Credential</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Credential</code>.
   */
  public final Credential value(final String value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: value
  /**
   ** Returns the value of a password <code>Credential</code>.
   **
   ** @return                    the algorithm of a password
   **                            <code>Credential</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String algorithm() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Resource)
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
    return Objects.hash(
      this.id
    , this.type
    , this.value
    , this.label
    , this.temporary
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  password
  /**
   ** Factory method to create a password <code>Credential</code> from the given
   ** string value.
   **
   ** @param  label              the human readable label of the
   **                            <code>Credential</code> to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the string value of the credential.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  temporary          whether the password <code>Credential</code> is
   **                            temporarly usebale only.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Credential</code> populated with
   **                            the properties provided.
   **                            <br>
   **                            Possible object is <code>Credential</code>.
   */
  public static Credential password(final String label, final String value, final Boolean temporary) {
    return new Credential(CredentialType.PASSWORD, value).label(label).temporary(temporary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  timeBased
  /**
   ** Factory method to create a <i>Time Based One-Time Password</i>
   ** <code>Credential</code> from the given string value.
   **
   ** @param  label              the human readable label of the
   **                            <code>Credential</code> to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the string value of the credential.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  temporary          whether the password <code>Credential</code> is
   **                            temporarly usebale only.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Credential</code> populated with
   **                            the properties provided.
   **                            <br>
   **                            Possible object is <code>Credential</code>.
   */
  public static Credential timeBased(final String label, final String value, final Boolean temporary) {
    return new Credential(CredentialType.TOTP, value).label(label).temporary(temporary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  counterBased
  /**
   ** Factory method to create a <i>Counter Based One-Time Password</i>
   ** <code>Credential</code> from the given string value.
   **
   ** @param  label              the human readable label of the
   **                            <code>Credential</code> to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the string value of the credential.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  temporary          whether the password <code>Credential</code> is
   **                            temporarly usebale only.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Credential</code> populated with
   **                            the properties provided.
   **                            <br>
   **                            Possible object is <code>Credential</code>.
   */
  public static Credential counterBased(final String label, final String value, final Boolean temporary) {
    return new Credential(CredentialType.HOTP, value).label(label).temporary(temporary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Credential</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Credential</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Credential that = (Credential)other;
    return Objects.equals(this.id,         that.id)
        && Objects.equals(this.type,       that.type)
        && Objects.equals(this.value,      that.value)
        && Objects.equals(this.label,      that.label)
        && Objects.equals(this.temporary,  that.temporary)
    ;
  }
}