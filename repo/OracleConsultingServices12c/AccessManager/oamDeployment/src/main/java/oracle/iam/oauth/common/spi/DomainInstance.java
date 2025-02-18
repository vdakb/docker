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

    File        :   DomainInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DomainInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.LinkedHashMap;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class DomainInstance
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>DomainInstance</code> represents an Identity Domain in Oracle Access
 ** Manager infrastructure that might be created, deleted or configured after or
 ** during an import operation.
 ** <p>
 ** A <code>Identity Domain</code> is a entity that contain all artifacts
 ** required to provide standard OAuth Services .
 ** <p>
 ** Each <code>Identity Domain</code> is an independent entity.
 ** <br>
 ** One of the primary use cases of the <code>Identity Domain</code> is for
 ** multi tenants deployments. Each <code>Identity Domain</code> will correspond
 ** to a tenant. This can apply to different departments in an organization if
 ** there is a need for independence. This will also be useful for cloud
 ** deployments where each <code>Identity Domain</code> can correspond to a
 ** separate tenant or entity. The following artifacts are just some of the
 ** components configured within an OAuth Services <code>Identity Domain</code>.
 ** <ul>
 **   <li>One or more Clients
 **   <li>One or more Resource Servers
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DomainInstance extends FeatureInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<TokenType, Token>            token;
  private Map<String,    Attribute>        attribute;
  private Map<String,    ResourceInstance> resource;
  private Map<String,    ClientInstance>   client;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum TokenType
  // ~~~~ ~~~~~~~~~
  public enum TokenType {

    /** ??? */
    ACCESS("access", "ACCESS_TOKEN"),
    AUTHZ("authz",   "AUTHZ_CODE"),
    LINK("link",     "SSO_LINK_TOKEN");

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4076685317866121248")
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
     ** Constructs a <code>TokenType</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    TokenType(final String logical, final String physical) {
      this.logical  = logical;
      this.physical = physical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: logical
    /**
     ** Returns the logical name of the <code>TokenType</code>.
     **
     ** @return                    the logical name of the
     **                            <code>TokenType</code>.
     */
    public String logical() {
      return this.logical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: physical
    /**
     ** Returns the physical name of the <code>TokenType</code>.
     **
     ** @return                    the physical name of the
     **                            <code>TokenType</code>.
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
     ** Factory method to create a proper <code>TokenType</code> property from
     ** the given string value by comparing the logical names.
     **
     ** @param  value              the string value the <code>TokenType</code>
     **                            should be returned for.
     **
     ** @return                    the <code>TokenType</code> property.
     */
    public static TokenType from(final String value) {
      for (TokenType cursor : TokenType.values()) {
        if (cursor.logical.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class TokenLifeCycle
  // ~~~~~ ~~~~~~~~~~~~~~
  public static class TokenLifeCycle {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int     expiry  = -1;
    private boolean enabled = false;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>TokenLifeCycle</code> task that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public TokenLifeCycle() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: expiry
    /**
     ** Called to inject the argument for parameter <code>expiry</code>.
     **
     ** @param  expiry             the expiry time of the <code>Token</code>.
     */
    public final void expiry(final int expiry) {
      this.expiry = expiry;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expiry
    /**
     ** Returns the expiry time of the <code>Token</code>.
     **
     ** @return                    the expiry time of the <code>Token</code>.
     */
    public final int expiry() {
      return this.expiry;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: enabled
    /**
     ** Called to inject the argument for parameter <code>enabled</code>.
     **
     ** @param  enabled          <code>true</code> if the life cycle of the
     **                          <code>Token</code> is enabled; otherwise
     **                          <code>false</code>.
     */
    public final void enabled(final boolean enabled) {
      this.enabled = enabled;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: enabled
    /**
     ** Returns the life cycle state of the <code>Token</code>.
     **
     ** @return                  <code>true</code> if the life cycle of the
     **                          <code>Token</code> is enabled; otherwise
     **                          <code>false</code>.
     */
    public final boolean enabled() {
      return this.enabled;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Token
  // ~~~~~ ~~~~~
  public static class Token extends TokenLifeCycle {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private TokenType      type;
    private TokenLifeCycle refreshLifeCycle;
    private boolean        refreshEnabled   = false;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Token</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Token() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Called to inject the argument for parameter <code>type</code>.
     **
     ** @param  type             the type of the <code>Token</code>.
     */
    public final void type(final String type) {
      type(TokenType.from(type));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Called to inject the argument for parameter <code>type</code>.
     **
     ** @param  type             the type of the <code>Token</code>.
     */
    public final void type(final TokenType type) {
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the type of the <code>Token</code>.
     **
     ** @return                    the type of the <code>Token</code>.
     */
    public final TokenType type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshEnabled
    /**
     ** Called to inject the argument for parameter <code>refreshEnabled</code>.
     **
     ** @param  enabled          <code>true</code> if the refresh life cycle of
     **                          the <code>Token</code> is enabled; otherwise
     **                          <code>false</code>.
     */
    public final void refreshEnabled(final boolean enabled) {
      this.refreshEnabled = enabled;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshEnabled
    /**
     ** Returns the  refresh life cycle state of the <code>Token</code>.
     **
     ** @return                  <code>true</code> if the refresh life cycle of
     **                          the <code>Token</code> is enabled; otherwise
     **                          <code>false</code>.
     */
    public final boolean refreshEnabled() {
      return this.refreshEnabled;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshLifeCyle
    /**
     ** Called to inject the argument for parameter <code>refresh</code>.
     **
     ** @param  refresh          the refresh life of the <code>Token</code>
     **                          itself.
     */
    public final void refreshLifeCyle(final TokenLifeCycle refresh) {
      if (this.refreshLifeCycle != null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "tokenRefresh"));

      this.refreshLifeCycle = refresh;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshLifeCyle
    /**
     ** Returns the refresh life of the <code>Token</code> itself.
     **
     ** @return                  the refresh life of the <code>Token</code>
     **                          itself.
     */
    public final TokenLifeCycle refreshLifeCyle() {
      return this.refreshLifeCycle;
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
      builder.append(String.format(ATTRIBUTE_STRING, DomainProperty.TOKENTYPE.id(),   this.type().physical()));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, DomainProperty.TOKENEXPIRY.id(), this.expiry() == -1 ? 3600 : this.expiry()));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, DomainProperty.TOKENLIFECYCLE.id(), this.enabled()));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, DomainProperty.REFRESHTOKENENABLED.id(), this.refreshEnabled));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, DomainProperty.REFRESHTOKENEXPIRY.id(), this.refreshLifeCycle.expiry == -1 ? 86400 : this.refreshLifeCycle.expiry));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, DomainProperty.REFRESHTOKENLIFECYCLE.id(), this.refreshLifeCycle.enabled));
      builder.append(ENTITY_CLOSE);
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  public static class Attribute {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String id;
    private String value;

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
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DomainInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DomainInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  Collection<ClientInstance> clients() {
    return this.client.values();
  }

  Collection<ResourceInstance> resources() {
    return this.resource.values();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  property           the name of the parameter to create a mapping
   **                            for on this instance.
   ** @param  value              the value for <code>property</code> to set on
   **                            this instance.
   **
   ** @throws BuildException     if the specified property id is already part of
   **                            the parameter mapping.
   */
  public void add(final DomainProperty property, final String value)
    throws BuildException {

    // validate basic requirements
    if (property.required && StringUtility.isEmpty(value))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MANDATORY, property.id));

    // ensure inheritance and apply further validation
    super.addParameter(property.id, value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a token.
   **
   ** @param  token              the token to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Token} with the same name.
   */
  public final void add(final Token token)
    throws BuildException {

    // lazy initialization of the token collection
    if (this.token == null)
      this.token = new LinkedHashMap<TokenType, Token>();

    // verify if a token with the same type is already contained
    if (this.token.keySet().contains(token.type))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, token.type.logical()));

    this.token.put(token.type, token);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a attribute.
   **
   ** @param  attribute          the attribute to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Attribute} with the same name.
   */
  public final void add(final Attribute attribute)
    throws BuildException {

    // lazy initialization of the attribute collection
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
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Resource Server</code>.
   **
   ** @param  resource           the <code>Resource Server</code> to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link ResourceInstance} with the same name.
   */
  public final void add(final ResourceInstance resource)
    throws BuildException {

    // lazy initialization of the resource collection
    if (this.resource == null)
      this.resource = new LinkedHashMap<String, ResourceInstance>();

    // verify if a token with the same type is already contained
    if (this.resource.keySet().contains(resource.name()))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, resource.name()));

    resource.domain(name());
    this.resource.put(resource.name(), resource);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Resource Client</code>.
   **
   ** @param  client             the <code>Resource Client</code> to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link ClientInstance} with the same name.
   */
  public final void add(final ClientInstance client)
    throws BuildException {

    // lazy initialization of the client collection
    if (this.client == null)
      this.client = new LinkedHashMap<String, ClientInstance>();

    // verify if a token with the same type is already contained
    if (this.client.keySet().contains(client.name()))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, client.name()));

    client.domain(name());
    this.client.put(client.name(), client);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the instance parameter to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    // validate strictly for create to avoid side effects
    validate(ServiceOperation.create);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
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

    // only create and modify commands requires specific mandatory parameters
    if (operation == ServiceOperation.none || operation == ServiceOperation.delete || operation == ServiceOperation.print)
      return;

    // validate that at least one parameter is specified for configuration
    // purpose
    final HashMap<String, Object> parameter = this.parameter();
    if (parameter.size() == 0)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.TYPE_PARAMETER_EMPTY));

    // initialize instance with default values if a value is not provided by
    // the frontend for a parameter which has a default
    for (DomainProperty cursor : DomainProperty.values()) {
      if (cursor.required() && cursor.defaultValue() != null && !parameter.containsKey(cursor.id))
        add(cursor, cursor.defaultValue());
    }

    // validate all required parameter
    // the frontend for a parameter which has a default
    for (DomainProperty cursor : DomainProperty.values()) {
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
      builder.append(String.format(ATTRIBUTE_STRING, DomainProperty.ID.id(), id()));
      builder.append(ADJACENT_SEPARATOR);
    }
    builder.append(String.format(ATTRIBUTE_STRING, DomainProperty.NAME.id(), name()));
    final Map<String, Object> parameter = parameter();
    for (Map.Entry<String, Object> entry : parameter.entrySet()) {
      builder.append(ADJACENT_SEPARATOR);
      final DomainProperty property = DomainProperty.from(entry.getKey());
      if (property.type == DomainProperty.Type.STRING)
        builder.append(String.format(ATTRIBUTE_STRING, entry.getKey(), entry.getValue()));
      else
        builder.append(String.format(ATTRIBUTE_RELAX, entry.getKey(), entry.getValue()));
    }
    if (this.attribute != null && this.attribute.size() > 0) {
      final StringBuilder bullshit = new StringBuilder("\"{");
      int i = 0;
      for (Attribute attribute : this.attribute.values()) {
        if (i++ > 0)
          bullshit.append(ADJACENT_SEPARATOR);
        bullshit.append(String.format("\\\"%s\\\":\\\"%s\\\"", attribute.id, attribute.value));
      }
      bullshit.append("}\"");
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, DomainProperty.CUSTOMATTRIBUTES.id(), bullshit.toString()));
    }
    if (this.token != null && this.token.size() > 0) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, DomainProperty.TOKENSETTINGS.id(), ARRAY_OPEN));
      int i = 0;
      for (Token token : this.token.values()) {
        if (i++ > 0)
          builder.append(ADJACENT_SEPARATOR);
        builder.append(token.marshal());
      }
      builder.append(ARRAY_CLOSE);
    }
    builder.append(ENTITY_CLOSE);
    return builder.toString();
  }
}