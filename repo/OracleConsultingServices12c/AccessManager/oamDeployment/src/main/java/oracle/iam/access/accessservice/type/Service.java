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

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessservice.type;

import oracle.iam.access.common.type.FeatureParameter;

import oracle.iam.access.common.spi.AvailableServiceType;
import oracle.iam.access.common.spi.AvailableServiceInstance;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** <code>Service</code> represents an <code>Access Service</code>
 ** instance in Oracle Access Manager that might be enabled or disabled during an
 ** import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Service extends FeatureParameter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the parameter definitions in alphabetical order
  private static final String[] VALUE = {
    AvailableServiceType.FEDIDP.id()
  , AvailableServiceType.FEDSP.id()
  , AvailableServiceType.ICS.id()
  , AvailableServiceType.MOBIL.id()
  , AvailableServiceType.OAMDCC.id()
  , AvailableServiceType.OAMECC.id()
  , AvailableServiceType.OAMDAS.id()
  , AvailableServiceType.OAMSSO.id()
  , AvailableServiceType.OAMOAUTH.id()
  , AvailableServiceType.OAAM.id()
  , AvailableServiceType.OAUTH.id()
  , AvailableServiceType.OESSO.id()
  , AvailableServiceType.OIDC.id()
  , AvailableServiceType.OICRP.id()
  , AvailableServiceType.OICREST.id()
  , AvailableServiceType.STS.id()
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final AvailableServiceInstance delegate = new AvailableServiceInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Service() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> with the specified name and value.
   **
   ** @param  physicalType       the name of the service aka the physical type
   **                            of the Oracle Acccess Manager.
   ** @param  name               the value for the name.
   */
  public Service(final String physicalType, final String name) {
    // ensure inheritance
    super(physicalType, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT kernel to inject the argument for parameter name.
   **
   ** @param  name             the name of the Oracle Identity Manager object
   **                          this category wraps.
   */
  @Override
  public void setName(final String name) {
    // validate the passed name to verify if its legal
    super.setValue(name);
    this.delegate.type(AvailableServiceType.from(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link AvailableServiceInstance} delegate of Access Server
   ** object to handle.
   **
   ** @return                    the {@link AvailableServiceInstance} delegate.
   */
  public final AvailableServiceInstance delegate() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return VALUE;
  }
}