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

    File        :   ResourceInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.LinkedHashMap;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ResourceInstance
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>ResourceInstance</code> represents a Resource Server in Oracle Access
 ** Manager infrastructure that might be created, deleted or configured after or
 ** during an import operation.
 ** <p>
 ** A <code>Resource Server</code> hosts protected resources. The
 ** <code>Resource Server</code> is capable of accepting and responding to
 ** protected resource requests using access tokens.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceInstance extends DependendInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<String, Scope>          scope;
  private AudienceClaim               audienceClaim;
  private Map<String, TokenAttribute> tokenAttribute;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Scope
  // ~~~~~ ~~~~~
  public static class Scope {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String name;
    private String description;

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
    // Method: description
    /**
     ** Called to inject the argument for parameter <code>description</code>.
     **
     ** @param  description      the value of the <code>Scope</code>.
     */
    public final void description(final String description) {
      this.description = description;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Returns the description of the <code>Scope</code>.
     **
     ** @return                    the description of the <code>Scope</code>.
     */
    public final String description() {
      return this.description;
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
      final StringBuilder builder = new StringBuilder(ENTITY_OPEN);
      builder.append(String.format(ATTRIBUTE_STRING, ResourceProperty.SCOPENAME.id(), this.name));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_STRING, ResourceProperty.DESCRIPTION.id(), this.description));
      builder.append(ENTITY_CLOSE);
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum TokenAttributeType
  // ~~~~ ~~~~~~~~~~~~~~~~~~
  public enum TokenAttributeType {

    /** ??? */
    STATIC("static",   "STATIC"),
    DYNAMIC("dynamic", "DYNAMIC");

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-1389793679930392083")
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
     ** Constructs a <code>TokenAttributeType</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    TokenAttributeType(final String logical, final String physical) {
      this.logical  = logical;
      this.physical = physical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: logical
    /**
     ** Returns the logical name of the <code>TokenAttributeType</code>.
     **
     ** @return                    the logical name of the
     **                            <code>TokenAttributeType</code>.
     */
    public String logical() {
      return this.logical;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: physical
    /**
     ** Returns the physical name of the <code>TokenAttributeType</code>.
     **
     ** @return                    the physical name of the
     **                            <code>TokenAttributeType</code>.
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
     ** Factory method to create a proper <code>TokenAttributeType</code>
     ** property from the given string value by comparing the logical names.
     **
     ** @param  value              the string value the
     **                            <code>TokenAttributeType</code> should be
     **                            returned for.
     **
     ** @return                    the <code>TokenAttributeType</code> property.
     */
    public static TokenAttributeType from(final String value) {
      for (TokenAttributeType cursor : TokenAttributeType.values()) {
        if (cursor.logical.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class AudienceClaim
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Identifies the audiences for which the OAuth token is intended.
   ** <br>
   ** Each principal intended to process the OAuth token must identify itself
   ** with a value in <code>AudienceClaim</code>.
   */
  public static class AudienceClaim {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private List<String> subject;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AudienceClaim</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public AudienceClaim() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Called to inject the argument for parameter <code>subject</code>.
     **
     ** @param  name               the subject of the <code>Audience</code>.
     */
    public final void subject(final String name) {
      // lazy initialization of the subject container
      if (this.subject == null)
        this.subject = new LinkedList<String>();

      // verify if a subject with the same name is already contained
      if (this.subject.contains(name))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, name));

      this.subject.add(name);
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
      final StringBuilder builder = new StringBuilder(String.format(ATTRIBUTE_RELAX, ResourceProperty.SUBJECTS.id(), ARRAY_OPEN));
      for (int i = 0; i < this.subject.size(); i++) {
        if (i > 0)
          builder.append(ADJACENT_SEPARATOR);
        builder.append("\"" + this.subject.get(i) + "\"");
      }
      builder.append(ARRAY_CLOSE);
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class TokenAttribute
  // ~~~~~ ~~~~~~~~~~~~~~
  public static class TokenAttribute {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String             id;
    private String             value;
    private TokenAttributeType type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>TokenAttribute</code> task that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public TokenAttribute() {
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
     ** @param  id               the identifier of the
     **                          <code>TokenAttribute</code>.
     */
    public final void id(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the identifier of the <code>TokenAttribute</code>.
     **
     ** @return                  the identifier of the
     **                          <code>TokenAttribute</code>.
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
     ** @param  type             the type of the <code>Token</code>.
     */
    public final void type(final String type) {
      type(TokenAttributeType.from(type));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Called to inject the argument for parameter <code>type</code>.
     **
     ** @param  type             the type of the <code>TokenAttribute</code>.
     */
    public final void type(final TokenAttributeType type) {
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the type of the <code>TokenAttribute</code>.
     **
     ** @return                    the type of the <code>TokenAttribute</code>.
     */
    public final TokenAttributeType type() {
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
      builder.append(String.format(ATTRIBUTE_STRING, ResourceProperty.TOKENATTRIBUTENAME.id(), this.id));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_STRING, ResourceProperty.TOKENATTRIBUTEVALUE.id(), this.value));
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_STRING, ResourceProperty.TOKENATTRIBUTETYPE.id(), this.type.physical));
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
   ** Constructs a <code>ResourceInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceInstance() {
    // ensure inheritance
    super();
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
   **                            under which the <code>Resource Server</code>
   **                            is created, modified or deleted.
   */
  @Override
  public final void domain(final String domain) {
    // validate the the given value is proper
    if (StringUtility.isEmpty(domain))
      handleAttributeMandatory(ResourceProperty.DOMAIN.id());

    this.parameter().put(ResourceProperty.DOMAIN.id(), domain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domain
  /**
   ** Returns the name of the <code>Identity Domain</code> the
   ** <code>Resource Server</code> belongs to.
   **
   ** @return                    the name of the <code>Identity Domain</code>
   **                            under which the <code>Resource Server</code>
   **                            is created, modified or deleted.
   */
  @Override
  public final String domain() {
    return stringParameter(ResourceProperty.DOMAIN.id());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   ** Call by the ANT deployment to inject the argument for adding a audience
   ** claim.
   **
   ** @param  audienceClaim      the audience claim to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link AudienceClaim} with the same subject.
   */
  public final void add(final AudienceClaim audienceClaim)
    throws BuildException {

    this.audienceClaim = audienceClaim;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Call by the ANT deployment to inject the argument for adding a token
   ** attribute.
   **
   ** @param  attribute          the token attribute to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link TokenAttribute} with the same name.
   */
  public final void add(final TokenAttribute attribute)
    throws BuildException {

    // lazy initialization of the token attribute container
    if (this.tokenAttribute == null)
      this.tokenAttribute = new LinkedHashMap<String, TokenAttribute>();

    // verify if a token with the same type is already contained
    if (this.tokenAttribute.keySet().contains(attribute.id))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, attribute.id));

    this.tokenAttribute.put(attribute.id, attribute);
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

    if (StringUtility.isEmpty(domain()))
      handleAttributeMissing(ResourceProperty.DOMAIN.id());

    // only create and modify commands requires specific mandatory parameters
    if (operation == ServiceOperation.none ||operation == ServiceOperation.delete || operation == ServiceOperation.print)
      return;

    // initialize instance with default values if a value is not provided by
    // the frontend for a parameter which has a default
    final HashMap<String, Object> parameter = this.parameter();
    for (ResourceProperty cursor : ResourceProperty.values()) {
      if (cursor.required() && cursor.defaultValue() != null && !parameter.containsKey(cursor.id))
        add(cursor, cursor.defaultValue());
    }

    // validate all required parameter the frontend expects
    for (ResourceProperty cursor : ResourceProperty.values()) {
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
      builder.append(String.format(ATTRIBUTE_STRING, ResourceProperty.ID.id(), id()));
      builder.append(ADJACENT_SEPARATOR);
    }
    builder.append(String.format(ATTRIBUTE_STRING, ResourceProperty.NAME.id(), name()));
    final Map<String, Object> parameter = parameter();
    for (Map.Entry<String, Object> entry : parameter.entrySet()) {
      builder.append(ADJACENT_SEPARATOR);
      final ResourceProperty property = ResourceProperty.from(entry.getKey());
      if (property.type == ResourceProperty.Type.STRING)
        builder.append(String.format(ATTRIBUTE_STRING, entry.getKey(), entry.getValue()));
      else
        builder.append(String.format(ATTRIBUTE_RELAX, entry.getKey(), entry.getValue()));
    }
    if (this.scope != null && this.scope.size() > 0) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, ResourceProperty.SCOPES.id(), ARRAY_OPEN));
      int i = 0;
      for (Scope scope : this.scope.values()) {
        if (i++ > 0)
          builder.append(ADJACENT_SEPARATOR);
        builder.append(scope.marshal());
      }
      builder.append(ARRAY_CLOSE);
    }
    if (this.audienceClaim != null && this.audienceClaim.subject.size() > 0) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, ResourceProperty.AUDIENCECLAIM.id(), ENTITY_OPEN));
      builder.append(this.audienceClaim.marshal());
      builder.append(ENTITY_CLOSE);
    }
    if (this.tokenAttribute != null && this.tokenAttribute.size() > 0) {
      builder.append(ADJACENT_SEPARATOR);
      builder.append(String.format(ATTRIBUTE_RELAX, ResourceProperty.TOKENATTRIBUTES.id(), ARRAY_OPEN));
      int i = 0;
      for (TokenAttribute attribute : this.tokenAttribute.values()) {
        if (i++ > 0)
          builder.append(ADJACENT_SEPARATOR);
        builder.append(attribute.marshal());
      }
      builder.append(ARRAY_CLOSE);
    }
    builder.append(ENTITY_CLOSE);
    return builder.toString();
  }
}