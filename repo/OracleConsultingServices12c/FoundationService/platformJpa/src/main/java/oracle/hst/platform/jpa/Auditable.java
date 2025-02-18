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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   Auditable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Auditable.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

////////////////////////////////////////////////////////////////////////////////
// interface Auditable
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** The base entity that have auditable informations.
 **
 ** @param  <U>                  the type of the audtitable user value
 **                              implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 ** @param  <D>                  the type of the audtitable timestamp value
 **                              implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Auditable<U, D> extends Persistable {

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCreatedBy
  /**
   ** Set the property of an identity whom have created the
   ** <code>Auditable</code>.
   **
   ** @param  value              the property of an identity who created the
   **                            <code>Auditable</code>.
   **                            <br>
   **                            Allowed object is <code>U</code>.
   */
  void setCreatedBy(final U value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCreatedBy
  /**
   ** Returns the property of an identity whom created the
   ** <code>Auditable</code>.
   **
   ** @return                    the property of an identity who created the
   **                            <code>Auditable</code>.
   **                            <br>
   **                            Possible object is <code>U</code>.
   */
  U getCreatedBy();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCreatedOn
  /**
   ** Sets the <code>createdOn</code> property of the <code>Auditable</code> to
   ** the time the entity was created.
   **
   ** @param  value              the <code>createdOn</code> property of the
   **                            <code>Auditable</code>.
   **                            <br>
   **                            Allowed object is <code>D</code>.
   */
  void setCreatedOn(final D value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCreatedOn
  /**
   ** Returns the <code>createdOn</code> property of the <code>Auditable</code>
   ** as the time the entity was created.
   **
   ** @return                    the <code>createdOn</code> property of the
   **                            <code>Auditable</code>.
   **                            <br>
   **                            Possible object is <code>D</code>.
   */
  D getCreatedOn();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUpdatedBy
  /**
   ** Set the property of an identity whom last recently modified the
   ** <code>Auditable</code>.
   **
   ** @param  value              the property of an identity whom last recently
   **                            modified the <code>Auditable</code>.
   **                            <br>
   **                            Allowed object is <code>U</code>.
   */
  void setUpdatedBy(final U value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUpdatedBy
  /**
   ** Returns the property of an identity who last recently modified the
   ** <code>Auditable</code>.
   **
   ** @return                    the property of an identity who last recently
   **                            modified the <code>Auditable</code>.
   **                            <br>
   **                            Possible object is <code>U</code>.
   */
  U getUpdatedBy();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUpdatedOn
  /**
   ** Sets the <code>updatedOn</code> property of the
   ** <code>Auditable</code> to the time the entity was last recently modified.
   **
   ** @param  value              the <code>updatedOn</code> property of the
   **                            <code>Auditable</code>.
   **                            <br>
   **                            Allowed object is <code>D</code>.
   */
  void setUpdatedOn(final D value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUpdatedOn
  /**
   ** Returns the <code>updatedOn</code> property of the
   ** <code>Auditable</code> as the time the entity was last recently modified.
   **
   ** @return                    the <code>updatedOn</code> property of the
   **                            <code>Auditable</code>.
   **                            <br>
   **                            Possible object is <code>D</code>.
   */
  D getUpdatedOn();
}