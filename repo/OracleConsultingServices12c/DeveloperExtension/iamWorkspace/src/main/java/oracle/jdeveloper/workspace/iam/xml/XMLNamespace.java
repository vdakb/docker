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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   XMLNamespace.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLNamespace.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.xml;

import java.util.Iterator;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface XMLNamespace
// ~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>XMLNamespace</code> object is used store the namespaces for an
 ** element. Each namespace added to this map can be added with a prefix.
 ** <p>
 ** A prefix is added only if the associated reference has not been added to a
 ** parent element. If a parent element has the associated reference, then the
 ** parents prefix is the one that will be returned when requested from this
 ** map.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   12.2.1.3.42.60.74
 */
public interface XMLNamespace extends Iterable<String>
                              ,       Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8841249455524951072")
  static final long serialVersionUID = -6299902463048992449L;

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** This is the prefix that is associated with the source element.
   ** <p>
   ** If the source element does not contain a namespace reference then this
   ** will return its parents namespace. This ensures that if a namespace has
   ** been declared its child elements will inherit its prefix.
   **
   ** @return                    the prefix that is currently in scope.
   */
  String prefix();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** This acquires the prefix for the specified namespace reference.
   ** <p>
   ** If the namespace reference has been set on this node with a given prefix
   ** then that prefix is returned, however if it has not been set this will
   ** search the parent elements to find the prefix that is in scope for the
   ** specified reference.
   **
   ** @param  reference          the reference to find a matching prefix for.
   **
   ** @return                    the prefix that is is scope.
   */
  String prefix(final String reference);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** This is used to add the namespace reference to the namespace map.
   ** <p>
   ** If the namespace has been added to a parent node then this will not add
   ** the reference. The prefix added to the map will be the default namespace,
   ** which is an empty prefix.
   **
   ** @param  reference          the reference to be added.
   **
   ** @return                     the prefix that has been replaced.
   */
  String reference(final String reference);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** This is used to add the namespace reference to the namespace map.
   ** <p>
   ** If the namespace has been added to a parent node then this will not add
   ** the reference.
   **
   ** @param  reference          the reference to be added.
   ** @param  prefix             the prefix to be added to the reference.
   **
   ** @return                    the prefix that has been replaced.
   */
  String reference(final String reference, final String prefix);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** This acquires the namespace reference for the specified prefix.
   ** <p>
   ** If the provided prefix has been set on this node with a given reference
   ** then that reference is returned, however if it has not been set this will
   ** search the parent elements to find the reference that is in scope for the
   ** specified reference.
   **
   ** @param  prefix             the prefix to find a matching reference for.
   **
   ** @return                     the reference that is in scope.
   */
  String match(final String prefix);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator
  /**
   ** This returns an iterator for the namespace of all the nodes in this
   ** <code>NamespaceMap</code>.
   ** <p>
   ** This allows the namespaces to be iterated within a for each loop in order
   ** to extract the prefix values associated with the map.
   **
   ** @return                   the namespaces contained in this map.
   */
  Iterator<String> iterator();
}