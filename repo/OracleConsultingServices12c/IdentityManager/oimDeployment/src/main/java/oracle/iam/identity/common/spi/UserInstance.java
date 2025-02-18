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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   UserInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UserInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class UserInstance
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>UserInstance</code> represents a user profile in Identity Manager that
 ** might be created, deleted or changed after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UserInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserInstance</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   ** <p>
   ** The validation is performed in two ways depended on the passed in mode
   ** requested by argument <code>strict</code>. If <code>strict</code> is set
   ** to <code>true</code> the validation is extended to check for all the
   ** mandatory attributes of an user profile like type. If it's
   ** <code>false</code> only the login name of the user profile has to be
   ** present.
   **
   ** @param  strict             the mode of validation.
   **                            If it's set to <code>true</code> the validation
   **                            is extended to check for all the mandatory
   **                            attributes of an user profile like type. If
   **                            it's <code>false</code> only the name of the
   **                            user profile has to be present.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate(final boolean strict)
    throws BuildException {

    // enforce validation of mandatory attributes if requested
    if (strict) {
      if (this.parameter().isEmpty())
        handleElementMissing("parameter");

      if (StringUtility.isEmpty((String)this.parameter().get(UserManagerConstants.AttributeName.FIRSTNAME.getId())))
        handleAttributeMissing(UserManagerConstants.AttributeName.FIRSTNAME.getId());

      if (StringUtility.isEmpty((String)this.parameter().get(UserManagerConstants.AttributeName.LASTNAME.getId())))
        handleAttributeMissing(UserManagerConstants.AttributeName.LASTNAME.getId());

      if (StringUtility.isEmpty((String)this.parameter().get(OrganizationManagerConstants.AttributeName.ORG_NAME.getId())))
        handleAttributeMissing(OrganizationManagerConstants.AttributeName.ORG_NAME.getId());

      if (StringUtility.isEmpty((String)this.parameter().get(UserManagerConstants.AttributeName.USERTYPE.getId())))
        handleAttributeMissing(UserManagerConstants.AttributeName.USERTYPE.getId());

      if (StringUtility.isEmpty((String)this.parameter().get(UserManagerConstants.AttributeName.EMPTYPE.getId())))
        handleAttributeMissing(UserManagerConstants.AttributeName.EMPTYPE.getId());
    }
    // ensure inhertitance
    super.validate();
  }
}