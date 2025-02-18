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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   WebServiceManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    WebServiceManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.Collection;

import java.io.StringWriter;
import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBElement;
import javax.management.Attribute;
import javax.management.ObjectName;
import javax.management.ObjectInstance;
import javax.management.MBeanServerConnection;

import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.CompositeDataSupport;

import oracle.wsm.search.SearchConstants;
import oracle.wsm.search.SearchCriterion;

import oracle.wsm.policy.parser.IPolicyReader;
import oracle.wsm.policy.parser.IPolicyWriter;

import oracle.wsm.policy.util.PolicySerializerDeSerializerFactory;

import oracle.wsm.policy.model.IPolicy;
import oracle.wsm.policy.model.IPolicySubject;
import oracle.wsm.policy.model.IAssertionTemplate;

import oracle.wsm.policy.model.impl.PolicySubject;

import oracle.wsm.policy.schema.sca11.PolicySet;
import oracle.wsm.policy.schema.wsp15.PolicyReference;
import oracle.wsm.policy.schema.orawsp.OverridePropertyType;

import oracle.wsm.policy.constants.PolicyConstants;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class WebServiceManager
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage the webservice security context in
 ** Oracle WebLogic Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WebServiceManager {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String           TYPE                                = "Repository";
  private static final String           DOCUMENTMANAGER                     = "WSMDocumentManager";

  private static final String           TYPE_COMPOSITE_SOA                  = "SCAComposite";

  private static final String           ATTRIBUTE_SUPPORT_MTOM              = "MTOMSupported";

  private static final String           TYPE_WSEE_RUNTIME                   = "WseeRuntime";
  private static final String           TYPE_WSEE_RUNTIME_CLIENT            = "WseeClientRuntime";
  private static final String           TYPE_WSEE_PORT_RUNTIME              = "WseePortRuntime";

  private static final String           TYPE_PORT_CONFIGURATION_CLIENT      = "WseePortConfigurationRuntime";

  private static final String           TYPE_POLICY_SUBJECT_BINDING_SERVER  = "binding.server.soap.http";
  private static final String           TYPE_POLICY_SUBJECT_BINDING_CLIENT  = "binding.client.soap.http";
  private static final String           TYPE_POLICY_SUBJECT_RESOURCEPATTERN = "PolicySubjectResourcePattern";

  private static final IPolicyReader    reader                              = PolicySerializerDeSerializerFactory.getPolicyReader(1);
  private static final IPolicyWriter    writer                              = PolicySerializerDeSerializerFactory.getPolicyWriter(2);

  private static final String[]         WLS_ITEM_NAMES                      = {"category", "policyReferenceURI", "enabled", "configOverrides", "direction"};

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static ObjectName                     documentManager                     = null;
  static CompositeType                  typeComposite                       = null;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    OpenType[] types = { SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, AbstractInvocationHandler.typeStringMap, SimpleType.STRING };
    try {
      typeComposite = new CompositeType("weblogic.management.runtime.WseePolicyReferenceInfo", "weblogic.management.runtime.WseePolicyReferenceInfo", WLS_ITEM_NAMES, WLS_ITEM_NAMES, types);
    }
    catch (OpenDataException ignored) {
      // intentionally left blank
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final ServiceFrontend frontend;
  protected final MBeanServerConnection connection;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Reference
  // ~~~~~ ~~~~~~~~~
  /**
   ** Helper class to wrap common WebService Manager Policies
   */
  public static class Reference {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    String              uri      = null;
    String              status   = null;
    Map<String, Object> override = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Reference</code> policy context for the specified
     ** <code>uri</code>, <code>status</code> and <code>override</code>s.
     ** <p>
     ** <b>Note</b>:
     ** This constructor is mainly used for testing prupose only.
     **
     ** @param  uri              the name of a policy referenced by a
     **                          WebService.
     ** @param  status           the status of the WebServive Manager Policy
     **                          for a WebService.
     ** @param  override         the override properties of the WebServive
     **                          Manager Policy for an atached WebService.
     */
    private Reference(final String uri, final String status, final Map<String, Object> override) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.uri      = uri;
      this.status   = status;
      this.override = override;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a <code>Reference</code>.
     **
     ** @param  uri              the name of a policy referenced by a
     **                          WebService.
     ** @param  status           the status of the WebServive Manager Policy
     **                          for a WebService.
     ** @param  override         the override properties of the WebServive
     **                          Manager Policy for an atached WebService.
     **
     ** @return                  an instance of <code>Reference</code> with the
     **                          properties provided.
     */
    public static Reference build(final String uri, final String status, final Map<String, Object> override) {
      return new Reference(uri, status, override);
    }

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
     **       the two objects must produce distinct integer results.  However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     */
    @Override
    public int hashCode() {
      long hc = getClass().hashCode();
      hc += this.uri.hashCode();
      return (int)hc;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one.
     ** <p>
     ** The <code>equals</code> method implements an equivalence relation on
     ** non-<code>null</code> object references:
     ** <ul>
     **   <li>It is <i>reflexive</i>: for any non-<code>null</code> reference
     **       value <code>x</code>, <code>x.equals(x)</code> should return
     **       <code>true</code>.
     **   <li>It is <i>symmetric</i>: for any non-<code>null</code> reference
     **       values <code>x</code> and <code>y</code>, <code>x.equals(y)</code>
     **       should return <code>true</code> if and only if
     **       <code>y.equals(x)</code> returns <code>true</code>.
     **   <li>It is <i>transitive</i>: for any non-<code>null</code> reference
     **       values <code>x</code>, <code>y</code>, and <code>z</code>, if
     **       <code>x.equals(y)</code> returns <code>true</code> and
     **       <code>y.equals(z)</code> returns <code>true</code>, then
     **       <code>x.equals(z)</code> should return <code>true</code>.
     **   <li>It is <i>consistent</i>: for any non-<code>null</code> reference
     **       values <code>x</code> and <code>y</code>, multiple invocations of
     **       <code>x.equals(y)</code> consistently return <code>true</code> or
     **       consistently return <code>false</code>, provided no information
     **       used in <code>equals</code> comparisons on the objects is
     **       modified.
     **   <li>For any non-<code>null</code> reference value <code>x</code>,
     **       <code>x.equals(null)</code> should return <code>false</code>.
     ** </ul>
     ** <p>
     ** Note that it is generally necessary to override the
     ** <code>hashCode</code> method whenever this method is overridden, so as
     ** to maintain the general contract for the <code>hashCode</code> method,
     ** which states that equal objects must have equal hash codes.
     **
     ** @param  other           the reference object with which to compare.
     **
     ** @return                 <code>true</code> if this object is the same as
     **                         the other argument; <code>false</code>
     **                         otherwise.
     **
     ** @see    #hashCode()
     */
    @Override
    public boolean equals(final Object other) {
      if (other == null || (!(other instanceof Reference)))
        return false;

      final Reference that = (Reference)other;
      return (this.uri.equals(that.uri));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>WebServiceManager</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  connection         the {@link MBeanServerConnection} providing
   **                            access to the remote service facades.
   */
  public WebServiceManager(final ServiceFrontend frontend, final MBeanServerConnection connection) {
    // ensure inheritance
    super();

    // initialize instance
    this.frontend   = frontend;
    this.connection = connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentManager
  /**
   ** Returns the name of the domain runtime service to connect to.
   **
   ** @return                    the name of the domain runtime service to
   **                            connect to.
   **
   ** @throws ServiceException   the string constructed based on the implemented
   **                            properties does not have the right format.
   */
  protected final ObjectName documentManager()
    throws ServiceException {

    if (documentManager == null) {
      final Map<String, String> properties = new HashMap<>();
      properties.put(AbstractInvocationHandler.JNDI.Type.JRF, TYPE);
      properties.put(AbstractInvocationHandler.JNDI.Name.JRF, DOCUMENTMANAGER);
      final ObjectInstance[] instance = queryBeans(AbstractInvocationHandler.createObjectName("oracle.wsm", properties));
      if (instance == null && instance.length == 0)
        throw new ServiceException(ServiceError.WEBSERVICE_POLICY_MANAGER);

      documentManager = instance[0].getObjectName();
    }
    return documentManager;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attachable
  /**
   ** Returns <code>true</code> the specified subjac can be attached to Oracle
   ** WebServive Manager.
   **
   ** @param  policySubject      the  name of the policy subject.
   **
   ** @return                    <code>true</code> if the specified subject is
   **                            attachable to Oracle WebServive Manager;
   **                            otherwise <code>flase</code>.
   **
   ** @throws ServiceException   the string constructed based on the implemented
   **                            properties does not have the right format.
   */
  protected boolean attachable(final String policySubject)
    throws ServiceException {

    return ((Boolean)invoke(policySubjectManager(), "isOWSMAttachable", new Object[] { policySubject }, new String[] { "java.lang.String" })).booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policySubjectManager
  /**
   ** Returns the name of the WebLogic Policy Subject Manager runtime.
   **
   ** @return                    the {@link ObjectName} of the policy
   **                            application.
   **
   ** @throws ServiceException   the string constructed based on the implemented
   **                            properties does not have the right format.
   */
  protected final ObjectName policySubjectManager()
    throws ServiceException {

    final Map<String, String> properties = new HashMap<>();
    properties.put(AbstractInvocationHandler.JNDI.Type.WLS, "WseePolicySubjectManagerRuntime");
    final ObjectName[] restResourceRuntimes = queryNames(AbstractInvocationHandler.JNDI.Root.WLS, properties);
    if (restResourceRuntimes.length != 1)
      throw new ServiceException(ServiceError.WEBSERVICE_APPLICATION_NOTFOUND, "WseePolicySubjectManagerRuntime");

    return restResourceRuntimes[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policyApplicationRuntime
  /**
   ** Returns the name of the WebService Manager application runtime.
   **
   ** @param  applicationRuntime the name of the application runtime to lookup.
   **
   ** @return                    the {@link ObjectName} of the policy
   **                            application.
   **
   ** @throws ServiceException   the string constructed based on the implemented
   **                            properties does not have the right format.
   */
  protected final ObjectName policyApplicationRuntime(final String applicationRuntime)
    throws ServiceException {

    final Map<String, String> properties = new HashMap<>();
    properties.put(AbstractInvocationHandler.JNDI.Type.JRF, "PolicyApplication");
    properties.put(AbstractInvocationHandler.JNDI.Name.JRF, applicationRuntime);
    final ObjectName[] runtimes = queryNames("oracle.wsm.metadata", properties);
    if (runtimes.length != 1)
      throw new ServiceException(ServiceError.WEBSERVICE_APPLICATION_NOTFOUND, applicationRuntime);

    return runtimes[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verbose
  /**
   ** Returns the flag indicating if verbosity is requested or not.
   **
   ** @return                    <code>true</code> if verbosity is requested;
   **                            otherwise <code>false</code>.
   */
  public final boolean verbose() {
    return this.frontend.verbose();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failonerror
  /**
   ** Returns how the task behavior will be in case an error is detected.
   **
   ** @return                    how the task behavior will be in case an error
   **                            is detected.
   */
  public final boolean failonerror() {
    return this.frontend.failonerror();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  what               the exception as the reason to log.
   */
  protected final void fatal(final Throwable what) {
    this.frontend.fatal(what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  protected final void error(final String pattern, final String argument) {
    this.frontend.error(pattern, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Writes an normal error message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void error(final String pattern, final Object[] arguments) {
    this.frontend.error(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an normal error message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  protected final void error(final String message) {
    this.frontend.error(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
  ** Writes a warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  protected final void warning(final String pattern, final String argument) {
    this.frontend.warning(pattern, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Writes an warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void warning(final String pattern, final Object[] arguments) {
    this.frontend.warning(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            what is the reason to log.
   */
  protected final void warning(final String message) {
    this.frontend.warning(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
  ** Writes a informational message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  protected final void info(final String pattern, final String argument) {
    this.frontend.info(pattern, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments.
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void info(final String pattern, final Object[] arguments) {
    this.frontend.info(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  protected final void info(final String message) {
    this.frontend.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  protected final void debug(final String pattern, final String what) {
    this.frontend.debug(pattern, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void debug(final String pattern, final Object[] arguments) {
    this.frontend.debug(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   debug
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  protected final void debug(final String message) {
    this.frontend.debug(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  protected final void trace(final String pattern, final String what) {
    this.frontend.trace(pattern, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void trace(final String pattern, final Object[] arguments) {
    this.frontend.trace(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   trace
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  protected final void trace(final String message) {
    this.frontend.trace(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reportPolicy
  /**
   ** Reports the specified policies an the WebService port specified by
   ** {@link WebServicePolicyHandler.Service} <code>service</code>.
   **
   ** @param  policySubject      the web service port to configure.
   ** @param  application        the name and path of the application for which
   **                            the WebService policies have to been fetche.
   **                            For example,
   **                            /domain/server/application#version_number.
   ** @param  collection         the {@link Collection} of policies to attach.
   **
   ** @throws ServiceException   in case a validation error does occur.
   */
  public void reportPolicy(final ObjectName policySubject, final String application, final Collection<WebServicePolicyHandler.Policy> collection)
    throws ServiceException {

    final Locale        locale    = Locale.getDefault();
    final String        format    = ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_FORMAT);
    final StringBuilder builder   = new StringBuilder();
    final Formatter     formatter = new Formatter(builder);
    final String        typeName  = policySubject.getKeyProperty(AbstractInvocationHandler.JNDI.Type.WLS);
    if ((TYPE_WSEE_PORT_RUNTIME.equals(typeName) || TYPE_PORT_CONFIGURATION_CLIENT.equals(typeName))) {
      final String[][] exists = null;//getPolicyReferences(policySubject);
      if (exists != null && exists.length > 0) {
        for (int i = 0; i < exists.length; i++) {
          formatter.format(locale, format, exists[i][1], exists[i][2], exists[i][0]);
        }
      }
      else
        builder.append(ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_EMPTY));
    }
    else {
      final String configName      = policySubject.getKeyProperty(AbstractInvocationHandler.JNDI.WebService.JRF);
      final String resourcePattern = resourcePattern(policySubject);
      // fetch all assinged policies to validate if one of the defined
      // policies are already assigned
      final PolicySet container = attachedPolicySet(application, resourcePattern);
      info(ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_HEADER));
      info(String.format("%s %s", ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_PREFIX), configName));
      info(ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_SEPARATOR));
      info(String.format(format, ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_CATEGORY), ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_STATUS), ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_ENTITY)));
      info(ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_SEPARATOR));
      if (container.getPolicySetReferenceOrIntentMapOrPolicyReference().size() > 0) {
        final List<Object> containee = container.getPolicySetReferenceOrIntentMapOrPolicyReference();
        for (Iterator<Object> i = containee.iterator(); i.hasNext();) {
          final Object subject = i.next();
          if (subject instanceof PolicyReference) {
            final PolicyReference local = (PolicyReference)subject;
            info(String.format(format, local.getCategory(), local.getStatus(), local.getURI()));
          }
        }
      }
      else
        builder.append(ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_EMPTY));
      info(ServiceResourceBundle.string(ServiceMessage.WEBSERVICE_POLICY_REPORT_SEPARATOR));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attachPolicy
  /**
   ** Attach the specified policies to the WebService port specified by
   ** {@link WebServicePolicyHandler.Policy} <code>collection</code>.
   ** <p>
   ** The policyURI's are validated through the Oracle WSM Policy Manager APIs
   ** if the wsm-pm application is installed on WebLogic Server and is
   ** available. If the PolicyURI that is specified already is attached or
   ** exists, then this command enables the policy if it is disabled.
   ** <p>
   ** If the wsm-pm application is not installed or is not available, this
   ** command is not executed.
   **
   ** @param  policySubject      the web service port to configure.
   ** @param  application        the name and path of the application for which
   **                            the WebService policies have to been fetche.
   **                            For example,
   **                            /domain/server/application#version_number.
   ** @param  collection         the {@link Collection} of policies to attach.
   ** @param  server             <code>true</code> if the service belongs to a
   **                            server; <code>false</code> if the service
   **                            belongs to a client.
   **
   ** @return                    <code>true</code> if the state of the policy
   **                            could be attached; otherwise
   **                            <code>false</code>.
   **
   ** @throws ServiceException   in case a validation error does occur.
   */
  public boolean attachPolicy(final ObjectName policySubject, final String application, final Collection<WebServicePolicyHandler.Policy> collection, final boolean server)
    throws ServiceException {

    boolean      result          = false;
    final String typeName        = policySubject.getKeyProperty(AbstractInvocationHandler.JNDI.Type.WLS);
    final String resourcePattern = resourcePattern(policySubject);
    if ((TYPE_WSEE_RUNTIME.equals(typeName) || TYPE_WSEE_RUNTIME_CLIENT.equals(typeName)) && (!attachable(resourcePattern))) {
        error(ServiceResourceBundle.string(ServiceError.WEBSERVICE_POLICY_INCOMPATIBLE));
      }
      else {
      // fetch all assinged policies to validate if one of the defined
      // policies are assigned
      final List<String>    attached = attachedPolicy(application, resourcePattern);
      final List<Reference> request  = fetchPolicy(collection);
      final IPolicySubject  subject  = new PolicySubject(PolicyConstants.QNames.RESOURCE_PATTERN_QName, resourcePattern);
      subject.setSubjectType(server ? TYPE_POLICY_SUBJECT_BINDING_SERVER : TYPE_POLICY_SUBJECT_BINDING_CLIENT);

      final String configName = policySubject.getKeyProperty(AbstractInvocationHandler.JNDI.WebService.JRF);
      for (Reference cursor : request) {
        // if the policy allready attached change the status of the reference
        // to enabled only
        if (attached.contains(cursor.uri)) {
          warning(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_POLICY_ALREADY_ATTACHED, configName, cursor.uri));
        }
        else {
          String category = "owsm-" + PolicyConstants.CATEGORIES_ENUM.SECURITY.value();
          if ((TYPE_WSEE_PORT_RUNTIME.equals(typeName)) || (TYPE_PORT_CONFIGURATION_CLIENT.equals(typeName))) {
            CompositeData data = null;
            try {
              final TabularData overrides = AbstractInvocationHandler.mapToTabularData(cursor.override);
              final Object[]   itemValues = { category, cursor.uri, PolicyConstants.AttributeValues.STATUS_ENABLED, overrides, "both" };
              data = new CompositeDataSupport(typeComposite, WLS_ITEM_NAMES, itemValues);
            }
            catch (OpenDataException e) {
              throw new ServiceException(ServiceError.ABORT, e);
            }
            invoke(policySubjectManager(), "attachPolicyReference", new Object[] { resourcePattern, data }, new String[] { String.class.getName(), CompositeData.class.getName() });
          }
          else {
            final PolicyReference reference = new PolicyReference();
            reference.setURI(cursor.uri);
            reference.setCategory(category);
            reference.setStatus(PolicyConstants.AttributeValues.STATUS_ENABLED);
            reference.getOverrideProperty().addAll(overrideProperty(cursor.override));

            final PolicySet container = attachedPolicySet(application, resourcePattern);
            container.getPolicySetReferenceOrIntentMapOrPolicyReference().add(reference);
            commit(application, resourcePattern, container);
          }
          warning(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_POLICY_ATTACHED, configName, cursor.uri));
        }
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detachPolicy
  /**
   ** Detach the specified policies from the WebService port specified by
   ** {@link WebServicePolicyHandler.Policy} <code>collection</code>.
   **
   ** @param  policySubject      the web service port to configure.
   ** @param  application        the name and path of the application for which
   **                            the WebService policies have to been fetche.
   **                            For example,
   **                            /domain/server/application#version_number.
   ** @param  collection         the {@link Collection} of policies to detach.
   **
   ** @return                    <code>true</code> if the state of the policy
   **                            could be detached; otherwise
   **                            <code>false</code>.
   **
   ** @throws ServiceException   in case a validation error does occur.
   */
  public boolean detachPolicy(final ObjectName policySubject, final String application, final Collection<WebServicePolicyHandler.Policy> collection)
    throws ServiceException {

    boolean      result = false;
    final String configName      = policySubject.getKeyProperty(AbstractInvocationHandler.JNDI.WebService.JRF);
    final String resourcePattern = resourcePattern(policySubject);
    // fetch all assinged policies to validate if one of the defined
    // policies are assigned
    final List<String> attached = attachedPolicy(application, resourcePattern);
    if (CollectionUtility.empty(attached)) {
      if (this.frontend.failonerror())
        throw new ServiceException(ServiceError.WEBSERVICE_POLICY_EMPTY, configName);
      else {
        error(ServiceResourceBundle.format(ServiceError.WEBSERVICE_POLICY_EMPTY, configName));
        return result;
      }
    }

    for (WebServicePolicyHandler.Policy cursor : collection) {
      if (none(cursor.name()))
        throw new ServiceException(ServiceError.WEBSERVICE_POLICY_INVALID, cursor.name());

      if (!attached.contains(cursor.name())) {
        final String[] arguments = {configName, cursor.name()};
        error(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_POLICY_ALREADY_DETACHED, arguments));
        if (this.frontend.failonerror())
          throw new ServiceException(ServiceMessage.WEBSERVICE_POLICY_ALREADY_DETACHED, arguments);
      }

      final String typeName = policySubject.getKeyProperty(AbstractInvocationHandler.JNDI.Type.WLS);
      if ((TYPE_WSEE_PORT_RUNTIME.equals(typeName)) || (TYPE_PORT_CONFIGURATION_CLIENT.equals(typeName))) {
        invoke(policySubjectManager(), "removePolicyReference", new Object[] { resourcePattern, cursor.name() }, new String[] { String.class.getName(), String.class.getName() });
        warning(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_POLICY_DETACHED, configName, cursor.name()));
        result = true;
      }
      else {
        boolean changed = false;
        final PolicySet    container = attachedPolicySet(application, resourcePattern);
        final List<Object> containee = container.getPolicySetReferenceOrIntentMapOrPolicyReference();
        for (Iterator<Object> i = containee.iterator(); i.hasNext();) {
          final Object subject = i.next();
          if (subject instanceof PolicyReference) {
            final PolicyReference local = (PolicyReference)subject;
            if (!cursor.name().equals(local.getURI()))
              continue;

            i.remove();
            changed = true;
          }
        }
        if (changed) {
          commit(application, resourcePattern, container);
          warning(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_POLICY_DETACHED, configName, cursor.name()));
        }
      }

      if (("oracle/wsmtom_policy".equals(cursor.name())) && (policySubject.toString().indexOf(TYPE_COMPOSITE_SOA) < 0)) {
        try {
          if (((Boolean)attribute(policySubject, ATTRIBUTE_SUPPORT_MTOM)).booleanValue())
            this.connection.setAttribute(policySubject, new Attribute(ATTRIBUTE_SUPPORT_MTOM, Boolean.valueOf(false)));
        }
        catch (Exception e) {
          result = false;
        }
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   togglePolicy
  /**
   ** Enables the specified policies on the WebService port specified by
   ** {@link WebServicePolicyHandler.Policy} <code>collection</code>.
   **
   ** @param  policySubject      the web service port to configure.
   ** @param  application        the name and path of the application for which
   **                            the WebService policies have to been fetche.
   **                            For example,
   **                            /domain/server/application#version_number.
   ** @param  collection         the {@link Collection} of policies to enable or
   **                            disable.
   ** @param  enable             <code>true</code> if the policy references has
   **                            to be enabled; <code>false</code> if the policy
   **                            references has to be disabled.
   **
   ** @return                    <code>true</code> if the state of the policy
   **                            could be changed; otherwise
   **                            <code>false</code>.
   **
   ** @throws ServiceException   in case a validation error does occur.
   */
  public boolean togglePolicy(final ObjectName policySubject, final String application, final Collection<WebServicePolicyHandler.Policy> collection, boolean enable)
    throws ServiceException {

    boolean      result          = false;
    final String configName      = policySubject.getKeyProperty(AbstractInvocationHandler.JNDI.WebService.JRF);
    final String resourcePattern = resourcePattern(policySubject);
    // fetch all assinged policies to validate if one of the defined
    // policies are assigned
    final List<String> attached = attachedPolicy(application, resourcePattern);
    if (CollectionUtility.empty(attached)) {
      if (this.frontend.failonerror())
        throw new ServiceException(ServiceError.WEBSERVICE_POLICY_EMPTY, configName);
      else {
        error(ServiceResourceBundle.format(ServiceError.WEBSERVICE_POLICY_EMPTY, configName));
        return result;
      }
    }

    for (WebServicePolicyHandler.Policy cursor : collection) {
      if (none(cursor.name()))
        throw new ServiceException(ServiceError.WEBSERVICE_POLICY_INVALID, cursor.name());

      if (!attached.contains(cursor.name())) {
        final String[] arguments = { cursor.name(), configName };
        error(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_POLICY_ALREADY_DETACHED, arguments));
        if (this.frontend.failonerror())
          throw new ServiceException(ServiceMessage.WEBSERVICE_POLICY_ALREADY_DETACHED, arguments);
      }

      final String typeName = policySubject.getKeyProperty(AbstractInvocationHandler.JNDI.Type.WLS);
      if ((TYPE_WSEE_PORT_RUNTIME.equals(typeName)) || (TYPE_PORT_CONFIGURATION_CLIENT.equals(typeName))) {
        invoke(policySubjectManager(), "setPolicyRefStatus", new Object[] { resourcePattern, cursor.name(), Boolean.valueOf(enable) }, new String[] { String.class.getName(), String.class.getName(), "java.lang.Boolean" });
        warning(ServiceResourceBundle.format(enable ? ServiceMessage.WEBSERVICE_POLICY_ENABLED : ServiceMessage.WEBSERVICE_POLICY_DISABLED, configName, cursor.name()));
        result = true;
      }
      else {
        boolean changed = false;
        final PolicySet    container = attachedPolicySet(application, resourcePattern);
        final List<Object> containee = container.getPolicySetReferenceOrIntentMapOrPolicyReference();
        for (Object subject : containee) {
          if (subject instanceof PolicyReference) {
            final PolicyReference local = (PolicyReference)subject;
            if (!cursor.name().equals(local.getURI()))
              continue;

            final String[] arguments = {application, cursor.name()};
            if (enable) {
              if (PolicyConstants.AttributeValues.STATUS_ENABLED.equals(local.getStatus())) {
                error(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_POLICY_ALREADY_ENABLED, arguments));
                if (this.frontend.failonerror())
                  throw new ServiceException(ServiceMessage.WEBSERVICE_POLICY_ALREADY_ENABLED, arguments);
              }
              else {
                local.setStatus(PolicyConstants.AttributeValues.STATUS_ENABLED);
                changed = true;
              }
            }
            else {
              if (PolicyConstants.AttributeValues.STATUS_DISABLED.equals(local.getStatus())) {
                error(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_POLICY_ALREADY_DISABLED, arguments));
                if (this.frontend.failonerror())
                  throw new ServiceException(ServiceMessage.WEBSERVICE_POLICY_ALREADY_DISABLED, arguments);
              }
              else {
                local.setStatus(PolicyConstants.AttributeValues.STATUS_DISABLED);
                changed = true;
              }
            }
          }
        }
        if (changed) {
          commit(application, resourcePattern, container);
          warning(ServiceResourceBundle.format(enable ? ServiceMessage.WEBSERVICE_POLICY_ENABLED : ServiceMessage.WEBSERVICE_POLICY_DISABLED, configName, cursor.name()));
        }
        result = true;
      }

      if ("oracle/wsmtom_policy".equals(cursor.name()) && policySubject.toString().indexOf(TYPE_COMPOSITE_SOA) < 0) {
        try {
          if (((Boolean)attribute(policySubject, ATTRIBUTE_SUPPORT_MTOM)).booleanValue())
            this.connection.setAttribute(policySubject, new Attribute(ATTRIBUTE_SUPPORT_MTOM, Boolean.valueOf(enable)));
        }
        catch (Exception emtom) {
          // intentionally left blank
          ;
        }
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Resolve the web service endpoint to configure.
   **
   ** @param  service            the web service to configure.
   **
   ** @return                    the {@link ObjectName} for the the web service
   **                            endpoint to configure.
   **
   ** @throws ServiceException   in case a validation error does occur.
   */
  public ObjectName service(final WebServicePolicyHandler.Service service)
    throws ServiceException {

    final Map<String, String> properties = new HashMap<>();
    switch (service.type) {
      case WLS : properties.put(AbstractInvocationHandler.JNDI.Name.WLS,        String.format("%s", service.serviceName));
                 properties.put(AbstractInvocationHandler.JNDI.Type.WLS,        AbstractInvocationHandler.JNDI.WebService.WLS);
                 properties.put(AbstractInvocationHandler.JNDI.Application.WLS, service.name());
                 break;
      case JRF : properties.put(AbstractInvocationHandler.JNDI.Name.JRF,        String.format("\"%s\"", service.serviceName));
                 properties.put(AbstractInvocationHandler.JNDI.Type.JRF,        AbstractInvocationHandler.JNDI.WebService.JRF);
                 properties.put(AbstractInvocationHandler.JNDI.Application.JRF, service.name());
                 break;
    }
    if (!StringUtility.isEmpty(service.location))
      properties.put(AbstractInvocationHandler.JNDI.Location.WLS, service.location);

    final ObjectName   query  = AbstractInvocationHandler.createObjectName(WebServicePolicyHandler.Service.ServiceType.JRF == service.type ? AbstractInvocationHandler.JNDI.Root.JRF : AbstractInvocationHandler.JNDI.Root.WLS, properties);
    final ObjectName[] result = queryNames(query);
    if (result == null || result.length == 0) {
      final String[] arguments = {service.name(), service.serviceName};
      throw new ServiceException(ServiceError.WEBSERVICE_WEBSERVICE_NOTFOUND, arguments);
    }
    else if (result.length > 1) {
      throw new ServiceException(ServiceError.WEBSERVICE_WEBSERVICE_AMBIGUOS);
    }
    warning(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_SUBJECT_LOCATED, result[0].getKeyProperty(AbstractInvocationHandler.JNDI.Name.JRF)));
    return result[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceEndpoint
  /**
   ** Resolve the web service endpoint port to configure.
   **
   ** @param  service            the web service to configure.
   **
   ** @return                    the {@link ObjectName} for the the web service
   **                            endpoint to configure.
   **
   ** @throws ServiceException   in case a validation error does occur.
   */
  public ObjectName serviceEndpoint(final WebServicePolicyHandler.Service service)
    throws ServiceException {

    final ObjectName endpoint  = service(service);
    ObjectName port = null;
    if (WebServicePolicyHandler.Service.ServiceType.JRF == service.type) {
      final ObjectName[] result = (ObjectName[])attribute(endpoint, AbstractInvocationHandler.JNDI.WebServicePort.JRF);
      if (result == null || result.length == 0) {
        error(ServiceResourceBundle.string(ServiceError.WEBSERVICE_WEBSERVICE_PORT));
        if (this.frontend.failonerror())
          throw new ServiceException(ServiceError.WEBSERVICE_WEBSERVICE_PORT);
      }

      for (ObjectName cursor : result) {
        if (cursor.getKeyProperty(AbstractInvocationHandler.JNDI.Name.JRF).equals(service.portName)) {
          port = cursor;
          break;
        }
      }
    }
    else {
      final ObjectName[] result = (ObjectName[])attribute(endpoint, AbstractInvocationHandler.JNDI.WebServicePort.WLS);
      if (result == null || result.length == 0) {
        error(ServiceResourceBundle.string(ServiceError.WEBSERVICE_WEBSERVICE_PORT));
        if (this.frontend.failonerror())
          throw new ServiceException(ServiceError.WEBSERVICE_WEBSERVICE_PORT);
      }

      for (ObjectName cursor : result) {
        if (cursor.getKeyProperty(AbstractInvocationHandler.JNDI.Name.WLS).equals(service.portName)) {
          port = cursor;
          break;
        }
      }
    }
    if (port == null) {
      error(ServiceResourceBundle.string(ServiceError.WEBSERVICE_WEBSERVICE_PORT));
      if (this.frontend.failonerror())
        throw new ServiceException(ServiceError.WEBSERVICE_WEBSERVICE_PORT);
    }

    return port;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchPolicy
  /**
   ** Fetchs all {@link IPolicy}'s for the collection of pathes specified by
   ** <code>policyPath</code>.
   ** <p>
   ** Invokes the operation <code>retrieveDocuments</code> on an MBean.
   **
   ** @param  collection         the name patterns identifying the
   **                            {@link IPolicy}'s to be retrieved.
   **
   ** @return                    a {@link List} of {@link IPolicy}'s which
   **                            match <code>policyPath</code> where each of
   **                            them represents the WebService Manager Policy.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public List<Reference> fetchPolicy(final Collection<WebServicePolicyHandler.Policy> collection)
    throws ServiceException {

    final List<Reference> property = new ArrayList<Reference>();
    for (WebServicePolicyHandler.Policy cursor : collection) {
      property.add(fetchPolicy(cursor.name(), cursor.parameter()));
    }
    return property;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchPolicy
  /**
   ** Fetchs all {@link IPolicy}'s for the path specified by
   ** <code>policyPath</code>.
   ** <p>
   ** Invokes the operation <code>retrieveDocuments</code> on an MBean.
   **
   ** @param  policyPath         the name pattern identifying the
   **                            {@link IPolicy} to be retrieved.
   ** @param  override           the override properties of the WebServive
   **                            Manager Policy for an atached WebService.
   **
   ** @return                    a {@link List} of {@link IPolicy}'s which
   **                            match <code>policyPath</code> where each of
   **                            them represents the WebService Manager Policy.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public Reference fetchPolicy(final String policyPath, final Map<String, Object> override)
    throws ServiceException {

    final Map<String, Object> flags = new HashMap<>();
    flags.put("subject-count", Boolean.TRUE);
    flags.put("version-info",  Boolean.TRUE);
    final TabularData data = AbstractInvocationHandler.mapToTabularData(flags);

    final String              expresssion = buildNameSearchCriterion(policyPath, SearchCriterion.Comparator.EQUALS).getString();
    final TabularData         result      = (TabularData)invoke(documentManager(), "retrieveDocuments", new Object[]{expresssion, data}, new String[]{String.class.getName(), TabularData.class.getName()});
    final Map<String, String> documents   = AbstractInvocationHandler.mapFromTabularData(result);
    if (CollectionUtility.empty(documents)) {
      error(ServiceResourceBundle.format(ServiceError.WEBSERVICE_POLICY_NOTFOUND, policyPath));
      if (this.frontend.failonerror())
        throw new ServiceException(ServiceError.WEBSERVICE_POLICY_NOTFOUND, policyPath);
    }

    if (documents.size() > 1) {
      error(ServiceResourceBundle.format(ServiceError.WEBSERVICE_POLICY_AMBIGUOS, policyPath));
      if (this.frontend.failonerror())
        throw new ServiceException(ServiceError.WEBSERVICE_POLICY_AMBIGUOS, policyPath);
    }

    final Collection<String>  values      = documents.values();
    final IPolicy             policy = unmarshall(values.iterator().next(), IPolicy.class);
    return Reference.build(policy.getPolicyName(), policy.getStatus().name(), override);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshall
  /**
   ** Unmarshal XML data provided by the string <code>payload</code> and return
   ** the resulting content tree.
   **
   ** @param  <T>                the expected class type.
   ** @param  payload            the XML data to unmarshall.
   ** @param  serviceClass       the class of the content tree to create.
   **
   ** @return                    the result of unmarshalling.
   **                            It needs not be cast to the requested content
   **                            tree class.
   **
   ** @throws ServiceException   if <code>null</code> is provided for
   **                            <code>payload</code>.
   */
  protected static <T> T unmarshall(final String payload, final Class<T> serviceClass)
    throws ServiceException {

    // prevent bogus input
    if (payload == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "payload");

    try {
      if (IPolicy.class == serviceClass)
        return (T)reader.parsePolicy(payload);
      else if (IAssertionTemplate.class == serviceClass)
        return (T)reader.parseAssertionTemplate(new ByteArrayInputStream(payload.getBytes("UTF-8")));
      else if (PolicySet.class == serviceClass)
        return (T)reader.parsePolicySet(payload);
      else
        throw new ServiceException(ServiceError.ABORT, serviceClass.getName());
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshall
  /**
   ** Marshal content tree provided by <code>object</code> and return
   ** the resulting XML data .
   **
   ** @param  <T>                the expected class type.
   ** @param  object             the java content tree to marshall.
   ** @param  serviceClass       the class of the content tree to marshall.
   **
   ** @return                    the result of marshalling.
   **                            It needs not be cast to the requested content
   **                            tree class.
   **
   ** @throws ServiceException   if <code>null</code> is provided for
   **                            <code>object</code>.
   */
  protected static <T> String marshall(final T object, final Class<T> serviceClass)
    throws ServiceException {

    // prevent bogus input
    if (object == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "payload");

    try {
      if (IPolicy.class == serviceClass) {
        final StringWriter buffer = new StringWriter();
        writer.writePolicy((IPolicy)object, buffer);
        return buffer.toString();
      }
      else if (IAssertionTemplate.class == serviceClass) {
        final StringWriter buffer = new StringWriter();
        writer.writeAssertionTemplate((IAssertionTemplate)object, buffer);
        return buffer.toString();
      }
      else if (PolicySet.class == serviceClass) {
        return writer.write((PolicySet)object);
      }
      else
        throw new ServiceException(ServiceError.ABORT, serviceClass.getName());
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildNameSearchCriterion
  /**
   ** Factory method to build the {@link SearchCriterion} to search for
   ** WebService Manager Policies by name patterns.
   **
   ** @param  policyURI         the query name pattern to search for policies.
   ** @param  comparator        the comparator to apply.
   **
   ** @return                    the {@link SearchCriterion} for search for
   **                            WebService Manager Policies by name patterns.
   **
   ** @throws ServiceException   if <code>null</code> is provided for
   **                            <code>policyURI</code>.
   */
  protected static SearchCriterion buildNameSearchCriterion(final String policyURI, final SearchCriterion.Comparator comparator)
    throws ServiceException {

    // prevent bogus input
    if (policyURI == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "policyURI");

    final String[] uri = splitDocumentPath(policyURI);
    return new SearchCriterion(SearchConstants.DOCUMENT_TYPE_POLICIES, true, uri[0], valueExpression(uri[1], comparator, true));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueExpression
  /**
   ** Factory method to build the value criteria to be used by searches for
   ** WebService Manager Policies by value patterns.
   **
   ** @param  value              the value the expression to build is based on.
   **                            Must not be <code>null</code>.
   ** @param  comparator         the {@link SearchCriterion.Comparator} to be
   **                            used.
   **                            Must not be <code>null</code>.
   ** @param  escape             <code>true</code> to escpae the
   **                            <code>one-match-char</code> in pattern based
   **                            expression.
   **
   ** @return                    the string expresseion of the value for search
   **                            for WebService Manager Policies by value
   **                            patterns.
   **
   ** @throws ServiceException   if mandatory parameters are <code>null</code>.
   */
  protected static String valueExpression(String value, SearchCriterion.Comparator comparator, final boolean escape)
    throws ServiceException {

    // prevent bogus input
    if (value == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "value");

    // prevent bogus input
    if (comparator == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "comparator");

    if (!value.endsWith("%") && SearchCriterion.Comparator.LIKE == comparator) {
      value = new StringBuilder().append(value).append("%").toString();
    }
    if (value.startsWith("%") && SearchCriterion.Comparator.LIKE == comparator) {
      value = new StringBuilder().append("%").append(value).toString();
    }
    if (escape && SearchCriterion.Comparator.EQUALS != comparator) {
      value = value.replace("_", "\\_");
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryNames
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the array of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  domain             a string of characters not including the
   **                            character colon <code>:</code>.
   **                            <br>
   **                            It is recommended that the domain should not
   **                            contain the string "//", which is reserved for
   **                            future use.
   **                            <br>
   **                            if the provided value is empty, it will be
   **                            replaced the pattern <code>*</code>.
   ** @param  properties         a unordered collection keys and associated
   **                            values.
   **                            <br>
   **                            Each key is a nonempty string of characters
   **                            which may not contain any of the characters
   **                            comma <code>,</code>, equals<code>=</code>,
   **                            colon <code>:</code>, asterisk <code>*</code>,
   **                            or question mark <code>?</code>.
   **                            <br>
   **                            Each value associated with a key is a string of
   **                            characters that is either unquoted or quoted.
   **                            <br>
   **                            An unquoted value is a possibly empty string of
   **                            characters which may not contain any of the
   **                            characters comma <code>,</code>,
   **                            equals<code>=</code>, colon <code>:</code>, or
   **                            quote <code>"</code>.
   **                            <br>
   **                            If the unquoted value contains at least one
   **                            occurrence of the wildcard characters asterisk
   **                            or question mark, then the object name is a
   **                            property value pattern. The asterisk matches
   **                            any sequence of zero or more characters, while
   **                            the question mark matches any single character.
   **
   ** @return                    an array containing the {@link ObjectName}s for
   **                            the MBeans selected. If no MBean satisfies the
   **                            query, an empty collection is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final ObjectName[] queryNames(final String domain, final Map<String, String> properties)
    throws ServiceException {

    // prevent bogus input
    if (domain == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "domain");

    // prevent bogus state
    if (this.connection == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return AbstractInvocationHandler.queryNames(this.connection, domain, properties);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryNames
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the array of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  query              an {@link ObjectName} which can be used by
   **                            pattern  matching and/or a Query expression.
   **
   ** @return                    an array containing the {@link ObjectName}s for
   **                            the MBeans selected. If no MBean satisfies the
   **                            query, an empty collection is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final ObjectName[] queryNames(final ObjectName query)
    throws ServiceException {

    // prevent bogus input
    if (query == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "query");

    // prevent bogus state
    if (this.connection == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return AbstractInvocationHandler.queryNames(this.connection, query);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   queryBeans
  /**
   ** Returns the names of MBeans controlled by the MBean server.
   ** <br>
   ** This method enables any of the following to be obtained:
   ** <ul>
   **   <li>The names of all MBeans
   **   <li>the names of a set of MBeans specified by
   **       pattern matching on the <code>ObjectName</code> and/or a Query
   **       expression
   **   <li>a specific MBean name (equivalent to testing whether an MBean is
   **       registered).
   ** </ul>
   ** When the object name is <code>null</code> or no domain and key properties
   ** are specified, all objects are selected (and filtered if a query is
   ** specified). It returns the array of {@link ObjectName}s for the
   ** MBeans selected.
   **
   ** @param  query              the {@link ObjectName} pattern identifying the
   **                            MBeans to be retrieved. If <code>null</code> or
   **                            no domain and key properties are specified, all
   **                            the MBeans registered will be retrieved.
   **
   ** @return                    an array containing the {@link ObjectInstance}s
   **                            for the MBeans selected. If no MBean satisfies
   **                            the query, an empty array is returned.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final ObjectInstance[] queryBeans(final ObjectName query)
    throws ServiceException {

    // prevent bogus input
    if (query == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "query");

    // prevent bogus state
    if (this.connection == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return AbstractInvocationHandler.queryBeans(this.connection, query);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes an operation on an MBean.
   ** <p>
   ** Because of the need for a signature to differentiate possibly-overloaded
   ** operations, it is much simpler to invoke operations through an MBean proxy
   ** where possible.
   **
   ** @param  instance           the object name of the MBean {@link ObjectName}
   **                            on which the method is to be invoked.
   ** @param  operation          the name of the operation to invoke.
   ** @param  parameter          an array containing the parameters to be set
   **                            when the operation is invoked.
   ** @param  signature          an array containing the signature of the
   **                            operation. The class objects will be loaded
   **                            using the same class loader as the one used for
   **                            loading the MBean on which the operation was
   **                            invoked.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final Object invoke(final ObjectName instance, final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    // prevent bogus input
    if (instance == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "instance");

    // prevent bogus state
    if (this.connection == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    return AbstractInvocationHandler.invoke(this.connection, instance, operation, parameter, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringAttribute
  /**
   ** Returns an attribute value of a MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean of
   **                            the attribute has to be returned from.
   ** @param  name               the name of the attribute to return.
   **
   ** @return                    the value of the retrieved attribute.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected String stringAttribute(final ObjectName objectName, final String name)
    throws ServiceException {

    return (String)attribute(objectName, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns an attribute value of a MBean.
   **
   ** @param  objectName         the {@link ObjectName} specifiying the MBean of
   **                            the attribute has to be returned from.
   ** @param  name               the name of the attribute to return.
   **
   ** @return                    the value of the retrieved attribute.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected Object attribute(final ObjectName objectName, final String name)
    throws ServiceException {

    // prevent bogus input
    if (objectName == null)
      throw new ServiceException(ServiceError.TASK_ATTRIBUTE_MISSING, "objectName");

    // prevent bogus state
    if (this.connection == null)
      throw new ServiceException(ServiceError.CONTEXT_MANDATORY);

    Object value = null;
    try {
      value = this.connection.getAttribute(objectName, name);
    }
    catch (Exception e) {
      handleThrowable(e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   none
  /**
   ** Validates if the provided string is empty or is equal to
   ** <code>none</code>.
   **
   ** @param  value              the string to test.
   **
   ** @return                    <code>true</code> if the string value is empty
   **                            or is equal to <code>none</code>; otherwise
   **                            <code>false</code>.
   */
  protected boolean none(final String value) {
    return (StringUtility.isEmpty(value) || "none".equalsIgnoreCase(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** Applies all changes on an configuration MBesn {@link ObjectName}.
   **
   ** @param  application        the name and path of the application for which
   **                            the WebService policies have to been fetche.
   **                            For example,
   **                            /domain/server/application#version_number.
   ** @param  resourcePattern    the attribute value for
   **                            <code>PolicySubjectResourcePattern</code>
   **                            defined on the specified mbean.
   ** @param  policySet          the actual set of WebService Manager Policies
   **                            to apply.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  protected final void commit(final String application, final String resourcePattern, final PolicySet policySet)
    throws ServiceException {

    invoke(policyApplicationRuntime(application), "setLocalPolicySet", new Object[]{resourcePattern, marshall(policySet, PolicySet.class)}, new String[] {String.class.getName(), String.class.getName()});
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleThrowable
  /**
   ** Creates and throws a {@link ServiceException} about an unhandled
   ** exception.
   **
   ** @param  throwable          the {@link Throwable} as the reason to notify.
   **
   ** @throws ServiceException   always.
   */
  protected void handleThrowable(final Throwable throwable)
    throws ServiceException {

    error(throwable.getLocalizedMessage());
    if (failonerror()) {
      if (throwable instanceof ServiceException) {
        throw (ServiceException)throwable;
      }
      throw new ServiceException(ServiceError.UNHANDLED, throwable);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   splitDocumentPath
  /**
   ** Splits the provided string in the parts.
   ** <p>
   ** The string is splitted by the last occurence of the character
   ** <code>/</code> (Slash).
   ** <br>
   ** As the result of this operation a array of strings is returned with exact
   ** two elements were the element at index <code>0</code> is the path and the
   ** element at index <code>1</code> the name of a WebService Manager Policy.
   **
   ** @param  documentURI        the query name pattern to search for documents.
   **
   ** @return                    the splitted path array were the element at
   **                            index <code>0</code> contains the path to the
   **                            name of the document which itself is returned
   **                            in the element of index <code>1</code>.
   */
  private static String[] splitDocumentPath(final String documentURI) {
    final String[] result = new String[2];
    result[0] = "";
    result[1] = "";
    if (documentURI != null && documentURI.contains("/")) {
      int index = documentURI.lastIndexOf("/");
      result[0] = documentURI.substring(0, index);
      result[1] = index + 1 < documentURI.length() ? documentURI.substring(index + 1) : "";
    }
    else {
      result[1] = documentURI;
    }
    return result;
  }

  private static List<OverridePropertyType> overrideProperty(final Map<String, Object> override) {
    final List<OverridePropertyType> result = new ArrayList<>();
    if (CollectionUtility.empty(override))
      return result;

    for (Map.Entry<String, Object> cursor : override.entrySet()){
      OverridePropertyType opt = new OverridePropertyType();
      opt.setName(cursor.getKey());
      opt.setValue(cursor.getValue().toString());
      result.add(opt);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourcePattern
  /**
   ** Returns the attribute value for
   ** <code>PolicySubjectResourcePattern</code> defined on the specified mbean.
   ** <p>
   ** A Resource Pattern is String that identifies a resource. Each resource is
   ** also associated with a type called subjecType. The subjectType specifies
   ** whether it is a client/service/component and takes on the values of the
   ** form binding.client.soap or binding.client.soap.http, etc.
   ** <p>
   ** If the value cannot be fetched the standard value <code>ws-wlst</code> is
   ** returned instead.
   **
   ** @param  subject            the {@link ObjectName} to lookup the attribute
   **                            value from.
   **
   ** @return                    the attribute value for
   **                            <code>PolicySubjectResourcePattern</code>
   **                            defined on the specified mbean.
   */
  private String resourcePattern(final ObjectName subject) {
    String pattern = "ws-wlst";
    try {
      pattern = stringAttribute(subject, TYPE_POLICY_SUBJECT_RESOURCEPATTERN);
    }
    catch (Throwable t) {
      pattern = "ws-wlst";
    }
    return pattern;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attachedPolicy
  /**
   ** Fetchs all WebService Manager policies attached to the specified
   ** application which match the specified <code>resourcePattern</code>.
   **
   ** @param  application        the name and path of the application for which
   **                            the WebService policies have to been fetche.
   **                            For example,
   **                            /domain/server/application#version_number.
   ** @param  resourcePattern
   **
   ** @return                    a {@link List} of {@Reference}s containing the
   **                            minimal information to identify a attached
   **                            policy.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  private List<String> attachedPolicy(final String application, final String resourcePattern)
    throws ServiceException {

    // fetch all assinged policies to validate if one of the defined
    // policies are already assigned
    final String       payload     = (String)invoke(policyApplicationRuntime(application), "getEffectivePolicySet", new Object[]{resourcePattern}, new String[]{String.class.getName()});
    final PolicySet    policySet   = unmarshall(payload, PolicySet.class);
    final List<Object> constraints = policySet.getPolicySetReferenceOrIntentMapOrPolicyReference();

    final List<String> attached = new ArrayList<>();
    if (!CollectionUtility.empty(constraints)) {
      for (Iterator i = constraints.iterator(); i.hasNext();) {
        final Object constraint = i.next();
        PolicySet cursor = null;
        if ((constraint instanceof PolicySet)) {
          cursor = (PolicySet)constraint;
        }
        else {
          JAXBElement childElem = (JAXBElement)constraint;
          if (!(childElem.getValue() instanceof PolicySet)) {
            continue;
          }
          cursor = (PolicySet)childElem.getValue();
        }
        final List<Object> policyList = cursor.getPolicySetReferenceOrIntentMapOrPolicyReference();
        for (Iterator j = policyList.iterator(); j.hasNext(); ) {
          final Object reference = j.next();
          if ((reference instanceof PolicyReference)) {
            final PolicyReference subject = (PolicyReference)reference;
            if (PolicyConstants.CATEGORIES_ENUM.SECURITY.value().equals(subject.getCategory())) {
              // TODO: validate how we can obtain the overrides a policy
              // referenve have actual for a webservice
              attached.add(subject.getURI());
            }
          }
        }
      }
    }
    return attached;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attachedPolicySet
  /**
   ** Fetches  WebService Manager policy set attached to the specified
   ** application which match the specified <code>resourcePattern</code>.
   **
   ** @param  application        the name and path of the application for which
   **                            the WebService policies have to been fetche.
   **                            For example,
   **                            /domain/server/application#version_number.
   ** @param  resourcePattern
   **
   ** @return                    a {@link List} of {@Reference}s containing the
   **                            minimal information to identify a attached
   **                            policy.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  private PolicySet attachedPolicySet(final String application, final String resourcePattern)
    throws ServiceException {

    // fetch all assinged policies to validate if one of the defined
    // policies are already assigned
    final String       payload     = (String)invoke(policyApplicationRuntime(application), "getLocalPolicySet", new Object[]{resourcePattern}, new String[]{String.class.getName()});
    return unmarshall(payload, PolicySet.class);
  }
}