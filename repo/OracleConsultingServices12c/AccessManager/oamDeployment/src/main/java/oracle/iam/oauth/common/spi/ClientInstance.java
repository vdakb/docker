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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ClientInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ClientInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.net.URL;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ClientInstance
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>ClientInstance</code> represents a Client in Oracle Access Manager
 ** infrastructure that might be created, deleted or configured after or during
 ** an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ClientInstance extends DependendInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Type                   type;
  private String                 uid;
  private List<URL>              redirect;
  private Map<String, Grant>     grant;
  private Map<String, Scope>     scope;
  private Map<String, Attribute> attribute;

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  public enum Type {

    /** ??? */
    PUBLIC("public",             "PUBLIC_CLIENT"),
    MOBILE("mobile",             "MOBILE_CLIENT"),
    CONFIDENTIAL("confidential", "CONFIDENTIAL_CLIENT");

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-3327851736750945526")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final String logical;
    final String physical;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Type(final String logical, final String physical) {
      this.logical  = logical;
      this.physical = physical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: logical
    /**
     ** Returns the logical name of the <code>Type</code>.
     **
     ** @return                    the logical name of the <code>Type</code>.
     */
    public String logical() {
      return this.logical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: physical
    /**
     ** Returns the physical name of the <code>Type</code>.
     **
     ** @return                    the physical name of the <code>Type</code>.
     */
    public final String physical() {
      return this.physical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Type</code> property from the
     ** given string value by comparing the logical names.
     **
     ** @param  value              the string value the <code>Type</code> should
     **                            be returned for.
     **
     ** @return                    the <code>Type</code> property.
     */
    public static Type from(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.logical.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Grant
  // ~~~~ ~~~~~
  public enum Grant {

      /** ??? */
      AUTHORIZATION("authorization", "AUTHORIZATION_CODE")
    , CREDENTIAL("credential",       "CLIENT_CREDENTIALS")
    , BEARER("bearer",               "JWT_BEARER")
    , EXCHANGE("exchange",           "TOKEN_EXCHANGE")
    , PASSWORD("password",           "PASSWORD")
    , REFERSH("refresh",             "REFRESH_TOKEN");

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4713281666189885128")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final String logical;
    final String physical;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Grant</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Grant(final String logical, final String physical) {
      this.logical  = logical;
      this.physical = physical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: logical
    /**
     ** Returns the logical name of the <code>GrantType</code>.
     **
     ** @return                    the logical name of the
     **                            <code>GrantType</code>.
     */
    public String logical() {
      return this.logical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: physical
    /**
     ** Returns the physical name of the <code>GrantType</code>.
     **
     ** @return                    the physical name of the
     **                            <code>GrantType</code>.
     */
    public final String physical() {
      return this.physical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>GrantType</code> property from
     ** the given string value by comparing the logical names.
     **
     ** @param  value              the string value the <code>GrantType</code>
     **                            should be returned for.
     **
     ** @return                    the <code>GrantType</code> property.
     */
    public static Grant from(final String value) {
      for (Grant cursor : Grant.values()) {
        if (cursor.logical.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum AttributeType
  // ~~~~ ~~~~~~~~~~~~~~~~~~
  public enum AttributeType {

    /** ??? */
    STATIC("static",   "STATIC"),
    DYNAMIC("dynamic", "DYNAMIC");

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:5563724573053274406")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final String logical;
    final String physical;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AttributeType</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    AttributeType(final String logical, final String physical) {
      this.logical  = logical;
      this.physical = physical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: logical
    /**
     ** Returns the logical name of the <code>AttributeType</code>.
     **
     ** @return                    the logical name of the
     **                            <code>AttributeType</code>.
     */
    public String logical() {
      return this.logical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: physical
    /**
     ** Returns the physical name of the <code>AttributeType</code>.
     **
     ** @return                    the physical name of the
     **                            <code>AttributeType</code>.
     */
    public final String physical() {
      return this.physical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>AttributeType</code>
     ** property from the given string value by comparing the logical names.
     **
     ** @param  value              the string value the
     **                            <code>AttributeType</code> should be returned
     **                            for.
     **
     ** @return                    the <code>AttributeType</code> property.
     */
    public static AttributeType from(final String value) {
      for (AttributeType cursor : AttributeType.values()) {
        if (cursor.logical.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Scope
  // ~~~~~ ~~~~~
  public static class Scope {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String name;
    private String prefix;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Scope</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Scope() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Scope</code> with the specified properties
     **
     ** @param  prefix             the prefix of the scope to add referencing an
     **                            <code>Resource Server</code>.
     ** @param  name               the name of the scope to add referencing an
     **                            scope of the <code>Resource Server</code>.
     */
    private Scope(final String prefix, final String name) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.prefix = prefix;
      this.name   = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Called to inject the argument for parameter <code>name</code>.
     **
     ** @param  name               the identifier of the <code>Scope</code>.
     */
    public final void name(final String name) {
      this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the <code>Scope</code>.
     **
     ** @return                    the name of the <code>Scope</code>.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix
    /**
     ** Called to inject the argument for parameter <code>prefix</code>.
     **
     ** @param  prefix             the prefix of the <code>Scope</code>.
     */
    public final void prefix(final String prefix) {
      this.prefix = prefix;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Returns the description of the <code>Scope</code>.
     **
     ** @return                    the prefix of the <code>Scope</code>.
     */
    public final String prefix() {
      return this.prefix;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   marshal
    /**
     ** Returns a string representation of this instance in JSON format.
     ** <br>
     ** Adjacent elements are separated by the character "," (comma).
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                    the string representation of this instance in
     **                            JSON format.
     */
    public final String marshal() {
      return String.format("\"%s.%s\"", this.prefix,  this.name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   unmarshal
    /**
     ** Returns a <code>Scope</code> by parsing the given JSON string.
     **
     ** @param  json               the JSON String to parse.
     **
     ** @return                    the <code>Scope</code> parsed from the JSON
     **                            string.
     */
    public static void unmarshal( final ClientInstance instance, final String json) {
      int start = 0;
      int limit = json.length();
      // skip leading whitspace and the square bracket
      while (start < limit && (json.charAt(start) <= ' ' || json.charAt(start) == '['))
        start++;
      // skip trailing whitspace and the square bracket
      while ((limit > 0) && (json.charAt(limit - 1) <= ' ')) {
        limit--;
      }
      final String[] scopes = json.substring(start, limit).split(",");
      for (String scope : scopes) {
        final String[] part = scope.split(".");
        instance.addScope(part[0], part[1]);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  public static class Attribute {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String        id;
    private String        value;
    private AttributeType type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Attribute() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Called to inject the argument for parameter <code>id</code>.
     **
     ** @param  id               the identifier of the <code>Attribute</code>.
     */
    public final void id(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the identifier of the <code>Attribute</code>.
     **
     ** @return                    the identifier of the <code>Attribute</code>.
     */
    public final String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Called to inject the argument for parameter <code>value</code>.
     **
     ** @param  value            the value of the <code>Attribute</code>.
     */
    public final void value(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Attribute</code>.
     **
     ** @return                    the value of the <code>Attribute</code>.
     */
    public final String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Called to inject the argument for parameter <code>type</code>.
     **
     ** @param  type             the type of the <code>Attribute</code>.
     */
    public final void type(final String type) {
      type(AttributeType.from(type));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Called to inject the argument for parameter <code>type</code>.
     **
     ** @param  type             the type of the <code>Attribute</code>.
     */
    public final void type(final AttributeType type) {
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the type of the <code>Attribute</code>.
     **
     ** @return                    the type of the <code>Attribute</code>.
     */
    public final AttributeType type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   marshal
    /**
     ** Returns a string representation of this instance in JSON format.
     ** <br>
     ** Adjacent elements are separated by the character "," (comma).
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                    the string representation of this instance in
     **                            JSON format.
     */
    public final String marshal() {
      final StringBuilder builder = new StringBuilder(ENTITY_OPEN);
      builder.append(String.format(ATTRIBUTE_STRING, ClientProperty.ATTRIBUTNAME.id(), this.id));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_STRING, ClientProperty.ATTRIBUTVALUE.id(), this.value));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_STRING, ClientProperty.ATTRIBUTTYPE.id(), this.type.physical));
      builder.append(ENTITY_CLOSE);
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ClientInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ClientInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Called to inject the argument for attribute <code>type</code>.
   **
   ** @param  type               the id of the resource client instance.
   */
  public final void type(final String type) {
    type(Type.from(type));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Called to inject the argument for attribute <code>type</code>.
   **
   ** @param  type               the type of the resource client instance.
   */
  public final void type(final Type type) {
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Called to inject the argument for attribute <code>uid</code>.
   **
   ** @param  uid                the uid of the resource client instance.
   */
  public final void uid(final String uid) {
    this.uid = uid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Returns the uid the resource client instance.
   **
   ** @return                    the uid of the resource client instance.
   */
  public final Type uid() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domain (DependendInstance)
  /**
   ** Called to inject the argument for parameter <code>domain</code>.
   **
   ** @param  domain             the name of the <code>Identity Domain</code>
   **                            under which the <code>Resource Client</code> is
   **                            created, modified or deleted.
   */
  @Override
  public final void domain(final String domain) {
    // validate the the given value is proper
    if (StringUtility.isEmpty(domain))
      handleAttributeMandatory(ClientProperty.DOMAIN.id());

    this.parameter().put(ClientProperty.DOMAIN.id(), domain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domain
  /**
   ** Returns the name of the <code>Identity Domain</code> the
   ** <code>Resource Client</code> belongs to.
   **
   ** @return                    the name of the <code>Identity Domain</code>
   **                            under which the <code>Resource Client</code>
   **                            is created, modified or deleted.
   */
  @Override
  public final String domain() {
    return stringParameter(ClientProperty.DOMAIN.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a scope.
   **
   ** @param  prefix             the prefix of the scope to add referencing an
   **                            <code>Resource Server</code>.
   ** @param  name               the name of the scope to add referencing an
   **                            scope of the <code>Resource Server</code>.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Scope} with the same name.
   */
  protected final void addScope(final String prefix, final String name)
    throws BuildException {

    add(new Scope(prefix, name));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a scope.
   **
   ** @param  scope               the scope to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Scope} with the same name.
   */
  public final void add(final Scope scope)
    throws BuildException {

    // lazy initialization of the scope container
    if (this.scope == null)
      this.scope = new LinkedHashMap<String, Scope>();

    // verify if a token with the same type is already contained
    if (this.scope.keySet().contains(scope.name))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, scope.name));

    this.scope.put(scope.name, scope);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a scope.
   **
   ** @param  attribute          the attribute to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Scope} with the same name.
   */
  public final void add(final Attribute attribute)
    throws BuildException {

    // lazy initialization of the scope container
    if (this.attribute == null)
      this.attribute = new LinkedHashMap<String, Attribute>();

    // verify if a token with the same type is already contained
    if (this.attribute.keySet().contains(attribute.id))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, attribute.id));

    this.attribute.put(attribute.id, attribute);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a grant.
   **
   ** @param  grant               the grant to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Grant} with the same name.
   */
  public final void add(final Grant grant)
    throws BuildException {

    // lazy initialization of the scope container
    if (this.grant == null)
      this.grant = new LinkedHashMap<String, Grant>();

    // verify if a token with the same type is already contained
    if (this.grant.keySet().contains(grant.logical))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, grant.logical));

    this.grant.put(grant.logical, grant);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a redirect.
   **
   ** @param  redirect           the redirect to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link URL} with the same externmal name.
   */
  public final void add(final URL redirect)
    throws BuildException {

    // lazy initialization of the scope container
    if (this.redirect == null)
      this.redirect = new ArrayList<URL>();

    // verify if a token with the same type is already contained
    if (this.redirect.contains(redirect))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, redirect.toExternalForm()));

    this.redirect.add(redirect);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate  (overridden)
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @param  operation          the {@link ServiceOperation} to validate for
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#modify}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#report}
   **                            </ul>
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final ServiceOperation operation) {
    // ensure inheritance
    super.validate();

    if (StringUtility.isEmpty(domain()))
      handleAttributeMissing(ClientProperty.DOMAIN.id());

    // only create and modify commands requires specific mandatory parameters
    if (operation == ServiceOperation.none ||operation == ServiceOperation.delete || operation == ServiceOperation.print)
      return;

    // initialize instance with default values if a value is not provided by
    // the frontend for a parameter which has a default
    final HashMap<String, Object> parameter = this.parameter();
    for (ClientProperty cursor : ClientProperty.values()) {
      if (cursor.required() && cursor.defaultValue() != null && !parameter.containsKey(cursor.id))
        add(cursor, cursor.defaultValue());
    }

    // validate all required parameter
    // the frontend for a parameter which has a default
    for (ClientProperty cursor : ClientProperty.values()) {
      if (cursor.required() && !parameter.containsKey(cursor.id))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, cursor.id));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Returns a string representation of this instance in JSON format.
   ** <br>
   ** Adjacent elements are separated by the character "," (comma).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance in
   **                            JSON format.
   */
  public final String marshal() {
    final StringBuilder builder = new StringBuilder(ENTITY_OPEN);
    // if identifier is available put it on the payload
    if (!StringUtility.isEmpty(id())) {
      builder.append(String.format(ATTRIBUTE_STRING, ClientProperty.ID.id(), id()));
      builder.append(ADJACENT_SEPARATOR);
    }
    builder.append(String.format(ATTRIBUTE_STRING, ClientProperty.NAME.id(), name()));
    if (this.type != null) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_STRING, ClientProperty.TYPE.id(), this.type.physical()));
    }
    final Map<String, Object> parameter = parameter();
    for (Map.Entry<String, Object> entry : parameter.entrySet()) {
      builder.append(ADJACENT_SEPARATOR);
      final ClientProperty property = ClientProperty.from(entry.getKey());
      if (property.type == ClientProperty.Type.STRING)
        builder.append(String.format(ATTRIBUTE_STRING, entry.getKey(), entry.getValue()));
      else
        builder.append(String.format(ATTRIBUTE_RELAX, entry.getKey(), entry.getValue()));
    }
    if (this.scope != null && this.scope.size() > 0) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, ClientProperty.SCOPES.id(), ARRAY_OPEN));
      int i = 0;
      for (Scope scope : this.scope.values()) {
        if (i++ > 0)
          builder.append(ADJACENT_SEPARATOR);
        builder.append(scope.marshal());
      }
      builder.append(ARRAY_CLOSE);
    }
    if (this.attribute != null && this.attribute.size() > 0) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, ClientProperty.ATTRIBUTES.id(), ARRAY_OPEN));
      int i = 0;
      for (Attribute attribute : this.attribute.values()) {
        if (i++ > 0)
          builder.append(ADJACENT_SEPARATOR);
        builder.append(attribute.marshal());
      }
      builder.append(ARRAY_CLOSE);
    }
    if (this.grant != null && this.grant.size() > 0) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, ClientProperty.GRANTTYPES.id(), ARRAY_OPEN));
      int i = 0;
      for (Grant grant : this.grant.values()) {
        if (i++ > 0)
          builder.append(ADJACENT_SEPARATOR);
        builder.append(String.format("\"%s\"", grant.physical()));
      }
      builder.append(ARRAY_CLOSE);
    }
    if (this.redirect != null && this.redirect.size() > 0) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, ClientProperty.REDIRECTURIS.id(), ARRAY_OPEN));
      int i = 0;
      for (URL url : this.redirect) {
        if (i++ > 0)
          builder.append(ADJACENT_SEPARATOR);
        builder.append(ENTITY_OPEN);
        builder.append(String.format(ATTRIBUTE_STRING, ClientProperty.REDIRECTURL.id(), url.toExternalForm()));
        builder.append(ADJACENT_SEPARATOR);
        // the parameter isHttps is never used and present only for backward
        // compaitbility hance default to the same value as the API does
        builder.append(String.format(ATTRIBUTE_RELAX, ClientProperty.REDIRECTSECURE.id(), true));
        builder.append(ENTITY_CLOSE);
      }
      builder.append(ARRAY_CLOSE);
    }
    builder.append(ENTITY_CLOSE);
    return builder.toString();
  }
}