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

    File        :   RegistrationInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RegistrationInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

import oracle.iam.access.common.spi.schema.PublicResourcesList;
import oracle.iam.access.common.spi.schema.RregApplicationDomain;
import oracle.iam.access.common.spi.schema.ExcludedResourcesList;
import oracle.iam.access.common.spi.schema.HostPortVariationsList;
import oracle.iam.access.common.spi.schema.ProtectedResourcesList;

////////////////////////////////////////////////////////////////////////////////
// abstract class RegistrationInstance
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>RegistrationInstance</code> provides properties for the registration
 ** service of Oracle Access Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class RegistrationInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  String                 mode;
  String                 username;
  String                 password;
  String                 serviceURL;
  String                 description;

  PublicResourcesList    publicResource;
  ExcludedResourcesList  excludedResource;
  ProtectedResourcesList protectedResource;
  RregApplicationDomain  applicationDomain;
  HostPortVariationsList variation;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RegistrationInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RegistrationInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Access and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode
  /**
   ** Called to inject the argument for attribute <code>mode</code>.
   **
   ** @param  value              the value of the <code>mode</code> property.
   **                            Allowed object is {@link String}.
   */
  public void mode(final String value) {
    this.mode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode
  /**
   ** Returns the value of the <code>mode</code> property.
   **
   ** @return                    the value of the <code>mode</code> property.
   **                            Possible object is {@link String}.
   */
  public final String mode() {
    return this.mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Called to inject the argument for attribute <code>description</code>.
   **
   ** @param  value              the value of the <code>description</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void description(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the value of the <code>description</code> property.
   **
   ** @return                    the value of the <code>description</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Called to inject the argument for attribute <code>username</code>.
   **
   ** @param  value              the value of the <code>username</code> property.
   **                            Allowed object is {@link String}.
   */
  public void username(final String value) {
    this.username = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Returns the value of the <code>username</code> property.
   **
   ** @return                    the value of the <code>username</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public final String username() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Called to inject the argument for attribute <code>password</code>.
   **
   ** @param  value              the value of the <code>password</code> property.
   **                            Allowed object is {@link String}.
   */
  public void password(final String value) {
    this.password = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the value of the <code>password</code> property.
   **
   ** @return                    the value of the <code>password</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public final String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Called to inject the argument for attribute <code>serviceURL</code>.
   **
   ** @param  value              the value of the <code>serviceURL</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void serviceURL(final String value) {
    this.serviceURL = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Returns the value of the <code>serviceURL</code> property.
   **
   ** @return                    the value of the <code>serviceURL</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public final String serviceURL() {
    return this.serviceURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protectedResource
  /**
   ** Called to inject the argument for attribute
   ** <code>protectedResource</code>.
   **
   ** @param  value              the list of protected <code>Resources</code>s
   **                            to set for the instance.
   **                            Allowed object is
   **                            {@link ProtectedResourcesList}.
   */
  public void protectedResource(final ProtectedResourcesList value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "protectedResource"));

    // prevent bogus state
    if (this.protectedResource != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "protectedResource"));

    this.protectedResource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicResource
  /**
   ** Called to inject the argument for attribute <code>publicResource</code>.
   **
   ** @param  value              the list of public <code>Resources</code>s
   **                            to set for the instance.
   **                            Allowed object is {@link PublicResourcesList}.
   */
  public void publicResource(final PublicResourcesList value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "publicResource"));

    // prevent bogus state
    if (this.publicResource != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "publicResource"));

    this.publicResource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludedResource
  /**
   ** Called to inject the argument for attribute <code>excludedResource</code>.
   **
   ** @param  value              the list of excluded <code>Resources</code>s
   **                            to set for the instance.
   **                            Allowed object is
   **                            {@link ExcludedResourcesList}.
   */
  public void excludedResource(final ExcludedResourcesList value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "excludedResource"));

    // prevent bogus state
    if (this.excludedResource != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "excludedResource"));

    this.excludedResource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   variations
  /**
   ** Called to inject the argument for attribute <code>variation</code>.
   **
   ** @param  value              the list all variations of a particular host.
   **                            Allowed object is
   **                            {@link HostPortVariationsList}.
   */
  public void variations(final HostPortVariationsList value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "variations"));

    // prevent bogus state
    if (this.variation != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "variations"));

    this.variation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationDomain
  /**
   ** Called to inject the argument for attribute
   ** <code>applicationDomain</code>.
   **
   ** @param  value              the <code>Application Domain</code>
   **                            to set for the instance.
   **                            Allowed object is
   **                            {@link RregApplicationDomain}.
   */
  public void applicationDomain(final RregApplicationDomain value) {
    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "applicationDomain"));

    // prevent bogus state
    if (this.applicationDomain != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "applicationDomain"));

    this.applicationDomain = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validInteger
  /**
   ** Validates if the specified <code>value</code> string is a valid integer
   ** and is not less a the specified minimum value.
   **
   ** @param  value              the integer value to validate.
   ** @param  minimum            the minimum range of the value.
   **
   ** @return                    <code>true</code> if the given string is
   **                            convertable to an integer value and is greater
   **                            or equal than the given <code>minimum</code>
   **                            value; otherwise <code>false</code>.
   */
  protected static boolean validInteger(final String value, final int minimum) {
    // prevent bogus input
    if (StringUtility.isEmpty(value))
      return false;

    try {
      // convert the value to an integer which will raise a
      // NumberFormatException if the value isn't convertable
      int i = Integer.parseInt(value);
      // validate if the converted value is in range
      if ((i < minimum)) {
        return false;
      }
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validStatus
  /**
   ** Validates if the specified <code>value</code> string is valid.
   **
   ** @param  value              the status string to validate.
   **
   ** @return                    <code>true</code> if the given string is a
   **                            valid status; otherwise <code>false</code>.
   */
  protected static boolean validStatus(final String value) {
    // prevent bogus input
    if (StringUtility.isEmpty(value))
      return false;

    return (value.equals("Enabled") || value.equals("Disabled"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validSecurity
  /**
   ** Validates if the specified <code>security</code> string is a valid mode.
   **
   ** @param  value              the security string to validate.
   **
   ** @return                    <code>true</code> if the given string is
   **                            convertable to a security mode; otherwise
   **                            <code>false</code>.
   */
  protected static boolean validSecurity(final String value) {
    // prevent bogus input
    if (StringUtility.isEmpty(value))
      return false;

    // convert the value to the enum which will raise an
    // IllegalArgumentException if the value doen't fit
    try {
      AccessAgentProperty.Security.from(value);
    }
    catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validAddress
  /**
   ** Validates if the specified <code>address</code> string is a valid IP
   ** Address.
   **
   ** @param  value              the address string to validate.
   **
   ** @return                    <code>true</code> if the given string is
   **                            convertable to a IP address; otherwise
   **                            <code>false</code>.
   */
  protected static boolean validAddress(final String value) {
    // prevent bogus input
    if (StringUtility.isEmpty(value))
      return false;

    final String[] parts = value.split("\\.");
    if (parts.length != 4) {
      return false;
    }

    for (String s : parts) {
      try {
        int i = Integer.parseInt(s);
        if ((i < 0) || (i > 255)) {
          return false;
        }
      }
      catch (NumberFormatException e) {
        return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validURI
  /**
   ** Validates if the specified <code>uri</code> string is a valid {@link URI}.
   **
   ** @param  value              the {@link URI} string to validate.
   **
   ** @return                    <code>true</code> if the given string is
   **                            convertable to a {@link URI}; otherwise
   **                            <code>false</code>.
   */
  protected static boolean validURI(final String value) {
    if (StringUtility.isEmpty(value))
      return false;

    try {
      new URI(value);
    }
    catch (URISyntaxException e) {
      return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validURL
  /**
   ** Validates if the specified <code>url</code> string is a valid {@link URL}.
   **
   ** @param  value              the {@link URL} string to validate.
   **
   ** @return                    <code>true</code> if the given string is
   **                            convertable to a {@link URL}; otherwise
   **                            <code>false</code>.
   */
  protected static boolean validURL(final String value) {
    // prevent bogus input
    if (StringUtility.isEmpty(value))
      return false;

    URL url;
    try {
      url = new URL(value);
    }
    catch (MalformedURLException e) {
      return false;
    }
    String host = url.getHost();
    if (StringUtility.isEmpty(host) || host.charAt(0) == '[' || (host.charAt(0) == '-'))
      return false;

    return (url.getPort() < 0);
  }
}