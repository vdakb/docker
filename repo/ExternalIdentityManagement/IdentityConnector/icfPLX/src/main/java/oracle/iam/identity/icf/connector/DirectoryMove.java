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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryMove.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryMove.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import oracle.iam.identity.icf.foundation.SystemException;

import org.identityconnectors.framework.common.objects.ObjectClass;
////////////////////////////////////////////////////////////////////////////////
// class DirectoryMove
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Encapsulates the move operations of the connector.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryMove extends DirectoryOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Use the <code>DirectoryMove</code> passed in to immediately connect to a
   ** Directory Service. If the {@link DirectoryEndpoint} fails a
   ** {@link DirectoryException} will be thrown.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to move the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  private DirectoryMove(final DirectoryEndpoint context, final ObjectClass type)
    throws SystemException {

    // ensure inheritance
    super(context, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Return the entry modify operation.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to move the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   **
   ** @return                    the operation.
   **                            <br>
   **                            Possible object is {@link DirectoryMove}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  public static DirectoryMove build(final DirectoryEndpoint context, final ObjectClass type)
    throws SystemException {

    return new DirectoryMove(context, type);
  }

  public void execute(final DirectoryName origin, final DirectoryName target)
    throws SystemException {
    
    DirectoryRename.build(this.endpoint(), this.type).execute(origin, target);
/*
    String sourcePath = name;
    if (!StringUtility.empty(source))
      sourcePath += "," + source;

    String targetPath = name;
    if (!StringUtility.empty(target))
      targetPath += "," + target;

    rename(sourcePath, targetPath);
*/
  }
}
