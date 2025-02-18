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

    System      :   Oracle Identity Manager Plugin Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   FirstNameAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FirstNameAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.request;

import java.io.Serializable;

import oracle.iam.request.vo.RequestData;

import oracle.iam.request.exception.RequestServiceException;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.event.AbstractStringAdapter;

////////////////////////////////////////////////////////////////////////////////
// class FirstNameAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Pre-Populate Adapter to fillup the first name field in any request data.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class FirstNameAdapter extends AbstractStringAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepopulate (PrePopulationAdapter)
  /**
   ** Obtains the first name from the identity profile of the beneficiary.
   **
   ** @param  requestData        the contextual values that belongs to a
   **                            request.
   **
   ** @return                    the first name from the identity profile of the
   **                            the beneficiary.
   */
  @Override
  public Serializable prepopulate(final RequestData requestData)
    throws RequestServiceException {

    final String method = "prepopulate";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return beneficiaryFirstName(requestData);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}