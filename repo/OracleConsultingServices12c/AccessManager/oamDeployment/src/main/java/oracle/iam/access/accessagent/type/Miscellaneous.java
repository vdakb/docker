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

    File        :   Miscellaneous.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Miscellaneous.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessagent.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.type.DelegatingDataType;

import oracle.iam.access.common.spi.schema.MiscellaneousProperty;
import oracle.iam.access.common.spi.schema.MiscellaneousProperties;

import org.apache.tools.ant.BuildException;

////////////////////////////////////////////////////////////////////////////////
// class Miscellaneous
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Most agent properties are hot-swap enabled.
 ** <br>
 ** Changing configuration properties can have unexpected results. Hot-swappable
 ** properties take effect immediately. Therefore, mistakes are instantly
 ** implemented. Most agent properties are presented in a format that is most
 ** useful for configuring using Oracle Access Management Console. However,
 ** this format is not used in the OpenSSOAgentBootstrap.properties file.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** To enable OpenSSO <code>Access Agent</code> configuration hotswap, make sure
 ** the OpenSSO <code>Access Agent</code>s have the following properties in the
 ** miscellaneous properties section of their profile in the
 ** <code>OpenSSO Proxy</code> on <code>Access Server</code>, and the agent
 ** servers are restarted:
 ** <ul>
 **   <li><b>J2ee Agents</b>: <code>com.sun.identity.client.notification.url=http://&lt;AGENT_SERVER_HOST&gt;:&lt;AGENT_SERVER_PORT&gt;/agentapp/notification</code>
 **   <li><b>Web Agents</b>: <code>com.sun.identity.client.notification.url=http://AGENT_SERVER_HOST:AGENT_SERVER_PORT/UpdateAgentCacheServlet?shortcircuit=false</code>
 ** </ul>
 ** <p>
 ** <b>List Properties</b>:
 ** <br>
 ** Certain properties are specified as lists composed of a key that represents
 ** the property name; a positive number (starting from <code>0</code>) that
 ** increments by <code>1</code> for every value specified in the list; and a
 ** value. For example:
 ** <pre>
 **   com.sun.identity.agents.config.notenforced.uri[0]=/agentsample/public/*
 **   com.sun.identity.agents.config.notenforced.uri[1]=/agentsample/images/*
 **   com.sun.identity.agents.config.notenforced.uri[2]=/agentsample/index.html
 ** </pre>
 ** <b>Map Constructs</b>:
 ** <br>
 ** Certain properties are specified as map constructs composed of a key that
 ** represents the property name; a name string that forms the lookup key as
 ** available in the map; and the value associated with the name in the map.
 ** For example:
 ** <pre>
 **   com.sun.identity.agents.config.filter.mode[app1]=ALL
 **   com.sun.identity.agents.config.filter.mode[app2]=SSO_ONLY **
 ** </pre>
 ** <b>Note</b>:
 ** <br>
 ** For a given name, there can only be one entry in the configuration for a
 ** given configuration key. If multiple entries with the same &lt;name&gt; for
 ** a given configuration key are present only one of the values will be loaded
 ** in the system and the other values are discarded.
 ** <p>
 ** <b>Application-Specific Properties</b>:
 ** <br>
 ** Certain properties can be configured for specific applications. The agent
 ** can use different values of the same property for different applications as
 ** defined in the configuration file. Application Specific configuration
 ** properties must follow the rules and syntax of the map construct. The
 ** following settings for a single property serve as an example which
 ** illustrates that for applications other than the ones deployed on the root
 ** context and the context /Portal, the value of the property defaults to
 ** <code>value3</code>.
 ** <pre>
 **   com.sun.identity.agents.config.example[Portal] = value1
 **   com.sun.identity.agents.config.example[DefaultWebApp] = value2
 **   com.sun.identity.agents.config.example = value3
 ** </pre>
 ** <b>Global Properties</b>:
 ** <br>
 ** Properties that are not configured for specific applications apply to all
 ** the applications on that deployment container. Such properties are called
 ** global properties.
 **
 */
public class Miscellaneous extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final MiscellaneousProperties delegate = factory.createMiscellaneousProperties();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Property
  // ~~~~~ ~~~~~~~~
  public static class Property extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final MiscellaneousProperty delegate = factory.createMiscellaneousProperty();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Property</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Property() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setName
    /**
     ** Sets the value of the <code>name</code> property.
     **
     ** @param  value              the value of the <code>name</code> property.
     **                            Allowed object is {@link String}.
     */
    public void setName(final String value) {
      this.delegate.setName(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setValue
    /**
     ** Sets the value of the <code>value</code> property.
     **
     ** @param  value              the value of the <code>value</code> property.
     **                            Allowed object is {@link String}.
     */
    public void setValue(final String value) {
      this.delegate.setValue(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Miscellaneous</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Miscellaneous() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredMiscellaneousProperty
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** <code>Property</code>.
   **
   ** @param  value              the {@link Property} instance to add.
   **                            Allowed object is {@link Property}.
   **
   ** @throws BuildException     if the rule instance is already added.
   */
  public void addConfiguredMiscellaneousProperty(final Property value)
    throws BuildException {

    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "property"));

    // prevent bogus input
    if (this.delegate.getMiscellaneousProperty().contains(value.delegate))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "property"));

    this.delegate.getMiscellaneousProperty().add(value.delegate);
  }
}