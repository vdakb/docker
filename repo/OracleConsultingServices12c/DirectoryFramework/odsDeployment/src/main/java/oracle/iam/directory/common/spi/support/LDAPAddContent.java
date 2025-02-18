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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   LDAPAddContent.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPAddContent.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import javax.naming.ldap.Control;

import oracle.iam.directory.common.FeatureConstant;

////////////////////////////////////////////////////////////////////////////////
// class LDAPAddContent
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** An object of this class represents the content of an LDIF record that
 ** specifies a new entry to be added.
 ** <p>
 ** This class implements the {@link LDAPRecord} interface.
 ** <p>
 ** To get this object from an <code>LDAPRecord</code> object, use the
 ** <code>content</code> method and cast the return value as
 ** <code>LDAPAddContent</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LDAPAddContent extends LDAPRecord {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDAPAddContent() {
    // ensure inheritance
    super(ADD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  namespace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDAPAddContent(final String namespace) {
    // ensure inheritance
    super(ADD, namespace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  namespace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDAPAddContent(final String namespace, final Control[] controls) {
    // ensure inheritance
    super(ADD, namespace, controls);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPAddContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  controls           an array of {@link Control} objects or
   **                            <code>null</code> if none are to be specified.
   ** @param  other             the record.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDAPAddContent(final Control[] controls, final LDAPRecord other) {
    // ensure inheritance
    super(ADD, controls, other);
  }
}