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

    File        :   LookupInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LookupInstance.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import oracle.hst.deployment.spi.AbstractInstance;
import oracle.hst.foundation.utility.StringUtility;

import org.apache.tools.ant.BuildException;

////////////////////////////////////////////////////////////////////////////////
// class LookupInstance
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>LookupInstance</code> represents a Lookup Definition in Oracle
 ** Identity Manager that might be created, deleted or configured after or
 ** during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LookupInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String  group = null;
  private String  field = null;
  private String  meaning = null;
  private boolean required = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public LookupInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Called to inject the argument for parameter <code>group</code>.
   **
   ** @param  group              the group of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public void group(final String group) {
    this.group = group;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Returns the group of the <code>Lookup Definition</code> of Identity
   ** Manager to handle.
   **
   ** @return                    the group of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public final String group() {
    return this.group;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   field
  /**
   ** Called to inject the argument for parameter <code>field</code>.
   **
   ** @param  field              the field of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public void field(final String field) {
    this.field = field;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   field
  /**
   ** Returns the field of the <code>Lookup Definition</code> of Identity
   ** Manager to handle.
   **
   ** @return                    the field of the <code>Lookup Definition</code>
   **                            in Identity Manager.
   */
  public final String field() {
    return this.field;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   meaning
  /**
   ** Called to inject the argument for parameter <code>meaning</code>.
   **
   ** @param  meaning            the meaning of the
   **                            <code>Lookup Definition</code> in Identity
   **                            Manager.
   */
  public void meaning(final String meaning) {
    this.meaning = meaning;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   meaning
  /**
   ** Returns the meaning of the <code>Lookup Definition</code> in Identity
   ** Manager to handle.
   **
   ** @return                    the meaning of the
   **                            <code>Lookup Definition</code> in Identity
   **                            Manager.
   */
  public final String meaning() {
    return this.meaning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required
  /**
   ** Called to inject the argument for parameter <code>required</code>.
   **
   ** @param  required           the required flag of the
   **                            <code>Lookup Definition</code> instance in
   **                            Identity Manager.
   */
  public void required(final boolean required) {
    this.required = required;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   field
  /**
   ** Returns the required flag of the <code>Lookup Definition</code> in
   ** Identity Manager to handle.
   **
   ** @return                    the required flag of the
   **                            <code>Lookup Definition</code> instance in
   **                            Identity Manager.
   */
  public final boolean required() {
    return this.required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the group to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (StringUtility.isEmpty(this.group()))
      handleAttributeMissing("group");

    // ensure inheritance
    super.validate();
  }
}