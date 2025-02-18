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

    File        :   ResourceInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class ResourceInstance
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>ResourceInstance</code> represents a <code>Resource Object</code>
 ** instance in Identity Manager that might be created, deleted or configured
 ** after or during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceInstance</code> with the specified internal
   ** identifier.
   **
   ** @param  identifier         the internal identifier of the
   **                            <code>Resource Object</code> to handle.
   */
  public ResourceInstance(final long identifier) {
    // ensure inheritance
    super(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceInstance</code> with the specified name.
   **
   ** @param  name               the simple name of the
   **                            <code>Resource Object</code> to create.
   */
  public ResourceInstance(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceInstance</code> with the specified internal
   ** identifier and name.
   **
   ** @param  identifier         the internal identifier of the
   **                            <code>Resource Object</code> to handle.
   ** @param  name               the simple name of the
   **                            <code>Resource Object</code> to create.
   */
  public ResourceInstance(final long identifier, final String name) {
    // ensure inheritance
    super(identifier, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
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
    }

    // ensure inheritance
    super.validate();
  }
}