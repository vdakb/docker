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

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   Versionable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Versionable.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

////////////////////////////////////////////////////////////////////////////////
// interface Versionable
// ~~~~~~~~~ ~~~~~~~~~~~
/**
 ** The base entity that identified by a version context.
 **
 ** @param  <V>                  the type of the version value implementation.
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
public interface Versionable<V> extends Persistable {

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Set the <code>version</code> property of the <code>Versionable</code>.
   **
   ** @param  value              the <code>version</code> property of the
   **                            <code>Versionable</code>.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   */
  void setVersion(final V value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVersion
  /**
   ** Returns the <code>version</code> property of the <code>Versionable</code>.
   **
   ** @return                    the <code>version</code> property of the
   **                            <code>Versionable</code>.
   **                            <br>
   **                            Possible object is <code>V</code>.
   */
  V getVersion();
}