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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   AbstractServiceProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractServiceProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.HashMap;

import java.io.Serializable;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.iam.platform.context.ContextAware;

import oracle.iam.identity.foundation.AbstractAdapterTask;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractServiceProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Offers common methods to handle the data object <code>Users</code> in an
 ** <code>Entity Adapter</code> or <code>Rule Generator</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractServiceProvider extends AbstractAdapterTask {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractServiceProvider</code> task adpater that
   ** allows use as  a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public AbstractServiceProvider(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameterValue
  /**
   ** Load the resource bundel.
   **
   ** @param  key                the key of the parameter value to lookup.
   ** @param  parameter          the parameter mapping provided by the server.
   **
   ** @return                    the value obtained from the parameter mapping.
   **                            May be <code>null</code>.
   */
  protected String parameterValue(final String key, final HashMap<String, Serializable> parameter) {
    String value = (parameter.get(key) instanceof ContextAware) ? (String)((ContextAware)parameter.get(key)).getObjectValue() : (String)parameter.get(key);
    return value;
  }
}