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

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared XML Stream Facilities

    File        :   XMLNode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLNode.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface XMLNode
// ~~~~~~~~~ ~~~~~~~
/**
 ** The <code>XMLNode</code> is used to represent a name value pair and acts as
 ** the base form of data used within the framework.
 ** <p>
 ** Each of the attributes and elements are represented as nodes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface XMLNode extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4510646970763838429")
  static final long serialVersionUID = 7122849326196465567L;

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the node that this represents.
   ** <br>
   ** Returns the local name of an element or attribute, the prefix of a
   ** namespace node, the target of a processing instruction, or
   ** <code>null</code> for all other node types.
   ** <p>
   ** This is an immutable property and should not change for any node.
   **
   ** @return                    the local name of an element or attribute,
   **                            the prefix of a namespace node, the target of a
   **                            processing instruction, or <code>null</code>
   **                            for all other node types.
   */
  String name();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value for the node that this represents.
   ** <p>
   ** This is a modifiable property for the node and can be changed.
   **
   ** @return                    the value for this node instance.
   **
   ** @throws XMLException       if there is a problem getting the value.
   */
  String value()
    throws XMLException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** This is used to acquire the <code>XMLNode</code> that is the parent of
   ** this node or <code>null</code> if the node has no parent.
   ** <br>
   ** This method is valid on all node types except the root node. Attribute and
   ** namespace nodes have the element as their parent node.
   ** <p>
   ** This will return the node that is the direct parent of this node and
   ** allows for siblings to make use of nodes with their parents if required.
   **
   ** @return                    the parent node for this node.
   */
  XMLNode parent();
}