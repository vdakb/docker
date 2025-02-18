package oracle.iam.oauth.client.type;

import java.net.MalformedURLException;

import java.net.URL;

import org.apache.tools.ant.BuildException;

import oracle.iam.oauth.common.type.FeatureInstance;

import oracle.iam.oauth.common.spi.ClientInstance;
import oracle.iam.oauth.common.spi.ClientProperty;

////////////////////////////////////////////////////////////////////////////////
// class Instance
// ~~~~~ ~~~~~~~~
/**
 ** <code>Instance</code> represents an <code>Resource Client</code>
 ** instance in Oracle Access Manager that might be created, deleted or
 ** configured during an import operation.
 ** <p>
 ** A <code>Resource Client</code> initiates the OAuth protocol by invoking the
 ** OAuth Services.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Instance extends FeatureInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final ClientInstance delegate = new ClientInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Instance</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Instance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for attribute <code>name</code>.
   **
   ** @param  value              the name of the instance in Oracle Access
   **                            Manager to handle.
   */
  public final void setName(final String value) {
    checkAttributesAllowed();
    this.delegate.name(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Called to inject the argument for attribute <code>type</code>.
   **
   ** @param  value              the type of the instance in Oracle Access
   **                            Manager to handle.
   */
  public final void setType(final Type value) {
    checkAttributesAllowed();
    this.delegate.type(value.getValue());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link ClientInstance} delegate of Identity Domain
   ** object to handle.
   **
   ** @return                    the {@link ClientInstance} delegate.
   */
  public final ClientInstance delegate() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredParameter
  /**
   ** Call by the ANT deployment to inject the argument for adding a parameter.
   **
   ** @param  parameter          the parameter to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Parameter} with the same name.
   */
  public void addConfiguredParameter(final Parameter parameter)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending identity store property
    this.delegate.add(ClientProperty.from(parameter.getValue()), parameter.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredScope
  /**
   ** Call by the ANT deployment to inject the argument for adding a scope.
   **
   ** @param  scope              the scope to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Scope} with the same name.
   */
  public void addConfiguredScope(final Scope scope)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending identity domain property
    this.delegate.add(scope.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAttribute
  /**
   ** Call by the ANT deployment to inject the argument for adding an attribute.
   **
   ** @param  attribute          the attribute to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Attribute} with the same name.
   */
  public void addConfiguredAttribute(final Attribute attribute)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending identity domain property
    this.delegate.add(attribute.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredScope
  /**
   ** Call by the ANT deployment to inject the argument for adding a grant.
   **
   ** @param  grant              the grant to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Grant} with the same name.
   */
  public void addConfiguredGrant(final Grant grant)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending identity domain property
    this.delegate.add(ClientInstance.Grant.from(grant.delegate().getValue()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRedirect
  /**
   ** Call by the ANT deployment to inject the argument for adding a redirect.
   **
   ** @param  redirect           the redirect to add.
   **
   ** @throws BuildException     if this instance is already referencing an
   **                            {@link Grant} with the same name.
   */
  public void addConfiguredRedirect(final Redirect redirect)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // assign the correspondending identity domain property
    try {
      this.delegate.add(new URL(redirect.url()));
    }
    catch (MalformedURLException e) {
      throw new BuildException(e);
    }
  }
}