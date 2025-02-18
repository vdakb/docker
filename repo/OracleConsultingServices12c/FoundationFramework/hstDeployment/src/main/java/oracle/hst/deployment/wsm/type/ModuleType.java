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

    File        :   ModuleType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ModuleType.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wsm.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.hst.deployment.spi.WebServicePolicyHandler;

////////////////////////////////////////////////////////////////////////////////
// class ModuleType
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>ModuleType</code> defines the attribute restriction on values that can
 ** be passed as an moduleType attribute to WebServices.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ModuleType extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the exportable/importable object definitions in alphabetical
  // order
  private static final String[] VALUE = {
    WebServicePolicyHandler.Service.ModuleType.SOA.id()
  , WebServicePolicyHandler.Service.ModuleType.WEB.id()
  , WebServicePolicyHandler.Service.ModuleType.WLS.id()
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ModuleType</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ModuleType() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the WebService module type.
   **
   ** @return                    the value of the WebService module type.
   */
  public final String value() {
    return this.getValue();
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