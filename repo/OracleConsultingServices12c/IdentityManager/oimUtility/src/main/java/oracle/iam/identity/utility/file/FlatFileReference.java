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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Text Stream Facilities

    File        :   FlatFileReference.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileReference.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskDescriptor;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AttributeTransformation;

////////////////////////////////////////////////////////////////////////////////
// class FlatFileReference
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The descriptor to handle object references.
 */
public class FlatFileReference extends TaskDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String entryLinkAttribute  = null;
  private String entrySearchBase     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FlatFileReference</code> which is associated
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
  public FlatFileReference(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FlatFileReference</code> which is associated
   ** the specified Logging Provider <code>loggable</code> for debugging
   ** purpose.
   **
   ** @param  loggable           the {@link Loggable} this
   **                            <code>Metadata Descriptor</code> wrapper
   **                            belongs to.
   ** @param  mapping            the {@link AttributeMapping} of varaiables
   **                            provided by this reference descriptor.
   ** @param  constant           the {@link AttributeMapping} of constants
   **                            provided by this reference descriptor.
   ** @param  transformation     the {@link AttributeTransformation} of this
   **                            reference descriptor.
   */
  public FlatFileReference(final Loggable loggable, final AttributeMapping mapping, final AttributeMapping constant, final AttributeTransformation transformation) {
    // ensure inheritance
    super(loggable, mapping, constant, transformation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryLinkAttribute
  /**
   ** Sets the name of the entry link attribute defined for a LDAP server.
   **
   ** @param  entryLinkAttribute the name of the generic object link attribute.
   */
  public final void entryLinkAttribute(final String entryLinkAttribute) {
    this.entryLinkAttribute = entryLinkAttribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryLinkAttribute
  /**
   ** Returns the name of the entry link attribute defined for a LDAP server.
   **
   ** @return                    the name of the generic object link attribute.
   */
  public final String entryLinkAttribute() {
    return this.entryLinkAttribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entrySearchBase
  /**
   ** Sets the search base of the attribute defined for a flat file.
   **
   ** @param  entrySearchBase    the search base of the object entry.
   */
  public final void entrySearchBase(final String entrySearchBase) {
    this.entrySearchBase = entrySearchBase;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entrySearchBase
  /**
   ** Returns the search base of the attribute defined for a flat file.
   **
   ** @return                    the search base of the object entry.
   */
  public final String entrySearchBase() {
    return this.entrySearchBase;
  }
}