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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   AccountReference.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountReference.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.service.reconciliation;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskDescriptor;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AttributeTransformation;

////////////////////////////////////////////////////////////////////////////////
// class AccountReference
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The descriptor to handle entity references
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class AccountReference extends TaskDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String  segment = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountReference</code> which is associated
   ** the specified Logging Provider <code>loggable</code> for debugging
   ** purpose.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   */
  public AccountReference(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountReference</code> which is associated
   ** the specified Logging Provider <code>loggable</code> for debugging
   ** purpose.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   ** @param  mapping            the {@link AttributeMapping} of this reference
   **                            descriptor.
   ** @param  transformation     the {@link AttributeTransformation} of this
   **                            reference descriptor.
   */
  public AccountReference(final Loggable loggable, final AttributeMapping mapping, final AttributeTransformation transformation) {
    // ensure inheritance
    super(loggable, mapping, transformation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segment
  /**
   ** Sets the name of the segment name defined for a SAP System.
   **
   ** @param  segment            the name of the segment name defined for a SAP
   **                            System.
   */
  public final void objectClassName(final String segment) {
    this.segment = segment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   segment
  /**
   ** Returns the name of the segment name defined for a SAP System.
   **
   ** @return                    the name of the segment name defined for a SAP
   **                            System.
   */
  public final String segment() {
    return this.segment;
  }
}