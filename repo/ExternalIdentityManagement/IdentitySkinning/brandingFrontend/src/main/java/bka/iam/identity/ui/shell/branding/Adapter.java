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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   Branding Customization

    File        :   Adapter.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Adapter.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.1      08.02.2021  DSteding    Changed System Property key to be
                                         aligned with the naming conventions
    1.0.0.0      18.02.2020  SBernet     First release version
*/
package bka.iam.identity.ui.shell.branding;

import java.util.Map;

import java.io.Serializable;

import org.w3c.dom.Document;

import oracle.mds.core.MDSSession;
import oracle.mds.core.MOReference;
import oracle.mds.core.MetadataNotFoundException;

import oracle.mds.naming.InvalidReferenceException;
import oracle.mds.naming.InvalidReferenceTypeException;

import oracle.adf.share.ADFContext;

import oracle.hst.foundation.faces.ADF;
import oracle.hst.foundation.faces.JSF;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.iam.ui.platform.model.common.OIMClientFactory;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import bka.iam.identity.ui.BrandingError;
import bka.iam.identity.ui.BrandingException;

import bka.iam.identity.ui.shell.branding.model.Rule;
import bka.iam.identity.ui.shell.branding.model.Component;
import bka.iam.identity.ui.shell.branding.model.Serializer;
import bka.iam.identity.ui.shell.branding.model.Configuration;

////////////////////////////////////////////////////////////////////////////////
// class Adapter
// ~~~~~ ~~~~~~~
/**
 ** Data Access Object used by end users to get branding component value based
 ** on pre-defined rules.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.1
 ** @since   1.0.0.0
 */
