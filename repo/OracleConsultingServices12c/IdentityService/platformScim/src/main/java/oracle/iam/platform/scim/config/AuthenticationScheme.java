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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   AuthenticationScheme.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AuthenticationScheme.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.config;

import java.util.Objects;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import oracle.iam.platform.scim.annotation.Attribute;

import static oracle.iam.platform.scim.annotation.Attribute.Mutability;

////////////////////////////////////////////////////////////////////////////////
// class AuthenticationScheme
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A complex type that specifies supported Authentication Scheme properties.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@JsonPropertyOrder({"name","description","specUri","documentationUri","type","primary"})
public class AuthenticationScheme {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("name")
  @Attribute(description="The common authentication scheme name; e.g., HTTP Basic.", mutability=Mutability.READ_ONLY, required=true)
  private final String  name;

  @JsonProperty("description")
  @Attribute(description="A description of the Authentication Scheme.", mutability=Mutability.READ_ONLY, required=true)
  private final String  description;

  @JsonProperty("specUri")
  @Attribute(description="An HTTP addressable URI pointing to the Authentication Scheme's specification.", mutability=Mutability.READ_ONLY)
  private final URI     specificationURI;

  @JsonProperty("documentationUri")
  @Attribute(description="An HTTP addressable URI pointing to the Authentication Scheme's usage documentation.", mutability=Mutability.READ_ONLY)
  private final URI     documentationURI;

  @JsonProperty("type")
  @Attribute(description="A label indicating the authentication scheme type; e.g., \"oauth\" or \"oauth2\".", mutability=Mutability.READ_ONLY, required=true, canonical={"oauth", "oauth2", "oauthbearertoken", "httpbasic", "httpdigest"})
  private final String  type;

  @JsonProperty("primary")
  @Attribute(description="A Boolean value indicating whether this authentication scheme is preferred.", mutability=Mutability.READ_ONLY, required=true)
  private final boolean primary;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new complex type that specifies supported Authentication
   ** Scheme properties.
   **
   ** @param  name               the common authentication scheme name.
   **                            Allowed object is {@link String}.
   ** @param  description        a description of the Authentication Scheme.
   **                            Allowed object is {@link String}.
   ** @param  specificationURI   an HTTP addressable URI pointing to the
   **                            Authentication Scheme's specification.
   **                            Allowed object is {@link URI}.
   ** @param  documentationURI   an HTTP addressable URI pointing to the
   **                            Authentication Scheme's usage documentation.
   **                            Allowed object is {@link URI}.
   ** @param  type               a label indicating the authentication scheme
   **                            type.
   **                            Allowed object is {@link String}.
   ** @param  primary            a boolean value indicating whether this
   **                            authentication scheme is preferred.
   **                            Allowed object is <code>boolean</code>.
   */
  @JsonCreator
  public AuthenticationScheme(@JsonProperty(value="name", required=true) final String name, @JsonProperty(value="description", required=true) final String description, @JsonProperty(value="specUri") final URI specificationURI, @JsonProperty(value="documentationUri") final URI documentationURI, @JsonProperty(value="type") final String type, @JsonProperty(value="primary", defaultValue="false") final boolean primary) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.name             = name;
    this.description      = description;
    this.specificationURI = specificationURI;
    this.documentationURI = documentationURI;
    this.type             = type;
    this.primary          = primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the common authentication scheme name.
   **
   ** @return                    the common authentication scheme name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the Authentication Scheme.
   **
   ** @return                    the description of the Authentication Scheme.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationURI
  /**
   ** Returns the HTTP addressable URI pointing to the Authentication Scheme's
   ** specification.
   **
   ** @return                    the HTTP addressable URI pointing to the
   **                            Authentication Scheme's specification.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public URI specificationURI() {
    return this.specificationURI;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentationURI
  /**
   ** Returns the HTTP addressable URI pointing to the Authentication Scheme's
   ** usage documentation.
   **
   ** @return                    the HTTP addressable URI pointing to the
   **                            Authentication Scheme's usage documentation.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public URI documentationURI() {
    return this.documentationURI;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the label indicating the Authentication Scheme type.
   **
   ** @return                    the label indicating the Authentication Scheme
   **                            type.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Returns the boolean value indicating whether this authentication
   ** scheme is preferred.
   **
   ** @return                    the boolean value indicating whether this
   **                            authentication scheme is preferred.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean primary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   basicAuthentication
  /**
   ** Factory method that creates a new <code>AuthenticationScheme</code>
   ** instances for HTTP BASIC.
   **
   ** @param  primary            whether this authentication scheme is primary
   **
   ** @return                    a new AuthenticationScheme instances for
   **                            HTTP BASIC.
   */
  public static AuthenticationScheme basicAuthentication(final boolean primary) {
    return new AuthenticationScheme(
      "HTTP Basic"
    , "The HTTP Basic Access Authentication scheme. This scheme is not considered to be a secure method of user authentication (unless used in conjunction with some external secure system such as SSL), as the user name and password are passed over the network as cleartext."
    , UriBuilder.fromUri("http://www.rfc-editor.org/info/rfc2617").build()
    , null
    , "httpbasic"
    , primary
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bearerAuthentication
  /**
   ** Factory method that creates a new <code>AuthenticationScheme</code>
   ** instances for OAuth 2 bearer token.
   **
   ** @param  primary            whether this authentication scheme is primary
   **
   ** @return                    a new AuthenticationScheme instances for
   **                            OAuth 2 bearer token.
   */
  public static AuthenticationScheme bearerAuthentication(final boolean primary) {
    return new AuthenticationScheme(
      "OAuth 2.0 Bearer Token"
    , "The OAuth 2.0 Bearer Token Authentication scheme. OAuth enables clients to access protected resources by obtaining an access token, which is defined in RFC 6750 as 'a string representing an access authorization issued to the client', rather than using the resource owner's credentials directly."
    , UriBuilder.fromUri("http://www.rfc-editor.org/info/rfc6750").build()
    , null
    , "oauthbearertoken"
    , primary
    );
  }

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
   */
  @Override
  public int hashCode() {
    return Objects.hash(
      this.name
    , this.description
    , this.specificationURI
    , this.documentationURI
    , this.type
    , this.primary
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>AuthenticationScheme</code>s are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>AuthenticationScheme</code>s may be different even though they
   ** contain the same set of names with the same values, but in a different
   ** order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final AuthenticationScheme that = (AuthenticationScheme)other;
    return Objects.equals(this.name,             that.name)
        && Objects.equals(this.type,             that.type)
        && Objects.equals(this.primary,          that.primary)
        && Objects.equals(this.specificationURI, that.specificationURI)
        && Objects.equals(this.description,      that.description)
     ;
  }
}