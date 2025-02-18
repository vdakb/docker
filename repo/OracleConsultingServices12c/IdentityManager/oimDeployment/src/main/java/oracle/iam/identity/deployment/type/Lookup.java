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

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Lookup.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.LookupInstance;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** <code>Lookup</code> represents a <code>Lookup Definition</code> instance in
 ** Identity Manager that might be created or configured after or during an
 ** import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Lookup extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Lookup</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Lookup() {
    // ensure inheritance
    super(new LookupInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>Lookup Definition</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(((LookupInstance)this.delegate).group()))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setGroup
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>group</code>.
   **
   ** @param  group              the group name of the
   **                            <code>Lookup Definition</code> instance in
   **                            Identity Manager.
   */
  public void setGroup(final String group) {
    checkAttributesAllowed();
    ((LookupInstance)this.delegate).group(group);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setField
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>field</code>.
   **
   ** @param  field              the field name of the
   **                            <code>Lookup Definition</code> instance in
   **                            Identity Manager.
   */
  public void setField(final String field) {
    checkAttributesAllowed();
    ((LookupInstance)this.delegate).field(field);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMeaning
  /**
   ** Called to inject the argument for parameter <code>meaning</code>.
   **
   ** @param  meaning            the meaning of the
   **                            <code>Lookup Definition</code> in Identity
   **                            Manager.
   */
  public void setMeaning(final String meaning) {
    checkAttributesAllowed();
    ((LookupInstance)this.delegate).meaning(meaning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRequired
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>required</code>.
   **
   ** @param  required           the required flag of the
   **                            <code>Lookup Definition</code> instance in
   **                            Identity Manager.
   */
  public void setRequired(final boolean required) {
    checkAttributesAllowed();
    ((LookupInstance)this.delegate).required(required);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link LookupInstance} delegate of Identity Manager to handle.
   **
   ** @return                    the {@link LookupInstance} delegate of
   **                            Identity Manager to handle.
   */
  public final LookupInstance instance() {
    if (isReference())
      return ((Lookup)getCheckedRef()).instance();

    return (LookupInstance)this.delegate;
  }
}