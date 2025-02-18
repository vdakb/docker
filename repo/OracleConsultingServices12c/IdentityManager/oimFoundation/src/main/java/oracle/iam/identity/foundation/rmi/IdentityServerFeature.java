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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityServerFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityServerFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.rmi;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerFeature
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityServerFeature</code> provides the base feature
 ** description of a Identity Manager.
 ** <br>
 ** Implementation of OIM may vary in locations of certain informations and
 ** object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class IdentityServerFeature extends AbstractLookup {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityServerFeature</code> which is
   ** associated with the specified task.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private IdentityServerFeature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityServerFeature</code> which is associated with
   ** the specified task and belongs to the Metadata Descriptor specified by the
   ** given name.
   ** <br>
   ** The Metadata Descriptor will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instanceName       the name of the <code>Metadata Descriptor</code>
   **                            instance where this configuration wrapper will
   **                            obtains the values.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in the Oracle Identity Manager metadata
   **                            entries.
   */
  private IdentityServerFeature(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   urlEncoding
  /**
   ** Returns the url encoding.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerConstant#URL_ENCODING}.
   ** <p>
   ** If {@link IdentityServerConstant#URL_ENCODING} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link IdentityServerConstant#URL_ENCODING_DEFAULT}.
   **
   ** @return                    the url encoding.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String urlEncoding() {
    return stringValue(IdentityServerConstant.URL_ENCODING, IdentityServerConstant.URL_ENCODING_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator character for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerConstant#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link IdentityServerConstant#MULTIVALUE_SEPARATOR} is not mapped in
   ** the underlying <code>Metadata Descriptor</code> this method returns
   ** {@link IdentityServerConstant#MULTIVALUE_SEPARATOR_DEFAULT}
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String multiValueSeparator() {
    return stringValue(IdentityServerConstant.MULTIVALUE_SEPARATOR, IdentityServerConstant.MULTIVALUE_SEPARATOR_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementPrefixRequired
  /**
   ** Returns the <code>true</code> if the entitlements has to be prefixed with
   ** the internal system identifier and the name of the
   ** <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link IdentityServerConstant#ENTITLEMENT_PREFIX_REQUIRED}.
   ** <p>
   ** If {@link IdentityServerConstant#ENTITLEMENT_PREFIX_REQUIRED} is not mapped
   ** in the underlying <code>Metadata Descriptor</code> this method returns
   ** <code>true</code>.
   **
   ** @return                    <code>true</code> the entitlements has to be
   **                            prefixed with the internal system identifier
   **                            and the name of the <code>IT Resource</code>;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean entitlementPrefixRequired() {
    return booleanValue(IdentityServerConstant.ENTITLEMENT_PREFIX_REQUIRED, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>IdentityServerFeature</code> which is
   ** associated with the specified task.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    a non-populated
   **                            <code>IdentityServerFeature</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityServerFeature</code>.
   **
   */
  public static IdentityServerFeature build(final Loggable loggable) {
    return new IdentityServerFeature(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Constructs a <code>IdentityServerFeature</code> which is associated with
   ** the specified task and belongs to the Metadata Descriptor specified by the
   ** given name.
   ** <br>
   ** The Metadata Descriptor will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instanceName       the name of the <code>Metadata Descriptor</code>
   **                            instance where this configuration wrapper will
   **                            obtains the values.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IdentityServerFeature</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>IdentityServerFeature</code>.
   **
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in the Oracle Identity Manager metadata
   **                            entries.
   */
  public static IdentityServerFeature build(final Loggable loggable, final String instanceName)
    throws TaskException {

    return new IdentityServerFeature(loggable, instanceName);
  }
}