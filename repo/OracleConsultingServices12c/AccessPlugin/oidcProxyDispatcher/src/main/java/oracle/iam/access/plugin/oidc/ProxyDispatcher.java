/*
    ORACLE Deutschland B.V. & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Plugin
    Subsystem   :   OpenIdConnect Proxy

    File        :   ProxyDispatcher.java

    Compiler    :   Java Development Kit 8

    Author      :   nitin.popli@oracle.com

    Purpose     :   This file implements the class
                    ProxyDispatcher.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-11  npopli      First release version
*/

package oracle.iam.access.plugin.oidc;

import java.util.Map;
import java.util.Collections;

import oracle.security.am.plugin.PluginConfig;
import oracle.security.am.plugin.MonitoringData;
import oracle.security.am.plugin.ExecutionStatus;

import oracle.security.am.plugin.authn.PlugInUtil;
import oracle.security.am.plugin.authn.AuthenticationContext;
import oracle.security.am.plugin.authn.AuthenticationException;
import oracle.security.am.plugin.authn.AbstractAuthenticationPlugIn;

////////////////////////////////////////////////////////////////////////////////
// class ProxyDispatcher
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Plug-In that obtains the IdP name from the authentication context and
 ** matches it with the IDP name in the plugin parameter.
 ** <p>
 ** All the plug-in implementations have to extend
 ** {@link AbstractAuthenticationPlugIn} class.
 ** <br>
 ** Plug-ins that needs to handle the resource cleanup should override
 ** shutdown(Map &lt;String, Object&gt; OAMEnvironmentContext).
 ** An instance of java.util.Logger will be available to plug-ins.
 **
 ** @author  nitin.popli@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.1
 */
public class ProxyDispatcher extends AbstractAuthenticationPlugIn {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String THIS      = ProxyDispatcher.class.getName();

  /** The name of the request header */
  private static final String HEADER    = "x-oidc-idp-name";

  /** The name of the url query parameter */
  private static final String PARSMETER = "idp-name";

  /** The name of the authentication context value to inject */
  private static final String CONTEXT   = "IDP_NAME";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String idpName = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an Access Manager <code>ProxyDispatcher</code>  authentication
   ** plugin that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProxyDispatcher() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRevision (GenericPluginService)
  /**
   ** Returns the plug-in revision.
   ** Items required for plug-in management.
   ** Revision should be an integer value.
   ** Plugin developers can keep track of multiple revisions of the plugin.
   ** The plugin development framework will take care of maintaining version.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This method will be removed in future releases.
   ** 
   ** @return                    the plug-in revision.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int getRevision() {
    return 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlugInName (OAMAbstractAMPlugin)
  /**
   ** Returns the name that will be an identifier for the plug-in.
   ** <br>
   ** User can use the name and version of the plug-in in plug-in UI to identify
   ** the plug-in user want to use.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This method will be removed in future releases.
   ** 
   ** @return                    the name that will be an identifier for the
   **                            plug-in.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getPluginName() {
    return getClass().getSimpleName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription (OAMAbstractAMPlugin)
  /**
   ** Returns the description of the plug-in.
   ** <br>
   ** This value will be displayed in the UI when the user selects an plug-in to
   ** assign to an plugin point.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This method will be removed in future releases.
   ** 
   ** @return                    the description of the plug-in.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getDescription() {
    return "Plug-In to discover an OpenIdConnect IdP";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMonitoringStatus (OAMAbstractAMPlugin)
  /**
   ** Sets the monitoring status configured for the plugin.
   ** 
   ** @param  value              the monitoring status of the plug-in.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @Override
  public void setMonitoringStatus(final boolean value) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMonitoringStatus (OAMAbstractAMPlugin)
  /**
   ** Returns the monitoring status of the plug-in.
   ** 
   ** @return                    the monitoring status of the plug-in.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean getMonitoringStatus() {
    // indicates if logging DMS data is enabled for the plugins
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMonitoringData (OAMAbstractAMPlugin)
  /**
   ** A plug-in can return monitoring data after plug-in execution is complete.
   ** <br>
   ** The Server will log this data.
   ** 
   ** 
   ** @return                    the monitoring data.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link MonitoringData} as the value.
   */
  @Override
  public Map<String, MonitoringData> getMonitoringData() {
    // Plug-In's can log DMS data which will be picked by the Auth framework
    // and logged
    return Collections.emptyMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process (OAMAbstractAMPlugin)
  /**
   ** This method is used to obtain the IdP name from the authentication context
   ** and match it with the IdP name in the plugin parameter.
   ** <p>
   ** Authentication plug-ins can access all the data available in the
   ** {@link AuthenticationContext} object.
   ** <br>
   ** Authentication plug-ins can set response that will be added to SESSION,
   ** request and redirect contexts.
   ** <p>
   ** PluginAttributeContextType.SESSION all the attributes that need to be set
   ** in the session should be added with this context.
   ** <p>
   ** PluginAttributeContextType.REQUEST all the attributes that need to be set
   ** in the request context should be added with this context.
   ** <p>
   ** PluginAttributeContextType.REQUEST all the attributes that need to be set
   ** as a re-direct URL should be added with this context.
   **
   ** @param  context            an {@link AuthenticationContext} object.
   **                            <br>
   **                            Allowed object is
   **                            {@link AuthenticationContext}.
   **
   ** @return                    the execution status-
   **                            Possible object is {@link ExecutionStatus}.
   **
   ** @throws AuthenticationException if the authentication flow fails.
   */
  @Override
  public ExecutionStatus process(final AuthenticationContext context)
    throws AuthenticationException {

    final String method="process";
    LOGGER.entering(THIS, method);

    final String idpNameContext  = context.getStringAttribute("IDP_NAME");
    if (idpNameContext == null || (idpNameContext.length() == 0)) {
      LOGGER.severe(method + ":: IDP Name not set in the authentication context");
      LOGGER.exiting(THIS, method);
      return ExecutionStatus.FAILURE;
    }

    final String idpNameInstance = PlugInUtil.getFlowParam(context.getStringAttribute("StepName"), "IDP_Name", context);
    if (idpNameInstance == null || (idpNameInstance.length() == 0)) {
      LOGGER.severe(method + ":: IDP Name not set in the authentication plugin");
      LOGGER.exiting(THIS, method);
      return ExecutionStatus.FAILURE;
    }
    if (idpNameContext.equalsIgnoreCase(idpNameInstance)) {
      LOGGER.info(method + ":: IDP Name in the authentication plugin matches with the IDP name set in the authentication context");
      LOGGER.exiting(THIS, method);
      return ExecutionStatus.SUCCESS;
    }
    else {
      LOGGER.severe(method + ":: IDP Name in the authentication plugin does not match with the IDP name set in the authentication context");
      LOGGER.exiting(THIS, method);
      return ExecutionStatus.FAILURE;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The function initializes the Plug-in.
   ** <br>
   ** The function is called only once during the entire plug-in lifecycle.
   ** <p>
   ** <b>Assumption</b>:
   ** <br>
   ** The plug-in will itself pick the configuration. The plug-in and its
   ** configuration will first be across the cluster. The SDK will have the
   ** API's to read the oam-config.xml.
   **
   */
  @Override
  public ExecutionStatus initialize(final PluginConfig config){
    // ensure inheritance
    super.initialize(config);

    final String method = "initialize";
    LOGGER.entering(THIS, method);
    idpName = (String)config.getParameter("IDP_Name");
    LOGGER.info(method + ":: IDP_Name:" + idpName);
    LOGGER.exiting(THIS, method);
    return ExecutionStatus.SUCCESS;
  }
}