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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   Application.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Application.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee.schema;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Resource;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;

////////////////////////////////////////////////////////////////////////////////
// class Application
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>Application</code> REST resource in Google Apigee Edge.
 ** <p>
 ** <code>Application</code>s are API consumers registered with an API
 ** provider's organization.
 ** <br>
 ** <code>Application</code>s are registered with an organization to obtain
 ** credentials that enable access to one or more API products (or a set of
 ** URIs). The default <code>Application</code> profile can be extended by using
 ** custom attributes. <code>Application</code>s can be associated with
 ** developers (as developer <code>Application</code>s) or with companies
 ** (company <code>Application</code>s).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Application extends Entity<Application> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The JSON tag name of the <code>identifier</code> attribute */
  public static final String ID        = "appId";

  /** The JSON tag name of the <code>name</code> attribute */
  public static final String NAME      = "name";

  /** The JSON tag name of the <code>status</code> attribute */
  public static final String STATUS    = "status";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Read-only system GUID of an <code>Application</code>.
   ** <br>
   ** Useful when querying developer analytics.
   */
  @JsonProperty(ID)
  @Attribute(value=Attribute.IDENTIFIER, mutability=Mutability.IMMUTABLE)
  private String              id;

  /**
   ** The status of the credential for the <code>Application</code> either
   ** <ul>
   **   <li>approved
   **   <li>pending
   ** </ul>
   */
  @JsonProperty(STATUS)
  @Attribute(Attribute.STATUS)
  private String              status;

  /**
   ** The family name containing the <code>Application</code>.
   ** <br>
   ** This property is read-only.
   */
  @Attribute
  @JsonProperty("appFamily")
  private String              family;

  /**
   ** The <code>Application</code> name.
   ** <br>
   ** The primary key within this org/developer's app list.
   */
  @JsonProperty(NAME)
  @Attribute(value=Attribute.UNIQUE)
  private String              name;

  /**
   ** The developer GUID attribute of the developer who owns this app.
   ** <br>
   ** This property is read-only.
   */
  @Attribute
  @JsonProperty("developerId")
  private String              developer;

  /**
   ** The callback URL is used by OAuth 2.0 authorization servers to communicate
   ** authorization codes back to apps.
   */
  @Attribute
  @JsonProperty("callbackUrl")
  private String              callback;

  /**
   ** The collection of extended attribute name-value pairs for the
   ** <code>Application</code>.
   */
  @Attribute
  @JsonProperty("attributes")
  private List<Pair>         attribute;

  /**
   ** The collection of credentials for the <code>Application</code>.
   ** <br>
   ** Credentials are API key/secret pairs associated with API products.
   */
  @Attribute
  @JsonProperty("credentials")
  private List<Credential>    credential;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Credential
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** A certain credentials for the app.
   ** <br>
   ** Credentials are API key/secret pairs associated with API products.
   */
  public static class Credential implements Resource<Credential> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The value of the consumer key for the app.
     */
    @Attribute
    @JsonProperty("consumerKey")
    private String              key;

    /**
     ** The value of the consumer secret for the app.
     */
    @Attribute
    @JsonProperty("consumerSecret")
    private String              secret;

    /**
     ** The status of the consumer key for the credential: 'approved' or 'pending'.
     */
    @Attribute
    @JsonProperty("status")
    private String              status;

    /**
     ** The scope of the active credentials.
     */
    @Attribute
    @JsonProperty("credentialScopes")
    private List<String>        scope;

    @Attribute
    @JsonProperty("attributes")
    private List<Pair>           attribute;

    /**
     ** UNIX time when the <code>Entity</code> was issued.
     ** <br>
     ** This property is read-only.
     */
    @JsonProperty("issuedAt")
    protected Long               issued;

    /**
     ** UNIX time when the <code>Entity</code> was expired.
     ** <br>
     ** This property is read-only.
     */
    @JsonProperty("expiredAt")
    protected Long               expired;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Credential</code> REST Resource that allows
     ** use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Credential() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: key
    /**
     ** Sets the consumer key for the <code>Credential</code>.
     **
     ** @param  value            the consumer key for the
     **                          <code>Credential</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Credential</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Credential</code>.
     */
    public final Credential key(final String value) {
      this.key = value;
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: key
    /**
     ** Returns the consumer key for the <code>Credential</code>.
     **
     ** @return                    the consumer key for the
     **                            <code>Credential</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String key() {
      return this.key;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: secret
    /**
     ** Sets the consumer secret for the <code>Credential</code>.
     **
     ** @param  value            the consumer secret for the
     **                          <code>Credential</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Credential</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Credential</code>.
     */
    public final Credential secret(final String value) {
      this.secret = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: secret
    /**
     ** Returns the consumer secret for the <code>Credential</code>.
     **
     ** @return                  the consumer secret for the
     **                          <code>Credential</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String secret() {
      return this.secret;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: status
    /**
     ** Sets the status of the consumer key for the <code>Credential</code>:
     ** <ul>
     **   <li>approved
     **   <li>pending
     ** </ul>
     **
     ** @param  value            the status of the consumer key for the
     **                          <code>Credential</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Credential</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Credential</code>.
     */
    public final Credential status(final String value) {
      this.status = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: status
    /**
     ** Returns the status of the consumer key for the <code>Credential</code>:
     ** <ul>
     **   <li>approved
     **   <li>pending
     ** </ul>
     **
     ** @return                  the status of the consumer key for the
     **                          <code>Credential</code>.
     **                          <br>
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String status() {
      return this.status;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: issued
    /**
     ** Sets the timestamp of when the credential was issued.
     **
     ** @param  value              the date and time the credential was issued.
     **                            <br>
     **                            Allowed object is {@link Long}.
     **
     ** @return                    the <code>Entity</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>Credential</code>.
     */
    public final Credential issued(final Long value) {
      this.issued = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: issued
    /**
     ** Returns the timestamp when the credential was issued.
     **
     ** @return                    the date and time the credential was issued.
     **                            <br>
     **                            Possible object is {@link Long}.
     */
    public final Long issued() {
      return this.issued;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expired
    /**
     ** Sets he timestamp when the credential was expired.
     **
     ** @param  value              the date and time the credential was expired.
     **                            <br>
     **                            Allowed object is {@link Long}.
     **
     ** @return                    the <code>Entity</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>Credential</code>.
     */
    public final Credential expired(final Long value) {
      this.expired = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expired
    /**
     ** Returns the timestamp when the credential was expired.
     **
     ** @return                    the date and time the credential was expired.
     **                            <br>
     **                            Possible object is {@link Long}.
     */
    public final Long expired() {
      return this.expired;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:  scope
    /**
     ** Sets the scope of the active credentials for the
     ** <code>Credential</code>.
     **
     ** @param  value            the scope of the active credentials for the
     **                          <code>Credential</code>.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     **
     ** @return                  the <code>Credential</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Credential</code>.
     */
    public final Credential scope(final List<String> value) {
      this.scope = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Returns the scope of the active credentials for the
     ** <code>Credential</code>.
     **
     ** @return                  the scope of the active credentials for the
     **                          <code>Credential</code>.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public final List<String> scope() {
      return this.scope;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: attribute
    /**
     ** Sets the extended attribute of the <code>Credential</code> mapped at
     ** <code>name</code>.
     **
     ** @param  name               the name of the  attribute.
     **                            <br>
     **                            Allowed object is {@link String}.
     ** @param  value              the value of the  attribute.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>Credential</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>Credential</code>.
     */
    public final Credential attribute(final String name, final String value) {
      if (this.attribute == null) {
        this.attribute = new ArrayList<Pair>();
      }
      this.attribute.add(new Pair(name, value));
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: attribute
    /**
     ** Return the extended attribute of the <code>Credential</code>.
     ** <p>
     ** This accessor method returns a reference to the live collection, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the JSON object.
     **
     ** @return                    the extended attribute of the
     **                            <code>Credential</code>.
     **                            <br>
     **                            Allowed object is {@link List} where each
     **                            element is of type {@link Pair}.
     */
    public final List<Pair> attribute() {
      return this.attribute;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      return (this.key != null ? this.key.hashCode() : 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Pair</code> attributes are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>Pair</code> attributes may be different even though they
     ** contain the same set of names with the same values, but in a different
     ** order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Credential that = (Credential)other;
      if (this.key != null ? !this.key.equals(that.key) : that.key != null)
        return false;

      if (this.secret != null ? !this.secret.equals(that.secret) : that.secret != null)
        return false;

      if (this.status != null ? !this.status.equals(that.status) : that.status != null)
        return false;

      if (this.issued != null ? !this.issued.equals(that.issued) : that.issued != null)
        return false;

      return !(this.expired != null ? !this.expired.equals(that.expired) : that.expired != null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder("{");
      builder.append("\"key\":\"").append(this.key).append("\"");
      builder.append("}");
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Application</code> REST Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Application() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the system identifier of the <code>Application</code>.
   **
   ** @param  value              the system identifier of the
   **                            <code>Application</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public Application id(final String value) {
    this.id = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the system identifier of the <code>Application</code>.
   **
   ** @return                    the system identifier of the
   **                            <code>Application</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Sets the activation status of the <code>Application</code>.
   **
   ** @param  value              the activation status of the
   **                            <code>Application</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public Application status(final String value) {
    this.status = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the activation status of the <code>Application</code>.
   **
   ** @return                    the activation status of the
   **                            <code>Application</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   family
  /**
   ** Sets the family identifier of the <code>Application</code>.
   **
   ** @param  value              the family identifier of the
   **                            <code>Application</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public final Application family(final String value) {
    this.family = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   family
  /**
   ** Returns the family identifier of the <code>Application</code>.
   **
   ** @return                    the family identifier of the
   **                            <code>Application</code>.
   **                            <br>
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String family() {
    return this.family;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the unique identifier of the <code>Application</code>.
   **
   ** @param  value              the unique identifier of the
   **                            <code>Application</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public final Application name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the unique identifier of the <code>Application</code>.
   **
   ** @return                    the unique identifier of the
   **                            <code>Application</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developer
  /**
   ** Sets the system identifier of the {@link Developer} of the
   ** <code>Application</code>.
   **
   ** @param  value              the system identifier of the {@link Developer}
   **                            of the <code>Application</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public final Application developer(final String value) {
    this.developer = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   developer
  /**
   ** Returns the system identifier of the {@link Developer} of the
   ** <code>Application</code>.
   **
   ** @return                   the system identifier of the {@link Developer}
   **                            of the <code>Application</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String developer() {
    return this.developer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Adds a named-value <code>Pair</code> attribute to the {@link List} of
   ** named-value <code>Pair</code> attributes for the <code>Application</code>.
   **
   ** @param  name               the named-value <code>Pair</code> attributes.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the  attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public final Application attribute(final String name, final String value) {
    if (this.attribute == null) {
      this.attribute = new ArrayList<Pair>();
    }
    this.attribute.add(new Pair(name, value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets the {@link List} of named-value <code>Pair</code> attributes
   ** for the <code>Application</code>.
   **
   ** @param  value              the {@link List} of named-value
   **                            <code>Pair</code> attributes.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public final Application attribute(final List<Pair> value) {
    this.attribute = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Return the {@link List} of named-value <code>Pair</code> attributes for
   ** the <code>Application</code>.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link List} of named-value
   **                            <code>Pair</code> attributes.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   */
  public final List<Pair> attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   callback
  /**
   ** Sets callback URL is used by OAuth 2.0 authorization servers to
   ** communicate authorization codes back to the <code>Application</code>.
   **
   ** @param  value              the callback URL is used by OAuth 2.0
   **                            authorization servers.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public final Application callback(final String value) {
    this.callback = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   callback
  /**
   ** Returns callback URL is used by OAuth 2.0 authorization servers to
   ** communicate authorization codes back to the <code>Application</code>.
   **
   ** @return                    the callback URL is used by OAuth 2.0
   **                            authorization servers.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String callback() {
    return this.callback;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Sets the {@link List} of {@link Credential}s that make up the result set.
   **
   ** @param  value              the {@link List} of {@link Credential}s that
   **                            make up the result set.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Credential}.
   **
   ** @return                    the <code>Application</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Application</code>.
   */
  public final Application credential(final List<Credential> value) {
    this.credential = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Returns the {@link List} of {@link Credential}s that make up the result
   ** set.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link List} of {@link Credential}s that
   **                            make up the result set.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Credential}.
   */
  public final List<Credential> credential() {
    return this.credential;
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
    return PRIME * (this.id != null ? this.id.hashCode() : 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Application</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Application</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different
   ** order.
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

    final Application that = (Application)other;
    return !(this.id != null ? !this.id.equals(that.id) : that.id != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{\"id\":[\"").append(this.id).append("]}");
    return builder.toString();
  }
}