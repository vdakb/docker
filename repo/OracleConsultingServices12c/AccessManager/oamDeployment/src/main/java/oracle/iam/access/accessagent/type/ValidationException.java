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

    File        :   ValidationException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ValidationException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessagent.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.access.common.type.DelegatingDataType;

import oracle.iam.access.common.spi.schema.IpValidationExceptions;

////////////////////////////////////////////////////////////////////////////////
// class ValidationException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** IP address validation is specific to <code>Access Agent</code>s.
 ** <br>
 ** It determines if a client's IP address is the same as the IP address stored
 ** in the <b><code>ObSSOCookie</code></b> generated for single sign-on. The
 ** <code>ipValidation</code> parameter turns IP address validation on and off.
 ** If <code>ipValidation</code> is <code>true</code>, the IP address stored in
 ** the <b><code>ObSSOCookie</code></b> must match the client's IP address,
 ** otherwise, the cookie is rejected and the user must reauthenticate. The
 ** default <code>ipValidation</code> setting is <code>true</code>.
 ** <p>
 ** The <code>ipValidation</code> parameter parameter can cause problems with
 ** certain Web applications. For example, Web applications managed by a proxy
 ** server typically change the user's IP address, substituting the IP address
 ** of the proxy. This prevents single sign-on using the
 ** <b><code>ObSSOCookie</code></b>.
 ** <p>
 ** The IP Validation Exceptions lists IP addresses that are exceptions to this
 ** process. If <code>ipValidation</code> is <code>true</code>, the IP address
 ** can be compared to the IP Validation Exceptions list. If the address is
 ** found on the exceptions list, it does not need to match the IP address
 ** stored in the cookie. You can add as many IP addresses as needed. These
 ** addresses are the actual IP addresses of the client, not the IP addresses
 ** that are stored in the <b><code>ObSSOCookie</code></b>. If a cookie arrives
 ** from one of the exception IP addresses, the Access System ignores the
 ** address stored in the <b><code>ObSSOCookie</code></b> cookie for validation.
 ** <br>
 ** For example, the IP addresses in the IP Validation Exceptions can be used
 ** when the IP address in the cookie is for a reverse proxy.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ValidationException extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final IpValidationExceptions delegate = factory.createIpValidationExceptions();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Address
  // ~~~~~ ~~~~~~~
  public static class Address extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    String value;

    ////////////////////////////////////////////////////////////////////////////
  // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Address</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Address() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setValue
    /**
     ** Sets the value of the <code>value</code> property.
     **
     ** @param  value            the value of the <code>value</code> property.
     **                          Allowed object is {@link String}.
     */
    public void setValue(final String value) {
      this.value = value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ValidationException</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ValidationException() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAddress
  /**
   ** Sets the value of the <code>address</code> property.
   **
   ** @param  value            the value of the <code>address</code> property.
   **                          Allowed object is {@link Address}.
   */
  public void addConfiguredAddress(final Address value) {
    // verify if the reference id is set on the element to prevent inconsistency
    if (isReference())
      throw noChildrenAllowed();

    // prevent bogus input
    if (value == null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_PARAMETER_MISSING, "address"));

    // prevent bogus state
    if (this.delegate.getIpAddress().contains(value.value))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, "address"));

    this.delegate.getIpAddress().add(value.value);
  }
}