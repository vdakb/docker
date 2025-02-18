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

    File        :   LDAPModifyContent.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPModifyContent.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.util.List;
import java.util.ArrayList;

import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;

import javax.naming.directory.ModificationItem;

import oracle.iam.directory.common.FeatureConstant;
import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// class LDAPModifyContent
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An object of this class represents the content of an LDIF record that
 ** specifies modifications to an entry.
 ** <p>
 ** This class implements the {@link LDAPRecord} interface.
 ** <p>
 ** To get this object from an <code>LDAPRecord</code> object, use the
 ** <code>content</code> method and cast the return value as
 ** <code>LDAPModifyContent</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LDAPModifyContent extends LDAPRecord {

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<ModificationItem> modification = new ArrayList<ModificationItem>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPModifyContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDAPModifyContent() {
    // ensure inheritance
    super(MODIFICATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>LDAPModifyContent</code> object.
   ** <p>
   ** To specify the modifications to be made to the entry, use
   ** the <code>add</code> method.
   **
   ** @param  namespace          the distinguished name this record belongs to
   **                            representing the attributes of the entry.
   **
   ** @see LDAPRecord#add(Attribute)
   */
  public LDAPModifyContent(final String namespace) {
    // ensure inheritance
    super(MODIFICATION, namespace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>LDAPModifyContent</code> object with the specified
   ** attributes.
   **
   ** @param  modification       an array of {@link ModificationItem} objects
   **                            representing the attributes of the entry to be
   **                            added.
   **
   ** @throws FeatureException   if the {@link ModificationItem}s could not be
   **                            added to the collection of modifications.
   */
  public LDAPModifyContent(final ModificationItem[] modification)
    throws FeatureException {

    // ensure inheritance
    super(MODIFICATION);

    // initialize instance
    for (int i = 0; i < modification.length; i++)
      add(modification[i].getModificationOp(), modification[i].getAttribute());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifications
  /**
   ** Retrieves the list of the attributes specified in the content of the LDIF
   ** record.
   **
   ** @return                    an array of <code>ModificationItem</code>
   **                            objects that represent modifications specified
   **                            in the content of the LDAP record.
   */
  public ModificationItem[] modifications() {
    return this.modification.toArray(new ModificationItem[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (overridden)
  /**
   ** Adds an attribute to the content of the content.
   **
   ** @param  attribute          the attribute to add
   */
  @Override
  public void add(final Attribute attribute)
    throws FeatureException {

    if (FeatureConstant.DN.equalsIgnoreCase(attribute.getID()))
      throw new FeatureException(FeatureError.LDIF_UNEXPECTED, FeatureConstant.DN);

    add(DirContext.ADD_ATTRIBUTE, attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the attribute modification associated with a named object.
   ** <p>
   ** The order of the modifications is not specified. Where possible, the
   ** modifications are performed atomically.
   **
   ** @param  operation          the type of modification to make.
   **                            This can be one of the following:
   **                            <ul>
   **                              <li>{@link DirContext#ADD_ATTRIBUTE}
   **                                  (the value should be added to the
   **                                  attribute)
   **                              <li>{@link DirContext#REMOVE_ATTRIBUTE}
   **                                  (the value should be removed from the
   **                                  attribute)
   **                              <li>{@link DirContext#REPLACE_ATTRIBUTE}
   **                              (the value should replace the existing value
   **                              of the attribute)
   **                            </ul>
   ** @param  attribute          the attribute (possibly with values) to modify.
   **
   ** @throws FeatureException   if the {@link Attribute} could not be added
   **                            to the collection of modifications.
   */
  public void add(final int operation, final Attribute attribute)
    throws FeatureException {

    this.modification.add(new ModificationItem(operation, attribute));
  }
}