public class Adapter implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PROPERTY                    = "BKA.Branding.Path";

  private static final String EL_USR_ATTR                 = "#{oimcontext.currentUser['ATTRIBUTE_NAME']}";

  private static final String SHOWAPPNAVBAR               = "showAppNavbar";
  private static final String SHOWSTATUSINDICATOR         = "showStatusIndicator";
  private static final String LOGOIMAGEPATH               = "logoImagePath";
  private static final String LOGOSTYLECLASS              = "logoStyleClass";
  private static final String LOGOSHORTDESC               = "logoShortDesc";
  private static final String BRANDINGMAXWIDTH            = "brandingMaxWidth";
  private static final String LOGOSMALLIMAGEPATH          = "logoSmallImagePath";

  private static final String SHOWAPPNAVBAR_DEFAULT       = "#{attrs.showAppNavbar}";
  private static final String SHOWSTATUSINDICATOR_DEFAULT = "#{attrs.showStatusIndicator}";
  private static final String LOGOIMAGEPATH_DEFAULT       = "/images/Participant/provider.png";
  private static final String LOGOSTYLECLASS_DEFAULT      = "#{attrs.logoStyleClass}";
  private static final String LOGOSHORTDESC_DEFAULT       = "#{attrs.logoShortDesc}";
  private static final String BRANDINGMAXWIDTH_DEFAULT    = "#{attrs.brandingMaxWidth}";
  private static final String LOGOSMALLIMAGEPATH_DEFAULT  = "#{attrs.logoSmallImagePath}";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8833311101138557299")
  private static final long   serialVersionUID = 3731894014058733538L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the configuration model
  private Configuration       configuration;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Adapter</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Adapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShowAppNavbar
  /**
   ** Returns the evaluated ShowAppNavbar component value for the current user.
   **
   ** @return                    the evaluated ShowAppNavbar component value for
   **                            the current user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BrandingException  if the xml file loading fails.
   */
  public String getShowAppNavbar()
    throws BrandingException {

    final String showAppNavBar = evaluateComponent(SHOWAPPNAVBAR);
    return showAppNavBar != null ? showAppNavBar : SHOWAPPNAVBAR_DEFAULT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShowStatusIndicator
  /**
   ** Returns the evaluated showStatusIndicator component value for the current
   ** user.
   **
   ** @return                    the evaluated showStatusIndicator component
   **                            value for the current user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BrandingException  if the xml file loading fails.
   */
  public String getShowStatusIndicator()
    throws BrandingException {

    final String showStatusIndicator = evaluateComponent(SHOWSTATUSINDICATOR);
    return showStatusIndicator != null ? showStatusIndicator : SHOWSTATUSINDICATOR_DEFAULT;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoImagePath
  /**
   ** Returns the evaluated logoImagePath component value for the current user.
   **
   ** @return                    the evaluated logoImagePath component value for
   **                            the current user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BrandingException  if the xml file loading fails.
   */
  public String getLogoImagePath()
    throws BrandingException {

    final String logoImagePath = evaluateComponent(LOGOIMAGEPATH);
    return logoImagePath != null ? logoImagePath : LOGOIMAGEPATH_DEFAULT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoStyleClass
  /**
   ** Returns the evaluated logoStyleClass component value for the current user.
   **
   ** @return                    the evaluated logoStyleClass component value
   **                            for the current user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BrandingException  if the xml file loading fails.
   */
  public String getLogoStyleClass()
    throws BrandingException {

    final String logoStyleClass = evaluateComponent(LOGOSTYLECLASS);
    return logoStyleClass != null ? logoStyleClass : LOGOSTYLECLASS_DEFAULT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLogoShortDesc
  /**
   *  Returns the evaluated logoShortDesc component value for the current user.
   **
   ** @return                    the logoShortDesc value specific to the
   **                            connected user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BrandingException  if the xml file loading fails.
   */
  public String getLogoShortDesc()
    throws BrandingException {

    final String logoShortDesc = evaluateComponent(LOGOSHORTDESC);
    return logoShortDesc != null ? logoShortDesc : LOGOSHORTDESC_DEFAULT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBrandingMaxWidth
  /**
   ** Returns the evaluated brandingMaxWidth component value for the current
   ** user.
   **
   ** @return                    the evaluated brandingMaxWidth component value
   **                            for the current user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BrandingException  if the xml file loading fails.
   */
  public String getBrandingMaxWidth()
    throws BrandingException {

    final String brandingMaxWidth = evaluateComponent(BRANDINGMAXWIDTH);
    return brandingMaxWidth != null ? brandingMaxWidth : BRANDINGMAXWIDTH_DEFAULT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBrandingMaxWidth
  /**
   ** Returns the evaluated logoSmallImagePath component value for the current
   ** user.
   **
   ** @return                    the evaluated logoSmallImagePath component
   **                            value for the current user.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BrandingException  if the xml file loading fails.
   */
  public String getLogoSmallImagePath()
    throws BrandingException {

    final String logoSmallImagePath = evaluateComponent(LOGOSMALLIMAGEPATH);
    return logoSmallImagePath != null ? logoSmallImagePath : LOGOSMALLIMAGEPATH_DEFAULT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Factory method to prepare a {@link BrandingException} to been thrown.
   **
   ** @param  code               the key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the {@link Throwable} causing the
   **                            {@link BrandingException} to create.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the exception to be thrown.
   **                            <br>
   **                            Possible object is {@link BrandingException}}.
   */
  public static BrandingException exception(final String code, final Throwable throwable) {
    return new BrandingException(code, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Factory method to prepare a {@link OIMRuntimeException} to been thrown.
   **
   ** @param  code               the key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the array of substitution parameters.
   **                            <br>
   **                            Allowed object is array of {@link String}s.
   **
   ** @return                    the exception to be thrown.
   **                            <br>
   **                            Possible object is {@link BrandingException}}.
   */
  public static BrandingException exception(final String code, final String... arguments) {
    return new BrandingException(code, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initialize the configuration model.
   **
   ** @throws BrandingException  in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void initialize()
    throws BrandingException {

    // initialize the configuration model
    if (this.configuration == null) {
      String path = null;
      try {
        path = OIMClientFactory.getCachedServices().getPropertyAsString(PROPERTY);
      }
      catch (SystemConfigurationServiceException e) {
        throw new BrandingException(BrandingError.BRANDING_CONFIGURATION_PROPERTY, PROPERTY);
      }

      try {
        final MDSSession session  = (MDSSession)ADF.current().getMDSSessionAsObject();
        final Document   document = session.getMetadataObject(MOReference.create(path)).getDocument();
        this.configuration = Configuration.build(document);
      }
      catch (InvalidReferenceException e) {
        throw new BrandingException(BrandingError.BRANDING_CONFIGURATION_STREAM, e);
      }
      catch (InvalidReferenceTypeException e) {
        throw new BrandingException(BrandingError.BRANDING_CONFIGURATION_STREAM, e);
      }
      catch (MetadataNotFoundException e) {
        throw new BrandingException(BrandingError.BRANDING_CONFIGURATION_STREAM, e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compatibleRule
  /**
   ** Checks the given rule et returns <code>true</code>. Get the current user's
   ** attribute value and verify if if matches with the given regular expression
   ** in the {@link Rule} object.
   **
   ** @param   rule          the rule instance to check the compatibility with
   **                        the current user.
   **                        <br>
   **                        Allowed object is {@link Rule}s.
   **
   ** @return                <code>true</code> if rule is compatible with the
   **                        current user or <code>false</code> if it is not.
   */
  private boolean compatibleRule(final Rule rule) {
    final String attribute = rule.getAttribute();
    final String filter = rule.getFilter();
    //prevent bogus state
    if (StringUtility.isEmpty(attribute) || StringUtility.isEmpty(filter)) {
      return false;
    }
    final String exp = EL_USR_ATTR.replace("ATTRIBUTE_NAME", attribute);
    final String evaluatedExp = JSF.valueFromExpression(exp, String.class);
    if (StringUtility.isEmpty(evaluatedExp)) {
        return false;
    }
    return evaluatedExp.matches(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evaluateComponent
  /**
   ** Return the appropriate value based on the current user and the rules
   ** define in the xml file.
   ** Before evaluating the best value based on rules, it loads the xml file
   ** (ie {@link Serializer}), then looks for the
   ** {@param componentName} and iterates over the rules evaluating the best
   ** value that correspond to the current user.
   ** In case of unauthenticated user; it returns null;
   **
   ** @param  componentName      the component name that will be evaluated.
   **                            Allowed object is {@link String}s.
   **
   ** @return                    the computed value of the requested component
   **                            based on rules define in the xml file.
   **
   ** @throws BrandingException  if the xml file loading fails.
   */
  private String evaluateComponent(final String componentName)
    throws BrandingException {

    String evaluatedComponent = null;
    if (isAuthenticatedUser()) {
      if (this.configuration == null) {
        initialize();
      }
      final Component component = this.configuration.get(componentName);
      if (component != null) {
        for (Map.Entry<String, Rule> entry : component.getRule().entrySet()) {
          Rule rule = entry.getValue();
          //if rule matchs, get the value of this rule
          if (compatibleRule(rule)) {
            evaluatedComponent = rule.getValue();
            break;
          }
        }
        // If no rule match, get default rule
        if (evaluatedComponent == null && component.getDefault() != null) {
          evaluatedComponent = component.getDefault().getValue();
        }
      }
    }
    return evaluatedComponent != null ? JSF.valueFromExpression(evaluatedComponent, String.class) : evaluatedComponent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAuthenticatedUser
  /**
   ** Check the security context and returns <code>true</code> if user is
   ** authenticated and <code>false</code> if the user is anonymous
   ** (unauthenticated user).
   **
   **
   ** @return               <code>true</code> if user is authenticated and
   **                       <code>false</code> if the user is anonymous
   **                       (unauthenticated user).
   */
  private boolean isAuthenticatedUser() {
    boolean isAuthenticatedUser = false;
    final String[] adfroles = ADFContext.getCurrent().getSecurityContext().getUserRoles();
    for (String role : adfroles) {
      isAuthenticatedUser = role.equals("authenticated-role");
      if (isAuthenticatedUser) {
        break;
      }
    }
    return isAuthenticatedUser;
  }
}